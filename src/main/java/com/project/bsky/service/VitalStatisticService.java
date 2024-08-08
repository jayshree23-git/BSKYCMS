package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;

import com.project.bsky.model.VitalStatistics;

public interface VitalStatisticService {

	Response savevitalStatistics(VitalStatistics vitalstatistics);

	List<VitalStatistics> getvitalStatistics();

	Response deletevitalstatistics(Long vitalStatisticsId);

	VitalStatistics getVitalstatistics(Long vitalStatisticsId);

	Response update(Long vitalStatisticsId, VitalStatistics vitalstatistics);

	Long checkDuplicateVitalstatisticsName(String vitalstatisticsname);

	Long checkDuplicateVitalstatisticsCode(String vitalstatisticscode);

}
