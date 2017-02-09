package com.gmail.murmeldjur.adressbok;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

/**
 * Logging class for setting up logging
 */
public class Logging {
    /*
     * NOTE!
     * Output to console is disabled in .properties file with command:
     * "java.util.logging.ConsoleHandler.level = OFF"
     */
    public static void setupLogging() {
        String loggingFilePath = "C:\\Users\\katan\\IdeaProjects\\Adressbok\\files\\logging.properties";
        try (FileInputStream fileInputStream = new FileInputStream(loggingFilePath)) {
            LogManager.getLogManager().readConfiguration(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load log properties.", e);
        }
    }
}
