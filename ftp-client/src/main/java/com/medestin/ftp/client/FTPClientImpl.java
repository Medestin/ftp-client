package com.medestin.ftp.client;

import com.medestin.ftp.connection.CommandSocketManager;

public class FTPClientImpl implements FTPClient {
    private final static int COMMAND_PORT = 21;
    private final String hostname;
    private final CommandSocketManager commandSocket;

    public FTPClientImpl(String hostname) {
        this.hostname = hostname;
        this.commandSocket = new CommandSocketManager(hostname, COMMAND_PORT);
    }


    //TODO: Implement all below!

    @Override
    public boolean logIn(String username, String password) {
        return false;
    }

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
