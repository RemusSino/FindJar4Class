package ro.rs.findjar4class.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
