package com.medestin.ftp.client.connection.socket;

import com.medestin.ftp.utils.logger.FileLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

public final class SocketManager implements AutoCloseable{
    private static final Logger logger = FileLogger.getLogger(SocketManager.class);
    private static final String COULD_NOT_CONNECT = "Exception thrown while setting up connection to %s:%s";
    private static final String COULD_NOT_RECEIVE = "Exception caught while trying to retrieve message from socket";

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public SocketManager(String hostname, int port) throws SocketManagerException {
        try {
            this.socket = new Socket(hostname, port);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream());

        } catch (IOException e) {
            logger.log(WARNING, String.format(COULD_NOT_CONNECT, hostname, port), e);
            throw new SocketManagerException(String.format(COULD_NOT_CONNECT, hostname, port), e);
        }
    }

    public void send(String message) {
        writer.print(message);
        writer.flush();
    }

    public String receive() throws SocketManagerException {
        try {
            Thread.sleep(100);
            if(reader.ready()) {
                return reader.readLine();
            } else {
                return null;
            }
        } catch (IOException e) {
            logger.log(WARNING, COULD_NOT_RECEIVE, e);
            throw new SocketManagerException(COULD_NOT_RECEIVE, e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new SocketManagerException(COULD_NOT_RECEIVE, e);
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
        reader.close();
        writer.close();
    }
}
