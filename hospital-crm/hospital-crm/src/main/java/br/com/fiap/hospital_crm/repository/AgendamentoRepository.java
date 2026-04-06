package br.com.fiap.hospital_crm.repository;

import br.com.fiap.hospital_crm.model.Agendamento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public class AgendamentoRepository {

    private final JdbcTemplate jdbcTemplate;

    public AgendamentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Agendamento> agendamentoRowMapper = (ResultSet rs, int rowNum) -> {
        Agendamento ag = new Agendamento();
        ag.setId(rs.getLong("id"));
        ag.setPacienteId(rs.getLong("paciente_id"));
        Date dataAg = rs.getDate("data_agendamento");
        if (dataAg != null) {
            ag.setDataAgendamento(dataAg.toLocalDate());
        }
        Time hora = rs.getTime("hora");
        if (hora != null) {
            ag.setHora(hora.toLocalTime());
        }
        ag.setProcedimento(rs.getString("procedimento"));
        ag.setStatus(rs.getString("status"));
        return ag;
    };

    public Agendamento salvar(Agendamento agendamento) {
        String sql = "INSERT INTO T_AGENDAMENTO (paciente_id, data_agendamento, hora, procedimento, status) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                agendamento.getPacienteId(),
                agendamento.getDataAgendamento() != null ? Date.valueOf(agendamento.getDataAgendamento()) : null,
                agendamento.getHora() != null ? Time.valueOf(agendamento.getHora()) : null,
                agendamento.getProcedimento(),
                agendamento.getStatus());

        return agendamento;
    }

    public Optional<Agendamento> buscarPorId(Long id) {
        List<Agendamento> resultados = jdbcTemplate.query("SELECT * FROM T_AGENDAMENTO WHERE id = ?", agendamentoRowMapper, id);
        return resultados.stream().findFirst();
    }

    public List<Agendamento> listarTodos() {
        return jdbcTemplate.query("SELECT * FROM T_AGENDAMENTO ORDER BY data_agendamento, hora", agendamentoRowMapper);
    }

    // verifica se ja tem agendamento no mesmo horario pro mesmo paciente
    public boolean existeConflito(Long pacienteId, java.time.LocalDate data, java.time.LocalTime hora) {
        String sql = "SELECT COUNT(*) FROM T_AGENDAMENTO WHERE paciente_id = ? AND data_agendamento = ? AND hora = ? AND status != 'cancelado'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, pacienteId, Date.valueOf(data), Time.valueOf(hora));
        return count != null && count > 0;
    }

    public void atualizarStatus(Long id, String novoStatus) {
        jdbcTemplate.update("UPDATE T_AGENDAMENTO SET status = ? WHERE id = ?", novoStatus, id);
    }

    public List<Agendamento> buscarPorPacienteId(Long pacienteId) {
        return jdbcTemplate.query("SELECT * FROM T_AGENDAMENTO WHERE paciente_id = ? ORDER BY data_agendamento, hora",
                agendamentoRowMapper, pacienteId);
    }

    public void deletar(Long id) {
        jdbcTemplate.update("DELETE FROM T_AGENDAMENTO WHERE id = ?", id);
    }
}
