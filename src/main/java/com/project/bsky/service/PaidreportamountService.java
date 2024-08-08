package com.project.bsky.service;

import java.util.Map;

public interface PaidreportamountService {



	Map<String, Object> getsearchvalue(Long userid, String username, String fromdate, String todate, Long groupId,
			String state, String districtId, String hospitalCode)throws Exception;

	Map<String, Object> getsearchvalue(String paymentdate, Integer number, String totaldischarge,String Hospitalcode
			,String groupId)throws Exception;

}
