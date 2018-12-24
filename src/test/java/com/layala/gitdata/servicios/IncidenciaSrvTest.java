/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.layala.gitdata.servicios;

import com.layala.gitdata.entidades.Incidencia;
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
public class IncidenciaSrvTest {
    
    public IncidenciaSrvTest() {
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
     * Test of actualizarIncidencia method, of class IncidenciaSrv.
     */
    @Test
    public void testActualizarIncidencia_List() {
        System.out.println("actualizarIncidencia");
        List<Incidencia> incidencias = null;
        IncidenciaSrv instance = new IncidenciaSrv();
        long expResult = 0L;
        long result = instance.actualizarIncidencia(incidencias);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of actualizarIncidencia method, of class IncidenciaSrv.
     */
    @Test
    public void testActualizarIncidencia_Incidencia() {
        System.out.println("actualizarIncidencia");
        Incidencia incidencia = null;
        IncidenciaSrv instance = new IncidenciaSrv();
        long expResult = 0L;
        long result = instance.actualizarIncidencia(incidencia);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertarIncidencia method, of class IncidenciaSrv.
     */
    @Test
    public void testInsertarIncidencia_List() {
        System.out.println("insertarIncidencia");
        List<Incidencia> incidencias = null;
        IncidenciaSrv instance = new IncidenciaSrv();
        long expResult = 0L;
        long result = instance.insertarIncidencia(incidencias);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertarIncidencia method, of class IncidenciaSrv.
     */
    @Test
    public void testInsertarIncidencia_Incidencia() {
        System.out.println("insertarIncidencia");
        Incidencia incidencia = null;
        IncidenciaSrv instance = new IncidenciaSrv();
        long expResult = 0L;
        long result = instance.insertarIncidencia(incidencia);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIncidenciasPorRepositorio method, of class IncidenciaSrv.
     */
    @Test
    public void testGetIncidenciasPorRepositorio() throws Exception {
        System.out.println("getIncidenciasPorRepositorio");
        Repositorio repositorio = null;
        IncidenciaSrv instance = new IncidenciaSrv();
        List<Incidencia> expResult = null;
        List<Incidencia> result = instance.getIncidenciasPorRepositorio(repositorio);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
