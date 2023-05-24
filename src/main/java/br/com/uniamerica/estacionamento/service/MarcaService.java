package br.com.uniamerica.estacionamento.service;


import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final VeiculoRepository veiculoRepository;

    @Autowired
    public MarcaService(MarcaRepository marcaRepository, ModeloRepository modeloRepository, VeiculoRepository veiculoRepository) {
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public void validarMarca(final Marca marca) {
        if (marca.getNomeMarca() == null || marca.getNomeMarca().isEmpty()) {
            throw new IllegalArgumentException("Nome da Marca não informado!");
        }
        if (!marca.getNomeMarca().matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Nome da Marca inválido!");
        }
    }

    public Optional<Marca> findById(Long id) {
        return marcaRepository.findById(id);
    }

    public List<Marca> findByNomeMarca(String nomeMarca) {
        return marcaRepository.findByNomeMarca(nomeMarca);
    }

    public List<Marca> findByAtivo(boolean ativo) {
        return marcaRepository.findByAtivo(ativo);
    }

    public List<Marca> findAll() {
        return marcaRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public void cadastrar(final Marca marca) {
        validarMarca(marca);
        marcaRepository.save(marca);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void atualizar(Long id, Marca marca) {
        validarMarca(marca);

        Optional<Marca> marcaExistente = marcaRepository.findById(id);

        if (marcaExistente.isPresent()) {
            Marca marcaAtualizada = marcaExistente.get();

            if (marca.getNomeMarca() != null) {
                marcaAtualizada.setNomeMarca(marca.getNomeMarca());
            }

            marcaRepository.save(marcaAtualizada);
        } else {
            throw new IllegalArgumentException("Id inválido!");
        }
    }
}