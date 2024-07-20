package com.calva.controlasistencia.controller;


import com.calva.controlasistencia.Repositorios.DescuentoRepository;
import com.calva.controlasistencia.modelos.Descuento;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/descuentos")
public class DescuentoController {

    private DescuentoRepository descuentoRepository;

    @GetMapping("")
    public String descuentos(Model model) {
        List<Descuento> descuentos = descuentoRepository.findAll();
        model.addAttribute("descuentos", descuentos);
        return "descuentos";
    }

}
