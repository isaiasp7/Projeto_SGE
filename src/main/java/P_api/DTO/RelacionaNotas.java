package P_api.DTO;

import P_api.Model.Notas;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelacionaNotas {
    private float nota1;
    private float nota2;
    private long idDisc;
    private long idMat;

    public RelacionaNotas() {

    }
    public RelacionaNotas(Notas nota) {
        this.nota1 = nota.getNota1();
        this.nota2 = nota.getNota2();
        this.idMat = nota.getMatriculaFk().getId();
        this.idDisc = nota.getDisciplinaFk().getId();
    }
}
