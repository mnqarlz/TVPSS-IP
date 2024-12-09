package com.tvpss.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnectionManager {

    private static MongoClient mongoClient;
    private static final String DB_URI = "mongodb+srv://mnqarlz:Q%40rl%21na0245@tvpss-db.w3hwf.mongodb.net/tvpss-db?retryWrites=true&w=majority";
    private static final String DB_NAME = "tvpss-db-staging";

    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(DB_URI);
        }
        return mongoClient;
    }

    public static MongoDatabase getDatabase() {
        return getMongoClient().getDatabase(DB_NAME);
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
