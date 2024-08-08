package com.project.bsky.serviceImpl;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import com.project.bsky.config.dto.AddMoreDetails;
import com.project.bsky.config.dto.Allformdetails;
import com.project.bsky.config.dto.ArrFormWiseValue;
import com.project.bsky.config.dto.GetDetails;
import com.project.bsky.config.dto.ResponseForPreview;
import com.project.bsky.config.dto.TableColDetailsApp;
import com.project.bsky.entity.DYNJSONOptionTextDetailsData;
import com.project.bsky.entity.DynamicFormConfigurationApp;
import com.project.bsky.entity.TOnlineServiceApplication;
import com.project.bsky.entity.TOnlineServiceApproval;
import com.project.bsky.repository.CommunicationRepository;
import com.project.bsky.repository.DYNJSONOptionTextDetailsDataRepository;
import com.project.bsky.repository.DynamicFormConfigurationAppRepository;
import com.project.bsky.repository.MDynamicSmsMailRepository;
import com.project.bsky.repository.ProcessRepository;
import com.project.bsky.repository.TOnlineServiceApplicationRepository;
import com.project.bsky.repository.TOnlineServiceApprovalRepository;
import com.project.bsky.repository.TSetAuthorityRepository;
import com.project.bsky.service.ApplicantProcessService;
import com.project.bsky.util.ConvertClobToJson;

@SuppressWarnings("deprecation")
@Service
public class ApplicantProcessServiceImpl implements ApplicantProcessService {
	
	@Autowired
	private Logger logger;

	@Autowired
	private ProcessRepository process_repo;

	@Autowired
	DynamicFormConfigurationAppRepository dfcar;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TOnlineServiceApplicationRepository tOnlineServiceApplicationRepository;

	@Autowired
	private MDynamicSmsMailRepository mDynamicSmsMailRepository;

	@Autowired
	private TSetAuthorityRepository tSetAuthorityRepository;

	@Autowired
	private CommunicationRepository communicationRepository;

	@Autowired
	private TOnlineServiceApprovalRepository tOnlineServiceApprovalRepository;

	@Autowired
	private DYNJSONOptionTextDetailsDataRepository dYNJSONOptionTextDetailsDataRepository;

	@Value("${server.port}")
	private String serverport;

	@Value("${pdf.documentPathForTemp}")
	private String documentPathForTemp;

	@Value("${pdf.documentPathForTarget}")
	private String documentPathForTarget;

	@Value("${pdf.documentPathForurl}")
	private String documentPathForurl;

	@Override
	public JSONObject getFormDetails() throws JSONException {
		List<Object[]> processlist = process_repo.getByProcessIdDetails();
		List<GetDetails> getDetailsList = new ArrayList<GetDetails>();
		for (Object[] obj : processlist) {
			GetDetails getDetails = new GetDetails();
			getDetails.setIntProcessId((Integer) obj[0]);
			getDetails.setTxtSchemeDescription((String) obj[1]);
			getDetails.setVchProcessName((String) obj[2]);
			getDetails.setVchSchemePoster((String) obj[3]);
			getDetailsList.add(getDetails);
		}
		JSONObject response = new JSONObject();
		response.put("status", "200");
		response.put("result", getDetailsList);
		return response;
	}

	@Override
	public JSONObject getSchemeApplyDetails(String data) throws JsonProcessingException, JSONException {
		JSONObject jsonObject = new JSONObject(data);
		String processid = jsonObject.getString("intProcessId");
		Integer intOnlineServiceId = jsonObject.getInt("intOnlineServiceId");
		Integer sectionId = jsonObject.getInt("sectionId");
		JSONObject response = new JSONObject();
		if (Integer.parseInt(processid) > 0 && processid != null) {
			List<Object[]> processlist = process_repo.getSectiondetails(Integer.parseInt(processid));
			List<Allformdetails> allFormDetailsList = new ArrayList<Allformdetails>();
			JSONArray json2 = null;
			String formName = "";

			for (Object[] obj : processlist) {
				formName = (String) obj[2];
				json2 = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) obj[0]));
				Integer count = 0;

