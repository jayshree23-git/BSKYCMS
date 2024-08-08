package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

	@Query("from Group where isActive=0")
	List<Group> findAllactivedata();

	@Query("FROM Group order by groupId desc")
	List<Group> findAllActive();

	@Transactional
	@Modifying
	@Query("UPDATE Group SET isActive=1 where groupId=:groupId")
	void delete(Integer groupId);

	@Query("SELECT g.groupId FROM Group g WHERE g.groupName=:groupName")
	Integer getGoupIdByGroupName(@Param("groupName") String groupName);

	Group findBygroupName(String groupName);

}
