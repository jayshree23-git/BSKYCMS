package com.project.bsky.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.FloatExcelBean;

/**
 * @author ronauk.maharana
 *
 */
@PropertySource(value = "classpath:application.properties")
public class FileUtil {

	private static ResourceBundle bskyAppResourcesBundel = ResourceBundle.getBundle("application");
	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	public static String windowsRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.windows");
	public static String linuxRootFolder = bskyAppResourcesBundel.getString("file.upload.directory.linux");
	public static String reportFolder = bskyResourcesBundel.getString("file.path.Floatreports");
	public static String paymentFolder = bskyResourcesBundel.getString("file.path.Paymentreports");
	public static String oldReportFolder = bskyResourcesBundel.getString("file.path.Oldfloatreports");
	public static String oldPaymentFolder = bskyResourcesBundel.getString("file.path.Oldpaymentreports");

	public static String operatingSystem = System.getProperty("os.name").toLowerCase().trim();

	public static String generateExcel(FloatExcelBean bean, String filename) {
		Workbook workbook = new HSSFWorkbook();
		try {
			List<List<Object>> list = bean.getReport();
			String userId = bean.getUserId().toString();
			List<String> header = bean.getHeading();
			
			String fullPath = getFloatRootPath(filename) + userId;
			File newFile = new File(fullPath);
			if (!newFile.exists()) {
				newFile.mkdirs();
			}

//			DecimalFormat formatter = new DecimalFormat("#,###.00");
			DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
			
			FileOutputStream file = new FileOutputStream(fullPath + "/" + filename);
			Sheet sheet = workbook.createSheet("Sheet 1");

			Font font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			CellStyle style = workbook.createCellStyle();
			style.setFont(font);
			style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setVerticalAlignment(VerticalAlignment.CENTER);

			Row a = sheet.createRow(0);
			a.createCell(0).setCellValue("Actual Date Of Discharge From");
			a.createCell(2).setCellValue(f.format(bean.getFromDate()));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
			sheet.addMergedRegion(new CellRangeAddress(0,0,2,6));
			
			Row b = sheet.createRow(1);
			b.createCell(0).setCellValue("Actual Date Of Discharge To");
			b.createCell(2).setCellValue(f.format(bean.getToDate()));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,1));
			sheet.addMergedRegion(new CellRangeAddress(1,1,2,6));
			
			Row c = sheet.createRow(2);
			c.createCell(0).setCellValue("State");
			c.createCell(2).setCellValue(!bean.getStateId().equalsIgnoreCase("All")?bean.getStateName():bean.getStateId());
			sheet.addMergedRegion(new CellRangeAddress(2,2,0,1));
			sheet.addMergedRegion(new CellRangeAddress(2,2,2,6));
			
			Row d = sheet.createRow(3);
			d.createCell(0).setCellValue("District");
			d.createCell(2).setCellValue(!bean.getDistrictId().equalsIgnoreCase("All")?bean.getDistrictName():bean.getDistrictId());
			sheet.addMergedRegion(new CellRangeAddress(3,3,0,1));
			sheet.addMergedRegion(new CellRangeAddress(3,3,2,6));
			
			Row e = sheet.createRow(4);
			e.createCell(0).setCellValue("Hospital");
			e.createCell(2).setCellValue(!bean.getHospitalId().equalsIgnoreCase("All")?bean.getHospitalName():bean.getHospitalId());
			sheet.addMergedRegion(new CellRangeAddress(4,4,0,1));
			sheet.addMergedRegion(new CellRangeAddress(4,4,2,6));
			
			Row headerRow = sheet.createRow(6);

			for (int col = 0; col < header.size(); col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(header.get(col));
				cell.setCellStyle(style);
			}
			
			for(int i=0;i<list.size();i++) {
				Row row = sheet.createRow(i+7);
				List<Object> obj = list.get(i);
				for(int j=0;j<obj.size();j++) {
					row.createCell(j).setCellValue(obj.get(j).toString());
				}
			}

