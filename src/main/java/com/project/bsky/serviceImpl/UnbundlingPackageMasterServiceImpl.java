package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.UnbundlingSubmitBean;
import com.project.bsky.bean.Unbundlingpackagebean;
import com.project.bsky.service.UnbundlingPackageMasterService;

@Service
public class UnbundlingPackageMasterServiceImpl implements UnbundlingPackageMasterService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object> getpackageidandpackagename() {
		List<Object> data = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_UNBUNDLING_PAKAGEIDLIST")
					.registerStoredProcedureParameter("p_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_MSG", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_ACTION", "A");
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_MSG");
			while (rs.next()) {
				Unbundlingpackagebean list = new Unbundlingpackagebean();
				list.setPackageid(rs.getString(1));
				list.setPackagename(rs.getString(2));
				data.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return data;
	}

	@Override
	public Response getsubmitunbundlingpackage(UnbundlingSubmitBean resbean) {
		Response response = new Response();
		String packageid = null;
		StringBuffer bufferlist = new StringBuffer();
		Integer claimraiseInteger = null;

		if (resbean.getPackageid() != null) {
			for (String element : resbean.getPackageid()) {
				bufferlist.append(element + ",");
			}
			packageid = bufferlist.substring(0, bufferlist.length() - 1);
		}
		System.out.println(packageid);
		System.out.println(resbean.getUnbundlingpackage());
		System.out.println(resbean.getUserid());
		try {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return null;
	}

}
