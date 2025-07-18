package br.com.bancodigital.api.model.dto;

import java.math.BigDecimal;

public class LimiteCartaoDTO {
    private BigDecimal novoLimite;

    public BigDecimal getNovoLimite() {
        return novoLimite;
    }

    public void setNovoLimite(BigDecimal novoLimite) {
        this.novoLimite = novoLimite;
    }
}
