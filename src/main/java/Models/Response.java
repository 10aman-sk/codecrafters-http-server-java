package Models;

import java.util.Map;

public class Response {
    int statusCode;
    Map<String, String> headers;
    String response;

    public Response(int statusCode, Map<String, String> headers, String response) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.response = response;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "Response{" +
                "statusCode=" + statusCode +
                ", headers=" + headers +
                ", response='" + response + '\'' +
                '}';
    }
}
