package P_api.DAO.Services;

import P_api.DTO.ProfDiscDTO;
import P_api.Exceptions.Erros.FalhaRelacionamento;
import P_api.Model.Aluno;
import P_api.Model.Disciplina;
import P_api.Model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

         this.professor = new Professor("Mateus Jose", "5588988565845");
    }

    @Test
    void newProfessor() {
        Professor salvo = profService.newProfessor(professor);

        assertNotNull(salvo.getId()); // o save funcionou
    }


    @Test
    void getAllProfessores() {
        Professor professor1 = new Professor("messias", new Disciplina("Logic", 200), "5588988565845");
        Professor professor2 = new Professor("maduro",  new Disciplina("MAth", 200), "5588988565845");
        Professor professor3 = new Professor("carlos",  new Disciplina("philo", 200), "5588988565845");
        Professor professo4 = new Professor("jose",  new Disciplina("hardware", 200), "5588988565845");
        Professor[] professores = {professor1,professor2,professor3, professo4};
        Arrays.stream(professores).forEach(profService::newProfessor);
        assertEquals(professores.length,profService.getAllProfessores().size());

        List<ProfDiscDTO> profBanco = profService.getAllProfessores();

        Set<String> telefoneEsperados = Arrays.stream(professores)
                .map(Professor::getTelefone)
                .collect(Collectors.toSet());

        Set<String> telefoneBanco = profBanco.stream()
                .map(ProfDiscDTO::getTelefone)
                .collect(Collectors.toSet());

        assertEquals(telefoneEsperados, telefoneBanco);

    }

    @Test
    void getProfessorById() {
        Professor salvo = profService.newProfessor(professor);
        assertNotNull(profService.getProfessorById(salvo.getId()));
    }

    @Test
    void relacionaProf_Disc() {
        Disciplina disSalva = discService.createDisciplina(new Disciplina("Clean Code", 200));
        Professor profSalvo = profService.newProfessor(new Professor("ronaldo", "5588998874455"));
        assertNotNull( profService.relacionaProf_Disc(profSalvo.getId(), disSalva.getId()));

    }

    @Test
    void removeProfessor() {
        this.professor = profService.newProfessor(professor);
        profService.removeProfessor(professor.getId());
        assertNull(profService.getProfessorById(professor.getId()));
    }

    @Test
    void updateProfessor() {
        Professor salvo = profService.newProfessor(professor);

        profService.updateProfessor(
                salvo.getId(),
                new Professor("nome atualizado", "000000000")
        );

        Professor atualizado = profService.getProfessorById(salvo.getId());

        assertEquals("nome atualizado", atualizado.getNome());
        assertEquals("000000000", atualizado.getTelefone());
    }

    //======================================= ERROS / EXECEPTION ================================================

    @Test
    void newProfessorErro() {
        assertNull(profService.getProfessorById(professor.getId())); //
    }
    @Test
    void relacionaProf_DiscExecption() {
        Professor profSalvo = profService.newProfessor(new Professor("ronaldo", "5588998874455"));
        assertThrows(FalhaRelacionamento.class,
                ()-> profService.relacionaProf_Disc(profSalvo.getId(), 651651));

    }


}