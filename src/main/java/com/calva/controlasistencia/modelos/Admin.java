package com.calva.controlasistencia.modelos;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
}
