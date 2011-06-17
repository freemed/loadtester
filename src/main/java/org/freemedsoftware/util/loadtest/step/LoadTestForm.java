package org.freemedsoftware.util.loadtest.step;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.freemedsoftware.util.loadtest.LoadTestStep;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

@Root
public class LoadTestForm implements LoadTestStep, Serializable {

	private static final long serialVersionUID = -266141374927505002L;

	private static Logger log = Logger.getLogger(LoadTestForm.class);

	@Attribute
	private String formName = "";

	@ElementMap(name = "values", entry = "value", key = "key", attribute = true, inline = true)
	private Map<String, String> values = new HashMap<String, String>();

	@Element
	private String submitButton = "";

	@Element
	private String successString = "";

	@Attribute
	private boolean stripSession = false;

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

	public HtmlPage run(WebClient client, HtmlPage page) throws Exception {
		HtmlForm form = null;
		try {
			form = page.getFormByName(getFormName());
		} catch (ElementNotFoundException e) {
			log.warn("Using first form on page, couldn't get " + getFormName());
			form = page.getForms().get(0);
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
		HtmlSubmitInput button = null;
		try {
			button = form.getInputByName(getSubmitButton());
		} catch (ElementNotFoundException e) {
			button = form.getInputByValue(getSubmitButton());
		}
		return button.click();
	}

	public boolean checkOutput(HtmlPage resultPage) {
		if (resultPage.getWebResponse().getContentAsString().contains(getSuccessString())) {
			return true;
		} else {
			return false;
		}
	}

}
