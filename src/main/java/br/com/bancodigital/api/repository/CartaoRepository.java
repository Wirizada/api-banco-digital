package br.com.bancodigital.api.repository;

import br.com.bancodigital.api.model.entity.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
}
