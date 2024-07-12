package com.calva.controlasistencia.modelos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpleadoDto {
    @NotBlank(message = "{empleado.nombre.notBlank}")
    private String nombre;
    @NotBlank(message = "{empleado.apellido.notBlank}")
    private String apellido;
    @NotBlank(message = "{empleado.estatus.notBlank}")
    private String estatus;
    @NotBlank(message = "{empleado.departamento.notBlank}")
    private String departamento;
    @NotBlank(message = "{empleado.rol.notBlank}")
    private String rol;
    private String horaEntrada;
    private String horaSalida;
    @NotBlank(message = "{empleado.usuario.notBlank}")
    @Size(min = 3, message = "{empleado.usuario.size}")
    private String usuario;
    @NotBlank(message = "{empleado.contrasena.notBlank}")
    @Size(min = 3, message = "{empleado.contrasena.size}")
    private String contrasena;

}
