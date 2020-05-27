package com.medestin.ftp.client.runner;

import com.medestin.ftp.client.client.Client;

import java.util.Scanner;

public class ClientRunner {
    private final String hostname;
    private final int port;

    public ClientRunner(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void run() throws Exception {
        try(Client client = new Client(hostname, port)){
            client.connect();
            Scanner sc = new Scanner(System.in);
            boolean run = true;
            while(run) {
                String input = sc.nextLine();
                if(input.equalsIgnoreCase("q")) {
                    run = false;
                } else {
                    client.write(input);
                }
            }
        }
    }
}
