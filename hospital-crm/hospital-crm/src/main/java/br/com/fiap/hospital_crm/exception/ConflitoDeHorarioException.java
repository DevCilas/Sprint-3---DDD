package br.com.fiap.hospital_crm.exception;

/**
 * Exceção lançada quando se tenta agendar uma consulta em um horário
 * que já está ocupado por outro agendamento do mesmo paciente.
 */
public class ConflitoDeHorarioException extends RuntimeException {

    public ConflitoDeHorarioException(String mensagem) {
        super(mensagem);
    }

    public ConflitoDeHorarioException(Long pacienteId, String data, String hora) {
        super(String.format("Conflito de horário: paciente ID %d já possui agendamento em %s às %s.",
                pacienteId, data, hora));
    }
}
