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
('Anthony Pérez', 'admin1',  '123456', 'Administrador', 1),
('Anthony Boch',  'cajero1', '123456', 'Cajero',        1);

INSERT INTO PROVEEDOR (nombre_proveedor, telefono, correo, direccion) VALUES
('Distribuidora Central', '2222-1111', 'ventas@distcentral.com', 'Zona 4, Guatemala');

INSERT INTO METODO_PAGO (nombre_metodo, descripcion) VALUES
('Efectivo', 'Pago en efectivo'),
('Tarjeta',  'Pago con tarjeta');

INSERT INTO TIEMPO_COMIDA (nombre_horario, hora_inicio, hora_fin) VALUES
('Todo el dia', '00:00:00', '23:59:59');

INSERT INTO CATEGORIA (nombre_categoria, descripcion) VALUES
('Hamburguesas', 'Hamburguesas de la casa'),
('Bebidas',      'Bebidas frias');

INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) VALUES
('Hamburguesa Clasica', 'Carne, queso, lechuga',     35.00, 1, 1, 1),
('Hamburguesa BBQ',     'Carne, tocino, salsa BBQ',  42.00, 1, 1, 1),
('Coca Cola',           'Bebida gaseosa 12oz',       12.00, 2, 1, 1);

INSERT INTO TURNO (id_usuario, monto_inicial) VALUES
(1, 100.00);

INSERT INTO PROMOCION (nombre_promocion, descripcion, tipo_descuento, valor_descuento, fecha_inicio, fecha_fin, estado) VALUES
('Combo Clasico', 'Descuento en hamburguesa clasica', 'Porcentaje', 10.00, '2026-01-01', '2026-12-31', 1);

-- Proveedores adicionales
INSERT INTO PROVEEDOR (nombre_proveedor, telefono, correo, direccion) VALUES
('Carnes y Embutidos del Norte', '2333-2222', 'pedidos@carnesnorte.com', 'Zona 12, Guatemala'),
('Lacteos La Vaquita', '2444-3333', 'ventas@lavaquita.com', 'Zona 7, Guatemala'),
('Panaderia San Miguel', '2555-4444', 'contacto@sanmiguel.com', 'Zona 1, Guatemala'),
('Verduras y Frutas Frescas', '2666-5555', 'frescas@verduras.com', 'Zona 3, Guatemala');

-- Horarios y categorias adicionales
INSERT INTO TIEMPO_COMIDA (nombre_horario, hora_inicio, hora_fin) VALUES
('Desayuno',        '06:00:00', '10:59:59'),
('Almuerzo y cena', '11:00:00', '23:59:59');

INSERT INTO CATEGORIA (nombre_categoria, descripcion) VALUES
('Desayunos',         'Platillos para empezar el dia'),
('Pollo',             'Pollo empanizado, buffalo y nuggets'),
('Postres',           'Dulce final'),
('Antojos',           'Papas y snacks'),
('Cajita Feliz',      'Hamburguesa o nuggets para los pequenos');

