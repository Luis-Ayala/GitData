package com.layala.gitdata.excepciones;

/**
 * Excepción de la clase servicio de los repositorios
 * 
 * @author Luis Ayala
 * @version 1.0
 * @since 1.0
 */
public class GitDataRepositorioExcepcion extends GitDataException{
    
    /**
     * Construye una excepción nueva con un mensaje específico.
     * 
     * @param mensaje El mensaje de la excepción.
     */
    public GitDataRepositorioExcepcion(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Construye una excepción nueva con un mensaje específico y una causa.
     * 
     * @param mensaje El mensaje de la excepción.
     * @param causa La causa de la excepción.
     */
    public GitDataRepositorioExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Construye una excepción nueva con la causa.
     * 
     * @param causa La causa de la excepción.
     */
    public GitDataRepositorioExcepcion(Throwable causa) {
        super(causa);
    }
}
