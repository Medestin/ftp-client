package com.medestin.ftp.client.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Connection implements AutoCloseable {

    private final Socket socket;
    private final SocketAddress socketAddress;

    public Connection(String hostname, int port) {
        this.socket = new Socket();
        this.socketAddress = new InetSocketAddress(hostname, port);
    }

    public void connect() throws IOException {
        socket.connect(socketAddress);
    }

    @Override
    public void close() throws Exception {
        if (!socket.isClosed()) {
            socket.close();
        }
    }
}
