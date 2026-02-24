package P_api.Model;

import P_api.DTO.DisciplinaDTO;
import P_api.DTO.MatriculaDTO;
import Util.Utilities;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "turma")
@Getter
@Setter
public class Turma {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(length = 10,unique = true)
    private String nome;


    private int capacidadeAtual;
    
    private int capacidadeMax;

    //====================================================
    //mappedBy = "turma" -> matricula vai possuir atributo identificado de turma
    //cascade -> para não quebrar minhas perna com construct mais na frente
    //orphanRemoval -> remove matricula que não estão ligadas a ninguem
    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)//relacionamento um para muitos
    @JsonProperty("Lista de matriculados na turma")
    private List<Matricula> matriculas = new ArrayList<>();

    //=====================================================
    @JsonProperty("Lista de Disciplinas")
    @OneToMany(mappedBy = "turma")
    private List<Disciplina> disciplinaList = new ArrayList<>();

    //=====================================================


    public Turma() {
        this.id= Utilities.gerar_id("turma");
    }

    public Turma(Turma turma) {
        this.capacidadeMax = turma.getCapacidadeMax();
        this.id=Utilities.gerar_id("turma");
        this.nome = turma.getNome();;
        this.capacidadeAtual = 0;
    }

    public Turma(String nome,int capacidadeMax) {
        this.capacidadeMax = capacidadeMax;
        this.id=Utilities.gerar_id("turma");
        this.nome= nome;
        this.capacidadeAtual = 0;
    }

    public List<MatriculaDTO> getMatriculasDTO() {

        return this.getMatriculas().stream()
                .map(m -> new MatriculaDTO(m.getId(),m.getAluno().getNome(),m.getAluno())) // Mapeia cada matrícula para um DTO
                .collect(Collectors.toList());
    }

    public List<DisciplinaDTO> getDisciplinaDTO() {

        return this.getDisciplinaList().stream()
                .map(m -> new DisciplinaDTO(m.getId(),m.getNome())) // Mapeia cada matrícula para um DTO
                .collect(Collectors.toList());
    }
}