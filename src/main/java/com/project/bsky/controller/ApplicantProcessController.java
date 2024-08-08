package com.project.bsky.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.entity.OnlineServiceApprovalNotings;
import com.project.bsky.entity.TOnlineServiceApproval;
import com.project.bsky.entity.TOnlineServiceQueryDocument;
import com.project.bsky.repository.MDynamicSmsMailRepository;
import com.project.bsky.service.ApplicantProcessService;
import com.project.bsky.service.OnlineServiceApprovalNotingsService;
import com.project.bsky.service.TOnlineServiceApprovalService;
import com.project.bsky.service.TOnlineServiceQueryDocumentService;
import com.project.bsky.util.ConvertClobToJson;
import com.project.bsky.util.EmailUtil;

@RestController
@CrossOrigin(origins = "*")
public class ApplicantProcessController {
	
	@Autowired
    private Logger logger;

	@Autowired
	private EmailUtil eUtil;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MDynamicSmsMailRepository mDynamicSmsMailRepository;
	
	@Autowired
	private ApplicantProcessService applicantProcessService;
	
	@Autowired
	private OnlineServiceApprovalNotingsService onlineServiceApprovalNotingsService;
	
	@Autowired
	private TOnlineServiceApprovalService tOnlineServiceApprovalService;
	
	@Autowired
	private TOnlineServiceQueryDocumentService tOnlineServiceQueryDocumentService;
	
	@Value("${server.port}")
	private String serverport;

	@Value("${pdf.documentPathForTemp}")
	private String documentPathForTemp;

	@Value("${pdf.documentPathForurl}")
	private String documentPathForurl;
	
	@Value("${pdf.documentPathForCommon}")
	private String documentPathForCommon;
	
	@Value("${pdf.documentPathForTarget}")
	private String documentPathForTarget;

	@Value("${pdf.documentPathForempandgrv}")
	private String documentPathForEmpAndGrv;
	
	@GetMapping("/getFormDetails")
	public ResponseEntity<String> getFormDetails() throws Exception {
		JSONObject response =applicantProcessService.getFormDetails();
		return ResponseEntity.ok(response.toString());

	}

