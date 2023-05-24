package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("From Veiculo where ativo = :ativo")
    public List<Veiculo> findByAtivo(@Param("ativo")final boolean ativo);

    @Query(value = "From Veiculo where modelo = :modelo")
    List<Veiculo> findByModelo(@Param("modelo") Modelo modelo);
}
