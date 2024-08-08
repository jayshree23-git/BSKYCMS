/**
 * 
 */
package com.project.bsky.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;

import java.time.Instant;
import java.time.Year;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * @author priyanka.singh
 *
 */
public class METriggerUtil {
	
	private static ResourceBundle bskyAppResourcesBundel = ResourceBundle.getBundle("application");
	public static String windowsRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.windows");
	public static String linuxRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.linux");

	private static ResourceBundle bskyResourcesBundel6 = ResourceBundle.getBundle("fileConfiguration");
	public static String operatingSystem = System.getProperty("os.name").toLowerCase().trim();
	
	@Value("${pdf.documentPathForurl}")
	private String documentPathForurl;
	
	@Value("${pdf.documentPathForCommon}")
	private String documentPathForTemp;

	public static Response METriggerDocument(MultipartFile pdf,String year) {
		Response response=new Response();
		System.out.println(pdf+"file comes at fileconfiguration");
		Timestamp instant = Timestamp.from(Instant.now());
		String form1 = null;
		  try {
		       	
		       if (pdf != null) {
		    	   form1 = pdf.getOriginalFilename();	
		       }

				if (form1 != null) {
					dynamicFileUploadDoc(pdf,year,bskyResourcesBundel6.getString("folder.METrigger"),form1);
				}
				response.setStatus("200");
				response.setMessage(pdf.getOriginalFilename());

				} catch (Exception e) {					
					e.printStackTrace();
					response.setStatus("400");
					response.setMessage("Something Went Wrong");
				}	
			return response;
	}

	private static Object dynamicFileUploadDoc(MultipartFile pdf,String yeardata,String folderName, String newFileName) throws IOException {
		File fileData = null;
		String path = folderName+"/"+yeardata ;
		if (operatingSystem.contains("windows")) {
			 fileData = new File(windowsRootFolder + path); 
		}else {
			 fileData = new File(linuxRootFolder + path);
		}
		//File fileData = new File(windowsRootFolder + path);
	
		fileData.getParentFile().mkdirs();
		return dynamic(pdf, path, newFileName);
		
	}

	private static String dynamic(MultipartFile pdf, String path, String newFileName)throws IOException {
		String fileFlag = "";
		if (pdf.isEmpty()) {
			fileFlag = "FileEmpty";
		} else {
			String folderPath = fileExistsOrNot(path);
			byte[] bytes = pdf.getBytes();
			Path path1 = Paths.get(folderPath.trim() + "/" + newFileName.trim());// file.getOriginalFilename());
			Files.write(path1, bytes);
			fileFlag = folderPath + newFileName;// file.getOriginalFilename();
		}
		 if (operatingSystem.contains("windows")) {
				
				return fileFlag.replace(windowsRootFolder, "");
			}else {
				return fileFlag.replace(linuxRootFolder, "");
				 
			}
//		return null;
	}

	private static String fileExistsOrNot(String path) {
		String filePath = "";
		String targetFile = "";
		String result;
		if (operatingSystem.contains("windows")) {
			filePath = windowsRootFolder + path;
			targetFile = windowsRootFolder;
		} else if (operatingSystem.contains("nix") || operatingSystem.contains("nux")
				|| operatingSystem.contains("aix")) {
			filePath = linuxRootFolder + path;
			targetFile = linuxRootFolder;
		}else {
			filePath = linuxRootFolder + path;
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

	private static String getOriginalfilenameExtension(String originalFilename) {
		  String[] split = originalFilename.split("\\.");
		    // get last string
		    String last = split[split.length - 1];
		    String s="."+last;
		    ////System.out.println(s);
		    return s.trim();
	}

	public static void downloadDischargeDocForMe(String fileCode,  HttpServletResponse response) throws IOException{
		try {
			Year year = Year.now();
			String yeardata =year.toString();
			String path =null;
			if (operatingSystem.contains("windows")) {
				path = windowsRootFolder + bskyResourcesBundel6.getString("folder.METrigger")+"/"+yeardata;
			} else if (operatingSystem.contains("nix") || operatingSystem.contains("nux")
					|| operatingSystem.contains("aix")) {
				path = linuxRootFolder + bskyResourcesBundel6.getString("folder.METrigger")+"/"+yeardata;
			}else {
				path = linuxRootFolder + bskyResourcesBundel6.getString("folder.METrigger")+"/"+yeardata;
			}
			File file = new File(path + "/" + fileCode);
			System.out.println(file+"comes fule");
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
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
