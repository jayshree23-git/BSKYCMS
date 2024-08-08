/**
 * 
 */
package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Surveygroupmapping;
import com.project.bsky.model.QuestionMaster;
import com.project.bsky.model.SurveyMaster;

/**
 * @author santanu.barad
 *
 */
public interface SurveyMasterService {

	Response savequestionmaster(QuestionMaster questionmaster) throws Exception;

	List<QuestionMaster> getallquestionmst();

	Response updatequestionmaster(QuestionMaster questionmaster) throws Exception;
	
	Response saveSurveyMaster(SurveyMaster master) throws Exception;

	List<SurveyMaster> getsurveymstlist() throws Exception;

	Response updatesurveymst(SurveyMaster surveymaster) throws Exception;

	List<SurveyMaster> getactivesurveylist(Integer val) throws Exception;

	Map<String, Object> getgrouplistbysurveyid(Long surveyid) throws Exception;

	Response savegroupmapping(Surveygroupmapping surveymaster) throws Exception;

	Map<String, Object> getallsurveygroupmappinglist(Long surveyid) throws Exception;

	Map<String, Object> getquestionlistbysurveyid(Long surveyid) throws Exception;

	Response savequestionmapping(Surveygroupmapping questionmapping) throws Exception;

	Map<String, Object> getallsurveyquestionmappinglist(Long surveyid) throws Exception;
}
