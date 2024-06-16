import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            serverSocket = new ServerSocket(4221);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept(); // Wait for connection from client.
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String[] requestLine = reader.readLine().split(" ");
            String path = requestLine[1];
            if (path.equals("/")) {
                writer.println("HTTP/1.1 200 OK\r\n\r\n");
            } else {
                writer.println("HTTP/1.1 404 Not Found\r\n\r\n");
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            if (writer != null) writer.close();
            if (clientSocket != null) clientSocket.close();
        }
    }
}
