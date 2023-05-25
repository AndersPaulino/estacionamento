package br.com.uniamerica.estacionamento.service;


import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CondutorService {
    private final CondutorRepository condutorRepository;

    @Autowired
    public CondutorService(CondutorRepository condutorRepository) {
        this.condutorRepository = condutorRepository;
    }

    public void validarCondutor(final Condutor condutor) {
        if (condutor.getNomeCondutor().isEmpty()){
            throw new IllegalArgumentException("Nome do Condutor não informado");
        }
        if (!condutor.getNomeCondutor().matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Nome do Condutor inválido");
        }
        if (condutor.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF do condutor não informado");
        }
        if (!condutor.getCpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new IllegalArgumentException("CPF do condutor inválido");
        }
        String cpf = condutor.getCpf();
        if (!validarCPF(cpf)) {
            throw new IllegalArgumentException("CPF do condutor inválido");
        }
        if (condutor.getTelefone().isEmpty()) {
            throw new IllegalArgumentException("Telefone do condutor não informado");
        }

        if (condutor.getTelefone().length() < 10) {
            throw new IllegalArgumentException("Telefone do condutor inválido");
        }

        // Salvar ou atualizar o condutor usando o JpaRepository
        condutorRepository.save(condutor);
    }
    private boolean validarCPF(String cpf) {
        // Remover caracteres não numéricos do CPF
        cpf = cpf.replaceAll("\\D+", "");

        // Verificar se o CPF possui 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Calcular os dígitos verificadores do CPF
        int digito1 = calcularDigitoVerificadorCPF(cpf.substring(0, 9));
        int digito2 = calcularDigitoVerificadorCPF(cpf.substring(0, 9) + digito1);

        // Verificar se os dígitos verificadores calculados são iguais aos informados no CPF
        return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);
    }

    private int calcularDigitoVerificadorCPF(String cpf) {
        int soma = 0;
        int peso = 10;

        for (int i = 0; i < cpf.length(); i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * peso;
            peso--;
        }

        int resto = soma % 11;
        if (resto < 2) {
            return 0;
        } else {
            return 11 - resto;
        }
    }

    public void validarCondutorCadastrado(final Condutor condutor) {
        String telefone = condutor.getTelefone();
        String nome = condutor.getNomeCondutor();
        String cpf = condutor.getCpf();

        if (nome != null) {
            // Verificar se o nome contém apenas letras maiúsculas, minúsculas e espaço
            if (!nome.matches("[a-zA-Z ]+")) {
                throw new IllegalArgumentException("Nome do Condutor inválido");
            }
        }

        if (telefone != null) {
            // Verificar se o telefone contém apenas números
            if (!telefone.matches("\\d+") && telefone.length() < 10) {
                throw new IllegalArgumentException("Telefone do Condutor inválido");
            }
        }

        if (cpf != null) {
            // Verificar se o telefone contém apenas números
            if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
                throw new IllegalArgumentException("Cpf do Condutor inválido");
            }
        }

        if (nome == null && telefone == null && cpf == null) {
            throw new IllegalArgumentException("Nenhum campo do fornecedor informado");
        }
    }

    public Optional<Condutor> findById(Long id) {
        return condutorRepository.findById(id);
    }

    public List<Condutor> findByNomeCondutor(String nomeCondutor) {
        return condutorRepository.findByNomeCondutor(nomeCondutor);
    }

    public List<Condutor> findByAtivo(boolean ativo) {
        return condutorRepository.findByAtivo(ativo);
    }

    public List<Condutor> findAll() {
        return condutorRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void cadastrarCondutor(Condutor condutor) {
        validarCondutor(condutor);
        condutorRepository.save(condutor);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void atualizarCondutor(Long id, Condutor condutor) {
        validarCondutorCadastrado(condutor);

        Optional<Condutor> condutorExistente = condutorRepository.findById(id);

        if (condutorExistente.isPresent()) {
            Condutor condutorAtualizado = condutorExistente.get();

            // Atualizar os campos desejados do condutor
            if (condutor.getNomeCondutor() != null) {
                condutorAtualizado.setNomeCondutor(condutor.getNomeCondutor());
            }

            if (condutor.getTelefone() != null) {
                condutorAtualizado.setTelefone(condutor.getTelefone());
            }

            if (condutor.getCpf() != null) {
                condutorAtualizado.setCpf(condutor.getCpf());
            }

            // Salvar as alterações no repositório
            condutorRepository.save(condutorAtualizado);
        } else {
            throw new IllegalArgumentException("Id inválido!");
        }
    }

    public void deletarCondutor(Long id) {
        condutorRepository.deleteById(id);
    }

}
