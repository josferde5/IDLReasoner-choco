package idlreasonerchoco.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.io.Files;

public class FileManager {

    public static File recreateFile(String filePath) {
        File file = new File(filePath);
        file.delete();
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = openReader(filePath);

        try {
            String line = reader.readLine();
            while (line != null) {
                content.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    public static void appendContentToFile(String filePath, String content) {
        File file = new File(filePath);
        while (true) {
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
                out.append(content);
                out.flush();
                out.close();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeContentToFile(String filePath, String content) {
        File file = new File(filePath);
        while (true) {
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
                out.append(content);
                out.flush();
                out.close();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static BufferedWriter openWriter(String filePath) {
        File file = new File(filePath);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static BufferedReader openReader(String filePath) {
        File file = new File(filePath);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public static void copyFile(String originPath, String destPath) {
        try {
            Files.copy(new File(originPath), new File(destPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
