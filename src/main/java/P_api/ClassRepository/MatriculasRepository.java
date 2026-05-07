package P_api.ClassRepository;


import P_api.Model.Matricula;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MatriculasRepository extends JpaRepository<Matricula, Integer> {

}
