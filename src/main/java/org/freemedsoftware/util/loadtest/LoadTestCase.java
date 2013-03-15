package org.freemedsoftware.util.loadtest;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class LoadTestCase implements Cloneable {

	@ElementList
	private List<LoadTestStep> steps = new ArrayList<LoadTestStep>();

	@Element
	private String initialUrl = "";

	@Attribute
	private boolean useJavascript = false;

	@Attribute
	private String name = "";

	public void addStep(LoadTestStep step) {
		steps.add(step);
	}

	public void setSteps(List<LoadTestStep> steps) {
		this.steps = steps;
	}

	public List<LoadTestStep> getSteps() {
		return steps;
	}

	public void setInitialUrl(String initialUrl) {
		this.initialUrl = initialUrl;
	}

	public String getInitialUrl() {
		return initialUrl;
	}

	public void setUseJavascript(boolean useJavascript) {
		this.useJavascript = useJavascript;
	}

	public boolean isUseJavascript() {
		return useJavascript;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public LoadTestCase clone() {
		try {
			final LoadTestCase result = (LoadTestCase) super.clone();
			ArrayList<LoadTestStep> a = new ArrayList<LoadTestStep>();
			for (LoadTestStep s : getSteps()) {
				a.add(s.clone());
			}
			return result;
		} catch (final CloneNotSupportedException ex) {
			throw new AssertionError();
		}
	}

}
