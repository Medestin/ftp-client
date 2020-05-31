package com.medestin.ftp.connection;

import com.medestin.ftp.client.FTPClientException;
import com.medestin.ftp.utils.feed.FeedHandler;
import com.medestin.ftp.utils.logger.FileLogger;
import com.medestin.ftp.utils.socket.SocketConnectionException;
import com.medestin.ftp.utils.socket.SocketManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Level.SEVERE;

public class FTPCommandConnection implements AutoCloseable {
    private final Logger logger = FileLogger.getLogger(FTPCommandConnection.class);
    private static final int DEFAULT_PORT = 21;
    private static final int QUEUE_CAPACITY = 5;
    private static final long SLEEP_TIME_MILLIS = 120;

    private SocketManager commandSocket;
    private FeedHandler feed;
    private final BlockingQueue<String> responseQueue;

    public FTPCommandConnection() {
        this.responseQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    }

    public CommandConnectionResponse connect(String hostname) {
        try {
            close();
        } catch (Exception e) {
            String errorMessage = "Exception thrown while closing resources";
            FTPCommandConnectionException ftpCommandConnectionException = new FTPCommandConnectionException(errorMessage, e);
            logger.log(SEVERE, errorMessage, ftpCommandConnectionException);
            throw ftpCommandConnectionException;
        }
        try {
            commandSocket = new SocketManager(hostname, DEFAULT_PORT);
            this.feed = new FeedHandler(commandSocket::readLine, this::consumeFeed);
            logger.info(format("Successfully connected to %s", hostname));
            return retrieveWelcomeMessage();
        } catch (SocketConnectionException e) {
            String errorMessage = format("Failed trying to connect to %s", hostname);
            logger.warning(errorMessage);
            throw new FTPCommandConnectionException(errorMessage);
        }
    }

    private CommandConnectionResponse retrieveWelcomeMessage() {
        StringBuilder sb = new StringBuilder();
        String line = readLineOrWait();
        int code = Integer.parseInt(line.substring(0, 3));
        if (code == 220) {
            sb.append(line);
            String peek = checkForResponses();
            while (!"".equals(peek) && Integer.parseInt(peek.substring(0, 3)) == 220) {
                line = readLineOrWait();
                sb.append("\n").append(line);
                peek = checkForResponses();
            }
            return new CommandConnectionResponse(code, sb.toString());
        } else {
            String errorMessage = format("Received '%s' code while connecting", code);
            logger.severe(errorMessage);
            throw new FTPClientException(errorMessage);
        }
    }


    private String readLineOrWait() {
        try {
            String line = responseQueue.poll(150, MILLISECONDS);
            return line != null ? line : "";
        } catch (InterruptedException e) {
            String errorMessage = "Exception while reading message from queue";
            FTPCommandConnectionException ftpCommandConnectionException = new FTPCommandConnectionException(errorMessage, e);
            logger.log(SEVERE, errorMessage, ftpCommandConnectionException);
            throw ftpCommandConnectionException;
        }
    }

    private String checkForResponses() {
        String peek = responseQueue.peek();
        if (peek == null) {
            trySleep();
        }
        peek = responseQueue.peek();
        return peek != null ? peek : "";
    }

    private void trySleep() {
        try {
            Thread.sleep(SLEEP_TIME_MILLIS);
        } catch (InterruptedException e) {
            String errorMessage = "Exception thrown while sleeping";
            logger.severe(errorMessage);
            throw new FTPCommandConnectionException(errorMessage);
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
        if (feed != null) {
            feed.close();
            logger.info("Closed feed");
        }
        if (commandSocket != null) {
            commandSocket.close();
            logger.info("Closed command socket");
        }
    }
}
