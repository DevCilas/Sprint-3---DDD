package br.com.fiap.hospital_crm.repository;

import br.com.fiap.hospital_crm.model.Lead;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository (DAO) para a entidade Lead.
 * Utiliza JdbcTemplate do Spring para operações CRUD com o banco Oracle.
 */
@Repository
public class LeadRepository {

    private final JdbcTemplate jdbcTemplate;

    public LeadRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper para converter ResultSet em objeto Lead.
     */
    private final RowMapper<Lead> leadRowMapper = (ResultSet rs, int rowNum) -> {
        Lead lead = new Lead();
        lead.setId(rs.getLong("id"));
        lead.setNome(rs.getString("nome"));
        lead.setTelefone(rs.getString("telefone"));
        lead.setEmail(rs.getString("email"));
        lead.setCanalEntrada(rs.getString("canal_entrada"));
        lead.setProcedimentoInteresse(rs.getString("procedimento_interesse"));
        lead.setPreferenciaMedico(rs.getString("preferencia_medico"));
        Timestamp ts = rs.getTimestamp("data_cadastro");
        if (ts != null) {
            lead.setDataCadastro(ts.toLocalDateTime());
        }
        return lead;
    };

    /**
     * Insere um novo lead no banco de dados.
     */
    public Lead salvar(Lead lead) {
        String sql = """
            INSERT INTO T_LEAD (nome, telefone, email, canal_entrada, 
                               procedimento_interesse, preferencia_medico, data_cadastro)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        if (lead.getDataCadastro() == null) {
            lead.setDataCadastro(LocalDateTime.now());
        }

        jdbcTemplate.update(sql,
                lead.getNome(),
                lead.getTelefone(),
                lead.getEmail(),
                lead.getCanalEntrada(),
                lead.getProcedimentoInteresse(),
                lead.getPreferenciaMedico(),
                Timestamp.valueOf(lead.getDataCadastro()));

        return lead;
    }

    /**
     * Busca um lead pelo ID.
     */
    public Optional<Lead> buscarPorId(Long id) {
        String sql = "SELECT * FROM T_LEAD WHERE id = ?";
        List<Lead> resultados = jdbcTemplate.query(sql, leadRowMapper, id);
        return resultados.stream().findFirst();
    }

    /**
     * Lista todos os leads cadastrados.
     */
    public List<Lead> listarTodos() {
        String sql = "SELECT * FROM T_LEAD ORDER BY data_cadastro DESC";
        return jdbcTemplate.query(sql, leadRowMapper);
    }

    /**
     * Verifica se já existe um lead com o e-mail informado.
     */
    public boolean existePorEmail(String email) {
        String sql = "SELECT COUNT(*) FROM T_LEAD WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    /**
     * Verifica se já existe um lead com o telefone informado.
     */
    public boolean existePorTelefone(String telefone) {
        String sql = "SELECT COUNT(*) FROM T_LEAD WHERE telefone = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, telefone);
        return count != null && count > 0;
    }

    /**
     * Atualiza os dados de um lead existente.
     */
    public void atualizar(Lead lead) {
        String sql = """
            UPDATE T_LEAD SET nome = ?, telefone = ?, email = ?, canal_entrada = ?,
                             procedimento_interesse = ?, preferencia_medico = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                lead.getNome(),
                lead.getTelefone(),
                lead.getEmail(),
                lead.getCanalEntrada(),
                lead.getProcedimentoInteresse(),
                lead.getPreferenciaMedico(),
                lead.getId());
    }

    /**
     * Remove um lead pelo ID.
     */
    public void deletar(Long id) {
        String sql = "DELETE FROM T_LEAD WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
