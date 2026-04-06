package br.com.fiap.hospital_crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String sexo;
    private Double peso;
    private Double altura;
    private String email;
    private String telefone;
    private String canalConhecimento;

    public Paciente(String nome, String cpf, LocalDate dataNascimento, String sexo,
                    Double peso, Double altura, String email, String telefone,
                    String canalConhecimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        this.email = email;
        this.telefone = telefone;
        this.canalConhecimento = canalConhecimento;
    }
}
