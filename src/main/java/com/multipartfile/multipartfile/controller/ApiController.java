package com.multipartfile.multipartfile.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.multipartfile.multipartfile.Utils.UploadFile;
import com.multipartfile.multipartfile.model.Product;



@RestController
public class ApiController {

	
	@Value("${file.upload}")
	private String defaultFilePath;
	
	@GetMapping
	public String getMessage(){
		return "application up and running";
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/product")
	public Product saveProduct(@RequestParam(value="data")String data,  @RequestParam("mainImage") MultipartFile mainImage) throws Exception  {
		if(mainImage==null || mainImage.getOriginalFilename()==null ){
			System.out.println("File not found");
		}
		System.out.println(data);
		Product p = UploadFile.convertStringToProduct(data);
		UploadFile.uploadFile(mainImage, p.getCategory(), p.getProductName() ,defaultFilePath);
		String staticPath = UploadFile.creatStaticPath(p.getCategory(), p.getProductName(),mainImage.getOriginalFilename());
		p.setImagePath(staticPath);	
		return p;
		}
	
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@GetMapping("/{category}/{product}/{fileName:.+}")
		public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,@PathVariable("category") String category,@PathVariable("product") String product, @PathVariable("fileName") String fileName) throws  IOException {
			File file = new File(UploadFile.creatStaticURL(defaultFilePath,category,product,fileName));
			
			if (file.exists()) {
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}
				response.setContentType(mimeType);
				//eg Content-Disposition: inline; filename="filename.pdf"
				response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
				response.setContentLength((int) file.length());
				
				InputStream inputStream;
				try {
					inputStream = new BufferedInputStream(new FileInputStream(file));
					FileCopyUtils.copy(inputStream, response.getOutputStream());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	
	
}
