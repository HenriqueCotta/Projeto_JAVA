package br.com.gestaoproj.dao;

import br.com.gestaoproj.model.Usuario;
import br.com.gestaoproj.util.ConexaoMongo;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final MongoCollection<Document> collection;

    public UsuarioDAOImpl() {
        this.collection = ConexaoMongo.getDatabase().getCollection("usuarios");
    }

    private Document toDocument(Usuario u) {
        Document doc = new Document();
        if (u.getId() != null) {
            doc.put("_id", u.getId());
        }
        doc.put("nomeCompleto", u.getNomeCompleto());
        doc.put("cpf", u.getCpf());
        doc.put("email", u.getEmail());
        doc.put("cargo", u.getCargo());
        doc.put("login", u.getLogin());
        doc.put("senha", u.getSenha());
        doc.put("perfil", u.getPerfil() != null ? u.getPerfil().name() : null);
        return doc;
    }

    private Document toUpdateDocument(Usuario u) {
        Document doc = new Document();
        doc.put("nomeCompleto", u.getNomeCompleto());
        doc.put("cpf", u.getCpf());
        doc.put("email", u.getEmail());
        doc.put("cargo", u.getCargo());
        doc.put("login", u.getLogin());
        doc.put("senha", u.getSenha());
        doc.put("perfil", u.getPerfil() != null ? u.getPerfil().name() : null);
        return new Document("$set", doc);
    }

    private Usuario fromDocument(Document d) {
        if (d == null) return null;
        Usuario u = new Usuario();
        u.setId(d.getObjectId("_id"));
        u.setNomeCompleto(d.getString("nomeCompleto"));
        u.setCpf(d.getString("cpf"));
        u.setEmail(d.getString("email"));
        u.setCargo(d.getString("cargo"));
        u.setLogin(d.getString("login"));
        u.setSenha(d.getString("senha"));
        String perfilStr = d.getString("perfil");
        if (perfilStr != null) {
            try {
                u.setPerfil(Usuario.Perfil.valueOf(perfilStr));
            } catch (IllegalArgumentException e) {
                u.setPerfil(null);
            }
        }
        return u;
    }

    @Override
    public void salvar(Usuario obj) {
        try {
            Document doc = toDocument(obj);
            collection.insertOne(doc);
            ObjectId id = doc.getObjectId("_id");
            obj.setId(id);
        } catch (MongoException e) {
            System.err.println("[ERRO][UsuarioDAO] Falha ao salvar: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Usuario buscarPorId(ObjectId id) {
        try {
            Document d = collection.find(Filters.eq("_id", id)).first();
            return fromDocument(d);
        } catch (MongoException e) {
            System.err.println("[ERRO][UsuarioDAO] Falha ao buscar por id: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                lista.add(fromDocument(cursor.next()));
            }
        } catch (MongoException e) {
            System.err.println("[ERRO][UsuarioDAO] Falha ao listar: " + e.getMessage());
            throw e;
        }
        return lista;
    }

    @Override
    public void atualizar(Usuario obj) {
        if (obj.getId() == null) {
            throw new IllegalArgumentException("Id do usuário não pode ser nulo para atualizar.");
        }
        try {
            collection.updateOne(Filters.eq("_id", obj.getId()), toUpdateDocument(obj));
        } catch (MongoException e) {
            System.err.println("[ERRO][UsuarioDAO] Falha ao atualizar: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deletar(ObjectId id) {
        try {
            collection.deleteOne(Filters.eq("_id", id));
        } catch (MongoException e) {
            System.err.println("[ERRO][UsuarioDAO] Falha ao deletar: " + e.getMessage());
            throw e;
        }
    }
}
