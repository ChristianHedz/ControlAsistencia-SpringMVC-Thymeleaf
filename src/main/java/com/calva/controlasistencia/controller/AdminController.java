package com.calva.controlasistencia.controller;

import com.calva.controlasistencia.Repositorios.EmpleadoRepository;
import com.calva.controlasistencia.modelos.Empleado;
import com.calva.controlasistencia.modelos.dto.AdminDto;
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
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private EmpleadoRepository empleadoRepository;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("admin", new AdminDto());
        return "adminLogin";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("empleado", new EmpleadoDto());
        return "adminRegistro";
    }

    @PostMapping("/login")
    public String acceder(@Valid @ModelAttribute("admin") AdminDto adminDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "adminLogin";
        }

        Optional<Empleado> empleado = empleadoRepository.findByUsuarioAndContrasena(adminDto.getUsuario(), adminDto.getContrasena());

        if(empleado.isPresent() && empleado.get().getRol().getNombre().equals("Administrativo")) {
            model.addAttribute("empleado", new EmpleadoDto());
            return "adminRegistro";
        }

        model.addAttribute("loginError", "Usuario o contraseña incorrectos");
        return "adminLogin";
    }


}