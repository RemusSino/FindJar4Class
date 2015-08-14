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
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;
import ro.rs.findjar4class.db.PersistenceManager;
import ro.rs.findjar4class.util.Utils;

public final class FileIndexer {

    private Map<String, String> index;
    private static FileIndexer instance;

    private FileIndexer() {
        index = new ConcurrentHashMap<>();
    }

    public static synchronized FileIndexer getInstance() {
        if (instance == null) {
            instance = new FileIndexer();
        }

        return instance;
    }

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
            long startTime = System.nanoTime();
            List<Path> listOfFiles = Utils.listAllJarsUnderDirectory(libFolder);
            listOfFiles.parallelStream().forEach(p -> {
                try {
                    indexJarFile(p);
                } catch (IOException ex) {
                    Logger.getLogger(FileIndexer.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            long endTime = System.nanoTime();

            System.out.println("Execution time for indexingJarFiles = " + (endTime - startTime) / 1000000000.0);
        } catch (IOException ex) {
            Logger.getLogger(FileIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.index;
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

    public void saveIndexInDB(Map<String, String> map) {
        long startTime = System.nanoTime();
        System.out.println("Writing to DB " + this.index.size() + " lines");
        PersistenceManager instance = PersistenceManager.getInstance();
        instance.dropAndCreateTable();
        instance.insertMultiRow(map);
        long endTime = System.nanoTime();
        System.out.println("Execution time for method saveIndexInDB() = " + (endTime - startTime) / 1000000000.0);
    }

    public void loadExistingIndex() {
        PersistenceManager pm = PersistenceManager.getInstance();
        this.index = pm.getAllRows();
    }

    public Map<String, String> getIndex() {
        return index;
    }

    public class IndexerThread extends Thread {

        javax.swing.JTextField jarFolderTextField;

        public IndexerThread() {
            super();
        }

        public IndexerThread(javax.swing.JTextField jarFolderTextField) {
            this();
            this.jarFolderTextField = jarFolderTextField;
        }

        public void run() {
            String libFolder = this.jarFolderTextField.getText();
            this.jarFolderTextField.setText("Please wait. Indexing files...");
            if (libFolder == null) {
                JOptionPane.showMessageDialog(null, "Enter path for lib folder");
            }

            Map<String, String> map = indexClasses(Paths.get(libFolder));
            saveIndexInDB(map);
            this.jarFolderTextField.setText("Index file loaded");
        }
    }
}
