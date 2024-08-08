/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.SwasthyamitraMapping;


/**
 * @author priyanka.singh
 *
 */
@Repository
public interface SwasthyamitraMappingRepository extends JpaRepository<SwasthyamitraMapping, Long > {

//	@Query("select count(*) from SwasthyamitraMapping where userdetails.userId=:swasthyaId and statusflag=0")
//	Long countRowForCheckDuplicateType(Long swasthyaId);
	
	@Query("select count(*) from SwasthyamitraMapping where userdetails.userId=:swasthyaId and statusflag=0")
	Long countRowForCheckDuplicateType(Long swasthyaId);
	
	@Query("from SwasthyamitraMapping where userdetails.userId=:swasthyaId and statusflag=0")
	SwasthyamitraMapping countRowForCheckDuplicateTypea(Long swasthyaId);


	
	@Query("select us.mappingId,us.hospitalcode,us.userdetails.userId,us.statusflag,u.fullname,h.hospitalName,h.districtcode.districtcode,h.districtcode.statecode.stateCode,s.stateName,d.districtname"
			+ "	from SwasthyamitraMapping us "
			+ "	left join UserDetails u on u.userId=us.userdetails.userId"
			+ "	inner join HospitalInformation h on h.hospitalCode=us.hospitalcode"
			+ "	inner join State s on s.stateCode=h.districtcode.statecode.stateCode"
			+ "	inner join DistrictMaster d on d.districtcode=h.districtcode.districtcode"
			+ "	where d.statecode.stateCode=h.districtcode.statecode.stateCode")
		List<Object[]> findDetails();

		@Query("SELECT count(*) FROM SwasthyamitraMapping s WHERE s.userdetails.userId=:useId")
	Integer countRowForCheckDuplicateType(String useId);

		@Query("SELECT count(*) FROM SwasthyamitraMapping s WHERE s.hospitalcode=:hospitalCode and statusflag=0")
	Integer checkduplicateHospital(String hospitalCode);
		
		@Query("FROM SwasthyamitraMapping s WHERE s.hospitalcode=:hospitalCode  and statusflag=0")
		SwasthyamitraMapping checkduplicateHospitala(String hospitalCode);

		@Query("FROM SwasthyamitraMapping where userdetails.userId=:l AND statusflag=0")
		SwasthyamitraMapping getswas(Long l);

		@Query("Select h.hospitalCode,h.hospitalName ,h.districtcode.districtname,h.districtcode.statecode.stateName\r\n"
				+ "from SwasthyamitraMapping s\r\n"
				+ "inner JOIN HospitalInformation h on h.hospitalCode=s.hospitalcode\r\n"				
				+ "where statusflag=0 and s.userdetails.userId=:userid")
		List<Object[]> gethospilist(Long userid);

		@Query("from SwasthyamitraMapping where userdetails.userId=:userid and hospitalcode=:hospitalCode")
		SwasthyamitraMapping getforupdate(String hospitalCode, Long userid);

		@Query("from SwasthyamitraMapping where userdetails.userId=:userid")
		List<SwasthyamitraMapping> findByuserid(Long userid);

		@Transactional
		@Modifying
		@Query("update SwasthyamitraMapping set statusflag=1 where userdetails.userId=:userid")
		void inactiveuser(Long userid);

		@Query("select count(*) from SwasthyamitraMapping where userdetails.userId=:userid And hospitalcode=:hospitalCode")
		Integer cheakduplicate(Long userid, String hospitalCode);

		@Query(value="SELECT USMM.USER_ID,UD.FULL_NAME,COUNT(1)as count from user_swathya_mitra_mapping USMM\r\n"
				+ "INNER JOIN userdetails UD ON  UD.USERID=USMM.USER_ID \r\n"
				+ "WHERE UD.STATUS_FLAG=0\r\n"
				+ "AND   UD.USERID=?1\r\n"
				+ "AND   USMM.STATUSFLAG=0\r\n"
				+ "GROUP BY USMM.USER_ID,UD.FULL_NAME",nativeQuery=true)
		List<Object[]> getonesmuserdetail(Integer userId);

		@Query("from SwasthyamitraMapping where statusflag=0")
		List<SwasthyamitraMapping> findAllactivedata();

