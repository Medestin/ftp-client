package com.medestin.ftp.client.connection;

public class CommandResponse {
    public final int code;
    public final String message;

    public CommandResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CommandResponse[code: " + code +
                ", message: " + message + "]";
    }
}
