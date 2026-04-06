package br.com.fiap.hospital_crm.exception;

// exception de conflito de horario no agendamento
public class ConflitoDeHorarioException extends RuntimeException {

    public ConflitoDeHorarioException(String mensagem) {
        super(mensagem);
    }

    public ConflitoDeHorarioException(Long pacienteId, String data, String hora) {
        super(String.format("Conflito de horário: paciente ID %d já possui agendamento em %s às %s.",
                pacienteId, data, hora));
    }
}
