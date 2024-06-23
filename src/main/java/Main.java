import Models.Request;
import RequestHandlers.RequestHandler;
import RequestHandlers.RequestHandlerFactory;
import Utils.RequestUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static final int NUMBER_OF_THREADS = 8;
    private static int currentClientID = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        try {
            serverSocket = new ServerSocket(4221);
            while (true) {
                serverSocket.setReuseAddress(true);
                clientSocket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                Request request = RequestUtils.constructRequest(reader);
                System.out.println(request.toString());
                executorService.submit(RequestHandlerFactory.getRequestHandler(clientSocket, request, args));
            }
        } catch (Exception e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (clientSocket != null) clientSocket.close();
            executorService.shutdown();
        }
    }
}
