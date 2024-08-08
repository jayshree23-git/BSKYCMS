package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PostMasterModel;
@Repository
public interface PostMasterRepository extends JpaRepository<PostMasterModel, Long> {

	@Query("from PostMasterModel where bitStatus=0")
	List<PostMasterModel> findallactivedata();

}
