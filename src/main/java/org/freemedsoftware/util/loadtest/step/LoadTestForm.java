package org.freemedsoftware.util.loadtest.step;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.freemedsoftware.util.loadtest.LoadTestStep;
import org.freemedsoftware.util.loadtest.LoadTestStepStatistics;
import org.freemedsoftware.util.loadtest.LoadTester;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

@Root
public class LoadTestForm implements LoadTestStep, Serializable {

	private static final long serialVersionUID = -266141374927505002L;

	private static Logger log = Logger.getLogger(LoadTestForm.class);

	@Attribute(required = false)
	private String stepName = "";

	@Attribute
	private String formName = "";

	@ElementMap(name = "values", entry = "value", key = "key", attribute = true, inline = true)
	private Map<String, String> values = new HashMap<String, String>();

	@Element(required = false)
	private String submitButton = "";

	@Element
	private String successString = "";

	@Element(required = false)
	private String linkSubmit = "";

	@Attribute(required = false)
	private long waitTime = 2000L;

	@Attribute
	private boolean stripSession = false;

	private LoadTestStepStatistics stepStatistics = new LoadTestStepStatistics();

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormName() {
		return formName;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void putValue(String key, String value) {
		values.put(key, value);
	}

	public void setSubmitButton(String submitButton) {
		this.submitButton = submitButton;
	}

	public String getSubmitButton() {
		return submitButton;
	}

	public void setSuccessString(String successString) {
		this.successString = successString;
	}

	public String getSuccessString() {
		return successString;
	}

	public void setStripSession(boolean stripSession) {
		this.stripSession = stripSession;
	}

	public boolean isStripSession() {
		return stripSession;
	}

	public void setLinkSubmit(String linkSubmit) {
		this.linkSubmit = linkSubmit;
	}

	public String getLinkSubmit() {
		return linkSubmit;
	}

	public LoadTestStepStatistics getStepStatistics() {
		return stepStatistics;
	}

	public HtmlPage run(WebClient client, HtmlPage page) throws Exception {
		stepStatistics.setTestStep(this);

		HtmlForm form = null;
		try {
			form = page.getFormByName(getFormName());
		} catch (ElementNotFoundException e) {
			try {
				form = (HtmlForm) page.getElementById(getFormName());
			} catch (Exception e2) {
				log.warn("Using first form on page, couldn't get " + getFormName());
				form = page.getForms().get(0);
			}
		}

		if (form.getActionAttribute().contains(";jsessionid") && isStripSession()) {
			log.warn("Stripping jsessionid");
			form.setActionAttribute(form.getActionAttribute().split(";")[0]);
		}

		for (String k : getValues().keySet()) {
			String v = getValues().get(k);
			log.debug("Setting " + k + " to '" + v + "'");
			HtmlInput i = form.getInputByName(k);
			i.setValueAttribute(v);
		}
		if (getLinkSubmit().isEmpty()) {
			HtmlSubmitInput button = null;
			try {
				button = form.getInputByName(getSubmitButton());
			} catch (ElementNotFoundException e) {
				button = form.getInputByValue(getSubmitButton());
			}

			long begin = System.currentTimeMillis();
			HtmlPage next = button.click();
			long end = System.currentTimeMillis();
			stepStatistics.setProcessingTime(end - begin);
			return next;
		} else {
			log.debug("Iterating for link with text " + getLinkSubmit());
			for (HtmlAnchor a : page.getAnchors()) {
				if (a.getTextContent().contentEquals(getLinkSubmit())
						|| a.getId().contentEquals(getLinkSubmit())) {
					long begin = System.currentTimeMillis();
					HtmlPage next = a.click();
					long end = System.currentTimeMillis();
					stepStatistics.setProcessingTime(end - begin);
					return next;
				}
			}
			log.debug("Could not find link with text or id " + getLinkSubmit());
			if (LoadTester.DEBUG) {
				// log.debug(page.asText());
			}
			throw new Exception("Could not find link with text or id " + getLinkSubmit());
		}
	}

	public boolean checkOutput(HtmlPage resultPage) {
		if (resultPage.getWebResponse().getContentAsString().contains(getSuccessString())) {
			log.debug("Could not find link with text or id " + getLinkSubmit());
			if (LoadTester.DEBUG) {
				log.debug(resultPage.getWebResponse().getContentAsString());
			}
			return true;
		} else {
			return false;
		}
	}

}
