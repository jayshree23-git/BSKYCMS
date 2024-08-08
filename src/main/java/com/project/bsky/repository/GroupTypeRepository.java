package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.GroupTypeDetails;

@Repository
public interface GroupTypeRepository extends JpaRepository<GroupTypeDetails, Integer> {
	
	@Transactional
	@Modifying
	@Query("UPDATE GroupTypeDetails gtd SET gtd.status=1 where gtd.typeId=:typeId")
	void deleteById(@Param(value="typeId")Integer typeId);

	@Query("FROM GroupTypeDetails where typeId=:typeId")
	GroupTypeDetails findByTypeId(@Param("typeId")Integer typeId);
	
	@Query("SELECT g.typeId FROM GroupTypeDetails g WHERE g.groupTypeName=:groupTypeName")
	Integer getGoupIdByGroupName(@Param("groupTypeName")String groupTypeName);

	@Query("SELECT count(*) FROM GroupTypeDetails g WHERE g.typeId=:typeId")
	Integer checkTypeId(@Param("typeId")Integer typeId);

	@Query("FROM GroupTypeDetails where status=0 order by typeId")
	List<GroupTypeDetails> findAllDetails();

	GroupTypeDetails findBygroupTypeName(String groupTypeName);

	GroupTypeDetails findBytypeId(int parseInt);
	
	GroupTypeDetails findByGroupTypeNameIgnoreCase(String groupTypeName);


}
