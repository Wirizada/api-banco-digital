package br.com.bancodigital.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seguros")
public class SeguroController {

    @GetMapping
    public ResponseEntity<List<String>> listarTiposDeSeguro() {
        return ResponseEntity.ok(List.of("VIAGEM", "FRAUDE"));
    }
}
