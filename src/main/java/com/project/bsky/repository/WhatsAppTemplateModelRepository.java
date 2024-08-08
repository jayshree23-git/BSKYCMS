package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.WhatsAppTemplateModel;
import com.project.bsky.model.WhatsAppUserConfigurationModel;


@Repository
public interface WhatsAppTemplateModelRepository extends JpaRepository<WhatsAppTemplateModel, Long>{

	@Query("from WhatsAppTemplateModel where bitStatus=0 order by templateName asc")
	List<WhatsAppTemplateModel> alldata();

}
