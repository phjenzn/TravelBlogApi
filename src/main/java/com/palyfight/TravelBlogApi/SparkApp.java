package com.palyfight.TravelBlogApi;

import static spark.Spark.*;

public class SparkApp {

	public static void main(String[] args) {
		get("/images", (req, res) -> {
			DatabaseService ds = new DatabaseService(); 
			return ds.getAll();
		});
	}
}