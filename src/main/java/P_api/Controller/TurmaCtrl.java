package P_api.Controller;

import P_api.Services.TurmaService;
import P_api.DTO.TurmaDTO;
import P_api.Model.Turma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/TurmaCrud")
public class TurmaCtrl {
    @Autowired
    private TurmaService turmaService;


    @GetMapping("/getAll")
    public ResponseEntity<List<TurmaDTO>> getAll(){
       var local= turmaService.getTurmas();
        return ResponseEntity.ok(local);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable long id){
        Turma turma = turmaService.getByID(id);
        return ResponseEntity.ok(turma);
    }

    //================ CREATE ====================

    @PostMapping("createT")
    public ResponseEntity<Turma> createTurma(@RequestBody Turma turma) {//nome, capacidadeMax
        Turma Novaturma = turmaService.createTurma(turma);
        return ResponseEntity.ok(Novaturma);
    }



    @PutMapping("/alterCapacidade/{id}&{cap}")
    public ResponseEntity<?> alterCapAc(@PathVariable int id, @PathVariable int capM) {
        Turma turma =turmaService.alterarCapacidadeA(id,capM);
        return ResponseEntity.ok(turma);

    }
    @PutMapping("/alterNomes/{id}")
    public ResponseEntity<?> alterNome(@PathVariable long id, @RequestParam String turmaN) {
        Object turma = turmaService.alterarNome(id,turmaN);
        return ResponseEntity.ok(turma);

    }

    @DeleteMapping("deleteTurm")
    public ResponseEntity<?> deleteTurm(@RequestParam long id) {
        Boolean bool = turmaService.deleteTurma(id);
        return ResponseEntity.ok(bool?"turma excluida":"Turma não encontrada");

    }
}
