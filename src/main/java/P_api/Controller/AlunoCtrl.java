package P_api.Controller;

import P_api.DAO.Services.AlunoService;
import P_api.Model.Aluno;
import P_api.Model.Matricula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunoCrud")
//RODANDO
public class AlunoCtrl {
    @Autowired
    private AlunoService alunoS;

    //============== READ =====================

    @GetMapping("/getAll")
    public ResponseEntity<List<Aluno>> mostrarAlunos() {
        var alunos = alunoS.getAlunos();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/searchCpf/{cpf}")
    public ResponseEntity SearchCpf(@PathVariable String cpf) {
        var lista = alunoS.searchAluno(cpf);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/searchID/{id}")
    public ResponseEntity SearchId(@PathVariable int id) {
        return ResponseEntity.ok(alunoS.searchAlunoId(id));
    }




    //============== CREATE =====================

    @PostMapping("create")
    public ResponseEntity<Aluno> cadastrarAluno(@RequestBody Aluno aluno) {//cpf, nome, dataNasci
        aluno.setMatriculas(new Matricula());
        Aluno novoAl =alunoS.addAlunos(aluno);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoAl);
    }


    //================ DELETE ========================
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<String> deletarAlunoId(@PathVariable int id) {
        Aluno temp_aluno =alunoS.searchAlunoId(id);
        var AlunoNome = temp_aluno.getNome();
        boolean delBoolean = alunoS.deleteAlunoId(id);
        if (delBoolean) {
            //return ResponseEntity.ok("Aluno "+AlunoNome+" com id : "+id+" removido do sistema.");
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.ok("O id inserido não existe");
    }



    //=============== UPDATE ===========================

    @PutMapping("/updateAlunosId/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable int id, @RequestBody String email) {//aluno atualiza email
        Aluno alunoAtualizado= alunoS.updateAlunosId(id,email);
        return ResponseEntity.ok(alunoAtualizado);
    }

    @PutMapping("/updateAlunos/{cpf}")
    public ResponseEntity atualizarAluno(@PathVariable String cpf, @RequestBody Aluno aluno) {

        Aluno alunoAtualizado =alunoS.updateAlunos(cpf,aluno);
        return ResponseEntity.ok(alunoAtualizado);
    }

    //================== RELACIONAMENTO ========================







}
