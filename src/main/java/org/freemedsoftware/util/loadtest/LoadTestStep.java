package org.freemedsoftware.util.loadtest;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface LoadTestStep extends Cloneable {

	public HtmlPage run(WebClient client, HtmlPage page) throws Exception;
	
	public boolean checkOutput(HtmlPage resultPage);
	
	public long getWaitTime();
	
	public String getStepName();
	
	public LoadTestStepStatistics getStepStatistics();
	
	public void setStepStatistics(LoadTestStepStatistics stepStatistics);
	
	public LoadTestStep clone();

}
