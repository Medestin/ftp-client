package com.medestin.ftp.client.client;

import com.medestin.ftp.client.connection.SocketConnection;
import com.medestin.ftp.client.feed.Feed;

public class Client implements AutoCloseable {
    private final SocketConnection connection;
    private final Feed feed;

    public Client(String hostname, int port) {
        this.connection = new SocketConnection(hostname, port);
        this.feed = new Feed(connection);
    }

    public void connect() {
        connection.connect();
        feed.startFeed();
    }

    public void write(String message) {
        connection.write(message);
    }

    @Override
    public void close() throws Exception {
        feed.close();
        connection.close();
    }
}
