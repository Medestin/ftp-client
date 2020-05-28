package com.medestin.ftp.client.connection;

import com.medestin.ftp.client.logger.FileLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketConnection implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(SocketConnection.class.getName());
    private final String hostname;
    private final int port;

    private final Socket socket;
    private BufferedReader reader;
    private OutputStreamWriter writer;

    public SocketConnection(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.socket = new Socket();
    }

    public String readMessage() {
        if (socket.isConnected()) {
            try {
                if(reader.ready()){
                    return reader.readLine();
                } else {
                    return "";
                }
            } catch (IOException e) {
                throw new SocketConnectionException("Could not read message", e);
            }
        } else {
            throw new SocketConnectionException("Socket not connected!");
        }
    }

    public void write(String message) {
        if (socket.isConnected()) {
            logger.info("Writing message: \"".concat(message).concat("\""));
            try {
                writer.write(message.concat(System.lineSeparator()));
                writer.flush();
            } catch (IOException e) {
                throw new SocketConnectionException("Could not write message", e);
            }
        } else {
            throw new SocketConnectionException("Socket not connected!");
        }
    }

    public void connect() {
        if(!socket.isConnected()) {
            try {
                socket.connect(new InetSocketAddress(hostname, port));
                this.writer = new OutputStreamWriter(socket.getOutputStream());
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                throw new SocketConnectionException(
                        String.format("Exception while connecting to %s:%s", hostname, port), e);
            }
            logger.info(String.format("Established connection on %s:%s", hostname, port));
        } else {
            logger.warning("Socket is already connected");
        }
    }

    @Override
    public void close() throws Exception {
        reader.close();
        writer.close();
        socket.close();
        logger.info(String.format("Connection %s has been closed", socket.toString()));
    }
}
