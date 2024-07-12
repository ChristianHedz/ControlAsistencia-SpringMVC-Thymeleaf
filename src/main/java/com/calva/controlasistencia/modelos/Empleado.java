package com.calva.controlasistencia.modelos;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String estatus;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private String usuario;
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    // Método para verificar si el empleado es administrador o tiene determinado de contador
    public boolean esAdministrador() {
        return this.rol != null && this.rol.getNombre().equals("Administrativo");
    }

    public boolean esContador() {
        return this.rol != null && this.rol.getNombre().equals("Contador");
    }



}
