package com.calva.controlasistencia.Repositorios;

import com.calva.controlasistencia.modelos.Asistencia;
import com.calva.controlasistencia.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    Optional<Asistencia> findTopByEmpleadoOrderByFechaDesc(Empleado empleado);

    List<Asistencia> findByEmpleado(Empleado empleado);
}
