package com.medestin.ftp.client.model;

public enum ProtocolCommands {

    USER("USER"),
    PASS("PASS");

    private final String command;

    ProtocolCommands(String command) {
        this.command = command;
    }

    public String command() {
        return command;
    }
}
