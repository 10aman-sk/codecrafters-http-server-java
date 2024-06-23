package RequestHandlers;

import Models.Request;
import Utils.RequestUtils;

import java.net.Socket;

import static Utils.RequestUtils.isFile;

public class RequestHandlerFactory {
    public static RequestHandler getRequestHandler(Socket socket, Request request, String[] args) {
        if(isFile(request.getRequestLine())) {
            return new FileRequestHandlerImpl(0, socket, request, args[1]);
        }
        return new RequestHandler(0, socket, request);
    }

}