				if (json2.length() != 0) {
					for (int i = 0; i < json2.length(); i++) {

						JSONObject jsonObj = json2.getJSONObject(i);
						Allformdetails allForm = new Allformdetails();
						allForm.setGridType(Byte.parseByte(obj[1].toString()));
						allForm.setSecSlno((jsonObj.getInt("slno")));
						allForm.setSectionid(jsonObj.getInt("sectionid"));
						allForm.setSectionName(jsonObj.getString("sectionName"));
						JSONObject json6 = new JSONObject();
						allForm.setAddMoreValueDetails(json6);
						JSONArray jsonArr = new JSONArray();
						String formDetailsList = null;
						if (sectionId != 0) {
							if (sectionId == Integer.parseInt(jsonObj.getString("sectionid"))) {
								formDetailsList = ConvertClobToJson.convertClobToJSONString(
										dfcar.getFormDetailsBySectionId(sectionId, Integer.parseInt(processid)));
								jsonArr = new JSONArray(String.valueOf(formDetailsList));
								JSONObject jsonAddmore = new JSONObject();
								for (int j = 0; j < jsonArr.length(); j++) {

									JSONObject jsonObjectforAddMore = jsonArr.getJSONObject(j);
									if (jsonObjectforAddMore.getInt("ctrlTypeId") == 10) {
										String addMoreColumn = "jsonOptTxtDetails,";
										// //System.out.println("This is addmore");
										String addmoreTableName = jsonObjectforAddMore.getString("ctrlTableName");

										JSONObject jsonaddmore = new JSONObject();
										jsonaddmore.put("addMoreCtrlId", jsonObjectforAddMore.getString("ctrlName"));

										JSONArray jsonAddmoreFormDetails = jsonObjectforAddMore
												.getJSONArray("addmoreDetails");
										for (int n = 0; n < jsonAddmoreFormDetails.length(); n++) {
											JSONObject jsonObjectForAddmoreFormDetails = jsonAddmoreFormDetails
													.getJSONObject(n);
											JSONArray jsonArrayTableDetails = (JSONArray) jsonObjectForAddmoreFormDetails
													.get("addmoretablecolDetails");
											for (int h = 0; h < jsonArrayTableDetails.length(); h++) {
												JSONObject jsonObjectForaddmorwTableDetails = jsonArrayTableDetails
														.getJSONObject(h);
												if (!jsonObjectForaddmorwTableDetails.getString("ctrlTblColName")
														.equals("")) {
													addMoreColumn = addMoreColumn + jsonObjectForaddmorwTableDetails
															.getString("ctrlTblColName") + ",";
												}
											}
										}
										String finalColumns = "";
										if (!addMoreColumn.equals("")) {
											finalColumns = addMoreColumn.substring(0, addMoreColumn.length() - 1);
										}
										String selectQuery = "select INTID," + finalColumns + " from "
												+ addmoreTableName + " where bitDeletedFlag=0 and intParentId="
												+ intOnlineServiceId;
										List<Map<String, Object>> dataList = jdbcTemplate.queryForList(selectQuery);

										for (Map<String, Object> mapData : dataList) {
											JSONObject json = new JSONObject(mapData);
											String queryForjsonData = "select JSONOPTIONTEXTDETAILS from DYN_JSOPTTEDTL_DATA where ONLINESERVICEID="
													+ intOnlineServiceId + " and TABLENAME='" + addmoreTableName
													+ "' and INTID=" + json.getInt("INTID");
											String streetName = "";
											try {
												streetName = jdbcTemplate.queryForObject(queryForjsonData,
														String.class);
											} catch (Exception ex) {
												logger.error(ExceptionUtils.getStackTrace(ex));
											}
											mapData.put("jsonOptTxtDetails", streetName);
										}

										jsonaddmore.put("addMoreDataValue", dataList);

										jsonAddmore.put(jsonObjectforAddMore.getString("ctrlName"), jsonaddmore);

									}

								}
								allForm.setAddMoreValueDetails(jsonAddmore);
								String columnQuery = "";
								for (int f = 0; f < jsonArr.length(); f++) {
									JSONObject jsonTable = jsonArr.getJSONObject(f);
									JSONArray tableDetails = (JSONArray) jsonTable.get("tablecolDetails");
									for (int j = 0; j < tableDetails.length(); j++) {
										JSONObject jsonA = tableDetails.getJSONObject(j);
										if (!jsonA.getString("ctrlTblColName").equals("")) {
											columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + ",";
										}
									}
								}
								String finalColumns = "";
								if (!columnQuery.equals("")) {
									finalColumns = columnQuery.substring(0, columnQuery.length() - 1);
								} else {
									finalColumns = "";
								}
								String tableName = dfcar.getTableNameSectionWise(
										Integer.parseInt(jsonObj.getString("sectionid")), Integer.parseInt(processid));
								if (Strings.isNullOrEmpty(tableName)) {
									tableName = (String) obj[3];
								}

								
								String selectQueryString=null;
								if(finalColumns.equals("")) {
									selectQueryString="select D.INTID,J.JSONOPTIONTEXTDETAILS from " + tableName+" D left join DYN_JSOPTTEDTL_DATA J"
											+" on D.intId = J.intid and J.tablename =  '" + tableName+
											"' where bitDeletedFlag=0 and intOnlineServiceId=" + intOnlineServiceId;
								}else {
									selectQueryString="select D.INTID," + finalColumns + ",J.JSONOPTIONTEXTDETAILS from " + tableName+" D left join DYN_JSOPTTEDTL_DATA J"
											+" on D.intId = J.intid and J.tablename =  '" + tableName+
											"' where bitDeletedFlag=0 and intOnlineServiceId=" + intOnlineServiceId;
								}
								
								
								List<Map<String, Object>> dataList = jdbcTemplate.queryForList(selectQueryString);
								if (dataList.size() > 0) {
									JSONObject jsonObjectData=new JSONObject(dataList.get(0));
									jsonObjectData.remove("INTID");
									allForm.setDataValue(jsonObjectData.toString());
								} else {
									allForm.setDataValue(null);
								}

							}

						} else if (count == 0) {
							formDetailsList = ConvertClobToJson.convertClobToJSONString(dfcar.getFormDetailsBySectionId(
									Integer.parseInt(jsonObj.getString("sectionid")), Integer.parseInt(processid)));
							jsonArr = new JSONArray(String.valueOf(formDetailsList));
							for (int j = 0; j < jsonArr.length(); j++) {
								JSONObject jsonObjectforAddMore = jsonArr.getJSONObject(j);
								if (jsonObjectforAddMore.getInt("ctrlTypeId") == 10) {
									String addMoreColumn = "jsonOptTxtDetails,";
									String addmoreTableName = jsonObjectforAddMore.getString("ctrlTableName");

									JSONObject jsonaddmore = new JSONObject();
									jsonaddmore.put("addMoreCtrlId", jsonObjectforAddMore.getString("ctrlName"));

									JSONArray jsonAddmoreFormDetails = jsonObjectforAddMore
											.getJSONArray("addmoreDetails");
									for (int n = 0; n < jsonAddmoreFormDetails.length(); n++) {
										JSONObject jsonObjectForAddmoreFormDetails = jsonAddmoreFormDetails
												.getJSONObject(n);
										JSONArray jsonArrayTableDetails = (JSONArray) jsonObjectForAddmoreFormDetails
												.get("addmoretablecolDetails");
										for (int h = 0; h < jsonArrayTableDetails.length(); h++) {
											JSONObject jsonObjectForaddmorwTableDetails = jsonArrayTableDetails
													.getJSONObject(h);
											if (!jsonObjectForaddmorwTableDetails.getString("ctrlTblColName")
													.equals("")) {
												addMoreColumn = addMoreColumn
														+ jsonObjectForaddmorwTableDetails.getString("ctrlTblColName")
														+ ",";
											}
										}
									}
									String finalColumns = "";
									if (!addMoreColumn.equals("")) {
										finalColumns = addMoreColumn.substring(0, addMoreColumn.length() - 1);
									}
									String selectQuery = "select INTID," + finalColumns + " from " + addmoreTableName
											+ " where bitDeletedFlag=0 and intParentId=" + intOnlineServiceId;
									List<Map<String, Object>> dataList = jdbcTemplate.queryForList(selectQuery);
									for (Map<String, Object> mapData : dataList) {
										JSONObject json = new JSONObject(mapData);
										String queryForjsonData = "select JSONOPTIONTEXTDETAILS from DYN_JSOPTTEDTL_DATA where ONLINESERVICEID="
												+ intOnlineServiceId + " and TABLENAME='" + addmoreTableName
												+ "' and INTID=" + json.getInt("INTID");
										// //System.out.println(queryForjsonData);
										String streetName = jdbcTemplate.queryForObject(queryForjsonData, String.class);
										mapData.put("jsonOptTxtDetails", streetName);
									}

									jsonaddmore.put("addMoreDataValue", dataList);

									JSONObject jsonAddmore = new JSONObject();
									jsonAddmore.put(jsonObjectforAddMore.getString("ctrlName"), jsonaddmore);
									allForm.setAddMoreValueDetails(jsonAddmore);
								}
							}
							String columnQuery = "intId,";
							JSONArray json = new JSONArray(formDetailsList);
							for (int f = 0; f < json.length(); f++) {
								JSONObject jsonTable = json.getJSONObject(f);
								JSONArray tableDetails = (JSONArray) jsonTable.get("tablecolDetails");
								for (int j = 0; j < tableDetails.length(); j++) {
									JSONObject jsonA = tableDetails.getJSONObject(j);
									if (!jsonA.getString("ctrlTblColName").equals("")) {
										columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + ",";
									}
								}
							}
							String finalColumns = "";
							if (!columnQuery.equals("")) {
								finalColumns = columnQuery.substring(0, columnQuery.length() - 1);
							} else {
								finalColumns = "*";
							}

							String tableName = dfcar.getTableNameSectionWise(
									Integer.parseInt(jsonObj.getString("sectionid")), Integer.parseInt(processid));

							if (Strings.isNullOrEmpty(tableName)) {
								tableName = (String) obj[3];
							}
							String selectQuery = "select " + finalColumns + " from " + tableName
									+ " where bitDeletedFlag=0 and intOnlineServiceId=" + intOnlineServiceId;
							List<Map<String, Object>> dataList = jdbcTemplate.queryForList(selectQuery);
							if (dataList.size() > 0) {
								allForm.setDataValue(new JSONObject(dataList.get(0)).toString());
							} else {
								allForm.setDataValue(null);
							}
						}
						allForm.setFormDetails(jsonArr);
						allFormDetailsList.add(allForm);
						count++;
					}
				} else {
					DynamicFormConfigurationApp formListSecNotPre = dfcar
							.getFormDataSecListNotPre(Integer.parseInt(processid));
					Byte getGridType = process_repo.getGridType(Integer.parseInt(processid));
					Allformdetails allForm = new Allformdetails();
					allForm.setSectionName("");
					allForm.setSecSlno(0);
					allForm.setSectionid(0);
					allForm.setGridType(getGridType);
					JSONObject json1 = new JSONObject();
					allForm.setAddMoreValueDetails(json1);
					DynamicFormConfigurationApp getDataByProcessIdAndSectionId = dfcar
							.getDataByProcessIdAndSectionId(Integer.parseInt(processid), sectionId);
					String columnQuery = "intId,";
					JSONObject json = new JSONObject(getDataByProcessIdAndSectionId);

					String formDetails = json.getString("formDetails");
					JSONArray jsonArray = new JSONArray(formDetails);

					for (int j = 0; j < jsonArray.length(); j++) {
						JSONObject jsonObjectforAddMore = jsonArray.getJSONObject(j);
						if (jsonObjectforAddMore.getInt("ctrlTypeId") == 10) {
							String addMoreColumn = "jsonOptTxtDetails,";
							// //System.out.println("This is addmore");
							String addmoreTableName = jsonObjectforAddMore.getString("ctrlTableName");

							JSONObject jsonaddmore = new JSONObject();
							jsonaddmore.put("addMoreCtrlId", jsonObjectforAddMore.getString("ctrlName"));

							JSONArray jsonAddmoreFormDetails = jsonObjectforAddMore.getJSONArray("addmoreDetails");
							for (int n = 0; n < jsonAddmoreFormDetails.length(); n++) {
								JSONObject jsonObjectForAddmoreFormDetails = jsonAddmoreFormDetails.getJSONObject(n);
								JSONArray jsonArrayTableDetails = (JSONArray) jsonObjectForAddmoreFormDetails
										.get("addmoretablecolDetails");
								for (int h = 0; h < jsonArrayTableDetails.length(); h++) {
									JSONObject jsonObjectForaddmorwTableDetails = jsonArrayTableDetails
											.getJSONObject(h);
									if (!jsonObjectForaddmorwTableDetails.getString("ctrlTblColName").equals("")) {
										addMoreColumn = addMoreColumn
												+ jsonObjectForaddmorwTableDetails.getString("ctrlTblColName") + ",";
									}
								}
							}
							String finalColumns = "";
							if (!addMoreColumn.equals("")) {
								finalColumns = addMoreColumn.substring(0, addMoreColumn.length() - 1);
							}
							String selectQuery = "select INTID," + finalColumns + " from " + addmoreTableName
									+ " where bitDeletedFlag=0 and intParentId=" + intOnlineServiceId;
							List<Map<String, Object>> dataList = jdbcTemplate.queryForList(selectQuery);
							for (Map<String, Object> mapData : dataList) {
								JSONObject json8 = new JSONObject(mapData);
								String queryForjsonData = "select JSONOPTIONTEXTDETAILS from DYN_JSOPTTEDTL_DATA where ONLINESERVICEID="
										+ intOnlineServiceId + " and TABLENAME='" + addmoreTableName + "' and INTID="
										+ json8.getInt("INTID");
								// //System.out.println(queryForjsonData);
								try {
									String streetName = jdbcTemplate.queryForObject(queryForjsonData, String.class);
									mapData.put("jsonOptTxtDetails", streetName);
								} catch (Exception e) {
									logger.error(ExceptionUtils.getStackTrace(e));
								}
							}

							jsonaddmore.put("addMoreDataValue", dataList);

							JSONObject jsonAddmore = new JSONObject();
							jsonAddmore.put(jsonObjectforAddMore.getString("ctrlName"), jsonaddmore);
							allForm.setAddMoreValueDetails(jsonAddmore);
						}
					}

					for (int ii = 0; ii < jsonArray.length(); ii++) {
						JSONObject jsonArr1 = jsonArray.getJSONObject(ii);

						JSONArray tableDetails = (JSONArray) jsonArr1.get("tablecolDetails");
						for (int j = 0; j < tableDetails.length(); j++) {
							JSONObject jsonA = tableDetails.getJSONObject(j);
							if (!jsonA.getString("ctrlTblColName").equals("")) {
								columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + ",";
							}

						}
					}
					String finalColumns = "";
					if (!columnQuery.equals("")) {
						finalColumns = columnQuery.substring(0, columnQuery.length() - 1);
					} else {
						finalColumns = "*";
					}
					String selectQuery = "select " + finalColumns + " from " + obj[3].toString().toUpperCase()
							+ " where bitDeletedFlag=0 and intOnlineServiceId=" + intOnlineServiceId;
					// //System.out.println(selectQuery);
					List<Map<String, Object>> dataList = jdbcTemplate.queryForList(selectQuery);
					if (dataList.size() > 0) {
						allForm.setDataValue(new JSONObject(dataList.get(0)).toString());
					} else {
						allForm.setDataValue(null);
					}

					JSONObject jsonObj = new JSONObject(formListSecNotPre);
					JSONArray jArray = new JSONArray(String.valueOf(jsonObj.get("formDetails")));
					allForm.setFormDetails(jArray);
					allFormDetailsList.add(allForm);
				}

				String sectionid = "";
				JSONArray json = null;
				String tablename = "";

