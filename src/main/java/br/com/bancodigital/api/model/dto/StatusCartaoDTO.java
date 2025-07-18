package br.com.bancodigital.api.model.dto;

import br.com.bancodigital.api.enums.StatusCartao;

public class StatusCartaoDTO {
    private StatusCartao novoStatus;

    public StatusCartao getNovoStatus() {
        return novoStatus;
    }

    public void setNovoStatus(StatusCartao novoStatus) {
        this.novoStatus = novoStatus;
    }
}
