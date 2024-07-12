package com.calva.controlasistencia.modelos;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @OneToMany(mappedBy = "rol")
    private List<Empleado> empleados;

}
