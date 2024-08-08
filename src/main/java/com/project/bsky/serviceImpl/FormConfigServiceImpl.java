package com.project.bsky.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bsky.bean.ManageFormRequest;
import com.project.bsky.entity.MModuleName;
import com.project.bsky.entity.ProcessEntity;
import com.project.bsky.repository.MModuleNameRepository;
import com.project.bsky.repository.ModuleRepository;
import com.project.bsky.repository.ProcessRepository;
import com.project.bsky.response.Result;
import com.project.bsky.response.Root;
import com.project.bsky.service.FormConfigService;
import com.project.bsky.util.ConvertClobToJson;

@Service
public class FormConfigServiceImpl implements FormConfigService {

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	MModuleNameRepository moduleNameRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Logger logger;

	@Value("${dir.formIcon}")
	private String formIconDirectory;

	@Value("${dir.formGuideline}")
	private String formGuidelineDirectory;

	@Value("${server.port}")
	private int serverPort;

	@Value("${dir.formIcon}")
	private String formIcon;

	@Value("${dir.formGuideline}")
	private String formGuideline;

	@Override
	public String saveAndUpdate(String request) throws JsonMappingException, JsonProcessingException, Exception {

		JSONObject requestObj = new JSONObject(request);
		////System.out.println(requestObj);
		byte[] decoded = Base64.getDecoder().decode(requestObj.getString("REQUEST_DATA"));
		String decodedStr = new String(decoded, StandardCharsets.UTF_8);
		JSONObject object = new JSONObject(decodedStr);
		ObjectMapper mapper = new ObjectMapper();
		long currentStamp = System.currentTimeMillis();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ManageFormRequest manageFormRequest = mapper.readValue(object.toString(), ManageFormRequest.class);
		////System.out.println("Manage="+manageFormRequest);
		
		String fileIconName = "image_" + currentStamp + ".png";
		String fileGuidLineName = "application_" + currentStamp + ".pdf";

		String imageFormIconBytes = null;
		String imageFormDescriptionBytes = null;

		if (manageFormRequest.getFormicon() != null && !manageFormRequest.getFormicon().isEmpty()
				&& manageFormRequest.getFormicon().contains("base64")) { 

			imageFormIconBytes = manageFormRequest.getFormicon()
					.substring(manageFormRequest.getFormicon().indexOf(",") + 1);

			try {

				byte[] data = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(imageFormIconBytes);
				File formIconDirectory = new File(formIcon);
				
				if (formIconDirectory.exists()) {
				    ////System.out.println("Already exist:" + formIcon);
				} else {
					formIconDirectory.mkdir();
				    ////System.out.println("Directory created :" + formIcon);
				}
				
				try (OutputStream stream = new FileOutputStream(formIcon + "/" + fileIconName)) {
					stream.write(data);
				}

			} catch (IOException e1) {
				logger.error(ExceptionUtils.getStackTrace(e1));
			}
		}

		if (manageFormRequest.getGuideline() != null && !manageFormRequest.getGuideline().isEmpty()
				&& manageFormRequest.getGuideline().contains("base64")) {  //Edit case

			imageFormDescriptionBytes = manageFormRequest.getGuideline()
					.substring(manageFormRequest.getGuideline().indexOf(",") + 1);

			try {
				byte[] data2 = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(imageFormDescriptionBytes);
				
				File formGuidelineDirectory = new File(formGuideline);
				
				if (formGuidelineDirectory.exists()) {
				    ////System.out.println("Already exist:" + formGuideline);
				} else {
					formGuidelineDirectory.mkdir();
				    ////System.out.println("Directory created :" + formGuideline);
				}
				
				try (OutputStream stream2 = new FileOutputStream(formGuideline + "/" + fileGuidLineName)) {
					stream2.write(data2);
				}
			} catch (IOException e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}

		String response = null;
		JSONArray jsonArray = new JSONArray(manageFormRequest.getAddsection());
		JSONArray apiReferenceUrl = new JSONArray(manageFormRequest.getReferencedetails());
		JSONArray apiStatusUrl = new JSONArray(manageFormRequest.getStatusdetails());
		JSONArray apiUrl = new JSONArray(manageFormRequest.getApiUrldetails());
		int moduleId = 0;

		try {
			long millis = System.currentTimeMillis();
			KeyHolder keyHolder = new GeneratedKeyHolder();

			// ModuleEntity saveModule = null;
			if (manageFormRequest.getItemId() == null || manageFormRequest.getItemId().equals(""))   //If Model Name is New

			{

				if (manageFormRequest.getModuleId() == null || manageFormRequest.getModuleId().equals("")) {

					MModuleName module = new MModuleName();
					module.setVchModuleName(manageFormRequest.getModuleName());
					module.setDtmCreatedOn(new Date(millis));
					module.setBitDeletedFlag(0);
					MModuleName moduleRepo = moduleNameRepository.saveAndFlush(module);
					final int moduleId1 = module.getIntModuleId();

					  
					////System.out.println("Date is="+new Date(millis));
					
					
					
					
					jdbcTemplate.update(new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = connection.prepareStatement(
									" INSERT INTO m_process_name (vchProcessName,intPayment,intApproval,intDocument, "
											+ " intModuleId,tinProcessType,bitDeletedFlag,vchSection,txtSchemeDescription,tinPublishStatus,vchTableName, "
											+ " vchAPIReferenceURLDtls,vchAPIStatusURLDtls,vchAPIURLDtls,vchFormType,intAdminApplication,intWebsiteApplication,dtmCreatedOn,vchSchemePoster,vchSchemeGuideline,viewAtCitizen,intServiceMode) "
											+ " values ('" + manageFormRequest.getFormName() + "',"
											+ manageFormRequest.getConfigurationForpayment() + ","
											+ manageFormRequest.getConfigurationForapproval() + ","
											+ manageFormRequest.getConfigurationFordocument() + "," + moduleId1
											+ ",2,0, " + "  '" + jsonArray + "','"
											+ manageFormRequest.getFormdescription() + "',0 " + " ,'"
											+ manageFormRequest.getTableName() + "','" + apiReferenceUrl + "','"
											+ apiStatusUrl + "','" + apiUrl + "','" + manageFormRequest.getFormType()
											+ "'," + manageFormRequest.getEnableToAdminapplication() + ","
											+ manageFormRequest.getEnableToWebsiteapplication() + ",'"
											+ new Date(millis) + "','" + fileIconName + "','" + fileGuidLineName + "','"
											+ manageFormRequest.getConfigurationForviewcitizen() + "',"+manageFormRequest.getServicemode()+")",
									new String[] { "id" });

							try {

							} catch (Exception e) {

								e.printStackTrace();
							}

							return ps;
						}
					}, keyHolder);
				} else {
					final int moduleId2 = Integer.valueOf(manageFormRequest.getModuleId());

					////System.out.println("Date is="+new Date(millis));
					Date date=new Date(millis);
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
					////System.out.println(dateFormat.format(date));
					////System.out.println();
					
					
//					ProcessEntity mProcessName=new ProcessEntity();
//					mProcessName.setIntProcessId(3);
//					mProcessName.setVchProcessName(manageFormRequest.getVchProcessName());
//					mProcessName.setIntPayment(manageFormRequest.getConfigurationForpayment());
//					mProcessName.setIntApproval(manageFormRequest.getConfigurationForapproval());
//					mProcessName.setIntDocument(manageFormRequest.getConfigurationFordocument());
//					mProcessName.setIntModuleId(moduleId2);
//					mProcessName.setTinProcessType(2);
//					mProcessName.setBitDeletedFlag(0);
//					mProcessName.setVchSection(jsonArray.toString());
//					mProcessName.setTxtSchemeDescription(manageFormRequest.getFormdescription());
//					mProcessName.setTinPublishStatus(0);
//					mProcessName.setVchTableName(manageFormRequest.getTableName());
//					mProcessName.setVchAPIReferenceURLDtls(apiReferenceUrl.toString());
//					mProcessName.setVchAPIStatusURLDtls(apiStatusUrl.toString());
//					mProcessName.setVchAPIURLDtls(apiUrl.toString());
//					mProcessName.setVchFormType(manageFormRequest.getFormType());
//					mProcessName.setIntAdminApplication(manageFormRequest.getEnableToAdminapplication());
//					mProcessName.setIntWebsiteApplication(manageFormRequest.getEnableToWebsiteapplication());
//					mProcessName.setDtmCreatedOn(new Date(currentStamp));
//					mProcessName.setVchSchemePoster(fileIconName);
//					mProcessName.setVchSchemeGuideline(fileGuidLineName);
//					mProcessName.setViewAtCitizen(manageFormRequest.getConfigurationForviewcitizen());
//					mProcessName.setIntServiceMode(manageFormRequest.getServicemode());
//					processRepository.save(mProcessName);
					
					
					jdbcTemplate.update(new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = connection.prepareStatement(
									" INSERT INTO m_process_name (vchProcessName,intPayment,intApproval,intDocument, "
											+ " intModuleId,tinProcessType,bitDeletedFlag,vchSection,txtSchemeDescription,tinPublishStatus,vchTableName, "
											+ " vchAPIReferenceURLDtls,vchAPIStatusURLDtls,vchAPIURLDtls,vchFormType,intAdminApplication,intWebsiteApplication,dtmCreatedOn,vchSchemePoster,vchSchemeGuideline,viewAtCitizen,intServiceMode) "
											+ " values ('" + manageFormRequest.getFormName() + "',"
											+ manageFormRequest.getConfigurationForpayment() + ","
											+ manageFormRequest.getConfigurationForapproval() + ","
											+ manageFormRequest.getConfigurationFordocument() + "," + moduleId2
											+ ",2,0, " + "  '" + jsonArray + "','"
											+ manageFormRequest.getFormdescription() + "',0 " + " ,'"
											+ manageFormRequest.getTableName() + "','" + apiReferenceUrl + "','"
											+ apiStatusUrl + "','" + apiUrl + "','" + manageFormRequest.getFormType()
											+ "'," + manageFormRequest.getEnableToAdminapplication() + ","
											+ manageFormRequest.getEnableToWebsiteapplication() + ",'"
											+ dateFormat.format(date) + "','" + fileIconName + "','" + fileGuidLineName + "','"
											+ manageFormRequest.getConfigurationForviewcitizen() + "',"+manageFormRequest.getServicemode()+")",
									new String[] { "intProcessId" });

							try {

							} catch (Exception e) {

								e.printStackTrace();
							}

							return ps;
						}
					}, keyHolder);
				}

