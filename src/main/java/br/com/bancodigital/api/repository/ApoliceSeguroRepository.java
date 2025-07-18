package br.com.bancodigital.api.repository;

import br.com.bancodigital.api.model.entity.ApoliceSeguro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApoliceSeguroRepository extends JpaRepository<ApoliceSeguro, Long> {
}
