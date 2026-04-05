

-- Tabela de Leads
CREATE TABLE T_LEAD (
    id               NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome             VARCHAR2(150)  NOT NULL,
    telefone         VARCHAR2(20)   NOT NULL,
    email            VARCHAR2(150),
    canal_entrada    VARCHAR2(50)   NOT NULL,
    procedimento_interesse VARCHAR2(100),
    preferencia_medico     VARCHAR2(150),
    data_cadastro    TIMESTAMP      NOT NULL,
    CONSTRAINT uk_lead_telefone UNIQUE (telefone),
    CONSTRAINT uk_lead_email    UNIQUE (email)
);

COMMENT ON TABLE T_LEAD IS 'Tabela de leads capturados via canais digitais (Instagram, Facebook, Google, TikTok)';
COMMENT ON COLUMN T_LEAD.canal_entrada IS 'Canal de origem do lead: Instagram, Facebook, Google, TikTok, Indicacao';
COMMENT ON COLUMN T_LEAD.procedimento_interesse IS 'Procedimento pelo qual o lead demonstrou interesse';

-- Tabela de Pacientes
CREATE TABLE T_PACIENTE (
    id                NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome              VARCHAR2(150)  NOT NULL,
    cpf               VARCHAR2(14)   NOT NULL,
    data_nascimento   DATE           NOT NULL,
    sexo              VARCHAR2(20)   NOT NULL,
    peso              NUMBER(5,2),
    altura            NUMBER(3,2),
    email             VARCHAR2(150),
    telefone          VARCHAR2(20),
    canal_conhecimento VARCHAR2(50),
    CONSTRAINT uk_paciente_cpf UNIQUE (cpf)
);

COMMENT ON TABLE T_PACIENTE IS 'Tabela de pacientes cadastrados no CRM do hospital';
COMMENT ON COLUMN T_PACIENTE.cpf IS 'CPF do paciente - campo unico para evitar duplicidade';
COMMENT ON COLUMN T_PACIENTE.peso IS 'Peso do paciente em kg para calculo de IMC';
COMMENT ON COLUMN T_PACIENTE.altura IS 'Altura do paciente em metros para calculo de IMC';

-- Tabela de Agendamentos
CREATE TABLE T_AGENDAMENTO (
    id                NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    paciente_id       NUMBER         NOT NULL,
    data_agendamento  DATE           NOT NULL,
    hora              TIMESTAMP      NOT NULL,
    procedimento      VARCHAR2(100)  NOT NULL,
    status            VARCHAR2(20)   NOT NULL,
    CONSTRAINT fk_agendamento_paciente FOREIGN KEY (paciente_id) REFERENCES T_PACIENTE(id),
    CONSTRAINT ck_agendamento_status CHECK (status IN ('agendado','atendido','falta','abandono','reagendado','cancelado'))
);

COMMENT ON TABLE T_AGENDAMENTO IS 'Tabela de agendamentos de consultas medicas';
COMMENT ON COLUMN T_AGENDAMENTO.status IS 'Status do agendamento: agendado, atendido, falta, abandono, reagendado, cancelado';
COMMENT ON COLUMN T_AGENDAMENTO.paciente_id IS 'Chave estrangeira referenciando o paciente';
