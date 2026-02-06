package P_api.Controller;

import P_api.DAO.Services.AlunoService;
import P_api.DTO.AlunoDTO;
import P_api.Model.Aluno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/alunoCrud")
//RODANDO
public class AlunoCtrl {
    @Autowired
    private AlunoService alunoS;

    //============== READ =====================

    @GetMapping("/getAll")
    @Operation(description = "Retorna uma lista com todos os alunos cadastrados.")
    public ResponseEntity<List<Aluno>> mostrarAlunos() {
        return ResponseEntity.ok(alunoS.getAlunos());
    }

    @GetMapping("/searchCpf/{cpf}")
    @Operation(description = "Busca alunos pelo CPF (suporta busca parcial).")
    public ResponseEntity<Aluno> searchCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(alunoS.searchAluno(cpf));
    }

    @GetMapping("/searchID/{id}")
    @Operation(description = "Busca um aluno específico pelo seu ID único.")
    public ResponseEntity<Aluno> SearchId(@PathVariable int id) {
        return ResponseEntity.ok(alunoS.searchAlunoId(id));
    }

//============== CREATE =====================

    @PostMapping("create")
    @Operation(description = "Cadastra um novo aluno no sistema. Requer CPF, nome e data de nascimento.")
    public ResponseEntity<Aluno> cadastrarAluno(@RequestBody Aluno aluno) {
        Aluno novoAl = alunoS.addAlunos(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAl);
    }

//================ DELETE ========================

    @DeleteMapping("/delete/id/{id}")
    @Operation(description = "Remove um aluno do sistema usando seu ID como referência.")
    public ResponseEntity<String> deletarAlunoId(@PathVariable int id) {
        boolean delBoolean = alunoS.deleteAlunoId(id);
        if (delBoolean) {
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.ok("O id inserido não existe");
    }

//=============== UPDATE ===========================

    @PutMapping("/updateAlunosId/{id}")
    @Operation(description = "Atualiza o email de um aluno específico usando seu ID.")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable int id, @RequestBody String email) {
        Aluno alunoAtualizado = alunoS.updateAlunoEmail(id, email);
        return ResponseEntity.ok(alunoAtualizado);
    }

    @PutMapping("/updateAlunos/{cpf}")
    @Operation(description = "Atualiza múltiplos dados de um aluno usando seu CPF como referência.")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable String cpf, @RequestBody AlunoDTO aluno) {
        Aluno alunoAtualizado = alunoS.updateAlunos(cpf, aluno);
        return ResponseEntity.ok(alunoAtualizado);
    }
    //================== RELACIONAMENTO ========================







}
