package com.mongodb.m101j.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;


import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.all;
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

        // Get all documents that have entries in their scores array w\ Homework
        Bson filter = eq("scores.type", "homework");

        List<Document> students = collection.find(filter)
                .into(new ArrayList<Document>());

        // Iterate through the list of students
        for (Document student : students) {
            printJson(student);

            // Iterate through the list of scores to find the lowest score
            List<Document> scores = (List<Document>)student.get("scores");
            double lowestScore = Double.MAX_VALUE;
            for(Document curScore : scores) {
                String type = curScore.getString("type");
                double delScore = curScore.getDouble("score");
                if ( (type.equals("homework") && (delScore < lowestScore)) ) {
                    lowestScore = delScore;
                }
            }

            // Now delete the lowest score from the array
            Bson match = eq("_id", student.getInteger("_id"));
            Bson update = eq("scores", eq("score", lowestScore));
            collection.updateOne(match, Updates.pullByFilter(update));

            System.out.print("Lowest homework: " + lowestScore);
        }

    }

}
