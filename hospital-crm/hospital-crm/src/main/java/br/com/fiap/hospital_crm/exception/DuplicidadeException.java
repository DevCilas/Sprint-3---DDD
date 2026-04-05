package br.com.fiap.hospital_crm.exception;

/**
 * Exceção lançada quando se tenta cadastrar uma entidade que já existe no sistema.
 * Aplicável para Leads (duplicidade por e-mail/telefone) e Pacientes (duplicidade por CPF).
 */
public class DuplicidadeException extends RuntimeException {

    public DuplicidadeException(String mensagem) {
        super(mensagem);
    }

    public DuplicidadeException(String entidade, String campo, String valor) {
        super(String.format("Duplicidade detectada: %s com %s '%s' já existe no sistema.", entidade, campo, valor));
    }
}
