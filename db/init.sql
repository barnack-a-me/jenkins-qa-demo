CREATE TABLE IF NOT EXISTS productos (
    id     SERIAL PRIMARY KEY,
    nombre VARCHAR(100)   NOT NULL,
    precio NUMERIC(10, 2) NOT NULL CHECK (precio >= 0),
    stock  INT            NOT NULL CHECK (stock >= 0)
);

INSERT INTO productos (nombre, precio, stock) VALUES
    ('Teclado mecanico', 200.00, 10),
    ('Mouse inalambrico', 50.00, 25),
    ('Monitor 24 pulgadas', 1200.00, 4),
    ('Cable HDMI', 10.50, 100);
