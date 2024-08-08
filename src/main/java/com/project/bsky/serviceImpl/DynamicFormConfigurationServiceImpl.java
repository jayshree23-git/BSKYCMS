package com.project.bsky.serviceImpl;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.project.bsky.config.dto.DynamicFormBuild;
import com.project.bsky.config.dto.GetFormModuleName;
import com.project.bsky.config.dto.TableColDetails;
import com.project.bsky.entity.DynamicFormConfiguration;
import com.project.bsky.entity.DynamicFormConfigurationTemp;
import com.project.bsky.entity.MProcessName;
import com.project.bsky.repository.DynamicFormConfigurationRepository;
import com.project.bsky.repository.DynamicFormConfigurationTempRepository;
import com.project.bsky.repository.ProcessRepository;
import com.project.bsky.service.DynamicFormConfigurationService;
import com.project.bsky.util.ConvertClobToJson;

@Service
public class DynamicFormConfigurationServiceImpl implements DynamicFormConfigurationService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ProcessRepository processRepository;

	@Autowired
	private DynamicFormConfigurationTempRepository dynamicFormConfigurationTempRepository;

	@Autowired
	private DynamicFormConfigurationRepository dynamicFormConfigurationRepository;

	@Override
	public JSONObject getFormAndModuleName(String formIdAndModuleId) throws JSONException {
		JSONObject json = new JSONObject(formIdAndModuleId);
		String moduleId = json.getString("moduleId");
		String processId = json.getString("processId");
		if (moduleId.equals("")) {
			moduleId = "0";
		}
		if (processId.equals("")) {
			processId = "0";
		}
		List<GetFormModuleName> getFormModuleNameList = new ArrayList<GetFormModuleName>();
		List<Object[]> dataList = processRepository.findByProcessId(Integer.parseInt(processId),
				Integer.parseInt(moduleId));
		for (Object[] obj : dataList) {
			GetFormModuleName getFormModuleName1 = new GetFormModuleName();
			getFormModuleName1.setIntProcessId(Integer.valueOf(obj[0].toString()));
			getFormModuleName1.setVchProcessName((String) (obj[1]));
			getFormModuleName1.setIntModuleId(Integer.valueOf(obj[2].toString()));
			getFormModuleName1.setVchModuleName((String) obj[3]);
			getFormModuleNameList.add(getFormModuleName1);
		}
		JSONObject response = new JSONObject();
		response.put("status", "200");
		response.put("result", getFormModuleNameList);
		return response;

	}

	@Override
	public JSONObject saveDynamicFormConfiguration(HashMap<String, String> dynamicFormConfiguration)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(dynamicFormConfiguration.get("formData"));
		Integer itemId = jsonObject.getInt("itemId");
		Integer sectionId = jsonObject.getInt("sectionId");
		DynamicFormConfigurationTemp dfctp = dynamicFormConfigurationTempRepository.getDataByItemIdAndSectionId(itemId,
				sectionId);
		JSONObject response = new JSONObject();
		Long millis = System.currentTimeMillis();
		if (dfctp != null) {
			DynamicFormConfigurationTemp dynamic = new DynamicFormConfigurationTemp();
			dynamic.setConfigurationId(dfctp.getConfigurationId());
			dynamic.setItemId(dfctp.getItemId());
			dynamic.setSectionId(dfctp.getSectionId());
			dynamic.setFormDetails(jsonObject.get("formDetails").toString());
			dynamic.setStatus(jsonObject.getInt("status"));
			dynamic.setCreatedOn(dfctp.getCreatedOn());
			dynamic.setUpdatedOn(new Date(millis));
			dynamic.setUpdatedBy(1);
			dynamic.setCreatedBy(dfctp.getCreatedBy());
			dynamic.setDeletedFlag(dfctp.getDeletedFlag());
			dynamic.setTinPublishStatus(dfctp.getTinPublishStatus());
			dynamic.setOtherStatus(dfctp.getOtherStatus());
			dynamic.setVchSectionWiseTableName(dfctp.getVchSectionWiseTableName());
			dynamicFormConfigurationTempRepository.save(dynamic);
			response.put("status", "202");
			response.put("msg", "Updated!!");
		} else {
			Clob vchSection = processRepository
					.getVchSectionNameByFormId(Integer.parseInt(jsonObject.getString("itemId")));
			JSONArray jsonArrayForSectionList = new JSONArray(ConvertClobToJson.convertClobToJSONString(vchSection));
			if (jsonArrayForSectionList.length() != 0) {
				for (int sec = 0; sec < jsonArrayForSectionList.length(); sec++) {
					JSONObject jsonObjectForSection = jsonArrayForSectionList.getJSONObject(sec);
					if (jsonObjectForSection.getString("sectionid").equals(sectionId.toString())) {
						DynamicFormConfigurationTemp dynamic = new DynamicFormConfigurationTemp();
						dynamic.setItemId(jsonObject.getInt("itemId"));
						dynamic.setSectionId(jsonObject.getInt("sectionId"));
						dynamic.setFormDetails(jsonObject.get("formDetails").toString());
						dynamic.setStatus(jsonObject.getInt("status"));
						dynamic.setCreatedOn(new Date(millis));
						dynamic.setCreatedBy(1);
						dynamic.setDeletedFlag(0);
						dynamic.setTinPublishStatus(0);
						dynamic.setOtherStatus("0");
						if (!jsonObjectForSection.getString("sectionTableName").equals("")) {
							dynamic.setVchSectionWiseTableName(jsonObjectForSection.getString("sectionTableName"));
						}
						dynamicFormConfigurationTempRepository.save(dynamic);
						response.put("status", "200");
						response.put("msg", "Record inserted successfully!!");
					}
				}
			} else {
				DynamicFormConfigurationTemp dynamic = new DynamicFormConfigurationTemp();
				dynamic.setItemId(jsonObject.getInt("itemId"));
				dynamic.setSectionId(jsonObject.getInt("sectionId"));
				dynamic.setFormDetails(jsonObject.get("formDetails").toString());
				dynamic.setStatus(jsonObject.getInt("status"));
				dynamic.setCreatedOn(new Date(millis));
				dynamic.setCreatedBy(1);
				dynamic.setDeletedFlag(0);
				dynamic.setTinPublishStatus(0);
				dynamic.setOtherStatus("0");
				dynamicFormConfigurationTempRepository.save(dynamic);
				response.put("status", "200");
				response.put("msg", "Record inserted successfully!!");
			}
		}
		return response;
	}

	public JSONObject getFormConfigurationDetails(String itemIdSecIdStatus) throws JSONException {
		JSONObject jsonObject = new JSONObject(itemIdSecIdStatus);
		String sectionId = null;
		String intProcessId = null;
		String finalsubmitStatus = null;
		JSONObject response = new JSONObject();
		if (jsonObject.get("sectionId").equals("")) {
			sectionId = "0";
		} else if (jsonObject.get("sectionId").equals("undefined")) {
			sectionId = "0";
		} else {
			sectionId = jsonObject.getString("sectionId");
		}

		if (jsonObject.get("itemId").equals("")) {
			intProcessId = "-1";
		} else if (jsonObject.get("itemId").equals("undefined")) {

			intProcessId = "-1";
		} else {
			intProcessId = jsonObject.getString("itemId");
		}

		if (jsonObject.has("finalsubmitStatus")) {

			Object fsubmitStatus = jsonObject.get("finalsubmitStatus");

			if (fsubmitStatus.equals("")) {
				finalsubmitStatus = "-1";
			} else {
				finalsubmitStatus = jsonObject.getString("finalsubmitStatus");
			}
		} else {
			finalsubmitStatus = "-1";
		}
		List<DynamicFormConfigurationTemp> tempDynamicCongList = dynamicFormConfigurationTempRepository
				.getDataByItemId(Integer.parseInt(intProcessId), Integer.parseInt(sectionId));
		List<DynamicFormBuild> dynamicFormBuildList = new ArrayList<DynamicFormBuild>();
		if (tempDynamicCongList.size() != 0) {
			List<Object[]> mProcessNameList = processRepository.getByProcessIdWithAndStatusFromTemp(
					Integer.parseInt(intProcessId), Integer.parseInt(sectionId), Integer.parseInt(finalsubmitStatus));
			for (Object[] obj : mProcessNameList) {
				DynamicFormBuild dfb = new DynamicFormBuild();
				dfb.setConfigurationId(Integer.parseInt(obj[0].toString()));
				dfb.setItemId(Integer.parseInt(obj[1].toString()));
				Integer section = Integer.parseInt(obj[2].toString());
				dfb.setSectionId(Integer.parseInt(obj[2].toString()));
				JSONArray formDetailsArray = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) obj[3]));
				dfb.setFormDetails(formDetailsArray);
				dfb.setStatus(Byte.parseByte(obj[4].toString()));
				dfb.setCreatedOn((java.util.Date) obj[5]);
				dfb.setVchProcessName((String) obj[6]);
				dfb.setTinFinalSubmitStatus(Byte.parseByte(obj[7].toString()));
				JSONArray jsonArray = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) obj[8]));

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					if (!sectionId.equals("-1")) {
						if (sectionId == jsonObj.getString("sectionid")) {
							dfb.setSectionName(jsonObj.getString("sectionName"));
						}
					} else {
						if (section == jsonObj.getInt("sectionid")) {
							dfb.setSectionName(jsonObj.getString("sectionName"));
						}
					}

				}
				dynamicFormBuildList.add(dfb);
			}
		} else {

			List<Object[]> mProcessNameList = processRepository.getByProcessIdWithAndStatusFromMain(
					Integer.parseInt(intProcessId), Integer.parseInt(sectionId), Integer.parseInt(finalsubmitStatus));

			for (Object[] obj : mProcessNameList) {
				DynamicFormBuild dfb = new DynamicFormBuild();
				dfb.setConfigurationId(Integer.parseInt(obj[0].toString()));
				dfb.setItemId(Integer.parseInt(obj[1].toString()));
				Integer section = Integer.parseInt(obj[2].toString());
				dfb.setSectionId(Integer.parseInt(obj[2].toString()));
				JSONArray formDetailsArray = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) obj[3]));
				dfb.setFormDetails(formDetailsArray);
				dfb.setStatus(Byte.parseByte(obj[4].toString()));
				dfb.setCreatedOn((java.util.Date) obj[5]);
				dfb.setVchProcessName((String) obj[6]);
				dfb.setTinFinalSubmitStatus(Byte.parseByte(obj[7].toString()));
				JSONArray jsonArray = new JSONArray(ConvertClobToJson.convertClobToJSONString((Clob) obj[8]));
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					if (sectionId != "-1") {
						if (sectionId == jsonObj.getString("sectionid")) {
							dfb.setSectionName(jsonObj.getString("sectionName"));
						}
					} else {
						if (section == jsonObj.getInt("sectionid")) {
							dfb.setSectionName(jsonObj.getString("sectionName"));
						}
					}

				}
				dynamicFormBuildList.add(dfb);
			}
		}
		response.put("status", "200");
		response.put("result", dynamicFormBuildList);
		return response;
	}

	@Override
	public JSONObject saveDataInFinalTable(String data) throws SQLException, Exception {
		JSONObject jsonObject = new JSONObject(data);
		Integer itemId = jsonObject.getInt("itemId");
		Integer finalStatus = jsonObject.getInt("finalsubmitStatus");
		Integer gridType = jsonObject.getInt("gridType");
		JSONObject response = new JSONObject();
		List<DynamicFormConfigurationTemp> formConfigList = dynamicFormConfigurationTempRepository
				.findAllByItemId(itemId);

		List<TableColDetails> tableColDetailsListForMain = new ArrayList<TableColDetails>();
		List<String> formDetailsList = new ArrayList<String>();
		for (DynamicFormConfigurationTemp dfc : formConfigList) {
			List<TableColDetails> tableColDetailsList = new ArrayList<TableColDetails>();
			JSONObject json = new JSONObject(dfc);
			String formDetails = json.getString("formDetails");
			JSONArray jsonArr = new JSONArray(formDetails);
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsona = jsonArr.getJSONObject(i);
				if (jsona.getInt("ctrlTypeId") == 10) {
					String addMoreTableName = jsona.getString("ctrlTableName");
					JSONArray jso = (JSONArray) jsona.get("addmoreDetails");
					List<TableColDetails> addMoreTableColDetailsList = new ArrayList<TableColDetails>();
					List<String> addMoreFormDetailsList = new ArrayList<String>();
					for (int j = 0; j < jso.length(); j++) {
						JSONObject jsonObj = jso.getJSONObject(j);
						JSONArray jsonAddMoreForm = (JSONArray) jsonObj.get("addmoretablecolDetails");
						for (int h = 0; h < jsonAddMoreForm.length(); h++) {
							JSONObject addMoreJsonData = jsonAddMoreForm.getJSONObject(h);
							TableColDetails tcd = new TableColDetails();
							tcd.setCtrlTblColLength(addMoreJsonData.getString("ctrlTblColLength"));
							tcd.setCtrlTblColName(addMoreJsonData.getString("ctrlTblColName"));
							addMoreFormDetailsList.add(addMoreJsonData.getString("ctrlTblColName"));
							if (addMoreJsonData.get("ctrlTblColDeafult").equals(0)) {
								tcd.setCtrlTblColDeafult(
										(String) addMoreJsonData.get("ctrlTblColDeafult").toString());
							} else {
								tcd.setCtrlTblColDeafult((String) addMoreJsonData.get("ctrlTblColDeafult"));
							}
							tcd.setCtrlTblColConstraints(addMoreJsonData.getString("ctrlTblColConstraints"));
							if (addMoreJsonData.get("ctrlTblColParentTbl").equals(0)) {
								tcd.setCtrlTblColParentTbl(
										(String) addMoreJsonData.get("ctrlTblColParentTbl").toString());
							} else {
								tcd.setCtrlTblColParentTbl((String) addMoreJsonData.get("ctrlTblColParentTbl"));
							}
							if (addMoreJsonData.get("ctrlTblColParentTblClmName").equals(0)) {
								tcd.setCtrlTblColParentTblClmName(
										addMoreJsonData.get("ctrlTblColParentTblClmName").toString());
							} else {
								tcd.setCtrlTblColParentTblClmName(
										addMoreJsonData.getString("ctrlTblColParentTblClmName"));
							}
							tcd.setCtrlTblColType(addMoreJsonData.getString("ctrlTblColType"));
							addMoreTableColDetailsList.add(tcd);
						}

					}
					List<String> tableName = new ArrayList<String>();
					tableName = dynamicFormConfigurationTempRepository.getAllTableName();
					Integer status = null;
					for (int z = 0; z < tableName.size(); z++) {
						if (addMoreTableName.equalsIgnoreCase(tableName.get(z))) {
							status = 1;
							break;
						} else {
							status = 0;
						}
					}
					if (status == 0) {
						String StartQuery = "Create table " + addMoreTableName
								+ "(intId NUMBER(10) NOT NULL primary key,";
						String endQuery = "";
						for (TableColDetails tcd : addMoreTableColDetailsList) {
							endQuery = endQuery + tcd.getCtrlTblColName() + " " + tcd.getCtrlTblColType() + "("
									+ tcd.getCtrlTblColLength() + ")" + tcd.getCtrlTblColDeafult() + ",";

						}
						String finalEndQuery = endQuery.substring(0, endQuery.length() - 1);
						String finalQuery = StartQuery + finalEndQuery
								+ ",intCreatedBy NUMBER(10),intParentId NUMBER(10),intUpdatedBy NUMBER(10),dtmCreatedOn TIMESTAMP,stmUpdatedOn TIMESTAMP,bitDeletedFlag number(3) default 0,jsonOptTxtDetails clob)";
						KeyHolder keyHolder = new GeneratedKeyHolder();
						////System.out.println("Final Query=" + finalQuery);
						jdbcTemplate.update(new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection)
									throws SQLException {
								PreparedStatement ps = null;
								try {
									ps = connection.prepareStatement(finalQuery, new String[] { "id" });
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return ps;
							}
						}, keyHolder);

						String sequenceForAI = "Create sequence " + addMoreTableName
								+ "_seq Start with 1 Increment by 1";
						////System.out.println("Sequence Create=" + sequenceForAI);
						KeyHolder keyHolderForAI = new GeneratedKeyHolder();
						jdbcTemplate.update(new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection)
									throws SQLException {
								PreparedStatement ps = null;
								try {
									ps = connection.prepareStatement(sequenceForAI, new String[] { "id" });
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return ps;
							}
						}, keyHolderForAI);

						String trigger = "CREATE OR REPLACE TRIGGER " + addMoreTableName + "_seq_tr"
								+ " BEFORE INSERT ON " + addMoreTableName
								+ " FOR EACH ROW WHEN (NEW.intId IS NULL) BEGIN SELECT " + addMoreTableName
								+ "_seq.NEXTVAL INTO :NEW.intId FROM DUAL;END;";
						////System.out.println("Trigger Create=" + trigger);
						jdbcTemplate.execute(trigger);

					} else {
						List<String> colListFromNewTab = dynamicFormConfigurationTempRepository
								.getColList(addMoreTableName.toUpperCase());
						List<String> matchedList = new ArrayList<String>();
						List<String> misMatchedList = new ArrayList<String>();
						for (int k = 0; k < addMoreFormDetailsList.size(); k++) {
							Integer count = 0;
							for (int j = 0; j < colListFromNewTab.size(); j++) {

								if (addMoreFormDetailsList.get(k).equalsIgnoreCase(colListFromNewTab.get(j))) {
									matchedList.add(addMoreFormDetailsList.get(k));
									count = 1;
									break;

								}
							}
							if (count == 0) {
								if (addMoreFormDetailsList.get(k) != "") {
									misMatchedList.add(addMoreFormDetailsList.get(k));
								}
							}
						}
						List<TableColDetails> matchedArray = new ArrayList<TableColDetails>();
						List<TableColDetails> misMatchedArray = new ArrayList<TableColDetails>();
						for (TableColDetails tablecol : addMoreTableColDetailsList) {
							Integer count = 0;
							for (int ii = 0; ii < matchedList.size(); ii++) {

								if (tablecol.getCtrlTblColName().equalsIgnoreCase(matchedList.get(ii))) {
									matchedArray.add(tablecol);
									count = 1;
									break;

								}
							}
							if (count == 0) {
								if (tablecol.getCtrlTblColName() != "") {
									misMatchedArray.add(tablecol);
								}
							}

						}
						if (misMatchedArray.size() != 0) {
							String startQuery = "ALTER TABLE " + addMoreTableName.toUpperCase() + " add ( ";
							String endQuery = "";
							for (TableColDetails array : misMatchedArray) {
								if (array.getCtrlTblColName() != "") {
									endQuery = endQuery + array.getCtrlTblColName() + " " + array.getCtrlTblColType()
											+ " (" + array.getCtrlTblColLength() + ")" + array.getCtrlTblColDeafult()
											+ ",";
								}
							}

							String finalEndQuery = endQuery.substring(0, endQuery.length() - 1);
							String finalQuery = startQuery + finalEndQuery + ")";
							////System.out.println("Final Query=" + finalQuery);

							KeyHolder keyHolder = new GeneratedKeyHolder();
							jdbcTemplate.update(new PreparedStatementCreator() {
								public PreparedStatement createPreparedStatement(Connection connection)
										throws SQLException {
									PreparedStatement ps = null;
									try {
										ps = connection.prepareStatement(finalQuery, new String[] { "id" });
									} catch (SQLException e) {
										e.printStackTrace();
									}
									return ps;
								}
							}, keyHolder);
						}
						if (matchedArray.size() != 0) {
							String startQuery = "ALTER TABLE " + addMoreTableName.toUpperCase() + " modify(";
							String endQuery = "";
							for (TableColDetails tableColDetails : matchedArray) {
								endQuery = endQuery + tableColDetails.getCtrlTblColName() + " "
										+ tableColDetails.getCtrlTblColType() + " ("
										+ tableColDetails.getCtrlTblColLength() + "),";
							}
							String finalEndQuery = "";
							if (endQuery.length() != 0) {
								finalEndQuery = endQuery.substring(0, endQuery.length() - 1);
							}
							String finalQuery = startQuery + finalEndQuery + ")";
							////System.out.println("Final Query=" + finalQuery);
							KeyHolder keyHolder = new GeneratedKeyHolder();
							jdbcTemplate.update(new PreparedStatementCreator() {
								public PreparedStatement createPreparedStatement(Connection connection)
										throws SQLException {
									PreparedStatement ps = null;
									try {
										ps = connection.prepareStatement(finalQuery, new String[] { "id" });
									} catch (SQLException e) {
										e.printStackTrace();
									}
									return ps;
								}
							}, keyHolder);
						}
					}
				} else {

					if (!json.has("vchSectionWiseTableName")) {
						JSONArray jso = (JSONArray) jsona.get("tablecolDetails");
						for (int j = 0; j < jso.length(); j++) {
							JSONObject jsondata = jso.getJSONObject(j);
							TableColDetails tcd = new TableColDetails();
							tcd.setCtrlTblColLength(jsondata.getString("ctrlTblColLength"));
							tcd.setCtrlTblColName(jsondata.getString("ctrlTblColName"));
							formDetailsList.add(jsondata.getString("ctrlTblColName"));
							if (jsondata.get("ctrlTblColDeafult").equals("0")) {
								tcd.setCtrlTblColDeafult( jsondata.get("ctrlTblColDeafult").toString());
							} else {
								tcd.setCtrlTblColDeafult((String) jsondata.get("ctrlTblColDeafult"));
							}
							tcd.setCtrlTblColConstraints(jsondata.getString("ctrlTblColConstraints"));
							if (jsondata.get("ctrlTblColParentTbl").equals(0)) {
								tcd.setCtrlTblColParentTbl((String) jsondata.get("ctrlTblColParentTbl").toString());
							} else {
								tcd.setCtrlTblColParentTbl((String) jsondata.get("ctrlTblColParentTbl"));
							}
							if (jsondata.get("ctrlTblColParentTblClmName").equals(0)) {
								tcd.setCtrlTblColParentTblClmName(
										jsondata.get("ctrlTblColParentTblClmName").toString());
							} else {
								tcd.setCtrlTblColParentTblClmName(jsondata.getString("ctrlTblColParentTblClmName"));
							}
							tcd.setCtrlTblColType(jsondata.getString("ctrlTblColType"));
							tableColDetailsListForMain.add(tcd);
						}
					} else {
						if (json.getString("vchSectionWiseTableName") != "") {
							JSONArray jso = (JSONArray) jsona.get("tablecolDetails");
							for (int j = 0; j < jso.length(); j++) {
								JSONObject jsondata = jso.getJSONObject(j);
								TableColDetails tcd = new TableColDetails();
								tcd.setCtrlTblColLength(jsondata.getString("ctrlTblColLength"));
								tcd.setCtrlTblColName(jsondata.getString("ctrlTblColName"));
								formDetailsList.add(jsondata.getString("ctrlTblColName"));
								if (jsondata.get("ctrlTblColDeafult").equals(0)) {
									tcd.setCtrlTblColDeafult((String) jsondata.get("ctrlTblColDeafult").toString());
								} else {
									tcd.setCtrlTblColDeafult((String) jsondata.get("ctrlTblColDeafult"));
								}
								tcd.setCtrlTblColConstraints(jsondata.getString("ctrlTblColConstraints"));
								if (jsondata.get("ctrlTblColParentTbl").equals(0)) {
									tcd.setCtrlTblColParentTbl((String) jsondata.get("ctrlTblColParentTbl").toString());
								} else {
									tcd.setCtrlTblColParentTbl((String) jsondata.get("ctrlTblColParentTbl"));
								}
								if (jsondata.get("ctrlTblColParentTblClmName").equals(0)) {
									tcd.setCtrlTblColParentTblClmName(
											jsondata.get("ctrlTblColParentTblClmName").toString());
								} else {
									tcd.setCtrlTblColParentTblClmName(jsondata.getString("ctrlTblColParentTblClmName"));
								}
								tcd.setCtrlTblColType(jsondata.getString("ctrlTblColType"));
								tableColDetailsList.add(tcd);
							}
						}

					}

				}

			}
			if (tableColDetailsList.size() != 0) {
				String formTable = json.getString("vchSectionWiseTableName");
				List<String> tableName = new ArrayList<String>();
				tableName = dynamicFormConfigurationTempRepository.getAllTableName();
				Integer status = null;
				for (int i = 0; i < tableName.size(); i++) {
					if (formTable.equalsIgnoreCase(tableName.get(i))) {
						status = 1;
						break;
					} else {
						status = 0;
					}
				}
				if (status == 0) {
					String StartQuery = "Create table " + formTable + "(intId NUMBER(10) NOT NULL primary key,";
					String endQuery = "";
					for (TableColDetails tcd : tableColDetailsList) {
						endQuery = endQuery + tcd.getCtrlTblColName() + " " + tcd.getCtrlTblColType() + "("
								+ tcd.getCtrlTblColLength() + ")" + tcd.getCtrlTblColDeafult() + ",";
					}
					String finalEndQuery = endQuery.substring(0, endQuery.length() - 1);
					String finalQuery = StartQuery + finalEndQuery
							+ ",intCreatedBy NUMBER(10),intOnlineServiceId NUMBER(10),intUpdatedBy NUMBER(10),dtmCreatedOn TIMESTAMP,stmUpdatedOn TIMESTAMP,bitDeletedFlag number(3) default 0,jsonOptTxtDetails clob)";
					////System.out.println(finalQuery);
					KeyHolder keyHolder = new GeneratedKeyHolder();
					jdbcTemplate.update(new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(finalQuery, new String[] { "id" });
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return ps;
						}
					}, keyHolder);

					String sequenceForAI = "Create sequence " + formTable + "_seq Start with 1 Increment by 1";
					////System.out.println("Sequence Create=" + sequenceForAI);
					KeyHolder keyHolderForAI = new GeneratedKeyHolder();
					jdbcTemplate.update(new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sequenceForAI, new String[] { "id" });
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return ps;
						}
					}, keyHolderForAI);

					String trigger = "CREATE OR REPLACE TRIGGER " + formTable + "_seq_tr" + " BEFORE INSERT ON "
							+ formTable + " FOR EACH ROW WHEN (NEW.intId IS NULL) BEGIN SELECT " + formTable
							+ "_seq.NEXTVAL INTO :NEW.intId FROM DUAL;END;";
					////System.out.println("Trigger Create=" + trigger);
					jdbcTemplate.execute(trigger);
				} else {
					////System.out.println("This is for edit mode");

					List<String> colListFromNewTab = dynamicFormConfigurationTempRepository
							.getColList(formTable.toUpperCase());
					List<String> matchedList = new ArrayList<String>();
					List<String> misMatchedList = new ArrayList<String>();
					for (int i = 0; i < formDetailsList.size(); i++) {
						Integer count = 0;
						for (int j = 0; j < colListFromNewTab.size(); j++) {

							if (formDetailsList.get(i).equalsIgnoreCase(colListFromNewTab.get(j))) {
								matchedList.add(formDetailsList.get(i));
								count = 1;
								break;

							}
						}
						if (count == 0) {
							if (formDetailsList.get(i) != "") {
								misMatchedList.add(formDetailsList.get(i));
							}
						}
					}
					List<TableColDetails> matchedArray = new ArrayList<TableColDetails>();
					List<TableColDetails> misMatchedArray = new ArrayList<TableColDetails>();
					for (TableColDetails tablecol : tableColDetailsList) {
						Integer count = 0;
						for (int i = 0; i < matchedList.size(); i++) {

							if (tablecol.getCtrlTblColName().equalsIgnoreCase(matchedList.get(i))) {
								matchedArray.add(tablecol);
								count = 1;
								break;

							}
						}
						if (count == 0) {
							if (tablecol.getCtrlTblColName() != "") {
								misMatchedArray.add(tablecol);
							}
						}

					}

					if (misMatchedArray.size() != 0) {
						String startQuery = "ALTER TABLE " + formTable.toUpperCase() + " add(";
						String endQuery = "";
						for (TableColDetails array : misMatchedArray) {
							if (array.getCtrlTblColName() != "") {
								endQuery = endQuery + array.getCtrlTblColName() + " " + array.getCtrlTblColType() + " ("
										+ array.getCtrlTblColLength() + ")" + array.getCtrlTblColDeafult() + ",";
							}
						}

						String finalEndQuery = endQuery.substring(0, endQuery.length() - 1);
						String finalQuery = startQuery + finalEndQuery + ")";
						////System.out.println("Final Query=" + finalQuery);
						KeyHolder keyHolder = new GeneratedKeyHolder();
						jdbcTemplate.update(new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection)
									throws SQLException {
								PreparedStatement ps = null;
								try {
									ps = connection.prepareStatement(finalQuery);
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return ps;
							}
						}, keyHolder);
					}

					if (matchedArray.size() != 0) {
						String startQuery = "ALTER TABLE " + formTable.toUpperCase() + " modify(";
						String endQuery = "";
						for (TableColDetails tableColDetails : matchedArray) {
							endQuery = endQuery + tableColDetails.getCtrlTblColName() + " "
									+ tableColDetails.getCtrlTblColType() + " (" + tableColDetails.getCtrlTblColLength()
									+ "),";
						}
						String finalEndQuery = "";
						if (endQuery.length() != 0) {
							finalEndQuery = endQuery.substring(0, endQuery.length() - 1);
						}
						String finalQuery = startQuery + finalEndQuery + ")";
						////System.out.println("Final Query=" + finalQuery);
						KeyHolder keyHolder = new GeneratedKeyHolder();
						jdbcTemplate.update(new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection)
									throws SQLException {
								PreparedStatement ps = null;
								try {
									ps = connection.prepareStatement(finalQuery, new String[] { "id" });
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return ps;
							}
						}, keyHolder);
					}
				}
			}
		}
		MProcessName dataByItemId = processRepository.getByProcessId(itemId);
		String formTable = dataByItemId.getVchTableName();
		List<String> tableName = new ArrayList<String>();
		tableName = dynamicFormConfigurationTempRepository.getAllTableName();
		Integer status = null;
		for (int i = 0; i < tableName.size(); i++) {
			if (formTable.equalsIgnoreCase(tableName.get(i))) {
				status = 1;
				break;
			} else {
				status = 0;
			}
		}
		if (status == 0) {
			String StartQuery = "Create table " + formTable + "(intId NUMBER(10) NOT NULL primary key,";
			String endQuery = "";
//			String constraintQuery = "";
			for (TableColDetails tcd : tableColDetailsListForMain) {

				// For Constraints specification

				/*
				 * // if (tcd.getCtrlTblColConstraints() != "") { // ////System.out.println( //
				 * tcd.getCtrlTblColName() + " " + tcd.getCtrlTblColType() + " " +
				 * tcd.getCtrlTblColLength() // + " " + tcd.getCtrlTblColDeafult() + " " +
				 * tcd.getCtrlTblColConstraints() + " " // + tcd.getCtrlTblColParentTbl() + " "
				 * + tcd.getCtrlTblColParentTblClmName()); // endQuery = endQuery +
				 * tcd.getCtrlTblColName() + " " + tcd.getCtrlTblColType() + "(" // +
				 * tcd.getCtrlTblColLength() + ")unsigned default " + tcd.getCtrlTblColDeafult()
				 * + ","; // constraintQuery = constraintQuery + tcd.getCtrlTblColConstraints()
				 * + "(" + tcd.getCtrlTblColName() // + ")" + " references " +
				 * tcd.getCtrlTblColParentTbl() + "(" // + tcd.getCtrlTblColParentTblClmName() +
				 * "),"; // // } else { // endQuery = endQuery + tcd.getCtrlTblColName() + " " +
				 * tcd.getCtrlTblColType() + "(" // + tcd.getCtrlTblColLength() + ")" +
				 * tcd.getCtrlTblColDeafult() + ","; // }
				 */
				endQuery = endQuery + tcd.getCtrlTblColName() + " " + tcd.getCtrlTblColType() + "("
						+ tcd.getCtrlTblColLength() + ")" + tcd.getCtrlTblColDeafult() + ",";

			}
			String finalEndQuery = "";
			if (endQuery != "") {
				finalEndQuery = endQuery.substring(0, endQuery.length() - 1);
			} else {
				StartQuery = StartQuery.substring(0, StartQuery.length() - 1);
			}

			/*
			 * // String consQuery = ""; // if (constraintQuery.length() != 0) { //
			 * consQuery = constraintQuery.substring(0, constraintQuery.length() - 1); // }
			 * // consQuery=
			 */
			/*
			 * // ////System.out.println("ConsQuery=" + consQuery); // String finalQuery =
			 * StartQuery + finalEndQuery // +
			 * "intCreatedBy INT(11),intOnlineServiceId INT(11),intUpdatedBy INT(11),dtmCreatedOn DATETIME,dtmUpdatedOn DATETIME,bitDeletedFlag  tinyint(1),"
			 * // + consQuery + ")";
			 */ String finalQuery = StartQuery + finalEndQuery
					+ ",intCreatedBy NUMBER(10),intOnlineServiceId NUMBER(10),intUpdatedBy NUMBER(10),dtmCreatedOn TIMESTAMP,stmUpdatedOn TIMESTAMP,bitDeletedFlag number(3) default 0,jsonOptTxtDetails clob)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			////System.out.println("Final Query=" + finalQuery);
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(finalQuery, new String[] { "id" });
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return ps;
				}
			}, keyHolder);
			String sequenceForAI = "Create sequence " + formTable + "_seq Start with 1 Increment by 1";
			////System.out.println("Sequence Create=" + sequenceForAI);
			KeyHolder keyHolderForAI = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sequenceForAI, new String[] { "id" });
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return ps;
				}
			}, keyHolderForAI);

			String trigger = "CREATE OR REPLACE TRIGGER " + formTable + "_seq_tr" + " BEFORE INSERT ON " + formTable
					+ " FOR EACH ROW WHEN (NEW.intId IS NULL) BEGIN SELECT " + formTable
					+ "_seq.NEXTVAL INTO :NEW.intId FROM DUAL;END;";
			////System.out.println("Trigger Create=" + trigger);
			jdbcTemplate.execute(trigger);
		}

		else {
			List<String> colListFromNewTab = dynamicFormConfigurationTempRepository.getColList(formTable.toUpperCase());
			List<String> matchedList = new ArrayList<String>();
			List<String> misMatchedList = new ArrayList<String>();
			for (int i = 0; i < formDetailsList.size(); i++) {
				Integer count = 0;
				for (int j = 0; j < colListFromNewTab.size(); j++) {

					if (formDetailsList.get(i).equalsIgnoreCase(colListFromNewTab.get(j))) {
						matchedList.add(formDetailsList.get(i));
						count = 1;
						break;

					}
				}
				if (count == 0) {
					if (formDetailsList.get(i) != "") {
						misMatchedList.add(formDetailsList.get(i));
					}
				}
			}
			List<TableColDetails> matchedArray = new ArrayList<TableColDetails>();
			List<TableColDetails> misMatchedArray = new ArrayList<TableColDetails>();
			for (TableColDetails tablecol : tableColDetailsListForMain) {
				Integer count = 0;
				for (int i = 0; i < matchedList.size(); i++) {

					if (tablecol.getCtrlTblColName().equalsIgnoreCase(matchedList.get(i))) {
						matchedArray.add(tablecol);
						count = 1;
						break;

					}
				}
				if (count == 0) {
					if (tablecol.getCtrlTblColName() != "") {
						misMatchedArray.add(tablecol);
					}
				}

			}
			/*
			 * // if(misMatchedArray!=null) { // for(TableColDetails array:misMatchedArray)
			 * { // if(array.getCtrlTblColConstraints()!="") { //
			 * endQuery=endQuery+" add column "+array.getCtrlTblColName()+" "+array.
			 * getCtrlTblColType()+" ("+array.getCtrlTblColLength()+")"+array.
			 * getCtrlTblColDeafult()+","; //
			 * constraintQuery=constraintQuery+" "+array.getCtrlTblColConstraints()+"(" +
			 * array.getCtrlTblColName()+ ")" + // " references " +
			 * array.getCtrlTblColParentTbl() + "("+ array.getCtrlTblColParentTblClmName() +
			 * "),"; // }else { //
			 * endQuery=endQuery+" add column "+array.getCtrlTblColName()+" "+array.
			 * getCtrlTblColType()+" ("+array.getCtrlTblColLength()+")"+array.
			 * getCtrlTblColDeafult()+","; // } // // } // }
			 */
			//

//			String constraintQuery = "";

			if (misMatchedArray.size() != 0) {
				String startQuery = "ALTER TABLE " + formTable.toUpperCase() + " add(";
				String endQuery = "";
				for (TableColDetails array : misMatchedArray) {
					if (array.getCtrlTblColName() != "") {
						endQuery = endQuery + array.getCtrlTblColName() + " " + array.getCtrlTblColType() + " ("
								+ array.getCtrlTblColLength() + ")" + array.getCtrlTblColDeafult() + ",";
					}
				}

				String finalEndQuery = endQuery.substring(0, endQuery.length() - 1);
				String finalQuery = startQuery + finalEndQuery + ")";
				////System.out.println("Final Query=" + finalQuery);
				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = null;
						try {
							ps = connection.prepareStatement(finalQuery, new String[] { "id" });
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return ps;
					}
				}, keyHolder);
			}

			if (matchedArray.size() != 0) {
				String startQuery = "ALTER TABLE " + formTable.toUpperCase() + " modify(";
				String endQuery = "";
				for (TableColDetails tableColDetails : matchedArray) {
					endQuery = endQuery + tableColDetails.getCtrlTblColName() + " "
							+ tableColDetails.getCtrlTblColType() + " (" + tableColDetails.getCtrlTblColLength() + "),";
				}
				String finalEndQuery = "";
				if (endQuery.length() != 0) {
					finalEndQuery = endQuery.substring(0, endQuery.length() - 1);
				}
				String finalQuery = startQuery + finalEndQuery + ")";
				////System.out.println("Final Query=" + finalQuery);
				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = null;
						try {
							ps = connection.prepareStatement(finalQuery, new String[] { "id" });
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return ps;
					}
				}, keyHolder);
			}
