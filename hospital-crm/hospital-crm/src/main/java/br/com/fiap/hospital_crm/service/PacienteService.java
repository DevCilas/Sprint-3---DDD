package br.com.fiap.hospital_crm.service;

import br.com.fiap.hospital_crm.exception.DuplicidadeException;
import br.com.fiap.hospital_crm.exception.EntidadeNaoEncontradaException;
import br.com.fiap.hospital_crm.model.Paciente;
import br.com.fiap.hospital_crm.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    // cadastra paciente, mas antes verifica se o cpf ja existe
    public Paciente cadastrarPaciente(Paciente paciente) {
        if (paciente.getNome() == null || paciente.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do paciente é obrigatório.");
        }
        if (paciente.getCpf() == null || paciente.getCpf().isBlank()) {
            throw new IllegalArgumentException("O CPF do paciente é obrigatório.");
        }
        if (paciente.getDataNascimento() == null) {
            throw new IllegalArgumentException("A data de nascimento do paciente é obrigatória.");
        }
        if (paciente.getSexo() == null || paciente.getSexo().isBlank()) {
            throw new IllegalArgumentException("O sexo do paciente é obrigatório.");
        }

        if (pacienteRepository.existePorCpf(paciente.getCpf())) {
            throw new DuplicidadeException("Paciente", "CPF", paciente.getCpf());
        }

        return pacienteRepository.salvar(paciente);
    }

    // calcula o IMC do paciente: peso / (altura * altura)
    public String calcularIMC(Paciente paciente) {
        if (paciente.getPeso() == null || paciente.getPeso() <= 0) {
            throw new IllegalArgumentException("O peso do paciente deve ser maior que zero.");
        }
        if (paciente.getAltura() == null || paciente.getAltura() <= 0) {
            throw new IllegalArgumentException("A altura do paciente deve ser maior que zero.");
        }

        double imc = paciente.getPeso() / (paciente.getAltura() * paciente.getAltura());
        String classificacao;

        if (imc < 18.5) {
            classificacao = "Abaixo do peso";
        } else if (imc < 25.0) {
            classificacao = "Peso normal";
        } else if (imc < 30.0) {
            classificacao = "Sobrepeso";
        } else if (imc < 35.0) {
            classificacao = "Obesidade grau I";
        } else if (imc < 40.0) {
            classificacao = "Obesidade grau II";
        } else {
            classificacao = "Obesidade grau III";
        }

        return String.format("IMC: %.2f - Classificação: %s", imc, classificacao);
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente", id));
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.listarTodos();
    }
}
