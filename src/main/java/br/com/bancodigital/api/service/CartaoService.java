package br.com.bancodigital.api.service;

import br.com.bancodigital.api.model.dto.CartaoRequestDTO;
import br.com.bancodigital.api.model.entity.Cartao;
import br.com.bancodigital.api.model.entity.Conta;
import br.com.bancodigital.api.repository.CartaoRepository;
import br.com.bancodigital.api.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ContaRepository contaRepository;

    public Cartao emitirCatao(CartaoRequestDTO requestDTO) {

        Conta conta = contaRepository.findById(requestDTO.getContaId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        Cartao novoCartao = new Cartao();
        novoCartao.setConta(conta);
        novoCartao.setTipoCartao(requestDTO.getTipoCartao());
        novoCartao.setNumeroCartao(gerarNumeroCartao());
        novoCartao.setSenha("123456");

        switch (requestDTO.getTipoCartao()) {
            case CREDITO:
                switch (conta.getCliente().getCategoria()){
                    case COMUM:
                        novoCartao.setLimiteCredito(new BigDecimal("1000.00"));
                        break;
                    case SUPER:
                        novoCartao.setLimiteCredito(new BigDecimal("5000.00"));
                        break;
                    case PREMIUM:
                        novoCartao.setLimiteCredito(new BigDecimal("10000.00"));
                        break;
                }
            case DEBITO:
                novoCartao.setLimiteDiario(new BigDecimal("1000.00"));
                break;
        }

        return cartaoRepository.save(novoCartao);
    }

    private String gerarNumeroCartao() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(1000, 10000));
            if(i < 15) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}
