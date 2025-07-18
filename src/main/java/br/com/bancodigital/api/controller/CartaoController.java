package br.com.bancodigital.api.controller;

import br.com.bancodigital.api.model.dto.CartaoRequestDTO;
import br.com.bancodigital.api.model.entity.Cartao;
import br.com.bancodigital.api.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<?> emitirCartao(@RequestBody CartaoRequestDTO cartaoRequestDTO) {
        try {
            Cartao novoCartao = cartaoService.emitirCatao(cartaoRequestDTO);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(novoCartao.getId())
                    .toUri();

            return ResponseEntity.created(location).body(novoCartao);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
