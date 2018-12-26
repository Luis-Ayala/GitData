package com.layala.gitdata.servicios;

import com.layala.gitdata.entidades.Repositorio;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Luis
 */
public class RepositorioSrvTest {
    
    public RepositorioSrvTest() {
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
     * Test of actualizarRepositorio method, of class RepositorioSrv.
     */
    @Test
    public void testActualizarRepositorio_List() {
        System.out.println("actualizarRepositorio");
        List<Repositorio> repositorios = null;
        RepositorioSrv instance = new RepositorioSrv();
        long expResult = 0L;
        long result = instance.actualizarRepositorio(repositorios);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of actualizarRepositorio method, of class RepositorioSrv.
     */
    @Test
    public void testActualizarRepositorio_Repositorio() {
        System.out.println("actualizarRepositorio");
        Repositorio repositorio = null;
        RepositorioSrv instance = new RepositorioSrv();
        long expResult = 0L;
        long result = instance.actualizarRepositorio(repositorio);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertarRepositorio method, of class RepositorioSrv.
     */
    @Test
    public void testInsertarRepositorio_List() {
        System.out.println("insertarRepositorio");
        List<Repositorio> repositorios = null;
        RepositorioSrv instance = new RepositorioSrv();
        long expResult = 0L;
        long result = instance.insertarRepositorio(repositorios);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertarRepositorio method, of class RepositorioSrv.
     */
    @Test
    public void testInsertarRepositorio_Repositorio() {
        System.out.println("insertarRepositorio");
        Repositorio repositorio = null;
        RepositorioSrv instance = new RepositorioSrv();
        long expResult = 0L;
        long result = instance.insertarRepositorio(repositorio);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRepositorios method, of class RepositorioSrv.
     */
    @Test
    public void testGetRepositorios() throws Exception {
        System.out.println("getRepositorios");
        RepositorioSrv instance = new RepositorioSrv();
        List<Repositorio> expResult = null;
        List<Repositorio> result = instance.getRepositorios();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
