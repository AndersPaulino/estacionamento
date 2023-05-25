package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }
    public void validarVeiculo(final Veiculo veiculo) {

        if(veiculo.getPlaca() == null){
            throw new IllegalArgumentException("Veículo não informado");
        }
        if(veiculo.getModelo() == null){
            throw new IllegalArgumentException("Modelo não informado");
        }
        if(veiculo.getCor() == null){
            throw new IllegalArgumentException("Cor do veículo não informada");
        }
        if(veiculo.getTipo() == null){
            throw new IllegalArgumentException("Tipo não informado");
        }
        /*if(!veiculo.getPlaca().matches("^[A-Z]{3}-\\d{4}$") || !veiculo.getPlaca().matches("^[A-Z]{3}\\d{1}[A-Z]{1}\\d{2}$")) {
            throw new IllegalArgumentException("Placa inválida");
        }*/

        veiculoRepository.save(veiculo);
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void cadastrar(Veiculo veiculo){
        validarVeiculo(veiculo);
        veiculoRepository.save(veiculo);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void atualizar(Long id, Veiculo veiculo) {

        Optional<Veiculo> veiculoExistente = veiculoRepository.findById(id);

        if (veiculoExistente.isPresent()) {
            Veiculo veiculoAtualizado = veiculoExistente.get();

            if (veiculo.getPlaca() != null) {
                veiculoAtualizado.setPlaca(veiculo.getPlaca());
            }
            if (veiculo.getModelo() != null) {
                veiculoAtualizado.setModelo(veiculo.getModelo());
            }
            if (veiculo.getCor() != null) {
                veiculoAtualizado.setCor(veiculo.getCor());
            }
            if (veiculo.getTipo() != null) {
                veiculoAtualizado.setTipo(veiculo.getTipo());
            }

            veiculoRepository.save(veiculoAtualizado);
        } else {
            throw new IllegalArgumentException("Id inválido!");
        }
    }


}