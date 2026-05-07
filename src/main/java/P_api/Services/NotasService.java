package P_api.Services;

import P_api.ClassRepository.DisciplinasRepository;
import P_api.ClassRepository.MatriculasRepository;
import P_api.ClassRepository.NotasRepository;
import P_api.DTO.NotasDTO;
import P_api.DTO.RelacionaNotas;
import P_api.Model.Disciplina;
import P_api.Model.Matricula;
import P_api.Model.Notas;
import Util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Util.Utilities.reflexao;

@Service
public class NotasService {
    @Autowired
    private MatriculasRepository matricRepository;

    @Autowired
    private NotasRepository notasRepository;
    @Autowired
    private DisciplinasRepository disciplinasRepository;

    //add notas a uma matricula 1/2 @RequestParam(required = false)
    //alterar notas pelo id da nota  1/2
    //mostrar notas de um aluno
    public List<NotasDTO> mostrarNotas(){


        List<NotasDTO> nota=notasRepository.findAll()
                .stream()
                .map(NotasDTO::new) // Converte cada Turma para TurmaDTO
                .collect(Collectors.toList());
        return nota;

    }
    public NotasDTO buscarNotas(long idNota) {
        Notas n = notasRepository.findById(idNota).orElseThrow(() -> new Utilities.NotFoundException("Nota com ID " + idNota + " não encontrada"));;
        return new NotasDTO(n);
    }

    public Matricula newNotas(long idMat,long idDisc, float nota1,float nota2){
        Matricula mat = matricRepository.findById((int)idMat).orElseThrow(null);
        Disciplina disc = disciplinasRepository.findById(idDisc).orElseThrow(null);
        if(mat != null && disc != null){

            Notas notas = new Notas(mat,disc, nota1, nota2);
            notasRepository.save(notas);
            mat.setNotas(notas);
        }
        matricRepository.save(mat);
        return mat;
    }

    public RelacionaNotas updateNotas(long id, RelacionaNotas alterNota){
        System.out.println("=====================");
        System.out.println(id);
        System.out.println("=====================");
        Notas n = notasRepository.findById(id).orElseThrow(() -> new Utilities.NotFoundException("Nota com ID " + id + " não encontrada"));

        for (Map.Entry<String, Object> camposAlter : reflexao(alterNota).entrySet()) {
            String key = camposAlter.getKey();
            Object val = camposAlter.getValue();
            if(key.equals("nota1")){
                n.setNota1((Float)val);
            }
            else if(key.equals("nota2")){
                n.setNota2((Float)val);
            }

        }
        n.setMedia();
        notasRepository.save(n);
        return new RelacionaNotas(n);

    }



    public void removerNotas(long id){
        notasRepository.deleteById(id);

    }


}
