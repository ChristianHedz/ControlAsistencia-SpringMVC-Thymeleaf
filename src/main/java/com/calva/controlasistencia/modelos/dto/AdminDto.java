package com.calva.controlasistencia.modelos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminDto {
    @NotBlank(message = "{admin.usuario.notBlank}")
    @Size(min = 3, message = "{admin.usuario.size}")
    private String usuario;
    @NotBlank(message = "{admin.contrasena.notBlank}")
    @Size(min = 3, message = "{admin.contrasena.size}")
    private String contrasena;

}
