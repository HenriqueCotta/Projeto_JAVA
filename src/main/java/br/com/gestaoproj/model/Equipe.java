package br.com.gestaoproj.model;

import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class Equipe {

    private ObjectId id;
    private String nome;
    private String descricao;
    private List<ObjectId> membrosIds;

    public Equipe() {
        this.membrosIds = new ArrayList<>();
    }

    public Equipe(ObjectId id, String nome, String descricao, List<ObjectId> membrosIds) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.membrosIds = membrosIds != null ? membrosIds : new ArrayList<>();
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

    public List<ObjectId> getMembrosIds() {
        return membrosIds;
    }

    public void setMembrosIds(List<ObjectId> membrosIds) {
        this.membrosIds = membrosIds;
    }

    @Override
    public String toString() {
        return "Equipe{" +
                "id=" + (id != null ? id.toHexString() : null) +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", membrosIds=" + membrosIds +
                '}';
    }
}
