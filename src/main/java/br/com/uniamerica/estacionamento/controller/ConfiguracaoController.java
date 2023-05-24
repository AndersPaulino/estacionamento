package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/configuracao")
public class ConfiguracaoController {

    @Autowired
    ConfiguracaoRepository configuracaoRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(this.configuracaoRepository.findById(id));
    }

    @GetMapping("/{ativo}")
    public ResponseEntity<?> findByAtivo(@PathVariable boolean ativo){
        List<Configuracao> configuracoes = this.configuracaoRepository.findByAtivo(ativo);

        if (configuracoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(configuracoes);
    }
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Configuracao> configuracaos = this.configuracaoRepository.findAll();

        if (configuracaos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(configuracaos);
    }


    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Configuracao configuracao) {
        this.configuracaoRepository.save(configuracao);
        return ResponseEntity.ok().body("Registro cadastrado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable final @NotNull Long id, @RequestBody final Configuracao configuracao) {
        if (id.equals(configuracao.getId()) && !this.configuracaoRepository.findById(id).isEmpty()) {
            this.configuracaoRepository.save(configuracao);
        } else {
            return ResponseEntity.badRequest().body("Id nao foi encontrado");
        }
        return ResponseEntity.ok().body("Registro atualizado com sucesso");
    }
}