-- ------------------------------------------------------------
-- INGREDIENTES
-- ------------------------------------------------------------
INSERT INTO INGREDIENTE (nombre_ingrediente, unidad_medida, stock_actual, stock_minimo, id_proveedor) VALUES
('Carne de res', 'g', 30000, 5000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Filete de pollo', 'g', 25000, 4000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Nuggets de pollo', 'unidad', 1500, 300, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Alitas de pollo', 'unidad', 800, 150, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Pollo deshebrado', 'g', 8000, 1500, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Tocino', 'g', 10000, 2000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Jamon', 'g', 6000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Jamon de pavo', 'g', 5000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Salchicha', 'g', 6000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Salmon ahumado', 'g', 3000, 600, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Carnes y Embutidos del Norte')),
('Queso cheddar', 'g', 10000, 2000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Queso americano', 'g', 8000, 1500, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Queso suizo', 'g', 4000, 800, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Queso pepper jack', 'g', 4000, 800, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Queso mozzarella', 'g', 8000, 1500, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Queso crema', 'g', 5000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Queso azul', 'g', 2000, 400, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Mantequilla', 'g', 4000, 800, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Leche', 'ml', 30000, 5000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Crema', 'ml', 6000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Crema batida', 'ml', 5000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Yogurt natural', 'g', 8000, 1500, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Helado de vainilla', 'g', 20000, 4000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Helado de chocolate', 'g', 10000, 2000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Lacteos La Vaquita')),
('Pan de hamburguesa', 'unidad', 600, 100, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Pan muffin', 'unidad', 200, 40, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Croissant', 'unidad', 150, 30, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Bagel', 'unidad', 150, 30, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Tortilla de harina', 'unidad', 500, 100, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Pan artesanal', 'unidad', 400, 80, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Pan tostado', 'unidad', 400, 80, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Cono de galleta', 'unidad', 500, 100, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Dona sin glasear', 'unidad', 150, 30, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Masa de pan dulce', 'g', 6000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Masa para pay', 'g', 5000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Masa de galleta', 'g', 6000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Panaderia San Miguel')),
('Lechuga', 'g', 8000, 1500, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Tomate', 'g', 8000, 1500, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Cebolla', 'g', 10000, 2000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Aguacate', 'g', 6000, 1200, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Jalapeno', 'g', 2000, 400, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Champinones', 'g', 4000, 800, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Espinaca', 'g', 2000, 400, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Papa', 'g', 60000, 10000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Pico de gallo', 'g', 5000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Fresa', 'g', 6000, 1200, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Platano', 'g', 5000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Manzana', 'g', 5000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Frutos rojos', 'g', 3000, 600, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Frutas de temporada', 'g', 8000, 1500, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Naranja', 'unidad', 600, 100, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Limon', 'unidad', 600, 100, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Hierbabuena', 'g', 500, 100, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Eneldo', 'g', 300, 60, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Verduras y Frutas Frescas')),
('Huevo', 'unidad', 1500, 300, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Totopos', 'g', 6000, 1200, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Salsa roja', 'ml', 6000, 1200, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Salsa especial', 'ml', 5000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Salsa BBQ', 'ml', 6000, 1200, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Salsa buffalo', 'ml', 3000, 600, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Salsa ranch', 'ml', 3000, 600, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Salsa habanero', 'ml', 2500, 500, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Mayo chipotle', 'ml', 3000, 600, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Aderezo de ajo', 'ml', 3000, 600, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Salsa de fresa', 'ml', 2000, 400, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Jarabe de chocolate', 'ml', 3000, 600, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Caramelo', 'ml', 2000, 400, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Miel de maple', 'ml', 3000, 600, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Harina empanizadora', 'g', 10000, 2000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Mezcla para hotcakes y waffles', 'g', 10000, 2000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Mezcla para brownie', 'g', 4000, 800, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Aceite vegetal', 'ml', 20000, 4000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Azucar', 'g', 10000, 2000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Canela', 'g', 500, 100, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Especias sazonadoras', 'g', 1000, 200, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Chocolate en polvo', 'g', 5000, 1000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Cafe molido', 'g', 4000, 800, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Te negro', 'g', 800, 150, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Granola', 'g', 3000, 600, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Nueces', 'g', 2000, 400, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Galleta de chocolate', 'g', 4000, 800, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Chispas de colores', 'g', 800, 150, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Refresco de cola', 'ml', 40000, 8000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Agua mineral', 'ml', 30000, 6000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Agua purificada', 'ml', 60000, 10000, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central')),
('Cajita (empaque)', 'unidad', 400, 80, (SELECT id_proveedor FROM PROVEEDOR WHERE nombre_proveedor = 'Distribuidora Central'));

-- ------------------------------------------------------------
-- PRODUCTOS: Desayunos
-- ------------------------------------------------------------
INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) VALUES
('Huevos Jurasicos', 'Dos huevos revueltos estilo rancho con jamon y queso derretido.', 32.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('McMuffin T-Rex', 'Pan muffin tostado con salchicha, huevo y queso cheddar.', 28.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Croissant Pterodactilo', 'Croissant relleno de jamon, huevo y queso suizo.', 34.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Hotcakes Triceratops', 'Tres hotcakes esponjosos con miel y mantequilla.', 30.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Burrito Raptor', 'Burrito de huevo, tocino, papas y pico de gallo.', 33.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Bagel Estegosaurio', 'Bagel tostado con queso crema, salmon y eneldo.', 36.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Wrap Velociraptor', 'Tortilla rellena de huevo, aguacate y tocino.', 31.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Sandwich Braquiosaurio', 'Pan artesanal con huevo, jamon de pavo y queso.', 35.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Waffle Jurasico', 'Waffle crocante con fresas y crema batida.', 32.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Chilaquiles Cretacicos', 'Chilaquiles rojos con pollo deshebrado y crema.', 38.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Omelette Diplodocus', 'Omelette de tres quesos con champinones y espinaca.', 34.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Papas Rex con Huevo', 'Papas doradas con huevo estrellado y salsa.', 29.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Panque Fosil de Platano', 'Rebanada de panque casero con miel de maple.', 22.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Yogurt Prehistorico', 'Yogurt natural con granola y frutos rojos.', 24.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1),
('Combo Dino Huevo y Tocino', 'Dos huevos al gusto con tocino crujiente y pan tostado.', 36.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Desayunos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Desayuno'), 1);

-- ------------------------------------------------------------
-- PRODUCTOS: Hamburguesas
-- ------------------------------------------------------------
INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) VALUES
('T-Rex Doble', 'Dos carnes de res, doble queso cheddar, tocino y salsa especial.', 55.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Raptor Clasica', 'Carne de res, lechuga, tomate, cebolla y queso americano.', 38.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Triceratops BBQ', 'Carne de res con salsa BBQ, aros de cebolla y queso ahumado.', 48.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Diplodocus Gigante', 'Carne triple con queso, tocino y salsa secreta Chiksx.', 62.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Velociraptor Picante', 'Carne de res con jalapenos, queso pepper jack y mayo chipotle.', 46.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Estegosaurio Clasica', 'Carne de res con queso suizo y champinones salteados.', 44.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Compsognathus Junior', 'Hamburguesa sencilla ideal para los mas pequenos.', 28.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Ankylosaurus Fuego', 'Carne de res con salsa picante habanero y queso pepper jack.', 47.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Spinosaurus Especial', 'Carne de res, doble tocino, huevo frito y queso cheddar.', 58.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Megalosaurio Suprema', 'Carne de res, queso doble, tocino y aderezo Chiksx.', 56.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Carnotaurus Extreme', 'Carne de res picante con jalapenos y queso pepper jack.', 49.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Therizinosaurus Verde', 'Carne de res con guacamole, lechuga y pico de gallo.', 41.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Tiranosaurio Familiar', 'Combo doble de hamburguesas T-Rex para compartir.', 95.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Hamburguesas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1);

-- ------------------------------------------------------------
-- PRODUCTOS: Pollo
-- ------------------------------------------------------------
INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) VALUES
('Pterodactilo Crispy', 'Filete de pollo empanizado con lechuga y mayo de ajo.', 42.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Pollo'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Alosaurio Buffalo', 'Filete de pollo banado en salsa buffalo con queso azul.', 45.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Pollo'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Braquiosaurio Doble Pollo', 'Doble filete de pollo con tocino y queso cheddar.', 50.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Pollo'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Iguanodon Clasica de Pollo', 'Filete de pollo a la parrilla con vegetales frescos.', 40.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Pollo'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Parasaurolophus BBQ Pollo', 'Filete de pollo con salsa BBQ y aros de cebolla.', 44.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Pollo'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Utahraptor Ranch', 'Filete de pollo empanizado con salsa ranch y tocino.', 43.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Pollo'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1),
('Dino Nuggets Combo', 'Nuggets de pollo crujientes con papas y salsa a elegir.', 39.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Pollo'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Almuerzo y cena'), 1);

