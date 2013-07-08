package com.godaddy.sonar.ruby.simplecovrcov;

import static org.junit.Assert.*;

import org.apache.commons.configuration.Configuration;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.simplecov.SimpleCovCsvParser;
import com.godaddy.sonar.ruby.simplecov.SimpleCovSensor;

public class SimpleCovRcovJsonSensorTest {

	private IMocksControl mocksControl;
	private Settings settings;
	private ModuleFileSystem moduleFileSystem;
	private SimpleCovRcovJsonParser simpleCovRcovJsonParser;
	private SimpleCovRcovJsonSensor simpleCovRcovJsonSensor;
	private SensorContext sensorContext;
	
	@Before
	public void setUp() throws Exception
	{
		mocksControl = EasyMock.createControl();
		settings = new Settings();
		moduleFileSystem = mocksControl.createMock(ModuleFileSystem.class);
		simpleCovRcovJsonParser = mocksControl.createMock(SimpleCovRcovJsonParser.class);
		
		simpleCovRcovJsonSensor = new SimpleCovRcovJsonSensor(settings, moduleFileSystem, simpleCovRcovJsonParser);
	}
	
	@Test
	public void testConstructor() 
	{	
		assertNotNull(simpleCovRcovJsonSensor);
	}
	
	@Test
	public void testShouldExecuteOnRubyProject()
	{
		Configuration config = mocksControl.createMock(Configuration.class);
		expect(config.getString("sonar.language", "java")).andReturn("ruby");
		replay(config);
		
		Project project = new Project("test project");
		project.setConfiguration(config);
		
		assertTrue(simpleCovRcovJsonSensor.shouldExecuteOnProject(project));
	}
	
	@Test
	public void testShouldNotExecuteOnJavascriptProject()
	{
		Configuration config = mocksControl.createMock(Configuration.class);
		expect(config.getString("sonar.language", "java")).andReturn("javascript");
		replay(config);
		
		Project project = new Project("test project");
		project.setConfiguration(config);
		
		assertFalse(simpleCovRcovJsonSensor.shouldExecuteOnProject(project));
	}
	
	@Test
	public void testAnalyse()
	{
		
	}
}
