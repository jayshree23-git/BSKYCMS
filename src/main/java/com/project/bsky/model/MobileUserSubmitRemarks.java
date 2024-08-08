/**
 * 
 */
package com.project.bsky.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author preetam.mishra
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileUserSubmitRemarks {
  private String userName;
  private String urn;
  private String claimId;
  private String remarks;
  private Long approvedAmount;
  private String actionId;
}
