package com.medestin.ftp.client.connection;

public class FTPCommandConnectionException extends RuntimeException {
    public FTPCommandConnectionException(String message) {
        super(message);
    }

    public FTPCommandConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