-- ------------------------------------------------------------
-- PRODUCTOS: Postres
-- ------------------------------------------------------------
INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) VALUES
('Cono Volcan de Chocolate', 'Cono suave banado en chocolate caliente.', 18.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Malteada Jurasica', 'Malteada cremosa de vainilla con topping de galleta.', 24.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Pie Fosil de Manzana', 'Pay de manzana caliente con canela.', 20.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Sundae Dino Rex', 'Helado de vainilla con jarabe de chocolate y nuez.', 19.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Brownie Cretacico', 'Brownie tibio con nuez y trozo de chocolate.', 22.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Cheesecake Triasico', 'Rebanada de cheesecake con salsa de fresa.', 28.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Cono Clasico de Vainilla', 'Cono suave sabor vainilla.', 12.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Cono Clasico de Chocolate', 'Cono suave sabor chocolate.', 12.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Torbellino Oreo Dino', 'Helado suave mezclado con trozos de galleta de chocolate.', 25.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Paleta Prehistorica', 'Paleta helada de fresa con chispas de colores.', 14.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Dona Jurasica', 'Dona glaseada con chispas de colores.', 16.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Galleta Gigante Rex', 'Galleta de chocolate recien horneada.', 15.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Rollo Fosil de Canela', 'Rollo de canela con glaseado dulce.', 18.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Copa Cretacica de Frutas', 'Mezcla de frutas frescas de temporada.', 20.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Flan Casero Dino', 'Flan napolitano con caramelo.', 19.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Postres'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1);

-- ------------------------------------------------------------
-- PRODUCTOS: Bebidas
-- ------------------------------------------------------------
INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) VALUES
('Refresco Jurasico', 'Refresco de cola bien frio.', 14.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Limonada Raptor', 'Limonada natural con hierbabuena.', 16.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Te Helado Dino', 'Te negro helado con limon.', 15.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Cafe Fosil Americano', 'Cafe negro recien preparado.', 14.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Capuchino Cretacico', 'Cafe espresso con leche espumada.', 20.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Chocolate Caliente Rex', 'Chocolate caliente cremoso.', 18.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Agua Mineral Jurasica', 'Agua mineral con gas.', 10.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Jugo Natural de Naranja', 'Jugo de naranja recien exprimido.', 16.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Smoothie Dino de Fresa', 'Smoothie cremoso de fresa natural.', 22.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Malteada Triasica de Oreo', 'Malteada cremosa con galleta de chocolate.', 24.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Bebidas'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1);

