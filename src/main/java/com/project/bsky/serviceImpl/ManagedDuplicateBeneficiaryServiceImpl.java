package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CheackCardBalancebean;
import com.project.bsky.bean.CheckcardbalancedataBean;
import com.project.bsky.bean.ManageDuplicateBeneficiaryBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.WhatsAppUserConfigurationModel;
import com.project.bsky.service.CheckCardBalanceService;
import com.project.bsky.service.ManagedDuplicateBeneficiaryService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.JwtUtil;

@Service

public class ManagedDuplicateBeneficiaryServiceImpl implements ManagedDuplicateBeneficiaryService {
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private Logger logger;
	@Autowired
	private JwtUtil util;
	
//	VIEWLIST BY URN(ACTIONCODE-3)
	
	@Override
	public List<Object> manageduplicatebeneficiarylist(Long searchtype, String searchvalue) {
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MANAGE_DUPLICATE_BENEFICIARY")
					.registerStoredProcedureParameter("P_ACTIONCODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SEARCH_VALUE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", searchtype);
			storedProcedureQuery.setParameter("P_SEARCH_VALUE", searchvalue);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				ManageDuplicateBeneficiaryBean bean = new ManageDuplicateBeneficiaryBean();
				bean.setUrn(rs.getString(1));
				bean.setBeneficiaryname(rs.getString(2));
				bean.setUid(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setDob(rs.getString(5));
				bean.setAge(rs.getString(6));
				bean.setAadharno(rs.getString(7));
				bean.setMemberslno(rs.getLong(8));	
				bean.setAddmissionstatus(rs.getString(9));		
				bean.setExportdetails(rs.getString(10));		
				bean.setMemberid(rs.getString(11));	
				bean.setAmount(rs.getString(12));				
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
	
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return list;
	}
	
	//BENEFICIARYLIST BY URN AND UID(ACTIONCODE-1-URN,2-UID  )
	@Override
	public List<Object> beneficiarylist(Long searchtype, String searchvalue) {
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MANAGE_DUPLICATE_BENEFICIARY")
					.registerStoredProcedureParameter("P_ACTIONCODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SEARCH_VALUE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", searchtype);
			storedProcedureQuery.setParameter("P_SEARCH_VALUE", searchvalue);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				ManageDuplicateBeneficiaryBean bean = new ManageDuplicateBeneficiaryBean();
				bean.setUrn(rs.getString(1));
				bean.setExporttobskydate(rs.getString(2));
				bean.setAvailablebalanceforfamily(rs.getString(3));
				bean.setAmountblocked(rs.getString(4));
				bean.setClaimamount(rs.getString(5));
				bean.setFemalefund(rs.getString(6));
				bean.setState(rs.getString(7));
				bean.setDist(rs.getString(8));
				bean.setWard(rs.getString(9));
				bean.setVillage(rs.getString(10));
				bean.setStatus(0);
				bean.setSchemename(rs.getString(11));
				bean.setUid(rs.getString(12));


				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
	
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return list;
		}
	
	//DELETING
	@Override
	public Response inactivebeneficiary(ManageDuplicateBeneficiaryBean manageduplicatebeneficiarybean) {
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MANAGE_DUPLICATE_BENEFICIARY_SUBMIT" )
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARK", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MEMEBERSLNO", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AMOUNT", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIVE_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT",Long.class, ParameterMode.OUT);
			

			storedProcedureQuery.setParameter("P_ACTIONCODE", manageduplicatebeneficiarybean.getStatus());
			storedProcedureQuery.setParameter("P_REMARK", manageduplicatebeneficiarybean.getRemark());
			storedProcedureQuery.setParameter("P_USERID",manageduplicatebeneficiarybean.getUserid());
			storedProcedureQuery.setParameter("P_URN", manageduplicatebeneficiarybean.getUrn());
			storedProcedureQuery.setParameter("P_MEMEBERSLNO", manageduplicatebeneficiarybean.getMemberslno());
			Long amount=manageduplicatebeneficiarybean.getMemberslno()==null?0:
								Long.parseLong(manageduplicatebeneficiarybean.getAmount());
			storedProcedureQuery.setParameter("P_AMOUNT", amount);
			storedProcedureQuery.setParameter("P_ACTIVE_URN", manageduplicatebeneficiarybean.getActiveurn());
			
			storedProcedureQuery.execute();
			Long rs = (Long) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			if (rs==0) {
				response.setStatus("200");
				response.setMessage("Member Inactivated Successfully");
			}
			else if(rs ==1) {
				response.setStatus("401");
				response.setMessage("Data Already Exist");
			}
			else {
				response.setStatus("400");
				response.setMessage("Something Went Wrong");
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		return response;
		}

	@Override
	public List<Object> ongoingtreatmentlist(Long searchtype, String searchvalue) {
			ResultSet rs = null;
			List<Object> list = new ArrayList<Object>();
			
			try {
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("USP_MANAGE_DUPLICATE_BENEFICIARY")
						.registerStoredProcedureParameter("P_ACTIONCODE", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_SEARCH_VALUE", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

				storedProcedureQuery.setParameter("P_ACTIONCODE", searchtype);
				storedProcedureQuery.setParameter("P_SEARCH_VALUE", searchvalue);
				storedProcedureQuery.execute();
				rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
				while (rs.next()) {
					ManageDuplicateBeneficiaryBean bean = new ManageDuplicateBeneficiaryBean();
					bean.setCaseno(rs.getString(1));
					bean.setHospitalname(rs.getString(2));
					bean.setUrn(rs.getString(3));
					bean.setMemberid(rs.getString(4));
					bean.setMembername(rs.getString(5));
					bean.setAdmissiondate(rs.getString(6));
					bean.setUidreferenceno(rs.getString(7));
					bean.setAddmissionflag(rs.getString(8));				
					list.add(bean);
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
		
				} catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
			}
			return list;
	}

	@Override
	public List<Object> manageduplicatebeneficiaryviewlist(Long searchtype, String searchvalue) throws Exception {
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();		
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MANAGE_DUPLICATE_BENEFICIARY")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SEARCH_VALUE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 4);
			storedProcedureQuery.setParameter("P_SEARCH_VALUE", searchvalue);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				ManageDuplicateBeneficiaryBean bean = new ManageDuplicateBeneficiaryBean();
				bean.setUrn(rs.getString(1));	
				bean.setMembername(rs.getString(2));
				bean.setAadharno(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setDob(rs.getString(5));
				bean.setAge(rs.getString(6));
				bean.setUpdateby(rs.getString(7));
				bean.setUpdateon(rs.getString(8));
				bean.setRemark(rs.getString(9));
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}	
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return list;
	}
	

}
