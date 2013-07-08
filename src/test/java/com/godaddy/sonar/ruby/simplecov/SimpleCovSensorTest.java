package com.godaddy.sonar.ruby.simplecov;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.godaddy.sonar.ruby.simplecov.SimpleCovSensor;

import org.apache.commons.configuration.Configuration;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;

import org.junit.Before;
import org.junit.After;

import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

public class SimpleCovSensorTest {
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
		
		simpleCovSensor = new SimpleCovSensor(settings, moduleFileSystem, simpleCovCsvParser);
	}
	
	@After
	public void tearDowm() throws Exception
	{
	}
	
	@Test
	public void testAnalyse() 
	{		
	}
	
	@Test
	public void testAnalyseWithParserException()
	{
		
	}
}
