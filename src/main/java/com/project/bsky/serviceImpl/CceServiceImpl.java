package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.AssemblyConstitutionBean;
import com.project.bsky.bean.CceDto;
import com.project.bsky.bean.CceGroupBean;
import com.project.bsky.bean.CceOutBoundBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.TransactionInformationDto;
import com.project.bsky.model.Cce;
import com.project.bsky.model.CceLog;
import com.project.bsky.repository.CceLogRepository;
import com.project.bsky.repository.CceRepository;
import com.project.bsky.service.CceService;

@Service
public class CceServiceImpl implements CceService {
	@Autowired
	private CceRepository cceRepository;

	@Autowired
	private CceLogRepository ccelogRepository;

	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object> gettransactionInformation(Long userId, String action) {

		ResultSet patientObj = null;
		List<Object> transactionDetails = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PATIENT_BLOCKED_DATA_FEEDBACK")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.execute();
			patientObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (patientObj.next()) {
				TransactionInformationDto tiBean = new TransactionInformationDto();
				tiBean.setUrn(patientObj.getString(1));
				tiBean.setTransactionId(patientObj.getLong(2));
				tiBean.setInvoice(patientObj.getString(3));
				tiBean.setPatientName(patientObj.getString(4));
				tiBean.setPatientContactNumber(patientObj.getString(5));
				tiBean.setDistrictName(patientObj.getString(6));
				tiBean.setBlockName(patientObj.getString(7));
				tiBean.setPanchayatName(patientObj.getString(8));
				tiBean.setVillageName(patientObj.getString(9));
				tiBean.setAdmissionDate(patientObj.getString(10));
				tiBean.setTotalAmoutClaimed(patientObj.getString(11));
				tiBean.setStateName(patientObj.getString(12));
				tiBean.setHospitalDistrict(patientObj.getString(14));
				tiBean.setHospitalName(patientObj.getString(16));
				tiBean.setHospitalCode(patientObj.getString(17));
				tiBean.setMemberid(patientObj.getString(18));
				tiBean.setCreatedon(patientObj.getString(19));
				tiBean.setProcedureName(patientObj.getString(20));
				tiBean.setPackageName(patientObj.getString(21));
				tiBean.setCaseNo(patientObj.getString(22));
				tiBean.setAllottedDate(patientObj.getString(23));
				tiBean.setExecutiveUserId(patientObj.getLong(24));
				transactionDetails.add(tiBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (patientObj != null)
					patientObj.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return transactionDetails;
	}

	@Override
	public Response saveCce(CceGroupBean cce) {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		Response response = new Response();
		try {
			List<TransactionInformationDto> list = cce.getCcelist();
			for (TransactionInformationDto transactionInformationDto : list) {
				Cce cceRequest = new Cce();
				cceRequest.setQuestion1Response(cce.getQuestion1Response());
				cceRequest.setQuestion2Response(cce.getQuestion2Response());
				cceRequest.setQuestion3Response(cce.getQuestion3Response());
				cceRequest.setQuestion4Response(cce.getQuestion4Response());
				cceRequest.setAlternatePhoneno(cce.getAlternatePhoneno());
				cceRequest.setExecutiveRemarks(cce.getExecutiveRemarks());

				cceRequest.setStatus(transactionInformationDto.getActStat());
				cceRequest.setCategoryName(transactionInformationDto.getCatg());
				cceRequest.setUrn(transactionInformationDto.getUrn());
				cceRequest.setTransactionId(transactionInformationDto.getTransactionId());
				cceRequest.setInvoice(transactionInformationDto.getInvoice());
				cceRequest.setPatientName(transactionInformationDto.getPatientName());
				Long number=0l;
				try {
					number=Long.parseLong(transactionInformationDto.getPatientContactNumber());
				}catch (Exception e) {
					number=0l;
				}
				cceRequest.setPatientContactNumber(number);
				cceRequest.setStateName(transactionInformationDto.getStateName());
				cceRequest.setDistrictName(transactionInformationDto.getDistrictName());
				cceRequest.setBlockName(transactionInformationDto.getBlockName());
				cceRequest.setPanchayatName(transactionInformationDto.getPanchayatName());
				cceRequest.setVillageName(transactionInformationDto.getVillageName());
				cceRequest.setAdmissionDate(
//                        transactionInformationDto.getAdmissionDate());
						new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(transactionInformationDto.getAdmissionDate()));
				cceRequest.setTotalAmoutClaimed(transactionInformationDto.getTotalAmoutClaimed());
				cceRequest.setHospitalDistrict(transactionInformationDto.getHospitalDistrict());
				cceRequest.setHospitalName(transactionInformationDto.getHospitalName());
				cceRequest.setHospitalCode(transactionInformationDto.getHospitalCode());
				cceRequest.setPackageName(transactionInformationDto.getPackageName());
//				cceRequest.setProcedureName(transactionInformationDto.getProcedureName());
				String s = transactionInformationDto.getProcedureName();
				if (s != null) {
					if (s.length() > 4000) {
						s = s.substring(0, 3999);
					}
				}
				cceRequest.setProcedureName(s);
				cceRequest.setAllottedDate(transactionInformationDto.getAllottedDate());
				cceRequest.setExecutiveUserId(transactionInformationDto.getExecutiveUserId());
				cceRequest.setCreatedBy(cce.getCreatedBy());
				cceRequest.setUpdatedBy(-1);
				cceRequest.setCreatedOn(date);
				cceRequest.setUpdatedOn(date);
				cceRequest.setDeletedFlag(0);

				cceRequest.setReAssignConnectedStatus(1);
				if (transactionInformationDto.getActStat().equals("3")) {
					cceRequest.setConnectedStatus(0);
					if (cceRequest.getAttemptCount() == null || cceRequest.getAttemptCount() == 0) {
						cceRequest.setAttemptCount(1);
					}
				}

				if (transactionInformationDto.getActStat().equals("1")
						|| transactionInformationDto.getActStat().equals("2")) {
					cceRequest.setConnectedStatus(1);
					cceRequest.setAttemptCount(0);
				}
				cceRequest.setDgoQueryCount(0);
				cceRequest.setGoQueryCount(0);
				cceRequest.setDcAction("N");
				cceRequest.setDgoAction("N");
				cceRequest.setGoAction("N");
				cceRepository.save(cceRequest);
				CceLog ccelog = ccelogRepository.findByTRANSACTIONID(cceRequest.getTransactionId());
				ccelog.setACTIONTAKEN(1);
				ccelogRepository.save(ccelog);
			}
			response.setMessage("Record Saved Successfully");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}

		return response;
	}

	@Override
	public List<Object> getNotConnected(Long userId, String action) {
		ResultSet patientNotConObj = null;
		List<Object> notConnectedList = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PATIENT_BLOCKED_DATA_FEEDBACK")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.execute();
			patientNotConObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (patientNotConObj.next()) {
				CceDto rcBean = new CceDto();
				rcBean.setId(patientNotConObj.getLong(1));
				rcBean.setUrn(patientNotConObj.getString(2));
				rcBean.setTransactionId(patientNotConObj.getLong(3));
				rcBean.setInvoice(patientNotConObj.getString(4));
				rcBean.setPatientName(patientNotConObj.getString(5));
				rcBean.setPatientContactNumber(patientNotConObj.getString(6));
				rcBean.setDistrictName(patientNotConObj.getString(7));
				rcBean.setBlockName(patientNotConObj.getString(8));
				rcBean.setPanchayatName(patientNotConObj.getString(9));
				rcBean.setVillageName(patientNotConObj.getString(10));
				rcBean.setAdmissionDate(patientNotConObj.getString(11));
				rcBean.setTotalAmoutClaimed(patientNotConObj.getString(12));
				rcBean.setStateName(patientNotConObj.getString(13));
				rcBean.setHospitalDistrict(patientNotConObj.getString(14));
				rcBean.setHospitalName(patientNotConObj.getString(15));
				rcBean.setHospitalCode(patientNotConObj.getString(16));
				rcBean.setPackageName(patientNotConObj.getString(17));
				rcBean.setProcedureName(patientNotConObj.getString(18));
				rcBean.setStatus(patientNotConObj.getString(19));
				rcBean.setCategoryName(patientNotConObj.getString(20));
				rcBean.setAlternatePhoneno(patientNotConObj.getString(21));
				rcBean.setAttemptCount(patientNotConObj.getInt(22));
				rcBean.setMemberId(patientNotConObj.getLong(23));
				rcBean.setCaseNo(patientNotConObj.getString(24));
				rcBean.setAllottedDate(patientNotConObj.getString(25));
				rcBean.setExecutiveUserId(patientNotConObj.getLong(26));
				notConnectedList.add(rcBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (patientNotConObj != null)
					patientNotConObj.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return notConnectedList;
	}

	@Override
	public Response saveNotConnectedCce(CceGroupBean cceNot) {
		// System.out.println(cceNot);
		Response response = new Response();
		try {
			List<TransactionInformationDto> list = cceNot.getCcelist();
			for (TransactionInformationDto transactionInformationDto : list) {
				Cce cceRequest = new Cce();
				cceRequest.setQuestion1Response(cceNot.getQue1());
				cceRequest.setQuestion2Response(cceNot.getQue2());
				cceRequest.setQuestion3Response(cceNot.getQue3());
				cceRequest.setQuestion4Response(cceNot.getQue4());
				cceRequest.setAlternatePhoneno(cceNot.getAltNo());
				cceRequest.setExecutiveRemarks(cceNot.getRemk());

				cceRequest.setStatus(transactionInformationDto.getActStat());
				cceRequest.setCategoryName(transactionInformationDto.getCatg());
				cceRequest.setUrn(transactionInformationDto.getUrn());
				cceRequest.setTransactionId(transactionInformationDto.getTransactionId());
				cceRequest.setInvoice(transactionInformationDto.getInvoice());
				cceRequest.setPatientName(transactionInformationDto.getPatientName());
				Long number=0l;
				try {
					number=Long.parseLong(transactionInformationDto.getPatientContactNumber());
				}catch (Exception e) {
					number=0l;
				}
				cceRequest.setPatientContactNumber(number);
				cceRequest.setStateName(transactionInformationDto.getStateName());
				cceRequest.setDistrictName(transactionInformationDto.getDistrictName());
				cceRequest.setBlockName(transactionInformationDto.getBlockName());
				cceRequest.setPanchayatName(transactionInformationDto.getPanchayatName());
				cceRequest.setVillageName(transactionInformationDto.getVillageName());
				cceRequest.setAdmissionDate(
//                        transactionInformationDto.getAdmissionDate());
						new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(transactionInformationDto.getAdmissionDate()));
				cceRequest.setTotalAmoutClaimed(transactionInformationDto.getTotalAmoutClaimed());
				cceRequest.setHospitalDistrict(transactionInformationDto.getHospitalDistrict());
				cceRequest.setHospitalName(transactionInformationDto.getHospitalName());
				cceRequest.setHospitalCode(transactionInformationDto.getHospitalCode());
				cceRequest.setPackageName(transactionInformationDto.getPackageName());
				cceRequest.setProcedureName(transactionInformationDto.getProcedureName());
				cceRequest.setAllottedDate(transactionInformationDto.getAllottedDate());
				cceRequest.setExecutiveUserId(transactionInformationDto.getExecutiveUserId());
				cceRequest.setCreatedBy(cceNot.getCreatedBy());
				cceRequest.setUpdatedBy(-1);
				cceRequest.setCreatedOn(date);
				cceRequest.setUpdatedOn(date);
				cceRequest.setDeletedFlag(0);

				cceRequest.setReAssignConnectedStatus(1);
				cceRequest.setReAssignDate(transactionInformationDto.getReAssignDate());
				cceRequest.setReAssignRemark(transactionInformationDto.getReAssignRemark());
				cceRequest.setReAssignFlag(0);
				cceRequest.setDgoQueryCount(0);
				cceRequest.setGoQueryCount(0);
				cceRequest.setDcAction("N");
				cceRequest.setDgoAction("N");
				cceRequest.setGoAction("N");

				if (transactionInformationDto.getActStat().equals("3")) {
					cceRequest.setConnectedStatus(0);
					if (transactionInformationDto.getAttemptCount() != null) {
						cceRequest.setAttemptCount(transactionInformationDto.getAttemptCount() + 1);
					}
//                    if (transactionInformationDto.getAttemptCount() == 5) {
//                        cceRequest.setAttemptCount(5);
//                    }
				}

				if (transactionInformationDto.getActStat().equals("1")
						|| transactionInformationDto.getActStat().equals("2")) {
					cceRequest.setConnectedStatus(1);
					if (transactionInformationDto.getAttemptCount() != null) {
						cceRequest.setAttemptCount(transactionInformationDto.getAttemptCount() + 1);
					}
//                    if (transactionInformationDto.getAttemptCount() == 5) {
//                        cceRequest.setAttemptCount(6);
//                    }
					List<Cce> listCce = cceRepository.findByTransactionIdAndUrnAndInvoice(cceRequest.getTransactionId(),
							cceRequest.getUrn(), cceRequest.getInvoice());
					if (listCce.size() > 0) {
						for (Cce obj : listCce) {
							obj.setConnectedStatus(1);
							cceRepository.save(obj);
						}
					}
				}
				cceRepository.save(cceRequest);
			}
			response.setMessage("Record Saved Successfully");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<Object> getAllCce(Long userId, String action) {
		ResultSet patientNotConObj = null;
		List<Object> notConnectedList = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PATIENT_BLOCKED_DATA_FEEDBACK")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.execute();
			patientNotConObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (patientNotConObj.next()) {
				CceDto rcBean = new CceDto();
				rcBean.setId(patientNotConObj.getLong(1));
				rcBean.setUrn(patientNotConObj.getString(2));
				rcBean.setTransactionId(patientNotConObj.getLong(3));
				rcBean.setInvoice(patientNotConObj.getString(4));
				rcBean.setPatientName(patientNotConObj.getString(5));
				rcBean.setPatientContactNumber(patientNotConObj.getString(6));
				rcBean.setDistrictName(patientNotConObj.getString(7));
				rcBean.setBlockName(patientNotConObj.getString(8));
				rcBean.setPanchayatName(patientNotConObj.getString(9));
				rcBean.setVillageName(patientNotConObj.getString(10));
				rcBean.setAdmissionDate(patientNotConObj.getString(11));
				rcBean.setTotalAmoutClaimed(patientNotConObj.getString(12));
				rcBean.setStateName(patientNotConObj.getString(13));
				rcBean.setHospitalDistrict(patientNotConObj.getString(14));
				rcBean.setHospitalName(patientNotConObj.getString(15));
				rcBean.setHospitalCode(patientNotConObj.getString(16));
				rcBean.setPackageName(patientNotConObj.getString(17));
				rcBean.setProcedureName(patientNotConObj.getString(18));
				rcBean.setStatus(patientNotConObj.getString(19));
				rcBean.setCategoryName(patientNotConObj.getString(20));
				rcBean.setAttemptCount(patientNotConObj.getInt(21));
				rcBean.setMemberId(patientNotConObj.getLong(22));
				rcBean.setCaseNo(patientNotConObj.getString(23));
				rcBean.setAlternatePhoneno(patientNotConObj.getString(24));
				rcBean.setQuestion1Response(patientNotConObj.getString(25));
				rcBean.setQuestion2Response(patientNotConObj.getString(26));
				rcBean.setQuestion3Response(patientNotConObj.getString(27));
				rcBean.setQuestion4Response(patientNotConObj.getString(28));
				rcBean.setExecutiveRemarks(patientNotConObj.getString(29));
				rcBean.setReAssignRemark(patientNotConObj.getString(30));
				rcBean.setReAssignDate(patientNotConObj.getDate(31));
				rcBean.setAllottedDate(patientNotConObj.getString(32));
				rcBean.setExecutiveUserId(patientNotConObj.getLong(33));
//				System.out.println(patientNotConObj.getDate(34));

				rcBean.setCreatedOn(patientNotConObj.getDate(34));
//				rcBean.setDcReply(patientNotConObj.getString(35));;
				notConnectedList.add(rcBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (patientNotConObj != null)
					patientNotConObj.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return notConnectedList;
	}

	@Override
	public Response saveReAssignedCce(CceGroupBean cceNot) {
		Response response = new Response();
		try {
			List<TransactionInformationDto> list = cceNot.getCcelist();
			for (TransactionInformationDto transactionInformationDto : list) {
				Cce cceRequest = new Cce();
				Optional<Cce> cceOptional = cceRepository.findById(transactionInformationDto.getId());
				cceOptional.ifPresent(cceRequest1 -> {
					cceRequest1.setDeletedFlag(1);
					cceRepository.save(cceRequest1);
				});
				cceRequest.setQuestion1Response(cceNot.getQue1());
				cceRequest.setQuestion2Response(cceNot.getQue2());
				cceRequest.setQuestion3Response(cceNot.getQue3());
				cceRequest.setQuestion4Response(cceNot.getQue4());
				cceRequest.setAlternatePhoneno(cceNot.getAltNo());
				cceRequest.setExecutiveRemarks(cceNot.getRemk());

				cceRequest.setStatus(transactionInformationDto.getActStat());
				cceRequest.setCategoryName(transactionInformationDto.getCatg());
				cceRequest.setUrn(transactionInformationDto.getUrn());
				cceRequest.setTransactionId(transactionInformationDto.getTransactionId());
				cceRequest.setInvoice(transactionInformationDto.getInvoice());
				cceRequest.setPatientName(transactionInformationDto.getPatientName());
				Long number=0l;
				try {
					number=Long.parseLong(transactionInformationDto.getPatientContactNumber());
				}catch (Exception e) {
					number=0l;
				}
				cceRequest.setPatientContactNumber(number);
				cceRequest.setStateName(transactionInformationDto.getStateName());
				cceRequest.setDistrictName(transactionInformationDto.getDistrictName());
				cceRequest.setBlockName(transactionInformationDto.getBlockName());
				cceRequest.setPanchayatName(transactionInformationDto.getPanchayatName());
				cceRequest.setVillageName(transactionInformationDto.getVillageName());
				cceRequest.setAdmissionDate(
//                        transactionInformationDto.getAdmissionDate());
						new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(transactionInformationDto.getAdmissionDate()));
				cceRequest.setTotalAmoutClaimed(transactionInformationDto.getTotalAmoutClaimed());
				cceRequest.setHospitalDistrict(transactionInformationDto.getHospitalDistrict());
				cceRequest.setHospitalName(transactionInformationDto.getHospitalName());
				cceRequest.setHospitalCode(transactionInformationDto.getHospitalCode());
				cceRequest.setPackageName(transactionInformationDto.getPackageName());
				cceRequest.setProcedureName(transactionInformationDto.getProcedureName());
				cceRequest.setAllottedDate(transactionInformationDto.getAllottedDate());
				cceRequest.setExecutiveUserId(transactionInformationDto.getExecutiveUserId());
				cceRequest.setCreatedBy(cceNot.getCreatedBy());
				cceRequest.setUpdatedBy(-1);
				cceRequest.setCreatedOn(date);
				cceRequest.setUpdatedOn(date);
				cceRequest.setDeletedFlag(0);

				cceRequest.setReAssignConnectedStatus(1);
				cceRequest.setReAssignDate(transactionInformationDto.getReAssignDate());
				cceRequest.setReAssignRemark(transactionInformationDto.getReAssignRemark());
				cceRequest.setReAssignFlag(2);
				cceRequest.setDgoQueryCount(0);
				cceRequest.setGoQueryCount(0);
				cceRequest.setDcAction("N");
				cceRequest.setDgoAction("N");
				cceRequest.setGoAction("N");

				if (transactionInformationDto.getActStat().equals("3")) {
					cceRequest.setConnectedStatus(0);
					if (transactionInformationDto.getAttemptCount() != null) {
						cceRequest.setAttemptCount(transactionInformationDto.getAttemptCount() + 1);
					}
					List<Cce> listCce = cceRepository.findByTransactionIdAndUrnAndInvoice(cceRequest.getTransactionId(),
							cceRequest.getUrn(), cceRequest.getInvoice());
					if (listCce.size() > 0) {
						for (Cce obj : listCce) {
							obj.setReAssignConnectedStatus(0);
							cceRepository.save(obj);
						}
					}
				}

				if (transactionInformationDto.getActStat().equals("1")
						|| transactionInformationDto.getActStat().equals("2")) {
					cceRequest.setConnectedStatus(1);
					if (transactionInformationDto.getAttemptCount() != null) {
						cceRequest.setAttemptCount(transactionInformationDto.getAttemptCount() + 1);
					}
					List<Cce> listCce = cceRepository.findByTransactionIdAndUrnAndInvoice(cceRequest.getTransactionId(),
							cceRequest.getUrn(), cceRequest.getInvoice());
					if (listCce.size() > 0) {
						for (Cce obj : listCce) {
							obj.setReAssignConnectedStatus(0);
							obj.setConnectedStatus(1);
							cceRepository.save(obj);
						}
					}
				}
				cceRepository.save(cceRequest);
			}
			response.setMessage("Record Saved Successfully");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Map<Long, List<Object>> getallCceData(String formDate, String toDate, String stateCode, String distCode,
			String hospitalCode, String actionBy, String pendingAt, String action, String status, Integer pageIn,
			Integer pageEnd) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> count = new ArrayList<Object>();
		Long size = null;
		ResultSet goDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_GOCCEDATA")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONBY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PENDINGAT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", formDate);
			storedProcedureQuery.setParameter("P_TODATE", toDate);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_ACTIONBY", actionBy);
			storedProcedureQuery.setParameter("P_PENDINGAT", pendingAt);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.setParameter("P_STATUS", status);
			storedProcedureQuery.setParameter("P_PAGE_IN", pageIn);
			storedProcedureQuery.setParameter("P_PAGE_END", pageEnd);
			storedProcedureQuery.execute();

			size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
			goDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");

			if (action.equals("C")) {
				while (goDetailsObj.next()) {
					CceDto rcBeanC = new CceDto();
					if (goDetailsObj != null) {
						rcBeanC.setStatusFlag(false);
					} else {
						rcBeanC.setStatusFlag(true);
					}
					rcBeanC.setPendingDcCount(goDetailsObj.getInt(1));
					rcBeanC.setPendingDgoCount(goDetailsObj.getInt(2));
					rcBeanC.setPendingGoCount(goDetailsObj.getInt(3));
					rcBeanC.setActionDcCount(goDetailsObj.getInt(4));
					rcBeanC.setActionDgoCount(goDetailsObj.getInt(5));
					rcBeanC.setActionCceCount(goDetailsObj.getInt(6));
					count.add(rcBeanC);
				}
				map.put(size, count);
			}

			if (action.equals("A")) {
				while (goDetailsObj.next()) {
					CceDto rcBean = new CceDto();
					if (goDetailsObj != null) {
						rcBean.setStatusFlag(false);
					} else {
						rcBean.setStatusFlag(true);
					}
					rcBean.setTransactionId(goDetailsObj.getLong(1));
					rcBean.setUrn(goDetailsObj.getString(2));
					rcBean.setPatientName(goDetailsObj.getString(3));
					rcBean.setStateName(goDetailsObj.getString(4));
					rcBean.setDistrictName(goDetailsObj.getString(5));
					rcBean.setBlockName(goDetailsObj.getString(6));
					rcBean.setPanchayatName(goDetailsObj.getString(7));
					rcBean.setVillageName(goDetailsObj.getString(8));
					rcBean.setPatientContactNumber(goDetailsObj.getString(9));
					rcBean.setAdmissionDate(goDetailsObj.getString(10));
					rcBean.setTotalAmoutClaimed(goDetailsObj.getString(11));
					rcBean.setHospitalDistrict(goDetailsObj.getString(12));
					rcBean.setHospitalName(goDetailsObj.getString(13));
					rcBean.setHospitalCode(goDetailsObj.getString(14));
					rcBean.setGoRemarks(goDetailsObj.getString(15));
					rcBean.setGoSubmittedDDate(goDetailsObj.getString(16));
					rcBean.setStatus(goDetailsObj.getString(17));
					rcBean.setCategoryName(goDetailsObj.getString(18));
					rcBean.setQuestion1Response(goDetailsObj.getString(19));
					rcBean.setQuestion2Response(goDetailsObj.getString(20));
					rcBean.setQuestion3Response(goDetailsObj.getString(21));
					rcBean.setQuestion4Response(goDetailsObj.getString(22));
					rcBean.setExecutiveUserId(goDetailsObj.getLong(23));
					rcBean.setDcUserId(goDetailsObj.getLong(24));
					rcBean.setDcSubmittedDDate(goDetailsObj.getString(25));
					rcBean.setDcUploadPdfPath(goDetailsObj.getString(26));
					rcBean.setDcRemarks(goDetailsObj.getString(27));
					rcBean.setExecutiveRemarks(goDetailsObj.getString(28));
					rcBean.setAlternatePhoneno(goDetailsObj.getString(29));
					rcBean.setInvoice(goDetailsObj.getString(30));
					rcBean.setDcUploadAudio(goDetailsObj.getString(31));
					rcBean.setDcUploadVideo(goDetailsObj.getString(32));
					rcBean.setDgoRemarks(goDetailsObj.getString(33));
					rcBean.setDgoDoc(goDetailsObj.getString(34));
					rcBean.setDgoUserId(goDetailsObj.getLong(35));
					rcBean.setDgoSubmittedDDate(goDetailsObj.getString(36));
					rcBean.setId(goDetailsObj.getLong(37));
					rcBean.setAllottedDate(goDetailsObj.getString(38));
					rcBean.setPendingAt(goDetailsObj.getString(39));
					rcBean.setActionBy(goDetailsObj.getString(40));
					rcBean.setGoQueryCount(goDetailsObj.getInt(41));
					rcBean.setDgoReplay(goDetailsObj.getString(42));
					rcBean.setGoQueryRemarks(goDetailsObj.getString(43));
					rcBean.setDgoQueryRemarks(goDetailsObj.getString(44));
					rcBean.setDgoQueryDoc(goDetailsObj.getString(45));
					if ((rcBean.getActionBy().equals("DC") && rcBean.getStatus().equals("No"))
							|| rcBean.getPendingAt().equals("GO")) {
						rcBean.setShowction(1);
					} else {
						rcBean.setShowction(0);
					}
					rcBean.setExpiryStatus(goDetailsObj.getInt(46));
					count.add(rcBean);

				}
				map.put(size, count);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (goDetailsObj != null) {
					goDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}

	@Override
	public Map<Long, List<Object>> getallCceDataView(String formDate, String toDate, String stateCode, String distCode,
			String hospitalCode, String actionBy, String pendingAt, String action, String status, Integer pageIn,
			Integer pageEnd) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> count = new ArrayList<Object>();
		Long size = null;
		ResultSet goDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_GOCCEDATA")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONBY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PENDINGAT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", formDate);
			storedProcedureQuery.setParameter("P_TODATE", toDate);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_ACTIONBY", actionBy);
			storedProcedureQuery.setParameter("P_PENDINGAT", pendingAt);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.setParameter("P_STATUS", status);
			storedProcedureQuery.setParameter("P_PAGE_IN", pageIn);
			storedProcedureQuery.setParameter("P_PAGE_END", pageEnd);
			storedProcedureQuery.execute();

			size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
			goDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");

			while (goDetailsObj.next()) {
				CceDto rcBean = new CceDto();
				if (goDetailsObj != null) {
					rcBean.setStatusFlag(false);
				} else {
					rcBean.setStatusFlag(true);
				}
				rcBean.setTransactionId(goDetailsObj.getLong(1));
				rcBean.setUrn(goDetailsObj.getString(2));
				rcBean.setPatientName(goDetailsObj.getString(3));
				rcBean.setStateName(goDetailsObj.getString(4));
				rcBean.setDistrictName(goDetailsObj.getString(5));
				rcBean.setBlockName(goDetailsObj.getString(6));
				rcBean.setPanchayatName(goDetailsObj.getString(7));
				rcBean.setVillageName(goDetailsObj.getString(8));
				rcBean.setPatientContactNumber(goDetailsObj.getString(9));
				rcBean.setAdmissionDate(goDetailsObj.getString(10));
				rcBean.setTotalAmoutClaimed(goDetailsObj.getString(11));
				rcBean.setHospitalDistrict(goDetailsObj.getString(12));
				rcBean.setHospitalName(goDetailsObj.getString(13));
				rcBean.setHospitalCode(goDetailsObj.getString(14));
				rcBean.setGoRemarks(goDetailsObj.getString(15));
				rcBean.setGoSubmittedDDate(goDetailsObj.getString(16));
				rcBean.setStatus(goDetailsObj.getString(17));
				rcBean.setCategoryName(goDetailsObj.getString(18));
				rcBean.setQuestion1Response(goDetailsObj.getString(19));
				rcBean.setQuestion2Response(goDetailsObj.getString(20));
				rcBean.setQuestion3Response(goDetailsObj.getString(21));
				rcBean.setQuestion4Response(goDetailsObj.getString(22));
				rcBean.setExecutiveUserId(goDetailsObj.getLong(23));
				rcBean.setDcUserId(goDetailsObj.getLong(24));
				rcBean.setDcSubmittedDDate(goDetailsObj.getString(25));
				rcBean.setDcUploadPdfPath(goDetailsObj.getString(26));
				rcBean.setDcRemarks(goDetailsObj.getString(27));
				rcBean.setExecutiveRemarks(goDetailsObj.getString(28));
				rcBean.setAlternatePhoneno(goDetailsObj.getString(29));
				rcBean.setInvoice(goDetailsObj.getString(30));
				rcBean.setDcUploadAudio(goDetailsObj.getString(31));
				rcBean.setDcUploadVideo(goDetailsObj.getString(32));
				rcBean.setDgoRemarks(goDetailsObj.getString(33));
				rcBean.setDgoDoc(goDetailsObj.getString(34));
				rcBean.setDgoUserId(goDetailsObj.getLong(35));
				rcBean.setDgoSubmittedDDate(goDetailsObj.getString(36));
				rcBean.setId(goDetailsObj.getLong(37));
				rcBean.setAllottedDate(goDetailsObj.getString(38));
				rcBean.setGoStatus(goDetailsObj.getString(39));
				rcBean.setGoQueryCount(goDetailsObj.getInt(40));
				rcBean.setDgoReplay(goDetailsObj.getString(41));
				rcBean.setGoQueryRemarks(goDetailsObj.getString(42));
				rcBean.setDgoQueryRemarks(goDetailsObj.getString(43));
				rcBean.setDgoQueryDoc(goDetailsObj.getString(44));
				count.add(rcBean);
			}
			map.put(size, count);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (goDetailsObj != null) {
					goDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}

	@Override
	public Response addGoRemark(Long id, String goRemarks, Integer action, Long goUserId) {
		Response response = new Response();
		Integer msgOut = null;
		try {
//			Cce cceDb = cceRepository.findById(id).get();
//			if (Objects.nonNull(cceDb)) {
//				if(action == 1) {
//					cceDb.setGoQueryRemarks(goRemarks);
//					cceDb.setGoUserId(goUserId);
//					cceDb.setGoQueryDate(new Date());
//					cceDb.setGoQueryCount(1);
//					cceDb.setGoAction("Q");
//					cceDb.setDgoReplay("N");
//					cceRepository.save(cceDb);
//					response.setMessage("Query Raised Successfully");
//					response.setStatus("Success");
//				} else {
//					cceDb.setGoRemarks(goRemarks);
//					cceDb.setGoUserId(goUserId);
//					cceDb.setGoSubmittedDDate(new Date());
//					cceDb.setGoAction("Y");
//					cceRepository.save(cceDb);
//					response.setMessage("Action Taken Successfully");
//					response.setStatus("Success");
//				}
//			}

			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("USP_GO_CALLCENTER_UPDATE");
			query.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_ID", Long.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_ACTION", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			query.setParameter("P_USER_ID", goUserId);
			query.setParameter("P_ID", id);
			query.setParameter("P_REMARKS", goRemarks);
			query.setParameter("P_ACTION", action);
			query.execute();
			msgOut = (Integer) query.getOutputParameterValue("P_MSGOUT");
			if (msgOut == 1) {
				response.setMessage("Action Taken Successfully");
				response.setStatus("Success");
			} else if (msgOut == 2) {
				response.setMessage("Query Raised Successfully");
				response.setStatus("Success");
			} else {
				response.setMessage("Something went wrong");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Map<Long, List<Object>> getCceReSettlement(String formDate, String toDate, String stateCode, String distCode,
			String hospitalCode, String action) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> count = new ArrayList<Object>();
		Long size = null;
		ResultSet goDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_GOQUERYDATA")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", formDate);
			storedProcedureQuery.setParameter("P_TODATE", toDate);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.execute();

			goDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");

			if (action.equals("A")) {
				while (goDetailsObj.next()) {
					CceDto rcBean = new CceDto();
					if (goDetailsObj != null) {
						rcBean.setStatusFlag(false);
					} else {
						rcBean.setStatusFlag(true);
					}
					rcBean.setTransactionId(goDetailsObj.getLong(1));
					rcBean.setUrn(goDetailsObj.getString(2));
					rcBean.setPatientName(goDetailsObj.getString(3));
					rcBean.setStateName(goDetailsObj.getString(4));
					rcBean.setDistrictName(goDetailsObj.getString(5));
					rcBean.setBlockName(goDetailsObj.getString(6));
					rcBean.setPanchayatName(goDetailsObj.getString(7));
					rcBean.setVillageName(goDetailsObj.getString(8));
					rcBean.setPatientContactNumber(goDetailsObj.getString(9));
					rcBean.setAdmissionDate(goDetailsObj.getString(10));
					rcBean.setTotalAmoutClaimed(goDetailsObj.getString(11));
					rcBean.setHospitalDistrict(goDetailsObj.getString(12));
					rcBean.setHospitalName(goDetailsObj.getString(13));
					rcBean.setHospitalCode(goDetailsObj.getString(14));
					rcBean.setGoRemarks(goDetailsObj.getString(15));
					rcBean.setGoSubmittedDDate(goDetailsObj.getString(16));
					rcBean.setStatus(goDetailsObj.getString(17));
					rcBean.setCategoryName(goDetailsObj.getString(18));
					rcBean.setQuestion1Response(goDetailsObj.getString(19));
					rcBean.setQuestion2Response(goDetailsObj.getString(20));
					rcBean.setQuestion3Response(goDetailsObj.getString(21));
					rcBean.setQuestion4Response(goDetailsObj.getString(22));
					rcBean.setExecutiveUserId(goDetailsObj.getLong(23));
					rcBean.setDcUserId(goDetailsObj.getLong(24));
					rcBean.setDcSubmittedDDate(goDetailsObj.getString(25));
					rcBean.setDcUploadPdfPath(goDetailsObj.getString(26));
					rcBean.setDcRemarks(goDetailsObj.getString(27));
					rcBean.setExecutiveRemarks(goDetailsObj.getString(28));
					rcBean.setAlternatePhoneno(goDetailsObj.getString(29));
					rcBean.setInvoice(goDetailsObj.getString(30));
					rcBean.setDcUploadAudio(goDetailsObj.getString(31));
					rcBean.setDcUploadVideo(goDetailsObj.getString(32));
					rcBean.setDgoRemarks(goDetailsObj.getString(33));
					rcBean.setDgoDoc(goDetailsObj.getString(34));
					rcBean.setDgoUserId(goDetailsObj.getLong(35));
					rcBean.setDgoSubmittedDDate(goDetailsObj.getString(36));
					rcBean.setId(goDetailsObj.getLong(37));
					rcBean.setAllottedDate(goDetailsObj.getString(38));
					rcBean.setPendingAt(goDetailsObj.getString(39));
					rcBean.setActionBy(goDetailsObj.getString(40));
					rcBean.setGoQueryCount(goDetailsObj.getInt(41));
					rcBean.setDgoReplay(goDetailsObj.getString(42));
					rcBean.setGoQueryRemarks(goDetailsObj.getString(43));
					rcBean.setDgoQueryRemarks(goDetailsObj.getString(44));
					rcBean.setDgoQueryDoc(goDetailsObj.getString(45));
					if ((rcBean.getActionBy().equals("DC") && rcBean.getStatus().equals("No"))
							|| rcBean.getPendingAt().equals("GO")) {
						rcBean.setShowction(1);
					} else {
						rcBean.setShowction(0);
					}
					count.add(rcBean);

				}
				size = Long.parseLong(String.valueOf(count.size()));
				map.put(size, count);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (goDetailsObj != null) {
					goDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}

	@Override
	public Map<Long, List<Object>> getGOInitialTakeActionData(Long userId, String formDate, String toDate,
			String stateCode, String distCode, String hospitalCode, String action) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> count = new ArrayList<Object>();
		Long size = null;
		ResultSet cceOutBoundDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CCE_ITA_LIST")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", new SimpleDateFormat("dd-MMM-yyyy").parse(formDate));
			storedProcedureQuery.setParameter("P_TO_DATE", new SimpleDateFormat("dd-MMM-yyyy").parse(toDate));
			storedProcedureQuery.setParameter("P_ACTION", action);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.execute();

			cceOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");

			while (cceOutBoundDetailsObj.next()) {
				CceOutBoundBean rcBean = new CceOutBoundBean();
				if (cceOutBoundDetailsObj != null) {
					rcBean.setStatus(false);
				} else {
					rcBean.setStatus(true);
				}
				rcBean.setUrn(cceOutBoundDetailsObj.getString(1));
				rcBean.setPatientName(cceOutBoundDetailsObj.getString(2));
				rcBean.setMobileNo(cceOutBoundDetailsObj.getString(3));
				rcBean.setBlockName(cceOutBoundDetailsObj.getString(4));
				rcBean.setPanchayatName(cceOutBoundDetailsObj.getString(5));
				rcBean.setVillageName(cceOutBoundDetailsObj.getString(6));
				rcBean.setCallResponse(cceOutBoundDetailsObj.getString(7));
				rcBean.setInvoiceNo(cceOutBoundDetailsObj.getString(8));
				rcBean.setDateOfAdm(cceOutBoundDetailsObj.getString(9));
				rcBean.setTotalAmountBlocked(cceOutBoundDetailsObj.getInt(10));
				rcBean.setHospitalDist(cceOutBoundDetailsObj.getString(11));
				rcBean.setHospitalName(cceOutBoundDetailsObj.getString(12));
				rcBean.setProcedureName(cceOutBoundDetailsObj.getString(13));
				rcBean.setPackageName(cceOutBoundDetailsObj.getString(14));
				rcBean.setAlottedDate(cceOutBoundDetailsObj.getString(15));
				rcBean.setAlternativeNo(cceOutBoundDetailsObj.getString(16));
				rcBean.setTransId(cceOutBoundDetailsObj.getString(17));
				rcBean.setQuestion1Response(cceOutBoundDetailsObj.getString(18));
				rcBean.setQuestion2Response(cceOutBoundDetailsObj.getString(19));
				rcBean.setQuestion3Response(cceOutBoundDetailsObj.getString(20));
				rcBean.setQuestion4Response(cceOutBoundDetailsObj.getString(21));
				rcBean.setExecutiveRemarks(cceOutBoundDetailsObj.getString(22));
				rcBean.setCceId(cceOutBoundDetailsObj.getLong(23));
				rcBean.setMobileActiveStatus(cceOutBoundDetailsObj.getString(24));
				rcBean.setDistrictName(cceOutBoundDetailsObj.getString(25));
				rcBean.setHospitalCode(cceOutBoundDetailsObj.getString(26));
				rcBean.setCreatedOn(cceOutBoundDetailsObj.getString(27));
				rcBean.setState(cceOutBoundDetailsObj.getString(28));
				rcBean.setDcRemarks(cceOutBoundDetailsObj.getString(29));
				rcBean.setDgoRemarks(cceOutBoundDetailsObj.getString(30));
				rcBean.setGoQueryRemarks(cceOutBoundDetailsObj.getString(31));
				rcBean.setDcSubmittedDate(cceOutBoundDetailsObj.getString(32));
				rcBean.setDoc1(cceOutBoundDetailsObj.getString(33));
				rcBean.setDoc2(cceOutBoundDetailsObj.getString(34));
				rcBean.setDoc3(cceOutBoundDetailsObj.getString(35));
				rcBean.setDCUserId(cceOutBoundDetailsObj.getString(36));
				rcBean.setITAFlag(cceOutBoundDetailsObj.getInt(37));
				rcBean.setDgoDoc(cceOutBoundDetailsObj.getString(38));
				rcBean.setDgoAction(cceOutBoundDetailsObj.getString(39));
				count.add(rcBean);

			}
			size = Long.parseLong(String.valueOf(count.size()));
			map.put(size, count);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (cceOutBoundDetailsObj != null) {
					cceOutBoundDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}

	@Override
	public Map<Long, List<Object>> getallCceDataForSHASCEO(String formDate, String toDate, String stateCode,
			String distCode, String hospitalCode, Integer pageIn, Integer pageEnd) {
		Map<Long, List<Object>> map = new HashMap<>();
		List<Object> count = new ArrayList<>();
		Long size = null;
		ResultSet goDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_VIEW_CCEDATA_BYCEO")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", formDate);
			storedProcedureQuery.setParameter("P_TODATE", toDate);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_PAGE_IN", pageIn);
			storedProcedureQuery.setParameter("P_PAGE_END", pageEnd);
			storedProcedureQuery.execute();

			size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
			goDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");

			while (goDetailsObj.next()) {
				CceDto rcBean = new CceDto();
				rcBean.setStatusFlag(goDetailsObj == null);
				rcBean.setTransactionId(goDetailsObj.getLong(1));
				rcBean.setUrn(goDetailsObj.getString(2));
				rcBean.setPatientName(goDetailsObj.getString(3));
				rcBean.setStateName(goDetailsObj.getString(4));
				rcBean.setDistrictName(goDetailsObj.getString(5));
				rcBean.setBlockName(goDetailsObj.getString(6));
				rcBean.setPanchayatName(goDetailsObj.getString(7));
				rcBean.setVillageName(goDetailsObj.getString(8));
				rcBean.setPatientContactNumber(goDetailsObj.getString(9));
				rcBean.setAdmissionDate(goDetailsObj.getString(10));
				rcBean.setTotalAmoutClaimed(goDetailsObj.getString(11));
				rcBean.setHospitalDistrict(goDetailsObj.getString(12));
				rcBean.setHospitalName(goDetailsObj.getString(13));
				rcBean.setHospitalCode(goDetailsObj.getString(14));
				rcBean.setGoRemarks(goDetailsObj.getString(15));
				rcBean.setGoSubmittedDDate(goDetailsObj.getString(16));
				rcBean.setStatus(goDetailsObj.getString(17));
				rcBean.setCategoryName(goDetailsObj.getString(18));
				rcBean.setQuestion1Response(goDetailsObj.getString(19));
				rcBean.setQuestion2Response(goDetailsObj.getString(20));
				rcBean.setQuestion3Response(goDetailsObj.getString(21));
				rcBean.setQuestion4Response(goDetailsObj.getString(22));
				rcBean.setExecutiveUserId(goDetailsObj.getLong(23));
				rcBean.setDcUserId(goDetailsObj.getLong(24));
				rcBean.setDcSubmittedDDate(goDetailsObj.getString(25));
				rcBean.setDcUploadPdfPath(goDetailsObj.getString(26));
				rcBean.setDcRemarks(goDetailsObj.getString(27));
				rcBean.setExecutiveRemarks(goDetailsObj.getString(28));
				rcBean.setAlternatePhoneno(goDetailsObj.getString(29));
				rcBean.setInvoice(goDetailsObj.getString(30));
				rcBean.setDcUploadAudio(goDetailsObj.getString(31));
				rcBean.setDcUploadVideo(goDetailsObj.getString(32));
				rcBean.setDgoRemarks(goDetailsObj.getString(33));
				rcBean.setDgoDoc(goDetailsObj.getString(34));
				rcBean.setDgoUserId(goDetailsObj.getLong(35));
				rcBean.setDgoSubmittedDDate(goDetailsObj.getString(36));
				rcBean.setId(goDetailsObj.getLong(37));
				rcBean.setAllottedDate(goDetailsObj.getString(38));
				rcBean.setPendingAt(goDetailsObj.getString(39));
				rcBean.setActionBy(goDetailsObj.getString(40));
				rcBean.setGoQueryCount(goDetailsObj.getInt(41));
				rcBean.setDgoReplay(goDetailsObj.getString(42));
				rcBean.setGoQueryRemarks(goDetailsObj.getString(43));
				rcBean.setDgoQueryRemarks(goDetailsObj.getString(44));
				rcBean.setDgoQueryDoc(goDetailsObj.getString(45));
				if ((rcBean.getActionBy().equals("DC") && rcBean.getStatus().equals("No"))
						|| rcBean.getPendingAt().equals("GO")) {
					rcBean.setShowction(1);
				} else {
					rcBean.setShowction(0);
				}
				rcBean.setExpiryStatus(goDetailsObj.getInt(46));
				count.add(rcBean);

			}
			map.put(size, count);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (goDetailsObj != null) {
					goDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getassemblyConstituencyLgdCode() {
		Map<String, Object> reportMap = new LinkedHashMap<>();
		List<Object> data = new ArrayList<>();
		List<Object> details = new ArrayList<>();
		ResultSet getdata = null;
		ResultSet getdetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ASSEMBLY_CONSTITUENCY_RPT")
					.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_action_code", 1);
			getdata = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msgout");
			getdetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (getdata.next()) {
				AssemblyConstitutionBean assembly = new AssemblyConstitutionBean();
				assembly.setBenificiary(getdata.getString(1) != null ? getdata.getString(1) : "0");
				assembly.setTotalnumberofclaims(getdata.getString(2) != null ? getdata.getString(2) : "0");
				assembly.setTotalamountclaims(getdata.getString(3) != null ? getdata.getString(3) : "0");
				assembly.setLgdcode(getdata.getString(4) != null ? getdata.getString(4) : "0");
				assembly.setConstituencyname(getdata.getString(5) != null ? getdata.getString(5) : "N/A");
				assembly.setBskynabincardbenificiary(getdata.getString(6) != null ? getdata.getString(6) : "0");
				data.add(assembly);
			}
			while (getdetails.next()) {
				AssemblyConstitutionBean constitutionBean = new AssemblyConstitutionBean();
				constitutionBean.setHeadname(getdetails.getString(1) != null ? getdetails.getString(1) : "N/A");
				constitutionBean.setGp(getdetails.getString(2) != null ? getdetails.getString(2) : "N/A");
				constitutionBean.setBlock(getdetails.getString(3) != null ? getdetails.getString(3) : "N/A");
				constitutionBean.setRs(getdetails.getString(4) != null ? getdetails.getString(4) : "0");
				constitutionBean.setLgdcode(getdetails.getString(5) != null ? getdetails.getString(5) : "N/A");
				details.add(constitutionBean);
			}
			reportMap.put("data", data);
			reportMap.put("details", details);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (getdata != null) {
					getdata.close();
				}
				if (getdetails != null) {
					getdetails.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return reportMap;

	}
}
