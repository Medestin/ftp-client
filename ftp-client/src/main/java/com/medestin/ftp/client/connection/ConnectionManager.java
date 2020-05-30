package com.medestin.ftp.client.connection;

import com.medestin.ftp.client.connection.ServerResponse.ServerResponseBuilder;
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
            return receiveWelcomeMessage();
        } else {
            throw new ConnectionManagerException("Already connected!");
        }
    }

    public ServerResponse sendAndReceive(String message) {
        socketManager.send(message.concat(System.lineSeparator()));
        return receive();
    }

    private ServerResponse receive() {
        String line = socketManager.receive();
        if(line != null && line.length() >= 3) {
            ServerResponseBuilder builder = new ServerResponseBuilder();
            builder.code(Integer.parseInt(line.substring(0, 3)));
            builder.message(line);
            return builder.build();
        } else {
            throw new ConnectionManagerException("Could not receive message");
        }
    }

    private ServerResponse receiveWelcomeMessage() {
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
        return new ServerResponseBuilder()
                .code(code)
                .message(sb.toString())
                .build();
    }

}
