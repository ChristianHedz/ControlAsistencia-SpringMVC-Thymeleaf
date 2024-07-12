package com.calva.controlasistencia.Mappers;

import com.calva.controlasistencia.modelos.Empleado;
import com.calva.controlasistencia.modelos.dto.EmpleadoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper {

//    @Mapping(source = "course.id", target = "courseId")
//    LessonResponse lessonToLessonResponseDTO(Lesson lesson);

    @Mapping(target = "departamento", ignore = true)
    @Mapping(target = "horaEntrada", ignore = true)
    @Mapping(target = "horaSalida", ignore = true)
    @Mapping(target = "rol", ignore = true)
    Empleado empleadoDTOToEmpleado(EmpleadoDto empleadoDTO);
}
