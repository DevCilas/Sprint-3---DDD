package br.com.fiap.hospital_crm.service;

import br.com.fiap.hospital_crm.exception.ConflitoDeHorarioException;
import br.com.fiap.hospital_crm.exception.EntidadeNaoEncontradaException;
import br.com.fiap.hospital_crm.model.Agendamento;
import br.com.fiap.hospital_crm.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    private static final Set<String> STATUS_VALIDOS = Set.of(
            "agendado", "atendido", "falta", "abandono", "reagendado", "cancelado"
    );

    public AgendamentoService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    // agenda consulta e verifica se nao tem conflito de horario
    public Agendamento agendarConsulta(Agendamento agendamento) {
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

        // ve se ja tem algo marcado nesse horario
        if (agendamentoRepository.existeConflito(
                agendamento.getPacienteId(),
                agendamento.getDataAgendamento(),
                agendamento.getHora())) {
            throw new ConflitoDeHorarioException(
                    agendamento.getPacienteId(),
                    agendamento.getDataAgendamento().toString(),
                    agendamento.getHora().toString());
        }

        if (agendamento.getStatus() == null || agendamento.getStatus().isBlank()) {
            agendamento.setStatus("agendado");
        }

        return agendamentoRepository.salvar(agendamento);
    }

    // atualiza status mas nao deixa mudar se ja foi atendido ou cancelado
    public void atualizarStatus(Long id, String novoStatus) {
        if (!STATUS_VALIDOS.contains(novoStatus.toLowerCase())) {
            throw new IllegalArgumentException(
                    String.format("Status '%s' não é válido. Status permitidos: %s", novoStatus, STATUS_VALIDOS));
        }

        Agendamento agendamento = agendamentoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agendamento", id));

        String statusAtual = agendamento.getStatus();

        if ("cancelado".equalsIgnoreCase(statusAtual)) {
            throw new IllegalStateException("Não é possível alterar o status de um agendamento cancelado.");
        }
        if ("atendido".equalsIgnoreCase(statusAtual)) {
            throw new IllegalStateException("Não é possível alterar o status de um agendamento já atendido.");
        }

        agendamentoRepository.atualizarStatus(id, novoStatus.toLowerCase());
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agendamento", id));
    }

    public List<Agendamento> buscarPorPaciente(Long pacienteId) {
        return agendamentoRepository.buscarPorPacienteId(pacienteId);
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.listarTodos();
    }
}
