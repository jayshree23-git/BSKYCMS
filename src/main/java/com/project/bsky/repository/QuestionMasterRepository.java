/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.QuestionMaster;

/**
 * @author santanu.barad
 *
 */
public interface QuestionMasterRepository extends JpaRepository<QuestionMaster, Long> {

	@Query("from QuestionMaster order by questionId desc")
	List<QuestionMaster> findAllByOrderByQuestionIdDesc();

}
