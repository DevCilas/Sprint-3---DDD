package br.com.fiap.hospital_crm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "CRM Hospital São Rafael - API funcionando!";
    }
}
