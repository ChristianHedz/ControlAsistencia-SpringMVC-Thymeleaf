package com.calva.controlasistencia.Repositorios;

import com.calva.controlasistencia.modelos.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    //encontrar por nombre
    Optional<Departamento> findByNombre(String nombre);

}
