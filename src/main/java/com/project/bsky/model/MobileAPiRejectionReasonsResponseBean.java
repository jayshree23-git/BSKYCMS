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
public class MobileAPiRejectionReasonsResponseBean {
  private Long id;
  private String reason;
}
