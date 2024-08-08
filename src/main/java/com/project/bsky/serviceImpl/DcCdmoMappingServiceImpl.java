package com.project.bsky.serviceImpl;

import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Dccdmomappingbean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.AllowUserForHospitalVisit;
import com.project.bsky.model.DcCdmoMapping;
import com.project.bsky.model.DcCdmoMappingLog;
import com.project.bsky.model.DcGovtHospitalMapping;
import com.project.bsky.model.DcGovtHospitalMappingLog;
import com.project.bsky.model.MobileConfigurationmst;
import com.project.bsky.model.MobileConfigurationmstLog;
import com.project.bsky.model.MobileUserConfiguration;
import com.project.bsky.model.UserDCFaceDetails;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.AllowUserForHospitalVisitRepository;
import com.project.bsky.repository.DcCdmoMappingLogRepository;
import com.project.bsky.repository.DcCdmoMappingRepository;
import com.project.bsky.repository.DcGovtHospitalMappingLogRepository;
import com.project.bsky.repository.DcGovtHospitalMappingRepo;
import com.project.bsky.repository.MobileConfigurationmstLogRepository;
import com.project.bsky.repository.MobileConfigurationmstRepository;
import com.project.bsky.repository.UserDCFaceDetailsRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.DcCdmoMappingSevice;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Service
public class DcCdmoMappingServiceImpl implements DcCdmoMappingSevice {

	@Autowired
	private UserDetailsRepository userrepo;

	@Autowired
	private DcCdmoMappingRepository dcmappingrepo;

	@Autowired
	private DcCdmoMappingLogRepository dcmappinglogrepo;

	@Autowired
	private DcGovtHospitalMappingRepo dcgovthosprepo;

	@Autowired
	private DcGovtHospitalMappingLogRepository dcgovthosplogrepo;

	@Autowired
	private UserDCFaceDetailsRepository userdcfacedetails;
	
	@Autowired
	private AllowUserForHospitalVisitRepository userForHospitalVisitRepo;
	
	@Autowired
	private MobileConfigurationmstRepository mobileconfigurationmstrepo;
	
	@Autowired
	private MobileConfigurationmstLogRepository mobileconfigurationmstlogrepo;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Environment env;
	
	private Connection connection = null;
	private CallableStatement statement = null;

