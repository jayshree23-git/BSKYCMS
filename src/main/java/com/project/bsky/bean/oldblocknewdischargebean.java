package com.project.bsky.bean;

import java.util.Date;
import lombok.Data;

@Data
public class oldblocknewdischargebean {
	private Long userId;
	private Date fromDate;
	private Date toDate;
	private String stateId;
	private String districtId;
	private String hospitalId;
	private String mortality;
	private Long snaUserId;
	private String schemecategoryid;

}