-- ------------------------------------------------------------
-- PRODUCTOS: Antojos
-- ------------------------------------------------------------
INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) VALUES
('Papas Fritas Rex', 'Papas a la francesa crujientes.', 20.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Papas Gajo Jurasicas', 'Papas gajo sazonadas con especias.', 24.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Aros de Cebolla Dino', 'Aros de cebolla empanizados y crujientes.', 22.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Nuggets de Pollo Raptor', 'Nuggets de pollo crujientes, 6 piezas.', 28.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Alitas BBQ Triceratops', 'Alitas banadas en salsa BBQ.', 38.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Alitas Picantes Velociraptor', 'Alitas banadas en salsa picante.', 38.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Quesadilla Fosil', 'Quesadilla de queso derretido con tortilla de harina.', 26.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Bastones de Queso Dino', 'Bastones de queso mozzarella empanizados.', 24.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Palomitas de Pollo Rex', 'Trocitos de pollo empanizados estilo palomitas.', 27.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Totopos Cretacicos con Queso', 'Totopos banados en queso derretido.', 25.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Antojos'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1);

-- ------------------------------------------------------------
-- PRODUCTOS: Cajita Feliz
-- ------------------------------------------------------------
INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) VALUES
('Cajita Mini Rex', 'Hamburguesa sencilla, papas chicas y bebida a elegir.', 32.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Cajita Feliz'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Cajita Diplodocus', 'Hamburguesa con queso, papas chicas y bebida a elegir.', 38.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Cajita Feliz'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Cajita Doble Rex', 'Dos hamburguesas sencillas, papas chicas y bebida a elegir.', 45.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Cajita Feliz'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Cajita Raptor 4', '4 piezas de nuggets, papas chicas y bebida a elegir.', 30.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Cajita Feliz'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Cajita Raptor 6', '6 piezas de nuggets, papas chicas y bebida a elegir.', 36.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Cajita Feliz'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1),
('Cajita Fiesta Dino', 'Nuggets y papas grandes para compartir, con dos bebidas.', 58.00,
  (SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = 'Cajita Feliz'),
  (SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = 'Todo el dia'), 1);

-- ------------------------------------------------------------
-- RECETAS (PRODUCTO_INGREDIENTE)
-- ------------------------------------------------------------
-- Hamburguesa Clasica
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hamburguesa Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hamburguesa Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso americano'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hamburguesa Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Lechuga'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hamburguesa Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Hamburguesa BBQ
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hamburguesa BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hamburguesa BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hamburguesa BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa BBQ'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hamburguesa BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Coca Cola
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Coca Cola'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Refresco de cola'), 355);

