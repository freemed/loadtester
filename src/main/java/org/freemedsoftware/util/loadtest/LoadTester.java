package org.freemedsoftware.util.loadtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class LoadTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		URL log4j = LoadTester.class.getClassLoader().getResource("log4j.properties");
		PropertyConfigurator.configure(log4j);

		Logger log = Logger.getLogger(LoadTester.class);

		// Set logging for htmlunit
		System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "warn");

		// create Options object
		Options options = new Options();
		options.addOption("c", "count", true, "test thread count");
		options.addOption("o", "output", true, "output stats report as file");
		options.addOption("r", "repeat", true, "repeat load test (default is 1)");
		options.addOption("t", "testcase", true, "test case definition");
		options.addOption("w", "wait", true, "maximum start wait time override");
		options.addOption("h", "help", false, "help");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		HelpFormatter formatter = new HelpFormatter();
		if (cmd.hasOption("h")) {
			formatter.printHelp("LoadTester", options);
			System.exit(1);
		}

		Integer loopCount = Integer.parseInt(cmd.getOptionValue("r", "1"));
		Integer testCount = Integer.parseInt(cmd.getOptionValue("c", "1"));

		Serializer serializer = new Persister();
		File source = new File(cmd.hasOption("t") ? cmd.getOptionValue("t") : "testcase/test.xml");
		LoadTestList testCaseList = null;
		try {
			testCaseList = serializer.read(LoadTestList.class, source);
		} catch (FileNotFoundException ex) {
			System.err.println("Unable to open test case file " + source.getAbsolutePath());
			formatter.printHelp("LoadTester", options);
			System.exit(1);
		}

		LoadTestStatisticsReport report = new LoadTestStatisticsReport();

		long randomizationTime = cmd.hasOption("w") ? Long.parseLong(cmd.getOptionValue("w"))
				: testCaseList.getStartRandomizationTime();

		log.info("Loaded " + testCaseList.getCases().size() + " test cases");
		log.info("Using " + randomizationTime + " as maximum initial delay");

		for (int loopCounter = 0; loopCounter < loopCount; loopCounter++) {
			log.info("Beginning loop count #" + loopCounter);			
			
			for (int count = 0; count < testCount; count++) {
				log.info("Starting thread #" + count);
				LoadTestThread runnable = new LoadTestThread();
				LoadTestStatistics stat = new LoadTestStatistics();
				runnable.setStatisticsObject(stat);
				runnable.setStartRandomizationTime(randomizationTime);
				LoadTestCase c = testCaseList.getCases()
						.get(count % testCaseList.getCases().size());
				runnable.setTestCase(c);
				stat.setTestCase(c);
				stat.setLoopCount(loopCounter);
				Thread t = new Thread(runnable);
				stat.setThreadId(t.getId());
				t.start();
				report.addStatistics(stat);
			}

			while (stillRunning(report.getStatistics())) {
				log.debug("Still running, waiting 2000ms");
				Thread.sleep(2000L);
			}

			log.info("All threads have completed for loop count #" + loopCounter);
		}

		// Display time stats
		if (cmd.hasOption("o")) {
			System.out.println("Serializing results to " + cmd.getOptionValue("o"));
			File result = new File(cmd.getOptionValue("o"));
			serializer.write(report, result);
		} else {
			System.out.println("\nLoad test stats:\n");
			for (LoadTestStatistics s : report.getStatistics()) {
				System.out.println(s.toString());
			}
		}
	}

	private static boolean stillRunning(List<LoadTestStatistics> stats) {
		for (LoadTestStatistics s : stats) {
			if (!s.isFinished())
				return true;
		}
		return false;
	}

}
