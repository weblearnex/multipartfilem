package com.multipartfile.multipartfile.Utils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;
import com.multipartfile.multipartfile.model.Product;


public class UploadFile {
	
	public static String uploadFile(MultipartFile file, String category,String productName,String defaultFilePath) throws Exception {
		
		String filePath = "";
		if (file.isEmpty()) {
			throw new Exception("Error while saving file");
		}
		try {
			String message=createFolder(defaultFilePath, category,productName);
			if(!(StringUtils.isEmpty(message) && message.equalsIgnoreCase("folder created successfulluy"))||(StringUtils.isEmpty(message))) {
			byte[] bytes = file.getBytes();
			if(file.getOriginalFilename()!=null)
			filePath = defaultFilePath + "/" + category+ "/"+productName+"/"+ file.getOriginalFilename();
			System.out.println(filePath);
			Path path = Paths.get(filePath);
			Files.write(path, bytes);
			}else {
				throw new Exception("Error while saving file");
			}
		} catch (IOException e) {
			throw new Exception("Error while saving file");
		}
		return filePath;
	}
	
	private static String createFolder(String folderpath, String category,String productName) {
		
		String message = "";
		File folder = new File(folderpath + "/" + category);
		
		if (!folder.exists()) {
			if (folder.mkdir()) {
				message = "folder created successfulluy";
			} else {
				message = "folder alredy existed";
			}
		}
		
		File file = new File(folderpath + "/" + category+"/"+productName);
		if (!file.exists()) {
			if (file.mkdir()) {
				message = "folder created successfulluy";
			} else {
				message = "folder alredy existed";
			}
		}
		return message;
	}
	
	public static Product convertStringToProduct(String productString) {
		Product p = null;
		try {
			Gson g = new Gson();
			p = g.fromJson(productString, Product.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;

	}

	public static String  creatStaticPath(String category,String productName,String fileName) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(category+"/"+productName+"/"+fileName).toUriString();
	}
	
	public static String  creatStaticURL(String fullpath ,String category,String productName,String fileName) {
		return fullpath+"/"+ category+"/"+productName+"/"+fileName;
		
	}
}