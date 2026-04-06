package br.com.fiap.hospital_crm;

import br.com.fiap.hospital_crm.exception.ConflitoDeHorarioException;
import br.com.fiap.hospital_crm.exception.DuplicidadeException;
import br.com.fiap.hospital_crm.model.Agendamento;
import br.com.fiap.hospital_crm.model.Lead;
import br.com.fiap.hospital_crm.model.Paciente;
import br.com.fiap.hospital_crm.service.AgendamentoService;
import br.com.fiap.hospital_crm.service.LeadService;
import br.com.fiap.hospital_crm.service.PacienteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class HospitalCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalCrmApplication.class, args);
	}

	// zona de testes - roda quando a aplicacao inicia
	@Bean
	CommandLineRunner zonaDeTestes(LeadService leadService,
								  PacienteService pacienteService,
								  AgendamentoService agendamentoService) {
		return args -> {

			System.out.println("====================================");
			System.out.println("  CRM Hospital São Rafael - Testes");
			System.out.println("====================================\n");

			// teste 1 - cadastrar lead
			System.out.println(">> Teste 1: Cadastro de Lead");
			try {
				Lead lead1 = new Lead(
						"Maria Silva",
						"(11) 99999-1111",
						"maria.silva@email.com",
						"Instagram",
						"Rinoplastia",
						"Dr. Carlos Mendes"
				);
				leadService.cadastrarLead(lead1);
				System.out.println("Lead cadastrado: " + lead1.getNome());
			} catch (Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}

			// teste 2 - lead duplicado por email
			System.out.println("\n>> Teste 2: Lead duplicado (email)");
			try {
				Lead leadDup = new Lead(
						"Maria Aparecida",
						"(11) 88888-2222",
						"maria.silva@email.com",
						"Facebook",
						"Lipoaspiração",
						null
				);
				leadService.cadastrarLead(leadDup);
				System.out.println("Erro - deveria ter dado duplicidade");
			} catch (DuplicidadeException e) {
				System.out.println("OK - " + e.getMessage());
			}

			// teste 3 - lead duplicado por telefone
			System.out.println("\n>> Teste 3: Lead duplicado (telefone)");
			try {
				Lead leadDup2 = new Lead(
						"José Santos",
						"(11) 99999-1111",
						"jose@email.com",
						"Google",
						"Blefaroplastia",
						null
				);
				leadService.cadastrarLead(leadDup2);
				System.out.println("Erro - deveria ter dado duplicidade");
			} catch (DuplicidadeException e) {
				System.out.println("OK - " + e.getMessage());
			}

			// teste 4 - cadastrar paciente e calcular imc
			System.out.println("\n>> Teste 4: Cadastro de Paciente + IMC");
			try {
				Paciente pac1 = new Paciente(
						"João Pedro Oliveira",
						"123.456.789-00",
						LocalDate.of(1985, 3, 15),
						"Masculino",
						82.5,
						1.75,
						"joao.pedro@email.com",
						"(11) 97777-3333",
						"Instagram"
				);
				pacienteService.cadastrarPaciente(pac1);
				System.out.println("Paciente cadastrado: " + pac1.getNome());
				System.out.println(pacienteService.calcularIMC(pac1));
			} catch (Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}

			// teste 5 - paciente com cpf duplicado
			System.out.println("\n>> Teste 5: Paciente duplicado (CPF)");
			try {
				Paciente pacDup = new Paciente(
						"João P. Oliveira",
						"123.456.789-00",
						LocalDate.of(1985, 3, 15),
						"Masculino",
						80.0,
						1.75,
						"joao.dup@email.com",
						"(11) 96666-4444",
						"Google"
				);
				pacienteService.cadastrarPaciente(pacDup);
				System.out.println("Erro - deveria ter dado duplicidade");
			} catch (DuplicidadeException e) {
				System.out.println("OK - " + e.getMessage());
			}

			// teste 6 - imc com valores diferentes
			System.out.println("\n>> Teste 6: IMC - classificacoes");
			Paciente magro = new Paciente();
			magro.setPeso(50.0);
			magro.setAltura(1.80);
			System.out.println("Magro: " + pacienteService.calcularIMC(magro));

			Paciente obeso = new Paciente();
			obeso.setPeso(110.0);
			obeso.setAltura(1.65);
			System.out.println("Obeso: " + pacienteService.calcularIMC(obeso));

			// teste 7 - agendar consulta
			System.out.println("\n>> Teste 7: Agendamento");
			try {
				Agendamento ag1 = new Agendamento(
						1L,
						LocalDate.of(2026, 4, 10),
						LocalTime.of(14, 30),
						"Rinoplastia - Consulta Inicial",
						"agendado"
				);
				agendamentoService.agendarConsulta(ag1);
				System.out.println("Consulta agendada: " + ag1.getDataAgendamento() + " " + ag1.getHora());
			} catch (Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}

			// teste 8 - conflito de horario
			System.out.println("\n>> Teste 8: Conflito de horario");
			try {
				Agendamento ag2 = new Agendamento(
						1L,
						LocalDate.of(2026, 4, 10),
						LocalTime.of(14, 30),
						"Blefaroplastia",
						"agendado"
				);
				agendamentoService.agendarConsulta(ag2);
				System.out.println("Erro - deveria ter dado conflito");
			} catch (ConflitoDeHorarioException e) {
				System.out.println("OK - " + e.getMessage());
			}

			// teste 9 - mudar status pra atendido
			System.out.println("\n>> Teste 9: Mudar status");
			try {
				agendamentoService.atualizarStatus(1L, "atendido");
				System.out.println("Status atualizado pra atendido");
			} catch (Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}

			// teste 10 - tentar mudar status de atendido (nao pode)
			System.out.println("\n>> Teste 10: Tentar mudar status de atendido");
			try {
				agendamentoService.atualizarStatus(1L, "cancelado");
				System.out.println("Erro - nao deveria deixar");
			} catch (IllegalStateException e) {
				System.out.println("OK - " + e.getMessage());
			}

			System.out.println("\n====================================");
			System.out.println("  Testes finalizados!");
			System.out.println("====================================");
		};
	}
}
