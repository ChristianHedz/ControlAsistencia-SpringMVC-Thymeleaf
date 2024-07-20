INSERT INTO rol (nombre) VALUES ('Operativo'), ('Administrativo');
INSERT INTO departamento (nombre, jefe_responsable) VALUES ('Contabilidad', 'Pedro Rodriguez');
insert into departamento (nombre, jefe_responsable) values ('Sistemas', 'Juan Perez');
INSERT INTO departamento (nombre, jefe_responsable) VALUES ('Marketing', 'Maria Lopez');

INSERT INTO empleado (nombre, apellido, estatus, hora_entrada, hora_salida, retardos, faltas, sueldo,usuario, contrasena, departamento_id, rol_id) VALUES ('Lau', 'Perez Sanchez', 'ACTIVO', '2024-07-12 23:09:00', '2024-07-12 23:55:00', 0,0,12000,'Laura Martine', '1234', 1, 1);
INSERT INTO empleado (nombre, apellido, estatus, hora_entrada, hora_salida, retardos, faltas, sueldo,usuario, contrasena, departamento_id, rol_id) VALUES ('Laura', 'Mendez Hernandez', 'ACTIVO', '2024-07-12 23:09:00', '2024-07-12 23:55:00', 0,0,12000,'Laura', '111', 1, 2);

