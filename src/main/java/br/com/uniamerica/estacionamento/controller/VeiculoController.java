package br.com.uniamerica.estacionamento.controller;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.VeiculoService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/veiculo")
public class VeiculoController {

    private VeiculoRepository veiculoRepository;

    private VeiculoService veiculoService;


    private ModeloRepository modeloRepository;

    private MovimentacaoRepository movimentacaoRepository;

    public VeiculoController(VeiculoRepository veiculoRepository, VeiculoService veiculoService, ModeloRepository modeloRepository, MovimentacaoRepository movimentacaoRepository) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoService = veiculoService;
        this.modeloRepository = modeloRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> findById(@PathVariable Long id) {
        Optional<Veiculo> veiculo = veiculoRepository.findById(id);
        if (veiculo.isPresent()) {
            return ResponseEntity.ok().body(veiculo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ativo/{ativo}")
    public ResponseEntity<?> findByAtivo(@PathVariable boolean ativo) {
        List<Veiculo> veiculos = veiculoRepository.findByAtivo(ativo);
        if (veiculos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(veiculos);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Veiculo> veiculos = veiculoRepository.findAll();
        if (veiculos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(veiculos);
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Veiculo veiculo) {
        veiculoService.cadastrar(veiculo);
        return ResponseEntity.ok().body("Registro cadastrado com sucesso");
    }

    @GetMapping("/veiculos/modelo/{modeloId}")
    public ResponseEntity<List<Veiculo>> findByModelo(@PathVariable Long modeloId) {
        Optional<Modelo> optionalModelo = modeloRepository.findById(modeloId);
        if (optionalModelo.isPresent()) {
            Modelo modelo = optionalModelo.get();
            List<Veiculo> veiculos = veiculoRepository.findByModelo(modelo);
            if (veiculos.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(veiculos);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable @NotNull Long id, @RequestBody Veiculo veiculo) {
        try {
            veiculoService.atualizar(id, veiculo);
            return ResponseEntity.ok().body("Registro atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o registro.");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        final Veiculo veiculo = this.veiculoRepository.findById(id).orElse(null);
        if (veiculo != null) {
            List<Movimentacao> movimentacoesVinculadas = this.movimentacaoRepository.findByVeiculo(veiculo);
            if (movimentacoesVinculadas.isEmpty()) {
                this.veiculoRepository.delete(veiculo);
                return ResponseEntity.ok("Registro deletado com sucesso!");
            } else {
                return ResponseEntity.badRequest().body("Não é possível deletar o veículo, pois existem movimentações vinculadas a ele.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}