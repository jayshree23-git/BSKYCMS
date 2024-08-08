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

import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartFile;

@PropertySource(value = "classpath:application.properties")
@SuppressWarnings("unused")
public class DocUtill {
	private static ResourceBundle bskyAppResourcesBundel = ResourceBundle.getBundle("application");
	
	public static String windowsRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.windows");
	public static String linuxRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.linux");
	private static ResourceBundle bskyResourcesBundel1 = ResourceBundle.getBundle("fileConfiguration");

//	private static String hosBillPrefix = bskyResourcesBundel.getString("file.AdditionalDoc.prefix");

	private static String Additional1prefix = bskyResourcesBundel1.getString("file.moredocument.prefix");
	private static String Additional2prefix = bskyResourcesBundel1.getString("file.needmoredocument.prefix");
	private String Additionaldoc1 = bskyResourcesBundel1.getString("file.path.Additionaldoc1");
    private String Additionaldoc2 = bskyResourcesBundel1.getString("file.path.Additionaldoc2");
    public static String operatingSystem = System.getProperty("os.name").toLowerCase().trim();
    private static String Approvedprefix = bskyResourcesBundel1.getString("file.requestApprove.prefix");
	private String Approveddoc = bskyResourcesBundel1.getString("file.path.Approveddoc");
    public static Map<String, String> createFile1(String urnNumber,
			 String year, String hospitalCode,MultipartFile additionaldoc1,MultipartFile additionaldoc2) {
		Map<String, String> filePath = new HashMap<String, String>();
		Timestamp instant = Timestamp.from(Instant.now());

		String fileAdditionaldoc1 = null;
	    String fileAdditionaldoc2 = null;

        try {

        	if (additionaldoc1 != null)
        		fileAdditionaldoc1 = Additional1prefix + "_"
        				+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
        				+ instant.toString().replaceAll("[,-.:\\s]", "") +  getOriginalfilenameExtension(additionaldoc1.getOriginalFilename());

        if (additionaldoc2 != null)
        	fileAdditionaldoc2 = Additional2prefix + "_"
        				+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
        				+ instant.toString().replaceAll("[,-.:\\s]", "") +  getOriginalfilenameExtension(additionaldoc2.getOriginalFilename());

		filePath.put("Additionaldoc1", fileAdditionaldoc1);
		filePath.put("Additionaldoc2", fileAdditionaldoc2);

		if (fileAdditionaldoc1 != null)
			dynamicFileUploadDoc(additionaldoc1, year, hospitalCode,
					bskyResourcesBundel1.getString("folder.Additionaldoc1"), fileAdditionaldoc1);
		
		if (fileAdditionaldoc2 != null)
			dynamicFileUploadDoc(additionaldoc2, year, hospitalCode,
					bskyResourcesBundel1.getString("folder.Additionaldoc2"), fileAdditionaldoc2);

		} catch (Exception e) {
			// TODO: handle exception
		}	
				return filePath;
    }
    
