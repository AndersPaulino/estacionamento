package br.com.uniamerica.estacionamento.service;
import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ConfiguracaoService {

    private ConfiguracaoRepository configuracaoRepository;

    @Autowired
    public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository) {
        this.configuracaoRepository = configuracaoRepository;
    }

    public void validarConfiguracao(Configuracao configuracao) {
        if (configuracao.getValorHora() == null) {
            throw new IllegalArgumentException("Valor da hora não informado");
        }
        if (configuracao.getValorMinutoMulta() == null) {
            throw new IllegalArgumentException("Valor da multa por minuto não informado");
        }
        if (configuracao.getInicioExpediente() == null) {
            throw new IllegalArgumentException("Início do expediente não informado");
        }
        if (configuracao.getFimExpediente() == null) {
            throw new IllegalArgumentException("Fim do expediente não informado");
        }
        if (configuracao.getTempoDeDesconto() == null) {
            throw new IllegalArgumentException("Tempo de desconto não informado");
        }

        configuracaoRepository.save(configuracao);
    }

    public void atualizar(Configuracao configuracao) {
        final Configuracao configuracaoBanco = configuracaoRepository.findById(configuracao.getId()).orElse(null);

        if (configuracao.getValorHora() == null) {
            throw new IllegalArgumentException("Valor da hora não foi informado");
        }
        if (configuracao.getValorMinutoMulta() == null) {
            throw new IllegalArgumentException("Valor da multa por minuto não foi informado");
        }
        if (configuracao.getInicioExpediente() == null) {
            throw new IllegalArgumentException("Início do expediente não foi informado");
        }
        if (configuracao.getFimExpediente() == null) {
            throw new IllegalArgumentException("Fim do expediente não foi informado");
        }
        if (configuracao.getTempoDeDesconto() == null) {
            throw new IllegalArgumentException("Tempo de desconto não foi informado");
        }

        if (configuracaoBanco != null && configuracaoBanco.getId().equals(configuracao.getId())) {
            throw new IllegalArgumentException("Não foi possível identificar");
        }

        configuracaoRepository.save(configuracao);
    }

}