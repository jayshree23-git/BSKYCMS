/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.PrimaryLinkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.FunctionMaster;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.PrimaryLink;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserMenuMapping;
import com.project.bsky.repository.FunctionMasterRepository;
import com.project.bsky.repository.GlobalLinkRepository;
import com.project.bsky.repository.PrimaryLinkRepository;
import com.project.bsky.repository.UserMenuMappingRepository;
import com.project.bsky.service.PrimaryLinkService;
import com.project.bsky.util.ClassHelperUtils;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class PrimaryLInkServiceimpl implements PrimaryLinkService {

	@Autowired
	private GlobalLinkRepository globallinkrepo;

	@Autowired
	private UserMenuMappingRepository usermenumapping;

	@Autowired
	private FunctionMasterRepository functionMasterRepository;

	@Autowired
	private PrimaryLinkRepository primarylinkrepo;

	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public List<GlobalLink> getgloballinklist() {

		return globallinkrepo.allactiveglobaldata();
	}

	@Override
	public List<FunctionMaster> getgloballink() {

		return functionMasterRepository.allactivefunctiondata();
	}

	@Override
	public Response save(PrimaryLinkBean primarylinkbean) {
		Response response = new Response();
		try {
			Integer count = primarylinkrepo.cheakduplicate(primarylinkbean.getFunctionId());
			Integer countorder = primarylinkrepo.cheakduplicateorder(primarylinkbean.getGlobalLinkId(),
					primarylinkbean.getOrder());
			if (count == 0 && countorder == 0) {
				PrimaryLink primarylink = new PrimaryLink();
				primarylink.setPrimaryLinkName(primarylinkbean.getPrimaryLinkName());
				GlobalLink globallink = globallinkrepo.findById(primarylinkbean.getGlobalLinkId()).get();
				primarylink.setGlobalLink(globallink);
				FunctionMaster functionMaster = functionMasterRepository.findById(primarylinkbean.getFunctionId())
						.get();
				primarylink.setFunctionMaster(functionMaster);
				primarylink.setOrder(primarylinkbean.getOrder());
				primarylink.setDescription(primarylinkbean.getDescription());
				primarylink.setCreatedBy(primarylinkbean.getCreatedBy());
				primarylink.setBitStatus(0);
				primarylinkrepo.save(primarylink);
				response.setMessage("Primary Link Added");
				response.setStatus("Success");
			} else if (countorder != 0) {
				response.setMessage("Order No Already Exist");
				response.setStatus("Failed");
			} else {
				response.setMessage("Primary Link Already Exist");
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
	public List<PrimaryLink> findall() {

		return primarylinkrepo.findAlldesc();
	}

	@Override
	public Response deleteprimarylink(Long userid) {
		Response response = new Response();
		try {
			PrimaryLink primarylink = primarylinkrepo.findById(userid).get();
			primarylink.setBitStatus(1);
			primarylinkrepo.save(primarylink);
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
	public PrimaryLink getprimarylinkbyid(Long userid) {
		PrimaryLink primarylink = null;
		try {
			primarylink = primarylinkrepo.findById(userid).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return primarylink;

	}

	public Response Update(PrimaryLinkBean primarylinkbean) {
		Response response = new Response();
		PrimaryLink primarylink = primarylinkrepo.findById(primarylinkbean.getId()).get();
		primarylink.setPrimaryLinkName(primarylinkbean.getPrimaryLinkName());
		GlobalLink globallink = globallinkrepo.findById(primarylinkbean.getGlobalLinkId()).get();
		primarylink.setGlobalLink(globallink);
		FunctionMaster functionMaster = functionMasterRepository.findById(primarylinkbean.getFunctionId()).get();
		primarylink.setFunctionMaster(functionMaster);
		primarylink.setDescription(primarylinkbean.getDescription());
		if (primarylinkbean.getIsActive() == 1) {
			Integer countassignpmlink = usermenumapping.countassignpmlink(primarylink.getPrimaryLinkId());
			if (countassignpmlink > 0) {
				response.setMessage("Primary Link Assigned to Someone");
				response.setStatus("Failed");
				return response;
			}
		}
		primarylink.setBitStatus(primarylinkbean.getIsActive());
		primarylink.setOrder(primarylinkbean.getOrder());
		primarylink.setUpdatedBy(primarylinkbean.getUpdatedby());
		primarylink.setUpdatedOn(date);
		primarylinkrepo.save(primarylink);
		response.setMessage("Primary Link Update");
		response.setStatus("Success");
		return response;
	}

	@Override
	public Response update(PrimaryLinkBean primarylinkbean) {
		Response response = new Response();
		try {
			Integer count = primarylinkrepo.cheakduplicate(primarylinkbean.getFunctionId());
			Integer countorder = primarylinkrepo.cheakduplicateorder(primarylinkbean.getGlobalLinkId(),
					primarylinkbean.getOrder());
			PrimaryLink primarylink1 = primarylinkrepo.cheak(primarylinkbean.getFunctionId());
			PrimaryLink primarylink2 = primarylinkrepo.cheakorder(primarylinkbean.getGlobalLinkId(),
					primarylinkbean.getOrder());
			if (count == 0 && countorder == 0) {
				response = Update(primarylinkbean);
			} else if (count != 0 && countorder == 0) {
				if (primarylinkbean.getId().equals(primarylink1.getPrimaryLinkId())
						&& primarylinkbean.getFunctionId().equals(primarylink1.getFunctionMaster().getFunctionId())) {
					response = Update(primarylinkbean);
				} else {
					response.setMessage("Primary Link Already Exist");
					response.setStatus("Failed");
				}
			} else if (count == 0 && countorder != 0) {
				if (primarylinkbean.getId().equals(primarylink2.getPrimaryLinkId())
						&& primarylinkbean.getOrder().equals(primarylink2.getOrder())
						&& primarylinkbean.getGlobalLinkId().equals(primarylink2.getGlobalLink().getGlobalLinkId())) {
					response = Update(primarylinkbean);
				} else {
					response.setMessage("Order No Already Exist");
					response.setStatus("Failed");
				}
			} else {
				if (primarylinkbean.getId().equals(primarylink1.getPrimaryLinkId())
						&& primarylinkbean.getFunctionId().equals(primarylink1.getFunctionMaster().getFunctionId())
						&& primarylinkbean.getGlobalLinkId().equals(primarylink1.getGlobalLink().getGlobalLinkId())
						&& primarylinkbean.getId().equals(primarylink2.getPrimaryLinkId())
						&& primarylinkbean.getOrder().equals(primarylink2.getOrder())
						&& primarylinkbean.getGlobalLinkId().equals(primarylink2.getGlobalLink().getGlobalLinkId())) {
					response = Update(primarylinkbean);
				} else {
					response.setMessage("Order No Already Exist");
					response.setStatus("Failed");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<PrimaryLink> filterdata(String globalid, String primaryid, String functionid) {
		List<PrimaryLink> list = new ArrayList<PrimaryLink>();
		if (primaryid != "") {
			list.add(primarylinkrepo.findById(Long.parseLong(primaryid)).get());
			return list;
		} else if (globalid != "" && primaryid == "") {
			Long globalid1 = Long.parseLong(globalid);
			return primarylinkrepo.findbyglobalid(globalid1);
		} else {
			return primarylinkrepo.findAll();
		}

	}

	@Override
	public List<PrimaryLink> getrespmlist(Long gid) {

		return primarylinkrepo.getrespmlist(gid);
	}

	@Override
	public Map<String, Object> checkPrimaryLinkAccess(Map<String, String> mapRequest) {
		UserDetails userDetails = new UserDetails();
		Map<String, Object> map = new HashMap<>();
		JSONObject json = new JSONObject();
		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));
			userDetails.setUserId(json.has("userId") ? json.getLong("userId") : 0L);

			FunctionMaster func = functionMasterRepository.findByUrl(json.has("url") ? json.getString("url") : null);
			if (func != null) {
				if (func.getBitStatus() == 0) {
					List<PrimaryLink> list = primarylinkrepo.getPrimaryLinkList(func.getFunctionId());
					if (!list.isEmpty()) {
						for (PrimaryLink primaryLink : list) {
							if (primaryLink != null) {
								if (primaryLink.getBitStatus() == 0) {
									Integer count = usermenumapping.checkPrimaryLinkAccess(
											json.has("userId") ? json.getInt("userId") : 0,
											primaryLink.getPrimaryLinkId());
									if (count > 0) {
										String globalLink = primarylinkrepo
												.getGlobalLinkName(primaryLink.getPrimaryLinkId());
										map.put("status", "success");
										map.put("message", globalLink);
										break;
									} else {
										map.put("status", "failed");
										map.put("message", "Failed");
									}
								} else {
									map.put("status", "failed");
									map.put("message", "Failed");
								}
							}
						}
					} else {
						map.put("status", "failed");
						map.put("message", "Failed");
					}
				} else {
					map.put("status", "failed");
					map.put("message", "Failed");
				}
			} else {
				map.put("status", "success");
			}
		} catch (Exception e) {
			map.put("status", "failed");
			map.put("message", e.getMessage());
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return map;
	}

	@Override
	public List<UserMenuMapping> getMISReportList(Integer userId, Integer globalId) {
		return usermenumapping.findMISReportsLink(userId, globalId);
	}

}
