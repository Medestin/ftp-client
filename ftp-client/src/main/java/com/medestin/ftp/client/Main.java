package com.medestin.ftp.client;

public class Main {
    private static final String HOSTNAME = "localhost";
    private static final int port = 21;

    public static void main(String[] args) {
        try(Client client = new Client(HOSTNAME, port)) {
            client.writeMessage("USER user");
            client.writeMessage("PASS password");
            client.readMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
