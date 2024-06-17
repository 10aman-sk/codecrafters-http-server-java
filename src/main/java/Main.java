import Models.Request;
import Models.Response;
import Utils.RequestUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept();
            writer = new PrintWriter(clientSocket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Request request = RequestUtils.constructRequest(reader);
            System.out.println(request.toString());
            Response response = RequestUtils.handleRequest(request);
            RequestUtils.printResponse(writer, response);

        } catch (Exception e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (clientSocket != null) clientSocket.close();
        }
    }
}
