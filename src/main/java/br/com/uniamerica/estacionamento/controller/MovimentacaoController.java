package br.com.uniamerica.estacionamento.controller;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/movimentacao")
public class MovimentacaoController {
    private final MovimentacaoService movimentacaoService;
    private final MovimentacaoRepository movimentacaoRepository;

    private final ModeloRepository modeloRepository;
    private final VeiculoRepository veiculoRepository;

    @Autowired
    public MovimentacaoController(MovimentacaoService movimentacaoService, MovimentacaoRepository movimentacaoRepository, ModeloRepository modeloRepository, VeiculoRepository veiculoRepository) {
        this.movimentacaoService = movimentacaoService;
        this.movimentacaoRepository = movimentacaoRepository;
        this.modeloRepository = modeloRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimentacao> findById(@PathVariable Long id) {
        Optional<Movimentacao> movimentacao = movimentacaoRepository.findById(id);
        if (movimentacao.isPresent()) {
            return ResponseEntity.ok().body(movimentacao.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ativo/{ativo}")
    public ResponseEntity<?> findByAtivo(@PathVariable boolean ativo) {
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByAtivo(ativo);
        if (movimentacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(movimentacoes);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Movimentacao> movimentacoes = movimentacaoRepository.findAll();
        if (movimentacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(movimentacoes);
        }
    }


    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Movimentacao movimentacao) {
        try {
            Movimentacao novaMovimentacao = movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("Registro cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Movimentacao movimentacao) {
        try {
            final Movimentacao movimentacaobanco = this.movimentacaoRepository.findById(id).orElse(null);
            if(movimentacaobanco == null || !movimentacaobanco.getId().equals(movimentacao.getId())){
                throw new RuntimeException("O registro nao foi encontrado");
            }
            this.movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("registro cadastrado");

        }catch (DataIntegrityViolationException e){
            return ResponseEntity.internalServerError().body("erro" + e.getCause().getCause().getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body("erro" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);
        if (movimentacao != null) {
            this.movimentacaoRepository.delete(movimentacao);
            return ResponseEntity.ok("Registro deletado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}