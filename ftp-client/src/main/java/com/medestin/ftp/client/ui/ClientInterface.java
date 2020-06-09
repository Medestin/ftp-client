package com.medestin.ftp.client.ui;

import com.medestin.ftp.client.FTPClient;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientInterface {
    private final PrintStream out;
    private final Scanner in;
    private final FTPClient client;
    private boolean keepRunning;

    private final Map<String, Runnable> options;

    public ClientInterface() throws Exception {
        this.out = System.out;
        this.in = new Scanner(System.in);
        this.client = new FTPClient(out);
        this.options = options();
        runOptionWindow();
    }

    private void runOptionWindow() throws Exception {
        keepRunning = true;
        while (keepRunning) {
            printOptions();
            String input = in.nextLine();
            if(options.containsKey(input)){
                options.get(input).run();
            } else {
                out.println("Invalid input, try again");
            }
        }
        client.close();
    }

    private void printOptions() {
        out.println("Select one of below");
        options.keySet().forEach(out::println);
    }

    private Map<String, Runnable> options() {
        Map<String, Runnable> options = new LinkedHashMap<>();
        options.put("exit", () -> keepRunning = false);
        options.put("connect", connect());
        options.put("log-in", logIn());
        options.put("current-location", currentLocation());
        options.put("list-items", listItems());
        options.put("retrieve", retrieve());
        return options;
    }

    private Runnable connect() {
        return () -> {
          out.println("Please enter hostname address:");
            String input = in.nextLine();
            client.connect(input);
        };
    }

    private Runnable logIn() {
        return () -> {
            out.println("Please enter username:");
            String user = in.nextLine();
            out.println("Please enter password:");
            String psw = in.nextLine();
            client.logIn(user, psw);
        };
    }

    private Runnable currentLocation() {
        return client::currentLocation;
    }

    private Runnable listItems() {
        return client::list;
    }

    private Runnable retrieve() {
        return () -> {
            out.println("Please enter filename(with extension) to retrieve:");
            String filename = in.nextLine();
            client.retrieve(filename);
        };
    }
}
