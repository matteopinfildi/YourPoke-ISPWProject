-- Creazione del database (se non esiste già)
CREATE DATABASE IF NOT EXISTS yourPoke;

-- Seleziona il database da usare
USE yourPoke;

-- Creazione della tabella pokeLab (per le chitarre)
CREATE TABLE IF NOT EXISTS pokeLab (
    id INT PRIMARY KEY,
    price DECIMAL(10, 2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS pokeLabIngredients (
    plid INT,
    ingredientName VARCHAR(100) NOT NULL ,
    ingredientAlternative VARCHAR(100) NOT NULL ,
    PRIMARY KEY (plid, ingredientAlternative),
    FOREIGN KEY (plid) REFERENCES pokeLab(id) ON DELETE CASCADE
    );

-- Creazione della tabella users (per gli utenti)
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    utype ENUM('USER', 'PREMIUMUSER') NOT NULL,
    address VARCHAR(255),
    plid INT DEFAULT NULL,
    FOREIGN KEY (plid) REFERENCES yourPoke(id) ON DELETE SET NULL
    );