package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.*;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.MarcaService;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/marca")
public class MarcaController {

    private final MarcaService marcaService;
    private final MarcaRepository marcaRepository;

    private final ModeloRepository modeloRepository;

    private final VeiculoRepository veiculoRepository;

    @Autowired
    public MarcaController(MarcaService marcaService, MarcaRepository marcaRepository, ModeloRepository modeloRepository, VeiculoRepository veiculoRepository) {
        this.marcaService = marcaService;
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> findById(@PathVariable Long id) {
        Optional<Marca> marca = marcaRepository.findById(id);
        if (marca.isPresent()) {
            return ResponseEntity.ok().body(marca.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nome/{nomeMarca}")
    public ResponseEntity<?> findByNomeMarca(@PathVariable String nomeMarca) {
        List<Marca> marcas = marcaRepository.findByNomeMarca(nomeMarca);
        if (marcas.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(marcas);
        }
    }

    @GetMapping("/ativo/{ativo}")
    public ResponseEntity<?> findByAtivo(@PathVariable boolean ativo) {
        List<Marca> marcas = marcaRepository.findByAtivo(ativo);
        if (marcas.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(marcas);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Marca> marcas = marcaRepository.findAll();
        if (marcas.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(marcas);
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Marca marca) {
        try {
            marcaService.cadastrar(marca);
            return ResponseEntity.ok().body("Registro cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable @NotNull Long id, @RequestBody Marca marca) {
        try {
            marcaService.atualizar(id, marca);
            return ResponseEntity.ok().body("Registro atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o registro.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        final Marca marca = this.marcaRepository.findById(id).orElse(null);
        if (marca != null) {
            List<Modelo> modelosVinculados = this.modeloRepository.findByMarca(marca);
            if (modelosVinculados.isEmpty()) {
                this.marcaRepository.delete(marca);
                return ResponseEntity.ok("Registro deletado com sucesso!");
            } else {
                return ResponseEntity.badRequest().body("Não é possível deletar a marca, pois existem modelos vinculados a ela.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
