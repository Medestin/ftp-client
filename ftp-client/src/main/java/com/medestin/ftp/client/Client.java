package com.medestin.ftp.client;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Client implements AutoCloseable {
    private static final Logger log = Logger.getLogger(Client.class.getName());
    private final String hostname;
    private final int port;

    private Socket connection;
    private BufferedReader reader;
    private OutputStreamWriter writer;

    public Client(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        connect();
        setReader();
        setWriter();
    }

    private void connect() throws IOException {
        this.connection = new Socket(hostname, port);
        log.info(String.format("Established connection on %s:%s", hostname, port));
    }

    private void setReader() throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private void setWriter() throws IOException {
        this.writer = new OutputStreamWriter(connection.getOutputStream());
    }

    public void readMessage() throws IOException {
        String line = reader.readLine();
        while(line != null) {
            System.out.println(line);
            line = reader.readLine();
        }
    }

    public void writeMessage(String message) throws IOException {
        log.info("Writing message: \"".concat(message).concat("\""));
        writer.write(message.concat(System.lineSeparator()));
        writer.flush();
        log.info("done!");
    }

    @Override
    public void close() throws Exception {
        connection.close();
        log.info(String.format("Connection %s has been closed", connection.toString()));
    }
}
