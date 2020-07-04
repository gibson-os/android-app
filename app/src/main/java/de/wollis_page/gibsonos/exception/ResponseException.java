package de.wollis_page.gibsonos.exception;

import org.json.JSONObject;

public class ResponseException extends Exception
{
    private final String message;
    private final JSONObject response;
    private final Integer code;

    public ResponseException(String message, JSONObject response, Integer code) {
        this.message = message;
        this.response = response;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject getResponse() {
        return response;
    }
}
