CREATE TABLE aluno
(
    cpf        VARCHAR(11)  NOT NULL,
    nome       VARCHAR(50)  NOT NULL,
    data_nasc  date         NOT NULL,
    quant_falt INT          NULL,
    email      VARCHAR(255) NULL,
    senha      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_aluno PRIMARY KEY (cpf)
);

CREATE TABLE disciplina
(
    id            BIGINT      NOT NULL,
    nome          VARCHAR(50) NOT NULL,
    carga_horaria INT         NOT NULL,
    turma_fk      BIGINT      NULL,
    CONSTRAINT pk_disciplina PRIMARY KEY (id)
);

CREATE TABLE matricula
(
    id             BIGINT       NOT NULL,
    data_matricula date         NULL,
    status         VARCHAR(255) NOT NULL,
    aluno_cpf      VARCHAR(11)  NULL,
    turma_id       BIGINT       NULL,
    CONSTRAINT pk_matricula PRIMARY KEY (id)
);

CREATE TABLE notas
(
    id             BIGINT        NOT NULL,
    nota1          DECIMAL(5, 2) NULL,
    nota2          DECIMAL(5, 2) NULL,
    media          DECIMAL(5, 2) NULL,
    situacao       VARCHAR(255)  NULL,
    matricula_fk   BIGINT        NULL,
    disciplinan_fk BIGINT        NULL,
    CONSTRAINT pk_notas PRIMARY KEY (id)
);

CREATE TABLE professor
(
    id            BIGINT       NOT NULL,
    nome          VARCHAR(50)  NOT NULL,
    email         VARCHAR(50)  NULL,
    telefone      VARCHAR(255) NULL,
    senha         VARCHAR(255) NOT NULL,
    disciplina_id BIGINT       NULL,
    CONSTRAINT pk_professor PRIMARY KEY (id)
);

CREATE TABLE turma
(
    id               BIGINT      NOT NULL,
    nome             VARCHAR(10) NULL,
    capacidade_atual INT         NOT NULL,
    capacidade_max   INT         NOT NULL,
    CONSTRAINT pk_turma PRIMARY KEY (id)
);

ALTER TABLE matricula
    ADD CONSTRAINT uc_matricula_aluno_cpf UNIQUE (aluno_cpf);

ALTER TABLE notas
    ADD CONSTRAINT uc_notas_matricula_fk UNIQUE (matricula_fk);

ALTER TABLE professor
    ADD CONSTRAINT uc_professor_disciplina UNIQUE (disciplina_id);

ALTER TABLE professor
    ADD CONSTRAINT uc_professor_email UNIQUE (email);

ALTER TABLE turma
    ADD CONSTRAINT uc_turma_nome UNIQUE (nome);

ALTER TABLE disciplina
    ADD CONSTRAINT FK_DISCIPLINA_ON_TURMA_FK FOREIGN KEY (turma_fk) REFERENCES turma (id);

ALTER TABLE matricula
    ADD CONSTRAINT FK_MATRICULA_ON_ALUNO_CPF FOREIGN KEY (aluno_cpf) REFERENCES aluno (cpf);

ALTER TABLE matricula
    ADD CONSTRAINT FK_MATRICULA_ON_TURMA FOREIGN KEY (turma_id) REFERENCES turma (id);

ALTER TABLE notas
    ADD CONSTRAINT FK_NOTAS_ON_DISCIPLINAN_FK FOREIGN KEY (disciplinan_fk) REFERENCES disciplina (id);

ALTER TABLE notas
    ADD CONSTRAINT FK_NOTAS_ON_MATRICULA_FK FOREIGN KEY (matricula_fk) REFERENCES matricula (id);

ALTER TABLE professor
    ADD CONSTRAINT FK_PROFESSOR_ON_DISCIPLINA FOREIGN KEY (disciplina_id) REFERENCES disciplina (id);