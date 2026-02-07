package P_api.Model;


import Util.Utilities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="professor")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Professor {

    @Id
    @Column(unique = true, nullable = false)
    private long id;
    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String nome;
    @Column(columnDefinition = "varchar(50)",unique = true)
    private String email;
    private String telefone;

    //====================DISCIPLINA=======================
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "disciplina_id")//cria uma tabela chamada disciplina_id em professor

    private Disciplina disciplina_fk;

    public Professor() {

    }
    public Professor(Professor professor) {
        this.setNome(professor.getNome());
        this.email = Utilities.gerar_email(this.getNome());
        this.disciplina_fk = professor.getDisciplina_fk();
        this.id= Utilities.gerar_id("professor");
        this.telefone = professor.getTelefone();
    }

    public Professor(String nome, Disciplina disciplina_fk, String telefone) {
        this.nome = nome;
        this.email = Utilities.gerar_email(this.getNome());
        this.disciplina_fk = disciplina_fk;
        this.id=(int)Utilities.gerar_id("professor");
        this.telefone = telefone;
    }
    public Professor( String nome, String telefone) {
        this.nome = nome;
        this.id=(int)Utilities.gerar_id("professor");
        this.email = Utilities.gerar_email(this.getNome());
        this.telefone = telefone;

    }



    public void setNome(String nome) {
        if (nome!=null) {
            this.nome = nome;
        }


    }








}
