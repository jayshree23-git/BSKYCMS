/**
 * 
 */
package com.project.bsky.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;

/**
 * @author rajendra.sahoo
 *
 */
public class GrievanceDoc {

	private static ResourceBundle bskyAppResourcesBundel = ResourceBundle.getBundle("application");
	public static String windowsRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.windows");
	public static String linuxRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.linux");

	private static ResourceBundle bskyResourcesBundel5 = ResourceBundle.getBundle("fileConfiguration");
	
	private static String Grivanceprefix = bskyResourcesBundel5.getString("file.Internalgrivance.prefix");
	private String Grienvance = bskyResourcesBundel5.getString("file.path.Internalgrivance");
	public static String operatingSystem = System.getProperty("os.name").toLowerCase().trim();
	
	@Value("${pdf.documentPathForurl}")
	private String documentPathForurl;
	
	@Value("${pdf.documentPathForCommon}")
	private String documentPathForTemp;
	
	@Autowired
	private Environment env;
	
	
	public static Map<String, String> createFileforInternalgrivance( String year,MultipartFile form,String Month,String userid) {
		Map<String, String> filePath = new HashMap<String, String>();
		Timestamp instant = Timestamp.from(Instant.now());

		String form1 = null;

       try {
       	
       if (form != null)
    	   form1 = Grivanceprefix + "_"+ year+"_"+Month+"_"
       				+ instant.toString().replaceAll("[,-.:\\s]", "") +  getOriginalfilenameExtension(form.getOriginalFilename());

		filePath.put("Grivance", form1);
		

		if (form1 != null)
			dynamicFileUploadDoc(form, year,Month,
					bskyResourcesBundel5.getString("folder.Grievance"),form1);

		} catch (Exception e) {
			// TODO: handle exception
			////System.out.println(e);
		}	
	return filePath;
   	
   }
	public static String dynamicFileUploadDoc(MultipartFile file, String year,String Month, String folderName,
			String newFileName) throws IOException {
		File fileData = null;
		String path = folderName+"/"+year + "/"+Month ;
		if (operatingSystem.contains("windows")) {
			 fileData = new File(windowsRootFolder + path); 
		}else {
			 fileData = new File(linuxRootFolder + path);
		}
		//File fileData = new File(windowsRootFolder + path);
	
		fileData.getParentFile().mkdirs();
		return dynamic(file, path, newFileName);
	}
	public static String dynamic(MultipartFile file, String fileName, String newFileName) throws IOException {
		String fileFlag = "";
		if (file.isEmpty()) {
			fileFlag = "FileEmpty";
		} else {
			String folderPath = fileExistsOrNot(fileName);
			byte[] bytes = file.getBytes();
			Path path = Paths.get(folderPath.trim() + "/" + newFileName.trim());// file.getOriginalFilename());
			Files.write(path, bytes);
			fileFlag = folderPath + newFileName;// file.getOriginalFilename();
		}
		 if (operatingSystem.contains("windows")) {
				
				return fileFlag.replace(windowsRootFolder, "");
			}else {
				return fileFlag.replace(linuxRootFolder, "");
				 
			}
		//return fileFlag.replace(windowsRootFolder, "");
	}
	public static String fileExistsOrNot(String folderName) {
		String filePath = "";
		String targetFile = "";
		String result;
		if (operatingSystem.contains("windows")) {
			filePath = windowsRootFolder + folderName;
			targetFile = windowsRootFolder;
		} else if (operatingSystem.contains("nix") || operatingSystem.contains("nux")
				|| operatingSystem.contains("aix")) {
			filePath = linuxRootFolder + folderName;
			targetFile = linuxRootFolder;
		}else {
			filePath = linuxRootFolder + folderName;
			targetFile = linuxRootFolder;
		}


		// Checking Root Directory is Present or Not
		File file = new File(targetFile);
		if (!file.exists()) {
			Scanner scanner = new Scanner(System.in);
			////System.out.println("Root Directory is not Present. Please Create Root Directory\n"+ "Press Y to Create Root Directory\n");

			char response = scanner.next().charAt(0);
			if (Character.toUpperCase(response) == 'Y') {
				boolean checkCreated = file.mkdir();
				if (checkCreated) {
					File file1 = new File(filePath);
					if (!file1.exists()) {
						boolean checkCreated1 = file1.mkdir();
						if (checkCreated1) {
							result = "Success";
							return filePath + "/";
						} else {
							result = "Failed";
							return filePath + "/";
						}
					}
				}
			} else {
				result = "Failed";
			}
		} else {
			File file1 = new File(filePath);
			if (!file1.exists()) {
				boolean checkCreated2 = file1.mkdir();
				if (checkCreated2) {
					result = "Success";
					return filePath + "/";
				} else {
					result = "Failed";
					return filePath + "/";
				}
			}
		}
		return filePath;
	}
	
