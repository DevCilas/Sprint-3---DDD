package br.com.fiap.hospital_crm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller simples para exibir informações do projeto na raiz da aplicação.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return """
                <html>
                <head><title>CRM Hospital São Rafael</title></head>
                <body style="font-family: Arial, sans-serif; max-width: 700px; margin: 40px auto; padding: 20px;">
                    <h1>CRM Hospital São Rafael</h1>
                    <p>Sprint 3 - Domain Driven Design (Java/Spring/JDBC)</p>
                    <hr>
                    <h3>Status: Aplicação rodando com sucesso!</h3>
                    <p>Os testes das regras de negócio foram executados automaticamente no console.</p>
                    <h3>Regras de Negócio Implementadas:</h3>
                    <ol>
                        <li>Cadastro de Lead com verificação de duplicidade (e-mail e telefone)</li>
                        <li>Cadastro de Paciente com bloqueio de duplicidade por CPF</li>
                        <li>Cálculo de IMC com classificação OMS</li>
                        <li>Agendamento com validação de conflito de horário</li>
                        <li>Controle de transição de status do agendamento</li>
                    </ol>
                    <h3>Entidades Mapeadas:</h3>
                    <ul>
                        <li>T_LEAD - Leads capturados via canais digitais</li>
                        <li>T_PACIENTE - Pacientes cadastrados no hospital</li>
                        <li>T_AGENDAMENTO - Agendamentos de consultas</li>
                    </ul>
                </body>
                </html>
                """;
    }
}
