package com.medestin.ftp.client.connection;

import com.medestin.ftp.client.connection.socket.SocketManager;

public class ConnectionManager {
    private final String hostname;
    private final int port;
    private SocketManager socketManager;

    public ConnectionManager(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public ServerResponse connect() throws ConnectionManagerException {
        if(socketManager == null) {
            this.socketManager = new SocketManager(hostname, port);
            return drainWelcomeMessage();
        } else {
            throw new ConnectionManagerException("Already connected!");
        }
    }

    private ServerResponse drainWelcomeMessage() {
        StringBuilder sb = new StringBuilder();
        String line = socketManager.receive();
        int code = 0;
        if (line != null) {
            code = Integer.parseInt(line.substring(0, 3));
            sb.append(line);
            line = socketManager.receive();
            while (line != null) {
                sb.append("\n".concat(line));
                line = socketManager.receive();
            }
        }
        return new ServerResponse.ServerResponseBuilder()
                .code(code)
                .message(sb.toString())
                .build();
    }

}
