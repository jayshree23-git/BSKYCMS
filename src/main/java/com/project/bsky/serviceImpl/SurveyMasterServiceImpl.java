/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Surveygroupmapping;
import com.project.bsky.bean.mappingsurveygroupbean;
import com.project.bsky.model.QuestionMaster;
import com.project.bsky.model.QuestionMasterLog;
import com.project.bsky.model.SurveyGroupMapping;
import com.project.bsky.model.SurveyGroupmappingLog;
import com.project.bsky.model.SurveyMaster;
import com.project.bsky.model.SurveyMasterLog;
import com.project.bsky.model.SurveyQuestionMapping;
import com.project.bsky.model.SurveyQuestionMappingLog;
import com.project.bsky.repository.QuestionMasterLogRepository;
import com.project.bsky.repository.QuestionMasterRepository;
import com.project.bsky.repository.SurveyGroupMappingRepository;
import com.project.bsky.repository.SurveyGroupmappingLogRepository;
import com.project.bsky.repository.SurveyMasterLogRepository;
import com.project.bsky.repository.SurveyMasterRepository;
import com.project.bsky.repository.SurveyQuestionMappingLogRepository;
import com.project.bsky.repository.SurveyQuestionMappingRepository;
import com.project.bsky.service.SurveyMasterService;

/**
 * @author santanu.barad
 *
 */
@Service
public class SurveyMasterServiceImpl implements SurveyMasterService {

	@Autowired
	private SurveyMasterRepository surveymasterrepository;

	@Autowired
	private QuestionMasterRepository quertionmasterrepository;

	@Autowired
	private SurveyGroupMappingRepository surveygroupmappingrepo;

	@Autowired
	private SurveyQuestionMappingRepository surveyquestionmappingrepo;

	@Autowired
	private QuestionMasterLogRepository quertionmasterlogrepository;

	@Autowired
	private SurveyMasterLogRepository surveymasterlogrepository;

	@Autowired
	private SurveyGroupmappingLogRepository surveygroupmappinglogrepository;

	@Autowired
	private SurveyQuestionMappingLogRepository surveyquestionmappinglogrepository;

	Date currentDate = Calendar.getInstance().getTime();

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