		@Query(value="SELECT s.statecode,s.statename,d.districtcode,d.districtname, \r\n"
				+ "        us.FULL_NAME, us.UESRNAME,G.GROUP_TYPE_NAME,us.STATUS_FLAG,\r\n"
				+ "        us.MOBILENO,us.bskyuserid,us.emailid,us.USER_ID,COUNT(um.HOSPITALCODE)COUNT \r\n"
				+ "        \r\n"
				+ "        FROM user_details_profile us\r\n"
				+ "left JOIN user_swathya_mitra_mapping UM ON um.user_id=us.USER_ID AND um.STATUSFLAG=0\r\n"
				+ "left JOIN group_type G ON G.TYPE_ID=us.GROUP_TYPE_ID\r\n"
				+ "LEFT JOIN STATE S ON US.STATE_CODE=S.STATECODE\r\n"
				+ "LEFT JOIN DISTRICT D ON US.DISTRICTCODE=D.DISTRICTCODE AND US.STATE_CODE=D.STATECODE\r\n"
				+ "WHERE G.TYPE_ID=14\r\n"
				+ "GROUP BY s.statecode,s.statename,d.districtcode,d.districtname, us.FULL_NAME,us.UESRNAME,\r\n"
				+ "G.GROUP_TYPE_NAME,us.STATUS_FLAG,us.MOBILENO,us.bskyuserid,us.emailid,us.USER_ID\r\n"
				+ "order by us.FULL_NAME",nativeQuery=true)
		List<Object[]> getswasthyaMitraDetails(Integer groupId, String stateId, String districtId);

		@Query("Select h.hospitalCode,h.hospitalName ,h.districtcode.districtname,h.districtcode.statecode.stateName\r\n"
				+ "from SwasthyamitraMapping s\r\n"
				+ "inner JOIN HospitalInformation h on h.hospitalCode=s.hospitalcode\r\n"				
				+ "where statusflag=0 and s.userdetails.userId=:swasthyaId")
		List<Object[]> gethospitalData(Long swasthyaId);

		@Query(value="SELECT s.statecode,s.statename,d.districtcode,d.districtname, \r\n"
				+ "			        us.FULL_NAME, us.UESRNAME,G.GROUP_TYPE_NAME,us.STATUS_FLAG,\r\n"
				+ "				       us.MOBILENO,us.bskyuserid,us.emailid,us.USER_ID,COUNT(um.HOSPITALCODE)COUNT \r\n"
				+ "			       FROM user_details_profile us\r\n"
				+ "				left JOIN user_swathya_mitra_mapping UM ON um.user_id=us.USER_ID AND um.STATUSFLAG=0\r\n"
				+ "				left JOIN group_type G ON G.TYPE_ID=us.GROUP_TYPE_ID\r\n"
				+ "				LEFT JOIN STATE S ON US.STATE_CODE=S.STATECODE\r\n"
				+ "				LEFT JOIN DISTRICT D ON US.DISTRICTCODE=D.DISTRICTCODE AND US.STATE_CODE=D.STATECODE\r\n"
				+ "				WHERE us.status_flag=0 and G.TYPE_ID=14 and (:statecode is null or s.statecode=:statecode)\r\n"
				+ "				and (:districtcode is null or d.districtcode=:districtcode)\r\n"
				+ "			GROUP BY us.FULL_NAME,us.UESRNAME, s.statecode,s.statename,d.districtcode,d.districtname,\r\n"
				+ "				G.GROUP_TYPE_NAME,us.STATUS_FLAG,us.MOBILENO,us.bskyuserid,us.emailid,us.USER_ID"
				+ " order by us.FULL_NAME",nativeQuery=true)
		List<Object[]> getswasthyaMitraFilter(@Param("statecode")String stateId,@Param("districtcode")String districtId);

		@Query(value="select us.USER_NAME,us.FULL_NAME,u.PHONE,\r\n"
				+ "to_char(trunc(us.CREATEDON),'DD-MON-YYYY') regdate,\r\n"
				+ "us.USER_ID\r\n"
				+ "from user_swasthya_mitra_details us \r\n"
				+ "INNER JOIN USERDETAILS U ON U.USERID=us.USER_ID AND U.STATUS_FLAG=0\r\n"
				+ "where us.USER_ID=?1\r\n"
				+ "and us.STATUSFLAG=0",nativeQuery=true)
		List<Object[]> getsmdetailsforregister(Long smid);

		@Query(value="select us.full_name,\r\n"
				+ "To_char(us.CREATEDON,'DD-MON-YYYY HH:MI:SS AM') AS CREATEED,\r\n"
				+ "To_char(us.UPDATEDON,'DD-MON-YYYY HH:MI:SS AM') AS UPDATEED,\r\n"
				+ "U.FULL_NAME allowedby\r\n"
				+ "from  user_swasthya_mitra_details us\r\n"
				+ "INNER JOIN USERDETAILS u ON u.USERID=us.updatedby AND u.STATUS_FLAG=0\r\n"
				+ "where us.STATUSFLAG=1 and us.user_id=?1\r\n"
				+ "order by us.UPDATEDON desc",nativeQuery=true)
		List<Object[]> getfacelogdetails(Long smid);
		
		

		

		

}
