package P_api.Model;


import Util.Utilities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Table(name = "aluno")
@Getter
@Setter
public class Aluno {
@Id
//Validação em nível de aplicação: Garante que o campo não seja null antes de a entidade ser persistida no banco de dados.
    @Column(name = "cpf", length = 11, unique = true, nullable = false)
//Define restrições no banco de dados: Se nullable = false, cria a coluna no banco com regra  NOT NULL.
    private String cpf;

    @Column(name = "nome", columnDefinition = "varchar(50)", nullable = false)
    private String nome;

    @Column(columnDefinition = "date",  length = 10, nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("dataNascimento")
    private Date dataNascimento;

    @Column(length = 2)
    private Integer quantFaltas = 0;

    private String email;

    @Column(nullable = false)
    private String senha ;

    //==========================================

    @OneToOne(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//relacionamento um para muitos
    @JsonBackReference
    private Matricula matriculas;


    /*======================================*/
    public Aluno(String cpf, String nome, Date dataNasci) {

        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNasci;
        this.email = Utilities.gerar_email(this.nome);
        this.senha = "Aluno2026";


    }
    public Aluno(Aluno aluno) {

        this.cpf = aluno.getCpf();
        this.nome = aluno.getNome();
        this.dataNascimento = aluno.getDataNascimento();
        this.email = Utilities.gerar_email(this.nome);
        this.senha = "Aluno2026";


    }

    public Aluno() {

    }



}

