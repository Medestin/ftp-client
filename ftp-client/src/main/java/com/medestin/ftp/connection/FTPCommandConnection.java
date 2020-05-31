package com.medestin.ftp.connection;

import com.medestin.ftp.utils.feed.FeedHandler;
import com.medestin.ftp.utils.logger.FileLogger;
import com.medestin.ftp.utils.socket.SocketConnectionException;
import com.medestin.ftp.utils.socket.SocketManager;

import java.util.logging.Logger;

public class FTPCommandConnection implements AutoCloseable {
//    private final Logger logger = FileLogger.getLogger(FTPCommandConnection.class);
    private static final int DEFAULT_PORT = 21;

    private final SocketManager commandSocket;
    private final FeedHandler feed;

    public FTPCommandConnection(String localhost) throws SocketConnectionException {
        this.commandSocket = new SocketManager(localhost, DEFAULT_PORT);
        this.feed = new FeedHandler(commandSocket::readLine);
    }

    public String readResponse() {
        return feed.readLine();
    }

    @Override
    public void close() throws Exception {
        feed.close();
        commandSocket.close();
    }
}
