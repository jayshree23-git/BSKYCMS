package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.QueryTypeBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.QueryType;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.QueryTypeRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.QueryTypeService;

@Service
public class QueryTypeServiceImpl implements QueryTypeService {

	@Autowired
	private UserDetailsRepository userDetailsRepo;

	@Autowired
	private QueryTypeRepository queryTypeRepository;

	@Autowired
	private Logger logger;

	@Override
	public Response saveQueryType(QueryTypeBean queryTypeBean) {
		Response response = new Response();
		try {

			QueryType queryType = new QueryType();
			Integer count = queryTypeRepository.countRowForCheckDuplicateType(queryTypeBean.getTypeName());

			if (count == 0) {
				queryType.setRemarks(queryTypeBean.getRemark());
				queryType.setTypeName(queryTypeBean.getTypeName());
				Calendar calendar = Calendar.getInstance();
				queryType.setCreatedOn((calendar.getTime()));
				UserDetails userdetails1 = userDetailsRepo.findById(Long.parseLong(queryTypeBean.getCreatedBy())).get();
				queryType.setCreatedBy(userdetails1);
				queryType.setStatusflag(0l);
				queryType.setUpdatedOn(null);
				queryType.setUpdatedBy(null);
				queryTypeRepository.save(queryType);
				response.setStatus("Success");
				response.setMessage("QueryType Successfully Inserted");
			} else {
				response.setStatus("Failed");
				response.setMessage("QueryType Already Exist");
			}
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
			return response;
		}
	}

	@Override
	public List<QueryType> getDetails() {
		return queryTypeRepository.findDetails();

	}

	@Override
	public QueryType getQueryById(Long typeId) {
		try {
			return queryTypeRepository.findById(typeId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}

	}

	@Override
	public Response updateQueryType(QueryTypeBean queryTypeBean) {
		Response response = new Response();
		try {

			QueryType queryType = queryTypeRepository.findById(Long.parseLong(queryTypeBean.getTypeId())).get();
			Integer count = queryTypeRepository.countRowForCheckDuplicateType(queryTypeBean.getTypeName());
			QueryType queryType1 = queryTypeRepository.findBytypeName(queryTypeBean.getTypeName());
			if (count == 0) {
				queryType.setRemarks(queryTypeBean.getRemark());
				queryType.setTypeName(queryTypeBean.getTypeName());
				Calendar calendar = Calendar.getInstance();
				UserDetails userdetails1 = userDetailsRepo.findById(Long.parseLong(queryTypeBean.getUpdatedBy())).get();
				queryType.setStatusflag(queryTypeBean.getStatusflag());
				queryType.setUpdatedOn(calendar.getTime());
				queryType.setUpdatedBy(userdetails1);
				queryTypeRepository.save(queryType);
				response.setStatus("Success");
				response.setMessage("QueryType Successfully Updated");
			} else if (queryType1.getTypeId() == Long.parseLong(queryTypeBean.getTypeId())
					&& queryTypeBean.getTypeName().equals(queryType1.getTypeName())) {
				queryType.setRemarks(queryTypeBean.getRemark());
				queryType.setTypeName(queryTypeBean.getTypeName());
				Calendar calendar = Calendar.getInstance();
				UserDetails userdetails1 = userDetailsRepo.findById(Long.parseLong(queryTypeBean.getUpdatedBy())).get();
				queryType.setStatusflag(queryTypeBean.getStatusflag());
				queryType.setUpdatedOn(calendar.getTime());
				queryType.setUpdatedBy(userdetails1);
				queryTypeRepository.save(queryType);
				response.setStatus("Success");
				response.setMessage("QueryType Successfully Updated");
			} else {
				response.setStatus("Failed");
				response.setMessage("QueryType Already Exist");
			}
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
			return response;
		}
	}

}
