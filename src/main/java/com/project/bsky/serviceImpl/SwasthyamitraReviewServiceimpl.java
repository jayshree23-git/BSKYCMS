/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.service.SwasthyamitraReviewService;
import com.project.bsky.util.CommonFileUpload;

/**
 * Rajendra Sahoo
 */
@Service
public class SwasthyamitraReviewServiceimpl implements SwasthyamitraReviewService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private JdbcTemplate jdbctemplet;

	@Override
	public List<Object> getsmhelpdeskregister(Date formdate, Date todate, String statecode, String distcode,
			String hospitalCode, Long smid, Integer status, Long userId) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SWASTHYAMITRA_HELP_REG_RPT")
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUS_VALUE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SWASTHYAMITRA_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)					
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROM_DATE", formdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_STATE_CODE",statecode);
			storedProcedureQuery.setParameter("P_DIST_CODE", distcode);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedureQuery.setParameter("P_STATUS_VALUE", status);
			storedProcedureQuery.setParameter("P_SWASTHYAMITRA_ID", smid);
			storedProcedureQuery.setParameter("P_USER_ID", userId);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String, Object> map=new HashMap<>();
				map.put("enterBy",rs.getString(1));
				map.put("status", rs.getString(2));
				map.put("patientName", rs.getString(3));
				map.put("actionon", rs.getString(4));
				map.put("blockingDate", rs.getString(5));
				map.put("urn", rs.getString(6));
				map.put("hospitalName", rs.getString(7));
				map.put("address", rs.getString(8));
				map.put("phoneNo", rs.getString(9));
				map.put("refferornot", rs.getString(10));
				map.put("oopeatpreblock", rs.getString(11));
				map.put("oopeatblock", rs.getString(12));
				map.put("admissionphoto", rs.getString(13));
				map.put("dischargedate", rs.getString(14));
				map.put("sliprecieved", rs.getString(15));
				map.put("oopeduringtreatment", rs.getString(16));
				map.put("oopeduringdischarge", rs.getString(17));
				map.put("death", rs.getString(18));
				map.put("othergrievance", rs.getString(19));
				map.put("dischargephoto", rs.getString(20));
				map.put("successstory", rs.getString(21));
				map.put("hospitalcode", rs.getString(22));
				map.put("admissionremark", rs.getString(23)!=null?rs.getString(23):"N/A");
				map.put("dischargeremark", rs.getString(24)!=null?rs.getString(24):"N/A");
				list.add(map);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public void downloadsmreviewdoc(String fileName, String year, String hospitalcode, HttpServletResponse response) {
		try {
			CommonFileUpload.downloadsmreviewdoc(fileName, year, response, hospitalcode);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public List<Object> getsmpendingreport(Date formdate, Date todate, String statecode, String distcode,
			String hospitalCode, Long userId) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SWASTHYAMITRA_PENDING_RPT")
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROM_DATE", formdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_STATE_CODE",statecode);
			storedProcedureQuery.setParameter("P_DIST_CODE", distcode);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedureQuery.setParameter("P_USERID", userId);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String, Object> map=new HashMap<>();
				map.put("statename",rs.getString(1));
				map.put("district", rs.getString(2));
				map.put("hospital", rs.getString(4));
				map.put("totalblock", rs.getString(5));
				map.put("actiontaken", rs.getString(6));
				map.put("pending", rs.getString(7));
				list.add(map);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public List<Object> getsmlistforscoring(Integer year, Integer month, String statecode, String distcode,
			String hospitalCode, Long smid, Long userId) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SWASTHYAMITRA_SCORING_LIST")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SWASTHYAMITRA_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_HOSPITAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 1);
			storedProcedureQuery.setParameter("P_STATE_CODE",statecode);
			storedProcedureQuery.setParameter("P_DIST_CODE", distcode);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_YEAR", year);
			storedProcedureQuery.setParameter("P_MONTH", month);
			storedProcedureQuery.setParameter("P_SWASTHYAMITRA_ID", smid);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String, Object> map=new HashMap<>();
				map.put("smuserid",rs.getString(1));
				map.put("smusername", rs.getString(2));
				map.put("fullname", rs.getString(3));
				map.put("phoneNO", rs.getString(4));
				map.put("email", rs.getString(5));
				map.put("dateofjoin", rs.getString(6));
				list.add(map);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}
	
	@Override
	public Map<String, Object> getsmdetailsforscoring(Long smid, String year, String month) throws Exception {
		List<Object> list=new ArrayList<>();
		Map<String, Object> map=new HashMap<>();
		ResultSet rs=null;
		ResultSet rs1=null;
		Integer noofdays = 26;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SWASTHYAMITRA_SCORING_LIST")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SWASTHYAMITRA_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_HOSPITAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 2);
			storedProcedureQuery.setParameter("P_USERID", smid);
			storedProcedureQuery.setParameter("P_SWASTHYAMITRA_ID", smid);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_HOSPITAL");
			if (rs.next()) {
				map.put("smuserid",rs.getString(1));
				map.put("fullname", rs.getString(2));
				map.put("phoneNO", rs.getString(3));
				map.put("snaremark", rs.getString(4));
				map.put("snascore", rs.getString(6)!=null?rs.getString(6):"N/A");
				map.put("dcremark", rs.getString(5));
				map.put("dcscore", rs.getString(7)!=null?rs.getString(7):"N/A");
				map.put("dateofjoin", rs.getString(8));		
				Integer count=smpresentdays(rs.getString(1),year.toString(),month.toString());
				map.put("dutydays", noofdays);
				map.put("presentdays", count.toString());
			}
			while(rs1.next()) {
				Map<String, Object> map1=new HashMap<>();
				map1.put("state",rs1.getString(1));
				map1.put("dist", rs1.getString(2));
				map1.put("hospitalcode", rs1.getString(3));
				map1.put("hospitalname", rs1.getString(4)+" ("+rs1.getString(3)+")");
				list.add(map1);
			}
			map.put("hospital",list);
			
		}catch (Exception e) {
			throw new Exception(e);
		}
		return map;
	}

	@Override
	public List<Object> getsmscoreview(Integer year, Integer month, Long userId) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;		
		Integer noofdays = 26;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SWASTHYAMITRA_SCORING_LIST")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SWASTHYAMITRA_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_HOSPITAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 3);
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_YEAR", year);
			storedProcedureQuery.setParameter("P_MONTH", month);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String, Object> map=new HashMap<>();
				map.put("smuserid",rs.getString(1));
				map.put("fullname", rs.getString(2));
				map.put("phoneNO", rs.getString(3));
				map.put("snaremark", rs.getString(4));
				map.put("snascore", rs.getString(6)!=null?rs.getString(6):"N/A");
				map.put("dcremark", rs.getString(5));
				map.put("dcscore", rs.getString(7)!=null?rs.getString(7):"N/A");
				Integer count=smpresentdays(rs.getString(1),year.toString(),month.toString());
				map.put("dutydays", noofdays);
				map.put("presentdays", count.toString());
				list.add(map);
			}
			
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}
	
	private Integer smpresentdays(String userid,String year,String month) throws Exception {
		Integer presentdays = 0;
		Connection con = null;
		Statement stmt = null;
		try {	
			con = jdbctemplet.getDataSource().getConnection();
			stmt = con.createStatement();

			String query = "select \r\n"
					+ "TO_DATE(s.ATTENDANCE_DATE,'DD-MON-YYYY') ,count(s.login_time),count(s.logout_time) \r\n"
					+ "from swathyamitra_attendnace_dtls s\r\n"
					+ "inner join userdetails u on u.username=s.HOSPITAL_CODE and u.TMS_LOGIN_STATUS=0\r\n"
					+ "WHERE EXTRACT(YEAR FROM s.ATTENDANCE_DATE)=" + year + "\r\n"
					+ "AND EXTRACT(MONTH FROM s.ATTENDANCE_DATE)=" + month + "\r\n" + "and s.USER_ID="
					+ userid + " and s.statusflag=0\r\n"
					+ "group by TO_DATE(s.ATTENDANCE_DATE,'DD-MON-YYYY')";

			ResultSet rss = stmt.executeQuery(query);			
			while (rss.next()) {
				if (rss.getInt(2) != 0 && rss.getInt(2) == rss.getInt(3)) {
					presentdays++;
				}
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return presentdays;
	}

	@Override
	public List<Object> getsmscoringreport(Integer year, Integer month, Long userId, String statecode, String distcode,
			String hospitalCode, Long smid) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SWASTHYAMITRA_SCORING_RPT")
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SWASTHYAMITRA_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATE_CODE",statecode);
			storedProcedureQuery.setParameter("P_DIST_CODE", distcode);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_YEAR", year);
			storedProcedureQuery.setParameter("P_MONTH", month);
			storedProcedureQuery.setParameter("P_SWASTHYAMITRA_ID", smid);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String, Object> map=new HashMap<>();
				map.put("smuserid",rs.getString(1));
				map.put("smname",rs.getString(2));
				map.put("cnctno", rs.getString(3));
				map.put("snascore", rs.getString(4)!=null?rs.getString(4):"N/A");
				map.put("dcscore", rs.getString(5)!=null?rs.getString(5):"N/A");
				map.put("snarank", rs.getString(6));
				map.put("dcrank", rs.getString(7));
				map.put("overalscore", rs.getString(8));
				map.put("finalrank", rs.getString(9));
				Integer count=smpresentdays(rs.getString(1),year.toString(),month.toString());
				map.put("noofdutydays", 26);
				map.put("noofpresentdays", count.toString());
				list.add(map);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public Response submitsmscore(Long smid, Integer year, Integer month, String remark, Integer score,
			Long userId) throws Exception {
		Response response=new  Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SWASTHYAMITRA_SCORING_SUBMIT")
					.registerStoredProcedureParameter("P_SCORING_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCORING_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SWASTHYAMITRA_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCORING", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_SCORING_YEAR", year);
			storedProcedureQuery.setParameter("P_SCORING_MONTH",month);
			storedProcedureQuery.setParameter("P_SWASTHYAMITRA_ID", smid);
			storedProcedureQuery.setParameter("P_REMARKS", remark);
			storedProcedureQuery.setParameter("P_SCORING", score);
			storedProcedureQuery.setParameter("P_USERID", userId);
			
			storedProcedureQuery.execute();
			Integer count = (Integer) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if(count==1) {
				response.setStatus("200");
				response.setMessage("Remark Submitted Successfully");
			}else if(count==0) {
				response.setStatus("201");
				response.setMessage("Already Action taken!!");
			}else {
				response.setStatus("400");
				response.setMessage("Something Went Wrong!!");
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public List<Object> getsmfinalincenivereport(Integer year, Integer month, Long userId, String statecode,
			String distcode, String hospitalCode, Long smid) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SWASTHYAMITRA_FINALSCORE_RPT")
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)					
					.registerStoredProcedureParameter("P_SWASTHYAMITRA_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_YEAR", year);
			storedProcedureQuery.setParameter("P_MONTH", month);
			storedProcedureQuery.setParameter("P_STATECODE",statecode);
			storedProcedureQuery.setParameter("P_DISTRICTCODE", distcode);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_USERID", userId);			
			storedProcedureQuery.setParameter("P_SWASTHYAMITRA_ID", smid);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String, Object> map=new HashMap<>();
				map.put("districtname",rs.getString(1));
				map.put("hospitalname",rs.getString(2)+" ("+rs.getString(13)+")");
				map.put("datarecived", rs.getString(3));
				map.put("datarecivedrank", rs.getString(4));
				map.put("patientsatisfy", rs.getString(5));
				map.put("rankpatientsatisfy", rs.getString(6));
				map.put("successcall", rs.getString(7));
				map.put("ranksuccesscall", rs.getString(8));
				map.put("overalsmscore", rs.getString(9));
				map.put("finalsmrank", rs.getString(10));
				map.put("finalsmscore", rs.getString(11));
				map.put("finalincentive", rs.getString(12));
				map.put("hospitalcode", rs.getString(13));
				list.add(map);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	

}
