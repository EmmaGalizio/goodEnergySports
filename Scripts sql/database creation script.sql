-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema goodenergysport
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema goodenergysport
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `goodenergysport` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;
USE `goodenergysport` ;

-- -----------------------------------------------------
-- Table `goodenergysport`.`ambito_estado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`ambito_estado` (
  `id_ambito_estado` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_ambito_estado`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`categoria` (
  `id_categoria` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(60) NOT NULL,
  `descripcion` VARCHAR(200) NULL DEFAULT NULL,
  `fecha_baja` DATE NULL DEFAULT NULL,
  `categoria_superior` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_categoria`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC) VISIBLE,
  INDEX `categoria_categoriaSuperior_fk_idx` (`categoria_superior` ASC) VISIBLE,
  CONSTRAINT `categoria_categoriaSuperior_fk`
    FOREIGN KEY (`categoria_superior`)
    REFERENCES `goodenergysport`.`categoria` (`id_categoria`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`provincia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`provincia` (
  `id_provincia` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`id_provincia`))
ENGINE = InnoDB
AUTO_INCREMENT = 95
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`localidad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`localidad` (
  `id_localidad` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(60) NOT NULL,
  `codigo_postal` INT NULL DEFAULT NULL,
  `provincia` INT NOT NULL,
  PRIMARY KEY (`id_localidad`),
  INDEX `localidad_provincia_fk` (`provincia` ASC) VISIBLE,
  CONSTRAINT `localidad_provincia_fk`
    FOREIGN KEY (`provincia`)
    REFERENCES `goodenergysport`.`provincia` (`id_provincia`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1818
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`domicilio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`domicilio` (
  `id_domicilio` INT NOT NULL AUTO_INCREMENT,
  `calle` VARCHAR(100) NOT NULL,
  `numero` INT NOT NULL,
  `piso` INT NULL DEFAULT NULL,
  `dpto` VARCHAR(5) NULL DEFAULT NULL,
  `referencia` VARCHAR(200) NULL DEFAULT NULL,
  `localidad` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_domicilio`),
  INDEX `domicilio_localidad_fk` (`localidad` ASC) VISIBLE,
  CONSTRAINT `domicilio_localidad_fk`
    FOREIGN KEY (`localidad`)
    REFERENCES `goodenergysport`.`localidad` (`id_localidad`))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`tipo_documento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`tipo_documento` (
  `longitud` INT NULL DEFAULT NULL,
  `tipo_documento` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`tipo_documento`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`rol_usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`rol_usuario` (
  `id_rol_usuario` INT NOT NULL AUTO_INCREMENT,
  `rol` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `descripcion` VARCHAR(200) NULL DEFAULT NULL,
  PRIMARY KEY (`id_rol_usuario`),
  UNIQUE INDEX `rol_uq` (`rol` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `goodenergysport`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`usuario` (
  `email` VARCHAR(100) NOT NULL,
  `usuario` VARCHAR(100) NOT NULL,
  `password` VARCHAR(300) NOT NULL,
  `salt` VARCHAR(50) NULL DEFAULT NULL,
  `fecha_alta` DATE NOT NULL,
  `fecha_baja` DATE NULL DEFAULT NULL,
  `rol` INT NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE INDEX `usuario_UNIQUE` (`usuario` ASC) VISIBLE,
  INDEX `usuario_rolUsuario_idx` (`rol` ASC) VISIBLE,
  CONSTRAINT `usuario_rol_usuario_fk`
    FOREIGN KEY (`rol`)
    REFERENCES `goodenergysport`.`rol_usuario` (`id_rol_usuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`cliente` (
  `nombres` VARCHAR(200) NOT NULL,
  `apellidos` VARCHAR(200) NOT NULL,
  `telefono` VARCHAR(20) NULL DEFAULT NULL,
  `usuario` VARCHAR(100) NOT NULL,
  `domicilio` INT NOT NULL,
  `codigo_cliente` INT NOT NULL AUTO_INCREMENT,
  `nro_documento` VARCHAR(11) NULL DEFAULT NULL,
  `tipo_documento` VARCHAR(20) NULL DEFAULT NULL,
  PRIMARY KEY (`codigo_cliente`),
  UNIQUE INDEX `documento_uq` (`nro_documento` ASC) VISIBLE,
  INDEX `usuario_idx` (`usuario` ASC) VISIBLE,
  INDEX `cliente_domicilio_fk_idx` (`domicilio` ASC) VISIBLE,
  INDEX `cliente_tipo_documento_fk` (`tipo_documento` ASC) VISIBLE,
  CONSTRAINT `cliente_domicilio_fk`
    FOREIGN KEY (`domicilio`)
    REFERENCES `goodenergysport`.`domicilio` (`id_domicilio`),
  CONSTRAINT `cliente_tipo_documento_fk`
    FOREIGN KEY (`tipo_documento`)
    REFERENCES `goodenergysport`.`tipo_documento` (`tipo_documento`),
  CONSTRAINT `usuario_cliente_fk`
    FOREIGN KEY (`usuario`)
    REFERENCES `goodenergysport`.`usuario` (`email`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`producto` (
  `codigo_producto` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(200) NULL DEFAULT NULL,
  `precio` DECIMAL(9,2) NOT NULL,
  `categoria` INT NOT NULL,
  `fecha_alta` DATE NOT NULL,
  `fecha_baja` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`codigo_producto`),
  INDEX `producto_categoria_idx` (`categoria` ASC) VISIBLE,
  CONSTRAINT `producto_categoria`
    FOREIGN KEY (`categoria`)
    REFERENCES `goodenergysport`.`categoria` (`id_categoria`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`talle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`talle` (
  `talle` VARCHAR(6) NOT NULL,
  `equivalencia` VARCHAR(45) NULL DEFAULT NULL,
  `categoria_producto` INT NOT NULL,
  `fecha_baja` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`talle`, `categoria_producto`),
  INDEX `talle_categoria_fk_idx` (`categoria_producto` ASC) VISIBLE,
  CONSTRAINT `talle_categoria_fk`
    FOREIGN KEY (`categoria_producto`)
    REFERENCES `goodenergysport`.`categoria` (`id_categoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`stock`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`stock` (
  `producto` INT NOT NULL,
  `talle` VARCHAR(6) NOT NULL,
  `stock_disponible` INT NOT NULL,
  `categoria_talle` INT NOT NULL,
  `fecha_baja` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`producto`, `talle`, `categoria_talle`),
  INDEX `stock_talle_talle_fk_idx` (`talle` ASC, `categoria_talle` ASC) VISIBLE,
  CONSTRAINT `stock_producto_fk`
    FOREIGN KEY (`producto`)
    REFERENCES `goodenergysport`.`producto` (`codigo_producto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `stock_talle_fk`
    FOREIGN KEY (`talle` , `categoria_talle`)
    REFERENCES `goodenergysport`.`talle` (`talle` , `categoria_producto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`detalle_carrito`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`detalle_carrito` (
  `usuario` VARCHAR(100) NOT NULL,
  `producto` INT NOT NULL,
  `talle` VARCHAR(6) NOT NULL,
  `cantidad` INT NOT NULL,
  `categoria_talle` INT NOT NULL,
  PRIMARY KEY (`usuario`, `producto`, `talle`, `categoria_talle`),
  INDEX `detalle_carrito_stock_idx` (`producto` ASC, `talle` ASC, `categoria_talle` ASC) VISIBLE,
  CONSTRAINT `detalle_carrito_stock_fk`
    FOREIGN KEY (`producto` , `talle` , `categoria_talle`)
    REFERENCES `goodenergysport`.`stock` (`producto` , `talle` , `categoria_talle`),
  CONSTRAINT `detalleCarrito_usuario_fk`
    FOREIGN KEY (`usuario`)
    REFERENCES `goodenergysport`.`usuario` (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`estado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`estado` (
  `id_estado` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(200) NULL DEFAULT NULL,
  `ambito` INT NOT NULL,
  PRIMARY KEY (`id_estado`),
  INDEX `estado_ambitoEstado_fk_idx` (`ambito` ASC) VISIBLE,
  CONSTRAINT `estado_ambitoEstado_fk`
    FOREIGN KEY (`ambito`)
    REFERENCES `goodenergysport`.`ambito_estado` (`id_ambito_estado`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`pedido` (
  `codigo_pedido` INT NOT NULL,
  `fecha` DATETIME(2) NOT NULL,
  `estado` INT NOT NULL,
  `codigo_cliente` INT NOT NULL,
  PRIMARY KEY (`codigo_pedido`),
  INDEX `pedido_estado_fk_idx` (`estado` ASC) VISIBLE,
  INDEX `pedido_cliente` (`codigo_cliente` ASC) VISIBLE,
  CONSTRAINT `pedido_cliente`
    FOREIGN KEY (`codigo_cliente`)
    REFERENCES `goodenergysport`.`cliente` (`codigo_cliente`),
  CONSTRAINT `pedido_estado_fk`
    FOREIGN KEY (`estado`)
    REFERENCES `goodenergysport`.`estado` (`id_estado`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`detalle_pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`detalle_pedido` (
  `codigo_pedido` INT NOT NULL,
  `producto` INT NOT NULL,
  `talle` VARCHAR(6) NOT NULL,
  `cantidad` INT NOT NULL,
  `precio_unitario` DECIMAL(9,2) NOT NULL,
  `categoria_talle` INT NOT NULL,
  PRIMARY KEY (`codigo_pedido`, `producto`, `talle`, `categoria_talle`),
  INDEX `detalle_pedido_stock_idx` (`producto` ASC, `talle` ASC, `categoria_talle` ASC) VISIBLE,
  CONSTRAINT `detalle_pedido_stock`
    FOREIGN KEY (`producto` , `talle` , `categoria_talle`)
    REFERENCES `goodenergysport`.`stock` (`producto` , `talle` , `categoria_talle`),
  CONSTRAINT `detallePedido_pedido_fk`
    FOREIGN KEY (`codigo_pedido`)
    REFERENCES `goodenergysport`.`pedido` (`codigo_pedido`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`imagen_producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`imagen_producto` (
  `url` VARCHAR(200) NOT NULL,
  `producto` INT NOT NULL,
  `orden` INT NULL DEFAULT NULL,
  PRIMARY KEY (`url`),
  UNIQUE INDEX `urlImagen_UNIQUE` (`url` ASC) VISIBLE,
  INDEX `imagen_producto_fk_idx` (`producto` ASC) VISIBLE,
  CONSTRAINT `imagen_producto_fk`
    FOREIGN KEY (`producto`)
    REFERENCES `goodenergysport`.`producto` (`codigo_producto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `goodenergysport`.`permiso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`permiso` (
  `url` VARCHAR(100) NOT NULL,
  `metodo` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`url`, `metodo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_bin;


-- -----------------------------------------------------
-- Table `goodenergysport`.`permiso_x_rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`permiso_x_rol` (
  `id_rol_usuario` INT NOT NULL,
  `url` VARCHAR(100) NOT NULL,
  `metodo` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id_rol_usuario`, `url`, `metodo`),
  INDEX `permiso_fk` (`url` ASC, `metodo` ASC) VISIBLE,
  CONSTRAINT `permiso_fk`
    FOREIGN KEY (`url` , `metodo`)
    REFERENCES `goodenergysport`.`permiso` (`url` , `metodo`),
  CONSTRAINT `rol_fk`
    FOREIGN KEY (`id_rol_usuario`)
    REFERENCES `goodenergysport`.`rol_usuario` (`id_rol_usuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_bin;


-- -----------------------------------------------------
-- Table `goodenergysport`.`producto_metadata`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodenergysport`.`producto_metadata` (
  `producto` INT NOT NULL,
  `clave` VARCHAR(45) NOT NULL,
  `valor` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`producto`, `clave`),
  CONSTRAINT `producto_metadata_producto_fk`
    FOREIGN KEY (`producto`)
    REFERENCES `goodenergysport`.`producto` (`codigo_producto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
