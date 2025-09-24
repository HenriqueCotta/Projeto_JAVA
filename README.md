# Gestão de Projetos (CLI) – README

## 1. O que é este projeto

Aplicação de linha de comando (CLI) para gestão de usuários, projetos e equipes, com arquitetura MVC + DAO, escrita em Java 17, persistindo dados no MongoDB. O objetivo é permitir cadastro e gerenciamento de:

1. Usuários (com perfis ADMINISTRADOR, GERENTE, COLABORADOR).
2. Projetos (com status PLANEJADO, EM\_ANDAMENTO, CONCLUIDO, CANCELADO e gerente responsável).
3. Equipes (com membros vinculados a usuários).

A interface do CLI foi escrita com textos ASCII para máxima compatibilidade de terminais.

---

## 2. Requisitos do sistema

1. Java 17 instalado e no PATH (`java -version` deve mostrar 17).
2. Apache Maven instalado (`mvn -version`).
3. MongoDB em execução local acessível em `mongodb://localhost:27017`.
4. Permissões para executar comandos no terminal (Windows, macOS ou Linux).

Banco utilizado: `gestao_projetos` (criado automaticamente ao inserir dados).

---

## 3. Como obter o código

1. Descompacte o arquivo do projeto em uma pasta local, por exemplo: `gestao-projetos-cli/`.
2. A estrutura relevante de diretórios é:

   * `src/main/java/br/com/gestaoproj/`

     1. `model/` (entidades)
     2. `dao/` (interfaces e implementações MongoDB)
     3. `controller/` (regras de negócio)
     4. `view/` (console/CLI)
     5. `util/` (utilitários, conexão Mongo)
     6. `Main.java` (ponto de entrada)

---

## 4. Como executar

1. Inicie o MongoDB localmente.
2. No diretório raiz do projeto, execute:

   ```bash
   mvn -q clean compile
   mvn -q exec:java
   ```

3. Se preferir mais logs, remova `-q`.
4. Ao iniciar, será exibido o menu principal do sistema no terminal.

Observações sobre terminal:

1. Os textos da UI estão em ASCII para funcionar em qualquer console.
2. Se seus dados cadastrados tiverem acentos/ç e o console não estiver em UTF-8, esses caracteres podem aparecer incorretos. No Windows, usar Windows Terminal ou `chcp 65001` antes de rodar ajuda a exibir corretamente.

---

## 5. Visão geral dos fluxos do sistema

Ao iniciar, você verá o menu principal:

```bash
=== Sistema de Gestao de Projetos ===
1. Gerenciar Projetos
2. Gerenciar Equipes
3. Gerenciar Usuarios
0. Sair
Escolha uma opcao:
```

A navegação é simples: digite o número da opção e pressione ENTER.

### 5.1. Fluxo de Usuários

Menu:

```bash
--- Usuarios ---
1. Criar
2. Listar
3. Atualizar
4. Deletar
9. Voltar
```

1. Criar: informa nome completo, CPF, email, cargo, login, senha e perfil (ADMINISTRADOR/GERENTE/COLABORADOR).
2. Listar: mostra todos os usuários com seus `_id` (ObjectId do MongoDB) e dados.
3. Atualizar:

   * O sistema lista usuários e pede o ID do usuário a atualizar.
   * Exibe prompts com o valor atual entre colchetes.
   * Pressionar ENTER mantém o valor; digitar substitui.
   * Perfil deve ser exatamente um dos três valores especificados.
4. Deletar:

   * O sistema lista usuários e pede o ID do usuário a excluir.
   * Ao confirmar com um ID válido, remove o registro do MongoDB.

### 5.2. Fluxo de Projetos

Menu:

```bash
--- Projetos ---
1. Criar
2. Listar
3. Atualizar
4. Deletar
5. Vincular Gerente
9. Voltar
```

1. Criar: informa nome, descricao, data de inicio (AAAA-MM-DD), data termino prevista (AAAA-MM-DD), status (PLANEJADO/EM\_ANDAMENTO/CONCLUIDO/CANCELADO) e opcionalmente o ID do gerente responsável (ObjectId de um usuário com perfil GERENTE ou ADMINISTRADOR).
2. Listar: exibe todos os projetos com `_id`, campos e o `gerenteResponsavelId` (se houver).
3. Atualizar: semelhante ao de Usuários; ENTER mantém valores; datas devem estar no formato AAAA-MM-DD; status deve ser um dos quatro valores.
4. Deletar: lista projetos e pede o ID do projeto a remover.
5. Vincular Gerente: pede o ID do projeto e o ID do usuário gerente; valida se o usuário tem perfil GERENTE ou ADMINISTRADOR.

### 5.3. Fluxo de Equipes

Menu:

```bash
--- Equipes ---
1. Criar
2. Listar
3. Atualizar
4. Deletar
5. Adicionar Membro
6. Remover Membro
9. Voltar
```

1. Criar: informa nome, descricao e, opcionalmente, uma lista de IDs de usuários separados por vírgula para compor os membros.
2. Listar: mostra todas as equipes com `_id`, nome, descricao e lista de `membrosIds`.
3. Atualizar: permite alterar nome, descricao e a lista completa de `membrosIds` (fornecida como CSV de ObjectIds). ENTER mantém valores atuais.
4. Deletar: remove uma equipe pelo `_id`.
5. Adicionar Membro: pede o ID da equipe e o ID do usuário; valida se existem; não duplica membro.
6. Remover Membro: pede o ID da equipe e do usuário; remove se presente.

