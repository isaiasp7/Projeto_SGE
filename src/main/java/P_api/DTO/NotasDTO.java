package P_api.DTO;

import P_api.Model.Notas;
import P_api.Model.StatusEnums.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotasDTO {
    private MatriculaDTO matricula;
    private DisciplinaDTO disciplina;
    private float nota1;
    private float nota2;
    private float media;
    @Enumerated(EnumType.STRING)
    private StatusEnum.Situacao situacao;



    public NotasDTO(){

    }

    public NotasDTO(Notas nota) {
        this.disciplina = new DisciplinaDTO(nota.getDisciplinaFk());
        this.matricula = new MatriculaDTO(nota.getMatriculaFk());
        this.nota1 = nota.getNota1();
        this.nota2 = nota.getNota2();
        this.media = nota.getMedia();
        this.situacao = nota.getSituacao();



    }
}