	@Override
	public List<Object> getuserDetailsbygroup(Integer groupid) throws Exception {
		List<Object> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_CDMO_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DC_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GROUPID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1);
			storedProcedureQuery.setParameter("P_GROUPID", groupid);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("userId", rs.getString(1));
				map.put("userName", rs.getString(2));
				map.put("fullName", rs.getString(3) + " (" + rs.getString(2) + ")");
				map.put("mobileNo", rs.getString(4));
				map.put("emailId", rs.getString(5));
				map.put("stateName", rs.getString(6));
				map.put("districtName", rs.getString(7));
				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}

	@Override
	public Response saveDcCdmomapping(Dccdmomappingbean bean) throws Exception {
		Response response = new Response();
		try {
			// its for multiple CDMO
			String[] cdmoid = null;
			if (bean.getCdmoid() != null) {
			    cdmoid = bean.getCdmoid().split(",");
			    List<DcCdmoMapping> list = new ArrayList<>();
			    List<DcCdmoMappingLog> loglist = new ArrayList<>();
			    for (String s : cdmoid) {
			        Integer check = dcmappingrepo.getstatus(Long.parseLong(s), bean.getDcUserId());
			        if (check == null) {
			            DcCdmoMapping dccdmo = new DcCdmoMapping();
			            dccdmo.setCdmouserId(Long.parseLong(s));
			            dccdmo.setDcuserId(bean.getDcUserId());
			            dccdmo.setStatecode(bean.getStatecode());
			            dccdmo.setDistcode(bean.getDistcode());
			            dccdmo.setCreateby(bean.getCreatedby());
			            dccdmo.setCreateon(new Date());
			            dccdmo.setStatusflag(0);
			            list.add(dccdmo);
			        } else if (check == 1) {
			            DcCdmoMapping dccdmo = dcmappingrepo.findrecord(Long.parseLong(s), bean.getDcUserId());
			            loglist = updatedccdmomappinglog(dccdmo.getDcCdmoMappingId(), bean.getCreatedby(), loglist);
			            dccdmo.setStatusflag(0);
			            dccdmo.setStatecode(bean.getStatecode());
			            dccdmo.setDistcode(bean.getDistcode());
			            dccdmo.setUpdateby(bean.getCreatedby());
			            dccdmo.setUpdateon(new Date());
			            list.add(dccdmo);
			        } else {
			            response.setStatus("401");
			            response.setMessage("CDMO Already Tagged");
			            return response;
			        }
			    }
			    if (!loglist.isEmpty()) {
			        dcmappinglogrepo.saveAll(loglist);
			    }
			    dcmappingrepo.saveAll(list);
			    response.setStatus("200");
			    response.setMessage("CDMO Tagged Successfully");
			} else {
			    response.setStatus("400");
			    response.setMessage("Please Select CDMO Name.");
			}


			// for single cdmo
			/*
			 * Integer check = dcmappingrepo.checkdctagged(bean.getDcUserId()); if (check ==
			 * 0) { DcCdmoMapping dccdmo = new DcCdmoMapping();
			 * dccdmo.setCdmouserId(Long.parseLong(bean.getCdmoid()));
			 * dccdmo.setDcuserId(bean.getDcUserId());
			 * dccdmo.setStatecode(bean.getStatecode());
			 * dccdmo.setDistcode(bean.getDistcode());
			 * dccdmo.setCreateby(bean.getCreatedby()); dccdmo.setCreateon(new Date());
			 * dccdmo.setStatusflag(0); dcmappingrepo.save(dccdmo);
			 * response.setStatus("200");
			 * response.setMessage("DC CDMO Tagged Successfully"); } else {
			 * response.setStatus("401"); response.setMessage("DC Already Tagged"); }
			 */
		} catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public List<Object> getdccdmomaplist(Long dcId, Integer group) throws Exception {
		List<Object> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_CDMO_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DC_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GROUPID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 2);
			storedProcedureQuery.setParameter("P_DC_ID", dcId);
			storedProcedureQuery.setParameter("P_GROUPID", group);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("dcuserid", rs.getString(1));
				map.put("dcname", rs.getString(2));
				map.put("cdmouserid", rs.getString(3));
				map.put("cdmoname", rs.getString(4));
				map.put("createdby", rs.getString(5));
				map.put("createdon", rs.getString(6));
				map.put("statecode", rs.getString(7) != null ? rs.getString(7) : "");
				map.put("state", rs.getString(8));
				map.put("districtcode", rs.getString(9) != null ? rs.getString(9) : "");
				map.put("district", rs.getString(10));
				map.put("username", rs.getString(11));
				map.put("phoneno", rs.getString(12));
				map.put("groupname", rs.getString(13));
				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}

	@Override
	public List<Object> getmapingbydcid(Long dcId) throws Exception {
		List<Object> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_CDMO_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DC_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GROUPID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 3);
			storedProcedureQuery.setParameter("P_DC_ID", dcId);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("userId", rs.getString(1));
				map.put("userName", rs.getString(2));
				map.put("fullName", rs.getString(3) + " (" + rs.getString(2) + ")");
				map.put("mobileNo", rs.getString(4));
				map.put("emailId", rs.getString(5));
				map.put("stateName", rs.getString(6));
				map.put("districtName", rs.getString(7));
				map.put("statecode", rs.getString(8) != null ? rs.getString(8) : "");
				map.put("districtcode", rs.getString(9) != null ? rs.getString(9) : "");
				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}

	@Override
	public Response updateDcCdmomapping(Dccdmomappingbean bean) throws Exception {
		Response response = new Response();
		try {
			String[] cdmoid = null;
			List<DcCdmoMapping> list = new ArrayList<>();
			List<DcCdmoMappingLog> loglist = new ArrayList<>();

			if (bean.getCdmoid() != null && !bean.getCdmoid().equals("")) {
			    cdmoid = bean.getCdmoid().split(",");
			    for (String cdmo : cdmoid) {
			        DcCdmoMapping dcmapping = dcmappingrepo.findrecord(Long.parseLong(cdmo), bean.getDcUserId());
			        if (dcmapping != null) {
//			            if (dcmapping.getStatusflag() == 1) {
			                loglist = updatedccdmomappinglog(dcmapping.getDcCdmoMappingId(), bean.getCreatedby(), loglist);
			                dcmapping.setStatusflag(0);
			                dcmapping.setStatecode(bean.getStatecode());
			                dcmapping.setDistcode(bean.getDistcode());
			                dcmapping.setUpdateby(bean.getCreatedby());
			                dcmapping.setUpdateon(new Date());
			                list.add(dcmapping);
//			            }
			        } else {
			            DcCdmoMapping dccdmo = new DcCdmoMapping();
			            dccdmo.setCdmouserId(Long.parseLong(cdmo));
			            dccdmo.setDcuserId(bean.getDcUserId());
			            dccdmo.setStatecode(bean.getStatecode());
			            dccdmo.setDistcode(bean.getDistcode());
			            dccdmo.setCreateby(bean.getCreatedby());
			            dccdmo.setCreateon(new Date());
			            dccdmo.setStatusflag(0);
			            list.add(dccdmo);
			        }
			    }
			    List<DcCdmoMapping> maplist = dcmappingrepo.findBydcuserId(bean.getDcUserId());
			    for (DcCdmoMapping dcmapping : maplist) {
			        Boolean b = cheakcdmoexistornot(dcmapping.getCdmouserId(), cdmoid);
			        if (!b) {
			            loglist = updatedccdmomappinglog(dcmapping.getDcCdmoMappingId(), bean.getCreatedby(), loglist);
			            dcmapping.setStatusflag(1);
			            dcmapping.setUpdateby(bean.getCreatedby());
			            dcmapping.setUpdateon(new Date());
			            list.add(dcmapping);
			        }
			    }
			} else {
			    List<DcCdmoMapping> maplist = dcmappingrepo.findBydcuserId(bean.getDcUserId());
			    for (DcCdmoMapping dcmapping : maplist) {
			        loglist = updatedccdmomappinglog(dcmapping.getDcCdmoMappingId(), bean.getCreatedby(), loglist);
			        dcmapping.setStatusflag(1);
			        dcmapping.setUpdateby(bean.getCreatedby());
			        dcmapping.setUpdateon(new Date());
			        list.add(dcmapping);
			    }
			}
			dcmappinglogrepo.saveAll(loglist);
			dcmappingrepo.saveAll(list);
			response.setStatus("200");
			response.setMessage("CDMO Tagged Updated Successfully");

			/*
			 * List<DcCdmoMappingLog> loglist=new ArrayList<>(); List<DcCdmoMapping>
			 * list=new ArrayList<>(); if(bean.getCdmoid()!=null && bean.getCdmoid()!="") {
			 * DcCdmoMapping
			 * dcmapping=dcmappingrepo.findBydcuserIdrecord(bean.getDcUserId());
			 * if(dcmapping.getCdmouserId()==Long.parseLong(bean.getCdmoid())) {
			 * loglist=updatedccdmomappinglog(dcmapping.getDcCdmoMappingId(),dcmapping.
			 * getDcuserId(),loglist); dcmapping.setStatecode(bean.getStatecode());
			 * dcmapping.setDistcode(bean.getDistcode());
			 * dcmapping.setUpdateby(bean.getCreatedby()); dcmapping.setUpdateon(new
			 * Date()); list.add(dcmapping); }else {
			 * loglist=updatedccdmomappinglog(dcmapping.getDcCdmoMappingId(),dcmapping.
			 * getDcuserId(),loglist); dcmapping.setStatusflag(1);
			 * dcmapping.setUpdateby(bean.getCreatedby()); dcmapping.setUpdateon(new
			 * Date()); list.add(dcmapping);
			 * 
			 * DcCdmoMapping
			 * dcmapping1=dcmappingrepo.findrecord(Long.parseLong(bean.getCdmoid()),bean.
			 * getDcUserId()); if(dcmapping1!=null) {
			 * loglist=updatedccdmomappinglog(dcmapping.getDcCdmoMappingId(),dcmapping.
			 * getDcuserId(),loglist); dcmapping1.setStatusflag(0);
			 * dcmapping1.setStatecode(bean.getStatecode());
			 * dcmapping1.setDistcode(bean.getDistcode());
			 * dcmapping1.setUpdateby(bean.getCreatedby()); dcmapping1.setUpdateon(new
			 * Date()); list.add(dcmapping1); }else { DcCdmoMapping dccdmo=new
			 * DcCdmoMapping(); dccdmo.setCdmouserId(Long.parseLong(bean.getCdmoid()));
			 * dccdmo.setDcuserId(bean.getDcUserId());
			 * dccdmo.setStatecode(bean.getStatecode());
			 * dccdmo.setDistcode(bean.getDistcode());
			 * dccdmo.setCreateby(bean.getCreatedby()); dccdmo.setCreateon(new Date());
			 * dccdmo.setStatusflag(0); list.add(dccdmo); } }
			 * dcmappinglogrepo.saveAll(loglist); dcmappingrepo.saveAll(list);
			 * response.setMessage("CDMO Tagged Updated Successfully"); }else {
			 * List<DcCdmoMapping> maplist=dcmappingrepo.findBydcuserId(bean.getDcUserId());
			 * for(DcCdmoMapping dcmapping:maplist) {
			 * loglist=updatedccdmomappinglog(dcmapping.getDcCdmoMappingId(),dcmapping.
			 * getDcuserId(),loglist); dcmapping.setStatusflag(1);
			 * dcmapping.setUpdateby(bean.getCreatedby()); dcmapping.setUpdateon(new
			 * Date()); list.add(dcmapping); } dcmappinglogrepo.saveAll(loglist);
			 * dcmappingrepo.saveAll(list);
			 * response.setMessage("CDMO UnTagged Successfully"); }
			 * response.setStatus("200");
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return response;
	}

	private Boolean cheakcdmoexistornot(Long cdmouserId, String[] cdmoid) throws Exception {
		Boolean stat = false;
		try {
			for (String s : cdmoid) {
				if (Long.parseLong(s) == cdmouserId) {
					stat = true;
				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return stat;
	}

	private List<DcCdmoMappingLog> updatedccdmomappinglog(Long mappingid, Long createdby,
			List<DcCdmoMappingLog> loglist) throws Exception {
		try {
			DcCdmoMapping dcmapping = dcmappingrepo.findById(mappingid).get();
			DcCdmoMappingLog logmap = new DcCdmoMappingLog();
			logmap.setDcuserId(dcmapping.getDcuserId());
			logmap.setCdmouserId(dcmapping.getCdmouserId());
			logmap.setCreateby(createdby);
			logmap.setUpdateby(dcmapping.getUpdateby());
			logmap.setCreateon(new Date());
			logmap.setUpdateon(dcmapping.getUpdateon());
			logmap.setStatusflag(dcmapping.getStatusflag());
			logmap.setDcImage(dcmapping.getDcImage());
			logmap.setDcImageLatitude(dcmapping.getDcImageLatitude());
			logmap.setDcImageLongitude(dcmapping.getDcImageLongitude());
			logmap.setDcImageDate(dcmapping.getDcImageDate());
			logmap.setStatecode(dcmapping.getStatecode());
			logmap.setDistcode(dcmapping.getDistcode());
			loglist.add(logmap);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return loglist;
	}

	@Override
	public List<Object> getdccdmomapcount(Long dcId, Integer group) throws Exception {
		List<Object> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_CDMO_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DC_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GROUPID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 4);
			storedProcedureQuery.setParameter("P_DC_ID", dcId);
			storedProcedureQuery.setParameter("P_GROUPID", group);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("dcuserid", rs.getString(1));
				map.put("dcname", rs.getString(2));				
				map.put("statecode", rs.getString(3) != null ? rs.getString(3) : "");
				map.put("state", rs.getString(4));
				map.put("districtcode", rs.getString(5) != null ? rs.getString(5) : "");
				map.put("district", rs.getString(6));
				map.put("groupid", rs.getString(7));
				map.put("groupname", rs.getString(8));
				map.put("mapcount", rs.getString(9));
				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}

	@Override
	public List<Object> taggedlogdetails(Long dcId) throws Exception {
		List<Object> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_CDMO_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DC_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GROUPID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 5);
			storedProcedureQuery.setParameter("P_DC_ID", dcId);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("dcuserid", rs.getString(1));
				map.put("dcname", rs.getString(2));
				map.put("cdmouserid", rs.getString(3));
				map.put("cdmoname", rs.getString(4));
				map.put("createdby", rs.getString(5));
				map.put("createdon", rs.getString(6));
				map.put("updatedby", rs.getString(7));
				map.put("updatedon", rs.getString(8));
				map.put("status", rs.getString(9));
				map.put("statusflag", rs.getString(10));
				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}

	@Override
	public Response saveDcHospitalmapping(Dccdmomappingbean bean) throws Exception {
		Response response = new Response();
		try {
			String[] hospital = null;
			if (bean.getHospitalCode() != null) {
				hospital = bean.getHospitalCode().split(",");
				List<DcGovtHospitalMapping> list = new ArrayList<>();
				List<DcGovtHospitalMappingLog> loglist = new ArrayList<>();
				for (String s : hospital) {
					Integer status = dcgovthosprepo.getstatus(Long.parseLong(s), bean.getDcUserId());
					if (status == null) {
						DcGovtHospitalMapping dccdmo = new DcGovtHospitalMapping();
						dccdmo.setHospitalId(Long.parseLong(s));
						dccdmo.setDcuserId(bean.getDcUserId());
						dccdmo.setCreateby(bean.getCreatedby());
						dccdmo.setCreateon(new Date());
						dccdmo.setStatusflag(0);
						list.add(dccdmo);
					} else if (status == 1) {
						DcGovtHospitalMapping dccdmo = dcgovthosprepo.getrecord(Long.parseLong(s), bean.getDcUserId());
						loglist = updatedchospitalmappinglog(dccdmo.getDcHospMappingId(), bean.getCreatedby(), loglist);
						dccdmo.setStatusflag(0);
						dccdmo.setUpdateby(bean.getCreatedby());
						dccdmo.setUpdateon(new Date());
						list.add(dccdmo);
					} else {
						response.setStatus("401");
						response.setMessage("Hospital Already Tagged");
						return response;
					}
				}
				if (!loglist.isEmpty()) {
					dcgovthosplogrepo.saveAll(loglist);
				}
				dcgovthosprepo.saveAll(list);
				response.setStatus("200");
				response.setMessage("Hospital Tagged Successfully");
			} else {
				response.setStatus("400");
				response.setMessage("Please Select Hospital Name.");
			}

		} catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public List<Object> getdcgovthospmapcount(Long dcId, Integer group) throws Exception {
		List<Object> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_GOVT_HOSPITAL_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DC_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GROUPID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1);
			storedProcedureQuery.setParameter("P_DC_ID", dcId);
			storedProcedureQuery.setParameter("P_GROUPID", group);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("dcuserid", rs.getString(1));
				map.put("dcname", rs.getString(2));
				map.put("grouptype", rs.getString(3));
				map.put("groupname", rs.getString(4));
				map.put("mapcount", rs.getString(5));
				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}

	@Override
	public List<Object> getgovthospbydcid(Long dcId, Integer group) throws Exception {
		List<Object> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_GOVT_HOSPITAL_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DC_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GROUPID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 2);
			storedProcedureQuery.setParameter("P_DC_ID", dcId);
			storedProcedureQuery.setParameter("P_GROUPID", group);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("dcuserid", rs.getString(1));
				map.put("dcname", rs.getString(2));
				map.put("hospitalid", rs.getString(3));
				map.put("hospitalname", rs.getString(4));
				map.put("statecode", rs.getString(5));
				map.put("statename", rs.getString(6));
				map.put("distcode", rs.getString(7));
				map.put("distname", rs.getString(8));
				map.put("createdby", rs.getString(9));
				map.put("createdon", rs.getString(10));
				map.put("username", rs.getString(11));
				map.put("phoneno", rs.getString(12));
				map.put("grouptypename", rs.getString(13));
				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}

	@Override
	public List<Object> taggedHOSDClogdetails(Long dcId) throws Exception {
		List<Object> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_GOVT_HOSPITAL_MAPPING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DC_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GROUPID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 3);
			storedProcedureQuery.setParameter("P_DC_ID", dcId);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("dcuserid", rs.getString(1));
				map.put("dcname", rs.getString(2));
				map.put("hospitalid", rs.getString(3));
				map.put("hospitalname", rs.getString(4));
				map.put("statecode", rs.getString(5));
				map.put("statename", rs.getString(6));
				map.put("distcode", rs.getString(7));
				map.put("distname", rs.getString(8));
				map.put("createdby", rs.getString(9));
				map.put("createdon", rs.getString(10));
				map.put("updatedby", rs.getString(11));
				map.put("updatedon", rs.getString(12));
				map.put("status", rs.getString(13));
				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}

	@Override
	public Response updateDcHospitalmapping(Dccdmomappingbean bean) throws Exception {
		Response response = new Response();
		try {
			String[] hospital = null;
			List<DcGovtHospitalMapping> list = new ArrayList<>();
			List<DcGovtHospitalMappingLog> loglist = new ArrayList<>();
			if (bean.getHospitalCode() != null && !bean.getHospitalCode().equals("")) {
				hospital = bean.getHospitalCode().split(",");
				for (String hosp : hospital) {
					DcGovtHospitalMapping dcmapping = dcgovthosprepo.findrecord(Long.parseLong(hosp),
							bean.getDcUserId());
					if (dcmapping != null) {
						if (dcmapping.getStatusflag() == 1) {
							loglist = updatedchospitalmappinglog(dcmapping.getDcHospMappingId(),
									bean.getCreatedby(), loglist);
							dcmapping.setStatusflag(0);
							dcmapping.setUpdateby(bean.getCreatedby());
							dcmapping.setUpdateon(new Date());
							list.add(dcmapping);
						}
					} else {
						DcGovtHospitalMapping dccdmo = new DcGovtHospitalMapping();
						dccdmo.setHospitalId(Long.parseLong(hosp));
						dccdmo.setDcuserId(bean.getDcUserId());
						dccdmo.setCreateby(bean.getCreatedby());
						dccdmo.setCreateon(new Date());
						dccdmo.setStatusflag(0);
						list.add(dccdmo);
					}
				}
				List<DcGovtHospitalMapping> maplist = dcgovthosprepo.findBydcuserId(bean.getDcUserId());
				for (DcGovtHospitalMapping dcmapping : maplist) {
					Boolean b = cheakhospexistornot(dcmapping.getHospitalId(), hospital);
					if (b == false) {
						loglist = updatedchospitalmappinglog(dcmapping.getDcHospMappingId(), bean.getCreatedby(),
								loglist);
						dcmapping.setStatusflag(1);
						dcmapping.setUpdateby(bean.getCreatedby());
						dcmapping.setUpdateon(new Date());
						list.add(dcmapping);
					}
				}
			} else {
				List<DcGovtHospitalMapping> maplist = dcgovthosprepo.findBydcuserId(bean.getDcUserId());
				for (DcGovtHospitalMapping dcmapping : maplist) {
					loglist = updatedchospitalmappinglog(dcmapping.getDcHospMappingId(), bean.getCreatedby(),
							loglist);
					dcmapping.setStatusflag(1);
					dcmapping.setUpdateby(bean.getCreatedby());
					dcmapping.setUpdateon(new Date());
					list.add(dcmapping);
				}
			}
			dcgovthosplogrepo.saveAll(loglist);
			dcgovthosprepo.saveAll(list);
			response.setStatus("200");
			response.setMessage("Hospital Tagged Updated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return response;
	}

	private Boolean cheakhospexistornot(Long chospitalId, String[] hospital) throws Exception {
		Boolean stat = false;
		try {
			for (String s : hospital) {
				if (Long.parseLong(s) == chospitalId) {
					stat = true;
				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return stat;
	}

	private List<DcGovtHospitalMappingLog> updatedchospitalmappinglog(Long mappingid, Long createdby,
			List<DcGovtHospitalMappingLog> loglist) throws Exception {
		try {
			DcGovtHospitalMapping dcmapping = dcgovthosprepo.findById(mappingid).get();
			DcGovtHospitalMappingLog logmap = new DcGovtHospitalMappingLog();
			logmap.setDcHospMappingId(dcmapping.getDcHospMappingId());
			logmap.setDcuserId(dcmapping.getDcuserId());
			logmap.setHospitalId(dcmapping.getHospitalId());
			logmap.setCreateby(createdby);
			logmap.setUpdateby(dcmapping.getUpdateby());
			logmap.setCreateon(new Date());
			logmap.setUpdateon(dcmapping.getUpdateon());
			logmap.setStatusflag(dcmapping.getStatusflag());
			logmap.setDcImage(dcmapping.getDcImage());
			logmap.setDcImageLatitude(dcmapping.getDcImageLatitude());
			logmap.setDcImageLongitude(dcmapping.getDcImageLongitude());
			logmap.setDcImageDate(dcmapping.getDcImageDate());
			loglist.add(logmap);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return loglist;
	}

	@Override
	public List<Object> getdcfacelist(Long dcId,Integer group) throws Exception {
		Map<String, Object> details = null;
		List<Object> dcfacelist = new ArrayList<>();
		try {
			List<Object[]> objlist = userdcfacedetails.getdcfacelist(dcId,group);
			for (Object[] obj : objlist) {
				details = new HashMap<>();
				details.put("faceid", obj[0]);
				details.put("dcId", obj[1]);
				details.put("username", obj[2]);
				details.put("fullname", obj[3]);
				details.put("mobileno", obj[4]);
				details.put("rgistration", obj[5]);
				dcfacelist.add(details);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return dcfacelist;
	}

	@Override
	public Map<String, Object> getdctaggeddetails(Long dcid) throws Exception {
		Map<String, Object> details = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		List<Object> hosplist = new ArrayList<>();
		List<Object> cdmolist = new ArrayList<>();
		List<Object> refhosplist = new ArrayList<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_TAGGED_DETAILS")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DC_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_CDMO", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_REF_HOSPITAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 1);
			storedProcedureQuery.setParameter("P_DC_USER_ID", dcid);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_HOSPITAL");
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_CDMO");
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_REF_HOSPITAL");

			while (rs.next()) {
				details = new HashMap<>();
				details.put("userId", rs.getString(1));
				details.put("hospitalCode", rs.getString(2));
				details.put("hospitalName", rs.getString(3) + " (" + rs.getString(2) + ")");
				details.put("mobileno", rs.getString(4));
				details.put("email", rs.getString(5));
				details.put("category", rs.getString(6));
				details.put("lattitude", rs.getString(7) != null ? rs.getString(7) : "N/A");
				details.put("longitude", rs.getString(8) != null ? rs.getString(8) : "N/A");
				details.put("state", rs.getString(9));
				details.put("district", rs.getString(10));
				hosplist.add(details);
			}

			while (rs1.next()) {
				details = new HashMap<>();
				details.put("userId", rs1.getString(1));
				details.put("username", rs1.getString(2));
				details.put("fullname", rs1.getString(3));
				details.put("mobileno", rs1.getString(4));
				details.put("email", rs1.getString(5));
				details.put("dcimage", rs1.getString(6));
				details.put("lattitude", rs1.getString(7) != null ? rs1.getString(7) : "N/A");
				details.put("longitude", rs1.getString(8) != null ? rs1.getString(8) : "N/A");
				cdmolist.add(details);
			}

			while (rs2.next()) {
				details = new HashMap<>();
				details.put("userId", rs2.getString(1));
				details.put("hospitalName", rs2.getString(2));
				details.put("mobileno", rs2.getString(3));
				details.put("address", rs2.getString(4));
				details.put("dcimage", rs2.getString(5));
				details.put("lattitude", rs2.getString(6) != null ? rs2.getString(6) : "N/A");
				details.put("longitude", rs2.getString(7) != null ? rs2.getString(7) : "N/A");
				details.put("state", rs2.getString(8));
				details.put("district", rs2.getString(9));
				refhosplist.add(details);
			}

			details = new HashMap<>();
			details.put("hosplist", hosplist);
			details.put("cdmolist", cdmolist);
			details.put("refhosplist", refhosplist);

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return details;
	}

	@Override
	public Map<String, Object> updatedcfacelist(Long faceid, Long userid) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
			UserDCFaceDetails dcfacedata = userdcfacedetails.findById(faceid).get();
			if (dcfacedata != null) {
				dcfacedata.setStatusflag(1);
				dcfacedata.setUpdatedby(userid);
				dcfacedata.setUpdatedon(new Date());
				userdcfacedetails.save(dcfacedata);
				map.put("status", HttpStatus.OK.value());
				map.put("message", "Success");
			} else {
				map.put("status", HttpStatus.BAD_REQUEST.value());
				map.put("error", "record not found");
				map.put("message", "Error");
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return map;
	}

	@Override
	public List<Object> getfacelogdetails(Long dcid, Integer group) throws Exception {
		Map<String, Object> details = null;
		List<Object> dcfacelist = new ArrayList<>();
		try {
			List<Object[]> objlist = userdcfacedetails.getlogdtails(dcid,group);
			for (Object[] obj : objlist) {
				details = new HashMap<>();
				details.put("faceid", obj[0]);
				details.put("dcId", obj[1]);
				details.put("username", obj[2]);
				details.put("fullname", obj[3]);
				details.put("mobileno", obj[4]);
				details.put("createdon", obj[5]);
				details.put("updatedon", obj[6]);
				details.put("updatedby", obj[7]);
				dcfacelist.add(details);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return dcfacelist;
	}

	@Override
	public List<Object> allowhospitalmobileactivitylist() throws Exception {
		Map<String, Object> details = null;
		List<Object> dcfacelist = new ArrayList<>();
		try {
			List<Object[]> objlist = userForHospitalVisitRepo.allowhospitalmobileactivitylist();
			for (Object[] obj : objlist) {
				details = new HashMap<>();
				details.put("groupid", obj[0]);
				details.put("groupname", obj[1]);
				details.put("visitstatus",obj[2]);
				details.put("attendancestatus", obj[3]);
				details.put("createby", "");
				dcfacelist.add(details);
			}			
		} catch (Exception e) {
			throw new Exception(e);
		}
		return dcfacelist;
	}

	@Override
	public Map<String, Object> allowhospitalmobileactivitylist(List<AllowUserForHospitalVisit> list) throws Exception {
		Map<String, Object> details = new HashMap<>();
		try {
			List<AllowUserForHospitalVisit> arrlist=new ArrayList<>();
			for(AllowUserForHospitalVisit user:list) {
				Integer count=userForHospitalVisitRepo.checkduplicate(user.getGroupid());
				if(count==0) {
					user.setStatusflag(0);
					user.setCreateon(new Date());
					arrlist.add(user);
				}else {
					AllowUserForHospitalVisit allowuser=userForHospitalVisitRepo.getexistrecord(user.getGroupid());
					allowuser.setStatusflag(0);
					allowuser.setUpdateby(user.getCreateby());
					allowuser.setUpdateon(new Date());
					allowuser.setVisitstatus(user.getVisitstatus());
					allowuser.setAttendancestatus(user.getAttendancestatus());
					arrlist.add(allowuser);
				}
			}	
			userForHospitalVisitRepo.saveAll(arrlist);
			details.put("status", 200);
			details.put("message","Success");
		}catch (Exception e) {
			throw new Exception(e);
		}
		return details;
	}

	@Override
	public List<Object> getconfigGroupList() throws Exception {
		List<Object> list=new ArrayList<>();
		try {
			List<Object[]> objlist=mobileconfigurationmstrepo.getconfigGroupList();
				for(Object[] obj:objlist) {
					Map<String,Object> map=new HashMap<>();
					map.put("typeId", obj[0]);
					map.put("groupTypeName", obj[1]);
					map.put("status", obj[2]);
					map.put("tempstatus", obj[2]);
					list.add(map);
				}			
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public Map<String, Object> savegroupmobilemast(List<MobileConfigurationmst> list) throws Exception {
		Map<String, Object> details = new HashMap<>();
		List<MobileConfigurationmst> list2=new ArrayList<>();
		List<MobileConfigurationmstLog> loglist=new ArrayList<>();
		try {
			for(MobileConfigurationmst configobj:list) {
				MobileConfigurationmst configid=mobileconfigurationmstrepo.getconfigid(configobj.getGroupid());
				if(configid!=null) {
					MobileConfigurationmstLog log=new MobileConfigurationmstLog();
					log.setConfigid(configid.getConfigid());
					log.setAttendancestatus(configid.getAttendancestatus());
					log.setGroupid(configid.getGroupid());
					log.setCreateby(configobj.getCreateby());
					log.setCreateon(new Date());
					log.setUpdateby(configid.getUpdateby());
					log.setUpdateon(configid.getUpdateon());
					log.setStatusflag(configid.getStatusflag());
					loglist.add(log);
					
					configid.setAttendancestatus(configobj.getAttendancestatus());
					configid.setUpdateby(configobj.getCreateby());
					configid.setUpdateon(new Date());
					list2.add(configid);
				}else {
					configobj.setStatusflag(0);
					configobj.setCreateon(new Date());
					list2.add(configobj);
				}
			}
			mobileconfigurationmstlogrepo.saveAll(loglist);
			mobileconfigurationmstrepo.saveAll(list2);
			details.put("status", 200);
			details.put("message", "success");
		}catch (Exception e) {
			throw new Exception(e);
		}
		return details;
	}

	@Override
	public List<Object> getconfiggroupdata(Long userId) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs =null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MOBILE_ATTENDANCE_USER_CONFIGURATION_VIEW")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1);
			storedProcedureQuery.setParameter("P_USER_ID", userId);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("configid", rs.getString(1));
				map.put("groupid", rs.getString(2));
				map.put("groupname", rs.getString(3));
				map.put("status", rs.getInt(4));
				map.put("tempstatus",rs.getInt(4));
				list.add(map);
			}		
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}
	
	@Override
	public List<Object> getconfiggroupalldata() throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs =null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MOBILE_ATTENDANCE_USER_CONFIGURATION_VIEW")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 2);
			storedProcedureQuery.setParameter("P_USER_ID", null);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("configid", rs.getString(1));
				map.put("groupid", rs.getString(2));
				map.put("groupname", rs.getString(3));
				list.add(map);
			}		
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}
	
	private void getconnection() throws Exception {				
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public Map<String, Object> saveusermobileconfig(List<MobileUserConfiguration> list) throws Exception {
		Map<String, Object> details=new HashMap<>();
	try {
		StructDescriptor structDescriptor;
        
        getconnection();		
		
		STRUCT[] objbean = new STRUCT[list.size()];
		int i=0;
		for(MobileUserConfiguration bean:list){
			structDescriptor = new StructDescriptor("MOB_ATT_USER_CONFIG_T", connection);
			 Object[] ObjArr = {
					 bean.getUserid(),
					 bean.getGroupid(),
					 bean.getCreateby(),
					 bean.getStatusflag()
	            };
			 objbean[i] = new STRUCT(structDescriptor, connection, ObjArr);
	            i++;
		}
		ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_MOB_ATT_USER_CONFIG", connection);
		ARRAY array_to_pass = new ARRAY(des, connection, objbean);
		
		statement = connection.prepareCall("call USP_MOBILE_ATTENDANCE_USER_CONFIGURATION_SUBMIT(?,?,?,?)");
		statement.setInt(1, 1);//P_ACTION_CODE
		statement.setInt(2, 0);//GROUP ID 0 DEFAULT
		statement.setArray(3, array_to_pass);//P_MOB_ATT_USER
		statement.registerOutParameter(4, Types.INTEGER);//P_MSGOUT
		statement.execute();
		Integer out = statement.getInt(4);
		if(out==1) {
			details.put("status", 200);
			details.put("message", "success");
		}else {
			details.put("status", 400);
			details.put("message", "error in database");
		}		
	}catch (Exception e) {
		e.printStackTrace();
		throw new Exception(e);
	}
	return details;
	}

	@Override
	public List<Object> getusermobileconfiglist(Long userId) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs =null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MOBILE_ATTENDANCE_USER_CONFIGURATION_VIEW")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 3);
			storedProcedureQuery.setParameter("P_USER_ID", userId);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("userId", rs.getString(1));
				map.put("fullName", rs.getString(2));
				map.put("groupName", rs.getString(3));
				map.put("ateeendanceid", rs.getString(4));
				map.put("attendanceLocation",rs.getString(5));
				map.put("taggedBy",rs.getString(6));
				map.put("taggedOn",rs.getString(7));
				list.add(map);
			}		
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> savegroupwisemobileconfig(List<MobileUserConfiguration> list) throws Exception {
		Map<String, Object> details=new HashMap<>();
		try {
			System.out.println(list);
			StructDescriptor structDescriptor;
	        
	        getconnection();	
	        Long groupid=0l;
			
			STRUCT[] objbean = new STRUCT[list.size()];
			int i=0;
			for(MobileUserConfiguration bean:list){
				groupid=bean.getUserid();
				structDescriptor = new StructDescriptor("MOB_ATT_USER_CONFIG_T", connection);
				 Object[] ObjArr = {						 
						 bean.getUserid(),
						 bean.getGroupid(),
						 bean.getCreateby(),
						 bean.getStatusflag()
		            };
				 objbean[i] = new STRUCT(structDescriptor, connection, ObjArr);
		            i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_MOB_ATT_USER_CONFIG", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, objbean);
			
			statement = connection.prepareCall("call USP_MOBILE_ATTENDANCE_USER_CONFIGURATION_SUBMIT(?,?,?,?)");
			statement.setInt(1, 2);//P_ACTION_CODE
			statement.setLong(2, groupid);//GROUP ID 0 DEFAULT
			statement.setArray(3, array_to_pass);//P_MOB_ATT_USER
			statement.registerOutParameter(4, Types.INTEGER);//P_MSGOUT
			statement.execute();
			Integer out = statement.getInt(4);
			if(out==1) {
				details.put("status", 200);
				details.put("message", "success");
			}else {
				details.put("status", 400);
				details.put("message", "error in database");
			}		
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return details;
	}

	

}
