package P_api.Controller;

import P_api.DAO.Services.ProfService;
import P_api.DTO.ProfDiscDTO;
import P_api.DTO.RelacionaPDRequest;
import P_api.Factory.ClassFactory;
import P_api.Model.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ProfCrud")

public class ProfessorCtrl {
    @Autowired
    ProfService profService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ProfDiscDTO>> getAll(){
        List<ProfDiscDTO> listP =profService.getAllProfessores();
        return ResponseEntity.ok(listP);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        Professor prof = profService.getProfessorById(id);
        if(prof != null){
            return ResponseEntity.ok(ClassFactory.toProfDiscDTO(prof));//retorna o dto para que ao ler disciplinas de prof não retorne para prof novamente
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não existe professor com o id correspondente");
        }

    }


    //============= CREATE =================================

    @PostMapping("/create")
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor) {//nome,telefone
        System.out.println("==================================");
        System.out.println("CONTROLLER  : "+ professor.getNome());
        System.out.println("==================================");
        Professor prof = profService.newProfessor(professor);

        return ResponseEntity.ok(prof);
    }


    @PutMapping("/updateP/{id}")
    public ResponseEntity<ProfDiscDTO> updateP(@PathVariable long id,@RequestBody Professor professor) {
        Professor prof = profService.updateProfessor(id, professor);
        return ResponseEntity.ok(ClassFactory.toProfDiscDTO(prof));//ProfDiscDTO.toProfessorDTO
    }

    //============= DELETE =================================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteProfessor(@PathVariable long id) {
        profService.removeProfessor(id);
        return ResponseEntity.ok(true);


    }
    //============= RELACIONAMENTO ========================

    @PostMapping("/AddDisciplina")
    public ResponseEntity<ProfDiscDTO> cadastrarProfessor_Disc(@RequestBody RelacionaPDRequest id) {
            Professor disc=profService.relacionaProf_Disc(id.getProfessor_id(), id.getDisciplina_id());
            return ResponseEntity.ok(ClassFactory.toProfDiscDTO(disc));
    }




}
