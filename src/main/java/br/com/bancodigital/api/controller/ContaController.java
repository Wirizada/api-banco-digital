package br.com.bancodigital.api.controller;

import br.com.bancodigital.api.model.dto.ContaRequestDTO;
import br.com.bancodigital.api.model.dto.TransacaoDTO;
import br.com.bancodigital.api.model.dto.TransferenciaDTO;
import br.com.bancodigital.api.model.entity.Conta;
import br.com.bancodigital.api.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<?> criarConta(@RequestBody ContaRequestDTO contaRequestDTO){
        try {
            Conta novaConta = contaService.criarConta(contaRequestDTO);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(novaConta.getId())
                    .toUri();

            return ResponseEntity.created(location).body(novaConta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao criar conta: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<?> consultarSaldo(@PathVariable Long id) {
        try {
            BigDecimal saldo = contaService.consultarSaldo(id);
            return ResponseEntity.ok(saldo);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<?> realizarDeposito(@PathVariable Long id, @RequestBody TransacaoDTO transacaoDTO){
        try {
            Conta contaAtualizada = contaService.realizarDeposito(id, transacaoDTO.getValor());
            return ResponseEntity.ok(contaAtualizada);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<?> realizarSaque(@PathVariable Long id, @RequestBody TransacaoDTO transacaoDTO) {
        try {
            Conta contaAtualizada = contaService.realizarSaque(id, transacaoDTO.getValor());
            return ResponseEntity.ok(contaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/transferencia")
    public ResponseEntity<?> realizarTransferencia(@PathVariable Long id, @RequestBody TransferenciaDTO transferenciaDTO){
        try {
            contaService.realizarTransferencia(
                    id,
                    transferenciaDTO.getIdContaDestino(),
                    transferenciaDTO.getValor()
            );

            return ResponseEntity.ok().body("Transferência realizada com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/manuntencao")
    public ResponseEntity<?> aplicarTaxaManuntencao(@PathVariable Long id) {
        try {
            Conta contaAtualizada = contaService.aplicarTaxaManuntencao(id);
            return ResponseEntity.ok(contaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/rendimentos")
    public ResponseEntity<?> aplicarRendimentos(@PathVariable Long id) {
        try {
            Conta contaAtualizada = contaService.aplicarRendimentos(id);
            return ResponseEntity.ok(contaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
