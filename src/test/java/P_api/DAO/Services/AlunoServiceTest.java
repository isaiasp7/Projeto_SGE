package P_api.DAO.Services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import P_api.DAO.ClassRepository.AlunosRepository;
import P_api.Model.Aluno;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
class AlunoServiceTest {

    @Autowired
    private AlunoService alunoService;
    @Autowired
    private AlunosRepository alunoRepository;

    @Test
    public void deve_adicionar_aluno() {
        Aluno aluno = new Aluno("02356987452","jorge paulo",new Date(12/05/2005));

        this.alunoService.saveAlunos(aluno);

        assertTrue(alunoRepository.findByCpf("02356987452").isPresent());

    }
    @Test
    public void deve_retornar_alunos() {

        Aluno aluno = new Aluno("02543987452","jorge paulo",new Date(12/05/2005));
        Aluno aluno2 = new Aluno("07866987452","jorge paulo",new Date(12/05/2005));
        Aluno aluno3= new Aluno("01345987452","jorge paulo",new Date(12/05/2005));
        Aluno aluno4 = new Aluno("67856987452","jorge paulo",new Date(12/05/2005));
        Aluno[] teste = {aluno,aluno2,aluno3,aluno4};
        Arrays.stream(teste).forEach(alunoService::saveAlunos);
        int quantidade = alunoService.getAlunos().size();
        System.out.println();
        assertEquals(3, quantidade);

    }

    @Test
    public void deve_deletar_alunoCPF() {
        Aluno aluno = new Aluno("02356987452","jorge paulo",new Date(9091988));

        this.alunoService.saveAlunos(aluno);

        assertTrue(alunoRepository.findByCpf("02356987452").isPresent());

    }
    @Test
    public void deve_atualizar_alunoCPf() {
        Aluno aluno = new Aluno("02356987452","jorge paulo",new Date(9091988));

        this.alunoService.saveAlunos(aluno);

        assertTrue(alunoRepository.findByCpf("02356987452").isPresent());

    }
}