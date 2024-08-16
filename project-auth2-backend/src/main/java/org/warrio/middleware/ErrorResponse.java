package org.warrio.middleware;

import java.util.Map;

public class ErrorResponse {
    public int status;
    public String message;
    public Map<String, String> details;
    public ErrorResponse(int status, String message, Map<String, String> details){
        this.message = message;
        this.status = status;
        this.details = details;
    }
}
