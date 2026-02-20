// app.js
import { AlunoService } from './api.js';

const DOM = {
    grid: document.getElementById('dataGrid'),
    loading: document.getElementById('loading'),
    panel: document.getElementById('formPanel'),
    form: document.getElementById('crudForm'),
    title: document.getElementById('formTitle')
};

// 1. READ: Carregar dados da API
async function carregarLista() {
    try {
        DOM.loading.style.display = 'block';
        DOM.grid.innerHTML = '';
        
        const alunos = await AlunoService.listarTodos();
        
        if (alunos.length === 0) {
            DOM.grid.innerHTML = '<p style="color: #a1a1aa;">Nenhum registro encontrado no banco de dados.</p>';
            return;
        }

        DOM.grid.innerHTML = alunos.map(aluno => `
            <div class="card">
                <h3>${aluno.nome} <span style="font-size: 0.8rem; background: rgba(168, 85, 247, 0.2); color: var(--purple-glow); padding: 3px 8px; border-radius: 12px;">Ativo</span></h3>
                <p><i class="fas fa-id-card"></i> ${aluno.cpf}</p>
                <p><i class="fas fa-envelope"></i> ${aluno.email}</p>
                
                <div class="card-actions">
                    <button class="action-btn" onclick="window.editarRegistro(${aluno.cpf})"><i class="fas fa-pen"></i> Editar</button>
                    <button class="action-btn delete" onclick="window.deletarRegistro(${aluno.cpf})"><i class="fas fa-trash"></i> Excluir</button>
                </div>
            </div>
        `).join('');

    } catch (error) {
        DOM.grid.innerHTML = `<p style="color: var(--danger);"><i class="fas fa-exclamation-triangle"></i> Falha ao comunicar com localhost:8080. Verifique se o backend (Spring) está rodando e com CORS liberado.</p>`;
    } finally {
        DOM.loading.style.display = 'none';
    }
}

// 2. CREATE / UPDATE: Submeter Formulário
DOM.form.addEventListener('submit', async (e) => {
    e.preventDefault();
    
   
    
    // Objeto que será enviado ao Spring Boot (Os nomes das propriedades devem bater exatmente com sua Entidade Java)
    const payload = {
        nome: document.getElementById('nome').value,
        cpf: document.getElementById('cpf').value,
        dataNascimento: document.getElementById('dataNascimento').value
    };

    try {
        if (id) {
            await AlunoService.atualizar(id, payload);
        } else {
            await AlunoService.criar(payload);
        }
        window.fecharPainel();
        carregarLista(); // Recarrega a tela
    } catch (error) {
        alert("Erro ao salvar dados na API!");
    }
});

// 3. DELETE: Excluir Registro
window.deletarRegistro = async (cpf) => {
    if (confirm("Confirma a exclusão definitiva deste registro?")) {
        try {
            await AlunoService.deletar(cpf);
            carregarLista();
        } catch (error) {
            alert("Erro ao excluir registro.");
        }
    }
};

// Funções de UI (Tornando globais para os onclicks no HTML)
window.abrirPainel = () => {
    DOM.form.reset();
    document.getElementById('alunoId').value = '';
    DOM.title.innerText = 'Adicionar Registro';
    DOM.panel.classList.add('open');
};

window.fecharPainel = () => {
    DOM.panel.classList.remove('open');
};

window.editarRegistro = async (cpf) => {
    try {
        const aluno = await AlunoService.buscarPorCpf(cpf);
        //console.table(aluno[0].nome);
        document.getElementById('nome').placeholder = aluno[0].nome;
        document.getElementById('cpf').placeholder = aluno[0].cpf;
        document.getElementById('email').placeholder = aluno[0].email;
        // Dependendo de como o Java envia a data, pode precisar de formatação aqui
        document.getElementById('dataNascimento').placeholder = aluno.dataNascimento; 
        
        DOM.title.innerText = 'Editar Registro';
        DOM.panel.classList.add('open');
    } catch (error) {
        alert("Erro ao buscar dados do aluno para edição.");
    }
};

// Inicializa o sistema ao carregar a página
document.addEventListener('DOMContentLoaded', carregarLista);