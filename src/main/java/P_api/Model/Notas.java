package P_api.Model;

import P_api.Model.StatusEnums.StatusEnum;
import Util.Utilities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name= "notas")
@Getter
@Setter
public class Notas {
    @Id
    @Column(unique = true, nullable = false)
    private long id;
    @Column(columnDefinition = "decimal(5, 2)")
    private float nota1;
    @Column(columnDefinition = "decimal(5, 2)")
    private float nota2;
    @Column(columnDefinition = "decimal(5, 2)")
    private float media;

    @Enumerated(EnumType.STRING)
    private StatusEnum.Situacao situacao;
    //===================MATRICULA=====================

    @ManyToOne
    @JoinColumn(name ="matriculaFk" , unique = true)
    private Matricula matriculaFk;


//             DISCIPLINA

    @ManyToOne
    @JoinColumn(name = "disciplinaFk")
    private Disciplina disciplinaFk;


    //====================================


    public Notas() {
        this.id=(int) Utilities.gerar_id("notas");

    }

    public Notas(Matricula matricula_fk,Disciplina disciplinaN_fk, float nota1, float nota2) {
        this.id=(int) Utilities.gerar_id("notas");
        this.disciplinaFk=disciplinaN_fk;
        this.matriculaFk = matricula_fk;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.setMedia();
    }

    public void setMedia() {
        this.media=(this.nota1+this.nota2)/2;
        if(this.media>=7){
            this.situacao = StatusEnum.Situacao.APROVADO;
            if(this.media>10){
                this.media = 10;
            }
        }else if(this.media>=5){
            this.situacao = StatusEnum.Situacao.RECUPERACAO;
        }
        else{
            this.situacao = StatusEnum.Situacao.REPROVADO;
        }
    }

    public void setSituacao(StatusEnum.Situacao situacao) {
        // Usa Enum.valueOf() para verificar se o status é válido
        try {
            this.situacao = StatusEnum.Situacao.valueOf(situacao.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Situacao inválido!\n Use apenas : APROVADO, RECUPERACAO ou REPROVADO.");
        }
    }








}
