/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.rs.findjar4class.logic;

/**
 *
 * @author remussino
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import ro.rs.findjar4class.util.Utils;

public final class IndexLibFolder {

    private final Map<String, String> index = new HashMap();

    public void indexJarFile(File jarFile)
            throws IOException {
        ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if ((entry.getName().endsWith(".class")) && (!entry.isDirectory())) {
                StringBuilder className = new StringBuilder();
                for (String part : entry.getName().split("/")) {
                    if (className.length() != 0) {
                        className.append(".");
                    }
                    className.append(part);
                    if (part.endsWith(".class")) {
                        className.setLength(className.length() - ".class".length());
                    }
                }
                this.index.put(className.toString(), jarFile.getAbsolutePath());
            }
        }
        zip.close();
    }

    public Map<String, String> indexClasses(File libFolder) {
        if (libFolder.isDirectory()) {
            for (File f : libFolder.listFiles()) {
                if (f.isDirectory()) {
                    indexClasses(f);
                }
                if (Utils.isJarFile(f)) {
                    try {
                        indexJarFile(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this.index;
    }

    public void outputIndexToFile(File fname) {
        try {
            FileWriter writer = new FileWriter(fname);
            String line = "";
            for (String key : this.index.keySet()) {
                line = key + ":" + (String) this.index.get(key);
                writer.write(line);
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
