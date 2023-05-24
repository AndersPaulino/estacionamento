package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    @Query("From Marca where ativo = :ativo")
    public List<Marca> findByAtivo(@Param("ativo")final boolean ativo);

    @Query("From Marca where nomeMarca = :nomeMarca")
    List<Marca> findByNomeMarca(@Param("nomeMarca") String nomeMarca);

}
