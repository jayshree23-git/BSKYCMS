/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.MailServiceErrorLog;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface MailServiceErrorLogRepository extends JpaRepository<MailServiceErrorLog, Integer> {

}
