-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema your_poke
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema your_poke
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `your_poke` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `your_poke` ;

-- -----------------------------------------------------
-- Table `your_poke`.`poke_lab`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `your_poke`.`poke_lab` (
  `id` INT NOT NULL,
  `price` DOUBLE NOT NULL,
  `size` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `your_poke`.`poke_lab_ingredients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `your_poke`.`poke_lab_ingredients` (
  `plid` INT NOT NULL,
  `ingredient_name` VARCHAR(255) NOT NULL,
  `ingredient_alternative` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`plid`, `ingredient_name`),
  CONSTRAINT `pl_id`
    FOREIGN KEY (`plid`)
    REFERENCES `your_poke`.`poke_lab` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `your_poke`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `your_poke`.`users` (
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `utype` ENUM('USER', 'PREMIUMUSER') NOT NULL,
  `address` VARCHAR(255) NULL DEFAULT NULL,
  `plid` INT NULL DEFAULT NULL,
  PRIMARY KEY (`username`),
  INDEX `plid_idx` (`plid` ASC) VISIBLE,
  CONSTRAINT `plid`
    FOREIGN KEY (`plid`)
    REFERENCES `your_poke`.`poke_lab` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `your_poke`.`poke_wall_posts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `your_poke`.`poke_wall_posts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `poke_name` VARCHAR(100) NOT NULL,
  `size` VARCHAR(20) NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `username` (`username` ASC) VISIBLE,
  CONSTRAINT `poke_wall_posts_ibfk_1`
    FOREIGN KEY (`username`)
    REFERENCES `your_poke`.`users` (`username`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `your_poke`.`poke_wall_ingredients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `your_poke`.`poke_wall_ingredients` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `post_id` INT NOT NULL,
  `ingredient` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `post_id` (`post_id` ASC) VISIBLE,
  CONSTRAINT `poke_wall_ingredients_ibfk_1`
    FOREIGN KEY (`post_id`)
    REFERENCES `your_poke`.`poke_wall_posts` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
