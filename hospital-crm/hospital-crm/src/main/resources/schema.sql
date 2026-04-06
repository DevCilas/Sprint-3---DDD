
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
