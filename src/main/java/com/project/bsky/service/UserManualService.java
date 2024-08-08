package com.project.bsky.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.HospitalDoctorprofile;
import com.project.bsky.bean.PrimaryLinkBean;
import com.project.bsky.bean.Response;

public interface UserManualService {

	List<Object> getlistof();

	List<PrimaryLinkBean> getprimarylinkname(Integer groupid);

	Response saveUsermanuallreport(String description, MultipartFile selectedFile, Long primarylinkid,
			String primarylinname, Long grouptype, String grouptypename, Long userid, Long actionflag,
			Long user_manual_id) throws Exception;

	List<Object> getallviewlist(Long userid);

	void downLoadFile(String fileName, Long type, HttpServletResponse response);

	List<Object> getlinklistdata(Long userid);

	List<Object> getSearchdata(Long primarylinkid);

	List<Object> gethospitaldatabystate(String hospitalcode);

	List<Object> getSpecialitydetails(String hospitalcode);

	Response getSubmitdata(HospitalDoctorprofile requestBean);

	List<Object> getdetailshospitaldoctorprifile(String statecode, String districtcode, String hospitalcode,
			String hospitalCode2, Long userid);

	List<Object> gethospitladoctordetailsprofile(Long profileid);

	List<Object> getEditDoctorprofiledata(Long profilid);

	String getclaimmultipledoctortreatedlist();

	List<Object> getDoctorTaggedHospitalName(String regno);

	List<Object> getDoctortreatedhospitalDetails(String regnonumber);

}
