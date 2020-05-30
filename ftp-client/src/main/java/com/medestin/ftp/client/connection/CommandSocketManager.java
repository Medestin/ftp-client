package com.medestin.ftp.client.connection;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class CommandSocketManager {

    private final Socket socket;
    private final SocketAddress address;

    public CommandSocketManager(String hostname, int commandPort) {
        this.socket = new Socket();
        this.address = new InetSocketAddress(hostname, commandPort);
    }
}
