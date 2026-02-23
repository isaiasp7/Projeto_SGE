// telaadm-script.js - Lógica do painel administrativo

import {
    AlunoService,
    ProfessorService,
    TurmaService,
    DisciplinaService,
    NotaService,
    MatriculaService
} from './api.js';

const adm = {
    panel: null,
    formContent: null,
    formTitle: null,
    currentSection: 'review',
    editId: null,

    init() {
        this.panel = document.getElementById('formPanel');
        this.formContent = document.getElementById('formContent');
        this.formTitle = document.getElementById('formTitle');

        document.querySelectorAll('.nav-item[data-section]').forEach(el => {
            if (el.dataset.section === 'logout') return;
            el.addEventListener('click', () => this.trocarSecao(el.dataset.section));
        });

        document.querySelectorAll('.sub-nav-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                document.querySelectorAll('.sub-nav-btn').forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                document.getElementById('sub-lista').style.display = btn.dataset.sub === 'lista' ? 'block' : 'none';
                document.getElementById('sub-notas').style.display = btn.dataset.sub === 'notas' ? 'block' : 'none';
                if (btn.dataset.sub === 'notas') this.carregarNotas();
            });
        });

        this.trocarSecao('review');
    },

    trocarSecao(sec) {
        document.querySelectorAll('.nav-item[data-section]').forEach(el => {
            el.classList.toggle('active', el.dataset.section === sec);
        });
        document.querySelectorAll('.section').forEach(s => {
            s.classList.toggle('active', s.id === `section-${sec}`);
        });
        this.currentSection = sec;
        if (sec === 'review') this.carregarReview();
        if (sec === 'professores') this.carregarProfessores();
        if (sec === 'alunos') this.carregarAlunos();
        if (sec === 'turmas') this.carregarTurmas();
        if (sec === 'disciplinas') this.carregarDisciplinas();
    },

    async carregarReview() {
        const ids = { alunos: 'r-alunos', professores: 'r-professores', turmas: 'r-turmas', disciplinas: 'r-disciplinas', matriculas: 'r-matriculas', notas: 'r-notas' };
        const set = (id, v) => { const el = document.getElementById(id); if (el) el.textContent = v; };
        try {
            const [alunos, professores, turmas, disciplinas, matriculas, notas] = await Promise.all([
                AlunoService.listarTodos(),
                ProfessorService.listarTodos(),
                TurmaService.listarTodas(),
                DisciplinaService.listarTodas(),
                MatriculaService.listarTodas(),
                NotaService.listarTodas()
            ]);
            const nAlunos = Array.isArray(alunos) ? alunos.length : 0;
            const nProf = Array.isArray(professores) ? professores.length : 0;
            const nTurmas = Array.isArray(turmas) ? turmas.length : 0;
            const nDisc = Array.isArray(disciplinas) ? disciplinas.length : 0;
            const nMat = Array.isArray(matriculas) ? matriculas.length : 0;
            const nNotas = Array.isArray(notas) ? notas.length : 0;
            set(ids.alunos, nAlunos);
            set(ids.professores, nProf);
            set(ids.turmas, nTurmas);
            set(ids.disciplinas, nDisc);
            set(ids.matriculas, nMat);
            set(ids.notas, nNotas);

            const chartTurmas = document.getElementById('chart-turmas');
            if (chartTurmas) {
                const lista = Array.isArray(turmas) ? turmas : [];
                const maxCap = Math.max(...lista.map(t => t.capacidadeMax || 1), 1);
                chartTurmas.innerHTML = lista.slice(0, 8).map(t => {
                    const pct = Math.round(((t.capacidadeAtual || 0) / (t.capacidadeMax || 1)) * 100);
                    return `<div class="bar-row"><span>${t.nome || t.id}</span><div class="bar-track"><div class="bar-fill" style="width:${pct}%"></div></div><span>${pct}%</span></div>`;
                }).join('') || '<p class="empty-msg">Nenhuma turma</p>';
            }

            const chartDist = document.getElementById('chart-distribuicao');
            if (chartDist) {
                const total = nAlunos + nProf || 1;
                const pctAlunos = Math.round((nAlunos / total) * 100);
                const pctProf = Math.round((nProf / total) * 100);
                chartDist.innerHTML = `
                    <div class="bar-row"><span>Alunos</span><div class="bar-track"><div class="bar-fill" style="width:${pctAlunos}%"></div></div><span>${pctAlunos}%</span></div>
                    <div class="bar-row"><span>Professores</span><div class="bar-track"><div class="bar-fill" style="width:${pctProf}%"></div></div><span>${pctProf}%</span></div>
                `;
            }

            const reviewText = document.getElementById('review-text');
            if (reviewText) {
                const listT = Array.isArray(turmas) ? turmas : [];
                let somaOcup = 0;
                listT.forEach(t => { somaOcup += ((t.capacidadeAtual || 0) / (t.capacidadeMax || 1)) * 100; });
                const ocupacaoMedia = listT.length ? Math.round(somaOcup / listT.length) : 0;
                reviewText.innerHTML = `
                    <strong>Resumo do sistema:</strong> O SGE possui atualmente <strong>${nAlunos} alunos</strong> e <strong>${nProf} professores</strong>,
                    distribuídos em <strong>${nTurmas} turmas</strong> e <strong>${nDisc} disciplinas</strong>.
                    Há <strong>${nMat} matrículas</strong> ativas e <strong>${nNotas} registros de notas</strong>.
                    A ocupação média das turmas é de <strong>${ocupacaoMedia}%</strong>.
                    Use o menu à esquerda para gerenciar cada módulo.
                `;
            }
        } catch (e) {
            Object.values(ids).forEach(id => set(id, 'ERR'));
            const reviewText = document.getElementById('review-text');
            if (reviewText) reviewText.textContent = 'Erro ao carregar dados da API. Verifique se o backend está rodando.';
        }
    },

    mostrarLoading(sec, show) {
        const el = document.getElementById(`loading-${sec}`);
        if (el) el.style.display = show ? 'block' : 'none';
    },

    fecharPainel() {
        this.panel.classList.remove('open');
        this.editId = null;
    },

    // ==================== PROFESSORES ====================
    async carregarProfessores() {
        const grid = document.getElementById('grid-professores');
        this.mostrarLoading('professores', true);
        grid.innerHTML = '';
        try {
            const lista = await ProfessorService.listarTodos();
            this.mostrarLoading('professores', false);
            if (!lista || lista.length === 0) {
                grid.innerHTML = '<p class="empty-msg">Nenhum professor cadastrado.</p>';
                return;
            }
            grid.innerHTML = lista.map(p => `
                <div class="card">
                    <h3>${p.nome || 'N/A'}</h3>
                    <p><i class="fas fa-phone"></i> ${p.telefone || '-'}</p>
                    <p><i class="fas fa-book"></i> ${p.disciplina ? '1 disciplina' : '0 disciplinas'}</p>
                    <div class="card-actions">
                        <button class="action-btn" onclick="adm.abrirFormProfessor(${p.id})"><i class="fas fa-pen"></i> Editar</button>
                        <button class="action-btn delete" onclick="adm.deletarProfessor(${p.id})"><i class="fas fa-trash"></i> Excluir</button>
                    </div>
                </div>
            `).join('');
        } catch (e) {
            this.mostrarLoading('professores', false);
            grid.innerHTML = `<p class="empty-msg" style="color:#ef4444;">Erro ao carregar: ${e.message}</p>`;
        }
    },

    abrirFormProfessor(id) {
        this.editId = id;
        this.formTitle.textContent = id ? 'Editar Professor' : 'Novo Professor';
        this.formContent.innerHTML = `
            <form id="formProfessor">
                <div class="input-group"><label>Nome</label><input type="text" id="prof-nome" required></div>
                <div class="input-group"><label>Telefone</label><input type="text" id="prof-telefone"></div>
                <div class="panel-actions">
                    <button type="button" class="btn-cancel" onclick="adm.fecharPainel()">Cancelar</button>
                    <button type="submit" class="btn-glow" style="flex:1;">Salvar</button>
                </div>
            </form>
        `;
        if (id) {
            ProfessorService.buscarPorId(id).then(p => {
                document.getElementById('prof-nome').value = p.nome || '';
                document.getElementById('prof-telefone').value = p.telefone || '';
            }).catch(() => {});
        }
        document.getElementById('formProfessor').onsubmit = async (e) => {
            e.preventDefault();
            const data = { nome: document.getElementById('prof-nome').value, telefone: document.getElementById('prof-telefone').value };
            try {
                if (this.editId) await ProfessorService.atualizar(this.editId, data);
                else await ProfessorService.criar(data);
                this.fecharPainel();
                this.carregarProfessores();
            } catch (err) { alert('Erro ao salvar: ' + err.message); }
        };
        this.panel.classList.add('open');
    },

    async deletarProfessor(id) {
        if (!confirm('Excluir este professor?')) return;
        try {
            await ProfessorService.deletar(id);
            this.carregarProfessores();
        } catch (e) { alert('Erro ao excluir: ' + e.message); }
    },

    // ==================== ALUNOS ====================
    async carregarAlunos() {
        const grid = document.getElementById('grid-alunos');
        this.mostrarLoading('alunos', true);
        grid.innerHTML = '';
        try {
            const lista = await AlunoService.listarTodos();
            console.log(lista);
            this.mostrarLoading('alunos', false);
            if (!lista || lista.length === 0) {
                grid.innerHTML = '<p class="empty-msg">Nenhum aluno cadastrado.</p>';
                return;
            }
            grid.innerHTML = lista.map(a => `
                <div class="card">
                    <h3>${a.nome || 'N/A'}</h3>
                    <p><i class="fas fa-id-card"></i> CPF: ${a.cpf || '-'}</p>
                    <p><i class="fas fa-envelope"></i> ${a.email || '-'}</p>
                    <p><i class="fas fa-calendar"></i> ${a.dataNascimento || a.dataNasci || '-'}</p>
                    <div class="card-actions">
                        <button class="action-btn" onclick="adm.abrirFormAluno('${a.cpf}')"><i class="fas fa-pen"></i> Editar</button>
                        <button class="action-btn delete" onclick="adm.deletarAluno('${a.cpf}')"><i class="fas fa-trash"></i> Excluir</button>
                    </div>
                </div>
            `).join('');
        } catch (e) {
            this.mostrarLoading('alunos', false);
            grid.innerHTML = `<p class="empty-msg" style="color:#ef4444;">Erro ao carregar: ${e.message}</p>`;
        }
    },

    abrirFormAluno(cpf) {
        this.editId = cpf;
        this.formTitle.textContent = cpf ? 'Editar Aluno' : 'Novo Aluno';
        this.formContent.innerHTML = `
            <form id="formAluno">
                <div class="input-group"><label>Nome</label><input type="text" id="aluno-nome" required></div>
                <div class="input-group"><label>CPF</label><input type="text" id="aluno-cpf" ${cpf ? 'readonly' : ''} required></div>
                <div class="input-group"><label>Data Nascimento</label><input type="date" id="aluno-data" required></div>
                <div class="panel-actions">
                    <button type="button" class="btn-cancel" onclick="adm.fecharPainel()">Cancelar</button>
                    <button type="submit" class="btn-glow" style="flex:1;">Salvar</button>
                </div>
            </form>
        `;
        if (cpf) {
            AlunoService.buscarPorCpf(cpf).then(arr => {
                const a = Array.isArray(arr) ? arr[0] : arr;
                if (a) {
                    document.getElementById('aluno-nome').value = a.nome || '';
                    document.getElementById('aluno-cpf').value = a.cpf || '';
                    if (a.dataNascimento) document.getElementById('aluno-data').value = a.dataNascimento.split('T')[0];
                    else if (a.dataNasci) document.getElementById('aluno-data').value = String(a.dataNasci).split('T')[0];
                }
            }).catch(() => {});
        }
        document.getElementById('formAluno').onsubmit = async (e) => {
            e.preventDefault();
            const data = {
                nome: document.getElementById('aluno-nome').value,
                cpf: document.getElementById('aluno-cpf').value,
                dataNascimento: document.getElementById('aluno-data').value
            };
            try {
                if (this.editId) await AlunoService.atualizar(this.editId, data);
                else await AlunoService.criar(data);
                this.fecharPainel();
                this.carregarAlunos();
            } catch (err) { alert('Erro ao salvar: ' + err.message); }
        };
        this.panel.classList.add('open');
    },

    async deletarAluno(cpf) {
        if (!confirm('Excluir este aluno?')) return;
        try {
            await AlunoService.deletar(cpf);
            this.carregarAlunos();
        } catch (e) { alert('Erro ao excluir: ' + e.message); }
    },

    // ==================== TURMAS ====================
    async carregarTurmas() {
        const grid = document.getElementById('grid-turmas');
        this.mostrarLoading('turmas', true);
        grid.innerHTML = '';
        try {
            const lista = await TurmaService.listarTodas();
            this.mostrarLoading('turmas', false);
            if (!lista || lista.length === 0) {
                grid.innerHTML = '<p class="empty-msg">Nenhuma turma cadastrada.</p>';
                return;
            }
            grid.innerHTML = lista.map(t => `
                <div class="card">
                    <h3>${t.nome || 'N/A'}</h3>
                    <p><i class="fas fa-users"></i> ${t.capacidadeAtual || 0} / ${t.capacidadeMax || 0}</p>
                    <p><i class="fas fa-book"></i> ${(t.disciplinaList || []).length} disciplina(s)</p>
                    <div class="card-actions">
                        <button class="action-btn" onclick="adm.abrirFormTurma(${t.id})"><i class="fas fa-pen"></i> Editar</button>
                        <button class="action-btn delete" onclick="adm.deletarTurma(${t.id})"><i class="fas fa-trash"></i> Excluir</button>
                    </div>
                </div>
            `).join('');
        } catch (e) {
            this.mostrarLoading('turmas', false);
            grid.innerHTML = `<p class="empty-msg" style="color:#ef4444;">Erro ao carregar: ${e.message}</p>`;
        }
    },

    abrirFormTurma(id) {
        this.editId = id;
        this.formTitle.textContent = id ? 'Editar Turma' : 'Nova Turma';
        this.formContent.innerHTML = `
            <form id="formTurma">
                <div class="input-group"><label>Nome</label><input type="text" id="turma-nome" required></div>
                <div class="input-group"><label>Capacidade Máxima</label><input type="number" id="turma-cap" min="1" required></div>
                <div class="panel-actions">
                    <button type="button" class="btn-cancel" onclick="adm.fecharPainel()">Cancelar</button>
                    <button type="submit" class="btn-glow" style="flex:1;">Salvar</button>
                </div>
            </form>
        `;
        if (id) {
            TurmaService.buscarPorId(id).then(t => {
                document.getElementById('turma-nome').value = t.nome || '';
                document.getElementById('turma-cap').value = t.capacidadeMax || '';
            }).catch(() => {});
        }
        document.getElementById('formTurma').onsubmit = async (e) => {
            e.preventDefault();
            const nome = document.getElementById('turma-nome').value;
            const capacidadeMax = parseInt(document.getElementById('turma-cap').value, 10);
            try {
                if (this.editId) {
                    await TurmaService.alterarNome(this.editId, nome);
                    await TurmaService.alterarCapacidade(this.editId, capacidadeMax);
                } else {
                    await TurmaService.criar({ nome, capacidadeMax });
                }
                this.fecharPainel();
                this.carregarTurmas();
            } catch (err) { alert('Erro ao salvar: ' + err.message); }
        };
        this.panel.classList.add('open');
    },

    async deletarTurma(id) {
        if (!confirm('Excluir esta turma?')) return;
        try {
            await TurmaService.deletar(id);
            this.carregarTurmas();
        } catch (e) { alert('Erro ao excluir: ' + e.message); }
    },

    // ==================== DISCIPLINAS ====================
    async carregarDisciplinas() {
        const grid = document.getElementById('grid-disciplinas');
        this.mostrarLoading('disciplinas', true);
        grid.innerHTML = '';
        try {
            const lista = await DisciplinaService.listarTodas();
            this.mostrarLoading('disciplinas', false);
            const sel = document.getElementById('select-disc-notas');
            sel.innerHTML = '<option value="">Todas</option>' + (lista || []).map(d => `<option value="${d.id}">${d.nome || d.id}</option>`).join('');
            if (!lista || lista.length === 0) {
                grid.innerHTML = '<p class="empty-msg">Nenhuma disciplina cadastrada.</p>';
                return;
            }
            grid.innerHTML = lista.map(d => `
                <div class="card">
                    <h3>${d.nome || 'N/A'}</h3>
                    <p><i class="fas fa-clock"></i> ${d.cargaHoraria || 0}h</p>
                    <div class="card-actions">
                        <button class="action-btn" onclick="adm.abrirFormDisciplina(${d.id})"><i class="fas fa-pen"></i> Editar</button>
                        <button class="action-btn delete" onclick="adm.deletarDisciplina(${d.id})"><i class="fas fa-trash"></i> Excluir</button>
                    </div>
                </div>
            `).join('');
        } catch (e) {
            this.mostrarLoading('disciplinas', false);
            grid.innerHTML = `<p class="empty-msg" style="color:#ef4444;">Erro ao carregar: ${e.message}</p>`;
        }
    },

    abrirFormDisciplina(id) {
        this.editId = id;
        this.formTitle.textContent = id ? 'Editar Disciplina' : 'Nova Disciplina';
        this.formContent.innerHTML = `
            <form id="formDisciplina">
                <div class="input-group"><label>Nome</label><input type="text" id="disc-nome" required></div>
                <div class="input-group"><label>Carga Horária</label><input type="number" id="disc-carga" min="0" step="1"></div>
                <div class="panel-actions">
                    <button type="button" class="btn-cancel" onclick="adm.fecharPainel()">Cancelar</button>
                    <button type="submit" class="btn-glow" style="flex:1;">Salvar</button>
                </div>
            </form>
        `;
        if (id) {
            DisciplinaService.buscarPorId(id).then(d => {
                document.getElementById('disc-nome').value = d.nome || '';
                document.getElementById('disc-carga').value = d.cargaHoraria || '';
            }).catch(() => {});
        }
        document.getElementById('formDisciplina').onsubmit = async (e) => {
            e.preventDefault();
            const data = {
                nome: document.getElementById('disc-nome').value,
                cargaHoraria: parseInt(document.getElementById('disc-carga').value, 10) || 0
            };
            try {
                if (this.editId) await DisciplinaService.atualizar(this.editId, data);
                else await DisciplinaService.criar(data);
                this.fecharPainel();
                this.carregarDisciplinas();
            } catch (err) { alert('Erro ao salvar: ' + err.message); }
        };
        this.panel.classList.add('open');
    },

    async deletarDisciplina(id) {
        if (!confirm('Excluir esta disciplina?')) return;
        try {
            await DisciplinaService.deletar(id);
            this.carregarDisciplinas();
        } catch (e) { alert('Erro ao excluir: ' + e.message); }
    },

    // ==================== NOTAS (Disciplina > Aluno) ====================
    async carregarNotas() {
        const tbody = document.getElementById('tbody-notas');
        const discId = document.getElementById('select-disc-notas')?.value;
        try {
            const lista = await NotaService.listarTodas();
            let notas = Array.isArray(lista) ? lista : [];
            if (discId) notas = notas.filter(n => n.disciplina && String(n.disciplina.id) === String(discId));
            tbody.innerHTML = notas.length === 0
                ? '<tr><td colspan="6" style="text-align:center;color:var(--text-muted);">Nenhuma nota cadastrada.</td></tr>'
                : notas.map(n => `
                    <tr>
                        <td>${(n.matricula && n.matricula.nome) || (n.matricula && n.matricula.aluno && n.matricula.aluno.nome) || '-'}</td>
                        <td>${(n.disciplina && n.disciplina.nome) || '-'}</td>
                        <td>${n.nota1 ?? '-'}</td>
                        <td>${n.nota2 ?? '-'}</td>
                        <td>${n.media ?? '-'}</td>
                        <td>${n.situacao ?? '-'}</td>
                    </tr>
                `).join('');
        } catch (e) {
            tbody.innerHTML = `<tr><td colspan="6" style="color:#ef4444;">Erro: ${e.message}</td></tr>`;
        }
    },

    abrirFormNota() {
        this.formTitle.textContent = 'Adicionar Nota (Disciplina > Aluno)';
        Promise.all([MatriculaService.listarTodas(), DisciplinaService.listarTodas()]).then(([matriculas, disciplinas]) => {
            const optsMat = (matriculas || []).map(m => `<option value="${m.id}">${m.nome || m.id} - ${m.aluno?.cpf || ''}</option>`).join('');
            const optsDisc = (disciplinas || []).map(d => `<option value="${d.id}">${d.nome || d.id}</option>`).join('');
            this.formContent.innerHTML = `
                <form id="formNota">
                    <div class="input-group"><label>Matrícula (Aluno)</label><select id="nota-idMat" required>${optsMat || '<option value="">Nenhuma matrícula</option>'}</select></div>
                    <div class="input-group"><label>Disciplina</label><select id="nota-idDisc" required>${optsDisc || '<option value="">Nenhuma disciplina</option>'}</select></div>
                    <div class="input-group"><label>Nota 1</label><input type="number" id="nota1" min="0" max="10" step="0.1" required></div>
                    <div class="input-group"><label>Nota 2</label><input type="number" id="nota2" min="0" max="10" step="0.1" required></div>
                    <div class="panel-actions">
                        <button type="button" class="btn-cancel" onclick="adm.fecharPainel()">Cancelar</button>
                        <button type="submit" class="btn-glow" style="flex:1;">Salvar</button>
                    </div>
                </form>
            `;
            document.getElementById('formNota').onsubmit = async (e) => {
                e.preventDefault();
                try {
                    await NotaService.adicionar(
                        parseInt(document.getElementById('nota-idMat').value, 10),
                        parseInt(document.getElementById('nota-idDisc').value, 10),
                        parseFloat(document.getElementById('nota1').value) || 0,
                        parseFloat(document.getElementById('nota2').value) || 0
                    );
                    this.fecharPainel();
                    this.carregarNotas();
                } catch (err) { alert('Erro: ' + err.message); }
            };
            this.panel.classList.add('open');
        }).catch(() => alert('Erro ao carregar dados'));
    }
};

document.addEventListener('DOMContentLoaded', () => adm.init());
window.adm = adm;
