package com.godaddy.sonar.ruby.simplecov;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleCovCsvFileResultTest
{
	private SimpleCovCsvFileResult result;

	@Before
	public void setUp() throws Exception
	{
		result = new SimpleCovCsvFileResult();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testFileNameSetterGetter()
	{
		result.set("File", "/path/to/dummy/csv/file");
		assertSame("/path/to/dummy/csv/file", result.getFileName());
	}
	
	@Test
	public void testPercentCoveredSetterGetter()
	{
		result.set("% covered", "12.1");
		assertEquals(12.1, result.getPercentCoverage(), 1e-3);
	}

	@Test
	public void testLinesSetterGetter()
	{
		result.set("Lines", "1000");
		assertEquals(1000, result.getLines(), 1e-2);
	}
	
	@Test
	public void testRelevantLinesSetterGetter()
	{
		result.set("Relevant Lines", "1000");
		assertEquals(1000, result.getRelevantLines(), 1e-2);
	}
	
	@Test
	public void testLinesCoveredSetterGetter() {
		result.set("Lines covered", "100");
		assertEquals(100, result.getLinesCovered(), 1e-2);
	}
	
	@Test
	public void testLinesMissedSetterGetter()
	{
		result.set("Lines missed", "100");
		assertEquals(100, result.getLinesMissed(), 1e-2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongFieldNameDesc()
	{
		result.set("buggies field desc", "1234");
	}
	
	@Test(expected=NumberFormatException.class)
	public void testWrongNumberFormat()
	{
		result.set("% covered", "12.er");
	}
}
