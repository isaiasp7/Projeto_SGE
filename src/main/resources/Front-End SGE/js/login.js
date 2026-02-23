const BASE_URL = 'http://localhost:8080';

const Login = {
    async autenticar() {
        const usuarioInput = document.getElementById('login-usuario');
        const senhaInput = document.getElementById('login-senha');
        const perfilSelect = document.getElementById('login-perfil');

        const usuario = usuarioInput.value.trim();
        const senha = senhaInput.value.trim();
        const perfil = perfilSelect.value;

        if (!usuario || !senha) {
            alert('Informe usuário e senha.');
            return;
        }

        try {
            const response = await fetch(`${BASE_URL}/SGE/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ usuario, senha, perfil })
            });

            if (!response.ok) {
                if (response.status === 401) {
                    alert('Credenciais inválidas. Verifique usuário, senha e perfil.');
                    return;
                }
                throw new Error(`Erro HTTP: ${response.status}`);
            }

            const data = await response.json();
            const perfilResposta = (data.perfil || perfil).toUpperCase();

            if (perfilResposta === 'ADMIN') {
                window.location.href = '../adm/dashboard.html';
            } else if (perfilResposta === 'PROFESSOR') {
                window.location.href = '../professor/lista-alunos.html';
            } else if (perfilResposta === 'ALUNO') {
                window.location.href = '../aluno/lista-alunos.html';
            } else {
                alert('Perfil não reconhecido pelo sistema.');
            }
        } catch (error) {
            console.error('[Login] Erro ao autenticar:', error);
            alert('Erro ao conectar com o servidor de autenticação.');
        }
    },

    init() {
        const btnLogin = document.getElementById('btn-login');
        const linkCadastro = document.getElementById('link-cadastro');
        const linkEsqueci = document.getElementById('link-esqueci-senha');

        if (btnLogin) {
            btnLogin.addEventListener('click', () => this.autenticar());
        }

        if (linkCadastro) {
            linkCadastro.addEventListener('click', (e) => {
                e.preventDefault();
                alert('Para cadastro de novos alunos ou professores, acesse a área administrativa do sistema.');
            });
        }

        if (linkEsqueci) {
            linkEsqueci.addEventListener('click', (e) => {
                e.preventDefault();
                const perfilSelect = document.getElementById('login-perfil');
                const perfil = perfilSelect.value;
                if (perfil === 'ALUNO') {
                    alert('Senha padrão do aluno: Aluno2026');
                } else if (perfil === 'PROFESSOR') {
                    alert('Senha padrão do professor: Professor2026');
                } else {
                    alert('Entre em contato com o administrador para redefinir a senha.');
                }
            });
        }
    }
};

document.addEventListener('DOMContentLoaded', () => Login.init());

