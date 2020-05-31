package com.medestin.ftp.client;

import com.medestin.ftp.connection.FTPCommandConnection;
import com.medestin.ftp.utils.logger.FileLogger;
import com.medestin.ftp.utils.socket.SocketConnectionException;

import java.io.PrintStream;
import java.util.logging.Logger;

import static java.lang.String.format;

public class FTPClient implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(FTPClient.class);
    private final PrintStream out;

    private String hostname;
    private FTPCommandConnection commandConnection;

    public FTPClient() {
        this.out = System.out;
    }

    public boolean connect(String hostname) {
        this.hostname = hostname;
        try {
            this.commandConnection = new FTPCommandConnection(hostname);
            logger.info(format("Successfully connected to %s", hostname));
            retrieveWelcomeMessage();
            return true;
        } catch (SocketConnectionException e) {
            String errorMessage = format("Failed trying to connect to %s", hostname);
            logger.warning(errorMessage);
            return false;
        }
    }

    private void retrieveWelcomeMessage() {
        String line = commandConnection.readLineOrWait();
        int code = Integer.parseInt(line.substring(0, 3));
        if (code == 220) {
            out.println(line);
            String peek = commandConnection.checkForResponses();
            while (!"".equals(peek) && Integer.parseInt(peek.substring(0, 3)) == 220) {
                line = commandConnection.readLineOrWait();
                out.println(line);
                peek = commandConnection.checkForResponses();
            }
        } else {
            String errorMessage = format("Received '%s' code while connecting to %s", code, hostname);
            logger.severe(errorMessage);
            throw new FTPClientException(errorMessage);
        }
    }

    @Override
    public void close() throws Exception {
        if (commandConnection != null) {
            commandConnection.close();
        }
    }
}
