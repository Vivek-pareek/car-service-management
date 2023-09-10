package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseObj {
    private static final long serialVersionUID = -1L;
    private int httpStatus;
    private boolean success;
    private String message;
    private Object data;

    // Default constructor
    public ResponseObj() {
    }

    public ResponseObj(int httpStatus, boolean success, String message, Object data) {
        this.httpStatus = httpStatus;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

