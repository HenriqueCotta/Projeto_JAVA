package br.com.gestaoproj.dao;

import br.com.gestaoproj.model.Projeto;
import org.bson.types.ObjectId;

import java.util.List;

public interface ProjetoDAO {
    void salvar(Projeto obj);
    Projeto buscarPorId(ObjectId id);
    List<Projeto> listarTodos();
    void atualizar(Projeto obj);
    void deletar(ObjectId id);
}
