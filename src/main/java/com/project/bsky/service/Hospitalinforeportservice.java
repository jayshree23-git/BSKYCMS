/**
 * 
 */
package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.HospitalDetailBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.TreatingSubmitBean;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.HospitalBackdateConfigLog;
import com.project.bsky.model.HospitalInformation;

/**
 * @author rajendra.sahoo
 *
 */
public interface Hospitalinforeportservice {

	List<HospitalBean> gethospitalinfo();

	List<HospitalBean> getfilterhospitalinfo(String stateid, String distid, Integer issna, Integer isdc);

	Response updatebackdateconfig(Long userid, String addmission, String discharge, String hospital);

	List<HospitalBackdateConfigLog> getallhospitallogdata();

	List<HospitalBean> getallhospitalbackdatelogdata();

	List<HospitalInformation> getmouexplist();

	List<HospitalInformation> gethcexplist();

	List<HospitalBean> getincentive(String state, String dist);

	Map<String, Object> getincentivedetails(String state, String dist, Integer catagory);

	AuthRequest generateotp(Long userid);

	AuthRequest validateotphosp(Long accessid, String otp);

	Map<String, Object> hospitallistforotpconfigure(String state, String dist, Long userid, Long otpreq);

	Response submithospitallistforotpconfigure(List<HospitalDetailBean> list);

	Map<String, Object> getlistTreatingdoctorConfiguration(String state, String dist, String type, Long userid);

	Response SubmitgetTreatingdoctorconfigurationlist(TreatingSubmitBean hospbean);

	Map<String, Object> getlatestlogdetails();

	Map<String, Object> hospitallistforloginotpconfigure(String state, String dist, Long userid, Long otpreq);

	Response submithospitallistforloginotpconfigure(List<HospitalDetailBean> hospdetailbean);

}
