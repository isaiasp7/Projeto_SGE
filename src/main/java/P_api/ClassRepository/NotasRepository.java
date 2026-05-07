package P_api.ClassRepository;


import P_api.Model.Notas;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface NotasRepository extends JpaRepository<Notas, Long> {
}
