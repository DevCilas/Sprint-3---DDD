# CRM Hospital São Rafael - Sprint 3

## 👥 Grupo 6
- Cilas Pinto Macedo - RM 560745
- Ian Junji Maluvayshi Matsushita - RM 560588
- Pedro Arão Baquini - RM 559580
- Leandro Kamada Pesce Dimov - RM 560381

## 📄 Sobre o Projeto
Este projeto faz parte da Challenge FIAP do Hospital São Rafael e é referente à entrega da Sprint 3 da disciplina **Domain Driven Design – Java/Spring/JDBC**.

A proposta foi construir a estrutura de entrada de dados (API/Scanner) para auxiliar o novo sistema de CRM de Vendas da instituição focado na atração, cadastro e agendamento de consultas médicas em um ambiente organizado conforme os épicos planejados com a empresa parceira.

## ⚙️ Tecnologias Utilizadas
- Java 17
- Spring Boot
- Banco de Dados Oracle FIAP Cloud
- Spring Data JDBC (Utilizando `JdbcTemplate`)

## 🛠️ Regras de Negócios e Funcionalidades Implementadas
Implementamos as lógicas cobrindo desde a atração (Lead) até a reserva horária do procedimento.

**1. Gestão de Leads**
- **Cadastro e Restrição de duplicidade:** Um endpoint de cadastro que mapeia campos vitais. Antes de gravar no banco de dados da nuvem, a regra verifica na tabela se o e-mail ou telefone já estão em uso, prevenindo duplicidade de informações.

**2. Cadastros Gerais (Paciente)**
- **Verificação por CPF:** No momento em que um paciente entra definitivamente no sistema do Hospital o check contra CPF duplicados entra em vigor.
- **Biometria (Cálculo de IMC):** Implementamos a classificação automática do quadro de saúde do paciente, pegando o Peso e a Altura da requisição e gerando um cálculo conforme métricas da OMS que salva o status no console.

**3. Agendamento Médico**
- **Conflito de Horário:** A regra mais complexa, focada em impedir agendamentos duplos para pacientes em uma mesma data e horário.
- **Progresso de Status:** Garantimos as validações de transição do fluxo, bloqueando edições ou realocações de pacientes já com a situação definida em "Atendido" ou "Cancelado".

## 🚀 Instruções para Rodar a Aplicação

O projeto está configurado para iniciar a criação automática das tabelas e rodar uma bateria de testes diretamente no terminal utilizando componentes Spring (`CommandLineRunner`). É necessário ter o Java 17 instalado.

### Passo a passo para execução:

1. Realize o **git clone** deste repositório na sua máquina;
2. Pelo terminal ou prompt de comando, navegue até a pasta interna do projeto:
```bash
cd hospital-crm/hospital-crm
```
3. Abra esta pasta na sua IDE (*IntelliJ, Eclipse, VSCode*);
4. **(Importante):** Vá até o arquivo `src/main/resources/application.properties` e atualize com suas próprias credenciais do banco Oracle da FIAP (RM e Senha). Assim os testes serão contabilizados sob o seu usuário;
5. Retorne ao terminal e digite o comando do Maven Wrapper respectivo do seu sistema operacional para rodar a aplicação Spring Boot:

**No Windows PowerShell/CMD:**
```bash
.\mvnw.cmd spring-boot:run
```

**No Linux/Mac:**
```bash
./mvnw spring-boot:run
```

6. Acompanhe os logs de terminal. O sistema aplicará os schemas do banco e **10 testes que provam a funcionalidade das regras de negócio serão impressos linha por linha na tela.**

Assim que os testes de regra de negócio acabarem no console, a aplicação vai continuar ativa. O servidor nativo do Spring, o **Tomcat, estará escutando e rodando na porta 8080 da sua máquina**.