//			String finalConstraintQuery="";
//			if(constraintQuery!="") {
//				finalConstraintQuery=constraintQuery.substring(0, constraintQuery.length()-1);
//			}
		}
		List<DynamicFormConfiguration> dataInDB = dynamicFormConfigurationRepository
				.getDataBySectionIdAndItemId(itemId);
		if (dataInDB.size() != 0) {
//			for (DynamicFormConfigurationTemp dfct : formConfigList) {
				for (DynamicFormConfiguration dataForm : dataInDB) {
					dynamicFormConfigurationRepository.updateData(dataForm.getConfigurationId());
//					if (dataForm.getSectionId().equals(dfct.getSectionId())) {
//						Long millis = System.currentTimeMillis();
//						DynamicFormConfiguration dynamic = new DynamicFormConfiguration();
//						dynamic.setConfigurationId(dataForm.getConfigurationId());
//						dynamic.setItemId(dataForm.getItemId());
//						dynamic.setSectionId(dataForm.getSectionId());
//						dynamic.setFormDetails(dfct.getFormDetails().toString());
//						dynamic.setStatus(dataForm.getStatus());
//						dynamic.setCreatedOn(dataForm.getCreatedOn());
//						dynamic.setCreatedBy(dataForm.getCreatedBy());
//						dynamic.setUpdatedOn(new Date(millis));
//						dynamic.setUpdatedBy(1);
//						dynamic.setDeletedFlag(dataForm.getDeletedFlag());
//						dynamic.setTinPublishStatus(dataForm.getTinPublishStatus());
//						dynamic.setOtherStatus(dataForm.getOtherStatus());
//						dynamic.setVchSectionWiseTableName(dataForm.getVchSectionWiseTableName());
//						DynamicFormConfiguration dynamicForm=dynamicFormConfigurationRepository.save(dynamic);
//						if(dynamicForm.getConfigurationId()>0) {
//							dynamicFormConfigurationTempRepository.deleteById(dfct.getConfigurationId());
//						}
//						
//					}
				}
//			}
		} 
