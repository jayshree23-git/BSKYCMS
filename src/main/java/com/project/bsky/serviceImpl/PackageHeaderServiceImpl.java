package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Submitspecialitybean;
import com.project.bsky.bean.specialitybean;
import com.project.bsky.model.PackageHeader;
import com.project.bsky.repository.PackageHeaderRepo;
import com.project.bsky.service.PackageHeaderService;

@SuppressWarnings("unused")
@Service
public class PackageHeaderServiceImpl implements PackageHeaderService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PackageHeaderRepo packageHeaderRepo;

	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Response savepackageHeader(PackageHeader packageHeader) {
		Response response = new Response();
		try {
			packageHeader.setCreatedBy(-1);
			packageHeader.setCreatedOn(packageHeader.getCreatedOn());
			packageHeader.setDeletedFlag(0);
			PackageHeader save = packageHeaderRepo.save(packageHeader);
			response.setMessage("Package Header Added");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<PackageHeader> getpackageHeader() {
		List<PackageHeader> headerResponse = packageHeaderRepo.findAll(Sort.by(Sort.Direction.ASC, "packageheadername"));
			for (PackageHeader packageHeader : headerResponse) {
				packageHeader.setPackageheadername(
						packageHeader.getPackageheadername() + " (" + packageHeader.getPackageheadercode() + ")");
			}
		return headerResponse;
	}

	@Override
	public Response deletepackageheader(Long headerId) {
		Response response = new Response();
		try {
			PackageHeader packageHeader = packageHeaderRepo.findById(headerId).get();
			packageHeader.setDeletedFlag(1);
			packageHeaderRepo.save(packageHeader);
			response.setMessage("Record Successfully In-Active");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;

	}

	@Override
	public PackageHeader getbypackageHeader(Long id) {
		PackageHeader packageHeade = null;
		try {
			packageHeade = packageHeaderRepo.findById(id).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return packageHeade;
	}

	@Override
	public Response update(Long headerId, PackageHeader packageHeader) {
		Response response = new Response();
		try {
			PackageHeader packageHeaderResponse = this.getbypackageHeader(headerId);
			if (Objects.isNull(packageHeaderResponse)) {
				response.setMessage("packageHeader URL Already Exist");
			}
			packageHeader.setHeaderId(headerId);
			packageHeader.setUpdatedOn(date);
			packageHeader.setCreatedOn(packageHeaderResponse.getCreatedOn());
			packageHeader.setCreatedBy(packageHeaderResponse.getCreatedBy());
			packageHeader.setUpdatedBy(-1l);
			packageHeader.setDeletedFlag(packageHeaderResponse.getDeletedFlag());
			packageHeaderRepo.save(packageHeader);
			response.setMessage("Package Header Updated");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Long checkPackageHeaderName(String packageheadername) {
		Long checkPkg = null;
		try {
			checkPkg = packageHeaderRepo.getHeaderIdByHeaderName(packageheadername);
			return checkPkg;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkPkg;

	}

	@Override
	public Long checkDuplicatePackageheadercode(String packageheadercode) {
		Long checkPkg = null;
		try {
			checkPkg = packageHeaderRepo.getHeaderIdByHeaderCode(packageheadercode);
			return checkPkg;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkPkg;
	}

	@Override
	public List<Object> getAllpackageheaderdata() {
		List<Object> dataList = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GET_PACKAGEMASTER_DTLS")
					.registerStoredProcedureParameter("p_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SURGICALTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_ACTIONCODE", "A");
			storedProcedure.setParameter("P_PACKAGEHEADERCODE", null);
			storedProcedure.setParameter("P_PACKAGESUBCODE", null);
			storedProcedure.setParameter("P_PROCEDURECODE", null);
			storedProcedure.setParameter("P_SURGICALTYPE", null);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while (rs.next()) {
				specialitybean sp = new specialitybean();
				sp.setPackageheadercode(rs.getString(1));
				sp.setPackageheader(rs.getString(2));
				dataList.add(sp);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return dataList;
	}

	@Override
	public List<Object> getAllpackagesubctaegorydata(String packageheadercode) {
		List<Object> subcodeList = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GET_PACKAGEMASTER_DTLS")
					.registerStoredProcedureParameter("p_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SURGICALTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_ACTIONCODE", "B");
			storedProcedure.setParameter("P_PACKAGEHEADERCODE", packageheadercode.trim());
			storedProcedure.setParameter("P_PACKAGESUBCODE", null);
			storedProcedure.setParameter("P_PROCEDURECODE", null);
			storedProcedure.setParameter("P_SURGICALTYPE", null);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while (rs.next()) {
				specialitybean subsp = new specialitybean();
				subsp.setPackagesubcode(rs.getString(1));
				subsp.setPackagesubheader(rs.getString(2));
				subcodeList.add(subsp);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return subcodeList;
	}

	@Override
	public List<Object> getAllprocedurecodedata(String packagesubcode) {
		List<Object> procedureList = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GET_PACKAGEMASTER_DTLS")
					.registerStoredProcedureParameter("p_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SURGICALTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_ACTIONCODE", "C");
			storedProcedure.setParameter("P_PACKAGEHEADERCODE", null);
			storedProcedure.setParameter("P_PACKAGESUBCODE", packagesubcode.trim());
			storedProcedure.setParameter("P_PROCEDURECODE", null);
			storedProcedure.setParameter("P_SURGICALTYPE", null);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while (rs.next()) {
				specialitybean prosp = new specialitybean();
				prosp.setProcedurecode(rs.getString(1));
				prosp.setSubpackagename(rs.getString(2));
				procedureList.add(prosp);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return procedureList;
	}

	@Override
	public List<Object> getviewspecialitydetails(String packageheadercode, String packagesubcode, String procedurecode,
			Integer searchtype) {
		List<Object> view = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GET_PACKAGEMASTER_DTLS")
					.registerStoredProcedureParameter("p_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SURGICALTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_ACTIONCODE", "D");
			storedProcedure.setParameter("P_PACKAGEHEADERCODE", packageheadercode.trim());
			storedProcedure.setParameter("P_PACKAGESUBCODE", packagesubcode.trim());
			storedProcedure.setParameter("P_PROCEDURECODE", procedurecode.trim());
			storedProcedure.setParameter("P_SURGICALTYPE", searchtype);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while (rs.next()) {
				specialitybean viewlist = new specialitybean();
				viewlist.setPackageheadercode(rs.getString(1) != null ? rs.getString(1) : "N/A");
				viewlist.setPackageheader(rs.getString(2) != null ? rs.getString(2) : "N/A");
				viewlist.setProcedurecode(rs.getString(3) != null ? rs.getString(3) : "N/A");
				viewlist.setSubpackagename(rs.getString(4) != null ? rs.getString(4) : "N/A");
				viewlist.setMandatorypreauth(rs.getString(5) != null ? rs.getString(5) : "N/A");
				viewlist.setMaximumdays(rs.getString(6) != null ? rs.getString(6) : "N/A");
				viewlist.setPackageexceptionflag(rs.getString(7) != null ? rs.getString(7) : "N/A");
				viewlist.setPackageextention(rs.getString(8) != null ? rs.getString(8) : "N/A");
				viewlist.setPriceeditable(rs.getString(9) != null ? rs.getString(9) : "N/A");
				viewlist.setPreauthdocs(rs.getString(10) != null ? rs.getString(10) : "N/A");
				viewlist.setClaimprocesseddocs(rs.getString(11) != null ? rs.getString(11) : "N/A");
				viewlist.setId(rs.getString(12) != null ? rs.getString(12) : "N/A");
				viewlist.setSurgicaltype(rs.getString(13) != null ? rs.getString(13) : "N/A");
				view.add(viewlist);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return view;
	}

	@Override
	public Response savespecilaityRequest(Submitspecialitybean resbean) {
		Response response = new Response();
		String idlist = null;
		String procedurecode = null;
		Integer claimraiseInteger = null;
		StringBuffer bufferlist = new StringBuffer();
		StringBuffer bufferlist1 = new StringBuffer();
		if (resbean.getDataIdArray() != null) {
			for (Long element : resbean.getDataIdArray()) {
				bufferlist.append(element.toString() + ",");
			}
			idlist = bufferlist.substring(0, bufferlist.length() - 1);
		}
		if (resbean.getProcedurecodeArray() != null) {
			for (String element : resbean.getProcedurecodeArray()) {
				bufferlist1.append(element + ",");
			}
			procedurecode = bufferlist1.substring(0, bufferlist1.length() - 1);
		}
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_T_PACKAGE_SURGICAL_MAPPING")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ISSURGICAL", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEDETAILSID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedure.setParameter("P_ACTION", "A");
			storedProcedure.setParameter("P_ISSURGICAL", resbean.getType().trim());
			storedProcedure.setParameter("P_PROCEDURECODE", procedurecode.trim());
			storedProcedure.setParameter("P_PACKAGEDETAILSID", idlist.trim());
			storedProcedure.setParameter("P_USERID", resbean.getUserid());
			storedProcedure.execute();
			claimraiseInteger = (Integer) storedProcedure.getOutputParameterValue("P_MSGOUT");
			if (claimraiseInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Mapped Successfully");
			} else {
				response.setStatus("error");
				response.setMessage("Something Went Wrong");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return response;
	}

	@Override
	public List<Object> getpackagedetailslist(String procedurecode) {
		List<Object> packagelist = new ArrayList<Object>();
		ResultSet packagers = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GET_PACKAGEMASTER_DTLS")
					.registerStoredProcedureParameter("p_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_ACTIONCODE", "E");
			storedProcedure.setParameter("P_PACKAGEHEADERCODE", null);
			storedProcedure.setParameter("P_PACKAGESUBCODE", null);
			storedProcedure.setParameter("P_PROCEDURECODE", procedurecode.trim());
			storedProcedure.execute();
			packagers = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while (packagers.next()) {
				specialitybean pack = new specialitybean();
				pack.setPackageheadercode(packagers.getString(1));
				pack.setPackageheader(packagers.getString(2));
				pack.setPackagesubcode(packagers.getString(3));
				pack.setPackagesubheader(packagers.getString(4));
				pack.setProcedurecode(packagers.getString(5));
				pack.setSubpackagename(packagers.getString(6));
				pack.setProceduredescrption(packagers.getString(7));
				pack.setMandatorypreauth(packagers.getString(8));
				pack.setPackagecategorytype(packagers.getString(9));
				pack.setMaximumdays(packagers.getString(10));
				pack.setStaytype(packagers.getString(11));
				pack.setDaycare(packagers.getString(12));
				pack.setMultyprocedure(packagers.getString(13));
				pack.setPackageunderexception(packagers.getString(14));
				pack.setPackageextention(packagers.getString(15));
				pack.setPriceeditable(packagers.getString(16));
				pack.setPreauthdocs(packagers.getString(17));
				pack.setClaimprocesseddocs(packagers.getString(18));
				pack.setSurgicaltype(packagers.getString(19));
				pack.setId(packagers.getString(20));
				packagelist.add(pack);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (packagers != null)
					packagers.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return packagelist;
	}

	@Override
	public Response activepackageheader(Long headerId, Long userid) {
		Response response = new Response();
		try {
			PackageHeader packageHeader = packageHeaderRepo.findById(headerId).get();
			packageHeader.setDeletedFlag(0);
			packageHeader.setUpdatedBy(userid);
			packageHeader.setUpdatedOn(Calendar.getInstance().getTime());
			packageHeaderRepo.save(packageHeader);
			response.setMessage("Record Successfully Active");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}
}
