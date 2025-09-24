package br.com.gestaoproj.model;

import org.bson.types.ObjectId;

public class Usuario {

    private ObjectId id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private String cargo;
    private String login;
    private String senha;
    private Perfil perfil;

    public enum Perfil {
        ADMINISTRADOR, GERENTE, COLABORADOR
    }

    public Usuario() {
    }

    public Usuario(ObjectId id, String nomeCompleto, String cpf, String email, String cargo,
            String login, String senha, Perfil perfil) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + (id != null ? id.toHexString() : null) +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", cargo='" + cargo + '\'' +
                ", login='" + login + '\'' +
                ", perfil=" + perfil +
                '}';
    }
}
