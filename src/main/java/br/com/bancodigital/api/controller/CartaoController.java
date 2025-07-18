package br.com.bancodigital.api.controller;

import br.com.bancodigital.api.model.dto.*;
import br.com.bancodigital.api.model.entity.Cartao;
import br.com.bancodigital.api.service.CartaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cartoes")
@SecurityRequirement(name = "bearerAuth")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<?> emitirCartao(@RequestBody CartaoRequestDTO cartaoRequestDTO) {
        try {
            Cartao novoCartao = cartaoService.emitirCartao(cartaoRequestDTO);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(novoCartao.getId())
                    .toUri();

            return ResponseEntity.created(location).body(novoCartao);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/pagamento")
    public ResponseEntity<?> realizarPagamento(@PathVariable Long id, @RequestBody TransacaoDTO transacaoDTO) {
        try {
            Cartao cartaoAtualizado = cartaoService.realizarPagamento(id, transacaoDTO.getValor());
            return ResponseEntity.ok(cartaoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id, @RequestBody StatusCartaoDTO statusDTO) {
        try {
            Cartao cartao = cartaoService.alterarStatus(id, statusDTO.getNovoStatus());
            return ResponseEntity.ok(cartao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/limite")
    public ResponseEntity<?> alterarLimiteCredito(@PathVariable Long id, @RequestBody LimiteCartaoDTO limiteDTO) {
        try {
            Cartao cartao = cartaoService.alterarLimiteCredito(id, limiteDTO.getNovoLimite());
            return ResponseEntity.ok(cartao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/limite-diario")
    public ResponseEntity<?> alterarLimiteDiario(@PathVariable Long id, @RequestBody LimiteCartaoDTO limiteDTO) {
        try {
            Cartao cartao = cartaoService.alterarLimiteDiario(id, limiteDTO.getNovoLimite());
            return ResponseEntity.ok(cartao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/fatura")
    public ResponseEntity<?> consultarFatura(@PathVariable Long id) {
        try {
            FaturaDTO fatura = cartaoService.consultarFatura(id);
            return ResponseEntity.ok(fatura);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/fatura/pagamento")
    public ResponseEntity<?> pagarFatura(@PathVariable Long id, @RequestBody TransacaoDTO transacaoDTO) {
        try {
            Cartao cartao = cartaoService.pagarFatura(id, transacaoDTO.getValor());
            return ResponseEntity.ok(cartao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
