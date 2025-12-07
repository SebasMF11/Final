INSERT INTO doctores (nombre, apellido, contraseña, correo, especialidad, hora_inicio, hora_fin, estado)
VALUES 
('Carlos', 'Torres', '12345', 'doc1@salud.com', 'Dermatología', '08:00', '16:00', 'ACTIVO'),
('Laura', 'Méndez', '12345', 'doc2@salud.com', 'Pediatría', '10:00', '18:00', 'ACTIVO');

INSERT INTO pacientes (nombre, apellido, contraseña, correo)
VALUES
('Juan', 'Gómez', '123', 'juan@mail.com'),
('María', 'Díaz', '123', 'maria@mail.com');

INSERT INTO admins (correo, contraseña)
VALUES 
('admin@admin.com', 'admin');

INSERT INTO usuarios (correo, contraseña, rol)
VALUES
('admin@admin.com', 'admin', 'ADMIN'),
('doc1@salud.com', '12345', 'DOCTOR'),
('doc2@salud.com', '12345', 'DOCTOR'),
('juan@mail.com', '123', 'PACIENTE'),
('maria@mail.com', '123', 'PACIENTE');