				int primaryKey = keyHolder.getKey().intValue();

				if (primaryKey > 0) {
					JSONObject responseObj = new JSONObject();
					String obj = "200";
					String encodedResponseObject = Base64.getEncoder().encodeToString(obj.getBytes());
					responseObj.put("RESPONSE_DATA", encodedResponseObject);
					responseObj.put("RESPONSE_TOKEN", requestObj.getString("REQUEST_TOKEN"));
					response = responseObj.toString();
				}

			} else if (manageFormRequest.getItemId() != null && !manageFormRequest.getItemId().equals("")) {  // If Module name already exist

				JSONObject responseObj = new JSONObject();
				String encodedResponseObject = null;

				String[] itemIdArr = manageFormRequest.getItemId().split(",");

				for (String itemId : itemIdArr) {

					ProcessEntity entity = processRepository.getById(Integer.valueOf(itemId));
					if (!manageFormRequest.getItemStatus().equals("")
							&& Integer.valueOf(manageFormRequest.getItemStatus()) == 1) {

						jdbcTemplate.update(new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection)
									throws SQLException {
								PreparedStatement ps = connection.prepareStatement(
										" UPDATE m_process_name SET bitDeletedFlag = 1 where intProcessId = "
												+ Integer.valueOf(itemId));

								try {

								} catch (Exception e) {

									e.printStackTrace();
								}

								return ps;
							}
						});

						responseObj.put("messsage", "Record deleted successfully!!");
						responseObj.put("status", "200");
						encodedResponseObject = Base64.getEncoder().encodeToString(responseObj.toString().getBytes());

					} else if (!manageFormRequest.getItemStatus().equals("")
							&& Integer.valueOf(manageFormRequest.getItemStatus()) == 2) {

						jdbcTemplate.update(new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection)
									throws SQLException {
								PreparedStatement ps = connection.prepareStatement(
										" UPDATE m_process_name SET tinPublishStatus = 1 where intProcessId = "
												+ Integer.valueOf(itemId));

								try {

								} catch (Exception e) {

									e.printStackTrace();
								}

								return ps;
							}
						});

						responseObj.put("messsage", "Record published  successfully!!");
						responseObj.put("status", "200");
						encodedResponseObject = Base64.getEncoder().encodeToString(responseObj.toString().getBytes());

					} else if (!manageFormRequest.getItemStatus().equals("")
							&& Integer.valueOf(manageFormRequest.getItemStatus()) == 3) {
						jdbcTemplate.update(new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection)
									throws SQLException {
								PreparedStatement ps = connection.prepareStatement(
										" UPDATE m_process_name SET tinPublishStatus = 0 where intProcessId = "
												+ Integer.valueOf(itemId));

								try {

								} catch (Exception e) {

									e.printStackTrace();
								}

								return ps;
							}
						});
						responseObj.put("messsage", "Record unpublished  successfully!!");
						responseObj.put("status", "200");
						encodedResponseObject = Base64.getEncoder().encodeToString(responseObj.toString().getBytes());

					} else {
						
						
						////System.out.println("Date is="+new Date(millis));
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
						
						String responseString = "202";
						jdbcTemplate.update(new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection)
									throws SQLException {
								PreparedStatement ps = connection
										.prepareStatement(" UPDATE m_process_name SET vchProcessName ='"
												+ manageFormRequest.getFormName() + "',intPayment="
												+ manageFormRequest.getConfigurationForpayment() + ", intApproval="
												+ manageFormRequest.getConfigurationForapproval() + " ,intDocument="
												+ manageFormRequest.getConfigurationFordocument() + " , "
												+ " intModuleId = " + Integer.valueOf(manageFormRequest.getModuleId())
												+ ",tinProcessType = 2 ,bitDeletedFlag = 0,vchSection = '" + jsonArray
												+ "',txtSchemeDescription = '" + manageFormRequest.getFormdescription()
												+ "',vchTableName = '" + manageFormRequest.getTableName() + "', "
												+ " vchAPIReferenceURLDtls='" + apiReferenceUrl
												+ "',vchAPIStatusURLDtls='" + apiStatusUrl + "',vchAPIURLDtls='"
												+ apiUrl + "',vchFormType='" + manageFormRequest.getFormType()
												+ "',intAdminApplication="
												+ manageFormRequest.getEnableToAdminapplication()
												+ ",intWebsiteApplication="
												+ manageFormRequest.getEnableToWebsiteapplication()
												+ ",vchSchemePoster='" + fileIconName + "',vchSchemeGuideline='"
												+ fileGuidLineName + "',stmUpdatedOn ='" + dateFormat.format(millis)
												+ "',viewAtCitizen='"
												+ manageFormRequest.getConfigurationForviewcitizen()
												+ "' where intProcessId = " + Integer.valueOf(itemId));

								try {

								} catch (Exception e) {

									e.printStackTrace();
								}

								return ps;
							}
						});

						encodedResponseObject = Base64.getEncoder().encodeToString(responseString.getBytes());
					}

					processRepository.save(entity);

				}

				// encodedResponseObject =
				// Base64.getEncoder().encodeToString(responseObj.toString().getBytes());
				responseObj.put("RESPONSE_DATA", encodedResponseObject);
				responseObj.put("RESPONSE_TOKEN", requestObj.getString("REQUEST_TOKEN"));
				response = responseObj.toString();
			}

		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
		}
		return response;
	}

	@Override
	public Root viewFormList(String request) {
		////System.out.println("Req Obj:" + request.toString());
		Root root = new Root();

		try {

			JSONObject requestObj = new JSONObject(request);
			Result result = null;
			List<Result> listResult = new ArrayList<Result>();

			List<Object[]> listProcess = null;
			byte[] decoded = Base64.getDecoder().decode(requestObj.getString("REQUEST_DATA"));
			String decodedStr = new String(decoded, StandardCharsets.UTF_8);
			JSONObject object = new JSONObject(decodedStr);
			ObjectMapper mapper = new ObjectMapper();
			ManageFormRequest manageFormRequest = mapper.readValue(object.toString(), ManageFormRequest.class);

			String sql = "";

			if (manageFormRequest.getModuleId().isEmpty() && manageFormRequest.getIteamId().isEmpty()
					&& (manageFormRequest.getVchProcessName() == null
							|| manageFormRequest.getVchProcessName().equals(""))) {

				listProcess = processRepository.findAllActiveProcess();

			}

			else if (!manageFormRequest.getModuleId().isEmpty() && manageFormRequest.getIteamId().isEmpty()
					&& (manageFormRequest.getVchProcessName() == null
							|| manageFormRequest.getVchProcessName().equals(""))) {
				listProcess = processRepository
						.findAllActiveProcessByModuleName(Integer.valueOf(manageFormRequest.getModuleId()));
			} else if (!manageFormRequest.getModuleId().isEmpty() && !manageFormRequest.getIteamId().isEmpty()
					&& (manageFormRequest.getVchProcessName() == null
							|| manageFormRequest.getVchProcessName().equals(""))) {
				listProcess = processRepository.findAllActiveProcessByModuleNameAndItemId(
						Integer.valueOf(manageFormRequest.getModuleId()),
						Integer.valueOf(manageFormRequest.getIteamId()));
			}

			else if (manageFormRequest.getModuleId().equals("0") && !manageFormRequest.getIteamId().isEmpty()
					&& (manageFormRequest.getVchProcessName() != null
							&& !manageFormRequest.getVchProcessName().equals(""))) {
				listProcess = processRepository.findAllActiveProcessByItemIdAndProcessName(
						Integer.valueOf(manageFormRequest.getIteamId()), manageFormRequest.getVchProcessName());
			} else if (manageFormRequest.getModuleId().equals("0") && manageFormRequest.getIteamId().isEmpty()
					&& (manageFormRequest.getVchProcessName() != null
							&& !manageFormRequest.getVchProcessName().isEmpty())) {
				listProcess = processRepository
						.findAllActiveProcessByProcessName(manageFormRequest.getVchProcessName());
			} else if (!manageFormRequest.getModuleId().equals("0") && manageFormRequest.getIteamId().isEmpty()
					&& (manageFormRequest.getVchProcessName() != null
							&& !manageFormRequest.getVchProcessName().equals(""))) {

				listProcess = processRepository.findAllActiveProcessByModuleAndrocessName(
						Integer.valueOf(manageFormRequest.getModuleId()), manageFormRequest.getVchProcessName());
			} else {
				listProcess = processRepository
						.findAllActiveProcessByItemId(Integer.valueOf(manageFormRequest.getIteamId()));
			}

			if (!listProcess.isEmpty()) {

				root.setStatus("200");

				for (Object[] dto : listProcess) {
					result = new Result();
					result.setIntProcessId(Integer.valueOf(String.valueOf(dto[0])));
					result.setVchProcessName(String.valueOf(dto[1]));
					String paymentStatus =  dto[3].toString();
					if (paymentStatus.equals("1")) {
						result.setIntPayment(1);
					} else {
						result.setIntPayment(0);
					}
					String approval = dto[4].toString();
					if (approval.equals("1")) {
						result.setIntApproval(1);
					} else {
						result.setIntApproval(0);
					}
					String document = dto[5].toString();
					if (document.equals("1")) {
						result.setIntDocument(1);
					} else {
						result.setIntDocument(0);
					}

					String citizen = dto[6].toString();
					if (citizen.equals("1")) {
						result.setViewAtCitizen(1);
					} else {
						result.setViewAtCitizen(0);
					}
					// result.setViewAtCitizen(Integer.valueOf(String.valueOf(dto[6])));
					////System.out.println(dto[9]);
					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					 SimpleDateFormat dt2 = new SimpleDateFormat("dd-MMM-yyyy");
//					java.util.Date date = dt2.parse(String.valueOf(dto[9]));
//
//				        // *** same for the format String below
				    SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
//				    ////System.out.println(dt1.format(date));
//				    
					result.setDtmCreatedOn(dt1.format(dto[9]));
					result.setIntModuleId(Integer.valueOf(String.valueOf(dto[10])));
					result.setTinStatus(Integer.valueOf(String.valueOf(dto[11])));
					result.setTinPublishStatus(Integer.valueOf(String.valueOf(dto[39])));
//				 result.setIntCreatedBy(Integer.valueOf(String.valueOf(dto[18])));

					result.setVchSchemePoster(String.valueOf(dto[26]));
					result.setVchSchemeGuideline(String.valueOf(dto[27]));
					result.setTxtSchemeDescription(String.valueOf(dto[30]));

					result.setVchTableName(String.valueOf(dto[41]));
					result.setVchFormType(String.valueOf(dto[40]));

					// result.setIntWebsiteApplication(Integer.valueOf(String.valueOf(dto[43])));

					String adminApplication =  dto[42].toString();
					if (adminApplication.equals("1")) {
						result.setIntAdminApplication(1);
					} else {
						result.setIntAdminApplication(0);
					}

					String websiteApplication =  dto[43].toString();
					if (websiteApplication.equals("1")) {
						result.setIntWebsiteApplication(1);
					} else {
						result.setIntWebsiteApplication(0);
					}
					JSONArray sectionArray = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) dto[25]));
//					 result.setVchSection(String.valueOf(dto[25]));
					result.setVchSection(sectionArray);
					JSONArray apiUrlDtls = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) dto[33]));
					result.setVchAPIURLDtls(apiUrlDtls);
					// result.setVchAPIURLDtls(String.valueOf(dto[33]));
