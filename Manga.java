/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

/**
 *
 * @author Party
 */
public class Manga {
    private int id;
    private String nombre;
    private int autorId;
    private String estado;
    private int numCapitulos;

    /**
     *
     * @param id
     * @param nombre
     * @param autorId
     * @param estado
     * @param numCapitulos
     */
    public Manga(int id, String nombre, int autorId, String estado, int numCapitulos) {
        this.id = id;
        this.nombre = nombre;
        this.autorId = autorId;
        this.estado = estado;
        this.numCapitulos = numCapitulos;
    }

    // Getters y Setters

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public int getAutorId() {
        return autorId;
    }

    /**
     *
     * @param autorId
     */
    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    /**
     *
     * @return
     */
    public String getEstado() {
        return estado;
    }

    /**
     *
     * @param estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     *
     * @return
     */
    public int getNumCapitulos() {
        return numCapitulos;
    }

    /**
     *
     * @param numCapitulos
     */
    public void setNumCapitulos(int numCapitulos) {
        this.numCapitulos = numCapitulos;
    }
}
