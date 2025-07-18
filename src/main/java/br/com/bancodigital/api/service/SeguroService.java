package br.com.bancodigital.api.service;

import br.com.bancodigital.api.enums.TipoSeguro;
import br.com.bancodigital.api.model.entity.ApoliceSeguro;
import br.com.bancodigital.api.model.entity.Cartao;
import br.com.bancodigital.api.repository.ApoliceSeguroRepository;
import br.com.bancodigital.api.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class SeguroService {

    @Autowired
    private ApoliceSeguroRepository apoliceRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    public void contratarSeguroFraudeAutomatico(Cartao cartao) {
        ApoliceSeguro apolice = new ApoliceSeguro();
        apolice.setNumeroApolice("APF-" + UUID.randomUUID().toString());
        apolice.setCartao(cartao);
        apolice.setTipoSeguro(TipoSeguro.FRAUDE);
        apolice.setValorApolice(new BigDecimal("5000.00"));
        apolice.setValorMensal(BigDecimal.ZERO);
        apolice.setCondicoes("Cobertura contra fraudes, clonagem e transações não autorizadas.");
        apolice.setDataContratacao(LocalDate.now());

        apoliceRepository.save(apolice);
    }
}