package com.medestin.ftp.client;

import com.medestin.ftp.client.connection.CommandResponse;
import com.medestin.ftp.client.connection.FTPCommandConnection;
import com.medestin.ftp.utils.logger.FileLogger;

import java.io.PrintStream;
import java.util.logging.Logger;

import static com.medestin.ftp.client.model.ResponseCode.LOGGED_IN;
import static com.medestin.ftp.client.model.ResponseCode.NEED_PASSWORD;

public class FTPClient implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(FTPClient.class);
    private final PrintStream out;

    private final FTPCommandConnection commandConnection;

    public FTPClient() {
        this.out = System.out;
        this.commandConnection = new FTPCommandConnection();
    }

    public FTPClient(PrintStream out) {
        this.out = out;
        this.commandConnection = new FTPCommandConnection();
    }

    public void connect(String hostname) {
        CommandResponse response = commandConnection.connect(hostname);
        out.println(response.message);
    }

    public void logIn(String username, String password) {
        CommandResponse response = commandConnection.user(username);
        if (response.code == NEED_PASSWORD.code()) {
            response = commandConnection.password(password);
            if (response.code == LOGGED_IN.code()) {
                out.println("Logged in!");
            }
        }
    }

    public void currentLocation() {
        out.println(commandConnection.directory().message);
    }

    public void list() {
        enterPassiveMode();
        out.println(commandConnection.list());
    }

    public void retrieve(String filename) {
        enterPassiveMode();
        out.println(commandConnection.retrieve(filename));
    }

    private void enterPassiveMode() {
        out.println(commandConnection.passiveMode());
    }

    @Override
    public void close() throws Exception {
        commandConnection.close();
        logger.info("Closed command connection");
    }
}
