package P_api.Controller;

import P_api.DAO.Services.AlunoService;
import P_api.DAO.Services.ProfService;
import P_api.DTO.LoginRequest;
import P_api.DTO.LoginResponse;
import P_api.Model.Aluno;
import P_api.Model.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("SGE")
public class Login {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ProfService profService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> autenticar(@RequestBody LoginRequest request) {
        String perfil = request.getPerfil() != null ? request.getPerfil().toUpperCase() : "";
        String usuario = request.getUsuario();
        String senha = request.getSenha();

        if (usuario == null || usuario.isBlank() || senha == null || senha.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Credenciais obrigatórias não informadas", null));
        }

        try {
            switch (perfil) {
                case "ALUNO": {
                    Aluno aluno = alunoService.searchAluno(usuario);
                    if (aluno != null && senha.equals(aluno.getSenha())) {
                        return ResponseEntity.ok(new LoginResponse(true, null, "ALUNO"));
                    }
                    break;
                }
                case "PROFESSOR": {
                    long idProf;
                    try {
                        idProf = Long.parseLong(usuario);
                    } catch (NumberFormatException e) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new LoginResponse(false, "ID de professor inválido", null));
                    }
                    Professor professor = profService.getProfessorById(idProf);
                    if (professor != null && senha.equals(professor.getSenha())) {
                        return ResponseEntity.ok(new LoginResponse(true, null, "PROFESSOR"));
                    }
                    break;
                }
                case "ADMIN": {
                    if ("admin".equalsIgnoreCase(usuario) && "Admin2026".equals(senha)) {
                        return ResponseEntity.ok(new LoginResponse(true, null, "ADMIN"));
                    }
                    break;
                }
                default:
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new LoginResponse(false, "Perfil de usuário inválido", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Falha na autenticação", null));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(false, "Usuário ou senha inválidos", null));
    }
}
