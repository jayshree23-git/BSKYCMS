package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageMasterBSKY;
import com.project.bsky.repository.PackageMasterRepository;
import com.project.bsky.service.PackageMasterService;

@Service
public class PackageMasterServiceImpl implements PackageMasterService {

	@Autowired
	private PackageMasterRepository packageMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<PackageMasterBSKY> getDetails() {

		return packageMasterRepository.getDetails();
	}

	@Override
	public PackageMasterBSKY getbyId(Long userid) {
		PackageMasterBSKY packageMaster = null;
		try {
			packageMaster = packageMasterRepository.findById(userid).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return packageMaster;
	}

	@Override
	public Response savePackageMasterData(PackageMasterBSKY packageMasterBSKY) {
		Response response = new Response();
		try {
			Integer countPCode = packageMasterRepository
					.checkduplicateProcedureCode(packageMasterBSKY.getProcedureCode());
			Integer countProc = packageMasterRepository.checkduplicateProcedure(packageMasterBSKY.getProcedures());
			if (countPCode == 0 && countProc == 0) {
				packageMasterBSKY.setStatusFlag(0);
				packageMasterRepository.save(packageMasterBSKY);
				response.setStatus("Success");
				response.setMessage("PackageMaster Successfully Inserted");
			} else if (countPCode != 0) {
				response.setMessage("Procedure Code is Already Exist");
				response.setStatus("Failed");
			} else if (countProc != 0) {
				response.setMessage("Procedure Name is Already Exist");
				response.setStatus("Failed");
			} else {
				response.setStatus("Failed");
				response.setMessage("Already Exist");
			}
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
		}
		return response;
	}

	@Override
	public Response updatePackageData(PackageMasterBSKY packageMasterBSKY) {
		Response response = new Response();
		try {

			Integer countPCode = packageMasterRepository
					.checkduplicateProcedureCode(packageMasterBSKY.getProcedureCode());
			Integer countProc = packageMasterRepository.checkduplicateProcedure(packageMasterBSKY.getProcedures());
			PackageMasterBSKY packageMasterBSKY1 = packageMasterRepository
					.findByprocedureCode(packageMasterBSKY.getProcedureCode());
			PackageMasterBSKY packageMasterBSKY2 = packageMasterRepository
					.findByprocedures(packageMasterBSKY.getProcedures());
			if (countPCode == 0 && countProc == 0) {
				packageMasterRepository.save(packageMasterBSKY);
				response.setStatus("Success");
				response.setMessage("PackageMaster Successfully Updated");
			}

			else if (countPCode != 0 && countProc == 0) {
				if (packageMasterBSKY1.getId() == packageMasterBSKY.getId()
						&& packageMasterBSKY.getProcedureCode().equals(packageMasterBSKY1.getProcedureCode())) {
					packageMasterRepository.save(packageMasterBSKY);
					response.setStatus("Success");
					response.setMessage("PackageMaster Successfully Updated");
				} else {
					response.setMessage("Procedure Code is Already Exist");
					response.setStatus("Failed");
				}
			} else if (countPCode == 0 && countProc != 0) {
				if (packageMasterBSKY2.getId() == packageMasterBSKY.getId()
						&& packageMasterBSKY.getProcedures().equals(packageMasterBSKY2.getProcedures())) {
					packageMasterRepository.save(packageMasterBSKY);
					response.setStatus("Success");
					response.setMessage("PackageMaster Successfully Updated");
				} else {
					response.setMessage("Procedure Name is Already Exist");
					response.setStatus("Failed");
				}
			} else {
				if (packageMasterBSKY1.getId() == packageMasterBSKY.getId()
						&& packageMasterBSKY.getProcedureCode().equals(packageMasterBSKY1.getProcedureCode())
						&& packageMasterBSKY2.getId() == packageMasterBSKY.getId()
						&& packageMasterBSKY.getProcedures().equals(packageMasterBSKY2.getProcedures())) {
					packageMasterRepository.save(packageMasterBSKY);
					response.setStatus("Success");
					response.setMessage("PackageMaster Successfully Updated");
				} else {
					response.setStatus("Failed");
					response.setMessage("Already Exist");
				}
			}
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
		}
		return response;
	}

	@Override
	public String getPackageByProcedure(String procedureCode) throws SQLException {
		JSONObject finalResponse = new JSONObject();
		JSONObject packageObject = null;
		JSONArray packageArray = new JSONArray();
		ResultSet packageDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_PACKAGENAME_LIST")
					.registerStoredProcedureParameter("P_packageheadercode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_packageheadercode", procedureCode);
			storedProcedureQuery.execute();
			packageDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (packageDetails.next()) {
				packageObject = new JSONObject();
				packageObject.put("packageHeaderCode", packageDetails.getString(1));
				packageObject.put("procedureCode", packageDetails.getString(2));
				packageObject.put("procedureDescription",
						packageDetails.getString(3) + " (" + packageDetails.getString(2) + ")");
				packageArray.put(packageObject);
			}
			finalResponse.put("packageArray", packageArray);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (packageDetails != null)
				packageDetails.close();
		}
		return finalResponse.toString();
	}

}
