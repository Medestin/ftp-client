package com.medestin.ftp.utils.socket;

import com.medestin.ftp.utils.logger.FileLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

public class SocketManager implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(SocketManager.class);
    private static final String COULD_NOT_CONNECT = "Could not connect socket to %s:%s";
    private static final String COULD_NOT_SET_COMMS = "Could not set reader/writer for socket %s";
    private static final String EXCEPTION_WHILE_READING = "Exception thrown while reading from socket %s";

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public SocketManager(String hostname, int port) throws SocketConnectionException {
        try {
            socket = new Socket(hostname, port);
        } catch (IOException e) {
            String errorMessage = String.format(COULD_NOT_CONNECT, hostname, port);
            SocketConnectionException socketConnectionException = new SocketConnectionException(errorMessage, e);
            logger.log(WARNING, errorMessage, socketConnectionException);
            throw socketConnectionException;
        }
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            String errorMessage = String.format(COULD_NOT_SET_COMMS, socket);
            SocketManagerException exception = new SocketManagerException(errorMessage, e);
            logger.log(SEVERE, errorMessage, exception);
            throw exception;
        }
    }

    public String readLine() {
        try {
            if (reader.ready()) {
                return reader.readLine();
            } else {
                return "";
            }
        } catch (IOException e) {
            String errorMessage = String.format(EXCEPTION_WHILE_READING, socket);
            SocketManagerException exception = new SocketManagerException(errorMessage, e);
            logger.log(SEVERE, errorMessage, exception);
            throw exception;
        }
    }

    public void writeLine(String message) {
        writer.println(message);
        writer.flush();
    }

    @Override
    public void close() throws Exception {
        socket.close();
        reader.close();
        writer.close();
    }
}
