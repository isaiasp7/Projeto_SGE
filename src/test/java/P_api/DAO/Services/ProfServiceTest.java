package P_api.DAO.Services;

import P_api.Model.Disciplina;
import P_api.Model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class ProfServiceTest {
    @Autowired
    ProfService profService;
    @Autowired
    DiscService discService;
    private Professor professor;

    @BeforeEach
    void setup() {
        Disciplina disciplina= new Disciplina("Clean Code", 200);
        discService.createDisciplina(disciplina);
         this.professor = new Professor("Mateus Jose", disciplina, "5588988565845");
    }

    @Test
    void newProfessor() {
        profService.newProfessor(professor);
        assertNotNull(profService.getProfessorById(professor.getId()), "Professor should be saved and retrievable");
        assertEquals(professor.getId(),profService.getProfessorById(professor.getId()));

    }

    @Test
    void getAllProfessores() {

    }

    @Test
    void getProfessorById() {
    }

    @Test
    void relacionaProf_Disc() {
    }

    @Test
    void removeProfessor() {
    }

    @Test
    void updateProfessor() {
    }
}