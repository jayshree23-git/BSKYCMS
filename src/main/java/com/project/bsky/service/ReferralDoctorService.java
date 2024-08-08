/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.ReferralDoctorBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalTypeMaster;
import com.project.bsky.model.ReferalDoctor;
import com.project.bsky.model.ReferralHospital;
import com.project.bsky.model.ReferralResonMst;

/**
 * @author rajendra.sahoo
 *
 */
public interface ReferralDoctorService {

	Response savereferaldoctor(ReferalDoctor bean);

	List<ReferalDoctor> getreferaldoctor();

	Response updatereferaldoctor(ReferalDoctor bean);

	Response saverefdocConfiguration(ReferralDoctorBean referralDoctorBean);

	String getdoctortaglist();

	Response savereferralreson(ReferralResonMst bean);

	List<ReferralResonMst> getallreferralreson();

	Response updatereferralreson(ReferralResonMst bean);

	Response savereferalhospital(ReferralHospital bean);

	List<ReferralHospital> getreferalhospitallist();

	List<HospitalTypeMaster> getreferralhospitaltype();

	ReferralHospital getreferralhospitalbyid(Long hospitalid);

	Response updatereferalhospital(ReferralHospital bean);

	List<ReferralHospital> getrefHospitalbyDistrictId(String distid);

	List<ReferralHospital> getdoctortaglistbydoctorid(Long userid);

	List<ReferralHospital> getrefHospitalbyDistrictIdblockid(String distid, String block);

	void refdocConfigurationlog(Long cpdId, Integer updated);

	Response updaterefdocConfiguration(ReferralDoctorBean referralDoctorBean);

	List<HospitalTypeMaster> getallhosptype();

	Response saverefhospitaltype(HospitalTypeMaster hospitalTypeMaster);

	Response updaterefhospitaltype(HospitalTypeMaster hospitalTypeMaster);

	List<ReferralHospital> getrefHospitalbyDistrictIdhospitaltype(String distid, Integer hosptypeid);

}
