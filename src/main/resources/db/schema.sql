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

-- Datos iniciales de prueba (idempotentes)
MERGE INTO evento (id, nombre, fecha, lugar, aforo, bloqueados)
KEY (id)
VALUES
    ('EVT-001', 'CampusFest Unillanos 2026', '2026-12-05 14:00:00', 'Auditorio Eduardo Carranza', 100, 10),
    ('EVT-002', 'Hackathon Ingeniería de Sistemas', '2026-12-12 08:00:00', 'Bloque Ciencias Básicas - Sala 3', 50, 5),
    ('EVT-003', 'Concierto Universitario', '2026-12-18 18:00:00', 'Plazoleta Central Unillanos', 300, 0);

MERGE INTO reserva (codigo, evento_id, estudiante_id, vence_en, estado)
KEY (codigo)
VALUES
    ('RES-001', 'EVT-001', '160004501', '2026-12-01 10:15:00', 'CONFIRMADA'),
    ('RES-002', 'EVT-001', '160004502', '2026-12-01 10:30:00', 'CONFIRMADA'),
    ('RES-003', 'EVT-001', '160004503', '2026-12-05 13:55:00', 'PENDIENTE'),
    ('RES-004', 'EVT-002', '160004504', '2026-12-12 07:55:00', 'CONFIRMADA'),
    ('RES-005', 'EVT-002', '160004505', '2026-12-02 09:00:00', 'CANCELADA');

MERGE INTO asistencia (codigo_reserva, hora)
KEY (codigo_reserva)
VALUES
    ('RES-001', '2026-12-05 13:45:00');