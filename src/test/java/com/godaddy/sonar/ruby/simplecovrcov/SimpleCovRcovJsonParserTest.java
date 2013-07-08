package com.godaddy.sonar.ruby.simplecovrcov;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.CoverageMeasuresBuilder;

import junit.framework.TestCase;

import org.junit.Assert;

public class SimpleCovRcovJsonParserTest extends TestCase
{
	private final static String JSON_FILE_NAME = "src/test/java/com/godaddy/sonar/ruby/resources/simplecovrcov/simple-cov-rcov.json";
	
	private SimpleCovRcovJsonParserImpl parser = null;
	@Before
	public void setUp() throws Exception
	{
		parser = new SimpleCovRcovJsonParserImpl();
	}
	
	@After
	public void tearDown() throws Exception
	{
		
	}
	
	@Test
	public void testParserWithValidJson() throws IOException
	{
		File reportFile = new File(JSON_FILE_NAME);
		Map<String, CoverageMeasuresBuilder> coveredFiles = parser.parse(reportFile);
		
		String coveredFile1 = "/home/mxsmith/projects/GoDaddy-Hosting/cPanel-api/lib/c_panel_api/c_panel_api.rb";
		String coveredFile2 = "/home/mxsmith/projects/GoDaddy-Hosting/cPanel-api/lib/c_panel_api/account_validator.rb";
		String coveredFile3 = "/home/mxsmith/projects/GoDaddy-Hosting/cPanel-api/lib/c_panel_api/create_account_request.rb";
		
		assertEquals(coveredFiles.size(), 3);
		assertEquals(coveredFiles.containsKey(coveredFile1), true);
		assertEquals(coveredFiles.containsKey(coveredFile2), true);
		assertEquals(coveredFiles.containsKey(coveredFile3), true);
		
		CoverageMeasuresBuilder builder1 = coveredFiles.get(coveredFile1);
		System.out.println(builder1);
		assertEquals(builder1.getCoveredLines(), 17);		
	}
}
