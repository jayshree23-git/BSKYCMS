package com.project.bsky.service;

import java.util.Date;
import java.util.List;

public interface SnaRejectedLIstService {

	List<Object> snaRejetdList(Long userId, Date fromdate, Date toDate, String stateCode, String distCode,
			String hospitalCode);

}
