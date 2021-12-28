package apppack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    private String wrstr;

    void trueWriter(String wrstr) {
        this.wrstr = wrstr;

        FileWriter fwr = null;
        String string = "Adding ...";
        File file = new File("src/write.txt");

        try {
            fwr = new FileWriter(file, true);
            //fileOutputStream = new FileOutputStream("C:\\Books_calibre\\2019\\Horstmann C.S. - Core Java. Vol. 2. (56)\\test_out.txt");
            fwr.write(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fwr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
