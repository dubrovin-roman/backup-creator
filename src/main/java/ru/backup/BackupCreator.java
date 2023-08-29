package ru.backup;

import ru.logger.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupCreator {

    private Logger logger;
    private String pathInStr;
    private String pathOutStr;
    private String nameFileStr;
    private boolean isHaveParameters;

    public BackupCreator(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getPathInStr() {
        return pathInStr;
    }

    public void setPathInStr(String pathInStr) {
        this.pathInStr = pathInStr;
    }

    public String getPathOutStr() {
        return pathOutStr;
    }

    public void setPathOutStr(String pathOutStr) {
        this.pathOutStr = pathOutStr;
    }

    public String getNameFileStr() {
        return nameFileStr;
    }

    public void setNameFileStr(String nameFileStr) {
        this.nameFileStr = nameFileStr;
    }

    public boolean isHaveParameters() {
        return isHaveParameters;
    }

    public void setHaveParameters(boolean haveParameters) {
        isHaveParameters = haveParameters;
    }

    public void createBackup() {
        DataForBackupCreatorDTO data = getData();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(data.getPathOutWithFileName().toFile())))) {
            File[] catalogs = data.getPathIn().toFile().listFiles();
            if (catalogs == null) throw new RuntimeException("Исходный каталог не существует.");
            if (catalogs.length == 0) throw new RuntimeException("Исходный каталог пуст.");
            ArrayList<File> files = Util.getAllFiles(catalogs);
            files.forEach(file -> {
                Path filePathTemp = file.toPath();
                Path relativePath = data.getPathIn().relativize(filePathTemp);
                ZipEntry entry;
                if (file.isDirectory()) {
                    entry = new ZipEntry(relativePath + "\\");
                    copyCatalogToArchive(zipOutputStream, entry, filePathTemp, logger);
                } else {
                    entry = new ZipEntry(relativePath.toString());
                    copyFileToArchive(zipOutputStream, entry, filePathTemp, logger);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в zip файл.");
        }
    }

    private DataForBackupCreatorDTO getData() {
        Path pathIn;
        Path pathOut;
        Path nameFile;
        Path pathOutWithFileName;

        if (isHaveParameters) {
            pathIn = Path.of(this.pathInStr);
            pathOut = Path.of(this.pathOutStr);
            nameFile = Path.of(this.nameFileStr);
        } else {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
                logger.log("Введите дерикторию для которой необходимо создать backup:");
                pathIn = Path.of(bufferedReader.readLine());
                logger.log("Введите дерикторию где будет хранится backup:");
                pathOut = Path.of(bufferedReader.readLine());
                logger.log("Введите название backup файла:");
                String nameFileStr = bufferedReader.readLine();
                nameFile = Path.of(Util.getFileNameBackUpZip(nameFileStr));
            } catch (InvalidPathException | IOException e) {
                throw new RuntimeException("Вы ввели недопустимый путь директории.");
            }
        }



        // создание директории
        try {
            if (!Files.exists(pathOut)) {
                Files.createDirectory(pathOut);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не получилось создать директорию для хранения backup.");
        }
        // создание файла в директории
        try {
            pathOutWithFileName = pathOut.resolve(nameFile);
            Files.deleteIfExists(pathOutWithFileName);
            if (!Files.exists(pathOutWithFileName)) {
                Files.createFile(pathOutWithFileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не получилось создать backup файл.");
        }

        return new DataForBackupCreatorDTO(pathIn, pathOutWithFileName);
    }

    private void copyFileToArchive(ZipOutputStream zos, ZipEntry entry, Path filePath, Logger logger) {
        try {
            zos.putNextEntry(entry);
            long countCopyByte = Files.copy(filePath, zos);
            zos.closeEntry();
            long countByteAfterArchived = entry.getCompressedSize();
            double compressionPercentage = countCopyByte == 0 ? 0 : ((((double) countByteAfterArchived / (double) countCopyByte) - 1) * 100);
            logger.log("archived: "
                    + filePath.getFileName().toString()
                    + ", "
                    + "size: "
                    + countCopyByte
                    + " | "
                    + countByteAfterArchived
                    + " byte, "
                    + Util.roundDouble(Math.abs(compressionPercentage))
                    + " %");
        } catch (IOException e) {
            throw new RuntimeException("Не получилось получить доступ к архивируемому файлу.");
        }
    }

    private void copyCatalogToArchive(ZipOutputStream zos, ZipEntry entry, Path filePath, Logger logger) {
        try {
            zos.putNextEntry(entry);
            zos.closeEntry();
            logger.log("archived: "
                    + filePath.toString());
        } catch (IOException e) {
            throw new RuntimeException("Не получилось поместить пустой каталог в архив.");
        }
    }
}
