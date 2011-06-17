package org.freemedsoftware.util.loadtest;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface LoadTestStep {

	public HtmlPage run(WebClient client, HtmlPage page) throws Exception;
	
	public boolean checkOutput(HtmlPage resultPage);

}
