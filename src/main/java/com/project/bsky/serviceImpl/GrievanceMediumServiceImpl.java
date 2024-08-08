package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.GrievanceMedium;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.GrievanceMediumRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.GrievanceMediumService;

@SuppressWarnings("unused")
@Service
public class GrievanceMediumServiceImpl implements GrievanceMediumService {

	@Autowired
	private UserDetailsRepository userdetailsrepo;

	@Autowired
	private GrievanceMediumRepository grivancemediumrepository;

	@Autowired
	private Logger logger;

	@Override
	public Response saveGrivanceMediumData(GrievanceMedium grivancemedium) {
		Response response = new Response();
		try {
			Integer chechduplicatetypename = grivancemediumrepository
					.findBymediumname(grivancemedium.getGrivancemediumname());
			if (chechduplicatetypename == 0) {
				UserDetails userdetails = userdetailsrepo.findById(grivancemedium.getCreatedby()).get();
				grivancemedium.setCreatedby1(userdetails);
				grivancemedium.setStatusFlag(0);
				grivancemedium.setCreatedon(Calendar.getInstance().getTime());
				GrievanceMedium grivancemedium1 = grivancemediumrepository.save(grivancemedium);

				if (grivancemedium != null) {
					response.setMessage("Data Insert Successfully");
					response.setStatus("Success");
				} else {
					response.setMessage("Something Went Wrong!! Data Can Not Save");
					response.setStatus("failed");
				}
			} else {
				response.setMessage("Grievancemedium  Already Exist");
				response.setStatus("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("failed");
		}
		return response;
	}

	@Override
	public List<GrievanceMedium> getGrivanceMediumData() {
		List<GrievanceMedium> list = new ArrayList<GrievanceMedium>();
		try {
			list = grivancemediumrepository.findAll();
			for (GrievanceMedium x : list) {
				x.setScreatedate(x.getCreatedon().toString());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response updateGrivanceMediumData(GrievanceMedium grivancemedium) {
		Response response = new Response();
		try {
			Integer chechduplicatetypename = grivancemediumrepository
					.findBymediumname(grivancemedium.getGrivancemediumname());
			if (chechduplicatetypename == 0) {
				GrievanceMedium grivancemedium1 = grivancemediumrepository.findById(grivancemedium.getId()).get();
				grivancemedium1.setGrivancemediumname(grivancemedium.getGrivancemediumname());
				grivancemedium1.setStatusFlag(grivancemedium.getStatusFlag());
				grivancemedium1.setUpdatedon(Calendar.getInstance().getTime());
				GrievanceMedium grivancemedium2 = grivancemediumrepository.save(grivancemedium1);
				if (grivancemedium != null) {
					response.setMessage("Data Update Successfully");
					response.setStatus("Success");
				} else {
					response.setMessage("Something Went Wrong!! Data Can Not update");
					response.setStatus("failed");
				}
			} else {
				GrievanceMedium grivancemedium1 = grivancemediumrepository
						.findBygrivancemediumname(grivancemedium.getGrivancemediumname());
				if (grivancemedium.getId() == grivancemedium1.getId() && grivancemedium.getGrivancemediumname()
						.equalsIgnoreCase(grivancemedium1.getGrivancemediumname())) {
					grivancemedium1.setGrivancemediumname(grivancemedium.getGrivancemediumname());
					grivancemedium1.setStatusFlag(grivancemedium.getStatusFlag());
					grivancemedium1.setUpdatedon(Calendar.getInstance().getTime());
					GrievanceMedium grivancemedium2 = grivancemediumrepository.save(grivancemedium1);
					if (grivancemedium2 != null) {
						response.setMessage("Data Update Successfully");
						response.setStatus("Success");
					} else {
						response.setMessage("Something Went Wrong!! Data Can Not update");
						response.setStatus("failed");
					}
				} else {
					response.setMessage("Grievancetype Name Already Exist");
					response.setStatus("failed");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("failed");
		}
		return response;
	}

	@Override
	public GrievanceMedium getgrievanceMediumbyId(Long userid) {
		GrievanceMedium grivancemedium = null;
		try {
			grivancemedium = grivancemediumrepository.findById(userid).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return grivancemedium;
	}

	@Override
	public List<GrievanceMedium> getGrivanceMediumList() {
		List<GrievanceMedium> list = new ArrayList<>();
		try {
			list = grivancemediumrepository.findByStatusFlagOrderByIdDesc(0);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

}
