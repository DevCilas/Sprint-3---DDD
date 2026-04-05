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

/**
 * Classe principal do CRM Hospital São Rafael.
 * Contém a Zona de Testes (CommandLineRunner) com testes hardcoded
 * para demonstrar as regras de negócio implementadas.
 */
@SpringBootApplication
public class HospitalCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalCrmApplication.class, args);
	}

	/**
	 TESTES
	 */
	@Bean
	CommandLineRunner zonaDeTestes(LeadService leadService,
								  PacienteService pacienteService,
								  AgendamentoService agendamentoService) {
		return args -> {

			System.out.println("=============================================================");
			System.out.println("  CRM HOSPITAL SÃO RAFAEL - ZONA DE TESTES (Sprint 3)");
			System.out.println("=============================================================\n");


			// TESTE 1: Cadastro de Lead com sucesso
			System.out.println("--- TESTE 1: Cadastro de Lead ---");
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
				System.out.println("[OK] Lead cadastrado com sucesso: " + lead1.getNome());
				System.out.println("     Canal: " + lead1.getCanalEntrada());
				System.out.println("     Procedimento: " + lead1.getProcedimentoInteresse());
			} catch (Exception e) {
				System.out.println("[ERRO] " + e.getMessage());
			}

			// TESTE 2: Tentativa de Lead duplicado (email)
			System.out.println("\n--- TESTE 2: Lead Duplicado (mesmo e-mail) ---");
			try {
				Lead leadDuplicado = new Lead(
						"Maria Aparecida",
						"(11) 88888-2222",
						"maria.silva@email.com",  // mesmo e-mail do teste 1
						"Facebook",
						"Lipoaspiração",
						null
				);
				leadService.cadastrarLead(leadDuplicado);
				System.out.println("[FALHA] Deveria ter lançado exceção de duplicidade!");
			} catch (DuplicidadeException e) {
				System.out.println("[OK] Duplicidade detectada corretamente: " + e.getMessage());
			}

			// TESTE 3: Tentativa de Lead duplicado (telefone)
			System.out.println("\n--- TESTE 3: Lead Duplicado (mesmo telefone) ---");
			try {
				Lead leadDuplicadoTel = new Lead(
						"José Santos",
						"(11) 99999-1111",  // mesmo telefone do teste 1
						"jose@email.com",
						"Google",
						"Blefaroplastia",
						null
				);
				leadService.cadastrarLead(leadDuplicadoTel);
				System.out.println("[FALHA] Deveria ter lançado exceção de duplicidade!");
			} catch (DuplicidadeException e) {
				System.out.println("[OK] Duplicidade detectada corretamente: " + e.getMessage());
			}

			// TESTE 4: Cadastro de Paciente com sucesso + Cálculo de IMC
			System.out.println("\n--- TESTE 4: Cadastro de Paciente + Cálculo de IMC ---");
			try {
				Paciente paciente1 = new Paciente(
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
				pacienteService.cadastrarPaciente(paciente1);
				System.out.println("[OK] Paciente cadastrado com sucesso: " + paciente1.getNome());

				// Cálculo do IMC
				String resultadoIMC = pacienteService.calcularIMC(paciente1);
				System.out.println("[OK] " + resultadoIMC);
			} catch (Exception e) {
				System.out.println("[ERRO] " + e.getMessage());
			}

			// TESTE 5: Tentativa de Paciente duplicado (CPF)
			System.out.println("\n--- TESTE 5: Paciente Duplicado (mesmo CPF) ---");
			try {
				Paciente pacienteDuplicado = new Paciente(
						"João P. Oliveira",
						"123.456.789-00",  // mesmo CPF do teste 4
						LocalDate.of(1985, 3, 15),
						"Masculino",
						80.0,
						1.75,
						"joao.duplicado@email.com",
						"(11) 96666-4444",
						"Google"
				);
				pacienteService.cadastrarPaciente(pacienteDuplicado);
				System.out.println("[FALHA] Deveria ter lançado exceção de duplicidade!");
			} catch (DuplicidadeException e) {
				System.out.println("[OK] Duplicidade detectada corretamente: " + e.getMessage());
			}

			// TESTE 6: Cálculo de IMC com diferentes classificações
			System.out.println("\n--- TESTE 6: Diferentes Classificações de IMC ---");
			Paciente pacienteMagro = new Paciente();
			pacienteMagro.setPeso(50.0);
			pacienteMagro.setAltura(1.80);
			System.out.println("  Paciente magro -> " + pacienteService.calcularIMC(pacienteMagro));

			Paciente pacienteObeso = new Paciente();
			pacienteObeso.setPeso(110.0);
			pacienteObeso.setAltura(1.65);
			System.out.println("  Paciente obeso -> " + pacienteService.calcularIMC(pacienteObeso));


			// TESTE 7: Agendamento de Consulta com sucesso

			System.out.println("\n--- TESTE 7: Agendamento de Consulta ---");
			try {
				Agendamento agendamento1 = new Agendamento(
						1L,  // paciente_id (usar o ID do paciente cadastrado)
						LocalDate.of(2026, 4, 10),
						LocalTime.of(14, 30),
						"Rinoplastia - Consulta Inicial",
						"agendado"
				);
				agendamentoService.agendarConsulta(agendamento1);
				System.out.println("[OK] Consulta agendada com sucesso!");
				System.out.println("     Data: " + agendamento1.getDataAgendamento());
				System.out.println("     Hora: " + agendamento1.getHora());
				System.out.println("     Procedimento: " + agendamento1.getProcedimento());
				System.out.println("     Status: " + agendamento1.getStatus());
			} catch (Exception e) {
				System.out.println("[ERRO] " + e.getMessage());
			}


			// TESTE 8: Conflito de horário no agendamento

			System.out.println("\n--- TESTE 8: Conflito de Horário ---");
			try {
				Agendamento agendamentoDuplicado = new Agendamento(
						1L,
						LocalDate.of(2026, 4, 10),  // mesma data
						LocalTime.of(14, 30),        // mesmo horário
						"Blefaroplastia - Consulta",
						"agendado"
				);
				agendamentoService.agendarConsulta(agendamentoDuplicado);
				System.out.println("[FALHA] Deveria ter lançado exceção de conflito!");
			} catch (ConflitoDeHorarioException e) {
				System.out.println("[OK] Conflito detectado corretamente: " + e.getMessage());
			}


			// TESTE 9: Atualização de status do agendamento

			System.out.println("\n--- TESTE 9: Atualização de Status ---");
			try {
				agendamentoService.atualizarStatus(1L, "atendido");
				System.out.println("[OK] Status atualizado para 'atendido'.");
			} catch (Exception e) {
				System.out.println("[ERRO] " + e.getMessage());
			}

			// TESTE 10: Tentativa de alterar status de agendamento atendido
			System.out.println("\n--- TESTE 10: Status Imutável (atendido) ---");
			try {
				agendamentoService.atualizarStatus(1L, "cancelado");
				System.out.println("[FALHA] Deveria ter bloqueado a alteração!");
			} catch (IllegalStateException e) {
				System.out.println("[OK] Transição bloqueada corretamente: " + e.getMessage());
			}

			// RESUMO
			System.out.println("\n=============================================================");
			System.out.println("  ZONA DE TESTES FINALIZADA");
			System.out.println("  Regras de negócio validadas:");
			System.out.println("  1. Cadastro de Lead com verificação de duplicidade");
			System.out.println("  2. Cadastro de Paciente com bloqueio por CPF");
			System.out.println("  3. Cálculo de IMC com classificação OMS");
			System.out.println("  4. Agendamento com validação de conflito de horário");
			System.out.println("  5. Controle de transição de status do agendamento");
			System.out.println("=============================================================");
		};
	}
}
