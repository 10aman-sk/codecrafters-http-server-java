package RequestHandlers;

import Models.Request;
import Models.Response;
import Utils.RequestUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileRequestHandlerImpl extends RequestHandler {
    private final String directory;

    public FileRequestHandlerImpl(int clientId, Socket clientSocket, Request request, String directory) {
        super(clientId, clientSocket, request);
        this.directory = directory;
    }

    @Override
    public void run() {
        String httpCallType = RequestUtils.getHttpMethod(request.getRequestLine());
        if(httpCallType.equals("GET")) {
            processGetRequest();
            return;
        }
        processPutRequest();
    }

    private void processPutRequest() {
        String requestPath = RequestUtils.getPath(request.getRequestLine());
        String absoluteFilePath = directory + RequestUtils.getString(requestPath);
        Path filePath = Path.of(absoluteFilePath);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream());
            // write content to file
            System.out.println(request);
            try {
                Files.writeString(filePath, request.getBody());
            } catch (Exception e) {
                System.out.println(e);
            }
            Response response = new Response(201, null, null, "Created");
            RequestUtils.printResponse(writer, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processGetRequest() {
        String requestPath = RequestUtils.getPath(request.getRequestLine());
        String absoluteFilePath = directory + RequestUtils.getString(requestPath);
        Path filePath = Path.of(absoluteFilePath);
        Map<String, String> headers = new HashMap<>();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream());
            if (!Files.exists(filePath)) {
                Response response = new Response(404, null, null);
                System.out.println(response  + "file not found");
                RequestUtils.printResponse(writer, response);
                return;
            }
            String responseString = Files.readString(filePath);
            Response response = new Response(200, headers, responseString);
            response.addHeader("Content-Length", String.valueOf(responseString.length()));
            response.addHeader("Content-Type", "application/octet-stream");
            System.out.println(response);
            RequestUtils.printResponse(writer, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
