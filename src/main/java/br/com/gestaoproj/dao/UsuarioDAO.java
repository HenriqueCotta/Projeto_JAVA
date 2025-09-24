package br.com.gestaoproj.dao;

import br.com.gestaoproj.model.Usuario;
import org.bson.types.ObjectId;

import java.util.List;

public interface UsuarioDAO {
    void salvar(Usuario obj);
    Usuario buscarPorId(ObjectId id);
    List<Usuario> listarTodos();
    void atualizar(Usuario obj);
    void deletar(ObjectId id);
}
