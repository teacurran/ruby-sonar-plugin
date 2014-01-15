package com.godaddy.sonar.ruby.metricfu;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.jfree.util.Log;
import org.junit.Before;
import org.junit.Test;

public class MetricfuYamlParserTest extends TestCase
{
	private final static String YML_FILE_NAME = "src/test/resources/test-data/results.yml";
	
	private MetricfuYamlParser parser = null;
	
	@Before
	public void setUp() throws Exception
	{
		parser = new MetricfuYamlParser(YML_FILE_NAME);
	}
		
	@Test
	public void testParseFunction() throws IOException
	{
		List<SaikuroComplexity> rubyFunctions = parser.parseSaikuro("lib/some_path/foo_bar.rb");

		SaikuroComplexity rubyFunction0 = new SaikuroComplexity("lib/some_path/foo_bar.rb", 5, "FooBar#validate_user_name", 4);		
		assertTrue(rubyFunctions.size()==2);
		assertTrue(rubyFunctions.get(0).toString().equals(rubyFunction0.toString()));
		
		List<FlayReason> duplications = parser.parseFlay();        
		for (FlayReason duplication : duplications) {
			for (FlayReason.Match match : duplication.getMatches()) {
				Log.debug(match.getFile() + ":" + match.getStartLine());
			}
		}
	}	
}
