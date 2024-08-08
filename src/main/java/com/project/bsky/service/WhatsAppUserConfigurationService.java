package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.WhatsAppUserConfogurationBean;
import com.project.bsky.model.WhatsAppTemplateModel;
import com.project.bsky.model.WhatsAppUserConfigurationModel;

public interface WhatsAppUserConfigurationService {

	List<WhatsAppTemplateModel> getwhatsapptemplatename();

	List<Object> getUserNamebyGroupId(String groupid);


	Response savewhatappuserconfig(WhatsAppUserConfogurationBean whatsapptemplatebean) throws Exception;

	List<Object> getwhatsappconfigviewlist() throws Exception;

	Response inactiveonwhatsappconfig(Long configid, Integer status, Long updatedby) throws Exception;

}
