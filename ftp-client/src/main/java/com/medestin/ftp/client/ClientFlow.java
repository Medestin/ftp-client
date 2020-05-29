package com.medestin.ftp.client;

import com.medestin.ftp.client.connection.Connection;
import com.medestin.ftp.client.utils.interaction.UserInteraction;

public class ClientFlow {

    static void flow() throws Exception {
        UserInteraction ui = new UserInteraction();
        ui.write("Hello to my ftp client!");
        ui.write("Please provide server hostname:");
        String hostname = ui.read();
        ui.write("Please provide server command port(usually 21):");
        int port = Integer.parseInt(ui.read());

        Connection connection = new Connection(hostname, port);
        connection.connect(x -> System.out.println("Feed: ".concat(x)));

        ui.write("Enter \"q\" to quit...");
        String userInput = ui.read();
        while (!"q".equalsIgnoreCase(userInput)) {
            userInput = ui.read();
        }

        connection.close();
    }
}
