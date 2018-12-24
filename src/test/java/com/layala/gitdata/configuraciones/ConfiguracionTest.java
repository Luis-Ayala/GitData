package com.layala.gitdata.configuraciones;

import com.mongodb.client.MongoClient;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para los métodos de la clase Configuracion
 * @author Luis
 */
public class ConfiguracionTest {
    
    public ConfiguracionTest() {
    }
    
    /**
     * Prueba para el método getProperty, de la clase Configuracion.
     */
    @Test
    public void testGetProperty() {
        String llave = "puerto";
        String expResultado = "27017";
        String resultado = Configuracion.getProperty(llave);
        
        assertNotNull(resultado);
        assertEquals(expResultado, resultado);
    }

    /**
     * Test of crearConexion method, of class Configuracion.
     */
    @Test
    public void testCrearConexion() {
        MongoClient resultado = Configuracion.crearConexion();
        
        assertNotNull(resultado);
        resultado.close();
    }
    
}
