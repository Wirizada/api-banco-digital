package br.com.bancodigital.api.model.dto;

import br.com.bancodigital.api.enums.TipoCartao;
import br.com.bancodigital.api.enums.TipoConta;

public class CartaoRequestDTO {

    private Long contaId;
    private TipoCartao tipoCartao;

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }

    public TipoCartao getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(TipoCartao tipoCartao) {
        this.tipoCartao = tipoCartao;
    }
}