	public static String dynamicFileUploadDoc(MultipartFile file, String year, String hospitalcode, String folderName,
			String newFileName) throws IOException {
		File fileData = null;
		String path = year + "/" + hospitalcode + "/" + folderName;
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
				String folderPath = fileExistsOrNot(fileName).trim();
				byte[] bytes = file.getBytes();
				Path path = Paths.get(folderPath.trim() + "/" + newFileName.trim());// file.getOriginalFilename());
				////System.out.println("path"+path);
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
	
	public static Map<String, String> saveFile(String urnNumber,String year, String hospitalCode,MultipartFile ApproveDoc) {
		Map<String, String> filePath = new HashMap<String, String>();
		Timestamp instant = Timestamp.from(Instant.now());

		String fileApprovedoc = null;

       try {

       	if (ApproveDoc != null)
       		fileApprovedoc = Approvedprefix + "_"
       				+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
       				+ instant.toString().replaceAll("[,-.:\\s]", "") +  getOriginalfilenameExtension(ApproveDoc.getOriginalFilename());

       

		filePath.put("SystemRejectedDoc", fileApprovedoc);

		if (fileApprovedoc != null)
			dynamicFileUploadDoc(ApproveDoc, year, hospitalCode,
					bskyResourcesBundel1.getString("folder.Approveddoc"), fileApprovedoc);
		

		} catch (Exception e) {
			// TODO: handle exception
		}	
		return filePath;
   }
	/**
     * @author HrusiKesh Mohanty
     * @since 11.12.2022
     * @param get Extension for original file name 
     * @return
     */
	 public static String getOriginalfilenameExtension(String filename) {
		    String[] split = filename.split("\\.");
		    // get last string
		    String last = split[split.length - 1];
		    String s="."+last;
		    ////System.out.println(s);
		    return s.trim();
	 }
	 
	 
//		@Auther : Hrusikesh Mohanty
//		@Date : 11-01-2023
	// pupose to get the filepath
		
		public static String getFullDocumentPath(String fileName, String year, String hospitalCode,  String folderName) {
			return getDocumentPath1(year + "/" + hospitalCode + "/" + folderName.trim() + "/" + fileName);
		}

//		@Auther : Hrusikesh Mohanty
//		@Date : 11-01-2023
	// pupose to get the filepath
		public static String getDocumentPath1(String filePath){
			String docPath = "";
			if (operatingSystem.contains("win")) {
				docPath = windowsRootFolder.trim()+filePath.trim();
			}
			else if (operatingSystem.contains("nix") || operatingSystem.contains("nux") || operatingSystem.indexOf("aix") > 0 ) {
				docPath = linuxRootFolder.trim()+filePath.trim();
			}
			return docPath;
		}
//		@Auther : Hrusikesh Mohanty
//		@Date : 11-01-2023
	// pupose to get the folderpath
		public static String getFolderName(String fileName){
			String newName = fileName.split("_")[0];
//			////System.out.println("File Prefix Name : "+newName);
			String folderName;
			if (newName.equals(bskyResourcesBundel1.getString("file.moredocument.prefix"))) {
				folderName = bskyResourcesBundel1.getString("folder.Additionaldoc1");
			} else if (newName.equals(bskyResourcesBundel1.getString("file.needmoredocument.prefix"))) {
				folderName = bskyResourcesBundel1.getString("folder.Additionaldoc2");
			}else {
				folderName = "AdmissionSlip";
			}
//			////System.out.println("Folder Name : "+folderName);
			return folderName;
		}
		public static Map<String, String> createFile2(String urnNumber,
				 String year, String hospitalCode,MultipartFile additionaldoc1,MultipartFile additionaldoc2) {
			Map<String, String> filePath = new HashMap<String, String>();
			Timestamp instant = Timestamp.from(Instant.now());

			String fileAdditionaldoc1 = null;
		    String fileAdditionaldoc2 = null;

	        try {

	        	if (additionaldoc1 != null)
	        		fileAdditionaldoc1 = Additional1prefix + "_"
	        				+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
	        				+ instant.toString().replaceAll("[,-.:\\s]", "") +  getOriginalfilenameExtension(additionaldoc1.getOriginalFilename());

	        if (additionaldoc2 != null)
	        	fileAdditionaldoc2 = Additional2prefix + "_"
	        				+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
	        				+ instant.toString().replaceAll("[,-.:\\s]", "") +  getOriginalfilenameExtension(additionaldoc2.getOriginalFilename());

			filePath.put("Additionaldoc1", fileAdditionaldoc1);
			filePath.put("Additionaldoc2", fileAdditionaldoc2);

			if (fileAdditionaldoc1 != null)
				dynamicFileUploadDoc(additionaldoc1, year, hospitalCode,
						bskyResourcesBundel1.getString("folder.Additionaldoc1"), fileAdditionaldoc1);
			
			if (fileAdditionaldoc2 != null)
				dynamicFileUploadDoc(additionaldoc2, year, hospitalCode,
						bskyResourcesBundel1.getString("folder.Additionaldoc2"), fileAdditionaldoc2);

			} catch (Exception e) {
				// TODO: handle exception
			}	
					return filePath;
	    }
}