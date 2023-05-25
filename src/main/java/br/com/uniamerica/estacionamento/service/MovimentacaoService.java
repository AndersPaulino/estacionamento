package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final CondutorRepository condutorRepository;

    @Autowired
    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, ConfiguracaoRepository configuracaoRepository, CondutorRepository condutorRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.condutorRepository = condutorRepository;
    }



    public Optional<Movimentacao> findById(Long id) {
        return movimentacaoRepository.findById(id);
    }

    public List<Movimentacao> findByAtivo(boolean ativo) {
        return movimentacaoRepository.findByAtivo(ativo);
    }

    public List<Movimentacao> findAll() {
        return movimentacaoRepository.findAll();
    }
}
