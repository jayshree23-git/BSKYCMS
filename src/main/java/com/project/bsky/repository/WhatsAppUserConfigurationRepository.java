package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.WhatsAppUserConfigurationModel;

@Repository
public interface WhatsAppUserConfigurationRepository extends JpaRepository<WhatsAppUserConfigurationModel, Long>{

	@Query("select statusflag from WhatsAppUserConfigurationModel where templateid=:templateId and userid=:userid")
	Integer getstatus(Long templateId, Long userid);

	@Query("from WhatsAppUserConfigurationModel where templateid=:templateId and userid=:userid")
	WhatsAppUserConfigurationModel getrecord(Long templateId, Long userid);

	@Query(value = "SELECT WUC.WHATSAPPCONFIGURATION_ID,\r\n"
			+ "        WUC.TEMPLATE_ID,\r\n"
			+ "        WUC.WHATSAPP_TEMPLATE_NAME,\r\n"
			+ "        WUC.GROUPID,\r\n"
			+ "        G.GROUP_TYPE_NAME,\r\n"
			+ "        WUC.USERID,\r\n"
			+ "        U1.FULL_NAME usernamwe,\r\n"
			+ "        WUC.CREATEDBY,\r\n"
			+ "        U2.FULL_NAME createdname,\r\n"
			+ "        TO_CHAR(WUC.CREATEDON,'DD-MON-YYYY') CREATEDON,\r\n"
			+ "        WUC.STATUSFLAG,\r\n"
			+ "        WT.ODISHA_GOV_TEMPLATE_ID\r\n"
			+ "FROM WHATSAPP_USER_CONFIGURATION WUC\r\n"
			+ "LEFT JOIN USERDETAILS U1 ON U1.USERID=WUC.USERID\r\n"
			+ "LEFT JOIN USERDETAILS U2 ON U2.USERID=WUC.CREATEDBY\r\n"
			+ "LEFT JOIN GROUP_TYPE G ON G.TYPE_ID=WUC.GROUPID\r\n"
			+ "LEFT JOIN WHATSAPP_TEMPLATE WT ON WT.TEMPLATE_ID=WUC.TEMPLATE_ID\r\n"
			+ "ORDER BY WUC.WHATSAPPCONFIGURATION_ID DESC",nativeQuery = true)
	List<Object[]> getwhatsappconfigviewlist();
	

}