---

## 6. Modelo de dados (resumo)

1. `Usuario`

   * Campos: `ObjectId id`, `String nomeCompleto`, `String cpf`, `String email`, `String cargo`, `String login`, `String senha`, `Perfil perfil`.
   * `Perfil`: `ADMINISTRADOR`, `GERENTE`, `COLABORADOR`.

2. `Projeto`

   * Campos: `ObjectId id`, `String nome`, `String descricao`, `LocalDate dataInicio`, `LocalDate dataTerminoPrevista`, `Status status`, `ObjectId gerenteResponsavelId`.
   * `Status`: `PLANEJADO`, `EM_ANDAMENTO`, `CONCLUIDO`, `CANCELADO`.

3. `Equipe`

   * Campos: `ObjectId id`, `String nome`, `String descricao`, `List<ObjectId> membrosIds`.

Coleções no MongoDB: `usuarios`, `projetos`, `equipes`.

---

## 7. Arquitetura e organização

1. Padrões aplicados: MVC + DAO.
2. Camadas:

   * `model`: classes de domínio (POJOs e enums).
   * `dao`: interfaces de acesso e implementações usando MongoDB Java Driver (`insertOne`, `find`, `updateOne`, `deleteOne`).
   * `controller`: lógica de negócio, validações de perfis e status, orquestra chamadas aos DAOs.
   * `view`: interface de console (menus, leitura e exibição), nunca acessa DAO diretamente.
   * `util`: `ConexaoMongo` (singleton que provê `MongoDatabase`).
   * `Main`: inicializa controller e view, e inicia o loop do CLI.

---

## 8. Exemplo de sessão rápida

1. Criar usuário gerente:

   * Menu Principal → 3 (Usuarios) → 1 (Criar) → Perfil: `GERENTE`.
2. Criar projeto:

   * Menu Principal → 1 (Projetos) → 1 (Criar) → informe nome, descricao, datas e status.
   * Opcional: informe o `gerenteResponsavelId` usando o `_id` do usuário criado.
3. Vincular gerente (se não informou no passo anterior):

   * Menu Principal → 1 (Projetos) → 5 (Vincular Gerente) → informe IDs de projeto e do gerente.
4. Criar equipe e adicionar membros:

   * Menu Principal → 2 (Equipes) → 1 (Criar) → informe nome/descricao e uma lista CSV de IDs de usuários.
   * Para adicionar depois: 2 (Equipes) → 5 (Adicionar Membro).

---

## 9. Tratamento de erros e validações

1. Formato de ID: precisa ser um `ObjectId` válido (24 hex). Se for inválido, o sistema informa erro.
2. Perfil: somente `ADMINISTRADOR`, `GERENTE`, `COLABORADOR`. Outros valores geram erro.
3. Status: somente `PLANEJADO`, `EM_ANDAMENTO`, `CONCLUIDO`, `CANCELADO`.
4. Datas: devem ser informadas no formato `AAAA-MM-DD`. Entradas inválidas são rejeitadas com mensagem clara.
5. Entradas obrigatórias: prompts repetem até receber conteúdo não vazio.
6. Exceções do MongoDB: são capturadas e apresentadas com mensagens de erro no console.

---

## 10. Segurança e observações

1. Senhas são armazenadas em texto puro para simplificar a avaliação. Em produção, recomenda-se hashing (ex.: BCrypt).
2. UI em ASCII para máxima compatibilidade; se seu terminal suportar UTF-8, os dados com acentos digitados serão gravados corretamente, mas a exibição depende do console.
3. O banco é local e sem autenticação por padrão; em produção, configure usuário/senha e SSL conforme necessário.

---

## 11. Solução de problemas

1. “Falha ao conectar ao MongoDB”: verifique se o serviço está ativo e acessível em `mongodb://localhost:27017`.
2. “ID invalido”: confirme que o `_id` copiado tem 24 caracteres hexadecimais.
3. Acentuação aparecendo incorreta: use um terminal com UTF-8 (Windows Terminal, PowerShell moderno, macOS Terminal, bash/zsh) ou `chcp 65001` antes de rodar no `cmd.exe`.
4. Maven não encontrado: instale o Maven e adicione ao PATH.
5. Java versão diferente: garanta Java 17.

---

## 12. Critérios rápidos para avaliação

1. Requisitos funcionais: CRUD completo de Usuários, Projetos e Equipes; vínculo de gerente; adição/remoção de membros.
2. Requisitos técnicos: Java 17, Maven, MongoDB local, driver `mongodb-driver-sync`.
3. Arquitetura: separação MVC + DAO estrita; View não acessa DAO.
4. POO: encapsulamento, uso de interfaces nos DAOs, enums para Perfil/Status.
5. Qualidade: código comentado em português, tratamento de exceções de entrada e banco, UI clara e direta.

---

## 13. Comandos úteis

1. Compilar e executar:

   ```bash
   mvn clean compile
   mvn exec:java
   ```

2. Limpar artefatos:

   ```bash
   mvn clean
   ```

Pronto. Com os passos acima você consegue subir o sistema, navegar pelos menus e validar todos os fluxos solicitados.
