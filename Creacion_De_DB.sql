CREATE TABLE Autores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255),
    estado VARCHAR(50)
);

CREATE TABLE Manga (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255),
    autor_id INT,
    estado VARCHAR(50),
    num_capitulos INT,
    FOREIGN KEY (autor_id) REFERENCES Autores(id)
);

CREATE TABLE Animes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255),
    autor_id INT,
    manga_id INT,
    estado VARCHAR(50),
    num_capitulos INT,
    FOREIGN KEY (autor_id) REFERENCES Autores(id),
    FOREIGN KEY (manga_id) REFERENCES Manga(id)
);

CREATE TABLE Series (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_anime INT,
    id_manga INT,
    id_autor INT,
    FOREIGN KEY (id_anime) REFERENCES Animes(id),
    FOREIGN KEY (id_manga) REFERENCES Manga(id),
    FOREIGN KEY (id_autor) REFERENCES Autores(id)
);

