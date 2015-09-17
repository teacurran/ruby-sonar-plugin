package com.godaddy.sonar.ruby.simplecovrcov;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoverageMeasuresBuilder;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.godaddy.sonar.ruby.core.RubyFile;

public class SimpleCovRcovSensorTest 
{
	private static String RESULT_JSON_FILE_MUTLI_SRC_DIR = "src/test/resources/test-data/results.json";
	private static String RESULT_JSON_FILE_ONE_SRC_DIR = "src/test/resources/test-data/results-one-src-dir.json";
	
	private IMocksControl mocksControl;
	
    private PathResolver pathResolver;
	private SimpleCovRcovJsonParser simpleCovRcovJsonParser;
	private SimpleCovRcovSensor simpleCovRcovSensor;
	private SensorContext sensorContext;

    private Settings   settings;
    private FileSystem fs;
	
	@Before
	public void setUp() throws Exception
	{
		mocksControl = EasyMock.createControl();
        pathResolver = mocksControl.createMock(PathResolver.class);
		fs = mocksControl.createMock(FileSystem.class);
		simpleCovRcovJsonParser = mocksControl.createMock(SimpleCovRcovJsonParser.class);
        settings = new Settings();
		
		simpleCovRcovSensor = new SimpleCovRcovSensor(settings, fs, pathResolver, simpleCovRcovJsonParser);
	}
	
	@Test
	public void testConstructor() 
	{	
		assertNotNull(simpleCovRcovSensor);
	}
	
	
	@Test
	public void testAnalyse() throws IOException
	{
		
		File jsonFile = new File("coverage/.resultset.json");
		sensorContext = mocksControl.createMock(SensorContext.class);

        expect(fs.baseDir()).andReturn(new File("bar"));    
        expect(pathResolver.relativeFile(isA(File.class),isA(String.class))).andReturn(new File("foo"));
//		expect(simpleCovRcovJsonParser.parse(jsonFile)).andThrow(new IOException());
		
		mocksControl.replay();
		
		simpleCovRcovSensor.analyse(new Project("key_name"), sensorContext);
		mocksControl.verify();
		
		assertTrue(true);
	}
	
}
