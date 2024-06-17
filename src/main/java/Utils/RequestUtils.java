package Utils;

import Models.Request;
import Models.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class RequestUtils {

    private static final String INVALID_RESOURCE = "HTTP/1.1 404 Not Found\r\n\r\n";
    private static final String CRNF = "\r\n";
    private static final String  USER_AGENT = "User-Agent";

    public static Request constructRequest(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while (!((line = reader.readLine()).isEmpty()) && line != null) {
            lines.add(line);
        }

        // for handling invalid requests
        if (lines.isEmpty()) return null;

        // first line forms the request line
        String requestLine = lines.get(0);

        Map<String, String> headers = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] headerSplits = lines.get(i).split(":");
            if (headerSplits.length == 2) {
                headers.put(headerSplits[0].trim(), headerSplits[1].trim());
            }
        }
        return new Request(requestLine, headers, null);
    }

    public static Response handleRequest(Request request) {
        if (request == null) return null;
        String path = getPath(request.getRequestLine());
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        if (path.equals("/")) {
            return new Response(200, null, null);
        } else if (isEcho(path)) {
            String responseString = getString(path);
            Response response = new Response(200, headers, responseString);
            response.addHeader("Content-Length", String.valueOf(responseString.length()));
            System.out.println(response);
            return response;
        } else if (isUserAgent(request.getHeaders())) {
            String responseString = request.getHeaders().get(USER_AGENT);
            Response response = new Response(200, headers, responseString);
            response.addHeader("Content-Length", String.valueOf(responseString.length()));
            System.out.println(response);
            return response;
        }else {
            Response response = new Response(404, null, null);
            return response;
        }
    }

    public static void printResponse(PrintWriter writer, Response response) {
        if (response == null || response.getStatusCode() == 404) {
            writer.println(INVALID_RESOURCE);
            return;
        }
        writer.print("HTTP/1.1 200 OK");
        writer.print(CRNF);
        if (response.getHeaders() != null) {
            for (Map.Entry<String, String> entrySet : response.getHeaders().entrySet()) {
                String header = entrySet.getKey() + ": " + entrySet.getValue();
                writer.print(header);
                writer.print(CRNF);
            }
        }
        writer.print(CRNF);
        if(response.getResponse() != null) writer.print(response.getResponse());
    }

    private static String getPath(String requestLine) {

        return requestLine.split(" ")[1];
    }

    private static boolean isEcho(String path) {
        if (Arrays.stream(path.split("/")).count() > 0) {
            return path.split("/")[1].equals("echo");
        }
        return false;
    }

    private static boolean isUserAgent(Map<String, String> headers) {
        return headers.containsKey(USER_AGENT);
    }

    private static String getString(String path) {
        return path.split("/")[2];
    }
}
