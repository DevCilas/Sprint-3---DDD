package br.com.fiap.hospital_crm.exception;

// exception pra quando tentar cadastrar algo que ja existe
public class DuplicidadeException extends RuntimeException {

    public DuplicidadeException(String mensagem) {
        super(mensagem);
    }

    public DuplicidadeException(String entidade, String campo, String valor) {
        super(String.format("Duplicidade detectada: %s com %s '%s' já existe no sistema.", entidade, campo, valor));
    }
}
