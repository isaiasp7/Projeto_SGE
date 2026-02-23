// index-script.js - Tela para pessoas (alunos/professores): Turmas > Classes > Alunos + busca id/cpf

import { AlunoService, TurmaService, MatriculaService } from './api.js';

const app = {
    turmaSelecionada: null,
    matriculas: [],
    searchTimeout: null,
    SEARCH_DEBOUNCE_MS: 1000,

    init() {
        document.getElementById('searchInput').addEventListener('input', () => this.onSearchInput());
        this.carregarTurmas();
    },

    async carregarTurmas() {
        const grid = document.getElementById('dataGrid');
        const loading = document.getElementById('loading');
        const searchBar = document.getElementById('searchBar');
        const breadcrumb = document.getElementById('breadcrumb');

        loading.style.display = 'block';
        grid.innerHTML = '';
        searchBar.style.display = 'none';
        this.turmaSelecionada = null;
        breadcrumb.innerHTML = '<span onclick="app.voltarTurmas()">Turmas</span>';

        try {
            const turmas = await TurmaService.listarTodas();
            loading.style.display = 'none';
            if (!turmas || turmas.length === 0) {
                grid.innerHTML = '<p class="empty-msg">Nenhuma turma/classe cadastrada.</p>';
                return;
            }
            grid.innerHTML = turmas.map(t => `
                <div class="card card-turma" onclick="app.selecionarTurma(${t.id}, '${(t.nome || '').replace(/'/g, "\\'")}')">
                    <h3>${t.nome || 'N/A'} <span class="badge">Classe</span></h3>
                    <p><i class="fas fa-users"></i> ${t.capacidadeAtual ?? 0} / ${t.capacidadeMax ?? 0} alunos</p>
                    <p><i class="fas fa-book"></i> ${(t.disciplinaList || []).length} disciplina(s)</p>
                </div>
            `).join('');
        } catch (e) {
            loading.style.display = 'none';
            grid.innerHTML = `<p class="empty-msg" style="color:#f43f5e;">Erro ao carregar turmas: ${e.message}</p>`;
        }
    },

    async selecionarTurma(id, nome) {
        this.turmaSelecionada = { id, nome };
        const grid = document.getElementById('dataGrid');
        const loading = document.getElementById('loading');
        const searchBar = document.getElementById('searchBar');
        const breadcrumb = document.getElementById('breadcrumb');

        breadcrumb.innerHTML = `
            <span onclick="app.voltarTurmas()">Turmas</span>
            <span class="sep">›</span>
            <span>${nome || id}</span>
        `;
        searchBar.style.display = 'flex';
        document.getElementById('searchInput').value = '';

        loading.style.display = 'block';
        grid.innerHTML = '';

        try {
            const matriculas = await MatriculaService.listarTodas();
            this.matriculas = Array.isArray(matriculas) ? matriculas : [];
            const matriculasTurma = this.matriculas.filter(m => {
                const tid = m.litlleTurma?.id ?? m.turma?.id;
                return tid != null && String(tid) === String(id);
            });
            loading.style.display = 'none';
            this.renderizarAlunos(matriculasTurma);
        } catch (e) {
            loading.style.display = 'none';
            grid.innerHTML = `<p class="empty-msg" style="color:#f43f5e;">Erro ao carregar alunos: ${e.message}</p>`;
        }
    },

    renderizarAlunos(matriculas) {
        const grid = document.getElementById('dataGrid');
        if (!matriculas || matriculas.length === 0) {
            grid.innerHTML = '<p class="empty-msg">Nenhum aluno nesta turma.</p>';
            return;
        }
        grid.innerHTML = matriculas.map(m => {
            const aluno = m.aluno || m;
            const nome = m.nome || aluno?.nome || 'N/A';
            const cpf = aluno?.cpf ?? aluno?.id ?? '-';
            const email = aluno?.email ?? '-';
            return `
                <div class="card">
                    <h3>${nome}</h3>
                    <p><i class="fas fa-id-card"></i> CPF: ${cpf}</p>
                    <p><i class="fas fa-envelope"></i> ${email}</p>
                </div>
            `;
        }).join('');
    },

    voltarTurmas() {
        this.turmaSelecionada = null;
        this.carregarTurmas();
    },

    onSearchInput() {
        clearTimeout(this.searchTimeout);
        const input = document.getElementById('searchInput').value.trim();
        this.searchTimeout = setTimeout(() => this.executarBusca(input), this.SEARCH_DEBOUNCE_MS);
    },

    async executarBusca(termo) {
        const grid = document.getElementById('dataGrid');
        const loading = document.getElementById('loading');

        if (!termo) {
            if (this.turmaSelecionada) {
                const matriculas = this.matriculas.filter(m => {
                    const tid = m.litlleTurma?.id ?? m.turma?.id;
                    return tid != null && String(tid) === String(this.turmaSelecionada.id);
                });
                this.renderizarAlunos(matriculas);
            }
            return;
        }

        loading.style.display = 'block';
        grid.innerHTML = '';

        try {
            let resultado = [];
            const soNumeros = /^\d+$/.test(termo);
            if (soNumeros && termo.length <= 10) {
                resultado = await AlunoService.buscarPorId(parseInt(termo, 10));
            } else {
                resultado = await AlunoService.buscarPorCpf(termo);
            }
            loading.style.display = 'none';
            const arr = Array.isArray(resultado) ? resultado : (resultado ? [resultado] : []);
            if (arr.length === 0) {
                grid.innerHTML = '<p class="empty-msg">Nenhum resultado encontrado para a busca.</p>';
                return;
            }
            grid.innerHTML = arr.map(a => `
                <div class="card">
                    <h3>${a.nome || 'N/A'}</h3>
                    <p><i class="fas fa-id-card"></i> CPF: ${a.cpf ?? '-'}</p>
                    <p><i class="fas fa-envelope"></i> ${a.email ?? '-'}</p>
                </div>
            `).join('');
        } catch (e) {
            loading.style.display = 'none';
            grid.innerHTML = '<p class="empty-msg">Nenhum resultado encontrado para a busca.</p>';
        }
    }
};

document.addEventListener('DOMContentLoaded', () => app.init());
window.app = app;
