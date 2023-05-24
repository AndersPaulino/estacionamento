package br.com.uniamerica.estacionamento.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "modelos",schema = "public")
public class Modelo extends AbstractEntity{
    @Getter @Setter
    @Column(name = "nome_modelo",nullable = false, unique = true , length = 15)
    private String nomeModelo;

    @Getter @Setter
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "marca", unique = true)
    private Marca marca;

}
