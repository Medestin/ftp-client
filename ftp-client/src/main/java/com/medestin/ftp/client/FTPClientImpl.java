package com.medestin.ftp.client;

import com.medestin.ftp.client.connection.ConnectionManager;
import com.medestin.ftp.client.connection.ServerResponse;

import java.io.PrintStream;
import java.util.Optional;

public class FTPClientImpl implements FTPClient {
    private final static int COMMAND_PORT = 21;
    private final PrintStream out;

    private final String hostname;
    private final ConnectionManager commandConnection;

    public FTPClientImpl(String hostname) {
        this.hostname = hostname;
        this.commandConnection = new ConnectionManager(hostname, COMMAND_PORT);
        this.out = System.out;
    }

    @Override
    public boolean logIn(String username, String password) {
        ServerResponse response = commandConnection.connect();
        if(response.getCode() == 220) {
            out.println("Successfully connected to server...");
            response.getMessage().ifPresent(out::println);
            return true;
        } else {
            return false;
        }
    }

    //TODO: Implement all below!

    @Override
    public String showDirectory() {
        return null;
    }

    @Override
    public String listElements() {
        return null;
    }

    @Override
    public boolean enterDirectory(String directory) {
        return false;
    }

    @Override
    public boolean copy(String elementName) {
        return false;
    }
}
