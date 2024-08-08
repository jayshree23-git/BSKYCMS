package com.project.bsky.bean;

import java.util.Date;

import lombok.Data;

@Data

public class VitalStatisticsDto {

	private String vitalstatisticsname;
     private String vitalstatisticscode;
     private String vitalstatisticsdescription;
     private int BitStatus ;
 	private  Date setCreatedOn;
}
