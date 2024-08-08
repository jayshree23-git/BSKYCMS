/**
 * 
 */
package com.project.bsky.bean;

import java.sql.ResultSet;
import java.util.List;

import lombok.Data;

/**
 * @author priyanka.singh
 *
 */
@Data
public class UrnWiseDataBean {
	
private UrnWiseDetailsBean basicinformationObj;
	private List<Object> actionTakenHistory;
	private List<Object> multiplePackageBlocking;
	private List<Object> preAuthLog;
	private List<Object> treatmentHistory;
	private List<Object> noncompliance;

}
