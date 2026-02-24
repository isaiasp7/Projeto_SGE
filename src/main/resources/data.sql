-- Inserts for aluno (camelCase columns)
INSERT INTO aluno (cpf, nome, dataNascimento, quantFaltas, email, senha) VALUES
('12345678901', 'Ana Silva', '2000-05-15', 2, 'ana.silva@email.com', 'senha123'),
('23456789012', 'Bruno Costa', '1999-11-20', 0, 'bruno.costa@email.com', 'senha123'),
('34567890123', 'Carla Souza', '2001-02-10', 1, 'carla.souza@email.com', 'senha123'),
('45678901234', 'Diego Lima', '2000-07-25', 3, 'diego.lima@email.com', 'senha123'),
('56789012345', 'Elisa Martins', '1998-12-05', 0, 'elisa.martins@email.com', 'senha123'),
('67890123456', 'Felipe Rocha', '2002-03-18', 4, 'felipe.rocha@email.com', 'senha123'),
('78901234567', 'Gabriela Alves', '1999-08-30', 1, 'gabriela.alves@email.com', 'senha123'),
('89012345678', 'Hugo Fernandes', '2001-01-12', 2, 'hugo.fernandes@email.com', 'senha123'),
('90123456789', 'Isabela Pinto', '2000-09-22', 0, 'isabela.pinto@email.com', 'senha123'),
('01234567890', 'João Pereira', '1998-06-14', 3, 'joao.pereira@email.com', 'senha123');

-- Inserts for turma (camelCase columns)
INSERT INTO turma (id, nome, capacidadeAtual, capacidadeMax) VALUES
(1, 'A1', 25, 30),
(2, 'B1', 20, 30),
(3, 'C1', 28, 30),
(4, 'D1', 15, 25),
(5, 'E1', 30, 30),
(6, 'F1', 18, 25),
(7, 'G1', 22, 30),
(8, 'H1', 27, 30),
(9, 'I1', 20, 25),
(10, 'J1', 24, 30);

-- Inserts for disciplina (camelCase columns)
INSERT INTO disciplina (id, nome, cargaHoraria, turmaFk) VALUES
(101, 'Matemática', 60, 1),
(102, 'Português', 45, 2),
(103, 'História', 50, 3),
(104, 'Geografia', 40, 4),
(105, 'Física', 55, 5),
(106, 'Química', 50, 6),
(107, 'Biologia', 45, 7),
(108, 'Inglês', 60, 8),
(109, 'Educação Física', 30, 9),
(110, 'Artes', 35, 10);

-- Inserts for professor (camelCase columns)
INSERT INTO professor (id, nome, email, telefone, senha, disciplinaId) VALUES
(201, 'Carlos Menezes', 'carlos.menezes@email.com', '11999990001', 'senha123', 101),
(202, 'Daniela Ribeiro', 'daniela.ribeiro@email.com', '11999990002', 'senha123', 102),
(203, 'Eduardo Silva', 'eduardo.silva@email.com', '11999990003', 'senha123', 103),
(204, 'Fernanda Costa', 'fernanda.costa@email.com', '11999990004', 'senha123', 104),
(205, 'Gustavo Almeida', 'gustavo.almeida@email.com', '11999990005', 'senha123', 105),
(206, 'Helena Souza', 'helena.souza@email.com', '11999990006', 'senha123', 106),
(207, 'Igor Martins', 'igor.martins@email.com', '11999990007', 'senha123', 107),
(208, 'Juliana Lima', 'juliana.lima@email.com', '11999990008', 'senha123', 108),
(209, 'Kleber Fernandes', 'kleber.fernandes@email.com', '11999990009', 'senha123', 109),
(210, 'Larissa Pinto', 'larissa.pinto@email.com', '11999990010', 'senha123', 110);

-- Inserts for matricula (camelCase columns)
INSERT INTO matricula (id, dataMatricula, status, alunoCpf, turmaId) VALUES
(301, '2025-01-10', 'Ativo', '12345678901', 1),
(302, '2025-01-11', 'Ativo', '23456789012', 2),
(303, '2025-01-12', 'Ativo', '34567890123', 3),
(304, '2025-01-13', 'Ativo', '45678901234', 4),
(305, '2025-01-14', 'Ativo', '56789012345', 5),
(306, '2025-01-15', 'Ativo', '67890123456', 6),
(307, '2025-01-16', 'Ativo', '78901234567', 7),
(308, '2025-01-17', 'Ativo', '89012345678', 8),
(309, '2025-01-18', 'Ativo', '90123456789', 9),
(310, '2025-01-19', 'Ativo', '01234567890', 10);

-- Inserts for notas (camelCase columns)
INSERT INTO notas (id, nota1, nota2, media, situacao, matriculaFk, disciplinaFk) VALUES
(401, 7.5, 8.0, 7.75, 'Aprovado', 301, 101),
(402, 6.0, 7.0, 6.5, 'Aprovado', 302, 102),
(403, 5.0, 4.5, 4.75, 'Reprovado', 303, 103),
(404, 9.0, 8.5, 8.75, 'Aprovado', 304, 104),
(405, 7.0, 7.5, 7.25, 'Aprovado', 305, 105),
(406, 6.5, 6.0, 6.25, 'Aprovado', 306, 106),
(407, 8.0, 8.0, 8.00, 'Aprovado', 307, 107),
(408, 4.0, 5.0, 4.50, 'Reprovado', 308, 108),
(409, 7.5, 7.0, 7.25, 'Aprovado', 309, 109),
(410, 5.5, 6.0, 5.75, 'Aprovado', 310, 110);
