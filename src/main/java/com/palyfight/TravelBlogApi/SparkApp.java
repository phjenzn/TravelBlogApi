package com.palyfight.TravelBlogApi;

import static spark.Spark.*;

public class SparkApp {

	public static void main(String[] args) {
		get("/trips", (req, res) -> {
			DatabaseService ds = new DatabaseService(); 
			return ds.getTripsPics();
		});
		
		get("/trips/:trip", (req, res) -> {
			DatabaseService ds = new DatabaseService(); 
			return ds.getPicsByTrip(req.params(":trip"));
		});
		
		get("/images", (req, res) -> {
			DatabaseService ds = new DatabaseService(); 
			return ds.getAll();
		});
	}
}