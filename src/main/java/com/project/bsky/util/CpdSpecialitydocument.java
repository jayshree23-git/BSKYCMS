package com.project.bsky.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.PropertySource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author rajendra.sahoo
 *
 */
@PropertySource(value = "classpath:application.properties")
@SuppressWarnings("unused")
public class CpdSpecialitydocument {
	
	private static ResourceBundle bskyAppResourcesBundel = ResourceBundle.getBundle("application");
	public static String windowsRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.windows");
	public static String linuxRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.linux");

	private static ResourceBundle bskyResourcesBundel4 = ResourceBundle.getBundle("fileConfiguration");
	
	private static String fileprefix = bskyResourcesBundel4.getString("file.CPDSpecialityDoc.prefix");
	private static String filepath=bskyResourcesBundel4.getString("file.path.CPDSpecialityDoc");
	private static String foldername = bskyResourcesBundel4.getString("folder.CPDScecialityDoc");
	public static String operatingSystem = System.getProperty("os.name").toLowerCase().trim();
	
	public static String createFileforcpdspeciality( String year,MultipartFile form,Long userId, String packagename) {
		Timestamp instant = Timestamp.from(Instant.now());
		String form1 = null;
       try {       	
	       if (form != null)
	    	   form1 = fileprefix +"_"+packagename+"_"+year+"_"+
	       instant.toString().replaceAll("[,-.:\\s]", "") +  getOriginalfilenameExtension(form.getOriginalFilename());
			
	       if (form1 != null)
				dynamicFileUploadDoc(form, year,userId,form1);

		} catch (Exception e) {
			System.out.println(e);
		}	
	return form1;
   	
   }
	public static String dynamicFileUploadDoc(MultipartFile file, String year,Long userId,String newFileName) throws IOException {
		File fileData = null;
		String path = foldername+"/"+filepath+"_"+userId;
		if (operatingSystem.contains("windows")) {
			 fileData = new File(windowsRootFolder + path); 
		}else {
			 fileData = new File(linuxRootFolder + path);
		}
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
	    String last = split[split.length - 1];
	    String s="."+last;
	    return s.trim();
 }
	public static void downloadcpdspecdoc(String fileName, String userid, HttpServletResponse response) throws IOException {
		String path = foldername.trim() + "/"+filepath+"_"+userid.trim()+"/"+ fileName;
		File file = new File(CommonFileUpload.getDocumentPath(path));
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
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
}
