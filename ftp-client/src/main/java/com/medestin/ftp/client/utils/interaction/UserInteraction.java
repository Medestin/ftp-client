package com.medestin.ftp.client.utils.interaction;

import java.io.PrintStream;
import java.util.Scanner;

public class UserInteraction {
    private final Scanner scanner;
    private final PrintStream writer;

    public UserInteraction() {
        this.scanner = new Scanner(System.in);
        this.writer = System.out;
    }

    public String read() {
        return scanner.nextLine();
    }

    public void write(String message) {
        writer.println(message);
    }
}
