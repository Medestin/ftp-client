package com.medestin.ftp.client.model;

public enum ProtocolCommands {

    USER("USER"),
    PASS("PASS"),
    PWD("PWD");

    private final String command;

    ProtocolCommands(String command) {
        this.command = command;
    }

    public String command() {
        return command;
    }
}
