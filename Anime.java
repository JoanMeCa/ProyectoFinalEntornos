/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

/**
 * Clase que representa un anime.
 * 
 * @author Party
 */
public class Anime {
    private int id; // Identificador único del anime
    private String nombre; // Nombre del anime
    private int autorId; // ID del autor del anime
    private int mangaId; // ID del manga asociado al anime
    private String estado; // Estado actual del anime
    private int numCapitulos; // Número de capítulos del anime

    /**
     * Constructor de la clase Anime.
     * 
     * @param id Identificador único del anime.
     * @param nombre Nombre del anime.
     * @param autorId ID del autor del anime.
     * @param mangaId ID del manga asociado al anime.
     * @param estado Estado actual del anime.
     * @param numCapitulos Número de capítulos del anime.
     */
    public Anime(int id, String nombre, int autorId, int mangaId, String estado, int numCapitulos) {
        this.id = id;
        this.nombre = nombre;
        this.autorId = autorId;
        this.mangaId = mangaId;
        this.estado = estado;
        this.numCapitulos = numCapitulos;
    }

    // Getters y Setters

    /**
     * Obtiene el identificador único del anime.
     * 
     * @return El identificador único del anime.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del anime.
     * 
     * @param id El nuevo identificador único del anime.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del anime.
     * 
     * @return El nombre del anime.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del anime.
     * 
     * @param nombre El nuevo nombre del anime.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el ID del autor del anime.
     * 
     * @return El ID del autor del anime.
     */
    public int getAutorId() {
        return autorId;
    }

    /**
     * Establece el ID del autor del anime.
     * 
     * @param autorId El nuevo ID del autor del anime.
     */
    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    /**
     * Obtiene el ID del manga asociado al anime.
     * 
     * @return El ID del manga asociado al anime.
     */
    public int getMangaId() {
        return mangaId;
    }

    /**
     * Establece el ID del manga asociado al anime.
     * 
     * @param mangaId El nuevo ID del manga asociado al anime.
     */
    public void setMangaId(int mangaId) {
        this.mangaId = mangaId;
    }

    /**
     * Obtiene el estado actual del anime.
     * 
     * @return El estado actual del anime.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual del anime.
     * 
     * @param estado El nuevo estado del anime.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el número de capítulos del anime.
     * 
     * @return El número de capítulos del anime.
     */
    public int getNumCapitulos() {
        return numCapitulos;
    }

    /**
     * Establece el número de capítulos del anime.
     * 
     * @param numCapitulos El nuevo número de capítulos del anime.
     */
    public void setNumCapitulos(int numCapitulos) {
        this.numCapitulos = numCapitulos;
    }
}