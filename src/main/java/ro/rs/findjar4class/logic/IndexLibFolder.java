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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.SpringLayout;
import ro.rs.findjar4class.util.Utils;

public final class IndexLibFolder {

    private final Map<String, String> index = new ConcurrentHashMap<>();
//    ConcurrentHashMap<Object, Object>

    public void indexJarFile(Path jarFilePath)
            throws IOException {
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFilePath.toString()))) {

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

                    this.index.put(className.toString(), jarFilePath.toAbsolutePath().toString());
                }
            }
        }
    }

    public Map<String, String> indexClasses(Path libFolder) {
        try {
            List<Path> listOfFiles = listAllJarsUnderDirectory(libFolder);
            long startTime = System.nanoTime();

            listOfFiles.parallelStream().forEach(p -> {
                try {
                    indexJarFile(p);
                } catch (IOException ex) {
                    Logger.getLogger(IndexLibFolder.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            long endTime = System.nanoTime();

            System.out.println("Execution time for indexingJarFiles = " + (endTime - startTime) / 1000000000.0);
        } catch (IOException ex) {
            Logger.getLogger(IndexLibFolder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.index;
    }

    public List<Path> listAllJarsUnderDirectory(Path directory) throws IOException {

        long startTime = System.nanoTime();

        Deque<Path> stack = new ArrayDeque<Path>();
        List<Path> files = new LinkedList<>();

        if (directory.isAbsolute() && Files.isDirectory(directory)) {
            stack.push(directory);

            while (stack.size() > 0) {
                DirectoryStream<Path> stream = Files.newDirectoryStream(stack.pop());
                for (Path p : stream) {
                    if (Files.isDirectory(p)) {
                        stack.push(p);
                    } else if (Utils.isJarFile(p)) {
                        files.add(p);
                    }
                }
            }
        }
        System.out.println(files.size() + " jars listed");
        long endTime = System.nanoTime();
        System.out.println("Execution time for method listAllJarsUnderDirectory() = " + (endTime - startTime) / 1000000000.0);

        return files;
    }

    public void outputIndexToFile(File fname) {
        long startTime = System.nanoTime();
        System.out.println("Writing to file " + this.index.size() + " lines");
        try (FileWriter writer = new FileWriter(fname)) {
            String line = "";
            for (String key : this.index.keySet()) {
                line = key + ":" + (String) this.index.get(key);
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("Execution time for method outputIndexToFile() = " + (endTime - startTime) / 1000000000.0);
    }
}
