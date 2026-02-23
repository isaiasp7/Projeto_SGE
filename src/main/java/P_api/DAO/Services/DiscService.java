package P_api.DAO.Services;

import P_api.DAO.ClassRepository.DisciplinasRepository;
import P_api.DAO.ClassRepository.ProfessoresRepository;
import P_api.DAO.ClassRepository.TurmasRepository;
import P_api.Model.Disciplina;
import P_api.Model.Professor;
import P_api.Model.Turma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static Util.Utilities.reflexao;

@Service
public class DiscService {
    @Autowired
    private DisciplinasRepository discRepository;
    @Autowired
    private TurmasRepository turmasRepository;
    @Autowired
    private ProfessoresRepository profRepository;


    public List<Disciplina> getAllDisciplinas() {
        return discRepository.findAll();
    }

    public Disciplina createDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória");
        }
        if (disciplina.getNome() == null || disciplina.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da disciplina é obrigatório");
        }
        if (disciplina.getCargaHoraria() == null) {
            throw new IllegalArgumentException("Carga horária da disciplina é obrigatória");
        }
        Disciplina novaD = new Disciplina(disciplina);
        discRepository.save(novaD);
        return novaD;
    }

    public Disciplina getDisciplinaById(long id) {
        if (id <= 0) {
            return null;
        }
        Optional<Disciplina> disc = discRepository.findById(id);
        if (disc.isPresent()) {
            return disc.get();
        } else {
            return null;
        }

    }

    public void deleteDisciplinaById(int id) {
        if (id <= 0) {
            return;
        }
        Optional<Disciplina> disc = discRepository.findById((long) id);
        if (disc.isPresent()) {
            discRepository.delete(disc.get());
        } else {
            System.out.println("id disc service " + id);
        }
    }


    public Disciplina relacionaDT(long idDisciplina, long idTurma) {
        if (idDisciplina <= 0 || idTurma <= 0) {
            return null;
        }
        Disciplina disc = this.getDisciplinaById(idDisciplina);
        Turma turma = turmasRepository.findById(idTurma).orElse(null);
        if (disc != null && turma != null) {
            disc.setTurma(turma);
            discRepository.save(disc);
            return disc;
        }
        return null;
    }


    //==================
    //Problema: nome é necessario para associar prof a disc
    //====================
    public Disciplina updateDisciplina( long id, Disciplina alterDiscService) {
        if (alterDiscService == null) {
            throw new IllegalArgumentException("Alterações da disciplina são obrigatórias");
        }
        Disciplina disc = this.getDisciplinaById(id);
        if (disc == null) {
            return null;
        }

        try {
            for (Map.Entry<String, Object> camposAlter : reflexao(alterDiscService).entrySet()) {
                String key = camposAlter.getKey();
                Object val = camposAlter.getValue();
                Field field = Disciplina.class.getDeclaredField(key);//pega o atributo
                field.setAccessible(true);//isso é usado para dizer que mesmo o atributo sendo privado posso acessa-lo
                if ("id".equals(key)) {//O HIBERNATE PODE ESTAR GERANDO UM NOVO IID  PARA DISCIPLNA POR CONTA DO ID DO PROFESSOR PASSADO
                    /*System.out.println("=====================");
                    System.out.println("ENCONTREI O ID, MAS DEIXEI QUIETO => "+key+" : "+val);
                    System.out.println("=====================");*/
                    continue;
                }
                if ("professor".equals(key)) {//veriica se o campo é professor
                    System.out.println("=====================");
                    System.out.println("PROFESSOR FOI PASSADO");
                    System.out.println("=====================");
                    Professor professor = (Professor) val;
                    if (professor == null || profRepository.findById(professor.getId()).isEmpty()) {//verifica se professor não existe
                        continue;
                    }else{
                        System.out.println("=====================");
                        System.out.println("ELE EXISTE");
                        System.out.println("=====================");
                        Professor profExistente = profRepository.findById(professor.getId()).orElse(null);
                        if (profExistente == null) {
                            continue;
                        }
                        profExistente.setDisciplina_fk(disc);//garantindo que a tabela prof tera a disciplina
                        profRepository.save(profExistente);
                        //profService.updateProf(prof) seria necessario se não tivevesse @OneToOne(cascade = CascadeType.ALL) no relacionamento para  professor receber a disciplina sua tabela tbm
                    }
                }
                field.set(disc, val);

            }

            discRepository.save(disc);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return disc;
    }
    /*
   public Disciplina relacionaDT(long idDisciplina, long idTurma) {
       Disciplina disc = this.getDisciplinaById(idDisciplina);
       Turma turma = turmasRepository.findById(idTurma).orElseThrow(() -> new RuntimeException("Turma não encontrada"));

       if (disc != null) {
           disc.setTurma(turma); // Relaciona a disciplina com a turma
           discRepository.save(disc); // Salva a relação no banco
           return disc;
       }

       throw new RuntimeException("Disciplina não encontrada");
   }*/


}
