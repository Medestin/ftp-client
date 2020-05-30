package com.medestin.ftp.client;

public interface FTPClient {

    boolean logIn(String username, String password);

    String showDirectory();

    String listElements();

    boolean enterDirectory(String directory);

    boolean copy(String elementName);

}
