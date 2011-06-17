package org.freemedsoftware.util.loadtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class LoadTestList implements Serializable {

	private static final long serialVersionUID = -1056488031997970507L;

	@ElementList(entry = "loadTestCase", inline = true)
	private List<LoadTestCase> cases = new ArrayList<LoadTestCase>();

	@Attribute
	private Long startRandomizationTime = 5000L;

	public void setCases(List<LoadTestCase> cases) {
		this.cases = cases;
	}

	public List<LoadTestCase> getCases() {
		return cases;
	}

	public void addCase(LoadTestCase testCase) {
		cases.add(testCase);
	}

	public void setStartRandomizationTime(Long startRandomizationTime) {
		this.startRandomizationTime = startRandomizationTime;
	}

	public Long getStartRandomizationTime() {
		return startRandomizationTime;
	}

}
