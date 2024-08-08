package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.PostMasterModel;

public interface PostMasterService {

	Response savepostname(PostMasterModel postmastermodel);

	
	List<Object> getpostname() throws Exception;


	Response updatepostname(PostMasterModel postmastermodel);


//	PostMasterModel getpostnamebyid(Long userid);

}
