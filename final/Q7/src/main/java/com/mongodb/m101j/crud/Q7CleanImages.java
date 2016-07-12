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
 * Created by dave on 7/11/16.
 */
public class Q7CleanImages {
    static MongoClient client;
    static MongoDatabase database;
    static MongoCollection<Document> images;
    static MongoCollection<Document> albums;

    public static void main(String[] args) {
        client = new MongoClient();
        database = client.getDatabase("q7");
        images = database.getCollection("images");
        albums = database.getCollection("albums");

        List<Document> allImages = images.find()
                .into(new ArrayList<Document>());

        System.out.println("Images before clean: " + allImages.size());

        for (Document curImage : allImages) {
            CleanImageID(curImage.getInteger("_id"));
        }

        System.out.println("Images after clean: " + images.count());

    }

    private static void CleanImageID(int iClean)
    {
        Bson filter = eq("images", iClean);

        List<Document> allAlbums = albums.find(filter)
                .into(new ArrayList<Document>());

        if (allAlbums.size() == 0) {
            // This image doesn't have any albums, clean it
            images.deleteOne(eq("_id",iClean));
        }
    }

}

