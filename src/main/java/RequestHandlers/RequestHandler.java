package RequestHandlers;

import Models.Request;
import Models.Response;
import Utils.RequestUtils;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable{
    protected int clientId;
    protected Socket clientSocket;
    protected Request request;

    public RequestHandler(int clientId, Socket clientSocket, Request request) {
        this.clientId = clientId;
        this.clientSocket = clientSocket;
        this.request = request;
    }

    @Override
    public void run() {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream());
            Response response = RequestUtils.handleRequest(request);
            System.out.println(response);
            RequestUtils.printResponse(writer, response);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
                clientSocket.close();
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
