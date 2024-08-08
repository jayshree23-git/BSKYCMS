package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.HospitalProcedurePackageBean;
import com.project.bsky.bean.HospitalProcedureTagging;
import com.project.bsky.bean.HospitalSpecialistListBean;
import com.project.bsky.bean.PackageTaggingReportBean;
import com.project.bsky.bean.Response;

public interface HospitalSpecialityReportService {

	List<HospitalSpecialistListBean> gethospitalinfo(Long userid, Long actioncode, String state, String dist,
			String hospital);

	List<HospitalProcedureTagging> procedureTaggingDetails(HospitalProcedurePackageBean packageBean) throws Exception;

	List<Map<String, Object>> getPackageHeaderCode();

	Response submitTaggedProcedure(HospitalProcedurePackageBean packageBean) throws Exception;

	List<PackageTaggingReportBean> packageTaggingReport(String state, String dist, String hospital, Long type);

}
