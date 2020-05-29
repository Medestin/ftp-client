package com.medestin.ftp.client.utils.logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class FileLogger {
    private static final String LOGS_DIRECTORY = "application-logs/";
    private static final String LOGS_FILENAME = getNowFormatted();
    private static final String LOGS_EXTENSION = ".log";
    private static final File FILE_DIRECTORY = new File(LOGS_DIRECTORY);

    private static final String FILE_LOGGER_ERROR_MESSAGE = "There was an error with binding logs to a file";

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        try {
            if (!FILE_DIRECTORY.exists()) {
                //noinspection ResultOfMethodCallIgnored
                FILE_DIRECTORY.mkdir();
            }
            logger.addHandler(fileHandler());
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            Logger.getLogger(FileLogger.class.getName()).warning(FILE_LOGGER_ERROR_MESSAGE);
            e.printStackTrace();
        }
        return logger;
    }

    private static Handler fileHandler() throws IOException {
        return new FileHandler(LOGS_DIRECTORY.concat(LOGS_FILENAME).concat(LOGS_EXTENSION));
    }

    private static String getNowFormatted() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd-kk-mm"));
    }
}
