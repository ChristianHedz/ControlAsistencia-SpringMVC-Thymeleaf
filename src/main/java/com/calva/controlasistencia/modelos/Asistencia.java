package com.calva.controlasistencia.modelos;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private Long asistencia = 1L;

    @Enumerated(EnumType.STRING)
    private TipoRegistro tipoRegistro;

    public enum TipoRegistro {
        NORMAL,
        RETARDO,
        FALTA
    }

}
