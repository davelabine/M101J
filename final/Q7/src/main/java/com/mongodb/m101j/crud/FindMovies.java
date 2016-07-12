package com.mongodb.m101j.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.m101j.util.Helpers.printJson;

/**
 * Created by dave on 6/6/16.
 */
public class FindMovies {
    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("video");
        MongoCollection<Document> collection = database.getCollection("movieDetails");


//        Bson filter = new Document("x", 0)
//        .append("y", new Document("$gt", 10).append("$lt", 90));

        // Filter for Homework 2.5
        // Bson filter = and(eq("year", 2013), eq("rated", "PG-13"), eq("awards.wins", 0));

        Bson filter = in("countries.1", "Sweden");

        List<Document> all = collection.find(filter).into(new ArrayList<Document>());

        for (Document cur : all) {
            printJson(cur);
        }

        long count = collection.count(filter);
        System.out.println();
        System.out.println(count);
    }
}
