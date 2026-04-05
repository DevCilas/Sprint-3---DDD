package br.com.fiap.hospital_crm.service;

import br.com.fiap.hospital_crm.exception.ConflitoDeHorarioException;
import br.com.fiap.hospital_crm.exception.EntidadeNaoEncontradaException;
import br.com.fiap.hospital_crm.model.Agendamento;
import br.com.fiap.hospital_crm.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Service responsável pelas regras de negócio relacionadas a Agendamentos.
 * Implementa validação de conflitos de horário e controle de transição de status.
 */
@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    /** Status válidos para um agendamento. */
    private static final Set<String> STATUS_VALIDOS = Set.of(
            "agendado", "atendido", "falta", "abandono", "reagendado", "cancelado"
    );

    public AgendamentoService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    /**
     * Regra de Negócio 4: Agendar consulta com validação de conflito de horário.
     * - Valida campos obrigatórios (pacienteId, data, hora, procedimento).
     * - Verifica se já existe agendamento ativo para o mesmo paciente na mesma data/hora.
     * - Lança ConflitoDeHorarioException se houver conflito.
     *
     * @param agendamento Agendamento a ser criado
     * @return Agendamento salvo
     * @throws ConflitoDeHorarioException se já houver agendamento no mesmo horário
     * @throws IllegalArgumentException se campos obrigatórios não forem preenchidos
     */
    public Agendamento agendarConsulta(Agendamento agendamento) {
        // Validação de campos obrigatórios
        if (agendamento.getPacienteId() == null) {
            throw new IllegalArgumentException("O ID do paciente é obrigatório para agendar uma consulta.");
        }
        if (agendamento.getDataAgendamento() == null) {
            throw new IllegalArgumentException("A data do agendamento é obrigatória.");
        }
        if (agendamento.getHora() == null) {
            throw new IllegalArgumentException("O horário do agendamento é obrigatório.");
        }
        if (agendamento.getProcedimento() == null || agendamento.getProcedimento().isBlank()) {
            throw new IllegalArgumentException("O procedimento é obrigatório.");
        }

        // Verificação de conflito de horário
        if (agendamentoRepository.existeConflito(
                agendamento.getPacienteId(),
                agendamento.getDataAgendamento(),
                agendamento.getHora())) {
            throw new ConflitoDeHorarioException(
                    agendamento.getPacienteId(),
                    agendamento.getDataAgendamento().toString(),
                    agendamento.getHora().toString());
        }

        // Define status inicial como "agendado"
        if (agendamento.getStatus() == null || agendamento.getStatus().isBlank()) {
            agendamento.setStatus("agendado");
        }

        return agendamentoRepository.salvar(agendamento);
    }

    /**
     * Regra de Negócio 5: Atualizar status do agendamento com validação de transição.
     * Regras de transição:
     * - "cancelado" não pode voltar para nenhum outro status.
     * - "atendido" não pode ser alterado.
     * - O novo status deve ser um dos status válidos.
     *
     * @param id ID do agendamento
     * @param novoStatus Novo status a ser definido
     * @throws EntidadeNaoEncontradaException se agendamento não existir
     * @throws IllegalStateException se a transição de status não for permitida
     * @throws IllegalArgumentException se o status informado não for válido
     */
    public void atualizarStatus(Long id, String novoStatus) {
        // Valida se o status informado é válido
        if (!STATUS_VALIDOS.contains(novoStatus.toLowerCase())) {
            throw new IllegalArgumentException(
                    String.format("Status '%s' não é válido. Status permitidos: %s", novoStatus, STATUS_VALIDOS));
        }

        // Busca o agendamento existente
        Agendamento agendamento = agendamentoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agendamento", id));

        String statusAtual = agendamento.getStatus();

        // Regra: agendamento cancelado não pode mudar
        if ("cancelado".equalsIgnoreCase(statusAtual)) {
            throw new IllegalStateException(
                    "Não é possível alterar o status de um agendamento cancelado.");
        }

        // Regra: agendamento já atendido não pode mudar
        if ("atendido".equalsIgnoreCase(statusAtual)) {
            throw new IllegalStateException(
                    "Não é possível alterar o status de um agendamento já atendido.");
        }

        agendamentoRepository.atualizarStatus(id, novoStatus.toLowerCase());
    }

    /**
     * Busca um agendamento pelo ID.
     */
    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agendamento", id));
    }

    /**
     * Lista todos os agendamentos de um paciente.
     */
    public List<Agendamento> buscarPorPaciente(Long pacienteId) {
        return agendamentoRepository.buscarPorPacienteId(pacienteId);
    }

    /**
     * Lista todos os agendamentos.
     */
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.listarTodos();
    }
}
