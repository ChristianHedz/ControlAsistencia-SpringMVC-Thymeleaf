INSERT INTO rol (nombre) VALUES ('Operativo'), ('Administrativo');
insert into departamento (nombre, jefe_responsable) values ('Sistemas', 'Juan Perez');
INSERT INTO departamento (nombre, jefe_responsable) VALUES ('Contabilidad', 'Pedro Rodriguez');
INSERT INTO departamento (nombre, jefe_responsable) VALUES ('Marketing', 'Maria Lopez');

INSERT INTO empleado (nombre, apellido, estatus, hora_entrada, hora_salida, usuario, contrasena, departamento_id, rol_id) VALUES ('Juan', 'Perez', 'ACTIVO', '2024-07-11 15:30:00', '2024-07-11 15:30:00', 'juanp', '123', 1, 1);

