package com.medestin.ftp.client.connection;

import com.medestin.ftp.client.utils.logger.FileLogger;
import com.medestin.ftp.client.utils.stream.reader.PeriodicStringFeed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

public class Connection implements AutoCloseable {
    private final static Logger logger = FileLogger.getLogger(Connection.class);
    private static final int CAPACITY = 5;
    private static final int RESPONSE_CODE_BEGIN = 0;
    private static final int RESPONSE_CODE_END = 3;

    private final Socket socket;
    private final SocketAddress socketAddress;
    private final PeriodicStringFeed periodicStringFeed;
    private final BlockingQueue<String> responseQueue;

    public Connection(String hostname, int port) {
        this.socket = new Socket();
        this.socketAddress = new InetSocketAddress(hostname, port);
        this.periodicStringFeed = new PeriodicStringFeed();
        this.responseQueue = new ArrayBlockingQueue<>(CAPACITY);
    }

    public void connect(Consumer<String> consumer) throws IOException, InterruptedException {
        if(!socket.isConnected()) {
            socket.connect(socketAddress);
            directOutputTo(hookToConsumer(consumer));
            int response = parseInt(responseQueue.take().substring(RESPONSE_CODE_BEGIN, RESPONSE_CODE_END));
            if(response == 220) {
                logger.info("Successfully connected!");
            } else {
                String msg = "Unrecognized code: " + response;
                consumer.accept(msg);
                logger.info(msg);
            }
        } else {
            String msg = "Already connected.";
            consumer.accept(msg);
            logger.info(msg);
        }
    }

    private Consumer<String> hookToConsumer(Consumer<String> consumer) {
        return s -> {
            try {
                responseQueue.put(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            consumer.accept(s);
        };
    }

    private void directOutputTo(Consumer<String> consumer) throws IOException {
        periodicStringFeed.feed(readLineOrEmptyString(), consumer);
    }

    private Supplier<String> readLineOrEmptyString() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return () -> {
            try {
                if(reader.ready()) {
                    return reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        };
    }

    @Override
    public void close() throws Exception {
        periodicStringFeed.close();
        if (!socket.isClosed()) {
            socket.close();
        }
    }
}
