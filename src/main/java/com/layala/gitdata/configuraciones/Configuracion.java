package com.layala.gitdata.configuraciones;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Luis
 */
public abstract class Configuracion {
    private final static String USUARIO = "Luis-Ayala";
    private final static String PWD     = "kyo_1983!!";
    
    public final static String USUARIO_K = "USUARIO";
    public final static String PWD_K     = "PWD";

    public static Map<String, String> getCredenciales() {
        Map<String, String> credenciales = new HashMap<>();
        
        credenciales.put(Configuracion.USUARIO_K,  Configuracion.USUARIO);
        credenciales.put(Configuracion.PWD_K,      Configuracion.PWD);
        
        return credenciales;
    }
    
}
