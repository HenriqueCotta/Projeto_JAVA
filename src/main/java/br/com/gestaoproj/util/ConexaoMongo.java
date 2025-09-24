package br.com.gestaoproj.util;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class ConexaoMongo {

    private static final String DEFAULT_URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "gestao_projetos";

    private static volatile ConexaoMongo instance;
    private MongoClient client;
    private MongoDatabase database;

    private ConexaoMongo() {
        try {
            client = MongoClients.create(DEFAULT_URI);
            database = client.getDatabase(DB_NAME);
        } catch (MongoException e) {
            System.err.println("[ERRO] Falha ao conectar ao MongoDB: " + e.getMessage());
            throw e;
        }
    }

    private static ConexaoMongo getInstance() {
        if (instance == null) {
            synchronized (ConexaoMongo.class) {
                if (instance == null) {
                    instance = new ConexaoMongo();
                }
            }
        }
        return instance;
    }

    public static MongoDatabase getDatabase() {
        return getInstance().database;
    }

    public static void close() {
        ConexaoMongo i = instance;
        if (i != null && i.client != null) {
            try {
                i.client.close();
            } catch (Exception e) {
                System.err.println("[AVISO] Erro ao fechar conexão Mongo: " + e.getMessage());
            }
        }
    }
}
