package org.freemedsoftware.util.loadtest;

import java.io.Serializable;
import java.security.GeneralSecurityException;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class LoadTestThread implements Serializable, Runnable {

	private static final long serialVersionUID = 1705236613729865249L;

	private static Logger log = Logger.getLogger(LoadTestThread.class);

	private LoadTestCase testCase = null;
	private Long startRandomizationTime = 5000L;
	private LoadTestStatistics statisticsObject = null;

	public void run() {
		if (testCase == null) {
			log.error("Null test case object presented.");
			return;
		}
		
		long startTime = 0L;
		long endTime = 0L;

		long atomicStartTime = 0L;
		long atomicEndTime = 0L;

		long waitTime = (long) (Math.random() * startRandomizationTime);
		log.info("Waiting for " + waitTime + "ms  before execute");
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			log.error(e);
		}

		WebClient client = new WebClient(BrowserVersion.FIREFOX_3_6);
		client.setJavaScriptEnabled(testCase.isUseJavascript());
		client.setCssEnabled(false);
		CookieManager cookieManager = new CookieManager();
		cookieManager.setCookiesEnabled(true);
		client.setCookieManager(cookieManager);
		try {
			client.setUseInsecureSSL(true);
		} catch (GeneralSecurityException e) {
			log.error(e);
			return;
		}

		startTime = atomicStartTime = System.currentTimeMillis();
		HtmlPage currentPage = null;
		try {
			currentPage = client.getPage(testCase.getInitialUrl());
		} catch (Exception e) {
			log.error(e);
			endTime = atomicEndTime = System.currentTimeMillis();
			log.info("Terminated after " + (endTime - startTime) + "ms");
			statisticsObject.addToProcessingTime(atomicEndTime - atomicStartTime);
			statisticsObject.setFinished(true);
			statisticsObject.setSuccessful(false);
			return;
		}
		atomicEndTime = System.currentTimeMillis();

		// System.out.println(currentPage.getWebResponse().getContentAsString());
		log.debug("Got response of " + currentPage.getWebResponse().getContentAsString().length()
				+ " bytes in " + (atomicEndTime - atomicStartTime) + "ms");
		statisticsObject.addToProcessingTime(atomicEndTime - atomicStartTime);

		try {
			log.info("Found " + testCase.getSteps().size() + " test steps");
			for (LoadTestStep step : testCase.getSteps()) {
				long randomizedWaitTime = (long) (Math.random() * 2000L);
				log.info("Waiting for randomized < 2000ms time (" + randomizedWaitTime + "ms)");
				Thread.sleep(randomizedWaitTime);
				statisticsObject.addToIdleWaitTime(randomizedWaitTime);

				Long stepTimerBegin = System.currentTimeMillis();
				HtmlPage p = currentPage;
				for (Cookie c : client.getCookieManager().getCookies()) {
					log.debug("Cookie " + c.getName() + " : " + c.getValue());
				}
				currentPage = step.run(client, p);
				Long stepTimerEnd = System.currentTimeMillis();
				log.info("TIMER: Test step " + step.toString() + " ["
						+ (stepTimerEnd - stepTimerBegin) + "ms]");
				statisticsObject.addToProcessingTime(stepTimerEnd - stepTimerBegin);

				// System.out.println(currentPage.getWebResponse().getContentAsString());
				if (!step.checkOutput(currentPage)) {
					statisticsObject.setFinished(true);
					statisticsObject.setSuccessful(false);
					throw new Exception("Failed to pass verification");
				} else {
					log.info("Test step passed");
				}
			}
		} catch (Exception e) {
			log.error(e);
		}

		// Terminate everything
		client.closeAllWindows();

		endTime = System.currentTimeMillis();
		log.info("TIMER: Completed/terminated after " + (endTime - startTime) + "ms");
		statisticsObject.setFinished(true);
		statisticsObject.setSuccessful(true);
	}

	public void setTestCase(LoadTestCase testCase) {
		this.testCase = testCase;
	}

	public LoadTestCase getTestCase() {
		return testCase;
	}

	public void setStartRandomizationTime(Long startRandomizationTime) {
		this.startRandomizationTime = startRandomizationTime;
	}

	public Long getStartRandomizationTime() {
		return startRandomizationTime;
	}

	public void setStatisticsObject(LoadTestStatistics statisticsObject) {
		this.statisticsObject = statisticsObject;
	}

	public LoadTestStatistics getStatisticsObject() {
		return statisticsObject;
	}

}
