package com.medestin.ftp.client;

import com.medestin.ftp.client.connection.CommandConnectionResponse;
import com.medestin.ftp.client.connection.FTPCommandConnection;
import com.medestin.ftp.utils.logger.FileLogger;

import java.io.PrintStream;
import java.util.logging.Logger;

public class FTPClient implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(FTPClient.class);
    private final PrintStream out;

    private final FTPCommandConnection commandConnection;

    public FTPClient() {
        this.out = System.out;
        this.commandConnection = new FTPCommandConnection();
    }

    public void connect(String hostname) {
        CommandConnectionResponse response = commandConnection.connect(hostname);
        out.println(response.message);
    }

    @Override
    public void close() throws Exception {
        commandConnection.close();
        logger.info("Closed command connection");
    }
}
