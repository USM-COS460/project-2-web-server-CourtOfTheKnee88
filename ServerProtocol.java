import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Locale;

public class ServerProtocol implements Runnable {
    private String documentRoot;
    private Socket clientSocket;
    private static final String SERVER_NAME = "CourtneysHTTPServer/1.0";
    private static final DateTimeFormatter DATE = DateTimeFormatter
            .ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    public ServerProtocol(Socket clientSocket, String documentRoot) {
        this.clientSocket = clientSocket;
        this.documentRoot = documentRoot;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputStream = clientSocket.getOutputStream()) {

            String inputLine = in.readLine();
            if (inputLine != null) {
                processAndSendResponse(inputLine, outputStream);
            }

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public void processAndSendResponse(String input, OutputStream outputStream) {
        try {
            // Parse HTTP GET request
            if (input == null || !input.startsWith("GET")) {
                sendErrorResponse(outputStream, "400 Bad Request", "Bad Request");
                return;
            }

            String[] parts = input.split(" ");
            if (parts.length < 2) {
                sendErrorResponse(outputStream, "400 Bad Request", "Bad Request");
                return;
            }

            String requestedPath = parts[1];

            if ("/".equals(requestedPath)) {
                requestedPath = "/index.html";
            }

            if (!requestedPath.startsWith("/")) {
                sendErrorResponse(outputStream, "400 Bad Request", "Bad Request");
                return;
            }

            Path filePath = Paths.get(documentRoot + requestedPath).normalize();

            // Check if file exists
            if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
                sendErrorResponse(outputStream, "404 Not Found", "File Not Found");
                return;
            }

            byte[] fileContent = Files.readAllBytes(filePath);
            String contentType = getContentType(filePath.toString());

            // Build and send HTTP response
            StringBuilder headers = new StringBuilder();
            headers.append("HTTP/1.1 200 OK\r\n");
            headers.append("Date: ").append(ZonedDateTime.now(ZoneId.of("GMT")).format(DATE))
                    .append("\r\n");
            headers.append("Server: ").append(SERVER_NAME).append("\r\n");
            headers.append("Content-Type: ").append(contentType).append("\r\n");
            headers.append("Content-Length: ").append(fileContent.length).append("\r\n");
            headers.append("\r\n");

            outputStream.write(headers.toString().getBytes("UTF-8"));
            outputStream.write(fileContent);
            outputStream.flush();

        } catch (IOException e) {
            System.err.println("Error processing request: " + e.getMessage());
            try {
                sendErrorResponse(outputStream, "500 Internal Server Error", "Internal Server Error");
            } catch (IOException ex) {
                System.err.println("Error sending error response: " + ex.getMessage());
            }
        }
    }

    private void sendErrorResponse(OutputStream outputStream, String status, String message) throws IOException {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(status).append("\r\n");
        response.append("Date: ").append(ZonedDateTime.now(ZoneId.of("GMT")).format(DATE)).append("\r\n");
        response.append("Server: ").append(SERVER_NAME).append("\r\n");
        response.append("Content-Type: text/html\r\n");
        response.append("Content-Length: ").append(message.length()).append("\r\n");
        response.append("\r\n");
        response.append(message);

        outputStream.write(response.toString().getBytes("UTF-8"));
        outputStream.flush();
    }

    private String getContentType(String filePath) {
        if (filePath.endsWith(".html") || filePath.endsWith(".htm")) {
            return "text/html";
        } else if (filePath.endsWith(".css")) {
            return "text/css";
        } else if (filePath.endsWith(".js")) {
            return "application/javascript";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filePath.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "text/plain";
        }
    }
}