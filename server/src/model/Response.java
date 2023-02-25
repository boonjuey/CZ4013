package model;

import java.util.HashMap;

public class Response {
    private HashMap<String, Object> responseBody;

    public Response(HashMap<String, Object> responseBody) {
        this.responseBody = responseBody;
    }

    public HashMap<String, Object> getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(HashMap<String, Object> responseBody) {
        this.responseBody = responseBody;
    }

    public void addResponseBody(String key, Object value) {
        responseBody.put(key, value);
    }

    public Object getResponseBody(String key) {
        return responseBody.get(key);
    }
}
