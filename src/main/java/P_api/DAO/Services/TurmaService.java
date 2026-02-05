package P_api.DAO.Services;

import P_api.DAO.ClassRepository.TurmasRepository;
import P_api.DTO.TurmaDTO;
import P_api.Model.Turma;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurmaService {
    @Autowired
    TurmasRepository turmaRepository;
    @Autowired
    private TurmasRepository turmasRepository;


    public List<TurmaDTO> getTurmas() {
        List<TurmaDTO> sala =turmasRepository.findAll()
                .stream()
                .map(TurmaDTO::new) // Converte cada Turma para TurmaDTO
                .collect(Collectors.toList());
        return sala;
    }
    public Turma getByID(long id) {
        Turma sala =turmaRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Matricula não encontrada"));;
        return sala;
    }



    public Turma createTurma(Turma turma) {

        System.out.println("=======================");
        System.out.println(turma.getNome());
        System.out.println(turma.getCapacidadeMax());
        System.out.println("=======================");
        return turmasRepository.save(turma);

    }

    public Turma alterarCapacidadeA(int id, int novaCapacidadeM) {
        // Encontrar a sala pelo id
        Optional<Turma> salaOptional = turmasRepository.findById((long) id);

        if (salaOptional.isPresent()) {
            Turma sala = salaOptional.get();
            // Atualiza a capacidade
            sala.setCapacidadeMax(novaCapacidadeM);

            // Salva as alterações no banco
            turmasRepository.save(sala);
            return sala;
        } else {
            throw new RuntimeException("Sala não encontrada!");
        }
    }
    //============================
    //O spring trata de maneira default os erros de sql
    //******************** TRATAR ERRO DE REPETIÇÃO DE NOME *********************
    //============================
   public Object alterarNome(long id, String nome) {
        Turma turma = this.getByID(id);
        try{
            turma.setNome(nome);
            turmaRepository.save(turma);
            return turma;
            } catch (DataIntegrityViolationException e) {
             return "Já existe um turma com o nome " + nome + "!";
            }


       }

    public Boolean deleteTurma(long id) {
        Turma turma = this.getByID(id);
        if(turma != null){
            turmaRepository.delete(turma);
            return true;
        }
        return false;

    }




}
