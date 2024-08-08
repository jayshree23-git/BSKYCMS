package com.project.bsky.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CpdLeaveInfoBean;
import com.project.bsky.bean.CpdassignedhospitaldetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CPDConfiguration;
import com.project.bsky.model.CPDLeaveInfo;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsCpd;
import com.project.bsky.repository.CPDConfigurationRepository;
import com.project.bsky.repository.CPDLeaveInfoRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.UserDetailsCpdReposiitory;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.CPDLeaveInfoService;

@Service
public class CPDLeaveInfoServiceImpl implements CPDLeaveInfoService {

	@Autowired
	private CPDLeaveInfoRepository cpdleaveinforepository;

	@Autowired
	private UserDetailsCpdReposiitory userdetailsforcpdrepository;

	@Autowired
	private UserDetailsRepository userdetail;
	@Autowired
	private HospitalInformationRepository hospitalUserSaveRepository;

	@Autowired
	private CPDConfigurationRepository cpdconfigurationRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<CPDLeaveInfo> getallcpdleaveapplication(Long userId) {
		List<CPDLeaveInfo> getallcpdleavedetails = new ArrayList<CPDLeaveInfo>();
		List<CPDLeaveInfo> getallcpdleaveapplication = new ArrayList<CPDLeaveInfo>();
		try {
			getallcpdleavedetails = cpdleaveinforepository.getallcpdleaveapplication(userId);
			for (CPDLeaveInfo x : getallcpdleavedetails) {
				x.setScreateon(x.getCreateon().toString().substring(0, 10));
				x.setStodate(x.getTodate().toString().substring(0, 10));
				x.setSformdate(x.getFormdate().toString().substring(0, 10));
				getallcpdleaveapplication.add(x);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getallcpdleaveapplication;
	}

	@Override
	public CPDLeaveInfo getcpddetails(Long userId) {
		CPDLeaveInfo cpdleaveinfo = cpdleaveinforepository.findById(userId).get();
		if (cpdleaveinfo != null) {
			cpdleaveinfo.setScreateon(cpdleaveinfo.getCreateon().toString().substring(0, 10));
			cpdleaveinfo.setSformdate(cpdleaveinfo.getFormdate().toString().substring(0, 10));
			cpdleaveinfo.setStodate(cpdleaveinfo.getTodate().toString().substring(0, 10));
			long time = cpdleaveinfo.getTodate().getTime() - cpdleaveinfo.getFormdate().getTime();
			long days = ((time / (1000 * 60 * 60 * 24)) % 365) + 1;
			cpdleaveinfo.setNoofdays(days);
		}
		return cpdleaveinfo;
	}

	@Override
	public Response approverequest(Long leaveId, Integer approve, Long createby) {
		Response response = new Response();
		try {
			CPDLeaveInfo cpdleaveinfo = cpdleaveinforepository.findById(leaveId).get();
			if (cpdleaveinfo != null) {
				cpdleaveinfo.setStatus(approve);
				cpdleaveinfo.setActiontakenby(createby);
				cpdleaveinfo.setActiontakenon(Calendar.getInstance().getTime());
				CPDLeaveInfo cpdleaveinfo1 = cpdleaveinforepository.save(cpdleaveinfo);
				if (cpdleaveinfo1.getStatus() == 1) {
					response.setMessage("Leave Approve Successfull");
				} else if (cpdleaveinfo1.getStatus() == 2) {
					response.setMessage("Leave Reject Successfull");
				} else {
					response.setMessage("Nothing to Happen");
				}
				response.setStatus("Success");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<CpdassignedhospitaldetailsBean> cpdmappingdetails(Long user) {
		List<CpdassignedhospitaldetailsBean> objBean = new ArrayList<CpdassignedhospitaldetailsBean>();
		CpdassignedhospitaldetailsBean cpdassignedhospitaldetailsbean = null;
		try {
			CPDLeaveInfo cpdleaveinfo = cpdleaveinforepository.findById(user).get();
			List<CPDConfiguration> cpdConfiguration1 = cpdconfigurationRepository
					.findAllById(cpdleaveinfo.getCpduserId().getBskyUserId());
			for (CPDConfiguration x : cpdConfiguration1) {
				cpdassignedhospitaldetailsbean = new CpdassignedhospitaldetailsBean();
				List<Object[]> object = hospitalUserSaveRepository.hospital();
				if (!object.isEmpty()) {
					for (Object[] obj1 : object) {
						if (x.getHospitalCode().equals(obj1[3])) {
							HospitalInformation hos = new HospitalInformation();
							hos.setHospitalName((String) obj1[0]);
							hos.setState((String) obj1[1]);
							hos.setDist((String) obj1[2]);
							hos.setHospitalCode((String) obj1[3]);
							cpdassignedhospitaldetailsbean.setHohspital(hos);
						}
					}
				}
				objBean.add(cpdassignedhospitaldetailsbean);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return objBean;
	}

	@Override
	public Integer saveCPDLeaveInfo(CpdLeaveInfoBean cpdLeaveInfoBean) {
		UserDetailsCpd userDetailsCpd = new UserDetailsCpd();
		UserDetails userdetails = userdetail.findById(Long.parseLong(cpdLeaveInfoBean.getCreatedby())).get();
		userDetailsCpd = userdetailsforcpdrepository.findByuserid(userdetails);
		CPDLeaveInfo cpdLeaveInfo = new CPDLeaveInfo();
		try {
			Date fromDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(cpdLeaveInfoBean.getFormdate());
			Date todate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(cpdLeaveInfoBean.getTodate());
			List<CPDLeaveInfo> list = cpdleaveinforepository.getLeaveBetweenTwoDate(fromDate1, todate1,
					Integer.valueOf(userDetailsCpd.getBskyUserId()));
			if (list.size() > 0) {
				return 2;
			} else {

				cpdLeaveInfo.setFormdate(fromDate1);
				cpdLeaveInfo.setTodate(todate1);
				cpdLeaveInfo.setCreatedby(userdetails);
				cpdLeaveInfo.setRemarks(cpdLeaveInfoBean.getRemarks());
				cpdLeaveInfo.setActiontakenby(null);
				cpdLeaveInfo.setActiontakenon(null);
				cpdLeaveInfo.setStatus(0);
				cpdLeaveInfo.setAssignedsna(null);
				cpdLeaveInfo.setCreateon(new Date());
				Calendar calendar = Calendar.getInstance();
				cpdLeaveInfo.setCreateon(calendar.getTime());
				cpdLeaveInfo.setCpduserId(userDetailsCpd);
				cpdLeaveInfo = cpdleaveinforepository.save(cpdLeaveInfo);
				return 1;
			}
		} catch (ParseException e) {
			logger.error(ExceptionUtils.getStackTrace(e));

			return 0;
		}
	}

	@Override
	public List<CPDLeaveInfo> getcpdactiondetails(Long userId) {
		List<CPDLeaveInfo> getallcpdleavedetails = new ArrayList<CPDLeaveInfo>();
		List<CPDLeaveInfo> getallcpdleaveapplication = new ArrayList<CPDLeaveInfo>();
		try {
			getallcpdleavedetails = cpdleaveinforepository.findAlldata(userId);
			for (CPDLeaveInfo x : getallcpdleavedetails) {
				x.setScreateon(x.getCreateon().toString().substring(0, 10));
				x.setStodate(x.getTodate().toString().substring(0, 10));
				x.setSformdate(x.getFormdate().toString().substring(0, 10));
				long time = x.getTodate().getTime() - x.getFormdate().getTime();
				long days = ((time / (1000 * 60 * 60 * 24)) % 365) + 1;
				x.setNoofdays(days);
				getallcpdleaveapplication.add(x);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getallcpdleaveapplication;
	}

	@Override
	public List<CPDLeaveInfo> getcpdleavestatus(Long userId) {
		UserDetailsCpd userDetailsCpd = new UserDetailsCpd();
		UserDetails userdetails = userdetail.findById(userId).get();
		userDetailsCpd = userdetailsforcpdrepository.findByuserid(userdetails);
		List<CPDLeaveInfo> getallcpdleavedetails = new ArrayList<CPDLeaveInfo>();
		List<CPDLeaveInfo> getallcpdleaveapplication = new ArrayList<CPDLeaveInfo>();
		try {
			getallcpdleavedetails = cpdleaveinforepository.findhistory(userDetailsCpd.getBskyUserId());
			for (CPDLeaveInfo x : getallcpdleavedetails) {
				x.setScreateon(x.getCreateon().toString().substring(0, 10));
				x.setStodate(x.getTodate().toString().substring(0, 10));
				x.setSformdate(x.getFormdate().toString().substring(0, 10));
				long time = x.getTodate().getTime() - x.getFormdate().getTime();
				long days = ((time / (1000 * 60 * 60 * 24)) % 365) + 1;
				x.setNoofdays(days);
				getallcpdleaveapplication.add(x);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getallcpdleaveapplication;
	}

	@SuppressWarnings("unused")
	@Override
	public CPDLeaveInfo findAllByLeaveId(Long userId) {
		CPDLeaveInfo cPDLeaveInfo = null;
		UserDetailsCpd userDetailsCpd = new UserDetailsCpd();
		UserDetails userdetails = userdetail.findById(userId).get();
		userDetailsCpd = userdetailsforcpdrepository.findByuserid(userdetails);
		return cPDLeaveInfo;
	}

	@Override
	public void deletebyLeaveId(Long leaveId) {
		try {
			cpdleaveinforepository.deleteByleaveId(leaveId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<CPDLeaveInfo> getcpdleavehistory(Long user) {
		CPDLeaveInfo cpdleaveinfo = cpdleaveinforepository.findById(user).get();
		List<CPDLeaveInfo> getallcpdleavedetails = new ArrayList<CPDLeaveInfo>();
		List<CPDLeaveInfo> getallcpdleavehistory = new ArrayList<CPDLeaveInfo>();
		getallcpdleavedetails = cpdleaveinforepository.findBycpduserId(cpdleaveinfo.getCpduserId());
		for (CPDLeaveInfo x : getallcpdleavedetails) {
			x.setScreateon(x.getCreateon().toString().substring(0, 10));
			x.setStodate(x.getTodate().toString().substring(0, 10));
			x.setSformdate(x.getFormdate().toString().substring(0, 10));
			long time = x.getTodate().getTime() - x.getFormdate().getTime();
			long days = ((time / (1000 * 60 * 60 * 24)) % 365) + 1;
			x.setNoofdays(days);
			getallcpdleavehistory.add(x);
		}
		return getallcpdleavehistory;
	}

	@Override
	public List<CPDLeaveInfo> getallcpdleavefilterrequest(Long user, String formdate, String todate) {
		List<CPDLeaveInfo> getallcpdleavefilterrequest;
		List<CPDLeaveInfo> getallcpdleavefilterrequest1 = new ArrayList<CPDLeaveInfo>();
		try {
			Date fromDate = new SimpleDateFormat("dd-MMM-yyyy").parse(formdate);
			Date toDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			getallcpdleavefilterrequest = cpdleaveinforepository.getallcpdleaveapplication(user);
			for (CPDLeaveInfo x : getallcpdleavefilterrequest) {
				if (fromDate.compareTo(x.getFormdate()) <= 0 && toDate1.compareTo(x.getFormdate()) >= 0) {
					x.setScreateon(x.getCreateon().toString().substring(0, 10));
					x.setStodate(x.getTodate().toString().substring(0, 10));
					x.setSformdate(x.getFormdate().toString().substring(0, 10));
					long time = x.getTodate().getTime() - x.getFormdate().getTime();
					long days = ((time / (1000 * 60 * 60 * 24)) % 365) + 1;
					x.setNoofdays(days);
					getallcpdleavefilterrequest1.add(x);
				}

			}
		} catch (ParseException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getallcpdleavefilterrequest1;
	}

	@Override
	public List<CPDLeaveInfo> getallcpdfilteractiondetails(Long user, String formdate, String todate) {
		List<CPDLeaveInfo> getallcpdleavefilterrequest;
		List<CPDLeaveInfo> getallcpdleavefilterrequest1 = new ArrayList<CPDLeaveInfo>();
		try {
			Date fromDate = new SimpleDateFormat("dd-MMM-yyyy").parse(formdate);
			Date toDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			getallcpdleavefilterrequest = cpdleaveinforepository.findAlldata(user);
			for (CPDLeaveInfo x : getallcpdleavefilterrequest) {
				if (fromDate.compareTo(x.getFormdate()) <= 0 && toDate1.compareTo(x.getFormdate()) >= 0) {
					x.setScreateon(x.getCreateon().toString().substring(0, 10));
					x.setStodate(x.getTodate().toString().substring(0, 10));
					x.setSformdate(x.getFormdate().toString().substring(0, 10));
					long time = x.getTodate().getTime() - x.getFormdate().getTime();
					long days = ((time / (1000 * 60 * 60 * 24)) % 365) + 1;
					x.setNoofdays(days);
					getallcpdleavefilterrequest1.add(x);
				}
			}
		} catch (ParseException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getallcpdleavefilterrequest1;
	}

	@Override
	public List<CPDLeaveInfo> getCPDLeaveFilterData(Integer userId, String fromdate, String todate) {
		UserDetailsCpd userDetailsCpd = userdetailsforcpdrepository.findByuserdetails(userId.longValue());
		List<CPDLeaveInfo> getCPDListFilterData = null;
		List<CPDLeaveInfo> getallcpdleavefilterrequest1 = new ArrayList<CPDLeaveInfo>();
		try {
			Date formDate = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			Date toDate = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			getCPDListFilterData = cpdleaveinforepository.findFilterDataCPD(formDate, toDate,
					userDetailsCpd.getBskyUserId());
			for (CPDLeaveInfo x : getCPDListFilterData) {
				if (x.getCreateon() != null) {
					x.setScreateon(x.getCreateon().toString().substring(0, 10));
				}
				x.setStodate(x.getTodate().toString().substring(0, 10));
				x.setSformdate(x.getFormdate().toString().substring(0, 10));
				long time = x.getTodate().getTime() - x.getFormdate().getTime();
				long days = ((time / (1000 * 60 * 60 * 24)) % 365) + 1;
				x.setNoofdays(days);
				getallcpdleavefilterrequest1.add(x);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getallcpdleavefilterrequest1;
	}
}
