package ru.backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

    public static String getFileNameBackUpZip(String fileNameStartWord) {
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return String.format("%s-backup-%s.zip", fileNameStartWord, formatter.format(currentDate));
    }

    public static ArrayList<File> getAllFiles(File[] files) {
        ArrayList<File> result = new ArrayList<>();
        for (File file: files) {
            if (file.isDirectory()) {
                if (Objects.requireNonNull(file.listFiles()).length == 0) result.add(file);
                result.addAll(Util.getAllFiles(Objects.requireNonNull(file.listFiles())));
            } else {
                result.add(file);
            }
        }
        return result;
    }

    public static String roundDouble(double d) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(d);
    }
}
