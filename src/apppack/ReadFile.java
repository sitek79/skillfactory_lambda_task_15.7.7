package apppack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadFile {
    void trueReader() {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        Path filePath = Paths.get("src/read.txt");

        int bt = 0;

        try {
            fis = new FileInputStream(String.valueOf(filePath));
            isr = new InputStreamReader(fis, "UTF-8");
            while ((bt = isr.read()) != -1) {
                System.out.println((char) bt);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
