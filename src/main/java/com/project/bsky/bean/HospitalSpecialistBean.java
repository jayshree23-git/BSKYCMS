/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rajendra.sahoo
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalSpecialistBean {

	private Long hospitalId;

	private String hospitalcode;

	private List<Specilistbean> specialist;

	private Long createdby;
	
	private Integer schemeid;

}
