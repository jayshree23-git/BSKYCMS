package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.EnableHospitalDischargeBean;
import com.project.bsky.bean.HospBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.EnableHospitalDischargeLogModel;
import com.project.bsky.model.EnableHospitalDischargeModel;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.repository.EnableHospitalDischargeLog;
import com.project.bsky.repository.EnableHospitalDischargeRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.SNOConfigurationRepository;
import com.project.bsky.service.EnableHospitalDischargeService;

@Service
public class EnableHospitalDischargeServiceImpl implements EnableHospitalDischargeService {

	private static final Logger logger = LoggerFactory.getLogger(EnableHospitalDischargeServiceImpl.class);

	@Autowired
	private SNOConfigurationRepository snaconfigrepo;

	@Autowired
	private EnableHospitalDischargeRepository enabledischargerepo;

	@Autowired
	private HospitalInformationRepository hosprepo;

	@Autowired
	private EnableHospitalDischargeLog gospitaldischargelogrepo;

	@Override
	public List<EnableHospitalDischargeBean> gettaggedhospitallist(Long userid, String state, String dist,
			String hospital) {
		List<EnableHospitalDischargeBean> taggedhospital = new ArrayList<EnableHospitalDischargeBean>();
		try {
			List<Object[]> list = snaconfigrepo.getalltaggedhospitallist(userid, state, dist, hospital);
			for (Object[] obj : list) {
				EnableHospitalDischargeBean bean = new EnableHospitalDischargeBean();
				bean.setHospitalname((String) obj[0]);
				bean.setHospitalcode((String) obj[1]);
				bean.setStatename((String) obj[2]);
				bean.setDistname((String) obj[3]);
				String s = (String) obj[4];
				bean.setStatus(s.equals("A") ? true : false);
				taggedhospital.add(bean);
			}
		} catch (Exception e) {
			logger.error("Error " + e);
		}
		return taggedhospital;
	}

	@Override
	public List<HospBean> gettaggedhospitallistfosna(Long userid) {
		List<HospBean> list = new ArrayList<HospBean>();
		try {
			List<String> str = snaconfigrepo.gettaggedhospitallistfosna(userid.intValue());
			for (String s : str) {
				HospBean bean = new HospBean();
				bean.setHospitalName(s);
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response submit(EnableHospitalDischargeBean sna) {
		Response response = new Response();
		try {
			for (HospBean hosp : sna.getHospobj()) {
				Integer i = enabledischargerepo.checkduplicate(hosp.getHospitalCode());
				if (i == 0) {
					EnableHospitalDischargeModel model = new EnableHospitalDischargeModel();
					model.setCreatedBy(sna.getSnoid());
					model.setCreatedOn(Calendar.getInstance().getTime());
					model.setHospitalcode(hosp.getHospitalCode());
					model.setStatusflag(0);
					HospitalInformation hospital = hosprepo.findByhospitalCode(hosp.getHospitalCode());
					model.setStatecode(hospital.getDistrictcode().getStatecode().getStateCode());
					model.setDistrictcode(hospital.getDistrictcode().getDistrictcode());
					model.setApproveflag('A');
					enabledischargerepo.save(model);
				} else {
					EnableHospitalDischargeModel model = enabledischargerepo.findByhospitalcode(hosp.getHospitalCode());
					EnableHospitalDischargeLogModel modellog = new EnableHospitalDischargeLogModel();
					modellog.setApproveflag(model.getApproveflag());
					modellog.setCreateby(sna.getSnoid());
					modellog.setUpdateby(model.getUpdatedBy());
					modellog.setUpdateon(model.getUpdatedOn());
					modellog.setCreateon(Calendar.getInstance().getTime());
					modellog.setHospitalcode(model.getHospitalcode());
					modellog.setHospaprvlogid(model.getId());
					modellog.setStatusflag(model.getStatusflag());
					modellog.setStatecode(model.getStatecode());
					modellog.setDistrictcode(model.getDistrictcode());
					gospitaldischargelogrepo.save(modellog);
					model.setUpdatedBy(sna.getSnoid());
					model.setUpdatedOn(Calendar.getInstance().getTime());
					model.setApproveflag('A');
					enabledischargerepo.save(model);
				}
			}
			response.setMessage("Submitted Successfully");
			response.setStatus("200");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("400");
		}
		return response;
	}

	@Override
	public Response disable(EnableHospitalDischargeBean sna) {
		Response response = new Response();
		try {
			for (HospBean hosp : sna.getHospobjd()) {
				EnableHospitalDischargeModel model = enabledischargerepo.findByhospitalcode(hosp.getHospitalCode());
				if (model != null) {
					EnableHospitalDischargeLogModel modellog = new EnableHospitalDischargeLogModel();
					modellog.setApproveflag(model.getApproveflag());
					modellog.setCreateby(sna.getSnoid());
					modellog.setUpdateby(model.getUpdatedBy());
					modellog.setUpdateon(model.getUpdatedOn());
					modellog.setCreateon(Calendar.getInstance().getTime());
					modellog.setHospitalcode(model.getHospitalcode());
					modellog.setHospaprvlogid(model.getId());
					modellog.setStatusflag(model.getStatusflag());
					modellog.setStatecode(model.getStatecode());
					modellog.setDistrictcode(model.getDistrictcode());
					gospitaldischargelogrepo.save(modellog);
					model.setUpdatedBy(sna.getSnoid());
					model.setUpdatedOn(Calendar.getInstance().getTime());
					model.setApproveflag('N');
					enabledischargerepo.save(model);
				}
			}
			response.setMessage("Submitted Successfully");
			response.setStatus("200");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("400");
		}
		return response;
	}
}
