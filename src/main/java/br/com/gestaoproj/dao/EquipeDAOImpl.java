package br.com.gestaoproj.dao;

import br.com.gestaoproj.model.Equipe;
import br.com.gestaoproj.util.ConexaoMongo;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class EquipeDAOImpl implements EquipeDAO {

    private final MongoCollection<Document> collection;

    public EquipeDAOImpl() {
        this.collection = ConexaoMongo.getDatabase().getCollection("equipes");
    }

    private Document toDocument(Equipe e) {
        Document d = new Document();
        if (e.getId() != null) d.put("_id", e.getId());
        d.put("nome", e.getNome());
        d.put("descricao", e.getDescricao());
        d.put("membrosIds", e.getMembrosIds());
        return d;
    }

    private Document toUpdateDocument(Equipe e) {
        Document set = new Document();
        set.put("nome", e.getNome());
        set.put("descricao", e.getDescricao());
        set.put("membrosIds", e.getMembrosIds());
        return new Document("$set", set);
    }

    @SuppressWarnings("unchecked")
    private Equipe fromDocument(Document d) {
        if (d == null) return null;
        Equipe e = new Equipe();
        e.setId(d.getObjectId("_id"));
        e.setNome(d.getString("nome"));
        e.setDescricao(d.getString("descricao"));
        List<ObjectId> membros = (List<ObjectId>) d.get("membrosIds", List.class);
        e.setMembrosIds(membros != null ? membros : new ArrayList<>());
        return e;
    }

    @Override
    public void salvar(Equipe obj) {
        try {
            Document d = toDocument(obj);
            collection.insertOne(d);
            obj.setId(d.getObjectId("_id"));
        } catch (MongoException e) {
            System.err.println("[ERRO][EquipeDAO] Falha ao salvar: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Equipe buscarPorId(ObjectId id) {
        try {
            Document d = collection.find(Filters.eq("_id", id)).first();
            return fromDocument(d);
        } catch (MongoException e) {
            System.err.println("[ERRO][EquipeDAO] Falha ao buscar por id: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Equipe> listarTodos() {
        List<Equipe> lista = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                lista.add(fromDocument(cursor.next()));
            }
        } catch (MongoException e) {
            System.err.println("[ERRO][EquipeDAO] Falha ao listar: " + e.getMessage());
            throw e;
        }
        return lista;
    }

    @Override
    public void atualizar(Equipe obj) {
        if (obj.getId() == null) {
            throw new IllegalArgumentException("Id da equipe não pode ser nulo para atualizar.");
        }
        try {
            collection.updateOne(Filters.eq("_id", obj.getId()), toUpdateDocument(obj));
        } catch (MongoException e) {
            System.err.println("[ERRO][EquipeDAO] Falha ao atualizar: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deletar(ObjectId id) {
        try {
            collection.deleteOne(Filters.eq("_id", id));
        } catch (MongoException e) {
            System.err.println("[ERRO][EquipeDAO] Falha ao deletar: " + e.getMessage());
            throw e;
        }
    }
}
