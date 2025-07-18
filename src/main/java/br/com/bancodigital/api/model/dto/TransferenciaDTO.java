package br.com.bancodigital.api.model.dto;

import java.math.BigDecimal;

public class TransferenciaDTO {

    private BigDecimal valor;
    private Long idContaDestino;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Long getIdContaDestino() {
        return idContaDestino;
    }

    public void setIdContaDestino(Long idContaDestino) {
        this.idContaDestino = idContaDestino;
    }
}
