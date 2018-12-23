package com.layala.gitdata.configuraciones;

import com.layala.gitdata.servicios.RepositorioSrv;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.bson.Document;

/**
 *
 * @author Luis
 */
public final class Configuracion {

    private final static String USUARIO = "Luis-Ayala";
    private final static String PWD = "kyo_1983!!";

    public final static String USUARIO_K = "USUARIO";
    public final static String PWD_K = "PWD";

    public static Map<String, String> getCredenciales() {
        Map<String, String> credenciales = new HashMap<>();

        credenciales.put(Configuracion.USUARIO_K, Configuracion.USUARIO);
        credenciales.put(Configuracion.PWD_K, Configuracion.PWD);

        return credenciales;
    }

    public static String getProperty(String llave) {
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
    
    final static public MongoClient crearConexion() {
        final String SERVIDOR = Configuracion.getProperty("servidor");
        final String PUERTO = Configuracion.getProperty("puerto");
        final String CONEXION = new StringBuilder().append("mongodb://").append(SERVIDOR).append(":").append(PUERTO).toString();
        
        final MongoClient cliente = MongoClients.create(CONEXION);
        return cliente;
    }
}
