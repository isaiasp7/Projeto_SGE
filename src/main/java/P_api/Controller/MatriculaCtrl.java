package P_api.Controller;

import P_api.DAO.Services.AlunoService;
import P_api.DAO.Services.MatricService;
import P_api.DTO.MatriculaDTO;
import P_api.DTO.RelacionaAMRequest;
import P_api.Model.Matricula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//============================================================================

@RestController
@RequestMapping("/MatriCrud")
public class MatriculaCtrl {
    @Autowired
    private MatricService matricService;

    @Autowired
    private AlunoService alunoService;

    //============== READ ===================

    @GetMapping
    public ResponseEntity<List<MatriculaDTO>> getAll() {
        try {
            List<MatriculaDTO> mat = matricService.getMatriculas();
            if (mat.isEmpty()) {
                return ResponseEntity.noContent().build(); // Retorna 204 se a lista estiver vazia
            }
            return ResponseEntity.ok(mat); // Retorna 200 com a lista
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 em caso de erro
        }
    }


    @GetMapping("/getID/{id}")
    public ResponseEntity SearchId(@PathVariable int id) {
        Optional<Matricula> aluno = matricService.seachID(id);
        return ResponseEntity.ok(aluno);

    }
    //================== CREATE =========================


    @PostMapping("/createM/{cpf}")
    //==============================
    //Cria uma nova Matricula para o alunoCpf sem turma
    //==============================
    public ResponseEntity<?> relacionaMatric_Aluno(@PathVariable String cpf) {//dataMatricula,status,turma
        try {
            var aluno = matricService.relacionaMA(cpf);
            return ResponseEntity.ok(aluno);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao relacionar aluno com matrícula: " + e.getMessage());
        }
    }


    //================== DELETE =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatricula(@PathVariable long id, @RequestParam(required = false) boolean deleteAluno) {//?deleteAluno=

        if (deleteAluno) {
            // Lógica para deletar a matrícula e o aluno
            var retorno =matricService.deleteMatComp(id);
            return ResponseEntity.ok(retorno);
        } else {
            // Lógica para deletar apenas a matrícula
            var retorno = matricService.deletMatricula(id);
            return ResponseEntity.ok(retorno);
        }
    }


    //================== RELACIONAMENTO =========================
    @PutMapping("/relacionaMT")
    //==============================
    //Cadastra aluno em uma turma existente por meio de matricula
    //==============================
    public ResponseEntity createMatric_Turma(@RequestBody RelacionaAMRequest relacao) {//Recebe alunoCpf, turmaID
        MatriculaDTO newMatricula = matricService.createMatricula(relacao.getAlunoCpf(),relacao.getTurmaID());
        return ResponseEntity.ok(newMatricula);
    }






}
