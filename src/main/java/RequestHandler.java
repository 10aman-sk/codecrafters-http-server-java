import Models.Request;
import Models.Response;
import Utils.RequestUtils;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable{
    private int clientId;
    private Socket clientSocket;

    public RequestHandler(int clientId, Socket clientSocket) {
        this.clientId = clientId;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Request request = RequestUtils.constructRequest(reader);
            System.out.println(request.toString());
            Response response = RequestUtils.handleRequest(request);
            System.out.println(response);
            RequestUtils.printResponse(writer, response);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
