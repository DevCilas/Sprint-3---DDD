package br.com.fiap.hospital_crm.repository;

import br.com.fiap.hospital_crm.model.Paciente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

/**
 * Repository (DAO) para a entidade Paciente.
 * Utiliza JdbcTemplate do Spring para operações CRUD com o banco Oracle.
 */
@Repository
public class PacienteRepository {

    private final JdbcTemplate jdbcTemplate;

    public PacienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper para converter ResultSet em objeto Paciente.
     */
    private final RowMapper<Paciente> pacienteRowMapper = (ResultSet rs, int rowNum) -> {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getLong("id"));
        paciente.setNome(rs.getString("nome"));
        paciente.setCpf(rs.getString("cpf"));
        Date dataNasc = rs.getDate("data_nascimento");
        if (dataNasc != null) {
            paciente.setDataNascimento(dataNasc.toLocalDate());
        }
        paciente.setSexo(rs.getString("sexo"));
        paciente.setPeso(rs.getDouble("peso"));
        paciente.setAltura(rs.getDouble("altura"));
        paciente.setEmail(rs.getString("email"));
        paciente.setTelefone(rs.getString("telefone"));
        paciente.setCanalConhecimento(rs.getString("canal_conhecimento"));
        return paciente;
    };

    /**
     * Insere um novo paciente no banco de dados.
     */
    public Paciente salvar(Paciente paciente) {
        String sql = """
            INSERT INTO T_PACIENTE (nome, cpf, data_nascimento, sexo, peso, altura,
                                    email, telefone, canal_conhecimento)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getDataNascimento() != null ? Date.valueOf(paciente.getDataNascimento()) : null,
                paciente.getSexo(),
                paciente.getPeso(),
                paciente.getAltura(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getCanalConhecimento());

        return paciente;
    }

    /**
     * Busca um paciente pelo ID.
     */
    public Optional<Paciente> buscarPorId(Long id) {
        String sql = "SELECT * FROM T_PACIENTE WHERE id = ?";
        List<Paciente> resultados = jdbcTemplate.query(sql, pacienteRowMapper, id);
        return resultados.stream().findFirst();
    }

    /**
     * Lista todos os pacientes cadastrados.
     */
    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM T_PACIENTE ORDER BY nome";
        return jdbcTemplate.query(sql, pacienteRowMapper);
    }

    /**
     * Verifica se já existe um paciente com o CPF informado.
     */
    public boolean existePorCpf(String cpf) {
        String sql = "SELECT COUNT(*) FROM T_PACIENTE WHERE cpf = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cpf);
        return count != null && count > 0;
    }

    /**
     * Atualiza os dados de um paciente existente.
     */
    public void atualizar(Paciente paciente) {
        String sql = """
            UPDATE T_PACIENTE SET nome = ?, cpf = ?, data_nascimento = ?, sexo = ?,
                                  peso = ?, altura = ?, email = ?, telefone = ?,
                                  canal_conhecimento = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getDataNascimento() != null ? Date.valueOf(paciente.getDataNascimento()) : null,
                paciente.getSexo(),
                paciente.getPeso(),
                paciente.getAltura(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getCanalConhecimento(),
                paciente.getId());
    }

    /**
     * Remove um paciente pelo ID.
     */
    public void deletar(Long id) {
        String sql = "DELETE FROM T_PACIENTE WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
