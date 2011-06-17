package org.freemedsoftware.util.loadtest.step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.freemedsoftware.util.loadtest.LoadTestStep;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Root
public class LoadTestLink implements LoadTestStep, Serializable {

	private static final long serialVersionUID = 6380833317567334995L;

	private static Logger log = Logger.getLogger(LoadTestLink.class);

	@Element
	private String regex = "";

	@Element
	private String successString = "";

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

	public void setSuccessString(String successString) {
		this.successString = successString;
	}

	public String getSuccessString() {
		return successString;
	}

	public HtmlPage run(WebClient client, HtmlPage page) throws Exception {
		Pattern pattern = Pattern.compile(getRegex());
		List<HtmlAnchor> aElements = page.getAnchors();
		List<HtmlAnchor> matches = new ArrayList<HtmlAnchor>();
		for (HtmlAnchor a : aElements) {
			String href = a.getHrefAttribute();
			Matcher matcher = pattern.matcher(href);
			log.trace("Found a href " + href);
			if (matcher.find()) {
				matches.add(a);
			}
		}
		log.debug("Found " + matches.size() + " link matches");
		HtmlAnchor a = matches.get((int) (Math.random() * matches.size()));
		log.info("Clicking on anchor URL " + a.getHrefAttribute());
		return a.click();
	}

	public boolean checkOutput(HtmlPage resultPage) {
		if (resultPage.getWebResponse().getContentAsString().contains(
				getSuccessString())) {
			return true;
		} else {
			return false;
		}
	}

}
