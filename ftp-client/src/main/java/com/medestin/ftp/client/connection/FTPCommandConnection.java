package com.medestin.ftp.client.connection;

import com.medestin.ftp.client.model.ProtocolCommands;
import com.medestin.ftp.utils.feed.FeedHandler;
import com.medestin.ftp.utils.logger.FileLogger;
import com.medestin.ftp.utils.socket.SocketConnectionException;
import com.medestin.ftp.utils.socket.SocketManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import static com.medestin.ftp.client.model.ProtocolCommands.PASS;
import static com.medestin.ftp.client.model.ProtocolCommands.USER;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Level.SEVERE;

public class FTPCommandConnection implements AutoCloseable {
    private final Logger logger = FileLogger.getLogger(FTPCommandConnection.class);
    private static final int DEFAULT_PORT = 21;
    private static final int QUEUE_CAPACITY = 5;
    private static final long QUEUE_POLL_TIMEOUT_MILLIS = 150;

    private SocketManager commandSocket;
    private FeedHandler feed;
    private final BlockingQueue<String> responseQueue;

    public FTPCommandConnection() {
        this.responseQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    }

    public CommandResponse connect(String hostname) {
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

    public CommandResponse user(String username) {
        commandSocket.writeLine(String.join(" ", USER.command(), username));
        return fromLine(readAll());
    }

    public CommandResponse password(String password) {
        commandSocket.writeLine(String.join(" ", PASS.command(), password));
        return fromLine(readAll());
    }

    private CommandResponse retrieveWelcomeMessage() {
        return fromLine(readAll());
    }

    private CommandResponse fromLine(String line) {
        int code = Integer.parseInt(line.substring(0, 3));
        return new CommandResponse(code, line);
    }

    private String readAll() {
        List<String> lines = new ArrayList<>();
        String line = readLineOrWait();
        while (!"".equals(line)) {
            lines.add(line);
            line = readLineOrWait();
        }
        return String.join("\n", lines);
    }

    private String readLineOrWait() {
        try {
            String line = responseQueue.poll(QUEUE_POLL_TIMEOUT_MILLIS, MILLISECONDS);
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
