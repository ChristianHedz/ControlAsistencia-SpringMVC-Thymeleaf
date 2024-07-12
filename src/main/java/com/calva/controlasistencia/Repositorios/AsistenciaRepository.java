package com.calva.controlasistencia.Repositorios;

import com.calva.controlasistencia.modelos.Asistencia;
import com.calva.controlasistencia.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    Optional<Asistencia> findTopByEmpleadoOrderByFechaDesc(Empleado empleado);
}