	public static String getOriginalfilenameExtension(String filename) {
	    String[] split = filename.split("\\.");
	    // get last string
	    String last = split[split.length - 1];
	    String s="."+last;
	    ////System.out.println(s);
	    return s.trim();
 }
	
	public Response savemskgrivdoc(MultipartFile multipart,String docpath){
		Response response=new Response(); 
		try {
			Timestamp tt = new Timestamp(System.currentTimeMillis());
			String fileNameForType = (multipart.getOriginalFilename());
			String[] fileArray = fileNameForType.split("[.]");
			String actualType = fileArray[fileArray.length - 1];
			
			File consoleLogoFile = new File(docpath);
			if (!consoleLogoFile.exists()) {
				consoleLogoFile.mkdir();
			}			
				try {
					byte[] bytes = multipart.getBytes();
					try (OutputStream stream2 = new FileOutputStream(
							docpath + tt.getTime() + "." + actualType)) {
						stream2.write(bytes);
					}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				response.setStatus("200");
				response.setMessage(tt.getTime() + "." + actualType);

		}catch (Exception e) {
			e.printStackTrace();
			response.setStatus("400");
			response.setMessage("File Not Save");
		}
		return response;
	}
	
	public static Integer supportfiledoc(MultipartFile file) {
		Integer status=0;
		try {
			// Check if the selected file has an allowed extension
            String fileName = file.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            //System.out.println(fileName);

            // List of allowed extensions
            String[] allowedExtensions = { "pdf", "jpg", "jpeg", "png", "docx"}; 
            
            Integer filetype=0;
            for (String ext : allowedExtensions) {
                if (fileExtension.equalsIgnoreCase(ext)) {
                	filetype = 1;
                    break;
                }
            }
            if(filetype==1) {
            	if((double)file.getSize() / (1024 * 1024)>5) {
            		status=2;
            	}else {status=0;}
            }else {
            	status=1;
            }
		}catch (Exception e) {
			status=3;
		}
		return status;		
	}
	
	public static Integer supportfilevdo(MultipartFile file) {
		Integer status=0;
		try {
			// Check if the selected file has an allowed extension
            String fileName = file.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

            // List of allowed extensions
            String[] allowedExtensions1 = { "mp4", "mp3"}; 
            
            Integer filetype=0;
            for (String ext : allowedExtensions1) {
                if (fileExtension.equalsIgnoreCase(ext)) {
                	filetype = 1;
                    break;
                }
            }
            if(filetype==1) {
            	if((double)file.getSize() / (1024 * 1024)>5) {
            		status=2;
            	}else {status=0;}
            }else {
            	status=1;
            }
		}catch (Exception e) {
			status=3;
		}
		return status;		
	}
	public static void downgrivdoc(String fileName, HttpServletResponse response,String foldername) throws IOException {			
			File file = new File(foldername+ "/" + fileName); 
			System.out.println(file);
			if (!file.exists()) {
				String errorMessage = "Sorry. The file you are looking for does not exist";
				OutputStream outputStream = response.getOutputStream();
				outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
				outputStream.close();
				return;
			}
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
			response.setContentLength((int) file.length());
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
}
