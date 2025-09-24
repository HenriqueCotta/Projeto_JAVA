package br.com.gestaoproj.model;

import org.bson.types.ObjectId;

import java.time.LocalDate;

public class Projeto {

    private ObjectId id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataTerminoPrevista;
    private Status status;
    private ObjectId gerenteResponsavelId;

    public enum Status {
        PLANEJADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO
    }

    public Projeto() {
    }

    public Projeto(ObjectId id, String nome, String descricao, LocalDate dataInicio,
            LocalDate dataTerminoPrevista, Status status, ObjectId gerenteResponsavelId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevista = dataTerminoPrevista;
        this.status = status;
        this.gerenteResponsavelId = gerenteResponsavelId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataTerminoPrevista() {
        return dataTerminoPrevista;
    }

    public void setDataTerminoPrevista(LocalDate dataTerminoPrevista) {
        this.dataTerminoPrevista = dataTerminoPrevista;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ObjectId getGerenteResponsavelId() {
        return gerenteResponsavelId;
    }

    public void setGerenteResponsavelId(ObjectId gerenteResponsavelId) {
        this.gerenteResponsavelId = gerenteResponsavelId;
    }

    @Override
    public String toString() {
        return "Projeto{" +
                "id=" + (id != null ? id.toHexString() : null) +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataTerminoPrevista=" + dataTerminoPrevista +
                ", status=" + status +
                ", gerenteResponsavelId=" + (gerenteResponsavelId != null ? gerenteResponsavelId.toHexString() : null) +
                '}';
    }
}
