package br.com.bancodigital.api.model.dto;

import br.com.bancodigital.api.enums.TipoConta;

public class ContaRequestDTO {

    private Long clienteId;
    private TipoConta tipoConta;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }
}
