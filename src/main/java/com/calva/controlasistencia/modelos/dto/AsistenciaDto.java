package com.calva.controlasistencia.modelos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AsistenciaDto {
    @NotBlank(message = "El id del empleado es requerido")
    @Size(min = 1, max = 3, message = "El id del empleado debe tener entre 1 y 3 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "El id del empleado debe ser numérico")
    private String idEmpleado;
}
