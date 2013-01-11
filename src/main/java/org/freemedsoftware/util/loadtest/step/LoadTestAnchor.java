package org.freemedsoftware.util.loadtest.step;

import java.io.Serializable;

import org.freemedsoftware.util.loadtest.LoadTestStep;
import org.freemedsoftware.util.loadtest.LoadTester;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Root
public class LoadTestAnchor implements LoadTestStep, Serializable {

	private static final long serialVersionUID = -6148656101506813673L;

	private static Logger log = Logger.getLogger(LoadTestAnchor.class);

	@Attribute
	private String anchorName = "";

	@Element
	private String successString = "";

	@Attribute(required = false)
	private long waitTime = 2000L;

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public void setAnchorName(String anchorName) {
		this.anchorName = anchorName;
	}

	public String getAnchorName() {
		return anchorName;
	}

	public void setSuccessString(String successString) {
		this.successString = successString;
	}

	public String getSuccessString() {
		return successString;
	}

	public HtmlPage run(WebClient client, HtmlPage page) throws Exception {
		HtmlAnchor anchor = null;
		try {
			anchor = (HtmlAnchor) page.getElementById(getAnchorName());
		} catch (ElementNotFoundException e) {
		}
		return anchor.click();
	}

	public boolean checkOutput(HtmlPage resultPage) {
		if (resultPage.getWebResponse().getContentAsString().contains(getSuccessString())) {
			log.debug("Could not find text " + getSuccessString());
			if (LoadTester.DEBUG) {
				log.debug(resultPage.getWebResponse().getContentAsString());
			}
			return true;
		} else {
			return false;
		}
	}

}