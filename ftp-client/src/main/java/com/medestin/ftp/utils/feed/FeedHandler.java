package com.medestin.ftp.utils.feed;

import com.medestin.ftp.utils.logger.FileLogger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class FeedHandler implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(FeedHandler.class);
    private static final long AWAIT_TERMINATION_TIME_MILLIS = 5000;
    private static final long DEFAULT_DELAY_TIME_MILLIS = 100;

    private final Supplier<String> supplier;
    private final Consumer<String> consumer;
    private final ScheduledExecutorService executorService;

    public FeedHandler(Supplier<String> supplier, Consumer<String> consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        startExecutor();
    }

    private void startExecutor() {
        logger.info("Starting executor service reading from supplier");
        executorService.scheduleAtFixedRate(this::run, 0L, DEFAULT_DELAY_TIME_MILLIS, MILLISECONDS);
    }

    private void run() {
        String line = supplier.get();
        if (line != null && !line.equals("")) {
            consumer.accept(line);
        }
    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination(AWAIT_TERMINATION_TIME_MILLIS, MILLISECONDS);
        logger.info("Shut down executor service");
    }
}
