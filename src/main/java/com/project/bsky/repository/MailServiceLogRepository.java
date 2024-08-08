/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.MailServiceLog;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface MailServiceLogRepository extends JpaRepository<MailServiceLog, Integer> {

}
