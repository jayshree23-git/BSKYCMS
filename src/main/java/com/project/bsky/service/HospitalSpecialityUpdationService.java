/**
 * 
 */
package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.HospitalCivilInfrastructure;
import com.project.bsky.bean.HospitalSpecialistBean;
import com.project.bsky.bean.HospitalSpecialistListBean;
import com.project.bsky.bean.QcApprovalHospitalSpecialitybean;
import com.project.bsky.bean.Response;

/**
 * @author rajendra.sahoo
 *
 */
public interface HospitalSpecialityUpdationService {

	List<HospitalBean> gethospitalinfo(String state, String dist);

	List<HospitalSpecialistListBean> getpackagelistbyhospitalid(Long hospitalId, Long userid);

	Response savepecialistconfig(HospitalSpecialistBean hospitalSpecialistBean);

	Response savecivilinfraconfig(HospitalCivilInfrastructure civilInfrastructure);

	Map<String, Object> getEmpanelmentDetails(Integer hospitalId);

	List<HospitalBean> gethospitalDetails(String hospitalCode);

	String getHospitalEmpList(String state, String dist, String hospitalId, Long userid);

	String getCivilInfraDetailsById(Integer civilInfraId) throws Exception;

	String getSpecialityPackages(String packageCode, String hospitalCode) throws Exception;

	Response updatePackageSpecility(HospitalSpecialistBean hospitalSpecialistBean);

	List<Object> getSerachDataHospitalSpecialityApprovalList(String statecodeval, String districtcodeval,
			String userId);

	List<Object> getsearchdataofhospitallspecdetailslist(String hospitalid);

	List<Object> getsearchdataofhospitallspecdetailstpendingcase(String hospitalid);

	Response SavegQcapprovalofhospitalspeciality(QcApprovalHospitalSpecialitybean qcApprovalHospitalSpecialitybean)
			throws Exception;

	List<Object> specialityapprovelist(String statecode, String distcode, String hospitalcode, Integer type,
			Long userid);

	Map<String, Object> getschemepackagelistbyhospitalid(String hospitalCode, Integer schemeid) throws Exception;

	Map<String, Object> updateschemepackage(HospitalSpecialistBean hospitalSpecialistBean) throws Exception;

	Map<String, Object> getschemehospitalmappingrpt(String state, String dist, String hospitalCode, Integer schemeid) throws Exception;
}
