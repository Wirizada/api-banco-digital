package br.com.bancodigital.api.service;

import br.com.bancodigital.api.enums.TipoConta;
import br.com.bancodigital.api.model.dto.ContaRequestDTO;
import br.com.bancodigital.api.model.entity.Cliente;
import br.com.bancodigital.api.model.entity.Conta;
import br.com.bancodigital.api.repository.ClienteRepository;
import br.com.bancodigital.api.repository.ContaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private static final String AGENCIA_PADRAO = "0001";

    public Conta criarConta(ContaRequestDTO ContaDTO){

        Cliente cliente = clienteRepository.findById(ContaDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID: " + ContaDTO.getClienteId()));

        Conta novaConta = new Conta();
        novaConta.setCliente(cliente);
        novaConta.setTipoConta(ContaDTO.getTipoConta());
        novaConta.setAgencia(AGENCIA_PADRAO);

        String numeroConta = gerarNumeroContaUnico();
        novaConta.setNumeroConta(numeroConta);

        return contaRepository.save(novaConta);
    }

    private String gerarNumeroContaUnico() {
        long numeroAleaorio = ThreadLocalRandom.current().nextLong(10000, 100000);
        return String.valueOf(numeroAleaorio);
    }

    public BigDecimal consultarSaldo(Long contaId){

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com o ID: " + contaId));

        return conta.getSaldo();
    }

    @Transactional
    public Conta realizarDeposito(Long contaId, BigDecimal valor) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve positivo e não nulo.");
        }

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com o ID: " + contaId));

        BigDecimal novoSaldo = conta.getSaldo().add(valor);
        conta.setSaldo(novoSaldo);

        return contaRepository.save(conta);
    }

    @Transactional
    public Conta realizarSaque(Long contaId, BigDecimal valor) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO)<= 0) {
            throw new IllegalArgumentException("Valor de saque deve ser positivo.");
        }

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        if(conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para saque.");
        }

        BigDecimal novoSaldo = conta.getSaldo().subtract(valor);
        conta.setSaldo(novoSaldo);

        return contaRepository.save(conta);
    }

    @Transactional
    public void realizarTransferencia(Long idContaOrigem, Long idContaDestino, BigDecimal valor) {

        if (idContaOrigem.equals(idContaDestino)) {
            throw new IllegalArgumentException("A conta de origem e destino não podem ser a mesma.");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo e não nulo.");
        }

        Conta contaOrigem = contaRepository.findById(idContaOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));

        Conta contaDestino = contaRepository.findById(idContaDestino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para transferência.");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
    }

    @Transactional
    public Conta aplicarTaxaManuntencao(Long contaId){

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        if (conta.getTipoConta() != TipoConta.CORRENTE) {
            throw new IllegalArgumentException("A taxa de manuntenção apenas aplica-se em contas correntes.");
        }

        BigDecimal taxaManuntencao;
        switch (conta.getCliente().getCategoria()) {
            case COMUM:;
                taxaManuntencao = new BigDecimal("12.00");
                break;
            case SUPER:                ;
                taxaManuntencao = new BigDecimal("8.00");
                break;
            case PREMIUM:                ;
                taxaManuntencao = BigDecimal.ZERO;
                break;
            default:
                throw new IllegalArgumentException("Categoria de cliente inválida.");
        }

        if (taxaManuntencao.compareTo(BigDecimal.ZERO) == 0) {
            return conta;
        }

        conta.setSaldo(conta.getSaldo().subtract(taxaManuntencao));
        return contaRepository.save(conta);
    }

    @Transactional
    public Conta aplicarRendimentos(Long contaId) {

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        if (conta.getTipoConta() != TipoConta.POUPANCA) {
            throw new IllegalArgumentException("Rendimentos só aplicam-se em contas poupança.");
        }

        BigDecimal taxaAnual;
        switch (conta.getCliente().getCategoria()){
            case COMUM:
                taxaAnual = new BigDecimal("0.005");
                break;
            case SUPER:
                taxaAnual = new BigDecimal("0.007");
                break;
            case PREMIUM:
                taxaAnual = new BigDecimal("0.009");
                break;
            default:
                taxaAnual = BigDecimal.ZERO;
                break;
        }

        if(taxaAnual.compareTo(BigDecimal.ZERO) == 0) {
            return conta;
        }

        double taxaAnualDouble = taxaAnual.doubleValue();
        double taxaMensalDouble = Math.pow(1 + taxaAnualDouble, 1.0 / 12.0) - 1;
        BigDecimal taxaMensal = BigDecimal.valueOf(taxaMensalDouble);

        BigDecimal rendimento = conta.getSaldo().multiply(taxaMensal);
        conta.setSaldo(conta.getSaldo().add(rendimento));

        return contaRepository.save(conta);
    }
}