-- ===== Desayunos =====
-- Huevos Jurasicos
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Huevos Jurasicos'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Huevos Jurasicos'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Jamon'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Huevos Jurasicos'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Huevos Jurasicos'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mantequilla'), 10);

-- McMuffin T-Rex
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'McMuffin T-Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan muffin'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'McMuffin T-Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salchicha'), 60),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'McMuffin T-Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'McMuffin T-Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso americano'), 20);

-- Croissant Pterodactilo
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Croissant Pterodactilo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Croissant'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Croissant Pterodactilo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Jamon'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Croissant Pterodactilo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Croissant Pterodactilo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso suizo'), 25);

-- Hotcakes Triceratops
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hotcakes Triceratops'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mezcla para hotcakes y waffles'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hotcakes Triceratops'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Leche'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hotcakes Triceratops'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Miel de maple'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Hotcakes Triceratops'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mantequilla'), 15);

-- Burrito Raptor
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Burrito Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tortilla de harina'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Burrito Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Burrito Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Burrito Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 60),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Burrito Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pico de gallo'), 40);

-- Bagel Estegosaurio
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Bagel Estegosaurio'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Bagel'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Bagel Estegosaurio'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso crema'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Bagel Estegosaurio'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salmon ahumado'), 50),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Bagel Estegosaurio'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Eneldo'), 2);

-- Wrap Velociraptor
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Wrap Velociraptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tortilla de harina'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Wrap Velociraptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Wrap Velociraptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aguacate'), 50),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Wrap Velociraptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 40);

-- Sandwich Braquiosaurio
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Sandwich Braquiosaurio'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan artesanal'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Sandwich Braquiosaurio'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Sandwich Braquiosaurio'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Jamon de pavo'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Sandwich Braquiosaurio'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso americano'), 20);

-- Waffle Jurasico
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Waffle Jurasico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mezcla para hotcakes y waffles'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Waffle Jurasico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Leche'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Waffle Jurasico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Fresa'), 60),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Waffle Jurasico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Crema batida'), 40);

-- Chilaquiles Cretacicos
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Chilaquiles Cretacicos'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Totopos'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Chilaquiles Cretacicos'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa roja'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Chilaquiles Cretacicos'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pollo deshebrado'), 80),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Chilaquiles Cretacicos'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Crema'), 30);

-- Omelette Diplodocus
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Omelette Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 3),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Omelette Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Omelette Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso mozzarella'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Omelette Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Champinones'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Omelette Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Espinaca'), 30);

-- Papas Rex con Huevo
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Papas Rex con Huevo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 180),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Papas Rex con Huevo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Papas Rex con Huevo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa roja'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Papas Rex con Huevo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 20);

-- Panque Fosil de Platano
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Panque Fosil de Platano'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mezcla para hotcakes y waffles'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Panque Fosil de Platano'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Platano'), 80),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Panque Fosil de Platano'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Miel de maple'), 20);

-- Yogurt Prehistorico
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Yogurt Prehistorico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Yogurt natural'), 200),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Yogurt Prehistorico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Granola'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Yogurt Prehistorico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Frutos rojos'), 50);

-- Combo Dino Huevo y Tocino
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Combo Dino Huevo y Tocino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Combo Dino Huevo y Tocino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 50),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Combo Dino Huevo y Tocino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan tostado'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Combo Dino Huevo y Tocino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mantequilla'), 10);

