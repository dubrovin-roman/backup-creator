package ru.logger;

import ru.backup.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConsoleFileLogger implements Logger {
    private Path pathOut;
    private Path logFile = null;

    private StringBuilder builder = new StringBuilder();

    public ConsoleFileLogger() {
    }

    public Path getPathOut() {
        return pathOut;
    }

    public void setPathOut(Path pathOut) {
        this.pathOut = pathOut;
    }

    @Override
    public void log(String message) {
        if (logFile == null) logFile = createLogFile();
        System.out.println(message);
        builder.append(message).append(System.lineSeparator());
    }

    public void writeLogFile() {
        try {
            Files.write(logFile, builder.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Неудолось записать лог файл");
        }
    }

    private Path createLogFile() {
        Path logFile = null;
        String nameLogFile = Util.getFileNameBackUpZip("log").replaceFirst("zip", "txt");
        try {
            logFile = Files.createFile(this.pathOut.resolve(Path.of(nameLogFile)));
        } catch (IOException e) {
            throw new RuntimeException("Не получилось создать лог файл.");
        }

        return logFile;
    }
}
