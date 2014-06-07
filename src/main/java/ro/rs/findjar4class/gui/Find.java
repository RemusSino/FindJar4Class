package ro.rs.findjar4class.gui;

/**
 *
 * @author remussino
 */
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.layout.GroupLayout;
import ro.rs.findjar4class.logic.IndexLibFolder;
import ro.rs.findjar4class.util.Utils;

public class Find
        extends JFrame {

    public final IndexLibFolder finder = new IndexLibFolder();
    public final String outputIndexFile = "indexFile.txt";
    public Map<String, String> indexMap;
    public Map<String, String> shortcutIndexMap;
    private JButton find;
    private JTextField findWhatTextField;
    private JTextField foundJarTextField;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JTextField libFolderTextField;
    private JButton loadIndexFile;

    public Find() {
        initComponents();
        loadExistingIndexFile();
    }

    private void initComponents() {
        this.jLabel1 = new JLabel();
        this.findWhatTextField = new JTextField();
        this.find = new JButton();
        this.jLabel2 = new JLabel();
        this.libFolderTextField = new JTextField();
        this.loadIndexFile = new JButton();
        this.foundJarTextField = new JTextField();
        this.jLabel3 = new JLabel();

        setDefaultCloseOperation(3);
        setTitle("Find");

        this.jLabel1.setText("Find What:");

        this.find.setText("Find");
        this.find.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Find.this.findActionPerformed(evt);
            }
        });
        this.jLabel2.setText("Lib folder: ");

        this.libFolderTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Find.this.libFolderTextFieldActionPerformed(evt);
            }
        });
        this.loadIndexFile.setText("Load index from lib folder");
        this.loadIndexFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Find.this.loadIndexFileActionPerformed(evt);
            }
        });
        this.jLabel3.setText("Found jar: ");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(1).add(2, this.loadIndexFile, -1, -1, 32767).add(layout.createSequentialGroup().add(layout.createParallelGroup(1).add(this.jLabel1).add(this.jLabel2, -1, 68, 32767).add(this.jLabel3)).addPreferredGap(0).add(layout.createParallelGroup(1).add(layout.createSequentialGroup().add(this.findWhatTextField, -2, 313, -2).add(18, 18, 18).add(this.find, -1, 168, 32767)).add(this.libFolderTextField).add(this.foundJarTextField)))).addContainerGap()));

        layout.setVerticalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(3).add(this.jLabel2).add(this.libFolderTextField, -2, -1, -2)).add(18, 18, 18).add(this.loadIndexFile).add(18, 18, 18).add(layout.createParallelGroup(3).add(this.jLabel1).add(this.findWhatTextField, -2, -1, -2).add(this.find)).add(18, 18, 18).add(layout.createParallelGroup(3).add(this.foundJarTextField, -2, -1, -2).add(this.jLabel3)).addContainerGap(-1, 32767)));

        pack();
    }

    private void findActionPerformed(ActionEvent evt) {
        String findWhat = this.findWhatTextField.getText();
        if (findWhat != null) {
            if (this.indexMap.size() == 0) {
                JOptionPane.showMessageDialog(null, "Load index file first");
            }
            String jarName = (String) this.indexMap.get(findWhat);
            if (jarName == null) {
                for (String key : this.indexMap.keySet()) {
                    if (key.contains(findWhat)) {
                        jarName = (String) this.indexMap.get(key);
                        this.findWhatTextField.setText(key);
                        break;
                    }
                }
            }
            this.foundJarTextField.setText(jarName != null ? jarName : "null");
        }
    }

    private void libFolderTextFieldActionPerformed(ActionEvent evt) {
    }

    private void loadIndexFileActionPerformed(ActionEvent evt) {
        loadIndexFile();
    }

    private void loadIndexFile() {
        String libFolder = this.libFolderTextField.getText();
        if (libFolder == null) {
            JOptionPane.showMessageDialog(null, "Enter path for lib folder");
        }
        this.indexMap = this.finder.indexClasses(new File(libFolder));

        File indexFile = new File("indexFile.txt");
        this.finder.outputIndexToFile(indexFile);
        if ((indexFile.exists()) && (!Utils.fileIsEmpty(indexFile))) {
            this.libFolderTextField.setText("Index file loaded");
        }
    }

    private void loadExistingIndexFile() {
        File indexFile = new File("indexFile.txt");
        if (indexFile.exists()) {
            this.indexMap = new HashMap();
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
            this.libFolderTextField.setText("Index loaded from existing file");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
            for (int idx = 0; idx < installedLookAndFeels.length; idx++) {
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Find.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Find.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Find.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Find.class.getName()).log(Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Find().setVisible(true);
            }
        });
    }
}
