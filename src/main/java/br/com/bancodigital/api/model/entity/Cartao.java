package br.com.bancodigital.api.model.entity;

import br.com.bancodigital.api.enums.StatusCartao;
import br.com.bancodigital.api.enums.TipoCartao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cartoes")
public class Cartao {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCartao tipoCartao;

    @Column(unique = true, nullable = false,length = 16)
    private String numeroCartao;

    @Column(nullable = false, length = 8)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCartao statusCartao;

    private BigDecimal limiteCredito;
    private BigDecimal valorFaturaAtual;

    private BigDecimal limiteDiario;
    private BigDecimal gastoDiarioAtual;
    private LocalDate dataUltimaTransacao;

    public Cartao () {
        this.valorFaturaAtual = BigDecimal.ZERO;
        this.limiteCredito = BigDecimal.ZERO;
        this.statusCartao = StatusCartao.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public TipoCartao getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(TipoCartao tipoCartao) {
        this.tipoCartao = tipoCartao;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public StatusCartao getStatusCartao() {
        return statusCartao;
    }

    public void setStatusCartao(StatusCartao statusCartao) {
        this.statusCartao = statusCartao;
    }

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public BigDecimal getValorFaturaAtual() {
        return valorFaturaAtual;
    }

    public void setValorFaturaAtual(BigDecimal valorFaturaAtual) {
        this.valorFaturaAtual = valorFaturaAtual;
    }

    public BigDecimal getLimiteDiario() {
        return limiteDiario;
    }

    public void setLimiteDiario(BigDecimal limiteDiario) {
        this.limiteDiario = limiteDiario;
    }

    public BigDecimal getGastoDiarioAtual() {
        return gastoDiarioAtual;
    }

    public void setGastoDiarioAtual(BigDecimal gastoDiarioAtual) {
        this.gastoDiarioAtual = gastoDiarioAtual;
    }

    public LocalDate getDataUltimaTransacao() {
        return dataUltimaTransacao;
    }

    public void setDataUltimaTransacao(LocalDate dataUltimaTransacao) {
        this.dataUltimaTransacao = dataUltimaTransacao;
    }
}
