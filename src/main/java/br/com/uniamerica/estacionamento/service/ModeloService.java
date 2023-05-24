package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ModeloService {
    private final ModeloRepository modeloRepository;

    @Autowired
    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    public void validarModelo(final Modelo modelo) {
        if (modelo.getNomeModelo() == null || modelo.getNomeModelo().isEmpty()){
            throw new IllegalArgumentException("Nome do Modelo não informado");
        }
        if (!modelo.getNomeModelo().matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Nome do Modelo inválido");
        }
    }

    public Optional<Modelo> findById(Long id) {
        return modeloRepository.findById(id);
    }

    public List<Modelo> findByNomeModelo(String nomeModelo) {
        return modeloRepository.findByNomeModelo(nomeModelo);
    }

    public List<Modelo> findByAtivo(boolean ativo) {
        return modeloRepository.findByAtivo(ativo);
    }

    public List<Modelo> findAll() {
        return modeloRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public void cadastrar(final Modelo modelo) {
        validarModelo(modelo);
        modeloRepository.save(modelo);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void atualizarModelo(Long id, Modelo modelo) {
        validarModelo(modelo);

        Optional<Modelo> modeloExistente = modeloRepository.findById(id);

        if (modeloExistente.isPresent()) {
            Modelo modeloAtualizado = modeloExistente.get();

            if (modelo.getNomeModelo() != null) {
                modeloAtualizado.setNomeModelo(modelo.getNomeModelo());
            }

            if (modelo.getMarca() != null) {
                modeloAtualizado.setMarca(modelo.getMarca());
            }

            modeloRepository.save(modeloAtualizado);
        } else {
            throw new IllegalArgumentException("Id inválido para o modelo!");
        }
    }

}
