package P_api.Model;

import Util.Utilities;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="disciplina")
@Getter
@Setter
/*
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties(ignoreUnknown = true)//ignora campos nulos, usado para update
*/
public class Disciplina {
    @Id
    private long id;
    @Column(columnDefinition = "varchar(50)",nullable = false)
    @JsonMerge
    private String nome;
    @Column(nullable = false)
    private Integer cargaHoraria;

    //=======================PROFESSOR=========================

    @OneToOne(mappedBy = "disciplina_fk")//disciplina_id é nome do atributo na class= professor que recebera o valores

    private Professor professor;

    //====================== NOTAS ============================


    @OneToMany(mappedBy = "disciplinaN_fk")
    @JsonIgnore
    private List<Notas> notas = new ArrayList<>();

    //====================== TURMA ============================
    @ManyToOne
    @JoinColumn(name = "turma_fk")
    //@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Turma turma;


//==============================================================
    public Disciplina() {
        this.id= Utilities.gerar_id("disciplina");
    }

    public Disciplina(String nome, int carga_h) {
        this.nome = nome;
        this.setCargaHoraria(carga_h);
        this.id=Utilities.gerar_id("disciplina");
    }

    public Disciplina(Disciplina disciplina) {

    this.nome = disciplina.nome;
    this.cargaHoraria = disciplina.cargaHoraria;
    this.id = Utilities.gerar_id("disciplina");
    this.professor = disciplina.professor==null?disciplina.professor:null;
    this.turma = disciplina.turma==null?disciplina.turma:null;
    this.notas = disciplina.notas==null?null:disciplina.notas;
    }

}
