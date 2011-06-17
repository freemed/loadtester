package org.freemedsoftware.util.loadtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class LoadTestStatisticsReport implements Serializable {

	private static final long serialVersionUID = -4310001861699530638L;

	@ElementList
	private List<LoadTestStatistics> statistics = new ArrayList<LoadTestStatistics>();

	public void setStatistics(List<LoadTestStatistics> statistics) {
		this.statistics = statistics;
	}

	public List<LoadTestStatistics> getStatistics() {
		return statistics;
	}

	public void addStatistics(LoadTestStatistics stats) {
		statistics.add(stats);
	}

}
