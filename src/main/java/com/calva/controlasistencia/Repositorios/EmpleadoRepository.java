package com.calva.controlasistencia.Repositorios;

import com.calva.controlasistencia.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository  extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByUsuarioAndContrasena(String usuario, String contrasena);
}
