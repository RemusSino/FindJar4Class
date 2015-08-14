/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.rs.findjar4class.db;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rsinorchian
 */
public class PersistenceManagerTest {

    public PersistenceManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createTable method, of class PersistenceManager.
     */
    @Test
    public void testCreateTable() {
        System.out.println("createTable");
        PersistenceManager instance = PersistenceManager.getInstance();
        instance.createTable();
    }

    @Test
    public void testDropAndCreateTable() {
        System.out.println("dropAndCreateTable");
        PersistenceManager instance = PersistenceManager.getInstance();
        instance.dropAndCreateTable();
    }

    /**
     * Test of dropTable method, of class PersistenceManager.
     */
    @Test
    public void testDropTable() {
        System.out.println("dropTable");
        PersistenceManager instance = PersistenceManager.getInstance();
        instance.dropTable();
    }

    /**
     * Test of insertRow method, of class PersistenceManager.
     */
    @Test
    public void testInsertRow() {
        System.out.println("insertRow");
        String className = "wt.part.WTPart";
        String jarName = "D:\\wnc.jar";
        PersistenceManager instance = PersistenceManager.getInstance();
        instance.insertRow(className, jarName);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of deleteRow method, of class PersistenceManager.
     */
    @Test
    public void testDeleteRow() {
        System.out.println("deleteRow");
        String className = "";
        PersistenceManager instance = null;
        instance.deleteRow(className);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertMultiRow method, of class PersistenceManager.
     */
    @Test
    public void testInsertMultiRow() {
        System.out.println("insertMultiRow");
        Map<String, String> map = new HashMap<>();
        map.put("wt.doc.EPMDocument", "wnc.jar");
        map.put("wt.doc.CADDocument", "wnc.jar");
        map.put("wt.part.EPart", "wnc.jar");
        PersistenceManager instance = PersistenceManager.getInstance();
        instance.insertMultiRow(map);
    }

    @Test
    public void testGetAllRows() {
        System.out.println("deleteRow");
        String className = "";
        PersistenceManager instance = PersistenceManager.getInstance();
        Map<String, String> map = instance.getAllRows();
        System.out.println(map.size());
    }

    /**
     * Test of getJarName method, of class PersistenceManager.
     */
    @Test
    public void testGetJarName() {
        System.out.println("getJarName");
        String className = "";
        PersistenceManager instance = null;
        String expResult = "";
        String result = instance.getJarName(className);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
