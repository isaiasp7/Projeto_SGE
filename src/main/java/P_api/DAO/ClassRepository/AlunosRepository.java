package P_api.DAO.ClassRepository;


import P_api.Model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunosRepository extends JpaRepository<Aluno, Integer> {


    Optional<Aluno> findByCpf(String cpf);

    void deleteByCpf(String alunoCpf);
}
