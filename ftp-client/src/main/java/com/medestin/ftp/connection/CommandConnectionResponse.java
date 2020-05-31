package com.medestin.ftp.connection;

public class CommandConnectionResponse {
    public final int code;
    public final String message;

    public CommandConnectionResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
