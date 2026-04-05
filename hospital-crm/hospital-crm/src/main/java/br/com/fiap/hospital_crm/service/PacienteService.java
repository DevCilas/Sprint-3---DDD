package br.com.fiap.hospital_crm.service;

import br.com.fiap.hospital_crm.exception.DuplicidadeException;
import br.com.fiap.hospital_crm.exception.EntidadeNaoEncontradaException;
import br.com.fiap.hospital_crm.model.Paciente;
import br.com.fiap.hospital_crm.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsável pelas regras de negócio relacionadas a Pacientes.
 * Implementa cálculo de IMC, validações e verificação de duplicidade por CPF.
 */
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Regra de Negócio 2: Cadastrar Paciente com validação de duplicidade por CPF.
     * - Valida campos obrigatórios (nome, CPF, data de nascimento, sexo).
     * - Verifica se já existe paciente com o mesmo CPF.
     * - Lança DuplicidadeException se houver conflito.
     *
     * @param paciente Paciente a ser cadastrado
     * @return Paciente salvo
     * @throws DuplicidadeException se já existir paciente com mesmo CPF
     * @throws IllegalArgumentException se campos obrigatórios não forem preenchidos
     */
    public Paciente cadastrarPaciente(Paciente paciente) {
        // Validação de campos obrigatórios
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

        // Verificação de duplicidade por CPF
        if (pacienteRepository.existePorCpf(paciente.getCpf())) {
            throw new DuplicidadeException("Paciente", "CPF", paciente.getCpf());
        }

        return pacienteRepository.salvar(paciente);
    }

    /**
     * Regra de Negócio 3: Calcular o IMC (Índice de Massa Corporal) do paciente.
     * Fórmula: IMC = peso (kg) / altura (m)²
     *
     * Classificação OMS:
     * - Abaixo de 18.5: Abaixo do peso
     * - 18.5 a 24.9: Peso normal
     * - 25.0 a 29.9: Sobrepeso
     * - 30.0 a 34.9: Obesidade grau I
     * - 35.0 a 39.9: Obesidade grau II
     * - Acima de 40: Obesidade grau III
     *
     * @param paciente Paciente com peso e altura preenchidos
     * @return String formatada com o valor do IMC e a classificação
     * @throws IllegalArgumentException se peso ou altura forem inválidos
     */
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

    /**
     * Busca um paciente pelo ID.
     *
     * @param id ID do paciente
     * @return Paciente encontrado
     * @throws EntidadeNaoEncontradaException se não encontrar
     */
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Paciente", id));
    }

    /**
     * Lista todos os pacientes cadastrados.
     *
     * @return Lista de pacientes
     */
    public List<Paciente> listarTodos() {
        return pacienteRepository.listarTodos();
    }
}
