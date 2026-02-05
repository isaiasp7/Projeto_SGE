package P_api.DTO;

import P_api.Model.Aluno;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AlunoDTO {
    private Integer Quant_faltas = 0;


    public AlunoDTO() {

    }


    public AlunoDTO(Aluno aluno) {
        this.Quant_faltas = aluno.getQuant_faltas();


    }

    public AlunoDTO(Integer quant_faltas) {
        Quant_faltas = quant_faltas;
    }
}
