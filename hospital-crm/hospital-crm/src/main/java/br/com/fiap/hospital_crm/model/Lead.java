package br.com.fiap.hospital_crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lead {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String canalEntrada;
    private String procedimentoInteresse;
    private String preferenciaMedico;
    private LocalDateTime dataCadastro;

    public Lead(String nome, String telefone, String email, String canalEntrada,
                String procedimentoInteresse, String preferenciaMedico) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.canalEntrada = canalEntrada;
        this.procedimentoInteresse = procedimentoInteresse;
        this.preferenciaMedico = preferenciaMedico;
        this.dataCadastro = LocalDateTime.now();
    }
}
