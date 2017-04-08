package com.palyfight.TravelBlogApi;

import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class ImageService {
	private Logger logger = Logger.getLogger(ImageService.class);
	private Cloudinary cloudinary;
	
	ImageService(){
		if(this.cloudinary == null){
			this.cloudinary = new Cloudinary(ObjectUtils.asMap(
					  "cloud_name", System.getenv("CLOUD_NAME"),
					  "api_key", System.getenv("CLOUDINARY_API_KEY"),
					  "api_secret", System.getenv("CLOUDINARY_API_SECRET")));
		}
	}
	
	public String upload(String path){
		String secureUrl = "";
		try {
			Map<?, ?> uploadResult = cloudinary.uploader().upload(path, ObjectUtils.emptyMap());
			secureUrl = uploadResult.get("secure_url").toString();
		} catch (IOException e) {
			logger.log(Level.ERROR, "A problem occurred during the uploading. " + e.getMessage());
		}
		return secureUrl;
	}
}
