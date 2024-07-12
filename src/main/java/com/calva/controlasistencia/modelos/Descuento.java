package com.calva.controlasistencia.modelos;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Descuento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    public enum Tipo {
        RETARDO,
        FALTA
    }

    private Double monto;

}
