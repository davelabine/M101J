package com.mongodb.m101j.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.m101j.util.Helpers.printJson;

/**
 * Created by dave on 6/5/16.
 */
public class RemoveLowestHomework {
    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("students");
        MongoCollection<Document> collection = database.getCollection("grades");

        //Bson filter = and(eq("x", 0), gt("y", 10), lt("y", 90));

        Bson filter = eq("type", "homework");
        Bson sort = ascending("student_id", "score");

        List<Document> all = collection.find(filter)
                                        .sort(sort)
                                        .into(new ArrayList<Document>());

        int last_id = -1; // initialize to an invalid student ID
        for (Document cur : all) {
                // Find out the lowest score of the homework for each new student
                int current_id = cur.getInteger("student_id");

                if (current_id != last_id) {
                    // we haven't seen this before, delete it

                    // there MUST be an easier way to do this... :(
                    ObjectId delId = (ObjectId)cur.get("_id");
                    Bson del = eq("_id", delId);
                    collection.deleteOne(del);

                    // set the new student ID to the current ID so we don't delete any more...
                    last_id = current_id;
                }
        }

        for (Document cur : all) {
            printJson(cur);
        }

        long count = collection.count(filter);
        System.out.println();
        System.out.println(count);
    }
}
