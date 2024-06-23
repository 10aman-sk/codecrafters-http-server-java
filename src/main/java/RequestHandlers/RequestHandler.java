package RequestHandlers;

import Models.Request;
import Models.Response;
import Utils.RequestUtils;

import java.io.*;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;

public class RequestHandler implements Runnable {
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
            OutputStream outputStream = clientSocket.getOutputStream();
            Response response = RequestUtils.handleRequest(request);
            if (RequestUtils.isEcho(RequestUtils.getPath(request.getRequestLine())) && RequestUtils.containsGZIPHeader(request.getHeaders().get("Accept-Encoding"))) {
                String echoStr = response.getResponse();
                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream();
                try (GZIPOutputStream gzipOutputStream =
                             new GZIPOutputStream(byteArrayOutputStream)) {
                    gzipOutputStream.write(response.getResponse().getBytes("UTF-8"));
                }
                byte[] gzipData = byteArrayOutputStream.toByteArray();
                String httpResponse =
                        "HTTP/1.1 200 OK\r\nContent-Encoding: gzip\r\nContent-Type: text/plain\r\nContent-Length: " +
                gzipData.length + "\r\n\r\n";
                outputStream.write(httpResponse.getBytes("UTF-8"));
                outputStream.write(gzipData);
            } else {
                RequestUtils.printResponse(writer, response);
            }
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
