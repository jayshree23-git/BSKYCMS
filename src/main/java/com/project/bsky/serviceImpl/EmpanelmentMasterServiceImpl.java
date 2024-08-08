package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.DuplicateCheck;
import com.project.bsky.bean.Response;
import com.project.bsky.model.MedicalExpertiseModel;
import com.project.bsky.model.TypeOfExpertiseModel;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.BankIFSCDetailsRepository;
import com.project.bsky.repository.EmpanelmentMasterRepository;
import com.project.bsky.repository.TypeOfExpertiseRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.EmpanelmentMasterService;
import com.project.bsky.util.ResultsetListConverter;

/**
 * @author jayshree.moharana
 *
 */
@Service
public class EmpanelmentMasterServiceImpl implements EmpanelmentMasterService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EmpanelmentMasterRepository empanelmentMasterRepository;

	@Autowired
	private TypeOfExpertiseRepository typeOfExpertiseRepository;

	@Autowired
	private BankIFSCDetailsRepository bankIFSCDetailsRepository;

	@Autowired
	private UserDetailsRepository userdetailsrepo;

	Calendar cal = Calendar.getInstance();
	java.util.Date date = cal.getTime();

	@Override
	public List<Map<String, Object>> getEmpanelmentMasterDetails(String flag, String queryParam) {
		ResultSet rs = null;
		List<Map<String, Object>> members = new ArrayList<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_emp_master_details")
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_query_param", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", flag);
			storedProcedureQuery.setParameter("p_query_param", queryParam);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result");
			members = ResultsetListConverter.getListFromResultSet(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return members;
	}

	@Override
	public List<Map<String, Object>> getBankIFSCDetails(String bankName, String districtName) {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			list = bankIFSCDetailsRepository.getBankIFSCDetails(bankName, districtName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Response savemedicalexpertisedata(MedicalExpertiseModel medicalexpertisemodel) {
		Response response = new Response();
		try {

			Integer countPCode = empanelmentMasterRepository
					.checkduplicatemedicalexpertise(medicalexpertisemodel.getMedexpertisename());
			System.out.println(countPCode);
			if (countPCode == 0) {
				Optional<UserDetails> userdetails = userdetailsrepo.findById(medicalexpertisemodel.getCreatedby());
				if (userdetails.isPresent()) {
					medicalexpertisemodel.setUserdetails(userdetails.get());
				}
				medicalexpertisemodel.setStatusFlag(0);
				medicalexpertisemodel.setCreatedon(Calendar.getInstance().getTime());
				empanelmentMasterRepository.save(medicalexpertisemodel);
				response.setStatus("Success");
				response.setMessage("Medical Expertise  Successfully Submitted");
			} else {
				response.setMessage("Medical Expertise  is Already Exist");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
		}
		return response;
	}

	@Override
	public List<MedicalExpertiseModel> getmedicalexpertiseData() {
		return empanelmentMasterRepository.getmedicalexpertiseData();
	}

	@Override
	public MedicalExpertiseModel getmedicalexpertiseDataById(Long userid) {
		MedicalExpertiseModel medicalexpertisemodel = new MedicalExpertiseModel();
		try {
			Optional<MedicalExpertiseModel> optional = empanelmentMasterRepository.findById(userid);
			if (optional.isPresent())
				medicalexpertisemodel = optional.get();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return medicalexpertisemodel;
	}

	@Override
	public Response updateMedicalexpertise(MedicalExpertiseModel medicalexpertisemodel) {
		Response response = new Response();
		
		try {
			Integer countPCode = empanelmentMasterRepository
					.checkduplicatemedicalexpertise(medicalexpertisemodel.getMedexpertisename());
		
			if (countPCode == 0) {				
				MedicalExpertiseModel medicalexpertisemodel1 = empanelmentMasterRepository.findById(medicalexpertisemodel.getId()).get();
				medicalexpertisemodel1.setUpdatedon(Calendar.getInstance().getTime());
				medicalexpertisemodel1.setMedexpertisename(medicalexpertisemodel.getMedexpertisename());
				medicalexpertisemodel1.setUpdatedby(medicalexpertisemodel.getUpdatedby());
					empanelmentMasterRepository.save(medicalexpertisemodel1);
					response.setStatus("Success");
					response.setMessage("Medical Expertise Successfully Updated");
				
			} else {
				MedicalExpertiseModel medicalexpertisemodel1 = empanelmentMasterRepository.findBymedexpertisename(medicalexpertisemodel.getMedexpertisename());
				if (Objects.equals(medicalexpertisemodel1.getId(), medicalexpertisemodel.getId())
						&& medicalexpertisemodel.getMedexpertisename()
								.equals(medicalexpertisemodel1.getMedexpertisename())) {
					medicalexpertisemodel1.setUpdatedon(Calendar.getInstance().getTime());
					medicalexpertisemodel1.setMedexpertisename(medicalexpertisemodel.getMedexpertisename());
					medicalexpertisemodel1.setUpdatedby(medicalexpertisemodel.getUpdatedby());
						empanelmentMasterRepository.save(medicalexpertisemodel1);
						response.setStatus("Success");
						response.setMessage("Medical Expertise  Successfully Updated");
				} else {
					response.setMessage("Medical Expertise  is Already Exist");
					response.setStatus("Failed");
				}
			}

		} catch (Exception e) {
			System.out.println(e);
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
		}
		return response;
		
		}

	@Override
	public List<MedicalExpertiseModel> getmedicalexpname() {
		return empanelmentMasterRepository.findAllactivedata();
	}

	@Override
	public Integer saveTypeofExpertise(long medicalexpid, String typeofexpertise, String createdby) {
		try {
			Long count;
			count = typeOfExpertiseRepository.findtypeofexpertisename(typeofexpertise, medicalexpid);
			if (typeofexpertise.length() == 0 || typeofexpertise.equalsIgnoreCase("null")) {
				return 3;
			} else {
				if (count == 0) {
					TypeOfExpertiseModel expertisetype = new TypeOfExpertiseModel();
					expertisetype.setTypeofexpertisename(typeofexpertise);
					UserDetails userdetails = userdetailsrepo.findById(Long.parseLong(createdby)).get();
					expertisetype.setUserDetails(userdetails);
					MedicalExpertiseModel medicalexpertisemodel = new MedicalExpertiseModel();
					medicalexpertisemodel = empanelmentMasterRepository.findById(medicalexpid).get();
					expertisetype.setMedexpertiseid(medicalexpertisemodel);
					expertisetype.setCreatedate(date);
					expertisetype.setUpdatedate(date);
					expertisetype.setUpdateby("");
					expertisetype.setStatus(0);
					typeOfExpertiseRepository.save(expertisetype);
					return 1;
				} else {
					return 2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<TypeOfExpertiseModel> getExpertisetypeData() {
		return typeOfExpertiseRepository.findAllbyorder();
	}

	@Override
	public Integer delete(long typeofexpertiseid) {
		try {
			typeOfExpertiseRepository.delete(typeofexpertiseid);
			return 1;
		} catch (Exception e) {
			return 0;
		}

	}

	@Override
	public TypeOfExpertiseModel getbyid(long typeofexpertiseid) {
		try {
			TypeOfExpertiseModel typeOfexpertisemodel = typeOfExpertiseRepository.findById(typeofexpertiseid).get();
			return typeOfexpertisemodel;
		} catch (Exception e) {
			return null;
		}
	}

	public Integer updatecode(String typeofexpertisename, long medicalexpid, String updateby, long typeofexpertiseid,
			Integer status) {
		TypeOfExpertiseModel sgroup = typeOfExpertiseRepository.findById(typeofexpertiseid).get();
		MedicalExpertiseModel medicalexpertisemodel = new MedicalExpertiseModel();
		medicalexpertisemodel = empanelmentMasterRepository.findById(medicalexpid).get();
		sgroup.setMedexpertiseid(medicalexpertisemodel);
		sgroup.setTypeofexpertisename(typeofexpertisename);
		sgroup.setUpdateby(updateby);
		sgroup.setStatus(status);
		typeOfExpertiseRepository.save(sgroup);
		return 1;
	}

	@Override
	public Integer update(long medicalexpid, String typeofexpertisename, String updateby, long typeofexpertiseid,
			Integer status) {
		try {
			TypeOfExpertiseModel sl = typeOfExpertiseRepository.findBytypeofexpertisename(typeofexpertisename);
			Long count = typeOfExpertiseRepository.findtypeofexpertisename(typeofexpertisename, medicalexpid);
			if (count == 0) {
				return updatecode(typeofexpertisename, medicalexpid, updateby, typeofexpertiseid, status);
			} else if (sl.getTypeofexpertiseid().equals(typeofexpertiseid)
					&& sl.getTypeofexpertisename().equals(typeofexpertisename)) {
				return updatecode(typeofexpertisename, medicalexpid, updateby, typeofexpertiseid, status);
			} else {
				return 2;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Map<String, Object> checkduplicate(DuplicateCheck duplicateCheck) {
		Map<String, Object> response = new HashedMap<>();
		int count = 0;
		try {

			count = (int) this.entityManager.createNativeQuery("select emp_duplicate_check(?,?,?,?,?) from dual")
					.setParameter("p_insert_update", duplicateCheck.getInsertOrUpdate())
					.setParameter("p_table_flag", duplicateCheck.getWhichTable())
					.setParameter("p_value_flag", duplicateCheck.getInputFlag())
					.setParameter("p_hospital_id", duplicateCheck.getHospitalId())
					.setParameter("p_value", duplicateCheck.getInputValue()).getSingleResult();

			response.put("Status", "Success");
			response.put("Count", count);

		} catch (Exception e) {
			response.put("Status", "Failed");
			response.put("Message", ExceptionUtils.getStackTrace(e));
		}

		return response;
	}

}