	@PostMapping("/getSchemeApplyDetails")
	public ResponseEntity<String> getSchemeApplyDetails(@RequestBody String data) throws Exception {
		JSONObject response=applicantProcessService.getSchemeApplyDetails(data);
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping(value = "/schemeApply")
	public ResponseEntity<String> schemeApply(WebRequest request, @RequestParam("processId") Integer processId,
			@RequestParam("secId") Integer sectionId, @RequestParam("intOnlineServiceId") Integer intOnlineServiceId,@RequestParam("ProfileID")Integer profileId,@RequestParam("USERID") Integer userId) throws Exception {
		JSONObject response=applicantProcessService.schemeApply(request, processId,sectionId,intOnlineServiceId,profileId,userId);
		return ResponseEntity.ok(response.toString());
		
		
	}

	@PostMapping("/previewDynamicForm")
	public ResponseEntity<String> getPreviewDynamicForm(@RequestBody String data, WebRequest request) throws Exception {
		JSONObject response=applicantProcessService.getPreviewDynamicForm(data,request);
		return ResponseEntity.ok(response.toString());

	}
	
	@PostMapping("/applyForProcess")
	public ResponseEntity<String> applyForProcess(@RequestBody String data) throws Exception {
		JSONObject response = applicantProcessService.applyForProcess(data);
		return ResponseEntity.ok(response.toString());

	}

	@PostMapping(value = "/saveFileToTemp")
	public ResponseEntity<String> saveDocImgToTemp(@RequestParam("file") MultipartFile multipart,
			@RequestParam("fileSizeType") String fileSizeType, @RequestParam("fileSize") Integer fileSize,
			@RequestParam("fileType") String fileType) throws IOException, Exception {
		Timestamp tt = new Timestamp(System.currentTimeMillis());
		String fileNameForType = (multipart.getOriginalFilename());
		String[] fileArray = fileNameForType.split("[.]");
		String actualType = fileArray[fileArray.length - 1];
		Integer valid = 0;
		String fileName = "";
		Integer maxSize = 0;
		if (fileSizeType.equals("mb")) {
			maxSize = 1024 * 1024 * fileSize;
		} else if (fileSizeType.equals("kb")) {
			maxSize = 1024 * fileSize;
		}

		Map<String, String> allFileType = new HashMap<String, String>();
		allFileType.put("pdf", "pdf");
		allFileType.put("image", "jpeg,jpe,png,gif,jpg");
		allFileType.put("excel",
				"csv,dbf,htm,html,mht,mhtml,ods,pdf,prn,txt,xla,xlam,xls,xlsb,xlsx,xlt,xltm,xls,xlsb,xlsm,xlsx,xlw,xps");
		allFileType.put("doc", "doc,docm,docx,dot,dotm,dotx,htm,html,mht,mhtml,odt,pdf,rtf,txt,wps,xml,xps,msword");
		allFileType.put("video", "mp4,ogx,oga,ogv,ogg,webm");

		Map<String, String> mimeTypeData = new HashMap<String, String>();
		mimeTypeData.put("pdf", "application/pdf");
		mimeTypeData.put("image", "image/jpeg,image/jpe,image/png,image/gif");
		mimeTypeData.put("excel",
				"text/csv,application/dbf,text/html,multipart/related application/x-mimearchive, application/vnd.oasis.opendocument.spreadsheet,application/pdf, application/x-prn, text/plain, application/vnd, application/vnd.ms-excel.addin.macroEnabled.12, application/vnd.ms-excel,application/vnd.ms-excel.sheet.binary.macroEnabled.12, application/vnd.ms-excel.sheet.macroEnabled.12,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/msexcel,application/vnd.ms-excel.template.macroEnabled.12,application/vnd.ms-excel,application/vnd.ms-excel.sheet.binary.macroEnabled.12,application/vnd.ms-xpsdocument");
		mimeTypeData.put("doc",
				"application/msword, application/vnd.ms-word.document.macroEnabled.12,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/msword,application/vnd.ms-word.template.macroEnabled.12,application/vnd.openxmlformats-officedocument.wordprocessingml.template,text/html,multipart/related application/x-mimearchive,application/vnd.oasis.opendocument.text, application/pdf,application/rtf, text/plain, application/vnd.ms-works, application/xml,application/vnd.ms-xpsdocument");
		mimeTypeData.put("video", "video/mp4,application/ogg,audio/ogg,video/ogg,audio/ogg,video/webm");

		Path path = new File(fileNameForType).toPath();
		String mimeType = Files.probeContentType(path);
		if (!(allFileType.get(fileType).contains(actualType))) {
			valid = 1;
		} else if (maxSize < multipart.getSize()) {
			valid = 1;
		} 
		JSONObject response = new JSONObject();
		if (valid == 0) {


			try {
				byte[] bytes = multipart.getBytes();
				try (OutputStream stream2 = new FileOutputStream(
						documentPathForTemp + tt.getTime() + "." + actualType)) {
					stream2.write(bytes);
				}

			} catch (IOException e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}


		}
		if (valid == 0) {
			response.put("fileName", tt.getTime() + "." + actualType);
			response.put("filePath", documentPathForurl
					+ "/downloadForm/" + tt.getTime() +"."+ actualType);
			JSONObject response1 = new JSONObject();
			response1.put("result", response);
			response1.put("status", 200);
			return ResponseEntity.ok(response1.toString());
		} else if (valid == 1) {
			JSONObject response1 = new JSONObject();
			response1.put("result", response);
			response1.put("status", 400);
			return ResponseEntity.ok(response1.toString());
		}
		return null;
	}

	public Resource getFileAsResource(String url, String fileCode) {
		Path dirPath = Paths.get(url + fileCode);

		try {
			if (dirPath != null) {
				return new UrlResource(dirPath.toUri());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@GetMapping("/downloadForm/{fileName}")
	public ResponseEntity<?> downloadFormIcon(@PathVariable("fileName") String fileCode) throws IOException {
		Resource resource = null;
		resource = getFileAsResource(documentPathForTemp, fileCode);
		if (resource == null) {
			return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		}
		String contentType = "application/octet-stream";
		String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
	}

	
	@GetMapping("/downloadApprovalForm/{fileName}")
	public ResponseEntity<?> downloadApprovalFormIcon(@PathVariable("fileName") String fileCode) throws IOException {
		Resource resource = null;
		Resource resource1=null;
		resource = getFileAsResource(documentPathForCommon, fileCode);
		resource1=getFileAsResource(documentPathForEmpAndGrv, fileCode);
		if (!resource.exists()) {
			if (!resource1.exists()) {
				return new ResponseEntity<>("Sorry. The file you are looking for does not exist", HttpStatus.NOT_FOUND);
			} else {
				String contentType = "application/octet-stream";
				String headerValue = "attachment; filename=\"" + resource1.getFilename() + "\"";
				return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
						.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource1);
			}
		} else {
			String contentType = "application/octet-stream";
			String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
		}
		
	}
	@PostMapping("/tableColumnFetch")
	public ResponseEntity<String> tableColumnFetch(@RequestBody String data) throws Exception {
		JSONObject jsonObject = new JSONObject(data);
		String columnName = jsonObject.getString("columnName");
		String condition = jsonObject.getString("condition");
		String tableName = jsonObject.getString("tableName");
		String selectQuery = "select " + columnName + " from " + tableName;
		List<Map<String, Object>> dataListFromDB = null;
		try {
			if (condition.equals("")) {
				dataListFromDB = jdbcTemplate.queryForList(selectQuery);
			} else {
				
				selectQuery = "select " + columnName + " from " + tableName + " where " + condition;
				dataListFromDB = jdbcTemplate.queryForList(selectQuery);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			JSONObject response = new JSONObject();
			response.put("result", "No record Found");
			response.put("status", "404");
			return ResponseEntity.ok(response.toString());
		}
		JSONObject response = new JSONObject();
		response.put("result", dataListFromDB);
		response.put("status", "200");
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping("/getQueryDetails")
	public String getQueryDetails(@RequestBody String data) throws JSONException {
		List<Object[]> list = applicantProcessService.getQueryDetails(data);
		JSONObject response=null;
		for (Object[] obj : list) {
			String TXTNOTING =ConvertClobToJson.convertClobToJSONString((Clob) (obj[0]));
			String timeStamp = obj[1].toString();
			String VCHPROCESSNAME = obj[3].toString();
			Integer intNotingsId = (Integer.parseInt(obj[2].toString()));
			JSONObject json = new JSONObject();
			json.put("dtActionTaken", timeStamp);
			json.put("txtNoting", TXTNOTING);
			json.put("schemeName", VCHPROCESSNAME);
			json.put("intNotingsId", intNotingsId);
			 response = new JSONObject();
			response.put("result", json);
			response.put("status", "200");
		}
		return response.toString();
	}
	
	
	@PostMapping("/queryReplyInsert")
	public String saveQueryDetails(@RequestParam("remark") String remark, @RequestParam("processId") Integer processId,
			@RequestParam("serviceId") Integer serviceId, @RequestParam("dynamicListArr") String dynamicListArr) throws JSONException {
		Integer processIdd = Integer.parseInt(processId.toString());
		Integer serviceIdd = Integer.parseInt(serviceId.toString());
		OnlineServiceApprovalNotings OnlineServiceApprovalNotings = new OnlineServiceApprovalNotings();
		OnlineServiceApprovalNotings.setINTONLINESERVICEID(serviceId);
		OnlineServiceApprovalNotings.setINTPROFILEID(0);
		OnlineServiceApprovalNotings.setINTFROMAUTHORITYID(0);
		long millis = System.currentTimeMillis();
		OnlineServiceApprovalNotings.setDTACTIONTAKEN(new Date(millis));
		OnlineServiceApprovalNotings.setINTSTATUS(9);
		OnlineServiceApprovalNotings.setTXTNOTING(remark);
		OnlineServiceApprovalNotings.setTINRESUBMITSTATUS(0);
		OnlineServiceApprovalNotings.setTINSTAGECTR(1);
		OnlineServiceApprovalNotings.setTINQUERYTO(0);
		OnlineServiceApprovalNotings.setBITDELETEDFLAG(0);
		OnlineServiceApprovalNotings.setINTPROCESSID(processId);
		OnlineServiceApprovalNotings onlineServiceApprovalNoting=onlineServiceApprovalNotingsService.saveAll(OnlineServiceApprovalNotings);
		TOnlineServiceApproval obj = tOnlineServiceApprovalService.getAllDetails(processIdd, serviceIdd);	
		obj.setDtmStatusDate(new Date(millis));
		obj.setIntPendingAt(obj.getIntSentFrom().toString());
		obj.setIntSentFrom(0);
		obj.setTinStatus(9);
		obj.setTinQueryTo(0);
		tOnlineServiceApprovalService.save(obj);
		Integer intNotingsId=onlineServiceApprovalNoting.getINTNOTINGSID();
		
		
		JSONArray json = new JSONArray(dynamicListArr);
		for(int i=0;i<json.length();i++) {
		JSONObject objj=json.getJSONObject(i);
		String vchDocumentName=objj.getString("vchDocumentName");
		String vchDocumentFile=objj.getString("vchDocumentFile");
		TOnlineServiceQueryDocument tOnlineServiceQueryDocument=new TOnlineServiceQueryDocument();
		tOnlineServiceQueryDocument.setIntNotingId(intNotingsId);
		tOnlineServiceQueryDocument.setIntOnlineServiceId(serviceIdd);
		tOnlineServiceQueryDocument.setVchDocumentName(vchDocumentName);
		tOnlineServiceQueryDocument.setVchDocumentFile(vchDocumentFile);
		tOnlineServiceQueryDocument.setIntProfileId(0);
		tOnlineServiceQueryDocument.setStmCreatedOn(new Date(millis));
		tOnlineServiceQueryDocument.setIntUpdatedBy(0);
		tOnlineServiceQueryDocument.setBitDeletedFlag(0);
		tOnlineServiceQueryDocumentService.save(tOnlineServiceQueryDocument);
		File src = new File(documentPathForTemp + vchDocumentName);
		File dest = new File(documentPathForTarget + vchDocumentName);
		try {
			Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			Files.delete(src.toPath());
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		
		}
		JSONObject response = new JSONObject();
		response.put("result", "succes");
		response.put("status", "200");
		return response.toString();

	}
}
