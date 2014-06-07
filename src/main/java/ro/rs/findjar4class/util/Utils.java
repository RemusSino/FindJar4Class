package ro.rs.findjar4class.util;

/**
 * @author remussino
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

    public static boolean fileIsEmpty(File fname) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fname));
            if (br.readLine() == null) {
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean isJarFile(File f) {
        return (!f.isDirectory()) && (f.getName().endsWith(".jar"));
    }
}
