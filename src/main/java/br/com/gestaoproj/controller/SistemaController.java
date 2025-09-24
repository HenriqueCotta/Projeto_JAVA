package br.com.gestaoproj.controller;

import br.com.gestaoproj.dao.*;
import br.com.gestaoproj.model.Equipe;
import br.com.gestaoproj.model.Projeto;
import br.com.gestaoproj.model.Usuario;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SistemaController {

    private final UsuarioDAO usuarioDAO;
    private final ProjetoDAO projetoDAO;
    private final EquipeDAO equipeDAO;

    public SistemaController() {
        this.usuarioDAO = new UsuarioDAOImpl();
        this.projetoDAO = new ProjetoDAOImpl();
        this.equipeDAO = new EquipeDAOImpl();
    }

    public void criarNovoUsuario(String nomeCompleto, String cpf, String email, String cargo,
            String login, String senha, String perfilStr) {
        Usuario.Perfil perfil = parsePerfil(perfilStr);
        Usuario u = new Usuario(null, nomeCompleto, cpf, email, cargo, login, senha, perfil);
        usuarioDAO.salvar(u);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioDAO.listarTodos();
    }

    public Usuario buscarUsuarioPorId(ObjectId id) {
        return usuarioDAO.buscarPorId(id);
    }

    public void atualizarUsuario(ObjectId id, String nomeCompleto, String cpf, String email, String cargo,
            String login, String senha, String perfilStr) {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u == null)
            throw new IllegalArgumentException("Usuário não encontrado.");
        u.setNomeCompleto(nomeCompleto);
        u.setCpf(cpf);
        u.setEmail(email);
        u.setCargo(cargo);
        u.setLogin(login);
        u.setSenha(senha);
        u.setPerfil(parsePerfil(perfilStr));
        usuarioDAO.atualizar(u);
    }

    public void deletarUsuario(ObjectId id) {
        usuarioDAO.deletar(id);
    }

    private Usuario.Perfil parsePerfil(String s) {
        if (s == null)
            return null;
        s = s.trim().toUpperCase();
        switch (s) {
            case "ADMINISTRADOR":
                return Usuario.Perfil.ADMINISTRADOR;
            case "GERENTE":
                return Usuario.Perfil.GERENTE;
            case "COLABORADOR":
                return Usuario.Perfil.COLABORADOR;
            default:
                throw new IllegalArgumentException("Perfil inválido. Use ADMINISTRADOR, GERENTE ou COLABORADOR.");
        }
    }

    public void criarNovoProjeto(String nome, String descricao, LocalDate dataInicio,
            LocalDate dataTerminoPrevista, String statusStr, ObjectId gerenteId) {
        Projeto.Status status = parseStatus(statusStr);
        Projeto p = new Projeto(null, nome, descricao, dataInicio, dataTerminoPrevista, status, gerenteId);
        projetoDAO.salvar(p);
    }

    public List<Projeto> listarTodosProjetos() {
        return projetoDAO.listarTodos();
    }

    public Projeto buscarProjetoPorId(ObjectId id) {
        return projetoDAO.buscarPorId(id);
    }

    public void atualizarProjeto(ObjectId id, String nome, String descricao, LocalDate dataInicio,
            LocalDate dataTerminoPrevista, String statusStr, ObjectId gerenteId) {
        Projeto p = projetoDAO.buscarPorId(id);
        if (p == null)
            throw new IllegalArgumentException("Projeto não encontrado.");
        p.setNome(nome);
        p.setDescricao(descricao);
        p.setDataInicio(dataInicio);
        p.setDataTerminoPrevista(dataTerminoPrevista);
        p.setStatus(parseStatus(statusStr));
        p.setGerenteResponsavelId(gerenteId);
        projetoDAO.atualizar(p);
    }

    public void deletarProjeto(ObjectId id) {
        projetoDAO.deletar(id);
    }

    public void vincularGerenteAoProjeto(ObjectId projetoId, ObjectId gerenteId) {
        Usuario gerente = usuarioDAO.buscarPorId(gerenteId);
        if (gerente == null)
            throw new IllegalArgumentException("Gerente não encontrado.");
        if (gerente.getPerfil() != Usuario.Perfil.GERENTE && gerente.getPerfil() != Usuario.Perfil.ADMINISTRADOR) {
            throw new IllegalArgumentException("Usuário informado não possui perfil de GERENTE/ADMINISTRADOR.");
        }
        Projeto p = projetoDAO.buscarPorId(projetoId);
        if (p == null)
            throw new IllegalArgumentException("Projeto não encontrado.");
        p.setGerenteResponsavelId(gerenteId);
        projetoDAO.atualizar(p);
    }

    private Projeto.Status parseStatus(String s) {
        if (s == null)
            return null;
        s = s.trim().toUpperCase();
        switch (s) {
            case "PLANEJADO":
                return Projeto.Status.PLANEJADO;
            case "EM_ANDAMENTO":
                return Projeto.Status.EM_ANDAMENTO;
            case "CONCLUIDO":
                return Projeto.Status.CONCLUIDO;
            case "CANCELADO":
                return Projeto.Status.CANCELADO;
            default:
                throw new IllegalArgumentException(
                        "Status inválido. Use PLANEJADO, EM_ANDAMENTO, CONCLUIDO ou CANCELADO.");
        }
    }

    public void criarNovaEquipe(String nome, String descricao, List<ObjectId> membrosIds) {
        Equipe e = new Equipe(null, nome, descricao, membrosIds != null ? membrosIds : new ArrayList<>());
        equipeDAO.salvar(e);
    }

    public List<Equipe> listarTodasEquipes() {
        return equipeDAO.listarTodos();
    }

    public Equipe buscarEquipePorId(ObjectId id) {
        return equipeDAO.buscarPorId(id);
    }

    public void atualizarEquipe(ObjectId id, String nome, String descricao, List<ObjectId> membrosIds) {
        Equipe e = equipeDAO.buscarPorId(id);
        if (e == null)
            throw new IllegalArgumentException("Equipe não encontrada.");
        e.setNome(nome);
        e.setDescricao(descricao);
        e.setMembrosIds(membrosIds != null ? membrosIds : new ArrayList<>());
        equipeDAO.atualizar(e);
    }

    public void deletarEquipe(ObjectId id) {
        equipeDAO.deletar(id);
    }

    public void adicionarMembroEquipe(ObjectId equipeId, ObjectId usuarioId) {
        Equipe e = equipeDAO.buscarPorId(equipeId);
        if (e == null)
            throw new IllegalArgumentException("Equipe não encontrada.");
        if (usuarioDAO.buscarPorId(usuarioId) == null)
            throw new IllegalArgumentException("Usuário não encontrado.");
        if (!e.getMembrosIds().contains(usuarioId)) {
            e.getMembrosIds().add(usuarioId);
            equipeDAO.atualizar(e);
        }
    }

    public void removerMembroEquipe(ObjectId equipeId, ObjectId usuarioId) {
        Equipe e = equipeDAO.buscarPorId(equipeId);
        if (e == null)
            throw new IllegalArgumentException("Equipe não encontrada.");
        e.getMembrosIds().removeIf(id -> id.equals(usuarioId));
        equipeDAO.atualizar(e);
    }
}
