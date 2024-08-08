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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@PropertySource(value = "classpath:application.properties")
@SuppressWarnings("unused")
public class UserMenualFileUpdation {
	// -------------------File Content Read from application.properties
	private static ResourceBundle bskyAppResourcesBundel = ResourceBundle.getBundle("application");
	public static String windowsRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.windows");
	public static String linuxRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.linux");
	// ----------------------------------------
	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");
	private static String treatPrefix = bskyResourcesBundel.getString("file.DischargeSlip.prefix");
	private static String usermanulaPrefix = bskyResourcesBundel.getString("file.Usermanual.prefix");
	private static String postSurgPrefix = bskyResourcesBundel.getString("file.postsurgery-pic.prefix");
	private static String preSurgPrefix = bskyResourcesBundel.getString("file.presurgery-pic.prefix");
	private static String hosBillPrefix = bskyResourcesBundel.getString("file.AdditionalDoc.prefix");
	private static String IntraPrefix = bskyResourcesBundel.getString("file.Intrasurgery.prefix");
	private static String SpecimenPrefix = bskyResourcesBundel.getString("file.Specimen.prefix");
	private static String PatientPicPrefix = bskyResourcesBundel.getString("file.Patient.prefix");
	private String DischargeSlip = bskyResourcesBundel.getString("file.path.DischargeSlip");
	private String postSurgFilePath = bskyResourcesBundel.getString("file.path.presurgery");
	private String preSurgFilePath = bskyResourcesBundel.getString("file.path.postsurgery");
	private String hosBillFilePath = bskyResourcesBundel.getString("file.path.AdditionalDoc");
	private String IntraFilePath = bskyResourcesBundel.getString("file.path.IntraSurgery");
	private String SpecimenFilePath = bskyResourcesBundel.getString("file.path.SpecimenRemoval");
	private String Patient = bskyResourcesBundel.getString("file.path.PatientPic");
	private static final String investigationDocFile1Prefix = bskyResourcesBundel
			.getString("file.investigationDoc1.prefix");
	private static final String investigationDocFile2Prefix = bskyResourcesBundel
			.getString("file.investigationDoc2.prefix");
	private static final String cceDocFile1Prefix = bskyResourcesBundel.getString("file.cceDOc1.prefix");
	private static final String cceDocFile2Prefix = bskyResourcesBundel.getString("file.cceDOc2.prefix");
	private static final String cceDocFile3Prefix = bskyResourcesBundel.getString("file.cceDOc3.prefix");
	private static final String DGOPrefix = bskyResourcesBundel.getString("file.dgOfficer.prefix");
	public static String floatFolder = bskyResourcesBundel.getString("file.path.FloatDoc");
	public static String operatingSystem = System.getProperty("os.name").toLowerCase().trim();
	public static Logger log = LoggerFactory.getLogger(CommonFileUpload.class);
	private static final String ReferralPrefix = bskyResourcesBundel.getString("file.referral.prefix");

