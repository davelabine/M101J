package com.mongodb.m101j.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.m101j.util.Helpers.printJson;

/**
 * Created by davidl on 6/10/16.
 */
public class RemoveLowestHomework2 {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("school");
        MongoCollection<Document> collection = db.getCollection("students");

        List<Document> all = collection.find();

        // Iterate through all of the student data
        for (Document cur : all) {
            printJson(cur);
        }

        long count = collection.count();
        System.out.println();
        System.out.println(count);
    }

    // http://stackoverflow.com/questions/10345788/retrieve-sub-document-in-array-as-dbobjects

    static void PruneHomework(Document doc)
    {
        Bson filter = eq("type", "homework");
        Bson sort = ascending("student_id", "score");

        List<Document> all = doc.
                .sort(sort)
                .into(new ArrayList<Document>());
    }

}
