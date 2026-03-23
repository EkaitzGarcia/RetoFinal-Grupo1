DROP DATABASE IF EXISTS OKAPI;

CREATE DATABASE OKAPI;

USE OKAPI;

CREATE TABLE TRABAJADOR (
    NSS CHAR(12) PRIMARY KEY,
    NOMBRE_T VARCHAR(50),
    APELLIDO_T VARCHAR(50),
    TELEFONO_T CHAR(9),
    CORREO_T VARCHAR(100)
);

CREATE TABLE CLIENTE (
    DNI CHAR(9) PRIMARY KEY,
    NOMBRE_C VARCHAR(50),
    APELLIDO_C VARCHAR(50),
    TELEFONO_C CHAR(15),
    CORREO_C VARCHAR(100),
    DIRECCION VARCHAR(100)
);

CREATE TABLE PRODUCTO (
    REF VARCHAR(10) PRIMARY KEY,
    PRECIO DECIMAL(10, 2) CHECK (PRECIO > 0),
    DESCUENTO INT DEFAULT 0 CHECK (
        DESCUENTO >= 0
        AND DESCUENTO < 100
    )
);

CREATE TABLE COMPRA (
    ID INT PRIMARY KEY,
    FECHA DATE,
    TOTAL DECIMAL(10, 2) CHECK (TOTAL >= 0),
    METODO_PAGO ENUM('EFECTIVO', 'TARJETA'),
    DNI CHAR(9),
    NSS CHAR(12),
    FOREIGN KEY (DNI) REFERENCES CLIENTE (DNI),
    FOREIGN KEY (NSS) REFERENCES TRABAJADOR (NSS)
);

CREATE TABLE ESTA (
    ID INT,
    REF VARCHAR(10),
    CANTIDAD INT CHECK (CANTIDAD > 0),
    PRIMARY KEY (ID, REF),
    FOREIGN KEY (ID) REFERENCES COMPRA (ID),
    FOREIGN KEY (REF) REFERENCES PRODUCTO (REF)
);

/* TRABAJADORES */

INSERT INTO
    TRABAJADOR
VALUES (
        '111111111111',
        'Carlos',
        'Lopez',
        '600111111',
        'carlos@okapi.com'
    ),
    (
        '222222222222',
        'Laura',
        'Perez',
        '600222222',
        'laura@okapi.com'
    ),
    (
        '333333333333',
        'Mario',
        'Sanchez',
        '600333333',
        'mario@okapi.com'
    ),
    (
        '444444444444',
        'Ana',
        'Garcia',
        '600444444',
        'ana@okapi.com'
    ),
    (
        '555555555555',
        'David',
        'Martinez',
        '600555555',
        'david@okapi.com'
    );

/* CLIENTES */

INSERT INTO
    CLIENTE
VALUES (
        '12345678A',
        'Juan',
        'Gomez',
        '611111111',
        'juan@gmail.com',
        'Madrid'
    ),
    (
        '23456789B',
        'Lucia',
        'Fernandez',
        '622222222',
        'lucia@gmail.com',
        'Barcelona'
    ),
    (
        '34567890C',
        'Pedro',
        'Lopez',
        '633333333',
        'pedro@gmail.com',
        'Valencia'
    ),
    (
        '45678901D',
        'Sara',
        'Ruiz',
        '644444444',
        'sara@gmail.com',
        'Sevilla'
    ),
    (
        '56789012E',
        'Miguel',
        'Torres',
        '655555555',
        'miguel@gmail.com',
        'Bilbao'
    );

/* PRODUCTOS */

INSERT INTO
    PRODUCTO
VALUES ('P001', 29.99, 10),
    ('P002', 49.99, 0),
    ('P003', 19.99, 5),
    ('P004', 59.99, 15),
    ('P005', 39.99, 20),
    ('P006', 24.99, 0),
    ('P007', 34.99, 10);

/* COMPRAS */

INSERT INTO
    COMPRA
VALUES (
        1,
        '2026-03-01',
        79.98,
        'TARJETA',
        '12345678A',
        '111111111111'
    ),
    (
        2,
        '2026-03-02',
        49.99,
        'EFECTIVO',
        '23456789B',
        '222222222222'
    ),
    (
        3,
        '2026-03-03',
        59.99,
        'TARJETA',
        '34567890C',
        '333333333333'
    ),
    (
        4,
        '2026-03-04',
        89.98,
        'EFECTIVO',
        '45678901D',
        '111111111111'
    ),
    (
        5,
        '2026-03-05',
        39.99,
        'TARJETA',
        '56789012E',
        '444444444444'
    );

/* RELACIÓN COMPRA - PRODUCTO (ESTA) */

INSERT INTO
    ESTA
VALUES (1, 'P001', 2),
    (1, 'P003', 1),
    (2, 'P002', 1),
    (3, 'P004', 1),
    (4, 'P005', 2),
    (5, 'P006', 1),
    (5, 'P007', 1);

-- Procedimiento (Ekaitz)