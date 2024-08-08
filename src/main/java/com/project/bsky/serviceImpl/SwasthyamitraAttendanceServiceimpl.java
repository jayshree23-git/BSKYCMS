/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Hospital;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.Smattendancereportbean;
import com.project.bsky.bean.Smfacilityreport;
import com.project.bsky.bean.SwasthyaMitraBean;
import com.project.bsky.bean.SwasthyaMitraGeoTagAttendanceBean;
import com.project.bsky.bean.SwasthyaMitraGeoTagBean;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.SwasthyamitraMapping;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsProfile;
import com.project.bsky.model.Userswasthyamitradetails;
import com.project.bsky.repository.SwasthyamitraMappingRepository;
import com.project.bsky.repository.UserDetailsProfileRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.repository.UserswasthyamitradetailsRepository;
import com.project.bsky.service.SwasthyamitraAttendanceService;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class SwasthyamitraAttendanceServiceimpl implements SwasthyamitraAttendanceService {

	@Autowired
	private JdbcTemplate jdbctemplet;

	@Autowired
	private Logger logger;

	@Autowired
	private SwasthyamitraMappingRepository swasthyamitraMappingRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private UserDetailsProfileRepository userDetailsProfileRepository;

	@Autowired
	private UserswasthyamitradetailsRepository userswasthyamitrarepo;

	Calendar calendar = Calendar.getInstance();

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Smattendancereportbean> getsmattendancereport(Integer year, Integer month) {
		List<Smattendancereportbean> list = new ArrayList<Smattendancereportbean>();
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		Integer noofdays = 26;
		try {

			// step1 create the connection object
			con = jdbctemplet.getDataSource().getConnection();

			// step2 create the statement object
			stmt = con.createStatement();

			List<UserDetails> user = userDetailsRepository.finddetailsSwasthyaMitra();

			for (UserDetails details : user) {
				Smattendancereportbean bean = new Smattendancereportbean();
				bean.setSmname(details.getFullname());
				UserDetailsProfile userdeailprof = userDetailsProfileRepository.findByuserId(details.getUserId());
//				//System.out.println(details.getFullname());
				if (userdeailprof.getDateofjoin() != null) {
					bean.setDateofjoin(new SimpleDateFormat("dd-MMM-yyyy").format(userdeailprof.getDateofjoin()));
				} else {
					bean.setDateofjoin("N/A");
				}
				List<Object> hosplist = new ArrayList<Object>();
				Integer hospcount = 0;
				String name = "";
				String name1 = "";
				String dist = "";
				String code = "";
				try {
					List<Object[]> str = swasthyamitraMappingRepository.gethospilist(details.getUserId());
					for (Object[] x : str) {
						HospitalInformation hos = new HospitalInformation();
						hos.setHospitalCode((String) x[0]);
						hos.setHospitalName((String) x[1]);
						name = name + (String) x[1] + ",";
						code = code + (String) x[0] + ",";
						name1 = (String) x[1];
						hos.setState((String) x[2]);
						if (name1.length() > 40) {
							dist = dist + (String) x[2] + ",";
						} else {
							dist = dist + (String) x[2] + ",";
						}

						hos.setDist((String) x[3]);
						hosplist.add(hos);
						hospcount++;
					}
				} catch (Exception e) {
				}
				bean.setHosplist(hosplist);
				if (hospcount == 0) {
					bean.setHospcount("1");
				} else {
					bean.setHospcount(hospcount.toString());
				}
				bean.setHospital(name);
				bean.setCode(code);
				bean.setDiscrict(dist);
				rs = null;

				String query = "select \r\n"
						+ "TO_DATE(s.ATTENDANCE_DATE,'DD-MON-YYYY') ,count(s.login_time),count(s.logout_time) \r\n"
						+ "from swathyamitra_attendnace_dtls s\r\n"
						+ "inner join userdetails u on u.username=s.HOSPITAL_CODE and u.TMS_LOGIN_STATUS=0\r\n"
						+ "WHERE EXTRACT(YEAR FROM s.ATTENDANCE_DATE)=" + year + "\r\n"
						+ "AND EXTRACT(MONTH FROM s.ATTENDANCE_DATE)=" + month + "\r\n" + "and s.USER_ID="
						+ details.getUserId() + " and s.statusflag=0\r\n"
						+ "group by TO_DATE(s.ATTENDANCE_DATE,'DD-MON-YYYY')";

				rs = stmt.executeQuery(query);
				Integer Count = 0;
				while (rs.next()) {
					if (rs.getInt(2) != 0 && rs.getInt(2) == rs.getInt(3)) {
						Count++;
					}
				}
				bean.setNoofdutydays(Count.toString());
				Integer abs = noofdays - Count;
				if (abs > 0) {
					bean.setAbsent(abs.toString() + " Days");
				} else {
					bean.setAbsent("NIL");
				}
				list.add(bean);
			}
			con.close();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		Collections.sort(list,
				(b1, b2) -> (int) (Integer.parseInt(b2.getHospcount()) - Integer.parseInt(b1.getHospcount())));
		return list;
	}

	public static Integer getNumberOfDaysInMonth(Integer year, Integer month) {
		Calendar mycal = new GregorianCalendar(year, month, 1);
		Integer daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return daysInMonth;
	}

	public static int getWorkingDaysBetweenTwoDates(Calendar cal) {
		Calendar calendar = cal;
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date monthFirstDay = calendar.getTime();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date monthLastDay = calendar.getTime();

		Date startDate = monthFirstDay;
		Date endDate = monthLastDay;

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		int workDays = 0;

		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
			return 0;
		}

		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			startCal.setTime(endDate);
			endCal.setTime(startDate);
		}

		while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				++workDays;
			}
			startCal.add(Calendar.DAY_OF_MONTH, 1);
		}

		return workDays;
	}

	@Override
	public Response saveswasthyamitra(Long userid, List<Hospital> hospital, Integer created) {
		Response response = new Response();
		List<SwasthyamitraMapping> swasmap = new ArrayList<SwasthyamitraMapping>();
		try {
			for (Hospital x : hospital) {
				Integer count = swasthyamitraMappingRepository.cheakduplicate(userid, x.getHospitalCode());
				if (count == 0) {
					SwasthyamitraMapping swasthyamitraMapping = new SwasthyamitraMapping();
					swasthyamitraMapping.setHospitalcode(x.getHospitalCode());
					swasthyamitraMapping.setStatusflag(0);
					swasthyamitraMapping.setCreateon(calendar.getTime());
					swasthyamitraMapping.setCreateby(created);
					UserDetails userdetails2 = userDetailsRepository.findById(userid).get();
					swasthyamitraMapping.setUserdetails(userdetails2);
					swasmap.add(swasthyamitraMapping);
				} else {
					response.setMessage(x.getHospitalName() + " Already Tagged Choose Another Hospital");
					response.setStatus("400");
					return response;
				}
			}
			List<SwasthyamitraMapping> swasmap1 = swasthyamitraMappingRepository.saveAll(swasmap);
			if (swasmap1 != null) {
				response.setStatus("200");
				response.setMessage("Swasthya Mitra Mapped Successfully");
			} else {
				response.setMessage("Some Error Happen! Please Try Later");
				response.setStatus("400");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen! Please Try Later");
			response.setStatus("400");
		}
		return response;
	}

	@Override
	public List<Object> getsmmappinglist() {
		List<Object> list = new ArrayList<Object>();
		try {
			Map<String, Integer> frequencyMap = new HashMap<>();
			List<SwasthyamitraMapping> mappings = null;
			mappings = swasthyamitraMappingRepository.findAllactivedata();
			if (!mappings.isEmpty()) {
				for (SwasthyamitraMapping item : mappings) {
					String userid = String.valueOf(item.getUserdetails().getUserId());
					Integer count = frequencyMap.get(userid);
					if (count == null) {
						count = 0;
					}
					frequencyMap.put(userid, count + 1);
				}
			}
			for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
				for (SwasthyamitraMapping map : mappings) {
					if (entry.getKey().equals(map.getUserdetails().getUserId().toString())) {
						SwasthyamitraMapping swas = new SwasthyamitraMapping();
						swas.setFullname(map.getUserdetails().getFullname());
						swas.setUsername(map.getUserdetails().getUserName());
						swas.setUserid(entry.getKey());
						swas.setCount(entry.getValue().toString());
						list.add(swas);
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<Object> getsmtaggedhospital(Long userid) {
		List<Object> list = new ArrayList<Object>();
		try {
			List<Object[]> str = swasthyamitraMappingRepository.gethospilist(userid);
			for (Object[] x : str) {
				HospitalInformation hos = new HospitalInformation();
				hos.setHospitalCode((String) x[0]);
				hos.setHospitalName((String) x[1]);
				hos.setState((String) x[2]);
				hos.setDist((String) x[3]);
				list.add(hos);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response updateswasthyamitra(Long userid, List<Hospital> hospital, Integer updated, Integer status) {
		Response response = new Response();
		List<SwasthyamitraMapping> swasmap = new ArrayList<SwasthyamitraMapping>();
		try {
			if (status == 1) {
				swasthyamitraMappingRepository.inactiveuser(userid);
				response.setStatus("200");
				response.setMessage("Swasthya Mitra UpDate Successfully");
			} else {
				for (Hospital x : hospital) {
					SwasthyamitraMapping swas = swasthyamitraMappingRepository.getforupdate(x.getHospitalCode(),
							userid);
					if (swas != null) {
						swas.setStatusflag(0);
						swas.setUpdateby(updated);
						swas.setUpdateon(calendar.getTime());
					} else {
						swas = new SwasthyamitraMapping();
						swas.setHospitalcode(x.getHospitalCode());
						swas.setStatusflag(0);
						swas.setCreateon(calendar.getTime());
						swas.setCreateby(updated);
						UserDetails userdetails2 = userDetailsRepository.findById(userid).get();
						swas.setUserdetails(userdetails2);
					}
					swasmap.add(swas);
				}
				List<SwasthyamitraMapping> swasthya = swasthyamitraMappingRepository.findByuserid(userid);
				for (SwasthyamitraMapping x : swasthya) {
					Boolean b = cheakexistornot(x.getHospitalcode(), hospital);
					if (b == false) {
						x.setStatusflag(1);
						swasmap.add(x);
					}
				}
				List<SwasthyamitraMapping> swasmap1 = swasthyamitraMappingRepository.saveAll(swasmap);
				if (swasmap1 != null) {
					response.setStatus("200");
					response.setMessage("Swasthya Mitra update Successfully");
				} else {
					response.setMessage("Some Error Happen! Please Try Later");
					response.setStatus("400");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen! Please Try Later");
			response.setStatus("400");
		}
		return response;
	}

	private Boolean cheakexistornot(String hospitalcode, List<Hospital> hospital) {
		for (Hospital x : hospital) {
			if (x.getHospitalCode().equals(hospitalcode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Response inactiveSwasthyaMitra(Long userid, Long bskyid, Integer statusflag) {
		Response response = new Response();
		try {
			userDetailsProfileRepository.inactivateactive(userid, bskyid, statusflag);
			userDetailsRepository.inactivateactive(userid, statusflag);
			response.setStatus("200");
			response.setMessage("Record Update Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Some thing Went Wrong");
		}
		return response;
	}

	@Override
	public List<Object> getDistinctSwasthyaMitra(Integer userId) {

		List<Object> getList = new ArrayList<Object>();
		try {
			List<Object[]> list = swasthyamitraMappingRepository.getonesmuserdetail(userId);
			for (Object[] obj : list) {
				SwasthyamitraMapping swas = new SwasthyamitraMapping();
				BigDecimal bd = (BigDecimal) obj[0];
				swas.setUserid(bd.toString());
				swas.setFullname((String) obj[1]);
				BigDecimal bd1 = (BigDecimal) obj[2];
				swas.setCount(bd1.toString());
				getList.add(swas);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getList;
	}

	@Override
	public List<SwasthyaMitraBean> getswasthyaMitraDetails(Integer groupId, String stateId, String districtId) {

		List<SwasthyaMitraBean> getList = new ArrayList<SwasthyaMitraBean>();
		try {
			List<Object[]> list = swasthyamitraMappingRepository.getswasthyaMitraDetails(groupId, stateId, districtId);
			for (Object[] obj : list) {
				SwasthyaMitraBean swasthyaMitraBean = new SwasthyaMitraBean();
				swasthyaMitraBean.setStatCode((String) obj[0]);
				swasthyaMitraBean.setStateName((String) obj[1]);
				swasthyaMitraBean.setDistCode((String) obj[2]);
				swasthyaMitraBean.setDistrictName((String) obj[3]);
				swasthyaMitraBean.setFullName((String) obj[4]);
				swasthyaMitraBean.setUserName((String) obj[5]);
				swasthyaMitraBean.setGropName((String) obj[6]);
				BigDecimal bd3 = (BigDecimal) obj[7];
				swasthyaMitraBean.setStateFlg(bd3.longValue());
				BigDecimal bd4 = (BigDecimal) obj[8];
				swasthyaMitraBean.setMobileNo(bd4.longValue());
				BigDecimal bd5 = (BigDecimal) obj[9];
				swasthyaMitraBean.setBskyUserId(bd5.longValue());
				swasthyaMitraBean.setEmailId((String) obj[10]);
				BigDecimal bd1 = (BigDecimal) obj[11];
				swasthyaMitraBean.setSwasthyaId(bd1.longValue());
				BigDecimal bd2 = (BigDecimal) obj[12];
				swasthyaMitraBean.setCounthospital(bd2.longValue());
				List<Object> hosplist = new ArrayList<Object>();
				Long hospcount = 0l;
				String name = "";
				try {
					List<Object[]> str = swasthyamitraMappingRepository
							.gethospitalData(swasthyaMitraBean.getSwasthyaId());
					for (Object[] x : str) {
						HospitalInformation hos = new HospitalInformation();
						hos.setHospitalCode((String) x[0]);
						hos.setHospitalName((String) x[1] + " (" + (String) x[0] + ")");
						if (name.isEmpty()) {
							name = (String) x[1];
						} else {
							name = name + ',' + (String) x[1];
						}
						hosplist.add(hos);
						hospcount++;
					}
				} catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
				swasthyaMitraBean.setHosplist(hosplist);
				if (hospcount == 0) {
					swasthyaMitraBean.setCounthospital(0l);
				} else {
					swasthyaMitraBean.setCounthospital(hospcount);
				}
				swasthyaMitraBean.setHospitalName((name));
				getList.add(swasthyaMitraBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getList;
	}

	@Override
	public List<SwasthyaMitraBean> getswasthyaMitraFilter(String stateId, String districtId) {

		List<SwasthyaMitraBean> getList = new ArrayList<SwasthyaMitraBean>();
		try {
			List<Object[]> list = swasthyamitraMappingRepository.getswasthyaMitraFilter(stateId, districtId);
			for (Object[] obj : list) {
				SwasthyaMitraBean swasthyaMitraBean = new SwasthyaMitraBean();
				swasthyaMitraBean.setStatCode((String) obj[0]);
				swasthyaMitraBean.setStateName((String) obj[1]);
				swasthyaMitraBean.setDistCode((String) obj[2]);
				swasthyaMitraBean.setDistrictName((String) obj[3]);
				swasthyaMitraBean.setFullName((String) obj[4]);
				swasthyaMitraBean.setUserName((String) obj[5]);
				swasthyaMitraBean.setGropName((String) obj[6]);
				BigDecimal bd3 = (BigDecimal) obj[7];
				swasthyaMitraBean.setStateFlg(bd3.longValue());
				BigDecimal bd4 = (BigDecimal) obj[8];
				swasthyaMitraBean.setMobileNo(bd4.longValue());
				BigDecimal bd5 = (BigDecimal) obj[9];
				swasthyaMitraBean.setBskyUserId(bd5.longValue());
				swasthyaMitraBean.setEmailId((String) obj[10]);
				BigDecimal bd1 = (BigDecimal) obj[11];
				swasthyaMitraBean.setSwasthyaId(bd1.longValue());
				BigDecimal bd2 = (BigDecimal) obj[12];
				swasthyaMitraBean.setCounthospital(bd2.longValue());
				List<Object> hosplist = new ArrayList<Object>();
				Long hospcount = 0l;
				String name = "";
				try {
					List<Object[]> str = swasthyamitraMappingRepository
							.gethospitalData(swasthyaMitraBean.getSwasthyaId());
					for (Object[] x : str) {
						HospitalInformation hos = new HospitalInformation();
						hos.setHospitalCode((String) x[0]);
						hos.setHospitalName((String) x[1] + " (" + (String) x[0] + ")");
						if (name.isEmpty()) {
							name = (String) x[1];
						} else {
							name = name + ',' + (String) x[1];
						}
						hosplist.add(hos);
						hospcount++;
					}
				} catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
				swasthyaMitraBean.setHosplist(hosplist);
				if (hospcount == 0) {
					swasthyaMitraBean.setCounthospital(0l);
				} else {
					swasthyaMitraBean.setCounthospital(hospcount);
				}
				swasthyaMitraBean.setHospitalName((name));
				getList.add(swasthyaMitraBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getList;
	}

	@Override
	public List<Object> getsmlistbyhospital(String hospital) {
		ResultSet rs = null;
		List<Object> getsmlistbyhospital = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SM_FACE_REREGISTRATION")
					.registerStoredProcedureParameter("P_ACTION_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SM_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 1l);
			storedProcedureQuery.setParameter("P_SM_ID", null);
			storedProcedureQuery.setParameter("P_STATE_CODE", null);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", null);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospital);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				UserDetails user = new UserDetails();
				user.setUserId(rs.getLong(1));
				user.setFullname(rs.getString(2));
				user.setUserName(rs.getString(3));
				getsmlistbyhospital.add(user);
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
		return getsmlistbyhospital;
	}

	@Override
	public List<Object> getsmlistforregistaration(String state, String dist, String hospital, String smid) {
		ResultSet rs = null;
		List<Object> getsmlistforregistaration = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SM_FACE_REREGISTRATION")
					.registerStoredProcedureParameter("P_ACTION_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SM_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 2l);
			storedProcedureQuery.setParameter("P_SM_ID", smid);
			storedProcedureQuery.setParameter("P_STATE_CODE", state);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", dist);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospital);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Userswasthyamitradetails user = new Userswasthyamitradetails();
				user.setUsername(rs.getString(1));
				user.setFullname(rs.getString(2));
				user.setMobile(rs.getString(3));
				user.setEmailid(rs.getString(4));
				user.setRegdate(rs.getString(5));
				user.setUserid(rs.getInt(6));
				getsmlistforregistaration.add(user);
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
		return getsmlistforregistaration;
	}

	@Override
	public Map<String, Object> getsmdetailsforregister(Long smid) {
		Map<String, Object> details = new HashMap<>();
		try {
			List<Object[]> objlist = swasthyamitraMappingRepository.getsmdetailsforregister(smid);
			for (Object[] obj : objlist) {
				details.put("username", obj[0]);
				details.put("fullname", obj[1]);
				details.put("phoneno", obj[2]);
				details.put("regdate", obj[3]);
				details.put("userid", obj[4]);
			}
			details.put("status", 200);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("error", e);
		}
		return details;
	}

	@Override
	public Response allowsmforregister(Integer smid, Integer updateby) {
		Response response = new Response();
		try {
			userswasthyamitrarepo.inActivatesmdetails(smid, updateby);
			response.setStatus("200");
			response.setMessage("Successfully Allowed For Re-Registration");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen! Please Try Later");
			response.setStatus("400");
		}
		return response;
	}

	@Override
	public List<Object> getapprovesmlistforregistaration(String state, String dist, String hospital, String smid) {
		ResultSet rs = null;
		List<Object> getsmlistforregistaration = new ArrayList<Object>();
		System.out.println(hospital);
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SM_FACE_REREGISTRATION")
					.registerStoredProcedureParameter("P_ACTION_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SM_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 3l);
			storedProcedureQuery.setParameter("P_SM_ID", smid);
			storedProcedureQuery.setParameter("P_STATE_CODE", state);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", dist);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospital);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Userswasthyamitradetails user = new Userswasthyamitradetails();
				user.setUsername(rs.getString(1));
				user.setFullname(rs.getString(2));
				user.setMobile(rs.getString(3));
				user.setEmailid(rs.getString(4));
				user.setRegdate(rs.getString(5));
				user.setUserid(rs.getInt(6));
				getsmlistforregistaration.add(user);
			}
			System.out.println(getsmlistforregistaration.size());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return getsmlistforregistaration;
	}

	@Override
	public List<Object> getsmlogdetails(Long smid) {
		List<Object> list = new ArrayList<>();
		Map<String, Object> details;
		try {
			List<Object[]> str = swasthyamitraMappingRepository.getfacelogdetails(smid);
			for (Object[] x : str) {
				details = new HashMap<>();
				details.put("smname", x[0]);
				details.put("registration", x[1]);
				details.put("allow", x[2]);
				details.put("allowby", x[3]);
				list.add(details);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	@Override
	public Map<String, Object> swasthyaMitraGeoTagUpdate(SwasthyaMitraGeoTagBean geoTagBean) {
		Map<String, Object> map = new HashMap<>();
		int insertUpdate = 0;
		try {
			if (geoTagBean.getActionCode() == 3 || geoTagBean.getActionCode() == 4) {
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("USP_SWASTHYA_MITRA_GEO_TAG_UPDATE")
						.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_geo_tag", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_created_by", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_response", void.class, ParameterMode.REF_CURSOR);

				storedProcedureQuery.setParameter("p_action_code", geoTagBean.getActionCode());
				storedProcedureQuery.setParameter("p_user_id", geoTagBean.getUserId());
				storedProcedureQuery.execute();
				ResultSet rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_response");
				SwasthyaMitraGeoTagAttendanceBean geoTag = null;
				List<SwasthyaMitraGeoTagAttendanceBean> swasthyaMitraList = new ArrayList<>();

				while (rs.next()) {
					geoTag = new SwasthyaMitraGeoTagAttendanceBean();
					if (geoTagBean.getActionCode() == 3) {
						geoTag.setUserId(rs.getInt(1));
						geoTag.setFullname(rs.getString(2));
						geoTag.setUsername(rs.getString(3));
						geoTag.setMobileNo(rs.getString(4));
						geoTag.setGeoTag(rs.getInt(5));
					} else {
						geoTag.setGeoTagStatus(rs.getInt(1) == 0 ? "Active" : "In-Active");
						geoTag.setCreatedBy(rs.getString(2));
						geoTag.setCreatedOn(rs.getString(3));
						geoTag.setUpdatedBy(rs.getString(4));
						geoTag.setUpdatedOn(rs.getString(5));
					}
					swasthyaMitraList.add(geoTag);
				}

				map.put("status", "success");
				map.put("data", swasthyaMitraList);
				map.put("message", "Data Retrived Successfully");
			} else {
				for (SwasthyaMitraGeoTagAttendanceBean bean : geoTagBean.getAttendance()) {
					if (!Objects.equals(bean.getStatus(), bean.getGeoTag())) {
						StoredProcedureQuery storedProcedureQuery = this.entityManager
								.createStoredProcedureQuery("USP_SWASTHYA_MITRA_GEO_TAG_UPDATE")
								.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_geo_tag", Integer.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_created_by", Integer.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_response", void.class, ParameterMode.REF_CURSOR);

						storedProcedureQuery.setParameter("p_created_by", geoTagBean.getCreatedBy());
						storedProcedureQuery.setParameter("p_action_code", bean.getStatus() == 2 ? 1 : 2);
						storedProcedureQuery.setParameter("p_user_id", bean.getUserId());
						storedProcedureQuery.setParameter("p_geo_tag", bean.getGeoTag());
						storedProcedureQuery.execute();
						insertUpdate++;
					}
				}
				if (insertUpdate == 0) {
					map.put("status", "blank");
					map.put("message", "Please add or update any record for attendance");
				} else {
					map.put("status", "success");
					map.put("message", "Swasthya Mitra Geo Tagging Attendance Updated Successfully");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "failed");
			map.put("message", e.getMessage());
		}
		return map;
	}

}
