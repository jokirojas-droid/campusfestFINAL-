CREATE TABLE IF NOT EXISTS evento (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    lugar VARCHAR(200) NOT NULL,
    aforo INT NOT NULL,
    bloqueados INT NOT NULL
);

CREATE TABLE IF NOT EXISTS reserva (
    codigo VARCHAR(50) PRIMARY KEY,
    evento_id VARCHAR(50) NOT NULL,
    estudiante_id VARCHAR(50) NOT NULL,
    vence_en TIMESTAMP NOT NULL,
    estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (evento_id) REFERENCES evento(id)
);

CREATE TABLE IF NOT EXISTS asistencia (
    codigo_reserva VARCHAR(50) PRIMARY KEY,
    hora TIMESTAMP NOT NULL,
    FOREIGN KEY (codigo_reserva) REFERENCES reserva(codigo)
);