			for (int i = 0; i < header.size(); i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(file);
			////System.out.println("File generated");
			return filename;
		} catch (IOException e) {
			e.printStackTrace();
			return e.toString();
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void getFloatReportFile(String fileCode, String userId, HttpServletResponse response)
			throws IOException {
		try {
			String path = getFloatRootPath(fileCode) + userId;
			File file = new File(path + "/" + fileCode);
			////System.out.println(file);
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
	
	public static void getPaymentFreezeFile(String fileCode, String userId, HttpServletResponse response)
			throws IOException {
		try {
			String path = getPaymentRootPath(fileCode) + userId;
			File file = new File(path + "/" + fileCode);
			////System.out.println(file);
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
	
	public static void getOldPaymentFreezeFile(String fileCode, String userId, HttpServletResponse response)
			throws IOException {
		try {
			String path = getOldPaymentRootPath(fileCode) + userId;
			File file = new File(path + "/" + fileCode);
			////System.out.println(file);
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

	public static String savePdf(MultipartFile pdf, String userId) {
		try {
			String filename = pdf.getOriginalFilename();
			String path = getFloatRootPath(filename) + userId;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			Path root = Paths.get(path);
			////System.out.println(root);
			////System.out.println(filename);
			Files.copy(pdf.getInputStream(), root.resolve(filename));
			return filename;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public static String savePaymentReport(MultipartFile pdf, String userId) {
		try {
			String date = new SimpleDateFormat("yyyyMMddhhmmssa").format(new Date());
			String extn = null;
			////System.out.println(date);
			if(pdf.getOriginalFilename()!=null) {
				extn = pdf.getOriginalFilename().substring(pdf.getOriginalFilename().lastIndexOf('.') + 1).trim();
			}
			String filename = "BSKY_Payment_Freeze_Report_" + date + "." + extn;
			
//			String filename = pdf.getOriginalFilename();
			String path = getPaymentRootPath(filename) + userId;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			Path root = Paths.get(path);
			////System.out.println(root);
			////System.out.println(filename);
			Files.copy(pdf.getInputStream(), root.resolve(filename));
			return filename;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public static String saveOldFloatReportFile(MultipartFile pdf, String userId) {
		try {
			String filename = pdf.getOriginalFilename();
			String path = getOldFloatRootPath(filename) + userId;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			Path root = Paths.get(path);
			////System.out.println(root);
			////System.out.println(filename);
			Files.copy(pdf.getInputStream(), root.resolve(filename));
			return filename;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public static void getOldFloatReportFile(String fileCode, String userId, HttpServletResponse response)
			throws IOException {
		try {
			String path = getOldFloatRootPath(fileCode) + userId;
			File file = new File(path + "/" + fileCode);
			////System.out.println(file);
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
	
	public static String saveOldPaymentReport(MultipartFile pdf, String userId) {
		try {
			String date = new SimpleDateFormat("yyyyMMddhhmmssa").format(new Date());
			String extn = null;
			////System.out.println(date);
			if(pdf.getOriginalFilename()!=null) {
				extn = pdf.getOriginalFilename().substring(pdf.getOriginalFilename().lastIndexOf('.') + 1).trim();
			}
			String filename = "BSKY_Old_Payment_Freeze_Report_" + date + "." + extn;
			
//			String filename = pdf.getOriginalFilename();
			String path = getOldPaymentRootPath(filename) + userId;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			Path root = Paths.get(path);
			////System.out.println(root);
			////System.out.println(filename);
			Files.copy(pdf.getInputStream(), root.resolve(filename));
			return filename;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	private static String getFloatRootPath(String filename) {
		String year = filename.substring(29, 33);
		if (operatingSystem.indexOf("win") >= 0) {
			return windowsRootFolder.trim() + year + "/" + reportFolder + "/";
		} else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0) {
			return linuxRootFolder.trim() + year + "/" + reportFolder + "/";
		} else {
			return null;
		}
	}
	
	private static String getPaymentRootPath(String filename) {
		String year = filename.substring(27, 31);
		if (operatingSystem.indexOf("win") >= 0) {
			return windowsRootFolder.trim() + year + "/" + paymentFolder + "/";
		} else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0) {
			return linuxRootFolder.trim() + year + "/" + paymentFolder + "/";
		} else {
			return null;
		}
	}
	
	private static String getOldFloatRootPath(String filename) {
		String year = filename.substring(33, 37);
		if (operatingSystem.indexOf("win") >= 0) {
			return windowsRootFolder.trim() + year + "/" + oldReportFolder + "/";
		} else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0) {
			return linuxRootFolder.trim() + year + "/" + oldReportFolder + "/";
		} else {
			return null;
		}
	}
	
	private static String getOldPaymentRootPath(String filename) {
		String year = filename.substring(31, 35);
		if (operatingSystem.indexOf("win") >= 0) {
			return windowsRootFolder.trim() + year + "/" + oldPaymentFolder + "/";
		} else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
				|| operatingSystem.indexOf("aix") > 0) {
			return linuxRootFolder.trim() + year + "/" + oldPaymentFolder + "/";
		} else {
			return null;
		}
	}

	

	

}
