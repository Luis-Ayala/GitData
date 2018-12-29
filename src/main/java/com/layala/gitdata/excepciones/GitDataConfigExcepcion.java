package com.layala.gitdata.excepciones;

/**
 * Excepción de la clase Configuracion
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public class GitDataConfigExcepcion extends GitDataException {
    
    /**
     * Construye una excepción nueva con un mensaje específico.
     * 
     * @param mensaje El mensaje de la excepción.
     * @since 1.0
     */
    public GitDataConfigExcepcion(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Construye una excepción nueva con un mensaje específico y una causa.
     * 
     * @param mensaje El mensaje de la excepción.
     * @param causa La causa de la excepción.
     * @since 1.0
     */
    public GitDataConfigExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Construye una excepción nueva con la causa.
     * 
     * @param causa La causa de la excepción.
     * @since 1.0
     */
    public GitDataConfigExcepcion(Throwable causa) {
        super(causa);
    }
}
