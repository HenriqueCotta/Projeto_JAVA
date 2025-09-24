package br.com.gestaoproj.dao;

import br.com.gestaoproj.model.Equipe;
import org.bson.types.ObjectId;

import java.util.List;

public interface EquipeDAO {
    void salvar(Equipe obj);
    Equipe buscarPorId(ObjectId id);
    List<Equipe> listarTodos();
    void atualizar(Equipe obj);
    void deletar(ObjectId id);
}
