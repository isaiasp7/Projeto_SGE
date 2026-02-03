package P_api.DAO.Services;


import P_api.DAO.ClassRepository.AlunosRepository;
import P_api.Model.Aluno;
import P_api.Model.Matricula;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class AlunoService {
    @Autowired
    private AlunosRepository alunoRepository;
    @Autowired
    private MatricService matricService;//Alguns elementos de aluno estao em matricula = id,turma, notas...

    public List<Aluno> getAlunos() {
        return alunoRepository.findAll();
    }
    //====================================

    public Aluno searchAluno(String cpf) {
        Optional<Aluno> aluno = alunoRepository.findByCpf(cpf);
        if (aluno.isPresent()) {
            return aluno.get();
        }
        else{
            return aluno.orElse(null);
        }

    }

    public Aluno searchAlunoId(int matriculaId) {
        Matricula matricula = matricService.seachID(matriculaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));

        return matricula.getAluno_cpf(); // Retorna o aluno relacionado à matrícula
    }



    //=======================================================


    public Aluno addAlunos(Aluno aluno) {
        Aluno novoAluno = new Aluno(aluno);
        return alunoRepository.save(novoAluno); // Retorna o aluno cadastrado
    }

    //=====================UPDATE============================

    public void saveAlunos(Aluno aluno) {
        alunoRepository.save(aluno);
    }


    public Aluno updateAlunosId(int id, String email) {
        Aluno alunoExistente = this.searchAlunoId(id);

        try {

            // Não faz sentido aluno atualizar campos de cadastro
            if (email != null) {
                alunoExistente.setEmail(email);
            }else{
                throw new RuntimeException("Email não foi passado");
            }

            return alunoRepository.save(alunoExistente);
        } catch (Exception e) {
            throw new RuntimeException("Aluno não encontrado");
        }


    }


    public Aluno updateAlunos(String cpf, Aluno alunoAtualizado) {
        Aluno alunoExistente = this.searchAluno(cpf);

        try {

            // Não faz sentido aluno atualizar campos de cadastro
            if (alunoAtualizado.getEmail() != null) {
                alunoExistente.setEmail(alunoAtualizado.getEmail());
            }
            if(alunoAtualizado.getQuant_faltas()!=null) {
                alunoExistente.setQuant_faltas(alunoAtualizado.getQuant_faltas());
            }
            alunoRepository.save(alunoExistente);
            return alunoExistente;
        } catch (Exception e) {
             throw new RuntimeException("Aluno não encontrado");
        }


    }

    //======================DELETE===========================


    public boolean deleteAlunoId(int matriculaId) {
        if(this.searchAlunoId(matriculaId) != null) {
            alunoRepository.deleteById(matriculaId);
            return true;
        }else{
            return false;
        }

    }
   /* public boolean deleteAlunoCpf(String alunoCpf) {
        if(this.searchAluno(alunoCpf) != null) {
            alunoRepository.deleteByCpf(alunoCpf);
            return true;
        }else{
            return false;
        }

    }*/

    //====================== RELACIONAMENTO ===========================




}
