package org.freemedsoftware.util.loadtest;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class LoadTestStatistics implements Serializable {

	private static final long serialVersionUID = -6459721833597952444L;
	
	@Attribute
	private String testName = "";
	
	@Attribute
	private boolean finished = false;
	
	@Attribute
	private boolean successful = false;
	
	@Attribute
	private long idleWaitTime = 0L;
	
	@Attribute
	private long processingTime = 0L;
	
	@Attribute
	private long threadId = 0L;
	
	@Attribute
	private int loopCount = 0;

	private LoadTestCase testCase = null;

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

	public void setIdleWaitTime(long idleWaitTime) {
		this.idleWaitTime = idleWaitTime;
	}

	public long getIdleWaitTime() {
		return idleWaitTime;
	}

	public void addToIdleWaitTime(long time) {
		idleWaitTime += time;
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

	public void setTestCase(LoadTestCase testCase) {
		this.testCase = testCase;
		setTestName(getTestCase().getName());
	}

	public LoadTestCase getTestCase() {
		return testCase;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestName() {
		return testName;
	}

	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}

	public int getLoopCount() {
		return loopCount;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("LoadTestStatistics[threadId=").append(getThreadId())
				.append(",").append("testcase=").append(
						getTestCase() != null ? getTestCase().getName() : this.hashCode()).append(
						",").append("processingTime=").append(getProcessingTime()).append("ms,")
				.append("idleWaitTime=").append(getIdleWaitTime()).append("ms]").toString();
	}

}
