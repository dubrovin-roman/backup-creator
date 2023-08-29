package ru.backup;

import ru.logger.ConsoleLogger;
import ru.logger.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = new ConsoleLogger();

        try {
            BackupCreator backupCreator = new BackupCreator(logger);
            if (args.length == 3) {
                backupCreator.setHaveParameters(true);
                backupCreator.setPathInStr(args[0]);
                backupCreator.setPathOutStr(args[1]);
                backupCreator.setNameFileStr(args[2]);
            }
            backupCreator.createBackup();
        } catch (RuntimeException e) {
            logger.log(e.getMessage());
        }
    }
}