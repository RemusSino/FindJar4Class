/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.rs.findjar4class.logic;

import java.util.function.Predicate;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import ro.rs.findjar4class.util.Utils;

/**
 *
 * @author rsinorchian
 */
public class ClassFinder {

    public final FileIndexer fileIndexer = FileIndexer.getInstance();
    private DefaultListModel listModel;

    public ClassFinder() {
    }

    public ClassFinder(DefaultListModel listModel) {
        this();
        this.listModel = listModel;
    }

    public void findClass(String findWhat) {
        listModel.clear();

        if (!findWhat.isEmpty()) {
            if (findWhat.contains(".")) {
                findClassByFullyQualifiedName(findWhat);
            } else if (findWhat.contains("*")) {
                findClassByWildCard(findWhat);
            } else {
                findClassByClassName(findWhat);
            }
        }
    }

    public void findClassByClassName(String className) {
        if (fileIndexer.getIndex().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Load index file first");
        }

        Predicate<String> matchesClassName = s -> s.contains(className) && Utils.getClassNameFromFullyQualifiedName(s).equalsIgnoreCase(className);
        fileIndexer.getIndex().entrySet().parallelStream().filter(e -> matchesClassName.test(e.getKey())).forEach(e -> listModel.addElement(e.getKey() + " - " + e.getValue()));
    }

    public void findClassByWildCard(String wildCard) {
        if (fileIndexer.getIndex().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Load index file first");
        }

        if (!wildCard.contains(".") && wildCard.endsWith("*")) {
            String className = wildCard.substring(0, wildCard.length() - 1);
            String jarName = null;

            fileIndexer.getIndex().entrySet().parallelStream().filter(e -> Utils.getClassNameFromFullyQualifiedName(e.getKey()).startsWith(className)).forEach(e -> listModel.addElement(e.getKey() + " - " + e.getValue()));
        } else if (!wildCard.contains(".") && wildCard.startsWith("*")) {
            String className = wildCard.substring(1);
            String jarName = null;

            fileIndexer.getIndex().entrySet().parallelStream().filter(e -> Utils.getClassNameFromFullyQualifiedName(e.getKey()).endsWith(className)).forEach(e -> listModel.addElement(e.getKey() + " - " + e.getValue()));
        }
    }

    public void findClassByFullyQualifiedName(String fullyQualifiedClassName) {
        if (fileIndexer.getIndex().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Load index file first");
        }

        String jarName = (String) fileIndexer.getIndex().get(fullyQualifiedClassName);

        if (!jarName.isEmpty()) {
            listModel.addElement(fullyQualifiedClassName + " - " + jarName);
        }
    }
}
