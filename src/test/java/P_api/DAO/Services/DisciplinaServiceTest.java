package P_api.DAO.Services;



import P_api.DAO.ClassRepository.DisciplinasRepository;
import P_api.Model.Disciplina;
import P_api.Model.Turma;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DisciplinaServiceTest {

    @Autowired
    private DiscService discService;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private DisciplinasRepository disciplinasRepository;



    // ============================ getAllDisciplinas ============================

    @Test
    void getAllDisciplinas_valido_deveRetornarLista() {
        discService.createDisciplina(new Disciplina("Matemática", 200));
        discService.createDisciplina(new Disciplina("Português", 180));

        List<Disciplina> todas = discService.getAllDisciplinas();
        assertEquals(2, todas.size());
    }

    @Test
    void getAllDisciplinas_invalido_aposLimpeza() {
        discService.createDisciplina(new Disciplina("História", 120));
        disciplinasRepository.deleteAll();

        assertEquals(0, discService.getAllDisciplinas().size());
    }

    // ============================ createDisciplina ============================

    @Test
    void createDisciplina_valido_devePersistirECriarCopia() {
        Disciplina entrada = new Disciplina("Geografia", 160);

        Disciplina criada = discService.createDisciplina(entrada);

        assertNotNull(criada);
        assertEquals("Geografia", criada.getNome());
        assertEquals(160, criada.getCargaHoraria());
        assertNotEquals(entrada.getId(), criada.getId()); // service cria cópia com novo ID
        assertNotNull(discService.getDisciplinaById(criada.getId()));
    }

    @Test
    void createDisciplina_invalido_disciplinaNula() {
        assertThrows(IllegalArgumentException.class, () -> discService.createDisciplina(null));
    }

    @Test
    void createDisciplina_invalido_nomeVazioOuCargaNula() {
        Disciplina semNome = new Disciplina();
        semNome.setNome("  ");
        semNome.setCargaHoraria(100);

        assertThrows(IllegalArgumentException.class, () -> discService.createDisciplina(semNome));

        Disciplina semCarga = new Disciplina();
        semCarga.setNome("Física");
        semCarga.setCargaHoraria(null);

        assertThrows(IllegalArgumentException.class, () -> discService.createDisciplina(semCarga));
    }

    // ============================ getDisciplinaById ============================

    @Test
    void getDisciplinaById_valido_deveRetornarDisciplina() {
        Disciplina criada = discService.createDisciplina(new Disciplina("Química", 140));

        Disciplina encontrada = discService.getDisciplinaById(criada.getId());

        assertNotNull(encontrada);
        assertEquals(criada.getId(), encontrada.getId());
    }

    @Test
    void getDisciplinaById_invalido_idInexistente() {
        assertNull(discService.getDisciplinaById(999999));
    }

    @Test
    void getDisciplinaById_invalido_idNaoPositivo() {
        assertNull(discService.getDisciplinaById(0));
        assertNull(discService.getDisciplinaById(-1));
    }

    // ============================ deleteDisciplinaById ============================

    @Test
    void deleteDisciplinaById_valido_deveRemover() {
        Disciplina criada = discService.createDisciplina(new Disciplina("Biologia", 150));

        discService.deleteDisciplinaById((int) criada.getId());

        assertNull(discService.getDisciplinaById(criada.getId()));
    }

    @Test
    void deleteDisciplinaById_invalido_idInexistente_naoDeveLancarExcecao() {
        assertDoesNotThrow(() -> discService.deleteDisciplinaById(123456));
    }

    // ============================ relacionaDT ============================

    @Test
    void relacionaDT_valido_deveRelacionarDisciplinaComTurma() {
        Turma turma = turmaService.createTurma(new Turma("TURMA-A", 40));
        Disciplina disc = discService.createDisciplina(new Disciplina("Artes", 80));

        Disciplina relacionada = discService.relacionaDT(disc.getId(), turma.getId());

        assertNotNull(relacionada);
        assertNotNull(relacionada.getTurma());
        assertEquals(turma.getId(), relacionada.getTurma().getId());
    }

    @Test
    void relacionaDT_invalido_disciplinaInexistente() {
        Turma turma = turmaService.createTurma(new Turma("TURMA-B", 35));

        assertNull(discService.relacionaDT(999999, turma.getId()));
    }

    @Test
    void relacionaDT_invalido_turmaInexistente() {
        Disciplina disc = discService.createDisciplina(new Disciplina("Música", 60));

        assertNull(discService.relacionaDT(disc.getId(), 999999));
    }

    // ============================ updateDisciplina ============================

    @Test
    void updateDisciplina_valido_deveAtualizarCampos() {
        Disciplina criada = discService.createDisciplina(new Disciplina("Sociologia", 100));

        Disciplina alteracoes = new Disciplina();
        alteracoes.setNome("Sociologia (Atualizada)");
        alteracoes.setCargaHoraria(110);

        Disciplina atualizada = discService.updateDisciplina(criada.getId(), alteracoes);

        assertNotNull(atualizada);
        assertEquals("Sociologia (Atualizada)", atualizada.getNome());
        assertEquals(110, atualizada.getCargaHoraria());
    }

    @Test
    void updateDisciplina_invalido_idInexistente() {
        Disciplina alteracoes = new Disciplina();
        alteracoes.setNome("Qualquer");
        alteracoes.setCargaHoraria(10);

        assertNull(discService.updateDisciplina(999999, alteracoes));
    }

    @Test
    void updateDisciplina_invalido_alteracoesNulas() {
        Disciplina criada = discService.createDisciplina(new Disciplina("Filosofia", 90));
        assertThrows(IllegalArgumentException.class, () -> discService.updateDisciplina(criada.getId(), null));
    }
}
