package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    @Query("From Movimentacao where ativo = :ativo")
    public List<Movimentacao> findByAtivo(@Param("ativo")final boolean ativo);

    @Query(value = "From Movimentacao where veiculo = :veiculo")
    List<Movimentacao> findByVeiculo(@Param("veiculo") Veiculo veiculo);

    @Query(value = "From Movimentacao where condutor = :condutor")
    List<Movimentacao> findByCondutor(@Param("condutor") Condutor condutor);
}