-- ===== Hamburguesas =====
-- T-Rex Doble
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'T-Rex Doble'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 240),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'T-Rex Doble'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'T-Rex Doble'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'T-Rex Doble'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa especial'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'T-Rex Doble'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Raptor Clasica
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Raptor Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Raptor Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Lechuga'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Raptor Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tomate'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Raptor Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cebolla'), 15),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Raptor Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso americano'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Raptor Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Triceratops BBQ
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Triceratops BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Triceratops BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa BBQ'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Triceratops BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cebolla'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Triceratops BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Harina empanizadora'), 15),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Triceratops BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Triceratops BBQ'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Diplodocus Gigante
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Diplodocus Gigante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 360),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Diplodocus Gigante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 60),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Diplodocus Gigante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Diplodocus Gigante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa especial'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Diplodocus Gigante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Velociraptor Picante
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Velociraptor Picante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Velociraptor Picante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Jalapeno'), 15),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Velociraptor Picante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso pepper jack'), 25),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Velociraptor Picante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mayo chipotle'), 25),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Velociraptor Picante'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Estegosaurio Clasica
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Estegosaurio Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Estegosaurio Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso suizo'), 25),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Estegosaurio Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Champinones'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Estegosaurio Clasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Compsognathus Junior
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Compsognathus Junior'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 80),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Compsognathus Junior'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Lechuga'), 10),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Compsognathus Junior'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Ankylosaurus Fuego
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Ankylosaurus Fuego'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Ankylosaurus Fuego'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa habanero'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Ankylosaurus Fuego'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso pepper jack'), 25),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Ankylosaurus Fuego'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Spinosaurus Especial
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Spinosaurus Especial'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Spinosaurus Especial'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 60),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Spinosaurus Especial'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Spinosaurus Especial'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 25),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Spinosaurus Especial'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Megalosaurio Suprema
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Megalosaurio Suprema'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 180),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Megalosaurio Suprema'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 50),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Megalosaurio Suprema'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Megalosaurio Suprema'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa especial'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Megalosaurio Suprema'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Carnotaurus Extreme
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Carnotaurus Extreme'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Carnotaurus Extreme'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Jalapeno'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Carnotaurus Extreme'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso pepper jack'), 25),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Carnotaurus Extreme'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Therizinosaurus Verde
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Therizinosaurus Verde'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Therizinosaurus Verde'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aguacate'), 50),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Therizinosaurus Verde'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Lechuga'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Therizinosaurus Verde'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pico de gallo'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Therizinosaurus Verde'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Tiranosaurio Familiar
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Tiranosaurio Familiar'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 480),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Tiranosaurio Familiar'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 80),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Tiranosaurio Familiar'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Lechuga'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Tiranosaurio Familiar'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tomate'), 60),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Tiranosaurio Familiar'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Tiranosaurio Familiar'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 300);

-- ===== Pollo =====
-- Pterodactilo Crispy
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pterodactilo Crispy'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Filete de pollo'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pterodactilo Crispy'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Harina empanizadora'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pterodactilo Crispy'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Lechuga'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pterodactilo Crispy'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aderezo de ajo'), 25),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pterodactilo Crispy'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pterodactilo Crispy'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Alosaurio Buffalo
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alosaurio Buffalo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Filete de pollo'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alosaurio Buffalo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa buffalo'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alosaurio Buffalo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso azul'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alosaurio Buffalo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Harina empanizadora'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alosaurio Buffalo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Braquiosaurio Doble Pollo
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Braquiosaurio Doble Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Filete de pollo'), 240),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Braquiosaurio Doble Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Braquiosaurio Doble Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Braquiosaurio Doble Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Harina empanizadora'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Braquiosaurio Doble Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Iguanodon Clasica de Pollo
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Iguanodon Clasica de Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Filete de pollo'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Iguanodon Clasica de Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Lechuga'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Iguanodon Clasica de Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tomate'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Iguanodon Clasica de Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cebolla'), 15),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Iguanodon Clasica de Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Parasaurolophus BBQ Pollo
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Parasaurolophus BBQ Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Filete de pollo'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Parasaurolophus BBQ Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa BBQ'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Parasaurolophus BBQ Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cebolla'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Parasaurolophus BBQ Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Harina empanizadora'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Parasaurolophus BBQ Pollo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Utahraptor Ranch
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Utahraptor Ranch'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Filete de pollo'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Utahraptor Ranch'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Harina empanizadora'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Utahraptor Ranch'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa ranch'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Utahraptor Ranch'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tocino'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Utahraptor Ranch'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1);

