package ru.backup;

import ru.logger.ConsoleLogger;
import ru.logger.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = new ConsoleLogger();

        try {
            BackupCreator backupCreator = new BackupCreator(logger);
            backupCreator.createBackup();
        } catch (RuntimeException e) {
            logger.log(e.getMessage());
        }
    }
}