package com.layala.gitdata.excepciones;

/**
 * Excepción base para los diferentes tipos de excepciones del sistema
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public class GitDataException extends Exception {
    
    /**
     * Construye una excepción nueva con un mensaje específico.
     * 
     * @param mensaje El mensaje de la excepción.
     * @since 1.0
     */
    public GitDataException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Construye una excepción nueva con un mensaje específico y una causa.
     * 
     * @param mensaje El mensaje de la excepción.
     * @param causa La causa de la excepción.
     * @since 1.0
     */
    public GitDataException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Construye una excepción nueva con la causa.
     * 
     * @param causa La causa de la excepción.
     * @since 1.0
     */
    public GitDataException(Throwable causa) {
        super(causa);
    }
}
