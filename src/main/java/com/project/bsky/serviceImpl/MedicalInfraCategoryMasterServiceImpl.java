package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.MedicalInfraCategory;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.MedicalInfraCategoryRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.MedicalInfraCategoryMasterService;

@Service
public class MedicalInfraCategoryMasterServiceImpl implements MedicalInfraCategoryMasterService {

	@Autowired
	MedicalInfraCategoryRepository medicalInfraCategoryRepo;

	@Autowired
	private UserDetailsRepository userdetailsrepo;

//	@Override
//	public Response saveDetails(MedicalInfraCategory medicalInfraCategory) {
//		try {
//			Calendar calendar = Calendar.getInstance();
//			medicalInfraCategory.setCreatedOn((calendar.getTime()));
//			medicalInfraCategory.setStatusFlag(0);
//			//medicalInfraCategory.setCreatedBy(createdBy);
//			medicalInfraCategory = medicalInfraCategoryRepo.save(medicalInfraCategory);
//			if(medicalInfraCategory!=null) {
//				return 1;
//			} else {
//				return 0;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}

	@Override
	public Response saveDetails(MedicalInfraCategory medicalInfraCategory) {
		Response response = new Response();
		try {
			Calendar calendar = Calendar.getInstance();
			Integer countcategoryname = medicalInfraCategoryRepo
					.checkduplicateCategoryname(medicalInfraCategory.getMedInfraCatName());
			// Integer countProc =
			// packageMasterRepository.checkduplicateProcedure(packageMasterBSKY.getProcedures());
			if (countcategoryname == 0) {
				medicalInfraCategory.setStatusFlag(0);
				medicalInfraCategory.setCreatedOn(calendar.getTime());
				UserDetails userdetails = userdetailsrepo.findById((long) medicalInfraCategory.getCreatedBy()).get();
				medicalInfraCategory.setUserId(userdetails);
				System.out.println(medicalInfraCategory);
				medicalInfraCategoryRepo.save(medicalInfraCategory);
				response.setStatus("Success");
				response.setMessage("Medical Infra Category Successfully Saved");
			} else {
				response.setMessage("Medical Infra Category is Already Exist");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	@Override
	public List<MedicalInfraCategory> getDetails() {

		List<MedicalInfraCategory> list = new ArrayList<MedicalInfraCategory>();
		// DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			list = medicalInfraCategoryRepo.getDetails();
			for (MedicalInfraCategory x : list) {
				x.setScreatedate(x.getCreatedOn().toString());
			}
		} catch (Exception e) {
		}
		return list;

	}

	@Override
	public MedicalInfraCategory getbyid(Integer medInfracatId) {
		MedicalInfraCategory medicalInfraCategory = null;
		try {
			medicalInfraCategory = medicalInfraCategoryRepo.findById(medInfracatId).get();
		} catch (Exception e) {

		}
		return medicalInfraCategory;
	}

	@Override
	public Response updateCategory(MedicalInfraCategory medicalInfraCategory) {
		Response response = new Response();
		try {
			Calendar calendar = Calendar.getInstance();
			Integer countcategoryname = medicalInfraCategoryRepo
					.checkduplicateCategoryname(medicalInfraCategory.getMedInfraCatName());
			// Integer countProc =
			// packageMasterRepository.checkduplicateProcedure(packageMasterBSKY.getProcedures());
			if (countcategoryname == 0) {
				MedicalInfraCategory medicalInfraCategory1 = medicalInfraCategoryRepo
						.findById(medicalInfraCategory.getMedInfracatId()).get();
				// medicalInfraCategory.setStatusFlag(0);
				medicalInfraCategory.setUpdatedOn(calendar.getTime());
				medicalInfraCategoryRepo.save(medicalInfraCategory);
				response.setStatus("Success");
				response.setMessage("Medical Infra Category Successfully Updated");
			} else {

				MedicalInfraCategory medicalInfraCategory1 = medicalInfraCategoryRepo
						.findBymedInfraCatName(medicalInfraCategory.getMedInfraCatName());
				if (medicalInfraCategory1.getMedInfracatId() == medicalInfraCategory.getMedInfracatId()
						&& medicalInfraCategory.getMedInfraCatName()
								.equals(medicalInfraCategory1.getMedInfraCatName())) {
					// medicalInfraCategory.setStatusFlag(0);
					// medicalInfraCategory.setUpdatedOn(calendar.getTime());
					medicalInfraCategory.setUpdatedOn(Calendar.getInstance().getTime());
					medicalInfraCategoryRepo.save(medicalInfraCategory);
					response.setStatus("Success");
					response.setMessage("Medical Infra Category Successfully Updated");
				} else {
					response.setMessage("Medical Infra Category is Already Exist");
					response.setStatus("Failed");
				}

			}
			// return response;
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	@Override
	public List<MedicalInfraCategory> getActiveCategory() {

		return medicalInfraCategoryRepo.getActiveCategoryList();
	}

	@Override
	public List<Map<String, Object>> getMedicalInfraCategoryList() {
		List<Map<String, Object>> map = new ArrayList<>();
		try {
			map = medicalInfraCategoryRepo.getMedicalInfraCategoryList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
