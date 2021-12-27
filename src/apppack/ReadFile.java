package apppack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFile {
    void trueReader() {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        int bt = 0;

        try {
            fis = new FileInputStream("C:\\Books_calibre\\2019\\Horstmann C.S. - Core Java. Vol. 2. (56)\\Horstmann C.S. - Core Java. Vol - 2019.pdf");
            isr = new InputStreamReader(fis, "UTF-8");
            while ((bt = isr.read()) != -1) {
                System.out.println((char) bt);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
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
