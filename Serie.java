/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

/**
 * Clase que representa una serie en el sistema.
 * Contiene información como el ID, ID del anime asociado, ID del manga asociado y ID del autor asociado.
 * 
 * @author Party
 */
public class Serie {
    private int id;         // ID de la serie
    private int idAnime;    // ID del anime asociado a la serie
    private int idManga;    // ID del manga asociado a la serie
    private int idAutor;    // ID del autor asociado a la serie

    /**
     * Constructor de la clase Serie.
     * 
     * @param id      ID de la serie
     * @param idAnime ID del anime asociado a la serie
     * @param idManga ID del manga asociado a la serie
     * @param idAutor ID del autor asociado a la serie
     */
    public Serie(int id, int idAnime, int idManga, int idAutor) {
        this.id = id;
        this.idAnime = idAnime;
        this.idManga = idManga;
        this.idAutor = idAutor;
    }

    // Getters y Setters

    /**
     * Método para obtener el ID de la serie.
     * 
     * @return El ID de la serie
     */
    public int getId() {
        return id;
    }

    /**
     * Método para establecer el ID de la serie.
     * 
     * @param id El ID de la serie a establecer
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Método para obtener el ID del anime asociado a la serie.
     * 
     * @return El ID del anime asociado a la serie
     */
    public int getIdAnime() {
        return idAnime;
    }

    /**
     * Método para establecer el ID del anime asociado a la serie.
     * 
     * @param idAnime El ID del anime asociado a la serie a establecer
     */
    public void setIdAnime(int idAnime) {
        this.idAnime = idAnime;
    }

    /**
     * Método para obtener el ID del manga asociado a la serie.
     * 
     * @return El ID del manga asociado a la serie
     */
    public int getIdManga() {
        return idManga;
    }

    /**
     * Método para establecer el ID del manga asociado a la serie.
     * 
     * @param idManga El ID del manga asociado a la serie a establecer
     */
    public void setIdManga(int idManga) {
        this.idManga = idManga;
    }

    /**
     * Método para obtener el ID del autor asociado a la serie.
     * 
     * @return El ID del autor asociado a la serie
     */
    public int getIdAutor() {
        return idAutor;
    }

    /**
     * Método para establecer el ID del autor asociado a la serie.
     * 
     * @param idAutor El ID del autor asociado a la serie a establecer
     */
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }
}