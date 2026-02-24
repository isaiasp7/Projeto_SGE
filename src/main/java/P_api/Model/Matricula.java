package P_api.Model;

import P_api.Model.StatusEnums.StatusEnum;
import Util.Utilities;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name="matricula")
@Entity
@Getter
@Setter
public class Matricula {
    @Id
    @Column(name ="id", unique = true, nullable = false)
    private long id;

    @Column(columnDefinition = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataMatricula;

    @Enumerated(EnumType.STRING)//traduz o valor de status para enum
    @NotNull
    @Column(nullable = false)
    private StatusEnum.Status status;

    //=============== ALUNO =================
    //PERSIST = salva  / MERGE = atualizar }
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})   //representa uma relação de um para um. como é o caso de uma matricula para cada aluno
    @JoinColumn(name = "alunoCpf", referencedColumnName = "cpf")
    private Aluno aluno;

    //                 TURMAS

    @ManyToOne
    @JoinColumn(name = "turmaId")//junção de colunas && uma nova tabela chamada turma_id sera gerada possuindo apenas
    // a FK
    private Turma turma;


    //                 NOTAS

    @OneToMany(mappedBy = "matriculaFk")

    private List<Notas> notas = new ArrayList<>();

    //=====================================

    //construtor
    public Matricula() {
        this.status =StatusEnum.Status.ATIVO;
        this.id= (int) Utilities.gerar_id("matricula");
    }

    public Matricula(LocalDate dataMatricula, StatusEnum.Status status, Turma turma) {

        this.dataMatricula = dataMatricula;
        this.id= (int) Utilities.gerar_id("matricula");
        if(this.getAluno().getEmail()==null){
            this.getAluno().setEmail(Utilities.gerar_email(this.getAluno().getNome()));
        }

        this.setStatus(status);
        this.turma = turma;

    }
//=============================================================



    public void setStatus(StatusEnum.Status status) {
        // Usa Enum.valueOf() para verificar se o status é válido
        try {
            this.status = StatusEnum.Status.valueOf(status.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido!\n Use apenas : ATIVO, DESATIVADO ou TRANCADO.");
        }
    }


    public void setDataMatricula(LocalDate dataM) {
        this.dataMatricula = dataM;
    }

    public void setNotas(Notas notas) {
       this.notas.add(notas);
    }


}
