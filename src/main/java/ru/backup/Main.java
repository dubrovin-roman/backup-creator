package ru.backup;

import ru.logger.ConsoleFileLogger;
import ru.logger.ConsoleLogger;
import ru.logger.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = new ConsoleFileLogger();

        try {
            BackupCreator backupCreator = new BackupCreator(logger);
            if (args.length == 4) {
                backupCreator.setHaveParameters(true);
                backupCreator.setPathInStr(args[0]);
                backupCreator.setPathOutStr(args[1]);
                backupCreator.setNameFileStr(args[2]);
                backupCreator.setDelFiles(Boolean.parseBoolean(args[3]));
            }
            backupCreator.createBackup();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}