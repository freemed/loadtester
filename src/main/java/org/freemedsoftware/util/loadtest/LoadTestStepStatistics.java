package org.freemedsoftware.util.loadtest;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class LoadTestStepStatistics implements Serializable {

	private static final long serialVersionUID = -2705905137330011844L;

	@Attribute
	private String stepName = "";

	@Attribute
	private boolean finished = false;

	@Attribute
	private boolean successful = false;

	@Attribute
	private long processingTime = 0L;

	@Attribute
	private long threadId = 0L;

	private LoadTestStep testStep = null;

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public boolean isSuccessful() {
		return successful;
	}

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

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getStepName() {
		return stepName;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("LoadTestStepStatistics[threadId=").append(getThreadId())
				.append(",").append("testcase=")
				.append(getTestStep() != null ? getTestStep().getStepName() : this.hashCode())
				.append(",").append("processingTime=").append(getProcessingTime()).append("ms]")
				.toString();
	}

}
