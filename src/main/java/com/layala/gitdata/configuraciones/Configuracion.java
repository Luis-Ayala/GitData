package com.layala.gitdata.configuraciones;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de configuraciones
 * @author Luis
 */
public final class Configuracion {
    
    /**
     * Obtiene el valor de una propiedad dada de config.properties
     * @param llave Llave de la propiedad a buscar
     * @return El valor de la propiedad
     */
    public final static String getProperty(String llave) {
        final Properties prop = new Properties();
        try {
            final String archivo = "config.properties";
            final InputStream input = Configuracion.class.getClassLoader().getResourceAsStream(archivo);
            if (input == null) {
                throw new RuntimeException("No se pudo cargar el archivo de propiedades");
            }
            
            prop.load(input);
        } catch (IOException ex) {
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prop.getProperty(llave);
    }
    
    /**
     * Crea la conexion a mongo
     * @return MongoClient
     */
    public final static MongoClient crearConexion() {
        final String SERVIDOR = Configuracion.getProperty("servidor");
        final String PUERTO = Configuracion.getProperty("puerto");
        final String CONEXION = new StringBuilder().append("mongodb://").append(SERVIDOR).append(":").append(PUERTO).toString();
        final MongoClient cliente = MongoClients.create(CONEXION);
        return cliente;
    }
}
