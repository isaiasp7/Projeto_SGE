package P_api.DAO.Services;


import P_api.DAO.ClassRepository.AlunosRepository;
import P_api.DTO.AlunoDTO;
import P_api.Exceptions.Erros.EntidadeNaoEncontrada;
import P_api.Model.Aluno;
import P_api.Model.Matricula;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AlunoService {
    @Autowired
    private AlunosRepository alunoRepository;
    @Autowired
    private MatricService matricService;//Alguns elementos de aluno estao em matricula = id,turma, notas...

    public List<Aluno> getAlunos() {//se não houver ninguem retora uma lista vazia
        return alunoRepository.findAll();
    }

    //====================================

    public Aluno searchAluno(String cpf) {
        return alunoRepository.findByCpf(cpf)
                .orElseThrow(() ->
                        new EntidadeNaoEncontrada("Aluno não encontrado para CPF: " + cpf)
                );
    }


    public Aluno searchAlunoId(long matriculaId) {
        Matricula matricula =
                matricService.seachID(matriculaId)
                        .orElseThrow(() -> new EntidadeNaoEncontrada("Aluno não encontrado"));

        return matricula.getAluno_cpf();

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


    public Aluno updateAlunoEmail(long id, String email) {//REVER - FAZ SENTIDO MUDAR O EMAIL? (REGRA DE NEGOCIO)
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


    public Aluno updateAlunos(String cpf, AlunoDTO alunoAtualizado) {
        Aluno alunoExistente = this.searchAluno(cpf);

        try {

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


    public boolean deleteAlunoId(int matriculaId) {//melhorar esse retorno
        if(this.searchAlunoId(matriculaId) != null) {
            alunoRepository.deleteById(matriculaId);
            return true;
        }else{
            return false;
        }

    }
    @Transactional
   public boolean deleteAlunoCpf(String alunoCpf) {
        try {
            if(this.searchAluno(alunoCpf) != null) {
                alunoRepository.deleteByCpf(alunoCpf);
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //====================== RELACIONAMENTO ===========================




}
