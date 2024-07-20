package com.calva.controlasistencia.controller;

import com.calva.controlasistencia.Repositorios.DepartamentoRepository;
import com.calva.controlasistencia.Repositorios.RolRepository;
import com.calva.controlasistencia.modelos.Departamento;
import com.calva.controlasistencia.modelos.Rol;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {
    private DepartamentoRepository departamentoRepository;
    private RolRepository rolRepository;
    @GetMapping("")
    public String departamentos(Model model) {
        List<Departamento> departamentos = departamentoRepository.findAll();
        List<Rol> roles =  rolRepository.findAll();
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("roles", roles);
        return "departamentos";
    }
}