				if (processlist.size() > 0) {
					for (Object[] obj1 : processlist) {
						json = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) obj1[0]));
						tablename = (obj1[3].toString());
					}

					if (json.length() == 0) {
						sectionid = "0";
					}
					for (int i = 0; i < json.length(); i++) {
						JSONObject jsonObj = json.getJSONObject(i);
						if (jsonObj.getInt("slno") == 1) {
							sectionid = jsonObj.getString("sectionid");
							break;
						}
					}
					List<DynamicFormConfigurationApp> getdetailsdfcra = dfcar
							.getDataByItemIdApp(Integer.parseInt(processid), Integer.parseInt(sectionid));

					List<TableColDetailsApp> tableColDetailsList = new ArrayList<TableColDetailsApp>();
					for (DynamicFormConfigurationApp dfc : getdetailsdfcra) {
						JSONObject json1 = new JSONObject(dfc);
						String formDetails = json1.getString("formDetails");

						JSONArray jsonArr = new JSONArray(formDetails);
						for (int i = 0; i < jsonArr.length(); i++) {
							JSONObject jsona = jsonArr.getJSONObject(i);
							JSONArray jso = (JSONArray) jsona.get("tablecolDetails");
							for (int j = 0; j < jso.length(); j++) {
								JSONObject jsondata = jso.getJSONObject(j);
								TableColDetailsApp tcd = new TableColDetailsApp();
								tcd.setCtrlTblColName(jsondata.getString("ctrlTblColName"));
								tableColDetailsList.add(tcd);
							}
						}
					}
					ObjectMapper mp = new ObjectMapper();
					mp.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
					mp.enable(SerializationFeature.INDENT_OUTPUT);
					JSONArray jsonArray = new JSONArray();
					for (Allformdetails allFormdetails : allFormDetailsList) {
					JSONObject jsonObj = new JSONObject(allFormdetails);
					jsonObj.put("formDetails", allFormdetails.getFormDetails());
					jsonObj.put("addMoreValueDetails", allFormdetails.getAddMoreValueDetails());
					String jsonObj5 = mp.writeValueAsString(allFormdetails);
					JSONObject jsonObj90 = new JSONObject(jsonObj5);
					if (jsonObj90.isNull("VCH_FILE_MSTAFF")) {
					        jsonObj.remove("VCH_FILE_MSTAFF");
					    }
					if (jsonObj90.isNull("VCH_FILE_PMSTAFF")) {
					        jsonObj.remove("VCH_FILE_PMSTAFF");
					    }
					if (jsonObj90.isNull("VCH_FILE_NON_TECHNICAL")) {
					        jsonObj.remove("VCH_FILE_NON_TECHNICAL");
					    }
					 
					if (jsonObj90.isNull("dataValue")) {
					        jsonObj.put("dataValue", new JSONObject());
					    }
					 
					else {
					JSONObject replacedata = new JSONObject(jsonObj90.getString("dataValue"));
					jsonObj.put("dataValue", replacedata);
					}
					jsonArray.put(jsonObj);
					}

					
					JSONObject finalJson = new JSONObject();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						if (json2.length() != 0) {
							finalJson.put("sec_" + jsonObj.getInt("secSlno") + "_" + jsonObj.getInt("sectionid"),
									jsonObj);
						} else {
							finalJson.put("sec_" + jsonObj.getInt("sectionid"), jsonObj);
						}
					}
					response.put("status", 200);
					response.put("formName", formName);
					response.put("result", finalJson);
					return response;
				} else {
					sectionid = "0";
					response.put("status", "200");
					response.put("result", processlist);
					return response;
				}
			}
		}
		return response;
	}

	@Override
	public JSONObject schemeApply(WebRequest request, Integer processId, Integer sectionId, Integer intOnlineServiceId,
			Integer projectId, Integer userId) throws JSONException {
		String mainTable = process_repo.getTableName(processId);
		DynamicFormConfigurationApp getDataByProcessIdAndSectionId = dfcar.getDataByProcessIdAndSectionId(processId,
				sectionId);
		JSONObject json = new JSONObject(getDataByProcessIdAndSectionId);
		String formDetails = json.getString("formDetails");
		JSONObject response = new JSONObject();
		JSONArray jsonArray = new JSONArray(formDetails);
		// For validation
		Integer valid = 0;
		String msg = "";
		String query = "Select jsonOptTxtDetails from " + mainTable + " where intOnlineServiceId=" + intOnlineServiceId;
		String streetName = null;
		try {
			streetName = jdbcTemplate.queryForObject(query, String.class);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		if (valid == 0) {
			if (intOnlineServiceId.equals(0)) {
				TOnlineServiceApplication tOnlineServiceApp = new TOnlineServiceApplication();
				tOnlineServiceApp.setIntProfileId(projectId);
				tOnlineServiceApp.setIntProcessId(processId);
				tOnlineServiceApp.setIntCreatedBy(1);
				SimpleDateFormat obj = new SimpleDateFormat("dd-MMM-yy");
				Date res = new Date(System.currentTimeMillis());
				tOnlineServiceApp.setDtmCreatedOn(res);
				tOnlineServiceApp.setBitDeletedFlag((byte) 0);
				tOnlineServiceApp.setIntApplicationStatus(1);
				tOnlineServiceApp.setTinResubmitStatus((byte) 0);
				tOnlineServiceApp.setTinQueryStatus((byte) 0);
				tOnlineServiceApp.setTinApprovalStatus((byte) 0);
				TOnlineServiceApplication tServiceApp = tOnlineServiceApplicationRepository.save(tOnlineServiceApp);
				intOnlineServiceId = tServiceApp.getIntOnlineServiceId();
				String tableName = null;
				if (json.has("vchSectionWiseTableName")) {
					tableName = json.getString("vchSectionWiseTableName");
				} else {
					tableName = mainTable;
				}

				String StartQuery = "Insert into " + tableName + "(";
				String columnQuery = "";
				String valueQuery = "";
				String optionCol = "";
				JSONObject option = new JSONObject();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonArr = jsonArray.getJSONObject(i);
					if (jsonArr.getInt("ctrlTypeId") == 1) {
						JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
						for (int k = 0; k < tableDetails.length(); k++) {
							JSONObject dataField = tableDetails.getJSONObject(k);
							if (dataField.getString("ctrlTblColName") != "") {
								columnQuery = columnQuery + dataField.getString("ctrlTblColName") + ",";
							}
						}
						valueQuery = valueQuery + "'" + jsonArr.getString("ctrlLabelData") + "',";

					} else if (jsonArr.getInt("ctrlTypeId") == 7) {
						String uploadStringData = request
								.getParameter("uploadedFiles[" + jsonArr.getString("ctrlId") + "]");
						JSONObject uploadData = new JSONObject(uploadStringData);
						JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
						for (int j = 0; j < tableDetails.length(); j++) {
							JSONObject jsonA = tableDetails.getJSONObject(j);
							columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + ",";
						}
						valueQuery = valueQuery + "'" + uploadData.getString("fileName") + "',";
					} else if (jsonArr.getInt("ctrlTypeId") == 3) {
						optionCol = ",jsonOptTxtDetails";
						JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
						for (int k = 0; k < tableDetails.length(); k++) {
							JSONObject dataField = tableDetails.getJSONObject(k);
							if (dataField.getString("ctrlTblColName") != "") {
								columnQuery = columnQuery + dataField.getString("ctrlTblColName") + ",";
								option.put(dataField.getString("ctrlTblColName"),
										request.getParameter("ctrlValueText[" + jsonArr.getString("ctrlId") + "]"));
							}

						}
						valueQuery = valueQuery + "'"
								+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]") + "',";

					} else if (jsonArr.getInt("ctrlTypeId") == 5) {
						optionCol = ",jsonOptTxtDetails";
						JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
						for (int k = 0; k < tableDetails.length(); k++) {
							JSONObject dataField = tableDetails.getJSONObject(k);
							if (dataField.getString("ctrlTblColName") != "") {
								columnQuery = columnQuery + dataField.getString("ctrlTblColName") + ",";
								option.put(dataField.getString("ctrlTblColName"),
										request.getParameter("ctrlValueText[" + jsonArr.getString("ctrlId") + "]"));
							}

						}
						valueQuery = valueQuery + "'"
								+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]") + "',";

					} else if (jsonArr.getInt("ctrlTypeId") == 6) {
						optionCol = ",jsonOptTxtDetails";
						JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
						for (int k = 0; k < tableDetails.length(); k++) {
							JSONObject dataField = tableDetails.getJSONObject(k);
							if (dataField.getString("ctrlTblColName") != "") {
								columnQuery = columnQuery + dataField.getString("ctrlTblColName") + ",";
								option.put(dataField.getString("ctrlTblColName"),
										request.getParameter("ctrlValueText[" + jsonArr.getString("ctrlId") + "]"));
							}

						}
						valueQuery = valueQuery + "'"
								+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]") + "',";

					} else if (jsonArr.getInt("ctrlTypeId") == 8) {
						JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
						for (int j = 0; j < tableDetails.length(); j++) {
							JSONObject jsonA = tableDetails.getJSONObject(j);
							if (jsonA.getString("ctrlTblColName") != "") {
								columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + ",";
							}

						}
						valueQuery = valueQuery + "'"
								+ request.getParameter("lblName[" + jsonArr.getString("ctrlId") + "]") + "',";
					} else if (jsonArr.getInt("ctrlTypeId") == 10) {
						if (!request.getParameter("addMoreElementData[" + jsonArr.getString("ctrlId") + "]")
								.equalsIgnoreCase("undefined")) {
							JSONArray addMoreJsonArray = new JSONArray(
									request.getParameter("addMoreElementData[" + jsonArr.getString("ctrlId") + "]"));
							for (int j = 0; j < addMoreJsonArray.length(); j++) {
								String StartQuery1 = "Insert into " + jsonArr.getString("ctrlTableName") + "(";
								String columnQuery1 = "";
								String valueQuery1 = "";
								String optionCol1 = "";
								JSONObject option1 = new JSONObject();
								JSONArray jsonArrayFirstArray = addMoreJsonArray.getJSONArray(j);
								JSONArray formDetailsArr = jsonArr.getJSONArray("addmoreDetails");
								for (int b = 0; b < formDetailsArr.length(); b++) {
									JSONObject jsonObject = formDetailsArr.getJSONObject(b);
									JSONArray addmoretablecolDetailsList = jsonObject
											.getJSONArray("addmoretablecolDetails");
									for (int d = 0; d < addmoretablecolDetailsList.length(); d++) {
										JSONObject jsonObje = addmoretablecolDetailsList.getJSONObject(d);
										for (int ii = 0; ii < jsonArrayFirstArray.length(); ii++) {
											JSONObject jsonObject1 = jsonArrayFirstArray.getJSONObject(ii);
											if (jsonObject.getString("ctrlId")
													.equals(jsonObject1.getString("ctrlId"))) {
												if (jsonObject1.getInt("ctrlTypeId") == 3) {
													optionCol1 = ",jsonOptTxtDetails";
													if (jsonObject.getString("ctrlId")
															.equals(jsonObject1.getString("ctrlId"))) {
														if (jsonObje.getString("ctrlTblColName") != "") {
															columnQuery1 = columnQuery1
																	+ jsonObje.getString("ctrlTblColName") + ",";
														}
														valueQuery1 = valueQuery1 + "'"
																+ jsonObject1.getString("ctrlValue") + "',";
														option1.put(jsonObje.getString("ctrlTblColName"),
																jsonObject1.getString("ctrlValueText"));
														break;
													}
												} else if (jsonObject.getInt("ctrlTypeId") == 1) {
													JSONArray tableDetails = (JSONArray) jsonObject
															.get("addmoretablecolDetails");

													for (int k = 0; k < tableDetails.length(); k++) {
														JSONObject dataField = tableDetails.getJSONObject(k);
														if (dataField.getString("ctrlTblColName") != "") {
															columnQuery1 = columnQuery1
																	+ dataField.getString("ctrlTblColName") + ",";
														}
													}
													valueQuery1 = valueQuery1 + "'"
															+ jsonObject.getString("ctrlLabelData") + "',";
												} else if (jsonObject.getInt("ctrlTypeId") == 5) {
													optionCol1 = ",jsonOptTxtDetails";
													if (jsonObject.getString("ctrlId")
															.equals(jsonObject1.getString("ctrlId"))) {
														if (jsonObje.getString("ctrlTblColName") != "") {
															columnQuery1 = columnQuery1
																	+ jsonObje.getString("ctrlTblColName") + ",";
														}
														valueQuery1 = valueQuery1 + "'"
																+ jsonObject1.getString("ctrlValue") + "',";
														option1.put(jsonObje.getString("ctrlTblColName"),
																jsonObject1.getString("ctrlValueText"));
														break;
													}
												} else if (jsonObject.getInt("ctrlTypeId") == 6) {
													optionCol1 = ",jsonOptTxtDetails";
													if (jsonObject.getString("ctrlId")
															.equals(jsonObject1.getString("ctrlId"))) {
														if (jsonObje.getString("ctrlTblColName") != "") {
															columnQuery1 = columnQuery1
																	+ jsonObje.getString("ctrlTblColName") + ",";
														}
														valueQuery1 = valueQuery1 + "'"
																+ jsonObject1.getString("ctrlValue") + "',";
														option1.put(jsonObje.getString("ctrlTblColName"),
																jsonObject1.getString("ctrlValueText"));
														break;
													}
												} else if (jsonObject.getInt("ctrlTypeId") == 7) {
													if (jsonObject.getString("ctrlId")
															.equals(jsonObject1.getString("ctrlId"))) {
														if (jsonObje.getString("ctrlTblColName") != "") {
															columnQuery1 = columnQuery1
																	+ jsonObje.getString("ctrlTblColName") + ",";
														}
														JSONObject json3 = (JSONObject) jsonObject1.get("uploadFile");

														valueQuery1 = valueQuery1 + "'" + json3.getString("fileName")
																+ "',";
														break;
													}
												} else {
													if (jsonObje.getString("ctrlTblColName") != "") {
														columnQuery1 = columnQuery1
																+ jsonObje.getString("ctrlTblColName") + ",";
													}
													valueQuery1 = valueQuery1 + "'" + jsonObject1.getString("ctrlValue")
															+ "',";
													break;
												}

											}

										}
									}

								}
								String finalQuery1;
								if (optionCol1 == "") {
									finalQuery1 = StartQuery1 + columnQuery1
											+ "intCreatedBy,dtmCreatedOn,intParentId,bitDeletedFlag" + optionCol1
											+ ")values(" + valueQuery1 + "1,'" + obj.format(res) + "',"
											+ intOnlineServiceId + ",0)";
								} else {
									finalQuery1 = StartQuery1 + columnQuery1
											+ "intCreatedBy,dtmCreatedOn,intParentId,bitDeletedFlag" + optionCol1
											+ ")values(" + valueQuery1 + "1,'" + obj.format(res) + "',"
											+ intOnlineServiceId + ",0)";
								}
								KeyHolder keyHolder = new GeneratedKeyHolder();
								jdbcTemplate.update(new PreparedStatementCreator() {
									public PreparedStatement createPreparedStatement(Connection connection)
											throws SQLException {
										PreparedStatement ps = null;
										try {
											ps = connection.prepareStatement(finalQuery1, new String[] { "intId" });
										} catch (SQLException e) {
											logger.error(ExceptionUtils.getStackTrace(e));
										}
										return ps;
									}
								}, keyHolder);
								Integer intId = keyHolder.getKey().intValue();

								if (optionCol1 != "") {
									DYNJSONOptionTextDetailsData dynJSONSet = new DYNJSONOptionTextDetailsData();
									dynJSONSet.setIntId(intId);
									dynJSONSet.setOnlineServiceId(intOnlineServiceId);
									dynJSONSet.setTableName(tableName);
									dynJSONSet.setJsonOptionTextDetails(option1.toString());
									dYNJSONOptionTextDetailsDataRepository.save(dynJSONSet);
								}

							}
						}

					} else {
						JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
						for (int j = 0; j < tableDetails.length(); j++) {
							JSONObject jsonA = tableDetails.getJSONObject(j);
							if (jsonA.getString("ctrlTblColName") != "") {
								columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + ",";
							}

						}
						valueQuery = valueQuery + "'"
								+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]") + "',";

					}
				}
				String finalQuery;
				if (optionCol == "") {
					finalQuery = StartQuery + columnQuery
							+ "intCreatedBy,dtmCreatedOn,intOnlineServiceId,bitDeletedFlag,jsonOptTxtDetails)values("
							+ valueQuery + "1,'" + obj.format(res) + "'," + intOnlineServiceId + ",0,null)";
				} else {
					finalQuery = StartQuery + columnQuery
							+ "intCreatedBy,dtmCreatedOn,intOnlineServiceId,bitDeletedFlag,jsonOptTxtDetails)values("
							+ valueQuery + "1,'" + obj.format(res) + "'," + intOnlineServiceId + ",0,null)";
				}
				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = null;
						try {
							ps = connection.prepareStatement(finalQuery, new String[] { "intId" });
						} catch (SQLException e) {
							logger.error(ExceptionUtils.getStackTrace(e));
						}
						return ps;
					}
				}, keyHolder);
				Integer intId = keyHolder.getKey().intValue();

				if (optionCol != "") {
					DYNJSONOptionTextDetailsData dynJSONSet = new DYNJSONOptionTextDetailsData();
					dynJSONSet.setIntId(intId);
					dynJSONSet.setOnlineServiceId(intOnlineServiceId);
					dynJSONSet.setTableName(tableName);
					dynJSONSet.setJsonOptionTextDetails(option.toString());
					dYNJSONOptionTextDetailsDataRepository.save(dynJSONSet);
				}

				if (!tableName.equals(mainTable)) {
					String firstQuery = "Insert into " + mainTable + "(";
					String endQuery = "intCreatedBy,dtmCreatedOn,intOnlineServiceId,bitDeletedFlag,jsonOptTxtDetails)values("
							+ "1,'" + obj.format(res) + "'," + intOnlineServiceId + ",0,null)";

					String finalQueryForTable = firstQuery + endQuery;
					KeyHolder keyHolder3 = new GeneratedKeyHolder();
					// //System.out.println(finalQueryForTable);
					jdbcTemplate.update(new PreparedStatementCreator() {

						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(finalQueryForTable);
							} catch (SQLException e) {
								logger.error(ExceptionUtils.getStackTrace(e));
							}
							return ps;
						}
					}, keyHolder3);
				}

			} else {

				SimpleDateFormat obj = new SimpleDateFormat("dd-MMM-yy");
				Date res = new Date(System.currentTimeMillis());
				// //System.out.println("Update by" + userId);
				tOnlineServiceApplicationRepository.updateBy(obj.format(res), intOnlineServiceId);
				String tableName = null;
				if (json.has("vchSectionWiseTableName")) {
					tableName = json.getString("vchSectionWiseTableName");
				} else {
					tableName = mainTable;
				}
				String sql = "select intId from " + tableName.toUpperCase() + " where intOnlineServiceId="
						+ intOnlineServiceId;
				Integer intId = null;
				try {
					intId = (Integer) jdbcTemplate.queryForObject(sql, Integer.class);
				} catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
					String StartQuery = "Insert into " + tableName + "(";
					String columnQuery = "";
					String valueQuery = "";
					String optionCol = "";
					JSONObject option = new JSONObject();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonArr = jsonArray.getJSONObject(i);
						if (jsonArr.getInt("ctrlTypeId") == 1) {
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int k = 0; k < tableDetails.length(); k++) {
								JSONObject dataField = tableDetails.getJSONObject(k);
								if (dataField.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + dataField.getString("ctrlTblColName") + ",";
								}
							}
							valueQuery = valueQuery + "'" + jsonArr.getString("ctrlLabelData") + "',";

						} else if (jsonArr.getInt("ctrlTypeId") == 7) {
							String uploadStringData = request
									.getParameter("uploadedFiles[" + jsonArr.getString("ctrlId") + "]");
							JSONObject uploadData = new JSONObject(uploadStringData);
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int j = 0; j < tableDetails.length(); j++) {
								JSONObject jsonA = tableDetails.getJSONObject(j);
								columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + ",";
							}
							valueQuery = valueQuery + "'" + uploadData.getString("fileName") + "',";
						} else if (jsonArr.getInt("ctrlTypeId") == 3) {
							optionCol = ",jsonOptTxtDetails";
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int k = 0; k < tableDetails.length(); k++) {
								JSONObject dataField = tableDetails.getJSONObject(k);
								if (dataField.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + dataField.getString("ctrlTblColName") + ",";
									option.put(dataField.getString("ctrlTblColName"),
											request.getParameter("ctrlValueText[" + jsonArr.getString("ctrlId") + "]"));
								}

							}
							valueQuery = valueQuery + "'"
									+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]") + "',";

						} else if (jsonArr.getInt("ctrlTypeId") == 5) {
							optionCol = ",jsonOptTxtDetails";
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int k = 0; k < tableDetails.length(); k++) {
								JSONObject dataField = tableDetails.getJSONObject(k);
								if (dataField.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + dataField.getString("ctrlTblColName") + ",";
									option.put(dataField.getString("ctrlTblColName"),
											request.getParameter("ctrlValueText[" + jsonArr.getString("ctrlId") + "]"));
								}

							}
							valueQuery = valueQuery + "'"
									+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]") + "',";

						} else if (jsonArr.getInt("ctrlTypeId") == 6) {
							optionCol = ",jsonOptTxtDetails";
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int k = 0; k < tableDetails.length(); k++) {
								JSONObject dataField = tableDetails.getJSONObject(k);
								if (dataField.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + dataField.getString("ctrlTblColName") + ",";
									option.put(dataField.getString("ctrlTblColName"),
											request.getParameter("ctrlValueText[" + jsonArr.getString("ctrlId") + "]"));
								}

							}
							valueQuery = valueQuery + "'"
									+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]") + "',";

						} else if (jsonArr.getInt("ctrlTypeId") == 8) {
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int j = 0; j < tableDetails.length(); j++) {
								JSONObject jsonA = tableDetails.getJSONObject(j);
								if (jsonA.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + ",";
								}

							}
							valueQuery = valueQuery + "'"
									+ request.getParameter("lblName[" + jsonArr.getString("ctrlId") + "]") + "',";
						} else if (jsonArr.getInt("ctrlTypeId") == 10) {
							if (!request.getParameter("addMoreElementData[" + jsonArr.getString("ctrlId") + "]")
									.equalsIgnoreCase("undefined")) {
								JSONArray addMoreJsonArray = new JSONArray(request
										.getParameter("addMoreElementData[" + jsonArr.getString("ctrlId") + "]"));
								for (int j = 0; j < addMoreJsonArray.length(); j++) {
									String StartQuery1 = "Insert into " + jsonArr.getString("ctrlTableName") + "(";
									String columnQuery1 = "";
									String valueQuery1 = "";
									String optionCol1 = "";
									JSONObject option1 = new JSONObject();
									JSONArray jsonArrayFirstArray = addMoreJsonArray.getJSONArray(j);
									JSONArray formDetailsArr = jsonArr.getJSONArray("addmoreDetails");
									for (int b = 0; b < formDetailsArr.length(); b++) {
										JSONObject jsonObject = formDetailsArr.getJSONObject(b);
										JSONArray addmoretablecolDetailsList = jsonObject
												.getJSONArray("addmoretablecolDetails");
										for (int d = 0; d < addmoretablecolDetailsList.length(); d++) {
											JSONObject jsonObje = addmoretablecolDetailsList.getJSONObject(d);
											for (int ii = 0; ii < jsonArrayFirstArray.length(); ii++) {
												JSONObject jsonObject1 = jsonArrayFirstArray.getJSONObject(ii);
												if (jsonObject.getString("ctrlId")
														.equals(jsonObject1.getString("ctrlId"))) {
													if (jsonObject1.getInt("ctrlTypeId") == 3) {
														optionCol1 = ",jsonOptTxtDetails";
														if (jsonObject.getString("ctrlId")
																.equals(jsonObject1.getString("ctrlId"))) {
															if (jsonObje.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ jsonObje.getString("ctrlTblColName") + ",";
															}
															valueQuery1 = valueQuery1 + "'"
																	+ jsonObject1.getString("ctrlValue") + "',";
															option1.put(jsonObje.getString("ctrlTblColName"),
																	jsonObject1.getString("ctrlValueText"));
															break;
														}
													} else if (jsonObject.getInt("ctrlTypeId") == 1) {
														JSONArray tableDetails = (JSONArray) jsonObject
																.get("addmoretablecolDetails");

														for (int k = 0; k < tableDetails.length(); k++) {
															JSONObject dataField = tableDetails.getJSONObject(k);
															if (dataField.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ dataField.getString("ctrlTblColName") + ",";
															}
														}
														valueQuery1 = valueQuery1 + "'"
																+ jsonObject.getString("ctrlLabelData") + "',";
													} else if (jsonObject.getInt("ctrlTypeId") == 5) {
														optionCol1 = ",jsonOptTxtDetails";
														if (jsonObject.getString("ctrlId")
																.equals(jsonObject1.getString("ctrlId"))) {
															if (jsonObje.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ jsonObje.getString("ctrlTblColName") + ",";
															}
															valueQuery1 = valueQuery1 + "'"
																	+ jsonObject1.getString("ctrlValue") + "',";
															option1.put(jsonObje.getString("ctrlTblColName"),
																	jsonObject1.getString("ctrlValueText"));
															break;
														}
													} else if (jsonObject.getInt("ctrlTypeId") == 6) {
														optionCol1 = ",jsonOptTxtDetails";
														if (jsonObject.getString("ctrlId")
																.equals(jsonObject1.getString("ctrlId"))) {
															if (jsonObje.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ jsonObje.getString("ctrlTblColName") + ",";
															}
															valueQuery1 = valueQuery1 + "'"
																	+ jsonObject1.getString("ctrlValue") + "',";
															option1.put(jsonObje.getString("ctrlTblColName"),
																	jsonObject1.getString("ctrlValueText"));
															break;
														}
													} else if (jsonObject.getInt("ctrlTypeId") == 7) {
														if (jsonObject.getString("ctrlId")
																.equals(jsonObject1.getString("ctrlId"))) {
															if (jsonObje.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ jsonObje.getString("ctrlTblColName") + ",";
															}
															JSONObject json3 = (JSONObject) jsonObject1
																	.get("uploadFile");

															valueQuery1 = valueQuery1 + "'"
																	+ json3.getString("fileName") + "',";
															break;
														}
													} else {
														if (jsonObje.getString("ctrlTblColName") != "") {
															columnQuery1 = columnQuery1
																	+ jsonObje.getString("ctrlTblColName") + ",";
														}
														valueQuery1 = valueQuery1 + "'"
																+ jsonObject1.getString("ctrlValue") + "',";
														break;
													}

												}

											}
										}

									}
									String finalQuery1;
									if (optionCol1 == "") {
										finalQuery1 = StartQuery1 + columnQuery1
												+ "intCreatedBy,dtmCreatedOn,intParentId,bitDeletedFlag" + optionCol1
												+ ")values(" + valueQuery1 + "1,'" + obj.format(res) + "',"
												+ intOnlineServiceId + ",0)";
									} else {

										finalQuery1 = StartQuery1 + columnQuery1
												+ "intCreatedBy,dtmCreatedOn,intParentId,bitDeletedFlag" + optionCol1
												+ ")values(" + valueQuery1 + "1,'" + obj.format(res) + "',"
												+ intOnlineServiceId + ",0,null)";
									}
									// //System.out.println("Final Query is=" + finalQuery1);
									KeyHolder keyHolder = new GeneratedKeyHolder();
									jdbcTemplate.update(new PreparedStatementCreator() {
										public PreparedStatement createPreparedStatement(Connection connection)
												throws SQLException {
											PreparedStatement ps = null;
											try {
												ps = connection.prepareStatement(finalQuery1, new String[] { "intId" });
											} catch (SQLException e) {
												logger.error(ExceptionUtils.getStackTrace(e));
											}
											return ps;
										}
									}, keyHolder);
									Integer intId1 = keyHolder.getKey().intValue();

									if (optionCol1 != "") {
										DYNJSONOptionTextDetailsData dynJSONSet = new DYNJSONOptionTextDetailsData();
										dynJSONSet.setIntId(intId1);
										dynJSONSet.setOnlineServiceId(intOnlineServiceId);
										dynJSONSet.setTableName(jsonArr.getString("ctrlTableName"));
										dynJSONSet.setJsonOptionTextDetails(option1.toString());
										dYNJSONOptionTextDetailsDataRepository.save(dynJSONSet);
									}

								}
							}
						} else {
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int j = 0; j < tableDetails.length(); j++) {
								JSONObject jsonA = tableDetails.getJSONObject(j);
								if (jsonA.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + ",";
								}

							}
							valueQuery = valueQuery + "'"
									+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]") + "',";

						}
					}
					String finalQuery;
					finalQuery = StartQuery + columnQuery
							+ "intCreatedBy,dtmCreatedOn,intOnlineServiceId,bitDeletedFlag,jsonOptTxtDetails)values("
							+ valueQuery + "1,'" + obj.format(res) + "'," + intOnlineServiceId + ",0,null)";
					KeyHolder keyHolder = new GeneratedKeyHolder();
					jdbcTemplate.update(new PreparedStatementCreator() {

						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(finalQuery, new String[] { "intId" });
							} catch (SQLException e) {
								logger.error(ExceptionUtils.getStackTrace(e));
							}
							return ps;
						}
					}, keyHolder);

					Integer intId1 = keyHolder.getKey().intValue();

					if (optionCol != "") {
						DYNJSONOptionTextDetailsData dynJSONSet = new DYNJSONOptionTextDetailsData();
						dynJSONSet.setIntId(intId1);
						dynJSONSet.setOnlineServiceId(intOnlineServiceId);
						dynJSONSet.setTableName(tableName);
						dynJSONSet.setJsonOptionTextDetails(option.toString());
						dYNJSONOptionTextDetailsDataRepository.save(dynJSONSet);
					}

				}
				if (intId != null) {
					String StartQuery = "UPDATE " + tableName.toUpperCase() + " set ";
					String columnQuery = "";
					String optionCol = "";
					JSONObject option = null;
					if (streetName != null) {
						option = new JSONObject(streetName);
					} else {
						option = new JSONObject();
					}
					String valueQuery = "STMUPDATEDON='" + obj.format(res) + "',INTUPDATEDBY=1" + " where INTID="
							+ intId;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonArr = jsonArray.getJSONObject(i);
						if (jsonArr.getInt("ctrlTypeId") == 1) {
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int k = 0; k < tableDetails.length(); k++) {
								JSONObject dataField = tableDetails.getJSONObject(k);
								if (dataField.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + dataField.getString("ctrlTblColName") + "='"
											+ jsonArr.getString("ctrlLabelData") + "',";
								}
							}
						} else if (jsonArr.getInt("ctrlTypeId") == 7) {
							String uploadStringData = request
									.getParameter("uploadedFiles[" + jsonArr.getString("ctrlId") + "]");
							JSONObject uploadData = new JSONObject(uploadStringData);
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int j = 0; j < tableDetails.length(); j++) {
								JSONObject jsonA = tableDetails.getJSONObject(j);
								columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + "='"
										+ uploadData.getString("fileName") + "',";

							}

						} else if (jsonArr.getInt("ctrlTypeId") == 3) {
							optionCol = ",jsonOptTxtDetails";
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int k = 0; k < tableDetails.length(); k++) {
								JSONObject dataField = tableDetails.getJSONObject(k);
								if (dataField.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + dataField.getString("ctrlTblColName") + "='"
											+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]")
											+ "',";
									option.put(dataField.getString("ctrlTblColName"),
											request.getParameter("ctrlValueText[" + jsonArr.getString("ctrlId") + "]"));
								}

							}

						} else if (jsonArr.getInt("ctrlTypeId") == 5) {
							optionCol = ",jsonOptTxtDetails";
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int k = 0; k < tableDetails.length(); k++) {
								JSONObject dataField = tableDetails.getJSONObject(k);
								if (dataField.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + dataField.getString("ctrlTblColName") + "='"
											+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]")
											+ "',";
									option.put(dataField.getString("ctrlTblColName"),
											request.getParameter("ctrlValueText[" + jsonArr.getString("ctrlId") + "]"));
								}

							}

						} else if (jsonArr.getInt("ctrlTypeId") == 6) {
							optionCol = ",jsonOptTxtDetails";
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int k = 0; k < tableDetails.length(); k++) {
								JSONObject dataField = tableDetails.getJSONObject(k);
								if (dataField.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + dataField.getString("ctrlTblColName") + "='"
											+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]")
											+ "',";
									option.put(dataField.getString("ctrlTblColName"),
											request.getParameter("ctrlValueText[" + jsonArr.getString("ctrlId") + "]"));
								}

							}
						} else if (jsonArr.getInt("ctrlTypeId") == 8) {
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int j = 0; j < tableDetails.length(); j++) {
								JSONObject jsonA = tableDetails.getJSONObject(j);
								if (jsonA.getString("ctrlTblColName") != "") {
									columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + "='"
											+ request.getParameter("lblName[" + jsonArr.getString("ctrlId") + "]")
											+ "',";
								}

							}


						} else if (jsonArr.getInt("ctrlTypeId") == 10) {
							String addmoreTableName = jsonArr.getString("ctrlTableName");
							String selectAddmoreData = "select * from " + addmoreTableName.toUpperCase()
									+ " where intParentId=" + intOnlineServiceId + " and bitDeletedFlag=0";
							List<Map<String, Object>> dataList = jdbcTemplate.queryForList(selectAddmoreData);
							if (dataList.size() != 0) {
								for (Map<String, Object> map : dataList) {
									JSONObject json7 = new JSONObject(map);
									String finalQuery1 = "update " + addmoreTableName
											+ " set bitDeletedFlag=1 where intId=" + json7.getInt("INTID");
									KeyHolder keyHolder = new GeneratedKeyHolder();
									jdbcTemplate.update(new PreparedStatementCreator() {
										public PreparedStatement createPreparedStatement(Connection connection)
												throws SQLException {
											PreparedStatement ps = null;
											try {
												ps = connection.prepareStatement(finalQuery1);
											} catch (SQLException e) {
												logger.error(ExceptionUtils.getStackTrace(e));
											}
											return ps;
										}
									}, keyHolder);
								}
							}
							if (!request.getParameter("addMoreElementData[" + jsonArr.getString("ctrlId") + "]")
									.equalsIgnoreCase("undefined")) {
								JSONArray addMoreJsonArray = new JSONArray(request
										.getParameter("addMoreElementData[" + jsonArr.getString("ctrlId") + "]"));
								for (int j = 0; j < addMoreJsonArray.length(); j++) {
									String StartQuery1 = "Insert into " + jsonArr.getString("ctrlTableName") + "(";
									String columnQuery1 = "";
									String valueQuery1 = "";
									String optionCol1 = "";
									JSONObject option1 = new JSONObject();
									JSONArray jsonArrayFirstArray = addMoreJsonArray.getJSONArray(j);
									JSONArray formDetailsArr = jsonArr.getJSONArray("addmoreDetails");
									for (int b = 0; b < formDetailsArr.length(); b++) {
										JSONObject jsonObject = formDetailsArr.getJSONObject(b);
										JSONArray addmoretablecolDetailsList = jsonObject
												.getJSONArray("addmoretablecolDetails");
										for (int d = 0; d < addmoretablecolDetailsList.length(); d++) {
											JSONObject jsonObje = addmoretablecolDetailsList.getJSONObject(d);
											for (int ii = 0; ii < jsonArrayFirstArray.length(); ii++) {
												JSONObject jsonObject1 = jsonArrayFirstArray.getJSONObject(ii);
												if (jsonObject.getString("ctrlId")
														.equals(jsonObject1.getString("ctrlId"))) {
													if (jsonObject1.getInt("ctrlTypeId") == 3) {
														optionCol1 = ",jsonOptTxtDetails";
														if (jsonObject.getString("ctrlId")
																.equals(jsonObject1.getString("ctrlId"))) {
															if (jsonObje.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ jsonObje.getString("ctrlTblColName") + ",";
															}
															valueQuery1 = valueQuery1 + "'"
																	+ jsonObject1.getString("ctrlValue") + "',";
															option1.put(jsonObje.getString("ctrlTblColName"),
																	jsonObject1.getString("ctrlValueText"));
															break;
														}
													} else if (jsonObject.getInt("ctrlTypeId") == 1) {
														if (jsonObject.getString("ctrlId")
																.equals(jsonObject1.getString("ctrlId"))) {
															if (jsonObje.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ jsonObje.getString("ctrlTblColName") + ",";
															}
															valueQuery1 = valueQuery1 + "'"
																	+ jsonObject1.getString("lblName") + "',";
															break;
														}
													} else if (jsonObject.getInt("ctrlTypeId") == 5) {
														optionCol1 = ",jsonOptTxtDetails";
														if (jsonObject.getString("ctrlId")
																.equals(jsonObject1.getString("ctrlId"))) {
															if (jsonObje.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ jsonObje.getString("ctrlTblColName") + ",";
															}
															valueQuery1 = valueQuery1 + "'"
																	+ jsonObject1.getString("ctrlValue") + "',";
															option1.put(jsonObje.getString("ctrlTblColName"),
																	jsonObject1.getString("ctrlValueText"));
															break;
														}
													} else if (jsonObject.getInt("ctrlTypeId") == 6) {
														optionCol1 = ",jsonOptTxtDetails";
														if (jsonObject.getString("ctrlId")
																.equals(jsonObject1.getString("ctrlId"))) {
															if (jsonObje.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ jsonObje.getString("ctrlTblColName") + ",";
															}
															valueQuery1 = valueQuery1 + "'"
																	+ jsonObject1.getString("ctrlValue") + "',";
															option1.put(jsonObje.getString("ctrlTblColName"),
																	jsonObject1.getString("ctrlValueText"));
															break;
														}
													} else if (jsonObject.getInt("ctrlTypeId") == 7) {
														if (jsonObject.getString("ctrlId")
																.equals(jsonObject1.getString("ctrlId"))) {
															if (jsonObje.getString("ctrlTblColName") != "") {
																columnQuery1 = columnQuery1
																		+ jsonObje.getString("ctrlTblColName") + ",";
															}
															JSONObject json3 = (JSONObject) jsonObject1
																	.get("uploadFile");

															valueQuery1 = valueQuery1 + "'"
																	+ json3.getString("fileName") + "',";
															break;
														}
													} else {
														if (jsonObje.getString("ctrlTblColName") != "") {
															columnQuery1 = columnQuery1
																	+ jsonObje.getString("ctrlTblColName") + ",";
														}
														valueQuery1 = valueQuery1 + "'"
																+ jsonObject1.getString("ctrlValue") + "',";
														break;
													}

												}

											}
										}

									}
									String finalQuery1;
									if (optionCol1 == "") {
										finalQuery1 = StartQuery1 + columnQuery1
												+ "intCreatedBy,dtmCreatedOn,intParentId,bitDeletedFlag" + ")values("
												+ valueQuery1 + "1,'" + obj.format(res) + "'," + intOnlineServiceId
												+ ",0)";
									} else {
										finalQuery1 = StartQuery1 + columnQuery1
												+ "intCreatedBy,dtmCreatedOn,intParentId,bitDeletedFlag" + ")values("
												+ valueQuery1 + "1,'" + obj.format(res) + "'," + intOnlineServiceId
												+ ",0)";
									}
									KeyHolder keyHolder = new GeneratedKeyHolder();
									jdbcTemplate.update(new PreparedStatementCreator() {
										public PreparedStatement createPreparedStatement(Connection connection)
												throws SQLException {
											PreparedStatement ps = null;
											try {
												ps = connection.prepareStatement(finalQuery1, new String[] { "intId" });
											} catch (SQLException e) {
												logger.error(ExceptionUtils.getStackTrace(e));
											}
											return ps;
										}
									}, keyHolder);
									Integer intId1 = keyHolder.getKey().intValue();
									if (optionCol1 != "") {
										DYNJSONOptionTextDetailsData dynJSONSet = new DYNJSONOptionTextDetailsData();

										dynJSONSet.setIntId(intId1);
										dynJSONSet.setOnlineServiceId(intOnlineServiceId);
										dynJSONSet.setTableName(jsonArr.getString("ctrlTableName"));
										dynJSONSet.setJsonOptionTextDetails(option1.toString());
										dYNJSONOptionTextDetailsDataRepository.save(dynJSONSet);
									}
								}
							}

						} else {
							JSONArray tableDetails = (JSONArray) jsonArr.get("tablecolDetails");
							for (int j = 0; j < tableDetails.length(); j++) {
								JSONObject jsonA = tableDetails.getJSONObject(j);
								columnQuery = columnQuery + jsonA.getString("ctrlTblColName") + "='"
										+ request.getParameter("ctrlValue[" + jsonArr.getString("ctrlId") + "]") + "',";
							}
						}

					}

					// Update the data
					if (columnQuery.equals("")) {

					} else {
						String finalQuery;
						if (optionCol == "") {
							finalQuery = StartQuery + columnQuery + valueQuery;
						} else {
							finalQuery = StartQuery + columnQuery + valueQuery;
						}
						KeyHolder keyHolder = new GeneratedKeyHolder();
						jdbcTemplate.update(new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection)
									throws SQLException {
								PreparedStatement ps = null;
								try {
									ps = connection.prepareStatement(finalQuery, new String[] { "INTID" });
								} catch (SQLException e) {
									logger.error(ExceptionUtils.getStackTrace(e));
								}
								return ps;
							}
						}, keyHolder);
						Integer intId1 = 0;
						try {
							intId1 = keyHolder.getKey().intValue();
						} catch (Exception e) {
							logger.error(ExceptionUtils.getStackTrace(e));
						}

						if (optionCol != "") {
							DYNJSONOptionTextDetailsData dynJSONData = dYNJSONOptionTextDetailsDataRepository
									.getDataByintIdTableNameServiceId(intId1, tableName, intOnlineServiceId);

							DYNJSONOptionTextDetailsData dynJSONSet = new DYNJSONOptionTextDetailsData();
							if (dynJSONData != null) {
								// //System.out.println("Data Present");
								dynJSONSet.setDetialsId(dynJSONData.getDetialsId());
							}
							dynJSONSet.setIntId(intId1);
							dynJSONSet.setOnlineServiceId(intOnlineServiceId);
							dynJSONSet.setTableName(tableName);
							dynJSONSet.setJsonOptionTextDetails(option.toString());
							dYNJSONOptionTextDetailsDataRepository.save(dynJSONSet);
						}
					}
				}
			}
		}

		if (valid == 0) {
			JSONObject jsonFinal = new JSONObject();
			jsonFinal.put("intOnlineServiceId", intOnlineServiceId);
			jsonFinal.put("validationMsg", msg);
			response.put("status", "200");
			response.put("result", jsonFinal);

		} else {
			// For validation error
			JSONObject jsonFinal = new JSONObject();
			jsonFinal.put("intOnlineServiceId", intOnlineServiceId);
			jsonFinal.put("validationMsg", msg);
			response.put("status", "400");
			response.put("result", jsonFinal);

		}
		return response;
	}

	@Override
	public JSONObject getPreviewDynamicForm(String data, WebRequest request) throws JSONException {
		JSONObject jsonObject = new JSONObject(data);
		String intOnlineServiceId = jsonObject.getString("intOnlineServiceId");
		String intProcessId = jsonObject.getString("intProcessId");
		List<Object[]> getFormData = process_repo.getDataByProcessId(Integer.parseInt(intProcessId));
		JSONArray sectionArray = null;
		Byte tinGridType = null;
		String vchProcessName = null;
		String vchTableName = null;
		for (Object[] obj : getFormData) {
			sectionArray = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) obj[0]));
			tinGridType = Byte.parseByte(obj[1].toString());
			vchProcessName = (String) obj[2];
			vchTableName = (String) obj[3];
		}
		String selectQuery = "select D.*,J.JSONOPTIONTEXTDETAILS from " + vchTableName.toUpperCase()
				+ " D left join DYN_JSOPTTEDTL_DATA J on D.intId = J.intid and J.tablename =  '" + vchTableName
				+ "' where bitDeletedFlag=0 and intOnlineServiceId=" + Integer.parseInt(intOnlineServiceId);
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(selectQuery);
		List<ResponseForPreview> responseForPreviewList = new ArrayList<ResponseForPreview>();

		JSONObject jsonObjSection = null;
		if (sectionArray.length() != 0) {
			for (int k = 0; k < sectionArray.length(); k++) {
				List<ArrFormWiseValue> arrForm = new ArrayList<ArrFormWiseValue>();
				List<Map<String, Object>> dataListSectionWise = null;
				jsonObjSection = sectionArray.getJSONObject(k);
				if (!jsonObjSection.getString("sectionTableName").equals("")) {
					String selectQuery1 = "select D.*,J.JSONOPTIONTEXTDETAILS from "
							+ jsonObjSection.getString("sectionTableName").toUpperCase()
							+ " D left join DYN_JSOPTTEDTL_DATA J on D.intId = J.intid and J.tablename =  '"
							+ jsonObjSection.getString("sectionTableName")
							+ "' where bitDeletedFlag=0 and intOnlineServiceId=" + Integer.parseInt(intOnlineServiceId);
					// //System.out.println(selectQuery1);
					dataListSectionWise = jdbcTemplate.queryForList(selectQuery1);
				} else {
					dataListSectionWise = dataList;
				}
				DynamicFormConfigurationApp dataForm = dfcar.getDataByProcessId(Integer.parseInt(intProcessId),
						Integer.parseInt(jsonObjSection.getString("sectionid")));
				JSONObject json = new JSONObject(dataForm);
				JSONArray json2 = new JSONArray(json.getString("formDetails"));
				ArrFormWiseValue arrFormVal = null;
				for (int i = 0; i < json2.length(); i++) {

					JSONObject jsonObj = json2.getJSONObject(i);
					arrFormVal = new ArrFormWiseValue();
					arrFormVal.setCtrlName(jsonObj.getString("ctrlLabel"));
					arrFormVal.setCtrlTypeId(jsonObj.getInt("ctrlTypeId"));
					arrFormVal.setCtrlHeadingType(jsonObj.getString("ctrlHeadingType"));
					if (jsonObj.getInt("ctrlTypeId") != 10) {
						JSONArray tableData = new JSONArray(jsonObj.get("tablecolDetails").toString());
						for (int j = 0; j < tableData.length(); j++) {
							JSONObject jsonTable = tableData.getJSONObject(j);
							for (Map<String, Object> map : dataListSectionWise) {
								for (Map.Entry<String, Object> entry : map.entrySet()) {
									if (jsonTable.getString("ctrlTblColName").equalsIgnoreCase(entry.getKey())) {
										JSONArray dependFieldJSON = new JSONArray(
												jsonObj.get("dependctrlDetails").toString());
										for (int l = 0; l < dependFieldJSON.length(); l++) {
											JSONObject depend = dependFieldJSON.getJSONObject(l); 
											if (jsonObj.getInt("ctrlTypeId") == 3) {
												JSONObject jsonopt = new JSONObject(map);
												JSONObject optionJson = new JSONObject(
														jsonopt.getString("JSONOPTIONTEXTDETAILS"));
												if (entry.getKey()
														.equalsIgnoreCase(jsonTable.getString("ctrlTblColName"))) {
													if (entry.getValue() != null) {
														arrFormVal.setCtrlValue(optionJson.getString(entry.getKey()));
														arrForm.add(arrFormVal);
													} else {
														arrFormVal.setCtrlValue(null);
														arrForm.add(arrFormVal);
													}

												}
											} else if (jsonObj.getInt("ctrlTypeId") == 5) {

												JSONObject jsonopt = new JSONObject(map);
												JSONObject optionJson = new JSONObject(
														jsonopt.getString("JSONOPTIONTEXTDETAILS"));
												if (entry.getKey()
														.equalsIgnoreCase(jsonTable.getString("ctrlTblColName"))) {
													if (entry.getValue() != null) {
														arrFormVal.setCtrlValue(optionJson.getString(entry.getKey()));
														arrForm.add(arrFormVal);
													} else {
														arrFormVal.setCtrlValue(null);
														arrForm.add(arrFormVal);
													}
												}
											} else if (jsonObj.getInt("ctrlTypeId") == 6) {
												JSONObject jsonopt = new JSONObject(map);
												JSONObject optionJson = new JSONObject(
														jsonopt.getString("JSONOPTIONTEXTDETAILS"));
												if (entry.getKey()
														.equalsIgnoreCase(jsonTable.getString("ctrlTblColName"))) {
													if (entry.getValue() != null) {
														arrFormVal.setCtrlValue(optionJson.getString(entry.getKey()));
														arrForm.add(arrFormVal);
													} else {
														arrFormVal.setCtrlValue(null);
														arrForm.add(arrFormVal);
													}
												}
											} else if (jsonObj.getInt("ctrlTypeId") == 7) {
												try {
													arrFormVal.setCtrlValue(
															"http://" + Inet4Address.getLocalHost().getHostAddress()
																	+ ":" + serverport + "/downloadForm/"
																	+ (String) entry.getValue().toString());
												} catch (UnknownHostException e) {
													logger.error(ExceptionUtils.getStackTrace(e));
												}
												arrForm.add(arrFormVal);
											} else if (jsonObj.getInt("ctrlTypeId") == 8) {
												if (entry.getValue() != null) {
													arrFormVal.setCtrlValue((String) entry.getValue().toString());
													arrForm.add(arrFormVal);
												} else {
													arrFormVal.setCtrlValue(null);
													arrForm.add(arrFormVal);
												}
											} else {
												if (entry.getValue() != null) {
													arrFormVal.setCtrlValue((String) entry.getValue().toString());
													arrForm.add(arrFormVal);
												} else {
													arrFormVal.setCtrlValue(null);
													arrForm.add(arrFormVal);
												}

											}
										}

									}
								}

							}

						}
					} else {
						String selectQueryforaddMore = "select D.*,J.JSONOPTIONTEXTDETAILS from "
								+ jsonObj.getString("ctrlTableName").toUpperCase()
								+ " D left join DYN_JSOPTTEDTL_DATA J on D.intId = J.intid and J.tablename =  '"
								+ jsonObj.getString("ctrlTableName") + "' where bitDeletedFlag=0 and intParentId="
								+ Integer.parseInt(intOnlineServiceId);

						List<Map<String, Object>> dataListforAddmore = jdbcTemplate.queryForList(selectQueryforaddMore);
						JSONArray addmoredetails = new JSONArray(jsonObj.get("addmoreDetails").toString());

						List<JSONArray> jsonArrayList = new ArrayList<JSONArray>();
						for (Map<String, Object> map : dataListforAddmore) {
							JSONArray jsonArray4 = null;
							List<AddMoreDetails> addMoreDetailsListField = new ArrayList<AddMoreDetails>();
							for (int u = 0; u < addmoredetails.length(); u++) {
								JSONObject addmoreFormDetails = addmoredetails.getJSONObject(u);

								AddMoreDetails addMoreDetailsList = new AddMoreDetails();
								addMoreDetailsList.setCtrlName(addmoreFormDetails.getString("ctrlLabel"));
								addMoreDetailsList.setCtrlTypeId(addmoreFormDetails.getString("ctrlTypeId"));

								JSONArray addMoretableData = new JSONArray(
										addmoreFormDetails.get("addmoretablecolDetails").toString());
								for (int j = 0; j < addMoretableData.length(); j++) {
									JSONObject addmoretable = addMoretableData.getJSONObject(j);
									for (Map.Entry<String, Object> entry : map.entrySet()) {
										if (addmoretable.getString("ctrlTblColName").equalsIgnoreCase(entry.getKey())) {
											if (addmoreFormDetails.getInt("ctrlTypeId") == 7) {
												try {
													addMoreDetailsList.setCtrlValue(
															"http://" + Inet4Address.getLocalHost().getHostAddress()
																	+ ":" + serverport + "/downloadForm/"
																	+ (String) entry.getValue().toString());
													addMoreDetailsListField.add(addMoreDetailsList);
												} catch (UnknownHostException e) {
													logger.error(ExceptionUtils.getStackTrace(e));
												}
											} else if (addmoreFormDetails.getInt("ctrlTypeId") == 3) {
												JSONObject jsonopt = new JSONObject(map);
												JSONObject optionJson = new JSONObject(
														jsonopt.getString("JSONOPTIONTEXTDETAILS"));
												if (entry.getKey()
														.equalsIgnoreCase(addmoretable.getString("ctrlTblColName"))) {
													if (entry.getValue() != null) {
														addMoreDetailsList
																.setCtrlValue(optionJson.getString(entry.getKey()));
														addMoreDetailsListField.add(addMoreDetailsList);
													} else {
														addMoreDetailsList.setCtrlValue(null);
														addMoreDetailsListField.add(addMoreDetailsList);
													}
												}
											} else if (addmoreFormDetails.getInt("ctrlTypeId") == 5) {
												JSONObject jsonopt = new JSONObject(map);
												JSONObject optionJson = new JSONObject(
														jsonopt.getString("JSONOPTIONTEXTDETAILS"));
												if (entry.getKey()
														.equalsIgnoreCase(addmoretable.getString("ctrlTblColName"))) {

													if (entry.getValue() != null) {
														addMoreDetailsList
																.setCtrlValue(optionJson.getString(entry.getKey()));
														addMoreDetailsListField.add(addMoreDetailsList);
													} else {
														addMoreDetailsList.setCtrlValue(null);
														addMoreDetailsListField.add(addMoreDetailsList);
													}
												}
											} else if (addmoreFormDetails.getInt("ctrlTypeId") == 6) {
												JSONObject jsonopt = new JSONObject(map);
												JSONObject optionJson = new JSONObject(
														jsonopt.getString("JSONOPTIONTEXTDETAILS"));
												if (entry.getKey()
														.equalsIgnoreCase(addmoretable.getString("ctrlTblColName"))) {
													if (entry.getValue() != null) {
														addMoreDetailsList
																.setCtrlValue(optionJson.getString(entry.getKey()));
														addMoreDetailsListField.add(addMoreDetailsList);
													} else {
														addMoreDetailsList.setCtrlValue(null);
														addMoreDetailsListField.add(addMoreDetailsList);
													}
												}
											} else {
												if (entry.getValue() != null) {
													addMoreDetailsList
															.setCtrlValue((String) entry.getValue().toString());
													addMoreDetailsListField.add(addMoreDetailsList);
												} else {
													addMoreDetailsList.setCtrlValue(null);
													addMoreDetailsListField.add(addMoreDetailsList);
												}

											}
										}

									}
								}
							}
							jsonArray4 = new JSONArray(addMoreDetailsListField);
							jsonArrayList.add(jsonArray4);
						}
						arrFormVal.setAddMoreDetails(jsonArrayList);
						arrForm.add(arrFormVal);
					}

				}

				ResponseForPreview respPreview = new ResponseForPreview();
				respPreview.setArrFormWiseValue(new JSONArray(arrForm));
				respPreview.setSecSlno(jsonObjSection.getInt("slno"));
				respPreview.setSectionId(jsonObjSection.getString("sectionid"));
				respPreview.setSectionName(jsonObjSection.getString("sectionName"));
				responseForPreviewList.add(respPreview);

			}
		} else {
			List<ArrFormWiseValue> arrForm = new ArrayList<ArrFormWiseValue>();
			DynamicFormConfigurationApp dataForm = dfcar.getDataByProcessId(Integer.parseInt(intProcessId));

			JSONObject json = new JSONObject(dataForm);
			JSONArray json2 = new JSONArray(json.getString("formDetails"));
			for (int i = 0; i < json2.length(); i++) {
				JSONObject jsonObj = json2.getJSONObject(i);
				ArrFormWiseValue arrFormVal = new ArrFormWiseValue();
				arrFormVal.setCtrlName(jsonObj.getString("ctrlLabel"));
				arrFormVal.setCtrlTypeId(jsonObj.getInt("ctrlTypeId"));
				if (jsonObj.getInt("ctrlTypeId") != 10) {
					JSONArray tableData = new JSONArray(jsonObj.get("tablecolDetails").toString());
					for (int j = 0; j < tableData.length(); j++) {
						JSONObject jsonTable = tableData.getJSONObject(j);
						for (Map<String, Object> map : dataList) {
							for (Map.Entry<String, Object> entry : map.entrySet()) {
								if (jsonTable.getString("ctrlTblColName").equalsIgnoreCase(entry.getKey())) {
									JSONArray dependFieldJSON = new JSONArray(
											jsonObj.get("dependctrlDetails").toString());
									for (int l = 0; l < dependFieldJSON.length(); l++) {
										JSONObject depend = dependFieldJSON.getJSONObject(l);
										if (depend.getBoolean("ctrlChkDepend") == true) {
											JSONArray jsonformDetails = new JSONArray(json.getString("formDetails"));
											for (int p = 0; p < jsonformDetails.length(); p++) {
												JSONObject jsonObjDepend = jsonformDetails.getJSONObject(p);
												if (jsonObjDepend.getString("ctrlId")
														.equalsIgnoreCase(depend.getString("ctrlSelDependParent"))) {
													JSONArray jsonTableDetails = new JSONArray(
															jsonObjDepend.get("tablecolDetails").toString());
													for (int s = 0; s < jsonTableDetails.length(); s++) {
														JSONObject getTableData = jsonTableDetails.getJSONObject(s);
														if (depend.getString("ctrlSelDependValue").equalsIgnoreCase(map
																.get(getTableData.get("ctrlTblColName")).toString())) {
															arrFormVal.setCtrlValue((String) entry.getValue());
															arrForm.add(arrFormVal);
														}
													}
												}
											}
										} else if (jsonObj.getInt("ctrlTypeId") == 3) {
											JSONObject jsonopt = new JSONObject(map);
											JSONObject optionJson = new JSONObject(
													jsonopt.getString("JSONOPTIONTEXTDETAILS"));
											if (entry.getKey()
													.equalsIgnoreCase(jsonTable.getString("ctrlTblColName"))) {
												if (entry.getValue() != null) {
													arrFormVal.setCtrlValue(optionJson.getString(entry.getKey()));
													arrForm.add(arrFormVal);
												} else {
													arrFormVal.setCtrlValue(null);
													arrForm.add(arrFormVal);
												}
											}
										} else if (jsonObj.getInt("ctrlTypeId") == 5) {
											JSONObject jsonopt = new JSONObject(map);
											JSONObject optionJson = new JSONObject(
													jsonopt.getString("JSONOPTIONTEXTDETAILS"));
											if (entry.getKey()
													.equalsIgnoreCase(jsonTable.getString("ctrlTblColName"))) {
												if (entry.getValue() != null) {
													arrFormVal.setCtrlValue(optionJson.getString(entry.getKey()));
													arrForm.add(arrFormVal);
												} else {
													arrFormVal.setCtrlValue(null);
													arrForm.add(arrFormVal);
												}
											}
										} else if (jsonObj.getInt("ctrlTypeId") == 6) {
											JSONObject jsonopt = new JSONObject(map);
											JSONObject optionJson = new JSONObject(
													jsonopt.getString("JSONOPTIONTEXTDETAILS"));
											if (entry.getKey()
													.equalsIgnoreCase(jsonTable.getString("ctrlTblColName"))) {
												if (entry.getValue() != null) {
													arrFormVal.setCtrlValue(optionJson.getString(entry.getKey()));
													arrForm.add(arrFormVal);
												} else {
													arrFormVal.setCtrlValue(null);
													arrForm.add(arrFormVal);
												}
											}
										} else if (jsonObj.getInt("ctrlTypeId") == 7) {
											try {
												arrFormVal.setCtrlValue(
														"http://" + Inet4Address.getLocalHost().getHostAddress() + ":"
																+ serverport + "/downloadForm/"
																+ (String) entry.getValue().toString());
											} catch (UnknownHostException e) {
												logger.error(ExceptionUtils.getStackTrace(e));
											}
											arrForm.add(arrFormVal);
										} else if (jsonObj.getInt("ctrlTypeId") == 8) {
											if (entry.getValue() != null) {
												arrFormVal.setCtrlValue((String) entry.getValue().toString());
												arrForm.add(arrFormVal);
											} else {
												arrFormVal.setCtrlValue(null);
												arrForm.add(arrFormVal);
											}
										} else {
											if (entry.getValue() != null) {
												arrFormVal.setCtrlValue((String) entry.getValue().toString());
												arrForm.add(arrFormVal);
											} else {
												arrFormVal.setCtrlValue(null);
												arrForm.add(arrFormVal);
											}

										}
									}
								}
							}

						}

					}
				} else {
					// //System.out.println("Here is add more");
					String selectQueryforaddMore = "select D.*,J.JSONOPTIONTEXTDETAILS from "
							+ jsonObj.getString("ctrlTableName").toUpperCase()
							+ " D left join DYN_JSOPTTEDTL_DATA J on D.intId = J.intid and J.tablename =  '"
							+ jsonObj.getString("ctrlTableName") + "' where bitDeletedFlag=0 and intParentId="
							+ Integer.parseInt(intOnlineServiceId);

					List<Map<String, Object>> dataListforAddmore = jdbcTemplate.queryForList(selectQueryforaddMore);
					JSONArray addmoredetails = new JSONArray(jsonObj.get("addmoreDetails").toString());

					List<JSONArray> jsonArrayList = new ArrayList<JSONArray>();
					for (Map<String, Object> map1 : dataListforAddmore) {
						JSONArray jsonArray4 = null;
						List<AddMoreDetails> addMoreDetailsListField = new ArrayList<AddMoreDetails>();
						for (int u = 0; u < addmoredetails.length(); u++) {
							JSONObject addmoreFormDetails = addmoredetails.getJSONObject(u);

							AddMoreDetails addMoreDetailsList = new AddMoreDetails();
							addMoreDetailsList.setCtrlName(addmoreFormDetails.getString("ctrlLabel"));
							addMoreDetailsList.setCtrlTypeId(addmoreFormDetails.getString("ctrlTypeId"));

							JSONArray addMoretableData = new JSONArray(
									addmoreFormDetails.get("addmoretablecolDetails").toString());
							for (int jj = 0; jj < addMoretableData.length(); jj++) {
								JSONObject addmoretable = addMoretableData.getJSONObject(jj);
								for (Map.Entry<String, Object> entry1 : map1.entrySet()) {
									if (addmoretable.getString("ctrlTblColName").equalsIgnoreCase(entry1.getKey())) {
										if (addmoreFormDetails.getInt("ctrlTypeId") == 7) {
											try {
												addMoreDetailsList.setCtrlValue(
														"http://" + Inet4Address.getLocalHost().getHostAddress() + ":"
																+ serverport + "/downloadForm/"
																+ (String) entry1.getValue().toString());
//												addMoreDetailsList.setCtrlValue(
//														"http://" + Inet4Address.getLocalHost().getHostAddress() +  
//														          "/bsky/downloadForm/"
//																+ (String) entry1.getValue().toString());
												addMoreDetailsListField.add(addMoreDetailsList);
											} catch (UnknownHostException e) {
												logger.error(ExceptionUtils.getStackTrace(e));
											}
										} else if (addmoreFormDetails.getInt("ctrlTypeId") == 3) {
											JSONObject jsonopt = new JSONObject(map1);
//											JSONObject optionJson = new JSONObject(
//													jsonopt.getString("jsonOptTxtDetails"));
											JSONObject optionJson = new JSONObject(
													jsonopt.getString("JSONOPTIONTEXTDETAILS"));
											if (entry1.getKey()
													.equalsIgnoreCase(addmoretable.getString("ctrlTblColName"))) {
												if (entry1.getValue() != null) {
													addMoreDetailsList
															.setCtrlValue(optionJson.getString(entry1.getKey()));
													addMoreDetailsListField.add(addMoreDetailsList);
												} else {
													addMoreDetailsList.setCtrlValue(null);
													addMoreDetailsListField.add(addMoreDetailsList);
												}
											}
										} else if (addmoreFormDetails.getInt("ctrlTypeId") == 5) {
											JSONObject jsonopt = new JSONObject(map1);
											JSONObject optionJson = new JSONObject(
													jsonopt.getString("JSONOPTIONTEXTDETAILS"));
											if (entry1.getKey()
													.equalsIgnoreCase(addmoretable.getString("ctrlTblColName"))) {
												if (entry1.getValue() != null) {
													addMoreDetailsList
															.setCtrlValue(optionJson.getString(entry1.getKey()));
													addMoreDetailsListField.add(addMoreDetailsList);
												} else {
													addMoreDetailsList.setCtrlValue(null);
													addMoreDetailsListField.add(addMoreDetailsList);
												}
											}
										} else if (addmoreFormDetails.getInt("ctrlTypeId") == 6) {
											JSONObject jsonopt = new JSONObject(map1);
											JSONObject optionJson = new JSONObject(
													jsonopt.getString("JSONOPTIONTEXTDETAILS"));
											if (entry1.getKey()
													.equalsIgnoreCase(addmoretable.getString("ctrlTblColName"))) {
												if (entry1.getValue() != null) {
													addMoreDetailsList
															.setCtrlValue(optionJson.getString(entry1.getKey()));
													addMoreDetailsListField.add(addMoreDetailsList);
												} else {
													addMoreDetailsList.setCtrlValue(null);
													addMoreDetailsListField.add(addMoreDetailsList);
												}
											}
										} else {
											if (entry1.getValue() != null) {
												addMoreDetailsList.setCtrlValue((String) entry1.getValue().toString());
												addMoreDetailsListField.add(addMoreDetailsList);
											} else {
												addMoreDetailsList.setCtrlValue(null);
												addMoreDetailsListField.add(addMoreDetailsList);
											}

										}
									}

								}
							}
						}
						jsonArray4 = new JSONArray(addMoreDetailsListField);
						jsonArrayList.add(jsonArray4);
					}
					arrFormVal.setAddMoreDetails(jsonArrayList);
					arrForm.add(arrFormVal);
				}
			}

			ResponseForPreview respPreview = new ResponseForPreview();
			respPreview.setArrFormWiseValue(new JSONArray(arrForm));
			respPreview.setSecSlno(0);
			respPreview.setSectionId("0");
			respPreview.setSectionName("");

			JSONObject jsonArrSec = new JSONObject(respPreview);
			JSONObject jsonsec = new JSONObject();
			jsonsec.put("sec_" + 0, jsonArrSec);
			JSONObject jsonObjResponse = new JSONObject();
			jsonObjResponse.put("arrSecFormDetails", jsonsec);
			jsonObjResponse.put("formName", vchProcessName);
			jsonObjResponse.put("tinGridType", tinGridType);

			JSONObject response = new JSONObject();
			response.put("result", jsonObjResponse);
			response.put("status", 200);
			return response;

		}
		JSONArray jsonArray = new JSONArray(responseForPreviewList);
		JSONObject finalJson = new JSONObject();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			finalJson.put("sec_" + jsonObj.getInt("secSlno") + "_" + jsonObj.getInt("sectionId"), jsonObj);
		}
		JSONObject jsonObjResponse = new JSONObject();
		jsonObjResponse.put("arrSecFormDetails", finalJson);
		jsonObjResponse.put("formName", vchProcessName);
		jsonObjResponse.put("tinGridType", tinGridType);

		JSONObject response = new JSONObject();
		response.put("result", jsonObjResponse);
		response.put("status", 200);
		return response;

	}

	@Override
	public JSONObject applyForProcess(String data) throws JSONException {
		JSONObject jsonObject = new JSONObject(data);
		String intOnlineServiceId = jsonObject.getString("intOnlineServiceId");
		String intProcessId = jsonObject.getString("intProcessId");
		Integer profileId = jsonObject.getInt("profileID");
		String tableName = process_repo.getTableName(Integer.parseInt(intProcessId));
		TOnlineServiceApproval tOnlineServiceApproval = new TOnlineServiceApproval();
		Integer stageNo = 1;
		Integer labelId = 0;
		String stateCode = tSetAuthorityRepository.getStateCode(Integer.parseInt(intOnlineServiceId));
		if (Integer.parseInt(stateCode) == 21) {
			labelId = 1;
		} else if (Integer.parseInt(stateCode) > 0) {
			labelId = 2;
		}
		TOnlineServiceApproval tApproval = null;
		// //System.out.println("Online=" + intOnlineServiceId);
		// labelId
		List<Object[]> tSetAuthorityList = tSetAuthorityRepository
				.getAuthorityDetailsByProcessIdAndStageNo(Integer.parseInt(intProcessId), stageNo, labelId);
		for (Object[] obj : tSetAuthorityList) {
			tOnlineServiceApproval.setIntPendingAt(obj[0].toString());
			tOnlineServiceApproval.setIntATAProcessId(Integer.parseInt(obj[1].toString()));
		}
		try {
			if (Integer.parseInt(intOnlineServiceId) != 0) {
				tApproval = tOnlineServiceApprovalRepository
						.findByOnlineServiceId(Integer.parseInt(intOnlineServiceId));
				if (tApproval.getTinStatus() == 3) {

					tOnlineServiceApproval.setIntOnlineServiceApprovalId(tApproval.getIntOnlineServiceApprovalId());
					tOnlineServiceApproval.setIntOnlineServiceId(tApproval.getIntOnlineServiceId());
					tOnlineServiceApproval.setIntProfileId(tApproval.getIntProfileId());
					tOnlineServiceApproval.setIntStageNo(1);
					tOnlineServiceApproval.setIntForwardTo(tApproval.getIntForwardTo());
					tOnlineServiceApproval.setIntSentFrom(tApproval.getIntSentFrom());
					Date res = new Date(System.currentTimeMillis());
					tOnlineServiceApproval.setDtmStatusDate(res);
					tOnlineServiceApproval.setTinStatus(0);
					tOnlineServiceApproval.setIntAssistantId(tApproval.getIntAssistantId());
					tOnlineServiceApproval.setTinQueryTo(tApproval.getTinQueryTo());
					tOnlineServiceApproval.setTinResubmitStatus(1);

					tOnlineServiceApproval.setStmCreatedOn(tApproval.getStmCreatedOn());

					tOnlineServiceApproval.setIntCreatedBy(tApproval.getIntCreatedBy());
					tOnlineServiceApproval.setBitDeletedFlag(tApproval.getBitDeletedFlag());
					tOnlineServiceApproval.setDtmUpdatedOn(res);
					tOnlineServiceApproval.setIntUpdatedBy(1);
					tOnlineServiceApproval.setIntProcessId(tApproval.getIntProcessId());
					tOnlineServiceApproval.setIntApproveDocIndexId(tApproval.getIntApproveDocIndexId());
					tOnlineServiceApproval.setTinDemandNoteGenStatus(tApproval.getTinDemandNoteGenStatus());
					tOnlineServiceApproval
							.setTinDemandNoteApplicableStatus(tApproval.getTinDemandNoteApplicableStatus());
					tOnlineServiceApprovalRepository.save(tOnlineServiceApproval);
				}
			}
		} catch (Exception e) {
			tOnlineServiceApproval.setIntOnlineServiceId(Integer.parseInt(intOnlineServiceId));
			tOnlineServiceApproval.setIntProfileId(profileId);
			tOnlineServiceApproval.setIntStageNo(1);
			tOnlineServiceApproval.setIntForwardTo(null);
			tOnlineServiceApproval.setIntSentFrom(null);
			tOnlineServiceApproval.setDtmStatusDate(null);
			tOnlineServiceApproval.setTinStatus(0);
			tOnlineServiceApproval.setIntAssistantId(null);
			tOnlineServiceApproval.setTinQueryTo(0);
			tOnlineServiceApproval.setTinResubmitStatus(0);
			Date res = new Date(System.currentTimeMillis());
			tOnlineServiceApproval.setStmCreatedOn(res);

			tOnlineServiceApproval.setIntCreatedBy(1);
			tOnlineServiceApproval.setBitDeletedFlag((byte) 0);
			tOnlineServiceApproval.setIntProcessId(Integer.parseInt(intProcessId));
			tOnlineServiceApproval.setIntApproveDocIndexId(0);
			tOnlineServiceApproval.setTinDemandNoteGenStatus(0);
			tOnlineServiceApproval.setTinDemandNoteApplicableStatus(0);
			TOnlineServiceApproval tOnline = tOnlineServiceApprovalRepository.save(tOnlineServiceApproval);
			if (tOnline.getIntOnlineServiceApprovalId() > 0) {
				String regdNumber = "HOSP/EMP/" + intOnlineServiceId;
				tOnlineServiceApplicationRepository.setRegdNumber(regdNumber, Integer.parseInt(intOnlineServiceId));
			}
		}
		
		JSONObject response = new JSONObject();
		response.put("result", "");
		response.put("status", 200);
		return response;
	}

	@Override
	public List<Object[]> getQueryDetails(String data) throws JSONException {
		JSONObject jsonObject = new JSONObject(data);
		String intOnlineServiceId = jsonObject.getString("intOnlineServiceId");
		String intProcessId = jsonObject.getString("intProcessId");
		List<Object[]> list = tOnlineServiceApprovalRepository.getQueryDetails(intOnlineServiceId, intProcessId);
		return list;
	}

}
