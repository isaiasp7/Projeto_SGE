package P_api.DTO;

import P_api.Model.Aluno;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AlunoDTO {
    private String cpf;
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataNasci;
    private Integer Quant_faltas = 0;


    public AlunoDTO() {

    }

    public AlunoDTO(Aluno aluno) {
        this.cpf = aluno.getCpf();
        this.email = aluno.getEmail();
        this.dataNasci = aluno.getDataNasci();
        this.Quant_faltas = aluno.getQuant_faltas();


    }

}
