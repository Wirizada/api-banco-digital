package br.com.bancodigital.api.repository;

import br.com.bancodigital.api.model.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
