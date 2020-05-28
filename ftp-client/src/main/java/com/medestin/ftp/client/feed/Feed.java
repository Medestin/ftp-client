package com.medestin.ftp.client.feed;

import com.medestin.ftp.client.connection.SocketConnection;
import com.medestin.ftp.client.logger.FileLogger;

import java.io.PrintStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.*;

public class Feed implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(Feed.class);
    private static final long INITIAL_DELAY = 0L;
    private static final long DELAY = 100L;

    private final SocketConnection connection;
    private final PrintStream out;
    private final ScheduledExecutorService executor;

    public Feed(SocketConnection connection) {
        this.connection = connection;
        this.out = System.out;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void startFeed() {
        logger.info("Starting feed...");
        executor.scheduleAtFixedRate(feedRunnable(), INITIAL_DELAY, DELAY, MILLISECONDS);
    }

    private Runnable feedRunnable() {
        return () -> {
                String line = connection.readMessage();
                if(!"".equals(line)) {
                    out.println(line);
                    logger.info(line);
                }
        };
    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
        executor.awaitTermination(5, SECONDS);
        logger.info("Feed closed");
    }
}
