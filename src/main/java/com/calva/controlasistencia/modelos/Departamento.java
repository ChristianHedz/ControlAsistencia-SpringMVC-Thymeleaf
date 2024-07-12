package com.calva.controlasistencia.modelos;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String jefeResponsable;

    @OneToMany(mappedBy = "departamento")
    private List<Empleado> empleados;

}
