package br.com.fiap.hospital_crm.exception;

/**
 * Exceção lançada quando uma entidade não é encontrada no banco de dados.
 * Aplicável para buscas por ID de Lead, Paciente ou Agendamento.
 */
public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(String.format("%s com ID %d não foi encontrado(a) no sistema.", entidade, id));
    }
}
