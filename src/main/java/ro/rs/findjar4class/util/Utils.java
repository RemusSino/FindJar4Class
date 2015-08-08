package ro.rs.findjar4class.util;

/**
 * @author remussino
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    public static boolean fileIsEmpty(File fname) {
        try (BufferedReader br = new BufferedReader(new FileReader(fname))) {
            if (br.readLine() == null) {
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean isJarFile(Path path) {
        return (!Files.isDirectory(path)) && (path.getFileName().toString().endsWith(".jar"));
    }

    public static boolean isSameSearchAsPreviousStep(String currentSearch, String previousSearch) {
        return currentSearch.equalsIgnoreCase(previousSearch);
    }

    public static String getClassNameFromResultMap(String value) {
        int index = value.indexOf("|");
        return value.substring(0, index);
    }

    public static String getJarNameFromResultMap(String value) {
        int index = value.indexOf("|");
        return value.substring(index + 1);
    }

    public static String getClassNameFromFullyQualifiedName(String fullyQualifiedName) {
        if (fullyQualifiedName.contains(".")) {
            return fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf(".") + 1);
        }

        return "";
    }
}
