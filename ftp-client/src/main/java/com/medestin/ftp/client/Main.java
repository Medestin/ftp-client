package com.medestin.ftp.client;

import com.medestin.ftp.client.runner.ClientRunner;

public class Main {
    private static final String HOSTNAME = "localhost";
    private static final int port = 21;

    public static void main(String[] args) throws Exception {
        ClientRunner runner = new ClientRunner(HOSTNAME, port);
        runner.run();
    }
}
