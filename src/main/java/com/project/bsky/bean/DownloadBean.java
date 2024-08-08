package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadBean {
  private String dateOfAdm;
  private String fileName;
  private String hospitalcode;

}
