package com.calva.controlasistencia.controller;

import com.calva.controlasistencia.Mappers.EmpleadoMapper;
import com.calva.controlasistencia.Repositorios.AsistenciaRepository;
import com.calva.controlasistencia.Repositorios.DepartamentoRepository;
import com.calva.controlasistencia.Repositorios.EmpleadoRepository;
import com.calva.controlasistencia.Repositorios.RolRepository;
import com.calva.controlasistencia.excepciones.ResourceNotFoundException;
import com.calva.controlasistencia.modelos.Asistencia;
import com.calva.controlasistencia.modelos.Departamento;
import com.calva.controlasistencia.modelos.Empleado;
import com.calva.controlasistencia.modelos.Rol;
import com.calva.controlasistencia.modelos.dto.AsistenciaDto;
import com.calva.controlasistencia.modelos.dto.EmpleadoDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private EmpleadoMapper empleadoMapper;
    private EmpleadoRepository empleadoRepository;
    private DepartamentoRepository departamentoRepository;
    private RolRepository rolRepository;
    private AsistenciaRepository asistenciaRepository;

    @GetMapping("")
    public String empleados(Model model) {
        return "empleados";
    }

    @PostMapping("/registrar")
    public String registrarEmpleado(@Valid @ModelAttribute("empleado") EmpleadoDto empleadoDto, BindingResult result,Model model) {

        System.out.println(empleadoDto.toString());

        if (result.hasErrors()) {
            model.addAttribute("empleado",empleadoDto);
            return "adminRegistro";
        }
        // Conversión de String a LocalTime
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horaEntradaTime = LocalTime.parse(empleadoDto.getHoraEntrada(), timeFormatter);
        LocalTime horaSalidaTime = LocalTime.parse(empleadoDto.getHoraSalida(), timeFormatter);

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Combinar fecha actual con hora de entrada y salida
        LocalDateTime horaEntradaDateTime = LocalDateTime.of(fechaActual, horaEntradaTime);
        LocalDateTime horaSalidaDateTime = LocalDateTime.of(fechaActual, horaSalidaTime);

        // Buscar departamento y rol por ID (ya implementado)
        Departamento departamento = departamentoRepository.findByNombre(empleadoDto.getDepartamento())
                .orElseThrow(() -> new ResourceNotFoundException("departamento","id",empleadoDto.getDepartamento()));

        // Buscar rol por ID (ya implementado)
        Rol rol = rolRepository.findByNombre(empleadoDto.getRol())
                .orElseThrow(() -> new ResourceNotFoundException("rol","id",empleadoDto.getRol()));

        // Convertir DTO a entidad
        Empleado empleado = empleadoMapper.empleadoDTOToEmpleado(empleadoDto);
        empleado.setHoraEntrada(horaEntradaDateTime);
        empleado.setHoraSalida(horaSalidaDateTime);
        empleado.setDepartamento(departamento);
        empleado.setRol(rol);

        // Guardar en la base de datos
        empleadoRepository.save(empleado);
        return "adminRegistro";
    }

    @PostMapping("/registrarAsistencia")
    public String registrarAsistencia(@Valid @ModelAttribute("empleado") AsistenciaDto asistenciaDto, BindingResult result,Model model) {
        Empleado empleado = empleadoRepository.findById(asistenciaDto.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("empleado","id",asistenciaDto.getIdEmpleado()));

        // sacar la hora de entrada del empleado y si ya ha pasado un minuto hacer algo
        LocalDateTime horaEntradaEmpleado = empleado.getHoraEntrada();
        LocalDateTime horaActual = LocalDateTime.now();

        // Verificar si ya se registró la entrada
        Optional<Asistencia> ultimaAsistencia = asistenciaRepository.findTopByEmpleadoOrderByFechaDesc(empleado);
        if (ultimaAsistencia.isPresent() && ultimaAsistencia.get().getHoraSalida() == null) {
            model.addAttribute("entradaError2", "Ya has registrado la entrada");
            return "empleados";
        }

        // Calcular la diferencia en minutos
        long minutosDeRetraso = ChronoUnit.MINUTES.between(horaEntradaEmpleado, horaActual);

        if (minutosDeRetraso <= 10) {
            // Dentro de la tolerancia, registrar asistencia normal
            registrarAsistencia(empleado, Asistencia.TipoRegistro.NORMAL);
            model.addAttribute("entradaSuccess", "Llegaste a tiempo , EntradaRegistrada");
        } else if (minutosDeRetraso > 10 && minutosDeRetraso <= 30) {
            // Entre 10 y 30 minutos tarde, registrar como retardo
            registrarAsistencia(empleado, Asistencia.TipoRegistro.RETARDO);
            model.addAttribute("entradaSuccess", "Llegaste con retardo , EntradaRegistrada con 100 pesos de descuento");
        } else {
            // Más de 30 minutos tarde, no registrar entrada
            registrarAsistencia(empleado, Asistencia.TipoRegistro.FALTA);
            model.addAttribute("entradaSuccess", "Llegaste muy tarde , No puedes ingresar , Hasta mañana");
        }
        return "empleados";
    }

    // Método para registrar asistencia normal
    private void registrarAsistencia(Empleado empleado, Asistencia.TipoRegistro tipoRegistro) {
        Asistencia asistencia = new Asistencia();
        asistencia.setEmpleado(empleado);
        asistencia.setFecha(LocalDate.now());
        asistencia.setHoraEntrada(LocalTime.now());
        asistencia.setTipoRegistro(tipoRegistro);
        asistenciaRepository.save(asistencia);
    }

    @PostMapping("/registrarSalida")
    public String registrarSalida(@Valid @ModelAttribute("empleado") AsistenciaDto asistenciaDto, BindingResult result,Model model) {

        System.out.println(asistenciaDto.toString());

        Empleado empleado = empleadoRepository.findById(asistenciaDto.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("empleado","id",asistenciaDto.getIdEmpleado()));

        // Buscar la última asistencia del empleado
        Optional<Asistencia> ultimaAsistencia = asistenciaRepository.findTopByEmpleadoOrderByFechaDesc(empleado);

        if(ultimaAsistencia.isEmpty()){
            model.addAttribute("entradaError", "No has registrado la entrada");
            return "empleados";
        }

        // Verificar si ya se registró la salida
        if (ultimaAsistencia.get().getHoraSalida() != null) {
            // Ya se registró la salida, no hacer nada
            model.addAttribute("salidaError", "Ya has registrado la salida");
            return "empleados";
        }

        // Actualizar la hora de salida
        ultimaAsistencia.get().setHoraSalida(LocalTime.now());
        asistenciaRepository.save(ultimaAsistencia.get());
        model.addAttribute("salidaSuccess", "Salida registrada correctamente");
        return "empleados";
    }


}