package br.com.fiap.hospital_crm.repository;

import br.com.fiap.hospital_crm.model.Paciente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public class PacienteRepository {

    private final JdbcTemplate jdbcTemplate;

    public PacienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Paciente> pacienteRowMapper = (ResultSet rs, int rowNum) -> {
        Paciente p = new Paciente();
        p.setId(rs.getLong("id"));
        p.setNome(rs.getString("nome"));
        p.setCpf(rs.getString("cpf"));
        Date dataNasc = rs.getDate("data_nascimento");
        if (dataNasc != null) {
            p.setDataNascimento(dataNasc.toLocalDate());
        }
        p.setSexo(rs.getString("sexo"));
        p.setPeso(rs.getDouble("peso"));
        p.setAltura(rs.getDouble("altura"));
        p.setEmail(rs.getString("email"));
        p.setTelefone(rs.getString("telefone"));
        p.setCanalConhecimento(rs.getString("canal_conhecimento"));
        return p;
    };

    public Paciente salvar(Paciente paciente) {
        String sql = "INSERT INTO T_PACIENTE (nome, cpf, data_nascimento, sexo, peso, altura, email, telefone, canal_conhecimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

    public Optional<Paciente> buscarPorId(Long id) {
        String sql = "SELECT * FROM T_PACIENTE WHERE id = ?";
        List<Paciente> resultados = jdbcTemplate.query(sql, pacienteRowMapper, id);
        return resultados.stream().findFirst();
    }

    public List<Paciente> listarTodos() {
        return jdbcTemplate.query("SELECT * FROM T_PACIENTE ORDER BY nome", pacienteRowMapper);
    }

    public boolean existePorCpf(String cpf) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM T_PACIENTE WHERE cpf = ?", Integer.class, cpf);
        return count != null && count > 0;
    }

    public void atualizar(Paciente paciente) {
        String sql = "UPDATE T_PACIENTE SET nome = ?, cpf = ?, data_nascimento = ?, sexo = ?, peso = ?, altura = ?, email = ?, telefone = ?, canal_conhecimento = ? WHERE id = ?";
        jdbcTemplate.update(sql, paciente.getNome(), paciente.getCpf(),
                paciente.getDataNascimento() != null ? Date.valueOf(paciente.getDataNascimento()) : null,
                paciente.getSexo(), paciente.getPeso(), paciente.getAltura(),
                paciente.getEmail(), paciente.getTelefone(),
                paciente.getCanalConhecimento(), paciente.getId());
    }

    public void deletar(Long id) {
        jdbcTemplate.update("DELETE FROM T_PACIENTE WHERE id = ?", id);
    }
}
