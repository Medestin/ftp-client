package com.medestin.ftp;

import com.medestin.ftp.client.FTPClient;
import com.medestin.ftp.client.FTPClientImpl;

public class Main {

    public static void main(String[] args) {
        FTPClient client = new FTPClientImpl("localhost");
        boolean logged = client.logIn("user", "password");

        System.out.println("Managed to log in: " + logged);
    }
}
