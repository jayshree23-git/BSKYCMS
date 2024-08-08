/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.HospitalOperator;
import com.project.bsky.model.UserDetails;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface HospitalOperatorRepository extends JpaRepository<HospitalOperator, Long> {

	@Query(value="select u.FULL_NAME,u.username,\r\n"
			+ "        u.mobileno,u.emailid,\r\n"
			+ "        S.STATENAME,D.DISTRICTNAME,\r\n"
			+ "        u.status_flag,u.HOSPITALOPERATOR_ID\r\n"
			+ "        from USER_HOSPITAL_OPERATOR u \r\n"
			+ "        left join State s on U.STATE_CODE = s.STATECODE \r\n"
			+ "        left join district d on U.DISTRICTCODE = d.DISTRICTCODE and d.STATECODE=s.STATECODE \r\n"
			+ "        where u.HOSPITALCODE=?1 order by u.CREATED_ON desc",nativeQuery=true) 
	List<Object[]> findbycode(String hospitalcode);

	@Query("from HospitalOperator where userid=:userId") 
	HospitalOperator getbyuserid(UserDetails userId);

	@Query(value="select h.HOSPITAL_CODE,h.HOSPITAL_NAME,h.MOBILE,\r\n"
			+ "u.FULL_NAME,u.mobileno,u.emailid,\r\n"
			+ "S.STATENAME,D.DISTRICTNAME,\r\n"
			+ "To_Char(u.CREATED_ON,'DD-MON-YYYY HH:MI:SS AM'),u.HOSPITALOPERATOR_ID,u.username\r\n"
			+ "from USER_HOSPITAL_OPERATOR u\r\n"
			+ "inner join hospital_info h on h.HOSPITAL_CODE=u.HOSPITALCODE and h.STATUS_FLAG=0\r\n"
			+ "left join State s on U.STATE_CODE = s.STATECODE \r\n"
			+ "left join district d on U.DISTRICTCODE = d.DISTRICTCODE and d.STATECODE=s.STATECODE \r\n"
			+ "where u.status_flag=2\r\n"
			+ "and h.HOSPITAL_CODE in (select HOSPITAL_CODE from user_sna_mapping where STATUS_FLAG=0 and SNO_USER_ID=?1)\r\n"
			+ "order by u.CREATED_ON desc",nativeQuery=true)
	List<Object[]> hospitalapplylistsna(Long userid);

	@Query(value="select h.HOSPITAL_CODE,h.HOSPITAL_NAME,h.MOBILE,\r\n"
			+ "        u.FULL_NAME,u.mobileno,u.emailid,\r\n"
			+ "        S.STATENAME,D.DISTRICTNAME,\r\n"
			+ "        To_Char(u.CREATED_ON,'DD-MON-YYYY HH:MI:SS AM'),u.HOSPITALOPERATOR_ID,u.username\r\n"
			+ "        from USER_HOSPITAL_OPERATOR u \r\n"
			+ "        inner join hospital_info h on h.HOSPITAL_CODE=u.HOSPITALCODE and h.STATUS_FLAG=0\r\n"
			+ "        left join State s on U.STATE_CODE = s.STATECODE \r\n"
			+ "        left join district d on U.DISTRICTCODE = d.DISTRICTCODE and d.STATECODE=s.STATECODE \r\n"
			+ "        where u.status_flag=2\r\n"
			+ "		   order by u.CREATED_ON desc",nativeQuery=true)
	List<Object[]> hospitalapplylistadmin();

	@Query("select Count(*) from HospitalOperator where lower(userName)=:userName")
	Integer checkduplicateuser(String userName);

	@Query(value="SELECT S.STATENAME,D.DISTRICTNAME,HO.HOSPITALCODE,H.HOSPITAL_NAME,COUNT(*) FROM USER_HOSPITAL_OPERATOR HO\r\n"
			+ "INNER JOIN HOSPITAL_INFO H ON H.HOSPITAL_CODE=HO.HOSPITALCODE AND H.STATUS_FLAG=0\r\n"
			+ "left join user_sna_mapping us on us.HOSPITAL_CODE=H.HOSPITAL_CODE and us.status_flag=0\r\n"
			+ "left join State s on H.STATE_CODE = s.STATECODE \r\n"
			+ "left join district d on H.DISTRICT_CODE = d.DISTRICTCODE and d.STATECODE=s.STATECODE \r\n"
			+ "WHERE HO.STATUS_FLAG=0\r\n"
			+ "AND H.STATE_CODE=DECODE(?1,NULL,H.STATE_CODE,?1)\r\n"
			+ "AND H.DISTRICT_CODE=DECODE(?2,NULL,H.DISTRICT_CODE,?2)\r\n"
			+ "AND H.HOSPITAL_CODE=DECODE(?3,NULL,H.HOSPITAL_CODE,?3)\r\n"
			+ "AND NVL(US.SNO_USER_ID,00)=DECODE(?4,NULL,NVL(US.SNO_USER_ID,00),?4)\r\n"
			+ "GROUP BY S.STATENAME,D.DISTRICTNAME,HO.HOSPITALCODE,H.HOSPITAL_NAME\r\n"
			+ "ORDER BY H.HOSPITAL_NAME",nativeQuery=true)
	List<Object[]> gethospwiseoperatorcount(String state, String dist, String hospital, Long userid);

	@Query("from HospitalOperator where userId.userId=:userId")
	HospitalOperator getbyuserid(Long userId);

	@Query(value="select S.STATENAME,D.DISTRICTNAME,\r\n"
			+ "        H.hospital_code,H.HOSPITAL_NAME,h.MOBILE,\r\n"
			+ "        ho.FULL_NAME,ho.MOBILENO\r\n"
			+ "from user_hospital_operator Ho\r\n"
			+ "INNER JOIN HOSPITAL_INFO H ON H.HOSPITAL_CODE=HO.HOSPITALCODE AND H.STATUS_FLAG=0\r\n"
			+ "left join user_sna_mapping us on us.HOSPITAL_CODE=H.HOSPITAL_CODE and us.status_flag=0\r\n"
			+ "left join State s on H.STATE_CODE = s.STATECODE \r\n"
			+ "left join district d on H.DISTRICT_CODE = d.DISTRICTCODE and d.STATECODE=s.STATECODE \r\n"
			+ "WHERE HO.STATUS_FLAG=0\r\n"
			+ "AND H.STATE_CODE=DECODE(?1,NULL,H.STATE_CODE,?1)\r\n"
			+ "AND H.DISTRICT_CODE=DECODE(?2,NULL,H.DISTRICT_CODE,?2)\r\n"
			+ "AND H.HOSPITAL_CODE=DECODE(?3,NULL,H.HOSPITAL_CODE,?3)\r\n"
			+ "AND NVL(US.SNO_USER_ID,00)=DECODE(?4,NULL,NVL(US.SNO_USER_ID,00),?4)\r\n"
			+ "order by H.HOSPITAL_NAME",nativeQuery=true)
	List<Object[]> gethospwiseoperatorlistreport(String state, String dist, String hospital, Long userid);

	@Query(value="select Count(*) \r\n"
			+ "from user_Hospital_Operator \r\n"
			+ "where FULL_NAME=?1 \r\n"
			+ "and MOBILENO=?2 \r\n"
			+ "and HOSPITALCODE=?3",nativeQuery=true)
	Integer checkduplicateuserbynameandmobile(String fullName, String mobileNo, String hospitalCode);
	
	@Query("from HospitalOperator where fullName=:fullName and mobileNo=:mobileNo and hospitalCode=:hospitalCode")
	HospitalOperator checkduplicateuserbynameandmobiledata(String fullName, String mobileNo, String hospitalCode);

}
