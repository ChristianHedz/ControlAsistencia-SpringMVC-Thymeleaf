package com.calva.controlasistencia.controller;

import com.calva.controlasistencia.Mappers.EmpleadoMapper;
import com.calva.controlasistencia.Repositorios.*;
import com.calva.controlasistencia.excepciones.ResourceNotFoundException;
import com.calva.controlasistencia.modelos.*;
import com.calva.controlasistencia.modelos.dto.AsistenciaDto;
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
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.IntStream;

@AllArgsConstructor
@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

    private EmpleadoMapper empleadoMapper;
    private EmpleadoRepository empleadoRepository;
    private DepartamentoRepository departamentoRepository;
    private RolRepository rolRepository;
    private AsistenciaRepository asistenciaRepository;
    private DescuentoRepository descuentoRepository;

    @GetMapping("")
    public String empleados(Model model) {
        model.addAttribute("asistencia", new AsistenciaDto());
        return "asistencias";
    }

    @PostMapping("/registrarAsistencia")
    public String registrarAsistencia(@Valid @ModelAttribute("asistencia") AsistenciaDto asistenciaDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("asistencia", asistenciaDto);
            return "asistencias";
        }

        Empleado empleado = empleadoRepository.findById(Long.parseLong(asistenciaDto.getIdEmpleado()))
                .orElseThrow(() -> new ResourceNotFoundException("empleado", "id", asistenciaDto.getIdEmpleado()));

        if (empleado.getEstatus().equals("BAJA")) {
            model.addAttribute("entradaError", "No puedes registrar asistencia, estás dado de baja");
            return "asistencias";
        }

        // sacar la hora de entrada del empleado y si ya ha pasado un minuto hacer algo
        LocalDateTime horaEntradaEmpleado = empleado.getHoraEntrada();
        LocalDateTime horaActual = LocalDateTime.now();

//        // Verificar si ya se registró la entrada
//        Optional<Asistencia> ultimaAsistencia = asistenciaRepository.findTopByEmpleadoOrderByFechaDesc(empleado);
//        if (ultimaAsistencia.isPresent() && ultimaAsistencia.get().getHoraSalida() == null) {
//            model.addAttribute("entradaError2", "Ya has registrado la entrada");
//            return "empleados";
//        }

        // Calcular la diferencia en minutos
        long minutosDeRetraso = ChronoUnit.MINUTES.between(horaEntradaEmpleado, horaActual);

        if (minutosDeRetraso <= 10) {
            registrarAsistencia(empleado, Asistencia.TipoRegistro.NORMAL);
            model.addAttribute("entradaSuccess", "Llegaste a tiempo , EntradaRegistrada");
        } else if (minutosDeRetraso > 10 && minutosDeRetraso <= 30) {
            registrarAsistencia(empleado, Asistencia.TipoRegistro.RETARDO);
            aplicarDescuento(empleado, Descuento.Tipo.RETARDO, 100.0);
            empleado.setRetardos(empleado.getRetardos() + 1);
            model.addAttribute("entradaSuccess", "Llegaste con retardo , EntradaRegistrada con 100 pesos de descuento");
        } else {
            aplicarDescuento(empleado, Descuento.Tipo.FALTA, 1000.0);
            empleado.setFaltas(empleado.getFaltas() + 1);
            model.addAttribute("entradaSuccess", "Llegaste muy tarde , No puedes ingresar , Hasta mañana");
        }
        manejarRetardos(empleado);
        manejarFaltas(empleado);
        model.addAttribute("asistencia", new AsistenciaDto());
        return "asistencias";
    }


    @PostMapping("/registrarSalida")
    public String registrarSalida(@Valid @ModelAttribute("asistencia") AsistenciaDto asistenciaDto, BindingResult result, Model model) {

        System.out.println(asistenciaDto.toString());

        if (result.hasErrors()) {
            model.addAttribute("asistencia", asistenciaDto);
            return "asistencias";
        }

        Empleado empleado = empleadoRepository.findById(Long.parseLong(asistenciaDto.getIdEmpleado()))
                .orElseThrow(() -> new ResourceNotFoundException("empleado", "id", asistenciaDto.getIdEmpleado()));

        // Buscar la última asistencia del empleado
        Optional<Asistencia> ultimaAsistencia = asistenciaRepository.findTopByEmpleadoOrderByFechaDesc(empleado);

        if (ultimaAsistencia.isEmpty()) {
            model.addAttribute("entradaError", "No has registrado la entrada");
            return "asistencias";
        }

        // Verificar si ya se registró la salida
        if (ultimaAsistencia.get().getHoraSalida() != null) {
            // Ya se registró la salida, no hacer nada
            model.addAttribute("salidaError", "Ya has registrado la salida");
            return "asistencias";
        }

        // Actualizar la hora de salida
        ultimaAsistencia.get().setHoraSalida(LocalTime.now());
        asistenciaRepository.save(ultimaAsistencia.get());
        model.addAttribute("salidaSuccess", "Salida registrada correctamente");
        return "asistencias";
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

    public void aplicarDescuento(Empleado empleado, Descuento.Tipo tipo, Double monto) {
        Descuento descuento = new Descuento();
        descuento.setEmpleado(empleado);
        descuento.setTipo(tipo);
        descuento.setMonto(monto);
        descuentoRepository.save(descuento);
    }

    private void manejarRetardos(Empleado empleado) {
        int retardos = empleado.getRetardos();
        int faltasActuales = empleado.getFaltas();

        // Calcula las faltas adicionales basadas en los retardos actuales
        int faltasPorRetardos = retardos / 3;
        int faltasIncrementadas = faltasPorRetardos - faltasActuales;

        if (faltasIncrementadas > 0) {
            // Incrementa las faltas
            empleado.setFaltas(faltasActuales + faltasIncrementadas);

            // Aplica descuentos por cada falta incrementada
            IntStream.range(0, faltasIncrementadas)
                    .forEach(i -> aplicarDescuento(empleado, Descuento.Tipo.FALTA, 1000.0));
        }

        System.out.println("Faltas en manejarRetardos: " + empleado.getFaltas());
        empleadoRepository.save(empleado);
    }

    private void manejarFaltas(Empleado empleado){
        if(empleado.getFaltas() >= 3){
            empleado.setEstatus("BAJA");
        }
        empleadoRepository.save(empleado);
    }
}
