package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.DuplicateCheck;
//import com.project.bsky.bean.EmpanelmentBasicInfo;
import com.project.bsky.bean.Response;
//import com.project.bsky.model.EmpanellmenthospitalbasicinfoModel;
import com.project.bsky.model.MedicalExpertiseModel;
import com.project.bsky.model.TypeOfExpertiseModel;

/**
 * @author jayshree.moharana
 *
 */
public interface EmpanelmentMasterService {

	Response savemedicalexpertisedata(MedicalExpertiseModel medicalexpertisemodel);

	List<MedicalExpertiseModel> getmedicalexpertiseData();

	MedicalExpertiseModel getmedicalexpertiseDataById(Long userid);

	Response updateMedicalexpertise(MedicalExpertiseModel medicalexpertisemodel);

	List<MedicalExpertiseModel> getmedicalexpname();

	Integer saveTypeofExpertise(long medicalexpid, String typeofexpertise, String createdby);

	List<TypeOfExpertiseModel> getExpertisetypeData();

	Integer delete(long typeofexpertiseid);

	TypeOfExpertiseModel getbyid(long typeofexpertiseid);

	Integer update(long medicalexpid, String typeofexpertisename, String updateby, long typeofexpertiseid, Integer status);

	List<Map<String, Object>> getEmpanelmentMasterDetails(String flag, String queryParam);

	List<Map<String, Object>> getBankIFSCDetails(String bankName, String districtName);

	Map<String, Object> checkduplicate(DuplicateCheck duplicateCheck);


}
