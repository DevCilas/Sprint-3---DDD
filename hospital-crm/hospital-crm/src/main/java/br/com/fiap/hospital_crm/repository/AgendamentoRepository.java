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

/**
 * Repository para a entidade Agendamento.
 * Utiliza JdbcTemplate do Spring para operações CRUD com o banco Oracle.
 */
@Repository
public class AgendamentoRepository {

    private final JdbcTemplate jdbcTemplate;

    public AgendamentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper para converter ResultSet em objeto Agendamento.
     */
    private final RowMapper<Agendamento> agendamentoRowMapper = (ResultSet rs, int rowNum) -> {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(rs.getLong("id"));
        agendamento.setPacienteId(rs.getLong("paciente_id"));
        Date dataAg = rs.getDate("data_agendamento");
        if (dataAg != null) {
            agendamento.setDataAgendamento(dataAg.toLocalDate());
        }
        Time hora = rs.getTime("hora");
        if (hora != null) {
            agendamento.setHora(hora.toLocalTime());
        }
        agendamento.setProcedimento(rs.getString("procedimento"));
        agendamento.setStatus(rs.getString("status"));
        return agendamento;
    };

    /**
     * Insere um novo agendamento no banco de dados.
     */
    public Agendamento salvar(Agendamento agendamento) {
        String sql = """
            INSERT INTO T_AGENDAMENTO (paciente_id, data_agendamento, hora, procedimento, status)
            VALUES (?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
                agendamento.getPacienteId(),
                agendamento.getDataAgendamento() != null ? Date.valueOf(agendamento.getDataAgendamento()) : null,
                agendamento.getHora() != null ? Time.valueOf(agendamento.getHora()) : null,
                agendamento.getProcedimento(),
                agendamento.getStatus());

        return agendamento;
    }

    /**
     * Busca um agendamento pelo ID.
     */
    public Optional<Agendamento> buscarPorId(Long id) {
        String sql = "SELECT * FROM T_AGENDAMENTO WHERE id = ?";
        List<Agendamento> resultados = jdbcTemplate.query(sql, agendamentoRowMapper, id);
        return resultados.stream().findFirst();
    }

    /**
     * Lista todos os agendamentos cadastrados.
     */
    public List<Agendamento> listarTodos() {
        String sql = "SELECT * FROM T_AGENDAMENTO ORDER BY data_agendamento, hora";
        return jdbcTemplate.query(sql, agendamentoRowMapper);
    }

    /**
     * Verifica se já existe um agendamento para o mesmo paciente na mesma data e hora.
     */
    public boolean existeConflito(Long pacienteId, java.time.LocalDate data, java.time.LocalTime hora) {
        String sql = "SELECT COUNT(*) FROM T_AGENDAMENTO WHERE paciente_id = ? AND data_agendamento = ? AND hora = ? AND status != 'cancelado'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                pacienteId, Date.valueOf(data), Time.valueOf(hora));
        return count != null && count > 0;
    }

    /**
     * Atualiza o status de um agendamento.
     */
    public void atualizarStatus(Long id, String novoStatus) {
        String sql = "UPDATE T_AGENDAMENTO SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, novoStatus, id);
    }

    /**
     * Lista agendamentos de um paciente específico.
     */
    public List<Agendamento> buscarPorPacienteId(Long pacienteId) {
        String sql = "SELECT * FROM T_AGENDAMENTO WHERE paciente_id = ? ORDER BY data_agendamento, hora";
        return jdbcTemplate.query(sql, agendamentoRowMapper, pacienteId);
    }

    /**
     * Remove um agendamento pelo ID.
     */
    public void deletar(Long id) {
        String sql = "DELETE FROM T_AGENDAMENTO WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
