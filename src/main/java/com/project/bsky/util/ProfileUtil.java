package com.project.bsky.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.springframework.web.multipart.MultipartFile;

public class ProfileUtil {

	private static String docname="BSKY_Profile_";
	
	private static ResourceBundle bskyAppResourcesBundel = ResourceBundle.getBundle("application");
	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	public static String windowsRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.windows");
	public static String linuxRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.linux");
	public static String profileFolder = bskyResourcesBundel.getString("file.path.ProfilePhoto");

	public static String operatingSystem = System.getProperty("os.name").toLowerCase().trim();

	public static String getProfilePhotoPath() {
		if (operatingSystem.indexOf("win") >= 0) {
			return windowsRootFolder.trim() + profileFolder + "/";
		} else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0) {
			return linuxRootFolder.trim() + profileFolder + "/";
		} else {
			return null;
		}
	}
	
	public static Map<String, String> createFileforprofile( MultipartFile form,String userid) {
		Map<String, String> filePath = new HashMap<String, String>();
		String form1 = null;

       try {
       	
       if (form != null)
    	   form1 =docname+ userid+  getOriginalfilenameExtension(form.getOriginalFilename());

		filePath.put("Notification", form1);
		

		if (form1 != null)
			dynamicFileUploadDoc(form,profileFolder,form1);

		} catch (Exception e) {
			// TODO: handle exception
		}	
	return filePath;
   	
   }
	
	public static String dynamicFileUploadDoc(MultipartFile file,  String folderName,
			String newFileName) throws IOException {
		File fileData = null;
		String path = folderName ;
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
}
