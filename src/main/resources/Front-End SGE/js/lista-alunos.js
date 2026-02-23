// lista-alunos.js - Lista todos os alunos com filtros por turma/sala e busca por ID, CPF ou Nome

import { AlunoService, TurmaService, MatriculaService } from './api.js';

const listaAlunos = {
    alunos: [],
    matriculas: [],
    turmas: [],
    alunoPorTurma: new Map(),
    searchTimeout: null,
    SEARCH_DEBOUNCE_MS: 800,

    init() {
        document.getElementById('filterTurma').addEventListener('change', () => this.aplicarFiltros());
        document.getElementById('filterSala').addEventListener('change', () => this.aplicarFiltros());
        document.getElementById('searchInput').addEventListener('input', () => this.onSearchInput());
        this.carregarDados();
    },

    async carregarDados() {
        const loading = document.getElementById('loading');
        const grid = document.getElementById('dataGrid');
        loading.style.display = 'block';
        grid.innerHTML = '';
        try {
            const [alunos, matriculas, turmas] = await Promise.all([
                AlunoService.listarTodos(),
                MatriculaService.listarTodas(),
                TurmaService.listarTodas()
            ]);
            this.alunos = Array.isArray(alunos) ? alunos : [];
            this.matriculas = Array.isArray(matriculas) ? matriculas : [];
            this.turmas = Array.isArray(turmas) ? turmas : [];

            this.construirMapaAlunoTurmas();
            this.preencherSelects();
            loading.style.display = 'none';
            this.aplicarFiltros();
        } catch (e) {
            loading.style.display = 'none';
            grid.innerHTML = `<p class="empty-msg" style="color:#f43f5e;">Erro ao carregar: ${e.message}</p>`;
        }
    },

    construirMapaAlunoTurmas() {
        this.alunoPorTurma.clear();
        for (const m of this.matriculas) {
            const cpf = m.aluno?.cpf ?? m.aluno?.id ?? m.nome;
            const turmaId = m.litlleTurma?.id ?? m.turma?.id;
            if (cpf != null && turmaId != null) {
                const key = String(cpf);
                if (!this.alunoPorTurma.has(key)) this.alunoPorTurma.set(key, new Set());
                this.alunoPorTurma.get(key).add(String(turmaId));
            }
        }
    },

    preencherSelects() {
        const opts = '<option value="">Todas</option>' + this.turmas.map(t =>
            `<option value="${t.id}">${t.nome || t.id}</option>`
        ).join('');
        document.getElementById('filterTurma').innerHTML = opts;
        document.getElementById('filterSala').innerHTML = opts;
    },

    alunoPertenceTurma(cpf, turmaId) {
        if (!turmaId) return true;
        const set = this.alunoPorTurma.get(String(cpf));
        return set ? set.has(String(turmaId)) : false;
    },

    aplicarFiltros() {
        const termo = document.getElementById('searchInput').value.trim();
        if (termo) {
            this.executarBusca(termo);
            return;
        }
        const turmaId = document.getElementById('filterTurma').value;
        const salaId = document.getElementById('filterSala').value;
        let lista = this.alunos;
        if (turmaId) lista = lista.filter(a => this.alunoPertenceTurma(a.cpf, turmaId));
        if (salaId) lista = lista.filter(a => this.alunoPertenceTurma(a.cpf, salaId));
        this.renderizar(lista);
    },

    onSearchInput() {
        clearTimeout(this.searchTimeout);
        const termo = document.getElementById('searchInput').value.trim();
        this.searchTimeout = setTimeout(() => {
            if (termo) this.executarBusca(termo);
            else this.aplicarFiltros();
        }, this.SEARCH_DEBOUNCE_MS);
    },

    async executarBusca(termo) {
        const grid = document.getElementById('dataGrid');
        const loading = document.getElementById('loading');
        if (!termo) {
            this.aplicarFiltros();
            return;
        }
        loading.style.display = 'block';
        grid.innerHTML = '';
        try {
            const soNumeros = /^\d+$/.test(termo);
            let resultado = [];
            if (soNumeros && termo.length <= 10) {
                const res = await AlunoService.buscarPorId(parseInt(termo, 10));
                resultado = res ? [res] : [];
            } else if (soNumeros && termo.length === 11) {
                const res = await AlunoService.buscarPorCpf(termo);
                resultado = Array.isArray(res) ? res : (res ? [res] : []);
            } else {
                const nomeLower = termo.toLowerCase();
                resultado = this.alunos.filter(a => (a.nome || '').toLowerCase().includes(nomeLower));
            }
            loading.style.display = 'none';
            const turmaId = document.getElementById('filterTurma').value;
            const salaId = document.getElementById('filterSala').value;
            if (turmaId) resultado = resultado.filter(a => this.alunoPertenceTurma(a.cpf, turmaId));
            if (salaId) resultado = resultado.filter(a => this.alunoPertenceTurma(a.cpf, salaId));
            this.renderizar(resultado);
        } catch (e) {
            loading.style.display = 'none';
            this.renderizar([]);
        }
    },

    renderizar(lista) {
        const grid = document.getElementById('dataGrid');
        if (!lista || lista.length === 0) {
            grid.innerHTML = '<p class="empty-msg">Nenhum aluno encontrado.</p>';
            return;
        }
        grid.innerHTML = lista.map(a => `
            <div class="card">
                <h3>${a.nome || 'N/A'}</h3>
                <p><i class="fas fa-id-card"></i> CPF: ${a.cpf ?? '-'}</p>
                <p><i class="fas fa-envelope"></i> ${a.email ?? '-'}</p>
                <p><i class="fas fa-calendar"></i> ${(a.dataNascimento || a.dataNasci || '-').toString().split('T')[0]}</p>
            </div>
        `).join('');
    }
};

document.addEventListener('DOMContentLoaded', () => listaAlunos.init());
window.listaAlunos = listaAlunos;
