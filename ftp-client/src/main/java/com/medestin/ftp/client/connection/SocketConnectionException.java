package com.medestin.ftp.client.connection;

public class SocketConnectionException extends RuntimeException{
    public SocketConnectionException(String message) {
        super(message);
    }

    public SocketConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
