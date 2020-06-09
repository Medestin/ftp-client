package com.medestin.ftp.utils.file.builder;

import com.medestin.ftp.utils.logger.FileLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class FileBuilder {
    private static final Logger logger = FileLogger.getLogger(FileBuilder.class);
    private static final String FILE_PATH = "transferred/";
    private static final File DIRECTORY = new File(FILE_PATH);

    public FileBuilder() {
        createDir();
    }

    public void writeToFile(String filename, String content) throws IOException {
        File file = new File(FILE_PATH.concat(filename));
        file.createNewFile();
        FileWriter writer = writer(file);
        writer.write(content);
        writer.close();
    }

    private void createDir() {
        if(!DIRECTORY.exists()) {
            DIRECTORY.mkdirs();
        }
    }

    private FileWriter writer(File file) {
        try {
            return new FileWriter(file);
        } catch (IOException e) {
            String errorMessage = String.format("Exception creating FileWriter for file '%s'", file.getAbsolutePath());
            FileBuilderException fileBuilderException = new FileBuilderException(errorMessage, e);
            logger.log(SEVERE, errorMessage, fileBuilderException);
            throw fileBuilderException;
        }
    }
}
