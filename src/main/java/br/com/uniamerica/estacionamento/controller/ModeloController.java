package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.*;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.ModeloService;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/modelo")
public class ModeloController {

   private VeiculoRepository veiculoRepository;


   private MarcaRepository marcaRepository;

    private final ModeloService modeloService;
    private final ModeloRepository modeloRepository;

    @Autowired
    public ModeloController(ModeloService modeloService, ModeloRepository modeloRepository, MarcaRepository marcaRepository, VeiculoRepository veiculoRepository) {
        this.modeloService = modeloService;
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modelo> findById(@PathVariable Long id) {
        Optional<Modelo> modelo = modeloRepository.findById(id);
        if (modelo.isPresent()) {
            return ResponseEntity.ok().body(modelo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nome/{nomeModelo}")
    public ResponseEntity<?> findByNomeModelo(@PathVariable String nomeModelo) {
        List<Modelo> modelos = modeloRepository.findByNomeModelo(nomeModelo);
        if (modelos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(modelos);
        }
    }

    @GetMapping("/ativo/{ativo}")
    public ResponseEntity<?> findByAtivo(@PathVariable boolean ativo) {
        List<Modelo> modelos = modeloRepository.findByAtivo(ativo);
        if (modelos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(modelos);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Modelo> modelos = modeloRepository.findAll();
        if (modelos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(modelos);
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Modelo modelo) {
        try {
            modeloService.cadastrar(modelo);
            return ResponseEntity.ok().body("Registro cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable @NotNull Long id, @RequestBody Modelo modelo) {
        try {
            modeloService.atualizarModelo(id, modelo);
            return ResponseEntity.ok().body("Registro atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o registro.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        final Modelo modelo = this.modeloRepository.findById(id).orElse(null);
        if (modelo != null) {
            List<Veiculo> veiculosVinculados = this.veiculoRepository.findByModelo(modelo);
            if (veiculosVinculados.isEmpty()) {
                this.modeloRepository.delete(modelo);
                return ResponseEntity.ok("Registro deletado com sucesso!");
            } else {
                return ResponseEntity.badRequest().body("Não é possível deletar o modelo, pois existem veículos vinculados a ele.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
