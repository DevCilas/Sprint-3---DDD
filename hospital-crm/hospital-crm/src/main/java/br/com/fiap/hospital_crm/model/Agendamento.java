package br.com.fiap.hospital_crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    private Long id;
    private Long pacienteId;
    private LocalDate dataAgendamento;
    private LocalTime hora;
    private String procedimento;
    private String status; // agendado, atendido, falta, abandono, reagendado, cancelado

    public Agendamento(Long pacienteId, LocalDate dataAgendamento, LocalTime hora,
                       String procedimento, String status) {
        this.pacienteId = pacienteId;
        this.dataAgendamento = dataAgendamento;
        this.hora = hora;
        this.procedimento = procedimento;
        this.status = status;
    }
}
