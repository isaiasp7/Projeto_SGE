package P_api.DAO.Services;

import P_api.DTO.AlunoDTO;
import P_api.DTO.MatriculaDTO;
import P_api.Exceptions.Erros.EntidadeNaoEncontrada;
import P_api.Model.Aluno;
import P_api.Model.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AlunoServiceTest {

    @Autowired
    private AlunoService alunoService;
    private Aluno aluno;
    @Autowired
    private TurmaService turmaService;
    @Autowired
    private MatricService matricService;


    @BeforeEach
    void setUp() {
        this.aluno = new Aluno(
                "02543987452",
                "Thomas",
                Date.from(
                        LocalDate.of(2005, 5, 12)
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                )
        );

    }

    @Test
    public void deve_pesquisar_alunoID() {
        alunoService.saveAlunos(aluno);
        Turma t = new Turma("Geografia",40);
        turmaService.createTurma(t);

       MatriculaDTO m = matricService.createMatricula(this.aluno.getCpf(),t.getId());
        System.out.println();
       assertNotNull(alunoService.searchAlunoId(m.getId()));



    }
    @Test
    public void deve_pesquisar_alunoCPF() {
        this.alunoService.saveAlunos(aluno);
        assertEquals("02543987452",alunoService.searchAluno(this.aluno.getCpf()).getCpf()); ;
    }
    @Test
    public void deve_adicionar_aluno() {//save
        this.alunoService.saveAlunos(aluno);
        assertNotNull(alunoService.searchAluno("02543987452"));


    }

    @Test
    void deve_retornar_alunos() {

        Aluno aluno2 = new Aluno("07866987452","jorge paulo",Date.from(
                LocalDate.of(2015, 5, 12)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        ));
        Aluno aluno3= new Aluno("01345987452","carlos",Date.from(
                LocalDate.of(1995, 5, 12)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        ));
        Aluno aluno4 = new Aluno("67856987452"," paulo",Date.from(
                LocalDate.of(2011, 5, 12)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        ));

            Aluno[] esperados = {aluno, aluno2, aluno3, aluno4};

            Arrays.stream(esperados).forEach(alunoService::saveAlunos);

            List<Aluno> alunosBanco = alunoService.getAlunos();

            assertEquals(4, alunosBanco.size());

            Set<String> cpfsEsperados = Arrays.stream(esperados)
                    .map(Aluno::getCpf)
                    .collect(Collectors.toSet());

            Set<String> cpfsBanco = alunosBanco.stream()
                    .map(Aluno::getCpf)
                    .collect(Collectors.toSet());

            assertEquals(cpfsEsperados, cpfsBanco);
        }



    @Test
    public void deve_deletar_aluno() {//
        this.alunoService.saveAlunos(aluno);
        alunoService.deleteAlunoCpf(aluno.getCpf());
        assertThrows(EntidadeNaoEncontrada.class, () ->alunoService.searchAluno("02543987452"));

    }
    @Test
    public void deve_atualizar_aluno() {
        this.alunoService.saveAlunos(aluno);
        assertNotNull(alunoService.searchAluno("02543987452"));

       assertNotNull(alunoService.updateAlunos(aluno.getCpf(), new AlunoDTO(5)));//verifica se os dados foram
        // atualizados
        assertEquals(5,alunoService.searchAluno("02543987452").getQuant_faltas());//verifica se o antigo aluno agora
        // possui quantidade de faltas = 5
    }

    //======================================= ERROS / EXECEPTION ================================================

    @Test
    public void Empty_getAlunos() {//lista vazia
        assertEquals(0,alunoService.getAlunos().size());

    }
    @Test
    public void exeception_searchCPF() {//lista vazia retornar um AlunoNaoEncontrado
        assertThrows(
                EntidadeNaoEncontrada.class,
                () -> alunoService.searchAluno("02543987452")
        );

    }
    @Test
    public void exeception_searchID() {//
        assertThrows(
                EntidadeNaoEncontrada.class,
                () -> alunoService.searchAlunoId(12315)
        );

    }
    /*@Test
    public void exeception_updateAlunoEmail(){//E NECESSÁRIO VALIDA EMAIL
        aluno.setMatriculas(new Matricula());
        alunoService.saveAlunos(aluno);
        assertThrows(RuntimeException.class,
                ()-> alunoService.updateAlunosId(aluno.getMatriculas().getId(),"")
        );
    }*/


}