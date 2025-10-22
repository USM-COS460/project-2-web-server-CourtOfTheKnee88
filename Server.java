import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Server {

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Usage: java Server <port number> <document root>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        String documentRoot = args[1];

        File docRootFile = new File(documentRoot);
        if (!docRootFile.exists() || !docRootFile.isDirectory()) {
            System.err.println("Error: Document root '" + documentRoot + "' does not exist or is not a directory");
            System.exit(1);
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ServerProtocol(clientSocket, documentRoot));
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}