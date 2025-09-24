package br.com.gestaoproj.view;

import br.com.gestaoproj.controller.SistemaController;
import br.com.gestaoproj.model.Equipe;
import br.com.gestaoproj.model.Projeto;
import br.com.gestaoproj.model.Usuario;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaView {

    private final Scanner scanner;
    private final SistemaController controller;

    public SistemaView(SistemaController controller) {
        this.scanner = new Scanner(System.in);
        this.controller = controller;
    }

    public void iniciar() {
        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            String opcao = lerLinha("Escolha uma opcao: ");
            switch (opcao) {
                case "1":
                    menuProjetos();
                    break;
                case "2":
                    menuEquipes();
                    break;
                case "3":
                    menuUsuarios();
                    break;
                case "0":
                    executando = false;
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opcao inválida.");
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n=== Sistema de Gestao de Projetos ===");
        System.out.println("1. Gerenciar Projetos");
        System.out.println("2. Gerenciar Equipes");
        System.out.println("3. Gerenciar Usuarios");
        System.out.println("0. Sair");
    }

    private void menuUsuarios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- Usuarios ---");
            System.out.println("1. Criar");
            System.out.println("2. Listar");
            System.out.println("3. Atualizar");
            System.out.println("4. Deletar");
            System.out.println("9. Voltar");
            String op = lerLinha("Opcao: ");
            switch (op) {
                case "1":
                    criarUsuario();
                    break;
                case "2":
                    listarUsuarios();
                    break;
                case "3":
                    atualizarUsuario();
                    break;
                case "4":
                    deletarUsuario();
                    break;
                case "9":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opcao inválida.");
            }
        }
    }

    private void menuProjetos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- Projetos ---");
            System.out.println("1. Criar");
            System.out.println("2. Listar");
            System.out.println("3. Atualizar");
            System.out.println("4. Deletar");
            System.out.println("5. Vincular Gerente");
            System.out.println("9. Voltar");
            String op = lerLinha("Opcao: ");
            switch (op) {
                case "1":
                    criarProjeto();
                    break;
                case "2":
                    listarProjetos();
                    break;
                case "3":
                    atualizarProjeto();
                    break;
                case "4":
                    deletarProjeto();
                    break;
                case "5":
                    vincularGerente();
                    break;
                case "9":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opcao inválida.");
            }
        }
    }

    private void menuEquipes() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- Equipes ---");
            System.out.println("1. Criar");
            System.out.println("2. Listar");
            System.out.println("3. Atualizar");
            System.out.println("4. Deletar");
            System.out.println("5. Adicionar Membro");
            System.out.println("6. Remover Membro");
            System.out.println("9. Voltar");
            String op = lerLinha("Opcao: ");
            switch (op) {
                case "1":
                    criarEquipe();
                    break;
                case "2":
                    listarEquipes();
                    break;
                case "3":
                    atualizarEquipe();
                    break;
                case "4":
                    deletarEquipe();
                    break;
                case "5":
                    adicionarMembroEquipe();
                    break;
                case "6":
                    removerMembroEquipe();
                    break;
                case "9":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opcao inválida.");
            }
        }
    }

    private void criarUsuario() {
        System.out.println("\n[NovO Usuário]");
        String nome = lerObrigatorio("Nome completo: ");
        String cpf = lerObrigatorio("CPF: ");
        String email = lerObrigatorio("Email: ");
        String cargo = lerObrigatorio("Cargo: ");
        String login = lerObrigatorio("Login: ");
        String senha = lerObrigatorio("Senha: ");
        String perfil = lerObrigatorio("Perfil (ADMINISTRADOR/GERENTE/COLABORADOR): ");
        try {
            controller.criarNovoUsuario(nome, cpf, email, cargo, login, senha, perfil);
            System.out.println("Usuario criado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarUsuarios() {
        System.out.println("\n[Lista de Usuarios]");
        List<Usuario> usuarios = controller.listarTodosUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("(vazio)");
            return;
        }
        for (Usuario u : usuarios) {
            System.out.println("- " + u);
        }
    }

    private void atualizarUsuario() {
        listarUsuarios();
        String idStr = lerObrigatorio("ID do usuario para atualizar: ");
        try {
            ObjectId id = new ObjectId(idStr);
            Usuario u = controller.buscarUsuarioPorId(id);
            if (u == null) {
                System.out.println("Usuario nao encontrado.");
                return;
            }
            System.out.println("Pressione ENTER para manter o valor atual.");

            String nome = lerOpcional("Nome completo [" + nullSafe(u.getNomeCompleto()) + "]: ", u.getNomeCompleto());
            String cpf = lerOpcional("CPF [" + nullSafe(u.getCpf()) + "]: ", u.getCpf());
            String email = lerOpcional("Email [" + nullSafe(u.getEmail()) + "]: ", u.getEmail());
            String cargo = lerOpcional("Cargo [" + nullSafe(u.getCargo()) + "]: ", u.getCargo());
            String login = lerOpcional("Login [" + nullSafe(u.getLogin()) + "]: ", u.getLogin());
            String senha = lerOpcional("Senha [" + (u.getSenha() != null ? "********" : "") + "]: ", u.getSenha());
            String perfil = lerOpcional(
                    "Perfil (ADMINISTRADOR/GERENTE/COLABORADOR) [" + (u.getPerfil() != null ? u.getPerfil().name() : "")
                            + "]: ",
                    u.getPerfil() != null ? u.getPerfil().name() : "");

            controller.atualizarUsuario(id, nome, cpf, email, cargo, login, senha, perfil);
            System.out.println("Usuario atualizado.");
        } catch (IllegalArgumentException e) {
            System.out.println("ID invalido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void deletarUsuario() {
        listarUsuarios();
        String idStr = lerObrigatorio("ID do usuario para deletar: ");
        try {
            controller.deletarUsuario(new ObjectId(idStr));
            System.out.println("Usuario deletado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void criarProjeto() {
        System.out.println("\n[Novo Projeto]");
        String nome = lerObrigatorio("Nome: ");
        String desc = lerObrigatorio("Descricao: ");
        LocalDate ini = lerDataObrigatoria("Data de inicio (AAAA-MM-DD): ");
        LocalDate fim = lerDataObrigatoria("Data termino prevista (AAAA-MM-DD): ");
        String status = lerObrigatorio("Status (PLANEJADO/EM_ANDAMENTO/CONCLUIDO/CANCELADO): ");
        String gerenteStr = lerOpcional("ID do gerente responsavel (opcional): ", "");
        ObjectId gerenteId = gerenteStr.isBlank() ? null : new ObjectId(gerenteStr);
        try {
            controller.criarNovoProjeto(nome, desc, ini, fim, status, gerenteId);
            System.out.println("Projeto criado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarProjetos() {
        System.out.println("\n[Lista de Projetos]");
        List<Projeto> projetos = controller.listarTodosProjetos();
        if (projetos.isEmpty()) {
            System.out.println("(vazio)");
            return;
        }
        for (Projeto p : projetos) {
            System.out.println("- " + p);
        }
    }

    private void atualizarProjeto() {
        listarProjetos();
        String idStr = lerObrigatorio("ID do projeto para atualizar: ");
        try {
            ObjectId id = new ObjectId(idStr);
            Projeto p = controller.buscarProjetoPorId(id);
            if (p == null) {
                System.out.println("Projeto nao encontrado.");
                return;
            }
            System.out.println("Pressione ENTER para manter o valor atual.");

            String nome = lerOpcional("Nome [" + nullSafe(p.getNome()) + "]: ", p.getNome());
            String desc = lerOpcional("Descricao [" + nullSafe(p.getDescricao()) + "]: ", p.getDescricao());
            LocalDate ini = lerDataOpcional(
                    "Data de inicio (AAAA-MM-DD) [" + (p.getDataInicio() != null ? p.getDataInicio() : "") + "]: ",
                    p.getDataInicio());
            LocalDate fim = lerDataOpcional(
                    "Data termino prevista (AAAA-MM-DD) ["
                            + (p.getDataTerminoPrevista() != null ? p.getDataTerminoPrevista() : "") + "]: ",
                    p.getDataTerminoPrevista());
            String status = lerOpcional(
                    "Status (PLANEJADO/EM_ANDAMENTO/CONCLUIDO/CANCELADO) ["
                            + (p.getStatus() != null ? p.getStatus().name() : "") + "]: ",
                    p.getStatus() != null ? p.getStatus().name() : "");
            String gerenteStr = lerOpcional("ID do gerente responsavel ["
                    + (p.getGerenteResponsavelId() != null ? p.getGerenteResponsavelId().toHexString() : "") + "]: ",
                    p.getGerenteResponsavelId() != null ? p.getGerenteResponsavelId().toHexString() : "");

            ObjectId gerenteId = gerenteStr.isBlank() ? null : new ObjectId(gerenteStr);
            controller.atualizarProjeto(id, nome, desc, ini, fim, status, gerenteId);
            System.out.println("Projeto atualizado.");
        } catch (IllegalArgumentException e) {
            System.out.println("ID invalido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void deletarProjeto() {
        listarProjetos();
        String idStr = lerObrigatorio("ID do projeto para deletar: ");
        try {
            controller.deletarProjeto(new ObjectId(idStr));
            System.out.println("Projeto deletado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void vincularGerente() {
        listarProjetos();
        String projetoIdStr = lerObrigatorio("ID do projeto: ");
        String gerenteIdStr = lerObrigatorio("ID do gerente: ");
        try {
            controller.vincularGerenteAoProjeto(new ObjectId(projetoIdStr), new ObjectId(gerenteIdStr));
            System.out.println("Gerente vinculado ao projeto.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void criarEquipe() {
        System.out.println("\n[Nova Equipe]");
        String nome = lerObrigatorio("Nome: ");
        String desc = lerObrigatorio("Descricao: ");
        System.out.println("Informe IDs de usuários (membros) separados por vírgula (ou deixe em branco):");
        String idsStr = lerOpcional("IDs: ", "");
        List<ObjectId> membros = parseObjectIdList(idsStr);
        try {
            controller.criarNovaEquipe(nome, desc, membros);
            System.out.println("Equipe criada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarEquipes() {
        System.out.println("\n[Lista de Equipes]");
        List<Equipe> equipes = controller.listarTodasEquipes();
        if (equipes.isEmpty()) {
            System.out.println("(vazio)");
            return;
        }
        for (Equipe e : equipes) {
            System.out.println("- " + e);
        }
    }

    private void atualizarEquipe() {
        listarEquipes();
        String idStr = lerObrigatorio("ID da equipe para atualizar: ");
        try {
            ObjectId id = new ObjectId(idStr);
            Equipe e = controller.buscarEquipePorId(id);
            if (e == null) {
                System.out.println("Equipe nao encontrada.");
                return;
            }
            System.out.println("Pressione ENTER para manter o valor atual.");

            String nome = lerOpcional("Nome [" + nullSafe(e.getNome()) + "]: ", e.getNome());
            String desc = lerOpcional("Descricao [" + nullSafe(e.getDescricao()) + "]: ", e.getDescricao());
            String idsStr = lerOpcional("IDs de membros separados por virgula [" + e.getMembrosIds() + "]: ", "");
            List<ObjectId> membros = idsStr.isBlank() ? e.getMembrosIds() : parseObjectIdList(idsStr);

            controller.atualizarEquipe(id, nome, desc, membros);
            System.out.println("Equipe atualizada.");
        } catch (IllegalArgumentException ex) {
            System.out.println("ID invalido: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void deletarEquipe() {
        listarEquipes();
        String idStr = lerObrigatorio("ID da equipe para deletar: ");
        try {
            controller.deletarEquipe(new ObjectId(idStr));
            System.out.println("Equipe deletada.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void adicionarMembroEquipe() {
        listarEquipes();
        String equipeId = lerObrigatorio("ID da equipe: ");
        String usuarioId = lerObrigatorio("ID do usuario: ");
        try {
            controller.adicionarMembroEquipe(new ObjectId(equipeId), new ObjectId(usuarioId));
            System.out.println("Membro adicionado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void removerMembroEquipe() {
        listarEquipes();
        String equipeId = lerObrigatorio("ID da equipe: ");
        String usuarioId = lerObrigatorio("ID do usuario: ");
        try {
            controller.removerMembroEquipe(new ObjectId(equipeId), new ObjectId(usuarioId));
            System.out.println("Membro removido.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private String lerLinha(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String lerObrigatorio(String prompt) {
        String v;
        do {
            v = lerLinha(prompt);
            if (v.isBlank())
                System.out.println("Campo obrigatorio. Tente novamente.");
        } while (v.isBlank());
        return v;
    }

    private String lerOpcional(String prompt, String atual) {
        String v = lerLinha(prompt);
        return v.isBlank() ? atual : v;
    }

    private LocalDate lerDataObrigatoria(String prompt) {
        while (true) {
            String s = lerObrigatorio(prompt);
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida. Use o formato AAAA-MM-DD.");
            }
        }
    }

    private LocalDate lerDataOpcional(String prompt, LocalDate atual) {
        String s = lerLinha(prompt);
        if (s.isBlank())
            return atual;
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            System.out.println("Data invalida. Mantendo valor atual.");
            return atual;
        }
    }

    private List<ObjectId> parseObjectIdList(String csv) {
        List<ObjectId> list = new ArrayList<>();
        if (csv == null || csv.isBlank())
            return list;
        String[] parts = csv.split(",");
        for (String p : parts) {
            String t = p.trim();
            if (!t.isEmpty()) {
                try {
                    list.add(new ObjectId(t));
                } catch (IllegalArgumentException e) {
                    System.out.println("ID ignorado (invalido): " + t);
                }
            }
        }
        return list;
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }
}
