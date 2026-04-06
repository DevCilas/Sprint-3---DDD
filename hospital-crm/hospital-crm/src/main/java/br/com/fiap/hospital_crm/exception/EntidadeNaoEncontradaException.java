package br.com.fiap.hospital_crm.exception;

// exception pra quando nao encontrar no banco
public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(String.format("%s com ID %d não foi encontrado(a) no sistema.", entidade, id));
    }
}
