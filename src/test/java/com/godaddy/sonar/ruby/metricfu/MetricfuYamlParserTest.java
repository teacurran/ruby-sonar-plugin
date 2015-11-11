package com.godaddy.sonar.ruby.metricfu;

import java.io.IOException;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

public class MetricfuYamlParserTest extends TestCase
{
	private final static String YML_SYNTAX_FILE_NAME = "src/test/resources/test-data/metricfu_report.yml";
    private final static String YML_PARSE_FILE_NAME = "src/test/resources/test-data/results.yml";

    private IMocksControl mocksControl;

    private Settings settings;
    private FileSystem fs;

	@Before
	public void setUp() throws Exception {
        mocksControl = EasyMock.createControl();
        fs = mocksControl.createMock(FileSystem.class);
        settings = new Settings();
	}


    @Test
    public void testCaneComplexity() throws IOException
    {
//        MetricfuYamlParser parser = new MetricfuYamlParser(settings, fs, YML_SYNTAX_FILE_NAME);
//        List<CaneViolation> violations = parser.parseCane("app/decorators/donation_page_decorator.rb");
//
//        assertTrue(violations.size() == 1);
//        assertTrue(violations.get(0) instanceof CaneComplexityViolation);
//        CaneComplexityViolation c = (CaneComplexityViolation) violations.get(0);
//        assertTrue(c.getMethod().equals("DonationPageDecorator#siteowner_address"));
//        assertTrue(c.getComplexity() == 24);
    }

	@Test
	public void testSaikuroComplexity() throws IOException
	{
//        MetricfuYamlParser parser = new MetricfuYamlParser(settings, fs, YML_PARSE_FILE_NAME);
//        List<SaikuroClassComplexity> cls = parser.parseSaikuro("lib/some_path/foo_bar.rb");
//
//        assertTrue(cls.size() == 1);
//        assertTrue(cls.get(0).getName().equals("FooBar"));
//        assertTrue(cls.get(0).getComplexity() == 13);
//        assertTrue(cls.get(0).getLines() == 36);
//
//        assertTrue(cls.get(0).getMethods().size() == 2);
//        assertTrue(cls.get(0).getMethods().get(0).getName().equals("FooBar#validate_user_name"));
//        assertTrue(cls.get(0).getMethods().get(0).getLines() == 5);
//        assertTrue(cls.get(0).getMethods().get(0).getComplexity() == 4);
	}
}
