import java.net.*;
import java.io.*;

public class ServerProtocol1 {
    private String documentRoot;

    public ServerProtocol1() {
        System.out.println("User has connected");
    }

    public String processInput(String documentRoot) {
        this.documentRoot = documentRoot;
        if (documentRoot == null) {
            return "Hello! You are connected to the server. Type 'exit' to disconnect.";
        } else if (documentRoot.equalsIgnoreCase("exit")) {
            return "Bye.";
        } else {
            return "You said: " + documentRoot;
        }
    }
}