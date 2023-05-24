package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    @Query("From Modelo where ativo = :ativo")
    public List<Modelo> findByAtivo(@Param("ativo")final boolean ativo);

    @Query(value = "From Modelo where marca = :marca")
    List<Modelo> findByMarca(@Param("marca")Marca marca);

    @Query("From Modelo where nomeModelo = :nomeModelo")
    List<Modelo> findByNomeModelo(@Param("nomeModelo") String nomeModelo);
}
