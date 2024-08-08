/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.InteenalCommunicationBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.InternalCommunication_user;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.InternalCommunicationUserRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.InternalCommunicationSercice;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.InternalCommunicationDoc;
import com.project.bsky.util.SMSUtil;
import com.project.bsky.util.Validation;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class InternalCommunicationServiceimpl implements InternalCommunicationSercice {
	
	@Autowired
	private InternalCommunicationUserRepository internalcommrepo;
	
	@Autowired
	private UserDetailsRepository userRepo;
	
	@Autowired
	private JavaMailSender javamailsender;
	
	@Autowired
	private Logger logger;
	
	@Value("${file.path.InternalCommunication}")
	private String file;
	
	@PersistenceContext
	private EntityManager entityManager;	
	
	SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
	
	private static ResourceBundle bskyResourcesBundel4 = ResourceBundle.getBundle("fileConfiguration");

	@Override
	public List<InternalCommunication_user> getintcommuserlist() {
		List<InternalCommunication_user> list=new ArrayList<InternalCommunication_user>();
		try {
			list=internalcommrepo.getallactiveuser();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response saveintcomm(InteenalCommunicationBean bean) {
		Response response=new Response();
		String rs = null;
		try {
			UserDetails userdetails=userRepo.findById(bean.getUserid()).get();
			boolean mobile=Validation.validatemobile(userdetails.getPhone().toString());
			if(mobile) {
				if(userdetails.getEmail()!=null) {
						String reqfilepath=null;
						if(bean.getFile2()!=null) {
						String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					    String Month= new SimpleDateFormat("MMM").format(new Date());
						Map<String, String> filePath = InternalCommunicationDoc.createFilefornotiication(year,bean.getFile2(),Month );
						////System.out.println(filePath);
							for (Map.Entry<String, String> entry : filePath.entrySet()) {
								if (file.contains(entry.getKey()))
									reqfilepath=entry.getValue();
								////System.out.println(reqfilepath);
							}
						}
						StoredProcedureQuery storedProcedureQuery = this.entityManager
								.createStoredProcedureQuery("usp_internal_communication")
								.registerStoredProcedureParameter("P_ACTION_CODE", String.class, ParameterMode.IN)
								.registerStoredProcedureParameter("P_INTERNAL_ID", Long.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_requested_by", Long.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_requested_for", String.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_to_whom", Long.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN)
								.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_required_by", Date.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_priority", String.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_request_attachment", String.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_resolve_attachment", String.class, ParameterMode.IN)
								.registerStoredProcedureParameter("p_msg_out", String.class, ParameterMode.OUT)
								.registerStoredProcedureParameter("p_msg_out1", void.class, ParameterMode.REF_CURSOR);
						
						storedProcedureQuery.setParameter("P_ACTION_CODE", "I");
						storedProcedureQuery.setParameter("p_user_id", bean.getUserid());
						storedProcedureQuery.setParameter("P_INTERNAL_ID", null);
						storedProcedureQuery.setParameter("P_REMARKS", null);
						storedProcedureQuery.setParameter("p_requested_by", bean.getUserid());
						storedProcedureQuery.setParameter("p_requested_for", bean.getReqfor());
						storedProcedureQuery.setParameter("p_to_whom", bean.getTowhom());
						storedProcedureQuery.setParameter("p_description", bean.getDescription());
						storedProcedureQuery.setParameter("p_required_by", bean.getDate());
						storedProcedureQuery.setParameter("p_priority", bean.getPriority());
						storedProcedureQuery.setParameter("p_request_attachment", reqfilepath);
						storedProcedureQuery.setParameter("p_resolve_attachment", null);	
						
						storedProcedureQuery.execute();		
						rs= (String) storedProcedureQuery.getOutputParameterValue("p_msg_out");
						if(rs!=null) {
							InternalCommunication_user user=internalcommrepo.getuser(bean.getTowhom());
							String message="Dear "+user.getFullname()+"\r\n"+"\r\n"+
							"      A Request "+rs+" has been generated.\r\n"+"\r\n"+
							"      For more details Go to Our portal https://bskyportal.odisha.gov.in/ \r\n"
							+"\r\n"+
							"This is a system generated mail. Please do not reply to this email.";						
							String  Subject="The Request No. "+rs+" has been generated";
							String mail=user.getEmail();
							SMSUtil.sendmail(Subject,message,mail);
							response.setStatus("200");
							response.setMessage("Request Sucessfully Applied . Your Requested Token No is : "+rs);
						}else {
							response.setStatus("400");
							response.setMessage("Something Went Wrong");
						}
				}else {
					response.setStatus("400");
					response.setMessage("Invalid Email No ! Kindly Update");
				}
			}else {
				response.setStatus("400");
				response.setMessage("Invalid Mobile No ! Kindly Update");
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	@Override
	public List<InteenalCommunicationBean> getintcommlist(Long userid) {
		ResultSet rs = null;
		List<InteenalCommunicationBean> list=new ArrayList<InteenalCommunicationBean>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_internal_communication")
					.registerStoredProcedureParameter("P_ACTION_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTERNAL_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_requested_by", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_requested_for", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_whom", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_required_by", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_priority", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_request_attachment", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resolve_attachment", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_msg_out1", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("P_ACTION_CODE", "V");
			storedProcedureQuery.setParameter("p_user_id", userid);
			storedProcedureQuery.setParameter("p_requested_by",null);
			storedProcedureQuery.setParameter("p_requested_for", null);
			storedProcedureQuery.setParameter("p_to_whom", null);
			storedProcedureQuery.setParameter("p_description", null);
			storedProcedureQuery.setParameter("p_required_by", null);
			storedProcedureQuery.setParameter("p_priority", null);
			storedProcedureQuery.setParameter("p_request_attachment",null);
			storedProcedureQuery.setParameter("p_resolve_attachment", null);	
			storedProcedureQuery.setParameter("P_REMARKS", null);
			storedProcedureQuery.setParameter("P_INTERNAL_ID", null);
			storedProcedureQuery.execute();		
			rs= (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out1");
			while(rs.next()) {
				InteenalCommunicationBean intcommuser=new InteenalCommunicationBean();
				intcommuser.setIntcommid(rs.getLong(1));;
				intcommuser.setTaken(rs.getString(2));
				intcommuser.setReqbyname(rs.getString(3));
				intcommuser.setReqfor(rs.getString(4));
				intcommuser.setTowhomename(rs.getString(5));
				if(rs.getString(6)!=null) {
					Date d=new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(6));
					intcommuser.setReqbydate(sdf.format(d));
				}
				intcommuser.setPriority(rs.getString(7));
				intcommuser.setDescription(rs.getString(8));
				intcommuser.setReqatch(rs.getString(9));
				intcommuser.setResolveatch(rs.getString(10));
				if(rs.getString(11)!=null) {
					Date d=new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(11));
					intcommuser.setCreateon(sdf.format(d));
				}	
				intcommuser.setProgstatus(rs.getString(12));
				intcommuser.setRemarks(rs.getString(13));
				////System.out.println(intcommuser);
				list.add(intcommuser);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<InteenalCommunicationBean> getintcommtasklist(Long userid) {
		ResultSet rs = null;
		List<InteenalCommunicationBean> list=new ArrayList<InteenalCommunicationBean>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_internal_communication")
					.registerStoredProcedureParameter("P_ACTION_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTERNAL_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_requested_by", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_requested_for", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_whom", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_required_by", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_priority", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_request_attachment", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resolve_attachment", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_msg_out1", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("P_ACTION_CODE", "V1");
			storedProcedureQuery.setParameter("p_user_id", null);
			storedProcedureQuery.setParameter("p_requested_by",null);
			storedProcedureQuery.setParameter("p_requested_for", null);
			storedProcedureQuery.setParameter("p_to_whom", userid);
			storedProcedureQuery.setParameter("p_description", null);
			storedProcedureQuery.setParameter("p_required_by", null);
			storedProcedureQuery.setParameter("p_priority", null);
			storedProcedureQuery.setParameter("p_request_attachment",null);
			storedProcedureQuery.setParameter("p_resolve_attachment", null);	
			storedProcedureQuery.setParameter("P_REMARKS", null);
			storedProcedureQuery.setParameter("P_INTERNAL_ID", null);
			storedProcedureQuery.execute();		
			rs= (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out1");
			while(rs.next()) {
				InteenalCommunicationBean intcommuser=new InteenalCommunicationBean();
				intcommuser.setIntcommid(rs.getLong(1));;
				intcommuser.setTaken(rs.getString(2));
				intcommuser.setReqbyname(rs.getString(3));
				intcommuser.setReqfor(rs.getString(4));
				intcommuser.setTowhomename(rs.getString(5));
				if(rs.getString(6)!=null) {
					Date d=new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(6));
					intcommuser.setReqbydate(sdf.format(d));
				}
				intcommuser.setPriority(rs.getString(7));
				intcommuser.setDescription(rs.getString(8));
				intcommuser.setReqatch(rs.getString(9));
				intcommuser.setResolveatch(rs.getString(10));
				if(rs.getString(11)!=null) {
					Date d=new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(11));
					intcommuser.setCreateon(sdf.format(d));
				}	
				intcommuser.setProgstatus(rs.getString(12));
				intcommuser.setRemarks(rs.getString(13));
				////System.out.println(intcommuser);
				list.add(intcommuser);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@Override
	public Response updateintcomm(MultipartFile form, String remarks, Long userid, Long commid) {
		Response response=new Response();
		String rs = null;
		try {
			String reqfilepath=null;
			if(form!=null) {
			String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		    String Month= new SimpleDateFormat("MMM").format(new Date());
			Map<String, String> filePath = InternalCommunicationDoc.createFilefornotiication(year,form,Month );
			////System.out.println(filePath);
				for (Map.Entry<String, String> entry : filePath.entrySet()) {
					if (file.contains(entry.getKey()))
						reqfilepath=entry.getValue();
					////System.out.println(reqfilepath);
				}
			}
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_internal_communication")
					.registerStoredProcedureParameter("P_ACTION_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTERNAL_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_requested_by", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_requested_for", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_whom", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_required_by", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_priority", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_request_attachment", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resolve_attachment", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_msg_out1", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("P_ACTION_CODE", "U");
			storedProcedureQuery.setParameter("p_user_id",userid );
			storedProcedureQuery.setParameter("P_INTERNAL_ID", commid);
			storedProcedureQuery.setParameter("P_REMARKS",remarks );
			storedProcedureQuery.setParameter("p_requested_by", null);
			storedProcedureQuery.setParameter("p_requested_for", null);
			storedProcedureQuery.setParameter("p_to_whom", null);
			storedProcedureQuery.setParameter("p_description", null);
			storedProcedureQuery.setParameter("p_required_by", null);
			storedProcedureQuery.setParameter("p_priority", null);
			storedProcedureQuery.setParameter("p_request_attachment", null);
			storedProcedureQuery.setParameter("p_resolve_attachment", reqfilepath);			
			storedProcedureQuery.execute();				
				response.setStatus("200");
				response.setMessage("Request Sucessfully Resolved");			
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}
	
	
	@Override
	public void downLoadPassbook(String fileName, String year, HttpServletResponse response, String month) {
		String folderName = null;
		try {
			folderName = bskyResourcesBundel4.getString("folder.IntCommunication");
			CommonFileUpload.downLoadintcommunicationdoc(fileName, year, response,month, folderName);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		
	}
	
	
	

	

	

	
    
	
}
