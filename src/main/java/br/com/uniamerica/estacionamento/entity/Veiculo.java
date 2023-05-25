package br.com.uniamerica.estacionamento.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "veiculos", schema = "public")
public class Veiculo extends AbstractEntity{
    @Getter @Setter
    @Column(name = "placa", nullable = false, unique = true, length = 10)
    private String placa;
    @Getter @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "modelo",unique = true)
    private Modelo modelo;
    @Getter @Setter
    @Column(name = "cor",nullable = false)
    private Cor cor;
    @Getter @Setter
    @Column(name = "tipo", nullable = false)
    private TipoVeiculo tipo;
    @Getter @Setter
    @Column(name = "ano_modelo", nullable = false, length = 5)
    private int anoModelo;


}
