package Models;

import java.util.Map;

public class Request {
    private String requestLine;
    private Map<String, String> headers;
    private String body;

    public Request(String requestLine, Map<String, String> headers, String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestLine='" + requestLine + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
