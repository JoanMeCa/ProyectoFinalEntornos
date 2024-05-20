/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

/**
 * Clase que representa a un autor en el sistema.
 * Cada autor tiene un ID único, un nombre y un estado.
 * 
 * @author Party
 */
public class Autor {
    private int id;         // ID del autor
    private String nombre;  // Nombre del autor
    private String estado;  // Estado del autor

    /**
     * Constructor de la clase Autor.
     * 
     * @param id      El ID del autor.
     * @param nombre  El nombre del autor.
     * @param estado  El estado del autor.
     */
    public Autor(int id, String nombre, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    // Getters y Setters

    /**
     * Método para obtener el ID del autor.
     * 
     * @return El ID del autor.
     */
    public int getId() {
        return id;
    }

    /**
     * Método para establecer el ID del autor.
     * 
     * @param id El ID del autor.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Método para obtener el nombre del autor.
     * 
     * @return El nombre del autor.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para establecer el nombre del autor.
     * 
     * @param nombre El nombre del autor.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para obtener el estado del autor.
     * 
     * @return El estado del autor.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Método para establecer el estado del autor.
     * 
     * @param estado El estado del autor.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}