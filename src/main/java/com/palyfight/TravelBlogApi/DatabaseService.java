package com.palyfight.TravelBlogApi;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
			System.out.println("YOLO => " + connStr);
			MongoClientURI uri  = new MongoClientURI(connStr); 
	        this.client = new MongoClient(uri);
	        this.db = client.getDatabase(uri.getDatabase());
		}
	}
	
	public void save(String url1, String url2, String category){
		MongoCollection<Document> images = db.getCollection("PalyTravelCollection");
		Document image = new Document("url", url1).append("compressedUrl", url2).append("category", category);
		images.insertOne(image);
		
		client.close();
	}
}