	@Override
	public Response saveSurveyMaster(SurveyMaster surveymaster) throws Exception {
		SurveyMaster returnValue = null;
		Response response = new Response();
		try {
			Integer count = surveymasterrepository.checkduplicate(surveymaster.getSurveyName());
			if (count == 0) {
				surveymaster.setEffectiveFrom(sdf.parse(surveymaster.getSfromdate()));
				surveymaster.setEffectiveTo(sdf.parse(surveymaster.getStodate()));
				surveymaster.setCreatedOn(currentDate);
				surveymaster.setStatusFlag(0);
				returnValue = surveymasterrepository.save(surveymaster);
				if (returnValue != null) {
					response.setStatus("200");
					response.setMessage("Survey Details Saved Successfully.");
				} else {
					response.setStatus("400");
					response.setMessage("Some Error Happen.");
				}
			} else {
				response.setStatus("400");
				response.setMessage("SurveyName Already Exist!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public Response savequestionmaster(QuestionMaster questionmaster) throws Exception {
		QuestionMaster returnValue = null;
		Response response = new Response();
		try {
			questionmaster.setCreatedOn(currentDate);
			questionmaster.setStatusFlag(0);
			returnValue = quertionmasterrepository.save(questionmaster);
			if (returnValue != null) {
				response.setStatus("200");
				response.setMessage("Question Saved Successfully.");
			} else {
				response.setStatus("400");
				response.setMessage("Some Error Happen.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public List<QuestionMaster> getallquestionmst() {
		return quertionmasterrepository.findAllByOrderByQuestionIdDesc();
	}

	@Override
	public Response updatequestionmaster(QuestionMaster questionmaster) throws Exception {
		Response response = new Response();
		try {
			QuestionMaster returnValue = quertionmasterrepository.findById(questionmaster.getQuestionId()).get();
			QuestionMasterLog questionlog = new QuestionMasterLog();
			questionlog.setQuestionId(returnValue.getQuestionId());
			questionlog.setQuestionName(returnValue.getQuestionName());
			questionlog.setQuestionType(returnValue.getQuestionType());
			questionlog.setMandotoryRemark(returnValue.getMandotoryRemark());
			questionlog.setCreatedBy(questionmaster.getUpdatedBy());
			questionlog.setCreatedOn(currentDate);
			questionlog.setUpdatedBy(returnValue.getUpdatedBy());
			questionlog.setUpdatedOn(returnValue.getUpdatedOn());
			questionlog.setStatusFlag(returnValue.getStatusFlag());
			quertionmasterlogrepository.save(questionlog);
			questionmaster.setCreatedBy(returnValue.getCreatedBy());
			questionmaster.setCreatedOn(returnValue.getCreatedOn());
			questionmaster.setUpdatedOn(currentDate);
			quertionmasterrepository.save(questionmaster);
			response.setStatus("200");
			response.setMessage("Question Updated Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public List<SurveyMaster> getsurveymstlist() throws Exception {
		List<SurveyMaster> surveylist = new ArrayList<>();
		try {
			Date currentDate = sdf.parse(sdf.format(new Date()));
			surveylist = surveymasterrepository.findAlldesc();
			for (SurveyMaster survey : surveylist) {
				survey.setSfromdate(sdf.format(survey.getEffectiveFrom()));
				survey.setStodate(sdf.format(survey.getEffectiveTo()));
				survey.setScreatedOn(sdf.format(survey.getCreatedOn()));
				int diff = currentDate.compareTo(survey.getEffectiveTo());
				survey.setEndstatus(diff == 1 ? 1 : 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return surveylist;
	}

	@Override
	public Response updatesurveymst(SurveyMaster surveymaster) throws Exception {
		Response response = new Response();
		try {
			Integer count = surveymasterrepository.checkduplicate(surveymaster.getSurveyName());
			if (count == 0) {
				SurveyMaster returnValue = surveymasterrepository.findById(surveymaster.getSurveyId()).get();
				updateSurveyMasterlog(returnValue, surveymaster.getUpdatedBy());
				surveymaster.setEffectiveFrom(sdf.parse(surveymaster.getSfromdate()));
				surveymaster.setEffectiveTo(sdf.parse(surveymaster.getStodate()));
				surveymaster.setCreatedBy(returnValue.getCreatedBy());
				surveymaster.setCreatedOn(returnValue.getCreatedOn());
				surveymaster.setUpdatedOn(currentDate);
				surveymasterrepository.save(surveymaster);
				response.setStatus("200");
				response.setMessage("Survey Details Updated Successfully.");
			} else {
				SurveyMaster returnValue = surveymasterrepository.findByname(surveymaster.getSurveyName());
				if (returnValue.getSurveyId().equals(surveymaster.getSurveyId())) {
					updateSurveyMasterlog(returnValue, surveymaster.getUpdatedBy());
					surveymaster.setEffectiveFrom(sdf.parse(surveymaster.getSfromdate()));
					surveymaster.setEffectiveTo(sdf.parse(surveymaster.getStodate()));
					surveymaster.setCreatedBy(returnValue.getCreatedBy());
					surveymaster.setCreatedOn(returnValue.getCreatedOn());
					surveymaster.setUpdatedOn(currentDate);
					surveymasterrepository.save(surveymaster);
					response.setStatus("200");
					response.setMessage("Survey Details Updated Successfully.");
				} else {
					response.setStatus("400");
					response.setMessage("SurveyName Already Exist!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return response;
	}

	public void updateSurveyMasterlog(SurveyMaster returnValue, Long updatedby) {
		try {
			SurveyMasterLog surveymasterlog = new SurveyMasterLog();
			surveymasterlog.setSurveyId(returnValue.getSurveyId());
			surveymasterlog.setSurveyName(returnValue.getSurveyName());
			surveymasterlog.setEffectiveFrom(returnValue.getEffectiveFrom());
			surveymasterlog.setEffectiveTo(returnValue.getEffectiveTo());
			surveymasterlog.setCreatedBy(updatedby);
			surveymasterlog.setCreatedOn(currentDate);
			surveymasterlog.setUpdatedBy(returnValue.getUpdatedBy());
			surveymasterlog.setUpdatedOn(returnValue.getUpdatedOn());
			surveymasterlog.setStatusFlag(returnValue.getStatusFlag());
			surveymasterlogrepository.save(surveymasterlog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<SurveyMaster> getactivesurveylist(Integer val) throws Exception {
		List<SurveyMaster> surveylist = new ArrayList<>();
		try {
			if (val != null) {
				if (val == 1) {
					List<Object[]> objlist = surveymasterrepository.getactivevalidsurveylist();
					for (Object[] obj : objlist) {
						SurveyMaster sm = new SurveyMaster();
						BigDecimal n = (BigDecimal) obj[0];
						sm.setSurveyId(n.longValue());
						sm.setSurveyName((String) obj[1]);
						surveylist.add(sm);
					}
				} else {
					surveylist = surveymasterrepository.getactivesurveylist();
				}
			} else {
				surveylist = surveymasterrepository.getactivesurveylist();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return surveylist;
	}

	@Override
	public Map<String, Object> getgrouplistbysurveyid(Long surveyid) throws Exception {
		List<Object> grouplist = new ArrayList<>();
		Map<String, Object> data = new HashMap<>();
		Boolean checkall = true;
		try {
			List<Object[]> objlist = surveymasterrepository.getgrouplistbysurveyid(surveyid);
			for (Object[] obj : objlist) {
				Map<String, Object> map = new HashMap<>();
				map.put("typeId", obj[0]);
				map.put("typeName", obj[1]);
				Number n = (Number) obj[2];
				map.put("status", n.intValue());
				if (n.intValue() == 1) {
					checkall = false;
				}
				grouplist.add(map);
			}
			data.put("data", grouplist);
			data.put("checkall", checkall);
			data.put("status", 200);
			data.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return data;
	}

	@Override
	public Response savegroupmapping(Surveygroupmapping surveymaster) throws Exception {
		Response response = new Response();
		List<SurveyGroupMapping> dataList = new ArrayList<>();
		try {
			for (mappingsurveygroupbean survey : surveymaster.getSelectlist()) {
				SurveyGroupMapping mappingdata = surveygroupmappingrepo
						.getdatabysurveyidandgroupid(surveymaster.getSurveyid(), survey.getId());
				if (mappingdata != null) {
					updateSurveyGroupMapping(mappingdata, surveymaster.getCreatedby());
					mappingdata.setStatusFlag(survey.getStatus());
					mappingdata.setUpdatedBy(surveymaster.getCreatedby());
					mappingdata.setUpdatedOn(currentDate);
					dataList.add(mappingdata);
				} else {
					if (survey.getStatus() == 0) {
						mappingdata = new SurveyGroupMapping();
						mappingdata.setStatusFlag(0);
						mappingdata.setCreatedBy(surveymaster.getCreatedby());
						mappingdata.setCreatedOn(currentDate);
						mappingdata.setSurveyId(surveymaster.getSurveyid());
						mappingdata.setGroupId(survey.getId());
						dataList.add(mappingdata);
					}
				}
			}
			surveygroupmappingrepo.saveAll(dataList);
			response.setStatus("200");
			response.setMessage("Data Mapped Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return response;
	}

	public void updateSurveyGroupMapping(SurveyGroupMapping mappingdata, Long createdby) {
		try {
			SurveyGroupmappingLog log = new SurveyGroupmappingLog();
			log.setGroupmappingid(mappingdata.getGroupmappingid());
			log.setSurveyId(mappingdata.getSurveyId());
			log.setGroupId(mappingdata.getGroupId());
			log.setCreatedBy(createdby);
			log.setCreatedOn(currentDate);
			log.setUpdatedBy(mappingdata.getUpdatedBy());
			log.setUpdatedOn(mappingdata.getUpdatedOn());
			log.setStatusFlag(mappingdata.getStatusFlag());
			surveygroupmappinglogrepository.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> getallsurveygroupmappinglist(Long surveyid) throws Exception {
		List<Object> list = new ArrayList<>();
		Map<String, Object> data = new HashMap<>();
		try {
			List<Object[]> objlist = surveygroupmappingrepo.getallsurveygroupmappinglist(surveyid);
			for (Object[] obj : objlist) {
				Map<String, Object> map = new HashMap<>();
				map.put("surveyName", obj[0]);
				map.put("groupName", obj[1]);
				map.put("createdBy", obj[2]);
				map.put("createdOn", obj[3]);
				list.add(map);
			}
			data.put("data", list);
			data.put("status", 200);
			data.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return data;
	}

	@Override
	public Map<String, Object> getquestionlistbysurveyid(Long surveyid) throws Exception {
		List<Object> questionlist = new ArrayList<>();
		Map<String, Object> data = new HashMap<>();
		Boolean checkall = true;
		try {
			List<Object[]> objlist = surveymasterrepository.getquestionlistbysurveyid(surveyid);
			for (Object[] obj : objlist) {
				Map<String, Object> map = new HashMap<>();
				map.put("qustionId", obj[0]);
				map.put("question", obj[1]);
				Number n = (Number) obj[2];
				map.put("status", n.intValue());
				if (n.intValue() == 1) {
					checkall = false;
				}
				questionlist.add(map);
			}
			data.put("data", questionlist);
			data.put("checkall", checkall);
			data.put("status", 200);
			data.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return data;
	}

	@Override
	public Response savequestionmapping(Surveygroupmapping questionmapping) throws Exception {
		Response response = new Response();
		List<SurveyQuestionMapping> dataList = new ArrayList<>();
		try {
			SurveyMaster surveymast = surveymasterrepository.findById(questionmapping.getSurveyid()).get();
			int diff = currentDate.compareTo(surveymast.getEffectiveFrom());
			if (diff >= 0) {
				response.setStatus("400");
				response.setMessage("This Form is not Valid !!");
				return response;
			}
			for (mappingsurveygroupbean survey : questionmapping.getSelectlist()) {
				SurveyQuestionMapping mappingdata = surveyquestionmappingrepo
						.getdatabysurveyidandquerionid(questionmapping.getSurveyid(), survey.getId());
				if (mappingdata != null) {
					updateSurveyQuestionMapping(mappingdata, questionmapping.getCreatedby());
					mappingdata.setStatusFlag(survey.getStatus());
					mappingdata.setUpdatedBy(questionmapping.getCreatedby());
					mappingdata.setUpdatedOn(currentDate);
					dataList.add(mappingdata);
				} else {
					if (survey.getStatus() == 0) {
						mappingdata = new SurveyQuestionMapping();
						mappingdata.setStatusFlag(0);
						mappingdata.setCreatedBy(questionmapping.getCreatedby());
						mappingdata.setCreatedOn(currentDate);
						mappingdata.setSurveyId(questionmapping.getSurveyid());
						mappingdata.setQuestionId(survey.getId());
						dataList.add(mappingdata);
					}
				}
			}
			surveyquestionmappingrepo.saveAll(dataList);
			response.setStatus("200");
			response.setMessage("Data Mapped Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return response;
	}

	public void updateSurveyQuestionMapping(SurveyQuestionMapping mappingdata, Long createdby) {
		try {
			SurveyQuestionMappingLog log = new SurveyQuestionMappingLog();
			log.setQuestionmappingid(mappingdata.getQuestionmappingid());
			log.setSurveyId(mappingdata.getSurveyId());
			log.setQuestionId(mappingdata.getQuestionId());
			log.setCreatedBy(createdby);
			log.setCreatedOn(currentDate);
			log.setUpdatedBy(mappingdata.getUpdatedBy());
			log.setUpdatedOn(mappingdata.getUpdatedOn());
			log.setStatusFlag(mappingdata.getStatusFlag());
			surveyquestionmappinglogrepository.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> getallsurveyquestionmappinglist(Long surveyid) throws Exception {
		List<Object> list = new ArrayList<>();
		Map<String, Object> data = new HashMap<>();
		try {
			List<Object[]> objlist = surveyquestionmappingrepo.getallsurveyquestionmappinglist(surveyid);
			for (Object[] obj : objlist) {
				Map<String, Object> map = new HashMap<>();
				map.put("surveyName", obj[0]);
				map.put("question", obj[1]);
				map.put("createdBy", obj[2]);
				map.put("createdOn", obj[3]);
				list.add(map);
			}
			data.put("data", list);
			data.put("status", 200);
			data.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return data;
	}

}