//				 result.setVchAPIReferenceURLDtls(vchApiRef);
					result.setVchModuleName(String.valueOf(dto[50]));
					JSONArray apiReferenceUrlDtls = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) dto[33]));
					// result.setVchAPIReferenceURLDtls(String.valueOf(dto[33]));
					result.setVchAPIReferenceURLDtls(apiReferenceUrlDtls);
					JSONArray apiStatusUrlDtls = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) dto[34]));
					result.setVchAPIStatusURLDtls(apiStatusUrlDtls);

					result.setVchSchemePosterUrl("http://" + Inet4Address.getLocalHost().getHostAddress() + ":"
							+ serverPort + "/downloadFormIcon/" + String.valueOf(dto[26]));
					result.setVchSchemePoster(String.valueOf(dto[26]));

					result.setVchSchemeGuideline(String.valueOf(dto[27]));

					result.setVchSchemeGuidelineUrl("http://" + Inet4Address.getLocalHost().getHostAddress() + ":"
							+ serverPort + "/downloadFormGuideline/" + String.valueOf(dto[27]));
					
					
					if(dto[29] != null) {
						result.setIntServiceMode(Integer.valueOf(String.valueOf(dto[29])));
					}
					else {
						result.setIntServiceMode(0);
					}
					listResult.add(result);
					// root.setResult(listResult);

				}

				root.setResult(listResult);

			}

		}

		catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return root;
	}

	@Override
	public List<Map<String, Object>> getModules() {
		List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();
		JSONArray array = new JSONArray();

		jdbcTemplate.query(
				" select intModuleId,vchModuleName from m_module_name where bitDeletedFlag =0 ORDER BY vchModuleName  ",
				new RowCallbackHandler() {
					public void processRow(ResultSet resultSet) throws SQLException {
						JSONObject object = null;
						do {

							try {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("intModuleId", resultSet.getInt("intModuleId"));
								map.put("vchModuleName", resultSet.getString("vchModuleName"));
								myList.add(map);

							} catch (Exception e) {
								logger.error(ExceptionUtils.getStackTrace(e));
							}

						} while (resultSet.next());

					}
				});

		return myList;

	}