-- Dino Nuggets Combo
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Dino Nuggets Combo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Nuggets de pollo'), 10),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Dino Nuggets Combo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Dino Nuggets Combo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Dino Nuggets Combo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa BBQ'), 30);

-- ===== Postres =====
-- Cono Volcan de Chocolate
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cono Volcan de Chocolate'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cono de galleta'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cono Volcan de Chocolate'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Helado de vainilla'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cono Volcan de Chocolate'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Jarabe de chocolate'), 30);

-- Malteada Jurasica
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Malteada Jurasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Helado de vainilla'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Malteada Jurasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Leche'), 200),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Malteada Jurasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Galleta de chocolate'), 20);

-- Pie Fosil de Manzana
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pie Fosil de Manzana'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Masa para pay'), 80),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pie Fosil de Manzana'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Manzana'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pie Fosil de Manzana'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Canela'), 3),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Pie Fosil de Manzana'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Azucar'), 20);

-- Sundae Dino Rex
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Sundae Dino Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Helado de vainilla'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Sundae Dino Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Jarabe de chocolate'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Sundae Dino Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Nueces'), 15);

-- Brownie Cretacico
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Brownie Cretacico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mezcla para brownie'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Brownie Cretacico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Nueces'), 15),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Brownie Cretacico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mantequilla'), 20);

-- Cheesecake Triasico
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cheesecake Triasico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso crema'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cheesecake Triasico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Galleta de chocolate'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cheesecake Triasico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa de fresa'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cheesecake Triasico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Azucar'), 20);

-- Cono Clasico de Vainilla
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cono Clasico de Vainilla'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cono de galleta'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cono Clasico de Vainilla'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Helado de vainilla'), 100);

-- Cono Clasico de Chocolate
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cono Clasico de Chocolate'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cono de galleta'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cono Clasico de Chocolate'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Helado de chocolate'), 100);

-- Torbellino Oreo Dino
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Torbellino Oreo Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Helado de vainilla'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Torbellino Oreo Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Galleta de chocolate'), 40);

-- Paleta Prehistorica
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Paleta Prehistorica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Fresa'), 60),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Paleta Prehistorica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Azucar'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Paleta Prehistorica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Chispas de colores'), 5);

-- Dona Jurasica
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Dona Jurasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Dona sin glasear'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Dona Jurasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Azucar'), 20),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Dona Jurasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Chispas de colores'), 5);

-- Galleta Gigante Rex
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Galleta Gigante Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Masa de galleta'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Galleta Gigante Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Chocolate en polvo'), 20);

-- Rollo Fosil de Canela
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Rollo Fosil de Canela'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Masa de pan dulce'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Rollo Fosil de Canela'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Canela'), 5),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Rollo Fosil de Canela'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Azucar'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Rollo Fosil de Canela'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Mantequilla'), 15);

-- Copa Cretacica de Frutas
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Copa Cretacica de Frutas'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Frutas de temporada'), 200);

-- Flan Casero Dino
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Flan Casero Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Leche'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Flan Casero Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Huevo'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Flan Casero Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Azucar'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Flan Casero Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Caramelo'), 30);

-- ===== Bebidas =====
-- Refresco Jurasico
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Refresco Jurasico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Refresco de cola'), 355);

-- Limonada Raptor
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Limonada Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Limon'), 3),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Limonada Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Azucar'), 25),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Limonada Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Hierbabuena'), 3),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Limonada Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Agua purificada'), 350);

-- Te Helado Dino
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Te Helado Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Te negro'), 5),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Te Helado Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Limon'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Te Helado Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Azucar'), 15),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Te Helado Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Agua purificada'), 350);

-- Cafe Fosil Americano
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cafe Fosil Americano'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cafe molido'), 15),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cafe Fosil Americano'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Agua purificada'), 240);

-- Capuchino Cretacico
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Capuchino Cretacico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cafe molido'), 18),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Capuchino Cretacico'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Leche'), 150);

-- Chocolate Caliente Rex
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Chocolate Caliente Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Chocolate en polvo'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Chocolate Caliente Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Leche'), 250),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Chocolate Caliente Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Azucar'), 10);

