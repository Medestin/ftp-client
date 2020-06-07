package com.medestin.ftp.client.connection;

public class CommandResponse {
    public final int code;
    public final String message;
    public final String passivePayload;

    public CommandResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.passivePayload = null;
    }

    public CommandResponse(int code, String message, String passivePayload) {
        this.code = code;
        this.message = message;
        this.passivePayload = passivePayload;
    }

    @Override
    public String toString() {
        return "CommandResponse[code: " + code + ", message: " + message +
                (passivePayload == null ? "]" : ",\npayload: " + passivePayload + "]");
    }
}
