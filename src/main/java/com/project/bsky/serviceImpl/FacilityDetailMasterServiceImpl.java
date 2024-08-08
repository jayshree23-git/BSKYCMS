/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.FacilityDetailBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.FacilityDetails;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.FacilityDetailMasterRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.FacilityDetailMasterService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class FacilityDetailMasterServiceImpl implements FacilityDetailMasterService {

	@Autowired
	private FacilityDetailMasterRepository facilityDetailMasterRepo;

	@Autowired
	private UserDetailsRepository userDetailsRepo;

	@Override
	public Response saveFaciltyData(FacilityDetailBean facilityDetailBean) {
		System.out.println(facilityDetailBean);
		Response response = new Response();
		try {
			FacilityDetails facilityDetail = new FacilityDetails();
			Integer countfacility = facilityDetailMasterRepo.checkFacility(facilityDetailBean.getFacilityName());
			System.out.println(countfacility);
			if (countfacility == 0) {

				Optional<UserDetails> optional = userDetailsRepo
						.findById(Long.parseLong(facilityDetailBean.getCreatedBy()));
				facilityDetail.setFacilityName(facilityDetailBean.getFacilityName());
				Calendar calendar = Calendar.getInstance();
				facilityDetail.setCreatedOn((calendar.getTime()));
				facilityDetail.setCreatedBy(optional.isPresent() ? optional.get() : null);
				facilityDetail.setStatusFlag(0);
				facilityDetail.setUpdatedOn(null);
				facilityDetail.setUpdatedBy(null);
				facilityDetailMasterRepo.save(facilityDetail);
				response.setMessage("Facility Detail Added");
				response.setStatus("Success");
			} else {
				response.setStatus("Failed");
				response.setMessage("Facility Detail Already Exist");
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<FacilityDetails> getAllFacilityDetail() {
		return facilityDetailMasterRepo.findFacilityDetail();
	}

	@Override
	public FacilityDetails getActionById(Integer facilityDetailId) {
		try {
			Optional<FacilityDetails> optional = facilityDetailMasterRepo.findById(facilityDetailId);
			if (optional.isPresent())
				return optional.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Response updateFacilityDetail(FacilityDetailBean facilityDetailBean) {
		Response response = new Response();
		try {
			FacilityDetails facilityDetails = facilityDetailMasterRepo
					.findById(facilityDetailBean.getFacilityDetailId()).get();
			Integer count = facilityDetailMasterRepo.checkFacility(facilityDetailBean.getFacilityName());
			UserDetails userdetails = userDetailsRepo.findById(Long.parseLong(facilityDetailBean.getUpdatedBy())).get();

//			if (count == 0) {
//				Calendar calendar = Calendar.getInstance();
//				facilityDetails.setFacilityName(facilityDetailBean.getFacilityName());
//				facilityDetails.setUpdatedOn(calendar.getTime());
//				facilityDetails.setUpdatedBy(userdetails);
//				facilityDetails.setStatusFlag(Integer.parseInt(facilityDetailBean.getStatusFlag()));
//				facilityDetailMasterRepo.save(facilityDetails);
//				response.setStatus("Success");
//				response.setMessage("Facility Detail Successfully Updated");
//			} else
			if (count == 0 || (facilityDetails.getFacilityName().equals(facilityDetailBean.getFacilityName())
					&& Objects.equals(facilityDetails.getFacilityDetailId(), facilityDetailBean.getFacilityDetailId()))) {
				Calendar calendar = Calendar.getInstance();
				facilityDetails.setFacilityName(facilityDetailBean.getFacilityName());
				facilityDetails.setUpdatedOn(calendar.getTime());
				facilityDetails.setUpdatedBy(userdetails);
				facilityDetails.setStatusFlag(Integer.parseInt(facilityDetailBean.getStatusFlag()));
				facilityDetailMasterRepo.save(facilityDetails);
				response.setStatus("Success");
				response.setMessage("Facility Detail Successfully Updated");
			} else {
				response.setStatus("Failed");
				response.setMessage("Facility Detail Already Exist");
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

}
