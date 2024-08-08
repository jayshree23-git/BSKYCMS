/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UnBundlingPackage;
import com.project.bsky.model.UnBundlingPackageLog;
import com.project.bsky.repository.UnBundlingPackageLogRepository;
import com.project.bsky.repository.UnBundlingPackageRepository;
import com.project.bsky.service.UnBundlingPackageService;

/**
 * @author rajendra.sahoo DT- 06-11-2023
 */
@Service
public class UnBundlingPackageServiceImpl implements UnBundlingPackageService {

	@Autowired
	private Logger logger;

	@Autowired
	private UnBundlingPackageRepository unBundlingPackageRepository;

	@Autowired
	private UnBundlingPackageLogRepository unBundlingPackagelogRepository;

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

	@Override
	public Response SubmitdunamicConfiguration(UnBundlingPackage unBundlingPackage) {
		Response response = new Response();

		try {
			String specode = unBundlingPackage.getSpecialitycode();
			if (specode != "") {
				unBundlingPackage.setSpecialitycode(specode.substring(0, specode.length() - 1));
			} else {
				response.setStatus("400");
				response.setMessage("Please Select Speciality Code");
				return response;
			}
			String packagecode = unBundlingPackage.getPackagecode();
			if (packagecode != "") {
				unBundlingPackage.setPackagecode(packagecode.substring(0, packagecode.length() - 1));
			} else {
				response.setStatus("400");
				response.setMessage("Please Select Package Code");
				return response;
			}
			unBundlingPackage.setStatus(0);
			unBundlingPackage.setCreatedOn(Calendar.getInstance().getTime());
			unBundlingPackageRepository.save(unBundlingPackage);
			response.setStatus("200");
			response.setMessage("Record Submitted Sucessfully");
		} catch (Exception e) {
			logger.error("Error in SubmitdunamicConfiguration" + e);
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	@Override
	public List<UnBundlingPackage> getdynamicconfigurationlist() {
		List<UnBundlingPackage> list = new ArrayList<UnBundlingPackage>();
		try {
			list = unBundlingPackageRepository.getall();
			for (UnBundlingPackage x : list) {
				x.setScreatedOn(sdf.format(x.getCreatedOn()));
				x.setPackagecode(x.getPackagecode() != null ? x.getPackagecode() : "N/A");
			}
		} catch (Exception e) {
			logger.error("Error in getdynamicconfigurationlist" + e);
		}
		return list;
	}

	@Override
	public UnBundlingPackage getunBundlingPackagebyid(Long slno) {
		UnBundlingPackage unBundlingPackage = new UnBundlingPackage();
		try {
			unBundlingPackage = unBundlingPackageRepository.findById(slno).get();
		} catch (Exception e) {
			logger.error("Error in getunBundlingPackageconfigurationlist" + e);
		}
		return unBundlingPackage;
	}

	@Override
	public Response updatedunamicConfiguration(UnBundlingPackage unBundlingPackage) {

		Response response = new Response();
		try {
			UnBundlingPackage unBundling = unBundlingPackageRepository.findById(unBundlingPackage.getUnboundlingid())
					.get();

			UnBundlingPackageLog logtable = new UnBundlingPackageLog();
			logtable.setUnboundlingid(unBundling.getUnboundlingid());
			logtable.setPackagecode(unBundling.getPackagecode());
			logtable.setPackagename(unBundling.getPackagename());
			logtable.setCreatedBy(unBundling.getCreatedBy());
			logtable.setCreatedOn(unBundling.getCreatedOn());
			logtable.setUpdatedBy(unBundling.getUpdatedBy());
			logtable.setUpdatedOn(unBundling.getUpdatedOn());
			logtable.setStatus(unBundling.getStatus());
			unBundlingPackagelogRepository.save(logtable);

			String specode = unBundlingPackage.getSpecialitycode();
			if (specode != "") {
				unBundlingPackage.setSpecialitycode(specode.substring(0, specode.length() - 1));
			} else {
				response.setStatus("400");
				response.setMessage("Please Select Speciality Code");
				return response;
			}
			String packagecode = unBundlingPackage.getPackagecode();
			if (packagecode != "") {
				unBundlingPackage.setPackagecode(packagecode.substring(0, packagecode.length() - 1));
			} else {
				response.setStatus("400");
				response.setMessage("Please Select Package Code");
				return response;
			}
			unBundlingPackage.setUpdatedOn(Calendar.getInstance().getTime());
			unBundlingPackageRepository.save(unBundlingPackage);
			response.setStatus("200");
			response.setMessage("Record Updated Sucessfully");
		} catch (Exception e) {
			logger.error("Error in updatedunamicConfiguration" + e);
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

}
