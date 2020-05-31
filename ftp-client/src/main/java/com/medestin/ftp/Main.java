package com.medestin.ftp;

import com.medestin.ftp.client.FTPClient;

public class Main {

    public static void main(String[] args) throws Exception {
        FTPClient client = new FTPClient();
        client.connect("localhost");
        client.logIn("user", "password");
        client.currentLocation();
        client.enterPassiveMode();

        client.close();
    }
}