//	@Override
//	public List<Map<String, Object>> getFormName(String formIdAndModuleId) {
//		JSONObject object = new JSONObject(formIdAndModuleId);
//		List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();
//		JSONArray array = new JSONArray();
//
//		jdbcTemplate.query(" select pn.intProcessId,pn.vchProcessName,mn.intModuleId,mn.vchModuleName from m_process_name pn inner join m_module_name mn "
//				+ " on pn.intModuleId = mn.intModuleId where pn.bitDeletedFlag = 0 and mn.bitDeletedFlag = 0 and pn.intModuleId ="+object.getInt("moduleId")+" and intProcessId = "+object.getInt("processId")+" ",
//				new RowCallbackHandler() {
//					public void processRow(ResultSet resultSet) throws SQLException {
//						JSONObject object = null;
//						do {
//
//							try {
//								Map<String, Object> map = new HashMap<String, Object>();
//								map.put("intProcessId", resultSet.getInt("intProcessId"));
//								map.put("vchProcessName", resultSet.getString("vchProcessName"));
//								map.put("intModuleId", resultSet.getInt("intModuleId"));
//								map.put("vchModuleName", resultSet.getString("vchModuleName"));
//								myList.add(map);
//
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//
//						} while (resultSet.next());
//
//					}
//				});
//
//		return myList;
//
//	}
	
}
