package com.calva.controlasistencia.Repositorios;

import com.calva.controlasistencia.modelos.Descuento;
import com.calva.controlasistencia.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DescuentoRepository extends JpaRepository<Descuento,Long> {

    List<Descuento> findByEmpleado(Empleado empleado);
}