//		else {
			for (DynamicFormConfigurationTemp dfct : formConfigList) {

				Long millis = System.currentTimeMillis();
				DynamicFormConfiguration dynamic = new DynamicFormConfiguration();
				dynamic.setItemId(dfct.getItemId());
				dynamic.setSectionId(dfct.getSectionId());
				dynamic.setFormDetails(dfct.getFormDetails().toString());
				dynamic.setStatus(dfct.getStatus());
				dynamic.setCreatedOn(new Date(millis));
				dynamic.setCreatedBy(1);
				dynamic.setTinPublishStatus(0);
				dynamic.setDeletedFlag(0);
				dynamic.setOtherStatus("0");
				if (!Strings.isNullOrEmpty(dfct.getVchSectionWiseTableName())) {
					dynamic.setVchSectionWiseTableName(dfct.getVchSectionWiseTableName());
				}
				DynamicFormConfiguration dynamicForm=dynamicFormConfigurationRepository.save(dynamic);
				if(dynamicForm.getConfigurationId()>0) {
					dynamicFormConfigurationTempRepository.deleteById(dfct.getConfigurationId());
				}
				
			}
//		}
		processRepository.setGridAndFinalStatus(finalStatus, gridType, itemId);
		response.put("status", "200");
		response.put("msg", "Record Saved Successfully!!");
		return response;
	}

}
