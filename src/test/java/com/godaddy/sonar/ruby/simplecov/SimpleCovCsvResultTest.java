package com.godaddy.sonar.ruby.simplecov;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleCovCsvResultTest
{

	private SimpleCovCsvResult result;
	
	@Before
	public void setUp() throws Exception
	{
		result = new SimpleCovCsvResult();
		SimpleCovCsvFileResult file1 = new SimpleCovCsvFileResult();
		file1.set("% covered", "12.1");
		file1.set("Lines", "100");
		file1.set("Relevant Lines", "100");
		file1.set("Lines covered", "100");
		file1.set("Lines missed", "100");
		List<SimpleCovCsvFileResult> filesResult = new ArrayList<SimpleCovCsvFileResult>();
		filesResult.add(file1);
		result.setCsvFilesResult(filesResult);
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testTotalPercentCoverageSetterGetter()
	{
		assertEquals(12.1, result.getTotalPercentCoverage(), 1e-2);
	}

	@Test
	public void testTotalLinesSetterGetter()
	{
		assertEquals(100, result.getTotalLines(), 1e-2);
	}

	@Test
	public void testTotalRelevantLinesSetterGetter()
	{
		assertEquals(100, result.getTotalRelevantLines(), 1e-2);
	}

	@Test
	public void testTotalLinesCoveredSetterGetter()
	{
		assertEquals(100, result.getTotalLinesCovered(), 1e-2);
	}
	
	@Test
	public void testTotalLinesMissedSetterGetter()
	{
		assertEquals(100, result.getTotalLinesMissed(), 1e-2);
	}	
}
