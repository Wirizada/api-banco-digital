package br.com.bancodigital.api.model.dto;

import java.math.BigDecimal;

public class TransacaoDTO {

    private BigDecimal valor;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
