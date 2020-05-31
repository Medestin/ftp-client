package com.medestin.ftp.client.model;

public enum ResponseCode {

    READY(220, "Service ready for new user");

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    private final int code;
    private final String description;

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    public ResponseCode getByCode(int code) {
        for (ResponseCode value : ResponseCode.values()) {
            if(value.code == code) {
                return value;
            }
        }
        return null;
    }

}
