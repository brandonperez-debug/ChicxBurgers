-- ============================================================
-- BASE DE DATOS: ChicxBurger
-- ============================================================
DROP DATABASE IF EXISTS ChicxBurger;
 
CREATE DATABASE IF NOT EXISTS ChicxBurger;
 
USE ChicxBurger;
 
-- ------------------------------------------------------------
-- Tabla: USUARIO
-- ------------------------------------------------------------
CREATE TABLE USUARIO (
    id_usuario      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    usuario_login   VARCHAR(50)  NOT NULL UNIQUE,
    contrasena      VARCHAR(255) NOT NULL,
    rol             ENUM('Administrador','Cajero') NOT NULL,
    estado          TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT chk_usuario_contrasena_longitud
        CHECK (CHAR_LENGTH(contrasena) >= 6)
);
 
-- ------------------------------------------------------------
-- Tabla: CATEGORIA
-- ------------------------------------------------------------
CREATE TABLE CATEGORIA (
    id_categoria     INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(50)  NOT NULL,
    descripcion      VARCHAR(150) NULL
);
 
-- ------------------------------------------------------------
-- Tabla: TIEMPO_COMIDA
-- ------------------------------------------------------------
CREATE TABLE TIEMPO_COMIDA (
    id_tiempo_comida INT AUTO_INCREMENT PRIMARY KEY,
    nombre_horario   VARCHAR(30) NOT NULL,
    hora_inicio      TIME NULL,
    hora_fin         TIME NULL
);
 
-- ------------------------------------------------------------
-- Tabla: METODO_PAGO
-- ------------------------------------------------------------
CREATE TABLE METODO_PAGO (
    id_metodo_pago INT AUTO_INCREMENT PRIMARY KEY,
    nombre_metodo  VARCHAR(30)  NOT NULL,
    descripcion    VARCHAR(100) NULL
);
 
-- ------------------------------------------------------------
-- Tabla: PROVEEDOR
-- ------------------------------------------------------------
CREATE TABLE PROVEEDOR (
    id_proveedor     INT AUTO_INCREMENT PRIMARY KEY,
    nombre_proveedor VARCHAR(100) NOT NULL,
    telefono         VARCHAR(20)  NULL,
    correo           VARCHAR(100) NULL,
    direccion        VARCHAR(150) NULL
);
 
-- ------------------------------------------------------------
-- Tabla: PROMOCION
-- ------------------------------------------------------------
CREATE TABLE PROMOCION (
    id_promocion     INT AUTO_INCREMENT PRIMARY KEY,
    nombre_promocion VARCHAR(100) NOT NULL,
    descripcion      VARCHAR(200) NULL,
    tipo_descuento   ENUM('Porcentaje','Monto Fijo') NOT NULL,
    valor_descuento  DECIMAL(10,2) NOT NULL,
    fecha_inicio     DATE NOT NULL,
    fecha_fin        DATE NOT NULL,
    estado           TINYINT(1) NOT NULL DEFAULT 1
);
 
-- ------------------------------------------------------------
-- Tabla: PRODUCTO
-- ------------------------------------------------------------
CREATE TABLE PRODUCTO (
    id_producto      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto  VARCHAR(100) NOT NULL,
    descripcion      VARCHAR(200) NULL,
    precio           DECIMAL(10,2) NOT NULL,
    id_categoria     INT NOT NULL,
    id_tiempo_comida INT NOT NULL,
    estado           TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id_categoria)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_producto_tiempo_comida
        FOREIGN KEY (id_tiempo_comida) REFERENCES TIEMPO_COMIDA(id_tiempo_comida)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
 
-- ------------------------------------------------------------
-- Tabla: INGREDIENTE
-- ------------------------------------------------------------
CREATE TABLE INGREDIENTE (
    id_ingrediente     INT AUTO_INCREMENT PRIMARY KEY,
    nombre_ingrediente VARCHAR(100) NOT NULL,
    unidad_medida      VARCHAR(20)  NOT NULL,
    stock_actual       DECIMAL(10,2) NOT NULL DEFAULT 0,
    stock_minimo       DECIMAL(10,2) NOT NULL DEFAULT 0,
    id_proveedor       INT NOT NULL,
    CONSTRAINT fk_ingrediente_proveedor
        FOREIGN KEY (id_proveedor) REFERENCES PROVEEDOR(id_proveedor)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
 
-- ------------------------------------------------------------
-- Tabla: PRODUCTO_INGREDIENTE
-- ------------------------------------------------------------
CREATE TABLE PRODUCTO_INGREDIENTE (
    id_producto        INT NOT NULL,
    id_ingrediente     INT NOT NULL,
    cantidad_requerida DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_producto, id_ingrediente),
    CONSTRAINT fk_pi_producto
        FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_pi_ingrediente
        FOREIGN KEY (id_ingrediente) REFERENCES INGREDIENTE(id_ingrediente)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
 
-- ------------------------------------------------------------
-- Tabla: TURNO
-- ------------------------------------------------------------
CREATE TABLE TURNO (
    id_turno       INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario     INT NOT NULL,
    fecha_apertura DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre   DATETIME NULL,
    monto_inicial  DECIMAL(10,2) NOT NULL DEFAULT 0,
    monto_final    DECIMAL(10,2) NULL,
    estado         ENUM('Abierto','Cerrado') NOT NULL DEFAULT 'Abierto',
    CONSTRAINT fk_turno_usuario
        FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
 
-- ------------------------------------------------------------
-- Tabla: VENTA
-- ------------------------------------------------------------
CREATE TABLE VENTA (
    id_venta        INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total           DECIMAL(10,2) NOT NULL DEFAULT 0,
    descuento_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    id_usuario      INT NOT NULL,
    id_metodo_pago  INT NOT NULL,
    id_turno        INT NOT NULL,
    id_promocion    INT NULL,
    CONSTRAINT fk_venta_usuario
        FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_venta_metodo_pago
        FOREIGN KEY (id_metodo_pago) REFERENCES METODO_PAGO(id_metodo_pago)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_venta_turno
        FOREIGN KEY (id_turno) REFERENCES TURNO(id_turno)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_venta_promocion
        FOREIGN KEY (id_promocion) REFERENCES PROMOCION(id_promocion)
        ON UPDATE CASCADE ON DELETE SET NULL
);
 
-- ------------------------------------------------------------
-- Tabla: DETALLE_VENTA
-- ------------------------------------------------------------
CREATE TABLE DETALLE_VENTA (
    id_detalle_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_venta         INT NOT NULL,
    id_producto      INT NOT NULL,
    cantidad         INT NOT NULL DEFAULT 1,
    precio_unitario  DECIMAL(10,2) NOT NULL,
    subtotal         DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_venta
        FOREIGN KEY (id_venta) REFERENCES VENTA(id_venta)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
 
-- ============================================================
-- DATOS DE PRUEBA
-- ============================================================
 
INSERT INTO USUARIO (nombre_completo, usuario_login, contrasena, rol, estado) VALUES
('Anthony Perez', 'admin1', '123456', 'Administrador', 1),
('Brandon Perez', 'cajero1', '123456', 'Cajero', 2);