package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.CondutorService;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/condutor")
public class CondutorController {

    private final CondutorService condutorService;
    private final CondutorRepository condutorRepository;

    private  final MovimentacaoRepository movimentacaoRepository;

    @Autowired
    public  CondutorController(CondutorService condutorService, CondutorRepository condutorRepository, MovimentacaoRepository movimentacaoRepository) {
        this.condutorService = condutorService;
        this.condutorRepository = condutorRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Condutor> findById(@PathVariable Long id) {
        Optional<Condutor> condutor = condutorService.findById(id);
        if (condutor.isPresent()) {
            return ResponseEntity.ok().body(condutor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nome/{nomeCondutor}")
    public ResponseEntity<?> findByNomeCondutor(@PathVariable String nomeCondutor) {
        List<Condutor> condutores = condutorService.findByNomeCondutor(nomeCondutor);
        if (condutores.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(condutores);
        }
    }

    @GetMapping("/ativo/{ativo}")
    public ResponseEntity<?> findByAtivo(@PathVariable boolean ativo) {
        List<Condutor> condutores = condutorService.findByAtivo(ativo);
        if (condutores.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(condutores);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Condutor> condutores = condutorService.findAll();
        if (condutores.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(condutores);
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Condutor condutor) {
        try {
            condutorService.cadastrarCondutor(condutor);
            return ResponseEntity.ok().body("Registro cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable @NotNull Long id, @RequestBody Condutor condutor) {
        try {
            condutorService.atualizarCondutor(id, condutor);
            return ResponseEntity.ok().body("Registro atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o registro.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        final Condutor condutor = this.condutorRepository.findById(id).orElse(null);
        if (condutor != null) {
            List<Movimentacao> movimentacoesVinculadas = this.movimentacaoRepository.findByCondutor(condutor);
            if (movimentacoesVinculadas.isEmpty()) {
                this.condutorRepository.delete(condutor);
                return ResponseEntity.ok("Registro deletado com sucesso!");
            } else {
                return ResponseEntity.badRequest().body("Não é possível deletar o condutor, pois existem movimentações vinculadas a ele.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
