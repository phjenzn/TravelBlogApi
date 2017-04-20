package com.palyfight.TravelBlogApi;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;

public class ImageService {
	private Logger logger = Logger.getLogger(ImageService.class);
	private Cloudinary cloudinary;

	ImageService() {
		Tinify.setKey(System.getenv("TINY_API_KEY"));
		
		if (this.cloudinary == null) {
			this.cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", System.getenv("CLOUD_NAME"), "api_key",
					System.getenv("CLOUDINARY_API_KEY"), "api_secret", System.getenv("CLOUDINARY_API_SECRET")));
		}
	}

	public String upload(String path) {
		String secureUrl = "";
		try {
			Map<?, ?> uploadResult = cloudinary.uploader().upload(path, ObjectUtils.emptyMap());
			secureUrl = uploadResult.get("secure_url").toString();
		} catch (IOException e) {
			logger.log(Level.ERROR, "A problem occurred during the uploading from a url. " + e.getMessage());
		} finally {
			System.out.println("Successfully uploaded image from: " + path + " to cloudinary!!!");
		}
		return secureUrl;
	}

	public String upload(File file) {
		String secureUrl = "";
		try {
			Map<?, ?> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
			secureUrl = uploadResult.get("secure_url").toString();
		} catch (IOException e) {
			logger.log(Level.ERROR, "A problem occurred during the uploading from a local file. " + e.getMessage());
		} finally {
			System.out.println("Successfully uploaded image from: " + file.getPath() + " to cloudinary!!!");
		}
		return secureUrl;
	}

	public String compress(String path, int from) {
		String compressedUrl = "";
		Source source = null;
		
		switch (from) {
		case 1: // local file
			try {
				source = Tinify.fromFile(path);
			} catch (IOException e) {
				logger.log(Level.ERROR, "Could not locate the specified file at this location: " + path + "\n\n" + e.getMessage());
			}
			break;
		case 2: // file from url
			source = Tinify.fromUrl(path);
			break;
		}
		
		try {
			Options options = new Options().with("method", "cover").with("width", 475.5).with("height", 400);
			Source resized = source.resize(options);
			resized.toFile("compressed.jpg");
		} catch (IOException e) {
			logger.log(Level.ERROR, "Could not save the compressed image. " + e.getMessage());
		} finally {
			System.out.println("Successfully compressed image from: " + path + " thanks to Tinify!!!");
		}
		
		if(source != null){
			File file = new File("compressed.jpg");
			compressedUrl = upload(file);
		}
		return compressedUrl;
	}
}