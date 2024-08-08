package com.project.bsky.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.project.bsky.bean.OnlinePostConfigBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.OnlinePostConfigModel;
import com.project.bsky.model.PostMasterModel;

public interface OnlinePostConfigService {

	List<PostMasterModel> getpostnamebyid() throws Exception;

	Response saveonlinepostconfig(OnlinePostConfigBean onlinepostconfigservice);

	void downLoadonlinepostDoc(String fileName,HttpServletResponse response);

	List<Object> getonlinepostconfiglist() throws Exception;

	Response updateonlinepostconfig(OnlinePostConfigBean onlinepostconfigbean);

	OnlinePostConfigModel getbyId(Long configid);

}
