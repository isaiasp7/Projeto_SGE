// api.js

const BASE_URL = 'http://localhost:8080/SGE';

// Configuração central das rotas (Altere aqui se os mapeamentos do seu Controller mudarem)
const ENDPOINTS = {
    alunos: '/alunoCrud'
};

// Função base para processar requisições com tratamento de erro
async function request(endpoint, method = 'GET', data = null) {
    const url = `${BASE_URL}${endpoint}`;
    
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            // 'Authorization': 'Bearer SEU_TOKEN_AQUI' // Descomente se usar Spring Security/JWT
        }
    };

    if (data && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, options);
        
        // Se for um DELETE ou 204 No Content, não há JSON para retornar
        if (response.status === 204 || method === 'DELETE') {
            return { success: true };
        }

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(`[API Error] ${method} ${url}:`, error);
        throw error;
    }
}

// Exportando os serviços do CRUD
export const AlunoService = {
    listarTodos: () => request(ENDPOINTS.alunos, 'GET'),
    
    buscarPorCpf: (cpf) => request(`${ENDPOINTS.alunos}?cpf?${cpf}`, 'GET'),
    buscarPorID: (id) => request(`${ENDPOINTS.alunos}?id=${cpf}`, 'GET'),
    
    criar: (aluno) => request(ENDPOINTS.alunos, 'POST', aluno),
    
    // Obs: Alguns controllers Spring recebem o PUT no endpoint raiz enviando o Cpf no body. 
    // Se for o seu caso, mude o primeiro parâmetro para ENDPOINTS.alunos
    atualizar: (Cpf, aluno) => request(`${ENDPOINTS.alunos}/updateAlunos/${Cpf}`, 'PUT', aluno),
    
    deletar: (Cpf) => request(`${ENDPOINTS.alunos}/delete/${Cpf}`, 'DELETE')
};