package P_api.ClassRepository;


import P_api.Model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinasRepository  extends JpaRepository<Disciplina, Long> {

}
