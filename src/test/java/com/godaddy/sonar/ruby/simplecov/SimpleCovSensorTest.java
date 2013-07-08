package com.godaddy.sonar.ruby.simplecov;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.godaddy.sonar.ruby.core.RubyFile;
import com.godaddy.sonar.ruby.simplecov.SimpleCovSensor;

import org.apache.commons.configuration.Configuration;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;

import org.junit.Before;
import org.junit.After;

import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

public class SimpleCovSensorTest {
	private static String RESULT_CSV_FILE = "src/test/resources/test-data/results.csv";
	
	private IMocksControl mocksControl;
	private Settings settings;
	private ModuleFileSystem moduleFileSystem;
	private SimpleCovCsvParser simpleCovCsvParser;
	private SimpleCovSensor simpleCovSensor;
	private SensorContext sensorContext;
	
	@Before
	public void setUp() throws Exception
	{
		mocksControl = EasyMock.createControl();
		settings = new Settings();
		moduleFileSystem = mocksControl.createMock(ModuleFileSystem.class);
		simpleCovCsvParser = mocksControl.createMock(SimpleCovCsvParser.class);
		sensorContext = mocksControl.createMock(SensorContext.class);
		
		simpleCovSensor = new SimpleCovSensor(settings, moduleFileSystem, simpleCovCsvParser);
	}
	
	@After
	public void tearDowm() throws Exception
	{
	}
	
	@Test
	public void testAnalyse() throws IOException
	{		
		SimpleCovCsvResult results = new SimpleCovCsvParserImpl().parse(new File(RESULT_CSV_FILE));
		
		List<File> sourceDirs = new ArrayList<File>();
		
		Measure measure = new Measure();
		
		expect(moduleFileSystem.sourceDirs()).andReturn(sourceDirs).once();
		expect(simpleCovCsvParser.parse(eq(new File("coverage/results.csv")))).andReturn(results).once();
		expect(sensorContext.saveMeasure(eq(CoreMetrics.COVERAGE), eq(results.getTotalPercentCoverage()))).andReturn(measure).once();
		expect(sensorContext.saveMeasure(isA(RubyFile.class), eq(CoreMetrics.LINE_COVERAGE), isA(Double.class))).andReturn(measure).times(12);
		
		mocksControl.replay();
		
		simpleCovSensor.analyse(new Project("key_name"), sensorContext);
		
		mocksControl.verify();
	}	
}
