package P_api.Controller;

import P_api.DAO.Services.DiscService;
import P_api.DTO.RelacionaDTRequest;
import P_api.Model.Disciplina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/DisciCrud")
//NÃO TESTADO
public class DisciplinaCtrl {
   @Autowired
    DiscService discService;

   @GetMapping("/getAll")
   public ResponseEntity<List<Disciplina>> getAll(){
       List<Disciplina> disc = discService.getAllDisciplinas();
       return ResponseEntity.ok(disc);
   }


   @GetMapping("/search/{id}")
    public ResponseEntity<Disciplina> searchDisciplina(@PathVariable int id) {
        discService.getDisciplinaById(id);
        return ResponseEntity.ok(discService.getDisciplinaById(id));
    }


   @PostMapping("/create")
    public ResponseEntity<Disciplina> createDisc(@RequestBody Disciplina disciplina) {//nome,cargaHoraria
       Disciplina disc = discService.createDisciplina(disciplina);
       return ResponseEntity.ok(disc);
   }

   @PutMapping("/updateDisc/{id}")
   public ResponseEntity<Disciplina> updateDisc(@PathVariable long id,@RequestBody Disciplina alterDisc) {// nome,cargaHoraria,professor(ainda não testado = notas,turma)
       Disciplina disc = discService.updateDisciplina(id,alterDisc);
       return ResponseEntity.ok(disc);
   }




   @DeleteMapping("/delete")
    public ResponseEntity<String>  deleteDisciplina(@RequestParam int id) {
       discService.deleteDisciplinaById(id);
       return ResponseEntity.ok("Deleted Disciplina");
   }

   //=============== RELACIONAMENTOS ======================================

    @PutMapping("/relacionaDT")//discpilna e turma
    public ResponseEntity relacioDisc_Turm(@RequestBody RelacionaDTRequest lista) {//Recebe idturma a idDisciplina
       Disciplina relacionamento =discService.relacionaDT(lista.getIdDisciplina(), lista.getIdTurma());
        return ResponseEntity.ok(relacionamento);
   }
}
