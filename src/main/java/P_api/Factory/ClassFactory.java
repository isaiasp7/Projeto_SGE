package P_api.Factory;


import P_api.DTO.AlunoDTO;
import P_api.DTO.DisciplinaDTO;
import P_api.DTO.MatriculaDTO;
import P_api.DTO.ProfDiscDTO;
import P_api.Model.Aluno;
import P_api.Model.Disciplina;
import P_api.Model.Matricula;
import P_api.Model.Professor;

public class ClassFactory {

    //RECEBE UMA STRING, CRIA A INSTANCIA E

    //TBM PODE RECEBER MAIS DE UM PARAMETRO COMO NOME E O QUE DESEJA FAZER

    public class GenerateObj {
        public static <T> T create(Class<T> clazz) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao criar instância de " + clazz.getSimpleName(), e);
            }
        }
    }


    //======================== TRANSFORMANDO ENTIDADES EM DTO  =================================================

    public static DisciplinaDTO toDisciplinaDTO(Disciplina disciplina) {
            return new DisciplinaDTO(disciplina.getId(),disciplina.getNome());
        }

    public static ProfDiscDTO toProfDiscDTO(Professor professor) {
        ProfDiscDTO dto = new ProfDiscDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setEmail(professor.getEmail());
        dto.setTelefone(professor.getTelefone());

        if (professor.getDisciplina_fk() != null) { // Verifica se a disciplina não é nula
            DisciplinaDTO disciplinaDTO = new DisciplinaDTO();
            disciplinaDTO.setId(professor.getDisciplina_fk().getId());
            disciplinaDTO.setNome(professor.getDisciplina_fk().getNome());
            // disciplinaDTO.setCargaHoraria(professor.getDisciplina().getCargaHoraria());

            dto.setDisciplina(disciplinaDTO);
        } else {
            dto.setDisciplina(null); // Ou pode definir um objeto vazio, dependendo do seu caso
        }

        return dto;
    }



}
