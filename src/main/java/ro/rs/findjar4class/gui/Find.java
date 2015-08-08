/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.rs.findjar4class.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import ro.rs.findjar4class.logic.IndexLibFolder;
import ro.rs.findjar4class.util.Utils;

/**
 *
 * @author rsinorchian
 */
public class Find extends javax.swing.JFrame {

    public final IndexLibFolder finder = new IndexLibFolder();
    public final String outputIndexFile = "indexFile.txt";
    public Map<String, String> indexMap;
    public Map<String, String> shortcutIndexMap;
    private List<String> results;
    private String previousSearch;
    private int sameSearchCounter = 0;
    private DefaultListModel listModel;

    /**
     * Creates new form Find
     */
    public Find() {
        initComponents();
        this.listModel = new DefaultListModel();
        this.resultList.setModel(this.listModel);
        loadExistingIndexFile();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jarFolderTextField = new javax.swing.JTextField();
        classNameTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        indexJars = new javax.swing.JButton();
        searchClass = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jarFolderTextField.setText("Enter the path of the folder which contains the jars");
        jarFolderTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jarFolderTextFieldActionPerformed(evt);
            }
        });

        classNameTextField.setText("Enter class name. Wildcards can be used with *");

        jLabel1.setText("Step 2: Search the class in the folder with jars");

        jLabel2.setText("Jars folder");

        jLabel3.setText("Class name");

        indexJars.setText("Index jars");
        indexJars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                indexJarsActionPerformed(evt);
            }
        });

        searchClass.setText("Search class");
        searchClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchClassActionPerformed(evt);
            }
        });

        jLabel4.setText("Step 1: Index the jar folder");

        resultList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(resultList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(classNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jarFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(searchClass, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(indexJars, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel4))
                        .addGap(0, 128, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jarFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(indexJars)
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(searchClass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jarFolderTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jarFolderTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jarFolderTextFieldActionPerformed

    private void indexJarsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_indexJarsActionPerformed
        // TODO add your handling code here:
        loadIndexFile();
    }//GEN-LAST:event_indexJarsActionPerformed

    private void searchClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchClassActionPerformed
        // TODO add your handling code here:
        findClass();
    }//GEN-LAST:event_searchClassActionPerformed

    private void loadIndexFile() {
        String libFolder = this.jarFolderTextField.getText();
        if (libFolder == null) {
            JOptionPane.showMessageDialog(null, "Enter path for lib folder");
        }
        
        
        this.indexMap = this.finder.indexClasses(Paths.get(libFolder));

        File indexFile = new File(outputIndexFile);
        this.finder.outputIndexToFile(indexFile);
        if ((indexFile.exists()) && (!Utils.fileIsEmpty(indexFile))) {
            this.jarFolderTextField.setText("Index file loaded");
        }
    }

    private void loadExistingIndexFile() {
        File indexFile = new File(outputIndexFile);

        if (indexFile.exists() && indexFile.length() != 0) {
            this.indexMap = new LinkedHashMap();
            try {
                BufferedReader br = new BufferedReader(new FileReader(indexFile));
                String line;
                while ((line = br.readLine()) != null) {
                    int indexOfDelimiter = line.indexOf(":");
                    String className = line.substring(0, indexOfDelimiter);
                    String jarName = line.substring(indexOfDelimiter + 1);
                    this.indexMap.put(className, jarName);
                }
            } catch (IOException ex) {
                Logger.getLogger(Find.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.jarFolderTextField.setText("Index loaded from existing file");
        }
    }

    public void findClass() {
        String findWhat = this.classNameTextField.getText();

        if (!findWhat.isEmpty()) {
            if (findWhat.contains(".")) {
                findClassByFullyQualifiedName(findWhat);
            } else if (findWhat.contains("*")) {
                findClassByWildCard(findWhat);
            } else {
                findClassByClassName(findWhat);
            }

//            this.findWhatTextField.setText(Utils.getClassNameFromResultMap(resultList.get(sameSearchCounter)));
        }
    }

    public void findClassByClassName(String className) {
        if (this.indexMap.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Load index file first");
        }

        Predicate<String> matchesClassName = s -> s.contains(className) && Utils.getClassNameFromFullyQualifiedName(s).equalsIgnoreCase(className);
        this.indexMap.entrySet().parallelStream().filter(e -> matchesClassName.test(e.getKey())).forEach(e -> listModel.addElement(e.getKey() + " - " + e.getValue()));
    }

    public void findClassByWildCard(String wildCard) {
        if (this.indexMap.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Load index file first");
        }

        if (!wildCard.contains(".") && wildCard.lastIndexOf("*") == wildCard.length() - 1) {
            String className = wildCard.substring(0, wildCard.length() - 1);
            String jarName = null;

            this.indexMap.entrySet().parallelStream().filter(e -> e.getKey().contains(className)).forEach(e -> listModel.addElement(e.getKey() + " - " + e.getValue()));
        }
    }

    public void findClassByFullyQualifiedName(String fullyQualifiedClassName) {
        if (this.indexMap.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Load index file first");
        }

        String jarName = (String) this.indexMap.get(fullyQualifiedClassName);

        if (!jarName.isEmpty()) {
            listModel.addElement(fullyQualifiedClassName + " - " + jarName);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField classNameTextField;
    private javax.swing.JButton indexJars;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jarFolderTextField;
    private javax.swing.JList resultList;
    private javax.swing.JButton searchClass;
    // End of variables declaration//GEN-END:variables
}
