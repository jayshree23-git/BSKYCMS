package com.project.bsky.util;
/*
 * @author - Hrushikesh Mohanty
 * date:05/09/2022
 * desc:
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.functors.WhileClosure;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@PropertySource(value = "classpath:application.properties")
@SuppressWarnings("unused")
public class CommonFileUpload {
// -------------------File Content Read from application.properties
	private static ResourceBundle bskyAppResourcesBundel = ResourceBundle.getBundle("application");
	public static String windowsRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.windows");
	public static String linuxRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.linux");
// ----------------------------------------
	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");
	private static String treatPrefix = bskyResourcesBundel.getString("file.DischargeSlip.prefix");
	private static String postSurgPrefix = bskyResourcesBundel.getString("file.postsurgery-pic.prefix");
	private static String preSurgPrefix = bskyResourcesBundel.getString("file.presurgery-pic.prefix");
	private static String hosBillPrefix = bskyResourcesBundel.getString("file.AdditionalDoc.prefix");
	private static String IntraPrefix = bskyResourcesBundel.getString("file.Intrasurgery.prefix");
	private static String SpecimenPrefix = bskyResourcesBundel.getString("file.Specimen.prefix");
	private static String PatientPicPrefix = bskyResourcesBundel.getString("file.Patient.prefix");
//------------------------------------------------------------------------------------------------------------------------------
// float file upload section
	private static String SNACertificationPrefix = bskyResourcesBundel.getString("file.SnaCertification.prefix");
	private static String MECertificationPicPrefix = bskyResourcesBundel.getString("file.MECertification.prefix");
	private static String OtherFilePrefix = bskyResourcesBundel.getString("file.OtherFile.prefix");
//-------------------------------------------------------------------------------------------------------------------------------- 
	private String SnaCertification = bskyResourcesBundel.getString("file.path.SnaCertification");
	private String MECertification = bskyResourcesBundel.getString("file.path.MECertification");
	private String OtherFile = bskyResourcesBundel.getString("file.path.OtherFile");
//------------------------------------------------------------------------------------------------------------------------------
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

// Upload Single File into Local Folder
	public static String singleFileUplaod(MultipartFile file, String fileName) throws IOException {
		String fileFlag = "";
		if (file.isEmpty()) {
			fileFlag = "FileEmpty";
		} else {
			String folderPath = fileExistsOrNot(fileName).trim();
			byte[] bytes = file.getBytes();
			Path path = Paths.get(folderPath.trim() + "/" + file.getOriginalFilename().trim());
			Files.write(path, bytes);
			fileFlag = folderPath + file.getOriginalFilename();
		}
		if (operatingSystem.contains("windows")) {

			return fileFlag.replace(windowsRootFolder, "");
		} else {
			return fileFlag.replace(linuxRootFolder, "");

		}
// return fileFlag.replace(windowsRootFolder, "");
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
// return fileFlag.replace(windowsRootFolder, "");
	}

	/*
	 * @author Ipsita Shaw date:06-09-2022
	 * 
	 * description:-Inroder to create a map of file path and and its corresponding
	 * key
	 */
	public static Map<String, String> createFile(String urnNumber, MultipartFile presurgery, MultipartFile postsurgery,
			MultipartFile treatmentDetailsSlip, MultipartFile hospitalBill, MultipartFile intasurgery,
			MultipartFile specimansurgery, MultipartFile patientpic, String year, String hospitalCode) {

		Map<String, String> filePath = new HashMap<String, String>();
		Timestamp instant = Timestamp.from(Instant.now());
		String fileTreatmentString = null;
		String filePostSurgery = null;
		String filePreSurgery = null;
		String fileHospitalBill = null;
		String fileIntraSurgery = null;
		String fileSpecimenRemoval = null;
		String filepatientpic = null;

		try {
//            if(TMSdischargedocumnet.equalsIgnoreCase("null")) {
			if (treatmentDetailsSlip != null)
				fileTreatmentString = treatPrefix + "_"
						+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(treatmentDetailsSlip.getOriginalFilename());
//            }else {
//            fileTreatmentString=TMSdischargedocumnet; 
//            }
			if (postsurgery != null)
				filePostSurgery = postSurgPrefix + "_"
						+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(postsurgery.getOriginalFilename());

			if (presurgery != null)
				filePreSurgery = preSurgPrefix + "_"
						+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(presurgery.getOriginalFilename());

			if (hospitalBill != null)
				fileHospitalBill = hosBillPrefix + "_"
						+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(hospitalBill.getOriginalFilename());

			if (intasurgery != null)
				fileIntraSurgery = IntraPrefix + "_"
						+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(intasurgery.getOriginalFilename());

			if (specimansurgery != null)
				fileSpecimenRemoval = SpecimenPrefix + "_"
						+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(specimansurgery.getOriginalFilename());

			if (patientpic != null)
				filepatientpic = PatientPicPrefix + "_"
						+ urnNumber.substring(urnNumber.length() - 6).replaceAll("[,-.:\\s]", "") + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(patientpic.getOriginalFilename());

			filePath.put("DischargeSlip", fileTreatmentString);
			filePath.put("presurgery", filePreSurgery);
			filePath.put("postsurgery", filePostSurgery);
			filePath.put("AdditionalDoc", fileHospitalBill);
			filePath.put("IntraSurgeryPic", fileIntraSurgery);
			filePath.put("SpecimenRemovalPic", fileSpecimenRemoval);
			filePath.put("PatientPic", filepatientpic);

// if(TMSdischargedocumnet.equalsIgnoreCase("null")) {
			if (fileTreatmentString != null)
				dynamicFileUpload(treatmentDetailsSlip, year, hospitalCode,
						bskyResourcesBundel.getString("folder.DischargeSlip"), fileTreatmentString);
// }

			if (filePostSurgery != null)
				dynamicFileUpload(postsurgery, year, hospitalCode,
						bskyResourcesBundel.getString("folder.patient.postsurg.photo"), filePostSurgery);

			if (filePreSurgery != null)
				dynamicFileUpload(presurgery, year, hospitalCode,
						bskyResourcesBundel.getString("folder.patient.presurg.photo"), filePreSurgery);

			if (fileHospitalBill != null)
				dynamicFileUpload(hospitalBill, year, hospitalCode,
						bskyResourcesBundel.getString("folder.AdditionalDoc"), fileHospitalBill);

			if (fileIntraSurgery != null)
				dynamicFileUpload(intasurgery, year, hospitalCode,
						bskyResourcesBundel.getString("folder.patient.IntraSurgeryPic.photo"), fileIntraSurgery);

			if (fileSpecimenRemoval != null)
				dynamicFileUpload(specimansurgery, year, hospitalCode,
						bskyResourcesBundel.getString("folder.patient.SpecimenRemovalPic.photo"), fileSpecimenRemoval);

			if (filepatientpic != null)
				dynamicFileUpload(patientpic, year, hospitalCode, bskyResourcesBundel.getString("folder.PatientPic"),
						filepatientpic);

		} catch (IOException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;

	}

	public static String dynamicFileUpload(MultipartFile file, String year, String hospitalcode, String folderName,
			String newFileName) throws IOException {
		File fileData = null;
		String path = year + "/" + hospitalcode + "/" + folderName;
		if (operatingSystem.contains("windows")) {
			fileData = new File(windowsRootFolder + path);
		} else {
			fileData = new File(linuxRootFolder + path);
		}
// File fileData = new File(windowsRootFolder + path);
		fileData.getParentFile().mkdirs();
		return dynamic(file, path, newFileName);
	}

// download file for Claim processing Hospitaall
	public static byte[] downLoadFileCLAIM(String filePath, String year, String hospitalcode, String folderName)
			throws IOException {
		byte[] fileContent = null;
		File file = null;
// Doc doc = service.getFile(fileId).get();
		String path = year + "/" + hospitalcode + "/" + folderName + "/" + filePath;
// //System.out.println(windowsRootFolder + path + ".jpg");
// File file = new File(windowsRootFolder + path + ".jpg");
		if (operatingSystem.contains("windows")) {
			file = new File(windowsRootFolder + path);
		} else {
			file = new File(linuxRootFolder + path);
		}
// file.le
// //System.out.println(file.exists());
		if (!file.exists()) {
			return null;
		}

		FileInputStream fl = new FileInputStream(file);
		byte[] arr = new byte[(int) file.length()];
		fl.read(arr);
		fl.close();
// //System.out.println(arr.toString());
// ////System.out.println(fileContent);
		return arr;

	}

// for date formating
	public static String dateformate(String Date) throws ParseException {
		String s = Date;
		String s1 = s.substring(0, 2);
		String s2 = s.substring(2, 4);
		String s3 = s.substring(4, s.length());
		String ssString = s1 + "/" + s2 + "/" + s3;
		Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(ssString);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String d = sdf.format(date1);
		return d;
	}

	/*****
	 * From CLOB to String* @return string representation of clob
	 *****/
	public static String clobToString(java.sql.Clob data) {
		final StringBuilder sb = new StringBuilder();

		try {
			final Reader reader = data.getCharacterStream();
			final BufferedReader br = new BufferedReader(reader);

			int b;
			while (-1 != (b = br.read())) {
				sb.append((char) b);
			}

			br.close();
		} catch (SQLException e) {
			log.error("SQL. Could not convert CLOB to string", e);
			return e.toString();
		} catch (IOException e) {
			log.error("IO. Could not convert CLOB to string", e);
			return e.toString();
		}

		return sb.toString();

	}

	public static byte[] downLoadFile1(String filePath, String year, String hospitalcode, String folderName)
			throws IOException {
		byte[] fileContent = null;
// Doc doc = service.getFile(fileId).get();
		String path = year + "/" + hospitalcode + "/" + folderName + "/" + filePath;
// //System.out.println(windowsRootFolder + path + ".jfif");
		File file = new File(windowsRootFolder + path + ".jfif");
// file.le
// //System.out.println(file.exists());
		if (!file.exists()) {
			return null;
		}

		FileInputStream fl = new FileInputStream(file);
		byte[] arr = new byte[(int) file.length()];
		fl.read(arr);
		fl.close();
// //System.out.println(arr.toString());
// ////System.out.println(fileContent);
		return arr;

	}

	public static long calculateNoOfDays(String dtAdmission, String dtDischarge) {

// calculate no of days difference -->noOfDays = dateOfDischarge
// -dateOfAdmission
		Date dateOfAdmission;
		Date dateOfDischarge;
		long difference_In_Days = 0;
		try {
			dateOfAdmission = new SimpleDateFormat("ddMMyyyy").parse(dtAdmission);
			dateOfDischarge = new SimpleDateFormat("ddMMyyyy").parse(dtDischarge);

// Calucalte time difference
// in milliseconds
			long difference_In_Time = dateOfDischarge.getTime() - dateOfAdmission.getTime();

			difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

			if (difference_In_Days == 0) {
				difference_In_Days = difference_In_Days + 1;
			}
// TODO Auto-generated method stub

		} catch (ParseException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return difference_In_Days;
	}

// calculating number of days with year wise
	public static int totalDaysBetweenDates(String fromDate, String toDate) {
		int daysBetween = 0;
		SimpleDateFormat myFormat = new SimpleDateFormat("ddMMyyyy");
		try {
			Date fromDate1 = myFormat.parse(fromDate);
			Date toDate1 = myFormat.parse(toDate);
			long difference = (toDate1.getTime() - fromDate1.getTime());
			daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
// //System.out.println("Number of Days : " + daysBetween);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daysBetween;
	}

	public static Map<String, File> dynamicFilePath(String year, String hospitalcode, String folderName,
			String newFileName) throws IOException {

		File fileData = null;
		String filePath = "";
		Map<String, File> fileMap = new HashMap<String, File>();
		if (newFileName != null) {
			String path = year + "/" + hospitalcode + "/" + folderName + "/" + newFileName;

			fileData = new File(windowsRootFolder + path);
			if (!fileData.exists()) {
				fileData.getParentFile().mkdirs();
				fileData.createNewFile();
				log.info("File is created:" + fileData.getAbsolutePath());
			} else
				log.info("File was already available:" + fileData.getAbsolutePath());

			filePath = fileData.getAbsolutePath();
// fileMap.put(filePath, fileData);
		}

		return fileMap;

	}

	public static String convertTimestampToString(Object date) {

		String dateStr = null;
		if (date == null)
			return null;

		if (java.sql.Timestamp.class.isAssignableFrom(date.getClass())) {
// dateStr = OUT_TIMESTAMP_FORMAT.format(date);
		}
		/*
		 * try { java.sql.Timestamp timestamp = toTimestamp( approvalDetails ); } catch(
		 * ParseException pe ) { pe.printStackTrace(); return null; }
		 */
		/*
		 * java.util.Date date = new
		 * java.util.Date(Long.parseLong(approvalDetails.toString()) * 1000);
		 * 
		 * String dateStr = new
		 * java.text.SimpleDateFormat("dd MMM YYYY H:m:s").format(date);
		 * ////System.out.println(dateStr);
		 */

		/*
		 * SimpleDateFormat dateFormat = new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSS"); Date parsedDate; try {
		 * parsedDate = dateFormat.parse((String)approvalDetails); Timestamp timestamp =
		 * new
		 * java.sql.Timestamp(parsedDate.getTime());//Timestamp.valueOf(stringValue);
		 * 
		 * //Timestamp ts = new Timestamp(new Date().getTime()); SimpleDateFormat format
		 * = new SimpleDateFormat("yyyy-MM-dd HH:mm"); Date fechaNueva =
		 * format.parse(timestamp.toString()); format = new
		 * SimpleDateFormat("dd MMM YYY, HH:mm");
		 * ////System.out.println(format.format(fechaNueva));
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		return dateStr;
	}

	public static Map<String, Integer> createClaimStatusMap() {
		Map<String, Integer> claimMap = new HashMap<String, Integer>();
		claimMap.put(bskyResourcesBundel.getString("claim.approvedTxt"),
				Integer.parseInt(bskyResourcesBundel.getString("claim.approvedValue")));
		claimMap.put(bskyResourcesBundel.getString("claim.rejectedTxt"),
				Integer.parseInt(bskyResourcesBundel.getString("claim.rejectedValue")));
		claimMap.put(bskyResourcesBundel.getString("claim.queryTxt"),
				Integer.parseInt(bskyResourcesBundel.getString("claim.queryValue")));
		claimMap.put(bskyResourcesBundel.getString("claim.paidTxt"),
				Integer.parseInt(bskyResourcesBundel.getString("claim.paidValue")));
		claimMap.put(bskyResourcesBundel.getString("claim.claimRaisedTxt"),
				Integer.parseInt(bskyResourcesBundel.getString("claim.claimRaisedValue")));
		return claimMap;
	}

	/***********
	 * @author Arabinda.guin
	 * @date 11/09/2022
	 * @description -To download files
	 ***********/
	public static void downloadFile(String filePath, String year, String hospitalcode, String folderName,
			HttpServletResponse response) throws IOException {
		String path = year + "/" + hospitalcode + "/" + folderName.trim() + "/" + filePath;
// System.out.println(path);
// System.out.println("Pre Auth Doc Path : " +
// CommonFileUpload.getDocumentPath(path));
		File file = new File(CommonFileUpload.getDocumentPath(path));
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

	public static File downloadMultipleFile(String filePath, String year, String hospitalcode, String folderName,
			HttpServletResponse response) throws IOException {
		String path = year + "/" + hospitalcode + "/" + folderName.trim() + "/" + filePath;
// //System.out.println(CommonFileUpload.getDocumentPath(path));
		File file = new File(CommonFileUpload.getDocumentPath(path));
// //System.out.println(file.exists());
		if (!file.exists()) {
			String errorMessage = "Sorry. The file you are looking for does not exist";
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return null;
		}
		return file;
// String mimeType = URLConnection.guessContentTypeFromName(file.getName());
// if (mimeType == null) {
// mimeType = "application/octet-stream";
// } 
// response.setContentType(mimeType);
// response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
// response.setContentLength((int) file.length());
// InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
//
// FileCopyUtils.copy(inputStream, response.getOutputStream()); 

	}

	/***********
	 * @author Rajendra.sahoo
	 * @date 25/09/2022
	 * @description -To download CPD Passbook file
	 ***********/
	public static void downLoadPassbook(String fileName, String year, HttpServletResponse response, String folderName)
			throws IOException {

		String path = year + "/" + folderName.trim() + "/" + fileName;
// //System.out.println(windowsRootFolder.trim() + path.trim());
		File file = new File(CommonFileUpload.getDocumentPath(path));
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

	/**
	 * @author Arabinda Guin
	 * @since 11.10.2022
	 * @param filePath
	 * @return
	 */
	public static String getDocumentPath(String filePath) {
		String docPath = "";
// //System.out.printf("operatingSystem"+operatingSystem);
		if (operatingSystem.indexOf("win") >= 0) {
			docPath = windowsRootFolder.trim() + filePath.trim();
		} else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0) {
			docPath = linuxRootFolder.trim() + filePath.trim();
		}
// //System.out.printf("path"+docPath);

		return docPath;
	}

	public static String getMergeDocumentPath() {
		String docPath = "";
		if (operatingSystem.indexOf("win") >= 0) {
			docPath = windowsRootFolder.trim();
		} else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0) {
			docPath = linuxRootFolder.trim();
		}
		return docPath;
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

	public static void downLoadNotificationdoc(String fileName, String year, HttpServletResponse response, String month,
			String folderName) throws IOException {
		String path = folderName.trim() + "/" + year + "/" + month + "/" + fileName;
// //System.out.println(windowsRootFolder.trim() + path.trim());
		File file = new File(CommonFileUpload.getDocumentPath(path));
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

	public static void downloadMultiplemergedFile(PDFMergerUtility pdfMerger, HttpServletResponse response)
			throws IOException {
		pdfMerger.setDestinationFileName(CommonFileUpload.getMergeDocumentPath() + "merged_file.pdf");
		pdfMerger.mergeDocuments();
		String path = pdfMerger.getDestinationFileName();
		File fileDemo = new File(path);
		String mimeType = URLConnection.guessContentTypeFromName(fileDemo.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileDemo.getName() + "\""));
		InputStream inputStream = new BufferedInputStream(new FileInputStream(fileDemo));
		FileCopyUtils.copy(inputStream, response.getOutputStream());
		try {
			fileDemo.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

// @Auther : Hrusikesh Mohanty
// @Date : 11-01-2023
// pupose to get the filepath

	public static String getFullDocumentPath(String fileName, String year, String hospitalCode, String folderName) {
		return getDocumentPath1(year + "/" + hospitalCode + "/" + folderName.trim() + "/" + fileName);
	}

// @Auther : Hrusikesh Mohanty
// @Date : 11-01-2023
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

// @Auther : Hrusikesh Mohanty
// @Date : 11-01-2023
// pupose to get the folderpath
	public static String getFolderName(String fileName) {
		String newName = fileName.split("_")[0];
// ////System.out.println("File Prefix Name : "+newName);
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
//-------------------For Float File-------------------------------------------------------------------------------------------------- 
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.SnaCertification.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.SnaCertification.name");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.MECertification.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.MECertification.name");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.OtherFile.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.OtherFile.name");
//------------------------------------------------------------------------------------------------------------------------------------ 
// for Mortality Audit Report --TMS
		} else if (fileName.startsWith("ADT")) {
			folderName = bskyResourcesBundel.getString("folder.MortalityAuditReport");
// for Mortality Report --TMS
		} else if (fileName.startsWith("MOT")) {
			folderName = bskyResourcesBundel.getString("folder.MoralityDoc");
		} else {
			folderName = "PREAUTHDOC";
		}
// ////System.out.println("Folder Name : "+folderName);
		return folderName;
	}

	/*
	 * @Auther : Sambit Kumar Pradhan
	 * 
	 * @Date : 11-01-2023
	 * 
	 * @Purpose : To Download Image Merged File From Server
	 */
	public static void downloadPDFFile(File mergedImageFile, HttpServletResponse httpServletResponse) {
		try {
			String mimeType = URLConnection.guessContentTypeFromName(mergedImageFile.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			httpServletResponse.setContentType(mimeType);
			httpServletResponse.setHeader("Content-Disposition",
					String.format("inline; filename=\"" + mergedImageFile.getName() + "\""));
			httpServletResponse.setContentLength((int) mergedImageFile.length());
			InputStream inputStream = new BufferedInputStream(Files.newInputStream(mergedImageFile.toPath()));

			FileCopyUtils.copy(inputStream, httpServletResponse.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				mergedImageFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getRootPath() {
		if (operatingSystem.contains("win")) {
			return windowsRootFolder.trim();
		} else if (operatingSystem.contains("nix") || operatingSystem.contains("nux")
				|| operatingSystem.indexOf("aix") > 0) {
			return linuxRootFolder.trim();
		}
		return "Operation System Not Found";
	}

	public static void copyFile(File file, String path) throws IOException {
		File dest = new File(path);
		FileCopyUtils.copy(file, dest);
	}

	/*
	 * @Auther : Sambit Kumar Pradhan
	 * 
	 * @Date : 17-02-2023
	 * 
	 * @Purpose : To Save Investigation File To Server Folder
	 */
	public static String saveFile(MultipartFile file, String fileName, String newFileName) throws IOException {
		String fileFlag;
		if (file.isEmpty())
			fileFlag = "File Not Found";
		else {
			String folderPath = fileExistsOrNot(fileName);
			byte[] bytes = file.getBytes();
			Path path = Paths.get(folderPath.trim() + "/" + newFileName.trim());
			Files.write(path, bytes);
			fileFlag = folderPath + "/" + newFileName;
		}
		return fileFlag;
	}

	/*
	 * @Auther : Sambit Kumar Pradhan
	 * 
	 * @Date : 17-02-2023
	 * 
	 * @Purpose : To Create Folder And Save Investigation File
	 */
	public static String uploadInvestigationDoc(String year, MultipartFile investigationDocFile, String hospitalCode,
			String folderName, String newFileName) throws IOException {
		File fileData;
		String path = year + "/" + hospitalCode + "/" + folderName;
		if (operatingSystem.contains("windows"))
			fileData = new File(windowsRootFolder + path);
		else
			fileData = new File(linuxRootFolder + path);
// ////System.out.println("File Path : " + fileData.getAbsolutePath());

		if (!fileData.exists())
			fileData.mkdirs();
		return saveFile(investigationDocFile, path, newFileName);
	}

	/*
	 * @Auther : Sambit Kumar Pradhan
	 * 
	 * @Date : 17-02-2023
	 * 
	 * @Purpose : For Saving Investigation File And Return File Name
	 */
	public static Map<String, String> saveInvestigationFile(String year, String URN, String hospitalCode,
			MultipartFile investigationDoc1, MultipartFile investigationDoc2) {
		Map<String, String> response = new HashMap<>();
		Timestamp instant = Timestamp.from(Instant.now());
		String investigationDoc1Name, investigationDoc2Name;
		try {
			if (investigationDoc1 != null) {
				investigationDoc1Name = investigationDocFile1Prefix.trim() + "_"
						+ URN.trim().replaceAll("[,-.:\\s]", "").substring(URN.length() - 6) + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(Objects.requireNonNull(investigationDoc1.getOriginalFilename()));

				String fileFullPath = uploadInvestigationDoc(year.trim(), investigationDoc1, hospitalCode,
						bskyResourcesBundel.getString("file.path.investigationDoc1"), investigationDoc1Name);

// //System.out.println("Investigation Doc 1 Name : " + investigationDoc1Name);
// //System.out.println("File Full Path : " + fileFullPath);

				if (new File(fileFullPath).exists())
					response.put("investigationDoc1", investigationDoc1Name);
				else
					throw new Exception(
							"Failed to Upload Investigation File 1 : " + investigationDoc1.getOriginalFilename());
			}

			if (investigationDoc2 != null) {
				investigationDoc2Name = investigationDocFile2Prefix.trim() + "_"
						+ URN.trim().replaceAll("[,-.:\\s]", "").substring(URN.length() - 6) + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(Objects.requireNonNull(investigationDoc2.getOriginalFilename()));

				String fileFullPath = uploadInvestigationDoc(year.trim(), investigationDoc2, hospitalCode,
						bskyResourcesBundel.getString("file.path.investigationDoc2"), investigationDoc2Name);

// //System.out.println("Investigation Doc 2 Name : " + investigationDoc2Name);
// //System.out.println("File Full Path : " + fileFullPath);

				if (new File(fileFullPath).exists())
					response.put("investigationDoc2", investigationDoc2Name);
				else
					throw new Exception(
							"Failed to Upload Investigation File 2 : " + investigationDoc2.getOriginalFilename());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = null;
		}
		return response;
	}

	public static void downLoadFileForOverride(String fileName, HttpServletResponse response, String folderName,
			String year, String hCode) throws IOException {
		String year1 = null;
		if (fileName.startsWith("PD")) {
			String s1 = fileName.substring(0, fileName.indexOf("."));
			String s2 = s1.substring(0, s1.length() - 13);
			year1 = s2.substring(s2.length() - 4);
		} else {
			year1 = year;
		}
		String path = year1 + "/" + hCode + "/" + folderName.trim() + "/" + fileName;
// //System.out.println(windowsRootFolder.trim() + path.trim());
		File file = new File(CommonFileUpload.getDocumentPath(path));
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

	public static Map<String, String> saveCCEOUtBoundFile(String year, String urn, String hospitalCode,
			MultipartFile cceDoc1, MultipartFile cceDoc2, MultipartFile cceDoc3) {
		Map<String, String> response = new HashMap<>();
		Timestamp instant = Timestamp.from(Instant.now());
		String cceDocument1, cceDocument2, cceDocument3;
		try {
			if (cceDoc1 != null) {
				cceDocument1 = cceDocFile1Prefix.trim() + "_"
						+ urn.trim().replaceAll("[,-.:\\s]", "").substring(urn.length() - 6) + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(Objects.requireNonNull(cceDoc1.getOriginalFilename()));

				String fileFullPath = uploadInvestigationDoc(year.trim(), cceDoc1, hospitalCode,
						bskyResourcesBundel.getString("file.path.cceOutBoundDoc1"), cceDocument1);

// //System.out.println("CCE Doc 1 Name : " + cceDocument1);
// //System.out.println("File Full Path : " + fileFullPath);

				if (new File(fileFullPath).exists())
					response.put("cceDoc1", cceDocument1);
				else
					throw new Exception("Failed to Upload CCE File 1 : " + cceDoc1.getOriginalFilename());
			}
			if (cceDoc2 != null) {
				cceDocument2 = cceDocFile2Prefix.trim() + "_"
						+ urn.trim().replaceAll("[,-.:\\s]", "").substring(urn.length() - 6) + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(Objects.requireNonNull(cceDoc2.getOriginalFilename()));

				String fileFullPath = uploadInvestigationDoc(year.trim(), cceDoc2, hospitalCode,
						bskyResourcesBundel.getString("file.path.cceOutBoundDoc2"), cceDocument2);

// //System.out.println("CCE Doc 2 Name : " + cceDocument2);
// //System.out.println("File Full Path : " + fileFullPath);

				if (new File(fileFullPath).exists())
					response.put("cceDoc2", cceDocument2);
				else
					throw new Exception("Failed to Upload CCE File 2 : " + cceDoc2.getOriginalFilename());
			}
			if (cceDoc3 != null) {
				cceDocument3 = cceDocFile3Prefix.trim() + "_"
						+ urn.trim().replaceAll("[,-.:\\s]", "").substring(urn.length() - 6) + "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(Objects.requireNonNull(cceDoc3.getOriginalFilename()));

				String fileFullPath = uploadInvestigationDoc(year.trim(), cceDoc3, hospitalCode,
						bskyResourcesBundel.getString("file.path.cceOutBoundDoc3"), cceDocument3);

// //System.out.println("CCE Doc 3 Name : " + cceDocument3);
// //System.out.println("File Full Path : " + fileFullPath);

				if (new File(fileFullPath).exists())
					response.put("cceDoc3", cceDocument3);
				else
					throw new Exception("Failed to Upload CCE File 3 : " + cceDoc3.getOriginalFilename());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = null;
		}
		return response;
	}

	public static Map<String, String> saveReferalDoc(String year, String urn, String hospitalCode, MultipartFile file) {
		Map<String, String> filePath = new HashMap<String, String>();
		Timestamp instant = Timestamp.from(Instant.now());
		String fileReferraldoc = null;
		try {
			if (file != null)
				fileReferraldoc = ReferralPrefix + "_" + urn.substring(urn.length() - 6).replaceAll("[,-.:\\s]", "")
						+ "_" + instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(Objects.requireNonNull(file.getOriginalFilename()));
			String fileFullPath = uploadInvestigationDoc(year.trim(), file, hospitalCode,
					bskyResourcesBundel.getString("file.path.referralDocument"), fileReferraldoc);

// //System.out.println("File Name : " + fileReferraldoc);
// //System.out.println("File Full Path : " + fileFullPath);

			if (new File(fileFullPath).exists())
				filePath.put("file", fileReferraldoc);
			else
				throw new Exception("Failed to Upload File  : " + file.getOriginalFilename());
// filePath.put("ReferralDocument", fileApprovedoc);
//
// if (fileApprovedoc != null)
// uploadInvestigationDoc(year.trim(), file, hospitalCode,
// bskyResourcesBundel.getString("folder.ReferralDocument"), fileApprovedoc);
		} catch (Exception e) {
// TODO: handle exception
		}
		return filePath;
	}

	public static Map<String, String> saveDGODoc(String year, String urn, String hospitalCode, MultipartFile dgoDoc) {
		Map<String, String> response = new HashMap<String, String>();
		Timestamp instant = Timestamp.from(Instant.now());
		String fileDGODoc;
		try {
			if (dgoDoc != null) {
				fileDGODoc = DGOPrefix.trim() + "_" + urn.trim().replaceAll("[,-.:\\s]", "").substring(urn.length() - 6)
						+ "_" + instant.toString().replaceAll("[,-.:\\s]", "")
						+ getOriginalfilenameExtension(Objects.requireNonNull(dgoDoc.getOriginalFilename()));

				String fileFullPath = uploadInvestigationDoc(year.trim(), dgoDoc, hospitalCode,
						bskyResourcesBundel.getString("file.path.dgoDocument"), fileDGODoc);

// //System.out.println("DGO Doc Name : " + fileDGODoc);
// //System.out.println("File Full Path : " + fileFullPath);

				if (new File(fileFullPath).exists())
					response.put("dgoDoc", fileDGODoc);
				else
					throw new Exception("Failed to Upload Investigation File 1 : " + dgoDoc.getOriginalFilename());
			}

		} catch (Exception e) {
			e.printStackTrace();
			response = null;
		}
		return response;
	}

	public static void downLoadGrivancedoc(String fileName, String year, HttpServletResponse response, String month,
			String folderName) throws IOException {
		String path = folderName.trim() + "/" + year + "/" + month + "/" + fileName;
// //System.out.println(windowsRootFolder.trim() + path.trim());
		File file = new File(CommonFileUpload.getDocumentPath(path));
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

	public static void downLoadintcommunicationdoc(String fileName, String year, HttpServletResponse response,
			String month, String folderName) throws IOException {
		String path = folderName.trim() + "/" + year + "/" + month + "/" + fileName;
// //System.out.println(windowsRootFolder.trim() + path.trim());
		File file = new File(CommonFileUpload.getDocumentPath(path));
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

	/***********
	 * @author Arabinda.guin
	 * @date 05/05/2023
	 * @description -To download Old files
	 ***********/
	public static void downloadOldFile(String filePath, String year, String hospitalcode, HttpServletResponse response)
			throws IOException {
		String path = "OLD" + "/" + year + "/" + hospitalcode + "/" + "TBL_TRANSACTIONDOCSOSTF" + "/" + filePath;
// //System.out.println(path);
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

	public static Map<String, String> createFileforFloat(MultipartFile file, String floatNo) {
		Map<String, String> filePath = new HashMap<String, String>();
		String file1 = null;
		String currentTime = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
		try {

			if (file != null)
				floatNo = floatNo.replace("/", "");
			file1 = floatNo + "_" + currentTime + getOriginalfilenameExtension(file.getOriginalFilename());

			filePath.put("Notification", file1);

			if (file1 != null)
				dynamicFileUploadDoc(file, floatFolder + "/" + floatNo, file1);

		} catch (Exception e) {
// TODO: handle exception
		}
		return filePath;

	}

	public static String dynamicFileUploadDoc(MultipartFile file, String folderName, String newFileName)
			throws IOException {
		File fileData = null;
		String path = folderName;
		if (operatingSystem.contains("windows")) {
			fileData = new File(windowsRootFolder + path);
		} else {
			fileData = new File(linuxRootFolder + path);
		}
// File fileData = new File(windowsRootFolder + path);

		fileData.getParentFile().mkdirs();
		return dynamic(file, path, newFileName);
	}

	/*
	 * @Auther : Sambit Kumar Pardhan
	 * 
	 * @Date : 20-07-2023
	 * 
	 * @Description : To download old data document
	 */
	public static void downloadOldDataDocument(String fileName, HttpServletResponse response, String folderName,
			String year, String hospitalCode) {
		try {
			String path = year + "/" + hospitalCode + "/" + folderName.trim() + "/" + fileName;
			File file = new File(CommonFileUpload.getDocumentPath(path));

			if (!file.exists()) {
				String errorMessage = "Sorry. The file you are looking for does not exist";
				OutputStream outputStream = response.getOutputStream();
				outputStream.write(errorMessage.getBytes(StandardCharsets.UTF_8));
				outputStream.close();
				return;
			}
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null)
				mimeType = "application/octet-stream";

			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
			response.setContentLength((int) file.length());
			InputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void downloadFloatFile(String filePath, String floatNumber, String folderName,
			HttpServletResponse response) throws IOException {
		try {
			String path = folderName.trim() + "/" + floatNumber.replace("/", "") + "/" + filePath;
// System.out.println(path);
// System.out.println(CommonFileUpload.getDocumentPath(path));
			File file = new File(CommonFileUpload.getDocumentPath(path));
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
		} catch (Exception e) {
// TODO: handle exception
			e.printStackTrace();
		}

	}

	/***********
	 * @author Hrusikesh Mohnaty
	 * @date 04/10/2023
	 * @description -To download files
	 ***********/
	public static void downloadFileenrollment(String filePath, String enrollmentfolder, String year,
			String hospitalcode, String Statecode, String district, String Block, String folderName,
			HttpServletResponse response) throws IOException {
		String path = enrollmentfolder + "/" + year + "/" + hospitalcode + "/" + Statecode + "/" + district + "/"
				+ Block + "/" + folderName.trim() + "/" + filePath;
// System.out.println(path);
// System.out.println("Pre Auth Doc Path : " +
// CommonFileUpload.getDocumentPathenrollment(path));
		File file = new File(CommonFileUpload.getDocumentPathenrollment(path));
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

	/**
	 * @author Hrusikesh Mohnaty
	 * @since 04.10.2023
	 * @param filePath
	 * @return
	 */
	public static String getDocumentPathenrollment(String filePath) {
		String docPath = "";
// //System.out.printf("operatingSystem"+operatingSystem);
		if (operatingSystem.indexOf("win") >= 0) {
			docPath = windowsRootFolder.trim() + filePath.trim();
		} else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0) {
			docPath = linuxRootFolder.trim() + filePath.trim();
		}
// //System.out.printf("path"+docPath);

		return docPath;
	}

	public static void downloadsmreviewdoc(String fileName, String year, HttpServletResponse response,
			String hospitalcode) throws IOException {
		String newName = fileName.split("_")[0];
		String folderName = "";
		if (newName.equals(bskyResourcesBundel.getString("file.smadmissionreview.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.SMAdmissionReviewDoc");
		} else if (newName.equals(bskyResourcesBundel.getString("file.smdischargereview.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.SMDischargeReviewDoc");
		} else if (newName.equals(bskyResourcesBundel.getString("file.smsuccessstory.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.SMSuccessStoryDoc");
		}

		String filepath = getDocumentPath(year + "/" + hospitalcode + "/" + folderName.trim() + "/" + fileName);

		File file = new File(filepath);
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

	public static void commonDownloadMethodForCPD(String fileName, String prifix, String userid,
			HttpServletResponse response) throws IOException {
		String folder = "";
		String path = "";
		if (prifix.equals("profilephoto") || prifix.equals("identityDoc") || prifix.equals("signaturedoc")) {
			folder = "CPDRegistration/";
			path = "CPDRegistration/" + userid + "/" + fileName;
		} else if (prifix.equals("specialityDoc")) {
			path = "CPDSpecialityDoc/" + "CPD_" + userid + "/" + fileName;
		} else {
			path = "CPDRegistration/" + userid + "/" + fileName;
		}

		File fileData = null;
		if (operatingSystem.contains("windows")) {
			fileData = new File(windowsRootFolder + path);
			System.out.println(fileData);
		} else {
			fileData = new File(linuxRootFolder + path);
		}
		if (!fileData.exists()) {
			String errorMessage = "Sorry. The file you are looking for does not exist";
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		String mimeType = URLConnection.guessContentTypeFromName(fileData.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileData.getName() + "\""));
		response.setContentLength((int) fileData.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(fileData));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

// file upload section for float generation
	public static Map<String, String> cretefileuploadforgeneratefloat(MultipartFile snacertification,
			MultipartFile mecertification, MultipartFile otherfile, String floatNo) {
		Map<String, String> filePath = new HashMap<String, String>();
		String snacertificate = null;
		String mecertificate = null;
		String othercertificate = null;
		floatNo = floatNo.replace("/", "");
		String currentTime = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
		try {

			if (snacertification != null) {
				snacertificate = SNACertificationPrefix + "_" + floatNo + "_" + currentTime
						+ getOriginalfilenameExtension(snacertification.getOriginalFilename());
			}

			if (mecertification != null) {
				mecertificate = MECertificationPicPrefix + "_" + floatNo + "_" + currentTime
						+ getOriginalfilenameExtension(mecertification.getOriginalFilename());
			}
			if (otherfile != null) {
				othercertificate = OtherFilePrefix + "_" + floatNo + "_" + currentTime
						+ getOriginalfilenameExtension(otherfile.getOriginalFilename());
			}

			filePath.put("snaCertificate", snacertificate);
			filePath.put("meCertificate", mecertificate);
			filePath.put("otherCertficate", othercertificate);

			if (snacertificate != null)
				dynamicFileUploadDocFloat(snacertification, floatNo,
						bskyResourcesBundel.getString("folder.SnaCertification.name"), snacertificate);

			if (mecertificate != null)
				dynamicFileUploadDocFloat(mecertification, floatNo,
						bskyResourcesBundel.getString("folder.MECertification.name"), mecertificate);

			if (othercertificate != null)
				dynamicFileUploadDocFloat(otherfile, floatNo, bskyResourcesBundel.getString("folder.OtherFile.name"),
						othercertificate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;

	}

	public static String dynamicFileUploadDocFloat(MultipartFile file, String floatNumber, String folderName,
			String newFileName) throws IOException {
		File fileData = null;
		Date date = new Date();
		int year = date.getYear() + 1900; // Add 1900 because getYear() returns year since 1900
		System.out.println("Current year: " + year);
		String path = floatFolder + "/" + year + "/" + floatNumber + "/" + folderName;
		System.out.println("file path in commonfile"+path);
		if (operatingSystem.contains("windows")) {
			fileData = new File(windowsRootFolder + path);
		} else {
			fileData = new File(linuxRootFolder + path);
		}
		fileData.getParentFile().mkdirs();
		return dynamicFloat(file, path, newFileName);
	}

	public static String dynamicFloat(MultipartFile file, String fileName, String newFileName) throws IOException {
		String fileFlag = "";
		if (file.isEmpty()) {
			fileFlag = "FileEmpty";
		} else {
			String folderPath = fileExistsOrNot(fileName);
			byte[] bytes = file.getBytes();
			Path path = Paths.get(folderPath.trim() + "/" + newFileName.trim());// file.getOriginalFilename());
			System.out.println("at the time of file writes"+path);
			Files.write(path, bytes);
			fileFlag = folderPath + newFileName;// file.getOriginalFilename();
			System.out.println("file flag name"+fileFlag);
		}
		if (operatingSystem.contains("windows")) {

			return fileFlag.replace(windowsRootFolder, "");
		} else {
			return fileFlag.replace(linuxRootFolder, "");
		}
	}

// @Auther : Hrusikesh Mohanty
// @Date : 11-01-2023
// pupose to get the Float File path

	public static String getFullDocumentPathfloat(String floatnumber, int year, String folderName) {
		return getDocumentPath1(floatFolder + "/" + year + "/" + floatnumber + "/" + folderName.trim());
	}

//-----------------------------------------Delete Float Documnet in Case Of 3(Record Alredy Exists)  and 2(Failed Case)-----------------------------------------------------------
	public static void deleteFilesStartingWith(String directoryPath, String filePattern) {
		Path directory = Paths.get(directoryPath);
		if (!Files.exists(directory)) {
			System.err.println("Directory does not exist: " + directoryPath);
			return;
		}
		try {
// List all files in the directory
			Files.list(directory).filter(file -> file.getFileName().toString().startsWith(filePattern))
					.forEach(file -> {
						try {
// Delete the file
							if (Files.exists(file)) {
								Files.delete(file);
								System.out.println("Deleted file: " + file);
							} else {
								System.out.println("File does not exist: " + file);
							}
						} catch (Exception e) {
							System.err.println("Failed to delete file: " + file);
							e.printStackTrace();
						}
					});
		} catch (IOException e) {
			System.err.println("Failed to list files in directory: " + directoryPath);
			e.printStackTrace();
		}
	}

	/***********
	 * @author Hrusikesh Mohanty
	 * @date 05/15/2024
	 * @description -To download files while generate Float New
	 ***********/
	public static void downloadFileFloat(String fileName, String floatNumber, String currentYear, String folderName,
			HttpServletResponse response) throws IOException {
		String path = null;
		if (fileName.startsWith("SNAC")) {
			path = floatFolder + "/" + currentYear + "/" + floatNumber + "/" + folderName + "/" + fileName;
		} else if (fileName.startsWith("MNEC")) {
			path = floatFolder + "/" + currentYear + "/" + floatNumber + "/" + folderName + "/" + fileName;
		} else if (fileName.startsWith("OTHC")) {
			path = floatFolder + "/" + currentYear + "/" + floatNumber + "/" + folderName + "/" + fileName;
		}
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

	public static String uploadonlinepostDoc(MultipartFile filename, Long postid) throws Exception {
		String customFileName = null, fileName = null, folderName = null, filePath = null, fileprifx = null;
		try {
			if (filename != null && filename.getOriginalFilename() != "") {
				fileName = filename.getOriginalFilename();
				Timestamp instant = Timestamp.from(Instant.now());
				String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
				folderName = bskyResourcesBundel.getString("folder.OnlinePostConfig.name");
				fileprifx = bskyResourcesBundel.getString("file.OnlinePostConfin.prefix");
				customFileName = fileprifx + "_" + postid+ "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "") + "." + fileExtension;
				filePath = getDocumentPath(folderName);
				File file = new File(filePath + "/" + customFileName);
				boolean mkdirs = file.getParentFile().mkdirs();
				
				byte[] bytes;
				try {
					bytes = filename.getBytes();
					Path path = Paths.get(filePath + "/" + customFileName);
					Files.write(path, bytes);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return customFileName;
	}

	public static void downLoadonlinepostDoc(String fileName, HttpServletResponse response) throws Exception {
		String filepath = null;
		try {
			String folderName = bskyResourcesBundel.getString("folder.OnlinePostConfig.name");
			filepath = getDocumentPath(folderName + "/" + fileName);
			File file = new File(filepath);
			if (!file.exists()) {
				String errorMessage = "Sorry! File You are Looking For Doesn't Exist";
				OutputStream outputStream = response.getOutputStream();
				outputStream.write(errorMessage.getBytes(StandardCharsets.UTF_8));
				outputStream.close();
			} else {
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
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static String uploadsuspendfile(MultipartFile filename, Integer action, String hospitalCode) throws Exception {
		String customFileName = null, fileName = null, folderName = null, filePath = null, fileprifx = null;
		try {
			if (filename != null && filename.getOriginalFilename() != "") {
				fileName = filename.getOriginalFilename();
				Timestamp instant = Timestamp.from(Instant.now());
				String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
				if(action==2) {
					folderName = bskyResourcesBundel.getString("folder.DeEmpanelFile.name");
					fileprifx = bskyResourcesBundel.getString("file.DeEmpanel.prefix");
				}else if(action==1){
					folderName = bskyResourcesBundel.getString("folder.SuspendFile.name");
					fileprifx = bskyResourcesBundel.getString("file.Suspend.prefix");
				}else {
					folderName = bskyResourcesBundel.getString("folder.AdditionalEmpanelFile.name");
					fileprifx = bskyResourcesBundel.getString("file.AdditionalEmpanel.prefix");
				}
				customFileName = fileprifx + "_" + hospitalCode+ "_"
						+ instant.toString().replaceAll("[,-.:\\s]", "") + "." + fileExtension;
				filePath = getDocumentPath(folderName);
				File file = new File(filePath + "/" + customFileName);
				boolean mkdirs = file.getParentFile().mkdirs();				
				byte[] bytes;
				try {
					bytes = filename.getBytes();
					Path path = Paths.get(filePath + "/" + customFileName);
					Files.write(path, bytes);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return customFileName;
	}

	public static void downLoaddeempanelDoc(String fileName, HttpServletResponse response) throws Exception {
		String filepath = null;
		try {
			String fileprifix=(fileName.split("_"))[0];
			String folderName="";
			if(fileprifix.equalsIgnoreCase(bskyResourcesBundel.getString("file.DeEmpanel.prefix"))) {
				folderName=bskyResourcesBundel.getString("folder.DeEmpanelFile.name");
			}else if(fileprifix.equalsIgnoreCase(bskyResourcesBundel.getString("file.Suspend.prefix"))) {
				folderName=bskyResourcesBundel.getString("folder.SuspendFile.name");
			}else if(fileprifix.equalsIgnoreCase(bskyResourcesBundel.getString("file.AdditionalEmpanel.prefix"))) {
				folderName=bskyResourcesBundel.getString("folder.AdditionalEmpanelFile.name");
			}else {
				throw new Exception("Folder ot Found !!");
			}
			filepath = getDocumentPath(folderName + "/" + fileName);
			System.out.println(filepath);
			File file = new File(filepath);
			if (!file.exists()) {
				String errorMessage = "Sorry! File You are Looking For Doesn't Exist";
				OutputStream outputStream = response.getOutputStream();
				outputStream.write(errorMessage.getBytes(StandardCharsets.UTF_8));
				outputStream.close();
			} else {
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
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
}
