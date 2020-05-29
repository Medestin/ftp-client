package com.medestin.ftp.client.utils.stream.reader;

import com.medestin.ftp.client.utils.logger.FileLogger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class PeriodicStringFeed implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(PeriodicStringFeed.class);
    private static final String STARTED_FEED_MESSAGE = "Created & started and executor service feed";
    private static final String CLASS_CLOSED = PeriodicStringFeed.class.getCanonicalName().concat(" closed.");
    private static final String FEED_ALREADY_RUNNING = "Tried to start feed but it is already running";
    private static final long DELAY = 100L;

    private final ScheduledExecutorService executor;
    private boolean isRunning;

    public PeriodicStringFeed() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void feed(Supplier<String> supplier, Consumer<String> consumer) {
        if(!isRunning) {
            executor.scheduleAtFixedRate(runnable(supplier, consumer), 0L, DELAY, MILLISECONDS);
            logger.info(STARTED_FEED_MESSAGE);
            isRunning = true;
        } else {
            consumer.accept(FEED_ALREADY_RUNNING);
            logger.info(FEED_ALREADY_RUNNING);
        }
    }

    private Runnable runnable(Supplier<String> supplier, Consumer<String> consumer) {
        return () -> {
            String string = supplier.get();
            if (string != null && !"".equalsIgnoreCase(string)) {
                consumer.accept(string);
            }
        };
    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
        executor.awaitTermination(5, SECONDS);
        logger.info(CLASS_CLOSED);
    }
}
