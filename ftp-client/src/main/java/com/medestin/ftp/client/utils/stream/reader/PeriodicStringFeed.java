package com.medestin.ftp.client.utils.stream.reader;

import com.medestin.ftp.client.utils.logger.FileLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ConcurrentStreamReader implements AutoCloseable {
    private static final Logger logger = FileLogger.getLogger(ConcurrentStreamReader.class);
    private static final long DELAY = 100L;
    private final List<ExecutorService> executors;

    public ConcurrentStreamReader() {
        executors = new ArrayList<>();
    }

    public void feed(Supplier<String> supplier, Consumer<String> consumer) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(runnable(supplier, consumer), 0L, DELAY, MILLISECONDS);
        executors.add(executorService);
    }

    private Runnable runnable(Supplier<String> supplier, Consumer<String> consumer) {
        return () -> {
            String string = supplier.get();
            if(string != null && !"".equalsIgnoreCase(string)) {
                consumer.accept(string);
            }
        };
    }

    @Override
    public void close() throws Exception {
        for (ExecutorService executor : executors) {
            executor.shutdown();
            executor.awaitTermination(5, SECONDS);
        }
    }
}
