/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.WhatsappTemplate;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface WhatsappTemplateRepository extends JpaRepository<WhatsappTemplate, Integer> {
	@Query(value = "SELECT WT.id FROM WhatsappTemplate WT WHERE WT.templateName=:templateName")
	Integer getIdByTemplateName(String templateName);

	WhatsappTemplate getWhatsappTemplateById(Integer id);
}