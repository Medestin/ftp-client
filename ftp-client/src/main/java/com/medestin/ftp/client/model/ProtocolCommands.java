package com.medestin.ftp.client.model;

public enum ProtocolCommands {

    EPSV("EPSV"),
    MLSD("MLSD"),
    PASS("PASS"),
    PWD("PWD"),
    USER("USER");

    private final String command;

    ProtocolCommands(String command) {
        this.command = command;
    }

    public String command() {
        return command;
    }
}
