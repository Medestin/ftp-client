package com.medestin.ftp.utils.feed;

import com.medestin.ftp.utils.logger.FileLogger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Level.SEVERE;

public class FeedHandler implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(FeedHandler.class);
    private static final long AWAIT_TERMINATION_TIME_MILLIS = 5000;
    private static final long DELAY_TIME_MILLIS = 100;
    private static final long AWAIT_MESSAGE_TIME_MILLIS = 300;
    private static final int QUEUE_CAPACITY = 5;

    private final Supplier<String> supplier;
    private final ScheduledExecutorService executorService;
    private final BlockingQueue<String> queue;

    public FeedHandler(Supplier<String> supplier) {
        this.supplier = supplier;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        startExecutor();
    }

    public String readLine() {
        try {
            String line = queue.poll(AWAIT_MESSAGE_TIME_MILLIS, MILLISECONDS);
            return line == null ? "" : line;
        } catch (InterruptedException e) {
            String errorMessage = "Exception thrown while trying to poll element from queue";
            FeedHandlerException feedHandlerException = new FeedHandlerException(errorMessage, e);
            logger.log(SEVERE, errorMessage, feedHandlerException);
            throw feedHandlerException;
        }
    }

    private void startExecutor() {
        logger.info("Starting executor service reading from supplier");
        executorService.scheduleAtFixedRate(this::run, 0L, DELAY_TIME_MILLIS, MILLISECONDS);
    }

    private void run() {
        String line = supplier.get();
        if (line != null && !line.equals("")) {
            try {
                queue.put(line);
            } catch (InterruptedException e) {
                String errorMessage = "Exception was thrown trying to put message in queue";
                FeedHandlerException feedHandlerException = new FeedHandlerException(errorMessage, e);
                logger.log(SEVERE, errorMessage, feedHandlerException);
                throw feedHandlerException;
            }
        }
    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination(AWAIT_TERMINATION_TIME_MILLIS, MILLISECONDS);
    }
}
