package com.medestin.ftp.connection;

import com.medestin.ftp.utils.feed.FeedHandler;
import com.medestin.ftp.utils.logger.FileLogger;
import com.medestin.ftp.utils.socket.SocketConnectionException;
import com.medestin.ftp.utils.socket.SocketManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Level.SEVERE;

public class FTPCommandConnection implements AutoCloseable {
    private final Logger logger = FileLogger.getLogger(FTPCommandConnection.class);
    private static final int DEFAULT_PORT = 21;
    private static final int QUEUE_CAPACITY = 5;

    private final SocketManager commandSocket;
    private final FeedHandler feed;
    private final BlockingQueue<String> responseQueue;

    public FTPCommandConnection(String localhost) throws SocketConnectionException {
        this.commandSocket = new SocketManager(localhost, DEFAULT_PORT);
        this.feed = new FeedHandler(commandSocket::readLine, this::consumeFeed);
        this.responseQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    }

    public String readLineOrWait() {
        try {
            String line = responseQueue.poll(200, MILLISECONDS);
            return line != null ? line : "";
        } catch (InterruptedException e) {
            String errorMessage = "Exception while reading message from queue";
            FTPCommandConnectionException ftpCommandConnectionException = new FTPCommandConnectionException(errorMessage, e);
            logger.log(SEVERE, errorMessage, ftpCommandConnectionException);
            throw ftpCommandConnectionException;
        }
    }

    private void consumeFeed(String feed) {
        try {
            responseQueue.put(feed);
        } catch (InterruptedException e) {
            String errorMessage = String.format("Exception while putting message '%s' in queue", feed);
            FTPCommandConnectionException ftpCommandConnectionException = new FTPCommandConnectionException(errorMessage, e);
            logger.log(SEVERE, errorMessage, ftpCommandConnectionException);
            throw ftpCommandConnectionException;
        }
    }

    @Override
    public void close() throws Exception {
        feed.close();
        commandSocket.close();
    }
}
