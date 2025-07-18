package br.com.bancodigital.api.model.entity;

import br.com.bancodigital.api.enums.TipoConta;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, length = 20)
    private String numeroConta;

    @Column(nullable = false, length = 10)
    private String agencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConta tipoConta;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Conta() {
        this.saldo = BigDecimal.ZERO;
    }

    public Conta(String numeroConta, String agencia, TipoConta tipoConta, Cliente cliente) {
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.tipoConta = tipoConta;
        this.cliente = cliente;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
