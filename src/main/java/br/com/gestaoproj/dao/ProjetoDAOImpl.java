package br.com.gestaoproj.dao;

import br.com.gestaoproj.model.Projeto;
import br.com.gestaoproj.util.ConexaoMongo;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAOImpl implements ProjetoDAO {

    private final MongoCollection<Document> collection;

    public ProjetoDAOImpl() {
        this.collection = ConexaoMongo.getDatabase().getCollection("projetos");
    }

    private String toIso(LocalDate date) {
        return date != null ? date.toString() : null;
    }

    private LocalDate parseIso(String s) {
        return s != null && !s.isBlank() ? LocalDate.parse(s) : null;
    }

    private Document toDocument(Projeto p) {
        Document d = new Document();
        if (p.getId() != null) d.put("_id", p.getId());
        d.put("nome", p.getNome());
        d.put("descricao", p.getDescricao());
        d.put("dataInicio", toIso(p.getDataInicio()));
        d.put("dataTerminoPrevista", toIso(p.getDataTerminoPrevista()));
        d.put("status", p.getStatus() != null ? p.getStatus().name() : null);
        d.put("gerenteResponsavelId", p.getGerenteResponsavelId());
        return d;
    }

    private Document toUpdateDocument(Projeto p) {
        Document set = new Document();
        set.put("nome", p.getNome());
        set.put("descricao", p.getDescricao());
        set.put("dataInicio", toIso(p.getDataInicio()));
        set.put("dataTerminoPrevista", toIso(p.getDataTerminoPrevista()));
        set.put("status", p.getStatus() != null ? p.getStatus().name() : null);
        set.put("gerenteResponsavelId", p.getGerenteResponsavelId());
        return new Document("$set", set);
    }

    private Projeto fromDocument(Document d) {
        if (d == null) return null;
        Projeto p = new Projeto();
        p.setId(d.getObjectId("_id"));
        p.setNome(d.getString("nome"));
        p.setDescricao(d.getString("descricao"));
        p.setDataInicio(parseIso(d.getString("dataInicio")));
        p.setDataTerminoPrevista(parseIso(d.getString("dataTerminoPrevista")));
        String status = d.getString("status");
        if (status != null) {
            try { p.setStatus(Projeto.Status.valueOf(status)); } catch (IllegalArgumentException e) { p.setStatus(null); }
        }
        ObjectId gerenteId = d.getObjectId("gerenteResponsavelId");
        p.setGerenteResponsavelId(gerenteId);
        return p;
    }

    @Override
    public void salvar(Projeto obj) {
        try {
            Document d = toDocument(obj);
            collection.insertOne(d);
            obj.setId(d.getObjectId("_id"));
        } catch (MongoException e) {
            System.err.println("[ERRO][ProjetoDAO] Falha ao salvar: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Projeto buscarPorId(ObjectId id) {
        try {
            Document d = collection.find(Filters.eq("_id", id)).first();
            return fromDocument(d);
        } catch (MongoException e) {
            System.err.println("[ERRO][ProjetoDAO] Falha ao buscar por id: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Projeto> listarTodos() {
        List<Projeto> lista = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                lista.add(fromDocument(cursor.next()));
            }
        } catch (MongoException e) {
            System.err.println("[ERRO][ProjetoDAO] Falha ao listar: " + e.getMessage());
            throw e;
        }
        return lista;
    }

    @Override
    public void atualizar(Projeto obj) {
        if (obj.getId() == null) {
            throw new IllegalArgumentException("Id do projeto não pode ser nulo para atualizar.");
        }
        try {
            collection.updateOne(Filters.eq("_id", obj.getId()), toUpdateDocument(obj));
        } catch (MongoException e) {
            System.err.println("[ERRO][ProjetoDAO] Falha ao atualizar: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deletar(ObjectId id) {
        try {
            collection.deleteOne(Filters.eq("_id", id));
        } catch (MongoException e) {
            System.err.println("[ERRO][ProjetoDAO] Falha ao deletar: " + e.getMessage());
            throw e;
        }
    }
}
