package br.com.fiap.hospital_crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa um Lead no CRM do Hospital São Rafael.
 * Um lead é um potencial paciente capturado através de canais digitais
 * (Instagram, Facebook, Google, TikTok).
 */
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

    // Construtor sem ID (para inserção)
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
