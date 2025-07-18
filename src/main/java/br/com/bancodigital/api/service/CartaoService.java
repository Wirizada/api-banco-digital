package br.com.bancodigital.api.service;

import br.com.bancodigital.api.enums.StatusCartao;
import br.com.bancodigital.api.enums.TipoCartao;
import br.com.bancodigital.api.model.dto.CartaoRequestDTO;
import br.com.bancodigital.api.model.dto.FaturaDTO;
import br.com.bancodigital.api.model.entity.Cartao;
import br.com.bancodigital.api.model.entity.Conta;
import br.com.bancodigital.api.repository.CartaoRepository;
import br.com.bancodigital.api.repository.ContaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private SeguroService seguroService;

    public Cartao emitirCartao(CartaoRequestDTO requestDTO) {

        Conta conta = contaRepository.findById(requestDTO.getContaId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        Cartao novoCartao = new Cartao();
        novoCartao.setConta(conta);
        novoCartao.setTipoCartao(requestDTO.getTipoCartao());
        novoCartao.setNumeroCartao(gerarNumeroCartao());
        novoCartao.setSenha("123456");

        switch (requestDTO.getTipoCartao()) {
            case CREDITO:
                Cartao cartaoSalvo = cartaoRepository.save(novoCartao);
                seguroService.contratarSeguroFraudeAutomatico(cartaoSalvo);
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
                return cartaoSalvo;
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

    @Transactional
    public Cartao realizarPagamento(Long cartaId, BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo.");
        }

        Cartao cartao = cartaoRepository.findById(cartaId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (cartao.getStatusCartao() != StatusCartao.ATIVO) {
            throw new RuntimeException("Cartão não está ativo para pagamento.");
        }

        switch (cartao.getTipoCartao()){
            case DEBITO:
                pagamentoDebito(cartao, valor);
                break;
            case CREDITO:
                pagamentoCredito(cartao, valor);
                break;
        }

        return cartaoRepository.save(cartao);
    }

    private void pagamentoDebito(Cartao cartao,BigDecimal valor) {

        if (cartao.getDataUltimaTransacao() != null && !cartao.getDataUltimaTransacao().isEqual(LocalDate.now())) {
            cartao.setGastoDiarioAtual(BigDecimal.ZERO);
        }
        if (cartao.getGastoDiarioAtual().add(valor).compareTo(cartao.getLimiteDiario()) > 0) {
            throw new IllegalArgumentException("Limite diário de transação excedido.");
        }

        Conta conta = cartao.getConta();
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo em conta insuficiente.");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        cartao.setGastoDiarioAtual(cartao.getGastoDiarioAtual().add(valor));
        cartao.setDataUltimaTransacao(LocalDate.now());

        contaRepository.save(conta);
    }

    private void pagamentoCredito(Cartao cartao, BigDecimal valor) {

        BigDecimal limiteDisponivel = cartao.getLimiteCredito().subtract(cartao.getValorFaturaAtual());
        if (valor.compareTo(limiteDisponivel) > 0) {
            throw new IllegalArgumentException("Limite de crédito insuficiente.");
        }

        cartao.setValorFaturaAtual(cartao.getValorFaturaAtual().add(valor));
    }

    public Cartao alterarStatus(Long cartaoId, StatusCartao novoStatus){

        if (novoStatus == null) {
            throw new IllegalArgumentException("Novo status não pode ser nulo.");
        }

        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado!"));

        cartao.setStatusCartao(novoStatus);
        return cartaoRepository.save(cartao);
    }

    public Cartao alterarLimiteCredito(Long cartaoId, BigDecimal novoLimite){

        if (novoLimite == null || novoLimite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Novo limite deve ser um valor positivo.");
        }

        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado!"));

        if (cartao.getTipoCartao() != TipoCartao.CREDITO) {
            throw new IllegalArgumentException("Esta operação só é válida para cartões de crédito.");
        }

        cartao.setLimiteCredito(novoLimite);
        return cartaoRepository.save(cartao);
    }

    public Cartao alterarLimiteDiario(Long cartaoId, BigDecimal novoLimite) {

        if (novoLimite == null || novoLimite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Novo limite deve ser um valor positivo.");
        }

        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado!"));

        if (cartao.getTipoCartao() != TipoCartao.DEBITO) {
            throw new IllegalArgumentException("Esta operação só é válida para cartões de débito.");
        }

        cartao.setLimiteDiario(novoLimite);
        return cartaoRepository.save(cartao);
    }

    public FaturaDTO consultarFatura(Long cartaoId) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (cartao.getTipoCartao() != TipoCartao.CREDITO) {
            throw new IllegalArgumentException("Faturas só podem ser consultadas para cartões de crédito.");
        }

        FaturaDTO faturaDTO = new FaturaDTO();
        faturaDTO.setCartaoId(cartao.getId());
        faturaDTO.setValorTotal(cartao.getValorFaturaAtual());
        faturaDTO.setLimiteDisponivel(cartao.getLimiteCredito().subtract(cartao.getValorFaturaAtual()));
        faturaDTO.setDataVencimento(LocalDate.now().plusDays(10));

        return faturaDTO;
    }

    @Transactional
    public Cartao pagarFatura(Long cartaoId, BigDecimal valorPagamento) {
        if (valorPagamento == null || valorPagamento.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser positivo.");
        }

        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado!"));

        if (cartao.getTipoCartao() != TipoCartao.CREDITO) {
            throw new IllegalArgumentException("Faturas são aplicáveis apenas a cartões de crédito.");
        }

        if (valorPagamento.compareTo(cartao.getValorFaturaAtual()) > 0) {
            throw new IllegalArgumentException("O valor do pagamento não pode ser maior que o valor da fatura.");
        }

        Conta contaAssociada = cartao.getConta();
        if (contaAssociada.getSaldo().compareTo(valorPagamento) < 0) {
            throw new IllegalArgumentException("Saldo em conta insuficiente para pagar a fatura.");
        }

        contaAssociada.setSaldo(contaAssociada.getSaldo().subtract(valorPagamento));
        cartao.setValorFaturaAtual(cartao.getValorFaturaAtual().subtract(valorPagamento));

        contaRepository.save(contaAssociada);
        return cartaoRepository.save(cartao);
    }
}
