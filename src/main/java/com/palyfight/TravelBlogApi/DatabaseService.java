package com.palyfight.TravelBlogApi;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

public class DatabaseService {
	private MongoClient client;
	private MongoDatabase db;
	
	DatabaseService(){
		if(this.db == null){
			String dbUser = System.getenv("DB_USER");
			String dbPassword = System.getenv("DB_PASSWORD");
			String dbHost = System.getenv("DB_HOST");
			int dbPort = Integer.parseInt(System.getenv("DB_PORT"));
			String dbName = "travelblogdb";
			
			String connStr = String.format("mongodb://%s:%s@%s:%d/%s", dbUser, dbPassword, dbHost, dbPort, dbName);
			MongoClientURI uri  = new MongoClientURI(connStr); 
	        this.client = new MongoClient(uri);
	        this.db = client.getDatabase(uri.getDatabase());
		}
	}
	
	public void save(String url1, String url2, String category, String tag){
		MongoCollection<Document> images = db.getCollection("PalyTravelCollection");
		Document image = new Document("url", url1).append("compressedUrl", url2).append("category", category).append("tag", tag);
		images.insertOne(image);
		System.out.println("Saved image!");
		client.close();
	}
	
	public JsonArray getAll(){
		MongoCollection<Document> images = db.getCollection("PalyTravelCollection");
		String results = JSON.serialize(images.find());
		JsonArray obj = (new JsonParser()).parse(results).getAsJsonArray();
		client.close();
		return obj;
	}
	
	public JsonArray getTripsPics(){
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("tag", "main");
		MongoCollection<Document> images = db.getCollection("PalyTravelCollection");
		String results = JSON.serialize(images.find(whereQuery));
		JsonArray pics = (new JsonParser()).parse(results).getAsJsonArray();
		client.close();
		return pics;
	}
	
	public JsonArray getPicsByTrip(String trip){
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("tag", "pics"));
		obj.add(new BasicDBObject("category", trip));
		andQuery.put("$and", obj);
		
		MongoCollection<Document> images = db.getCollection("PalyTravelCollection");
		String results = JSON.serialize(images.find(andQuery));
		JsonArray pics = (new JsonParser()).parse(results).getAsJsonArray();
		client.close();
		return pics;
	}
}