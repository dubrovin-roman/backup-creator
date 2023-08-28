package ru.backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {
    public static String getDataFromCommandString(String message) {
        System.out.println(message);
        String result;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            result = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String getFileNameBackUpZip() {
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return String.format("backup-%s.zip", formatter.format(currentDate));
    }

    public static ArrayList<File> getAllFiles(File[] files) {
        ArrayList<File> result = new ArrayList<>();
        for (File file: files) {
            if (file.isDirectory()) {
                result.addAll(Util.getAllFiles(Objects.requireNonNull(file.listFiles())));
            } else {
                result.add(file);
            }
        }
        return result;
    }
}
