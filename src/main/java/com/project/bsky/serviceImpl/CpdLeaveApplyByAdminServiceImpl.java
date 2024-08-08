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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CpdLeaveApplyAdmin;
import com.project.bsky.model.CPDLeaveInfo;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsCpd;
import com.project.bsky.repository.CPDLeaveInfoRepository;
import com.project.bsky.repository.UserDetailsCpdReposiitory;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.CpdLeaveApplyAdminService;

@SuppressWarnings("unused")
@Service
public class CpdLeaveApplyByAdminServiceImpl implements CpdLeaveApplyAdminService {

	@Autowired
	private UserDetailsRepository userdetail;

	@Autowired
	private CPDLeaveInfoRepository cpdleaveinforepository;

	@Autowired
	private UserDetailsCpdReposiitory userdetailsforcpdrepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<CPDLeaveInfo> getCpdAllLeaveData(Long userId) {
		List<CPDLeaveInfo> getCpdLeaveDetailsAdmin = new ArrayList<CPDLeaveInfo>();
		List<CPDLeaveInfo> getCpdLeaveHistoryAdmin = new ArrayList<CPDLeaveInfo>();
		try {
			getCpdLeaveDetailsAdmin = cpdleaveinforepository.findAll(Sort.by(Sort.Direction.DESC, "leaveId"));
			for (CPDLeaveInfo x : getCpdLeaveDetailsAdmin) {
				if (x.getCreateon() != null) {
					x.setScreateon(x.getCreateon().toString().substring(0, 10));
				}
				if (x.getTodate() != null) {
					x.setStodate(x.getTodate().toString().substring(0, 10));
				}
				if (x.getFormdate() != null) {
					x.setSformdate(x.getFormdate().toString().substring(0, 10));
				}
				long time = x.getTodate().getTime() - x.getFormdate().getTime();
				long days = ((time / (1000 * 60 * 60 * 24)) % 365) + 1;
				x.setNoofdays(days);
				getCpdLeaveHistoryAdmin.add(x);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return getCpdLeaveDetailsAdmin;
	}

	@Override
	public Integer saveCpdLeaveForAdmin(CpdLeaveApplyAdmin cpdLeaveApplyAdmin) {
		UserDetailsCpd userDetailsCpd = new UserDetailsCpd();
		UserDetails userdetails = userdetail.findById(Long.parseLong(cpdLeaveApplyAdmin.getCreatedby())).get();
		userDetailsCpd = userdetailsforcpdrepository.findById(Integer.parseInt(cpdLeaveApplyAdmin.getBskyUserId()))
				.get();
		CPDLeaveInfo cpdLeaveInfo = new CPDLeaveInfo();
		try {
			List<CPDLeaveInfo> pastcpdleavelist = cpdleaveinforepository.findBycpduserId(userDetailsCpd);
			Date fromDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(cpdLeaveApplyAdmin.getFormdate());
			Date todate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(cpdLeaveApplyAdmin.getTodate());
			List<CPDLeaveInfo> list = cpdleaveinforepository.getLeaveBetweenTwoDate(fromDate1, todate1,
					Integer.valueOf(cpdLeaveApplyAdmin.getBskyUserId()));
			if (list.size() > 0) {
				return 2;
			} else {
				cpdLeaveInfo.setFormdate(fromDate1);
				cpdLeaveInfo.setTodate(todate1);
				cpdLeaveInfo.setCreatedby(userdetails);
				cpdLeaveInfo.setRemarks(cpdLeaveApplyAdmin.getRemarks());
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
	public List<CPDLeaveInfo> getCPDLeaveFilterDataAdmin(Integer bskyUserId, String fromdate, String todate) {
		List<CPDLeaveInfo> getcpdListFilterAdmin = new ArrayList<CPDLeaveInfo>();
		List<CPDLeaveInfo> getcpdListFilterAdmin1 = new ArrayList<CPDLeaveInfo>();
		try {
			Date fromDate = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			Date toDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			if (bskyUserId == null) {
				getcpdListFilterAdmin = cpdleaveinforepository.findFilterDataadmin(fromDate, toDate1);
			} else {
				getcpdListFilterAdmin = cpdleaveinforepository.findFilterDataCPD(fromDate, toDate1, bskyUserId);
			}
			for (CPDLeaveInfo x : getcpdListFilterAdmin) {
				if (fromDate.compareTo(x.getFormdate()) <= 0 && toDate1.compareTo(x.getFormdate()) >= 0) {
					if (x.getCreateon() != null) {
						x.setScreateon(x.getCreateon().toString().substring(0, 10));
					}
					x.setStodate(x.getTodate().toString().substring(0, 10));
					x.setSformdate(x.getFormdate().toString().substring(0, 10));
					long time = x.getTodate().getTime() - x.getFormdate().getTime();
					long days = ((time / (1000 * 60 * 60 * 24)) % 365) + 1;
					x.setNoofdays(days);
					getcpdListFilterAdmin1.add(x);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getcpdListFilterAdmin1;
	}

}