	// Check For Folder Is Exists or Not
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
		} else {
			filePath = linuxRootFolder + folderName;
			targetFile = linuxRootFolder;
		}

		// Checking Root Directory is Present or Not
		File file = new File(targetFile);
		if (!file.exists()) {
			Scanner scanner = new Scanner(System.in);
			// //System.out.println("Root Directory is not Present. Please Create Root
			// Directory\n"+ "Press Y to Create Root Directory\n");

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
	/**
	 * 
	 * @author HrusiKesh Mohanty
	 * @since 11.12.2022
	 * @param get Extension for original file name
	 * @return
	 */
	public static String getOriginalfilenameExtension(String filename) {
		String[] split = filename.split("\\.");
		// get last string
		String last = split[split.length - 1];
		String s = "." + last;
		// //System.out.println(s);
		return s.trim();
	}
	/*
	 * @author Ipsita Shaw date:06-09-2022
	 * 
	 * description:-Inroder to create a map of file path and and its corresponding
	 * key
	 */
	public static Map<String, String> createFile(MultipartFile selectedFile) {
		Map<String, String> filePath = new HashMap<String, String>();
		Timestamp instant = Timestamp.from(Instant.now());
		String fileTreatmentString = null;

		try {
			if (selectedFile != null)
				fileTreatmentString = usermanulaPrefix + "_"+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(selectedFile.getOriginalFilename());
		
			filePath.put("Usermanual", fileTreatmentString);
			if (fileTreatmentString != null)
				dynamicFileUpload(selectedFile,bskyResourcesBundel.getString("folder.Usermanual"),
						fileTreatmentString);

		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return filePath;
	}
	public static String dynamicFileUpload(MultipartFile file, String folderName,String newFileName) throws IOException {
		File fileData = null;
		String path =folderName;
		if (operatingSystem.contains("windows")) {
			fileData = new File(windowsRootFolder + path);
		} else {
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
		} else {
			return fileFlag.replace(linuxRootFolder, "");

		}
	}
	
//	@Auther : Hrusikesh Mohanty
//	@Date : 11-01-2023
// pupose to get the filepath

	public static String getFullDocumentPath(String fileName,String folderName) {
		return getDocumentPath1(folderName.trim() + "/" + fileName);
	}
//	@Auther : Hrusikesh Mohanty
//	@Date : 11-01-2023
// pupose to get the filepath
	public static String getDocumentPath1(String filePath) {
		String docPath = "";
		if (operatingSystem.contains("win")) {
			docPath = windowsRootFolder.trim() + filePath.trim();
		} else if (operatingSystem.contains("nix") || operatingSystem.contains("nux")
				|| operatingSystem.indexOf("aix") > 0) {
			docPath = linuxRootFolder.trim() + filePath.trim();
		}
		return docPath;
	}
	
//	@Auther : Hrusikesh Mohanty
//	@Date : 11-01-2023
// pupose to get the folderpath
	public static String getFolderName(String fileName) {
		String newName = fileName.split("_")[0];
//		////System.out.println("File Prefix Name : "+newName);
		String folderName;
		if (newName.equals(bskyResourcesBundel.getString("file.presurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.presurg.photo");
		} else if (newName.equals(bskyResourcesBundel.getString("file.postsurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.postsurg.photo");
		} else if (newName.equals(bskyResourcesBundel.getString("file.DischargeSlip.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.DischargeSlip");
		} else if (newName.equals(bskyResourcesBundel.getString("file.AdditionalDoc.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.AdditionalDoc");
		} else if (newName.equals(bskyResourcesBundel.getString("file.Specimen.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.SpecimenRemovalPic.photo");
		} else if (newName.equals(bskyResourcesBundel.getString("file.Patient.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.PatientPic");
		} else if (newName.equals(bskyResourcesBundel.getString("file.moredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc1");
		} else if (newName.equals(bskyResourcesBundel.getString("file.needmoredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc2");
		} else if (newName.equals(bskyResourcesBundel.getString("file.Intrasurgery.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.IntraSurgeryPic.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.investigationDoc1.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.investigation1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.investigationDoc2.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.investigation2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Usermanual.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Usermanual");
		} else {
			folderName = "PREAUTHDOC";
		}
		return folderName;
	}
	/***********
	 * @author Hrusikesh Mohnaty
	 * @date 04/10/2023
	 * @description -To download files
	 ***********/
	public static void downloadFilenewenrollment(String filePath,String folderName,HttpServletResponse response) throws IOException {
		String path =folderName.trim()+ "/" + filePath;
		//System.out.println(path);
		//System.out.println("Pre Auth Doc Path : " + UserMenualFileUpdation.getDocumentPathenrollment(path));
		File file = new File(UserMenualFileUpdation.getDocumentPathenrollment(path));
		// //System.out.println(file.exists());
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
	
	
	/***********
	 * @author Hrusikesh Mohnaty
	 * @date 04/10/2023
	 * @description -To download files
	 ***********/
	public static void downloadFilenewenrollmentdownload(String filePath,String folderName,HttpServletResponse response) throws IOException {
		String path =folderName.trim()+ "/" + filePath;
		//System.out.println(path);
		//System.out.println("Pre Auth Doc Path : " + UserMenualFileUpdation.getDocumentPathenrollment(path));
		File file = new File(UserMenualFileUpdation.getDocumentPathenrollment(path));
		// //System.out.println(file.exists());
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
	/**
	 * @author Hrusikesh Mohnaty
	 * @since 04.10.2023
	 * @param filePath
	 * @return
	 */
	public static String getDocumentPathenrollment(String filePath) {
		String docPath = "";
		if (operatingSystem.indexOf("win") >= 0) {
			docPath = windowsRootFolder.trim() + filePath.trim();
		} else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0) {
			docPath = linuxRootFolder.trim() + filePath.trim();
		}
		return docPath;
	}
}
