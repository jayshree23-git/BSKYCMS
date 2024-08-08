/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.WhatsappMessageLog;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface WhatsappMessageLogRepository extends JpaRepository<WhatsappMessageLog, Integer> {

}
