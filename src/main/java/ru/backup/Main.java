package ru.backup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        String result = Util.getDataFromCommandString("Введите дерикторию для backup:");
        Path pathResult = Path.of(result);

        String fileName = Util.getFileNameBackUpZip();

        Path pathDir = Path.of("D:\\file");
        if (!Files.exists(pathDir)) {
            Files.createDirectory(pathDir);
        }
        Path pathFile = Path.of("D:\\file\\" + fileName);

        Files.deleteIfExists(pathFile);
        if (!Files.exists(pathFile)) {
            Files.createFile(pathFile);
        }

        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(pathFile.toFile())));
        ArrayList<File> files = Util.getAllFiles(pathResult.toFile().listFiles());
        files.forEach(file -> {
            Path filePathTemp = file.toPath();
            Path relativePath = pathResult.relativize(filePathTemp);
            ZipEntry entry = new ZipEntry(relativePath.toString());
            try {
                zipOutputStream.putNextEntry(entry);
                long countCopyByte = Files.copy(filePathTemp, zipOutputStream);
                zipOutputStream.closeEntry();
                System.out.println("Заархивирован файл с именем: "
                        + filePathTemp.getFileName().toString()
                        + ", "
                        + "размер: "
                        + countCopyByte
                        + " байт.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        zipOutputStream.close();
    }
}