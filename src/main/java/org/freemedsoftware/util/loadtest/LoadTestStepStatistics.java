package org.freemedsoftware.util.loadtest;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class LoadTestStepStatistics implements Serializable, Cloneable {

	private static final long serialVersionUID = -2705905137330011844L;

	@Attribute
	private String stepName = "";

	@Attribute
	private long processingTime = 0L;

	private LoadTestStep testStep = null;

	public void setProcessingTime(long processingTime) {
		this.processingTime = processingTime;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void addToProcessingTime(long time) {
		processingTime += time;
	}

	public void setTestStep(LoadTestStep testStep) {
		this.testStep = testStep;
		setStepName(getTestStep().getStepName());
	}

	public LoadTestStep getTestStep() {
		return testStep;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getStepName() {
		return stepName;
	}

	@Override
	public LoadTestStepStatistics clone() {
		try {
			final LoadTestStepStatistics result = (LoadTestStepStatistics) super.clone();
			return result;
		} catch (final CloneNotSupportedException ex) {
			throw new AssertionError();
		}
	}

	@Override
	public String toString() {
		return new StringBuilder().append("LoadTestStepStatistics[testcase=")
				.append(getTestStep() != null ? getTestStep().getStepName() : this.hashCode())
				.append(",").append("processingTime=").append(getProcessingTime()).append("ms]")
				.toString();
	}

}