-- Agua Mineral Jurasica
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Agua Mineral Jurasica'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Agua mineral'), 355);

-- Jugo Natural de Naranja
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Jugo Natural de Naranja'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Naranja'), 4);

-- Smoothie Dino de Fresa
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Smoothie Dino de Fresa'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Fresa'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Smoothie Dino de Fresa'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Yogurt natural'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Smoothie Dino de Fresa'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Leche'), 100);

-- Malteada Triasica de Oreo
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Malteada Triasica de Oreo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Helado de vainilla'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Malteada Triasica de Oreo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Leche'), 200),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Malteada Triasica de Oreo'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Galleta de chocolate'), 40);

-- ===== Antojos =====
-- Papas Fritas Rex
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Papas Fritas Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 200),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Papas Fritas Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 30);

-- Papas Gajo Jurasicas
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Papas Gajo Jurasicas'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 250),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Papas Gajo Jurasicas'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Papas Gajo Jurasicas'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Especias sazonadoras'), 5);

-- Aros de Cebolla Dino
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Aros de Cebolla Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cebolla'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Aros de Cebolla Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Harina empanizadora'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Aros de Cebolla Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 40);

-- Nuggets de Pollo Raptor
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Nuggets de Pollo Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Nuggets de pollo'), 6),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Nuggets de Pollo Raptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 20);

-- Alitas BBQ Triceratops
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alitas BBQ Triceratops'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Alitas de pollo'), 8),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alitas BBQ Triceratops'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa BBQ'), 50),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alitas BBQ Triceratops'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 40);

-- Alitas Picantes Velociraptor
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alitas Picantes Velociraptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Alitas de pollo'), 8),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alitas Picantes Velociraptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa habanero'), 40),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Alitas Picantes Velociraptor'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 40);

-- Quesadilla Fosil
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Quesadilla Fosil'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Tortilla de harina'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Quesadilla Fosil'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso mozzarella'), 80);

-- Bastones de Queso Dino
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Bastones de Queso Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso mozzarella'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Bastones de Queso Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Harina empanizadora'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Bastones de Queso Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 40);

-- Palomitas de Pollo Rex
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Palomitas de Pollo Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Filete de pollo'), 150),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Palomitas de Pollo Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Harina empanizadora'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Palomitas de Pollo Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Aceite vegetal'), 40);

-- Totopos Cretacicos con Queso
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Totopos Cretacicos con Queso'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Totopos'), 120),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Totopos Cretacicos con Queso'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso cheddar'), 60);

-- ===== Cajita Feliz =====
-- Cajita Mini Rex
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Mini Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 80),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Mini Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Mini Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Mini Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Refresco de cola'), 250),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Mini Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cajita (empaque)'), 1);

-- Cajita Diplodocus
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 80),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Queso americano'), 15),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 1),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Refresco de cola'), 250),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Diplodocus'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cajita (empaque)'), 1);

-- Cajita Doble Rex
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Doble Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Carne de res'), 160),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Doble Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Pan de hamburguesa'), 2),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Doble Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Doble Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Refresco de cola'), 250),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Doble Rex'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cajita (empaque)'), 1);

-- Cajita Raptor 4
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Raptor 4'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Nuggets de pollo'), 4),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Raptor 4'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Raptor 4'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Refresco de cola'), 250),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Raptor 4'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cajita (empaque)'), 1);

-- Cajita Raptor 6
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Raptor 6'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Nuggets de pollo'), 6),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Raptor 6'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 100),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Raptor 6'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Refresco de cola'), 250),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Raptor 6'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cajita (empaque)'), 1);

-- Cajita Fiesta Dino
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_requerida) VALUES
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Fiesta Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Nuggets de pollo'), 10),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Fiesta Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Papa'), 250),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Fiesta Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Refresco de cola'), 500),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Fiesta Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Salsa BBQ'), 30),
((SELECT id_producto FROM PRODUCTO WHERE nombre_producto = 'Cajita Fiesta Dino'),
  (SELECT id_ingrediente FROM INGREDIENTE WHERE nombre_ingrediente = 'Cajita (empaque)'), 1);