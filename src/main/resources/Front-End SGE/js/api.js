// api.js - Serviços centralizados da API SGE

const BASE_URL = 'http://localhost:8080';

const ENDPOINTS = {
    alunos: '/SGE/alunoCrud',
    professores: '/ProfCrud',
    turmas: '/TurmaCrud',
    disciplinas: '/DisciCrud',
    notas: '/NotaCrud',
    matriculas: '/MatriCrud'
};

async function request(endpoint, method = 'GET', data = null) {
    const url = `${BASE_URL}${endpoint}`;
    const options = {
        method,
        headers: { 'Content-Type': 'application/json' }
    };
    if (data && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(data);
    }
    try {
        const response = await fetch(url, options);
        if (response.status === 204 || method === 'DELETE') return { success: true };
        if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);
        return await response.json();
    } catch (error) {
        console.error(`[API] ${method} ${url}:`, error);
        throw error;
    }
}

// ==================== ALUNOS ====================
export const AlunoService = {
    listarTodos: () => request(ENDPOINTS.alunos, 'GET'),
    buscarPorCpf: (cpf) => request(`${ENDPOINTS.alunos}?cpf=${cpf}`, 'GET'),
    buscarPorId: (id) => request(`${ENDPOINTS.alunos}?id=${id}`, 'GET'),
    criar: (aluno) => request(ENDPOINTS.alunos, 'POST', aluno),
    atualizar: (cpf, aluno) => request(`${ENDPOINTS.alunos}/updateAlunos/${cpf}`, 'PUT', aluno),
    deletar: (cpf) => request(`${ENDPOINTS.alunos}/delete/${cpf}`, 'DELETE')
};

// ==================== PROFESSORES ====================
export const ProfessorService = {
    listarTodos: () => request(`${ENDPOINTS.professores}/getAll`, 'GET'),
    buscarPorId: (id) => request(`${ENDPOINTS.professores}/getById/${id}`, 'GET'),
    criar: (professor) => request(`${ENDPOINTS.professores}/create`, 'POST', professor),
    atualizar: (id, professor) => request(`${ENDPOINTS.professores}/updateP/${id}`, 'PUT', professor),
    deletar: (id) => request(`${ENDPOINTS.professores}/delete/${id}`, 'DELETE'),
    addDisciplina: (professor_id, disciplina_id) => request(`${ENDPOINTS.professores}/AddDisciplina`, 'POST', { professor_id, disciplina_id })
};

// ==================== TURMAS ====================
export const TurmaService = {
    listarTodas: () => request(`${ENDPOINTS.turmas}/getAll`, 'GET'),
    buscarPorId: (id) => request(`${ENDPOINTS.turmas}/getById/${id}`, 'GET'),
    criar: (turma) => request(`${ENDPOINTS.turmas}/createT`, 'POST', turma),
    alterarCapacidade: (id, cap) => request(`${ENDPOINTS.turmas}/alterCapacidade/${id}&${cap}`, 'PUT'),
    alterarNome: (id, turmaN) => request(`${ENDPOINTS.turmas}/alterNomes/${id}?turmaN=${encodeURIComponent(turmaN)}`, 'PUT'),
    deletar: (id) => request(`${ENDPOINTS.turmas}/deleteTurm?id=${id}`, 'DELETE')
};

// ==================== DISCIPLINAS ====================
export const DisciplinaService = {
    listarTodas: () => request(`${ENDPOINTS.disciplinas}/getAll`, 'GET'),
    buscarPorId: (id) => request(`${ENDPOINTS.disciplinas}/search/${id}`, 'GET'),
    criar: (disciplina) => request(`${ENDPOINTS.disciplinas}/create`, 'POST', disciplina),
    atualizar: (id, disciplina) => request(`${ENDPOINTS.disciplinas}/updateDisc/${id}`, 'PUT', disciplina),
    deletar: (id) => request(`${ENDPOINTS.disciplinas}/delete?id=${id}`, 'DELETE'),
    relacionarTurma: (idDisciplina, idTurma) => request(`${ENDPOINTS.disciplinas}/relacionaDT`, 'PUT', { idDisciplina, idTurma })
};

// ==================== NOTAS ====================
export const NotaService = {
    listarTodas: () => request(`${ENDPOINTS.notas}/getallN`, 'GET'),
    buscarPorId: (id) => request(`${ENDPOINTS.notas}/searchN/${id}`, 'GET'),
    adicionar: (idMat, idDisc, nota1, nota2) => request(`${ENDPOINTS.notas}/addN`, 'POST', { idMat: Number(idMat), idDisc: Number(idDisc), nota1: Number(nota1), nota2: Number(nota2) }),
    atualizar: (id, nota) => request(`${ENDPOINTS.notas}/updateN/${id}`, 'PUT', nota),
    deletar: (id) => request(`${ENDPOINTS.notas}/deletN/${id}`, 'DELETE')
};

// ==================== MATRÍCULAS ====================
export const MatriculaService = {
    listarTodas: () => request(ENDPOINTS.matriculas, 'GET'),
    buscarPorId: (id) => request(`${ENDPOINTS.matriculas}/getID/${id}`, 'GET'),
    criar: (cpf) => request(`${ENDPOINTS.matriculas}/createM/${cpf}`, 'POST'),
    relacionarTurma: (alunoCpf, turmaID) => request(`${ENDPOINTS.matriculas}/relacionaMT`, 'PUT', { alunoCpf, turmaID }),
    deletar: (id, deleteAluno = false) => request(`${ENDPOINTS.matriculas}/${id}?deleteAluno=${deleteAluno}`, 'DELETE')
};
