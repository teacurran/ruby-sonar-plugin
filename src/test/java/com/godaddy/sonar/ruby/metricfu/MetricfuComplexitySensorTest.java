package com.godaddy.sonar.ruby.metricfu;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.easymock.EasyMock;

import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.eq;

import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.CoreProperties;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.godaddy.sonar.ruby.RubySensor;
import com.godaddy.sonar.ruby.core.LanguageRuby;
import com.godaddy.sonar.ruby.core.RubyFile;


public class MetricfuComplexitySensorTest 
{
	private IMocksControl mocksControl;
	
	private PathResolver pathResolver;
	private SensorContext sensorContext;
	private MetricfuComplexityYamlParser metricfuComplexityYamlParser;
	private MetricfuComplexitySensor metricfuComplexitySensor;
	private Configuration config;
	private Project project;

	private Settings   settings;
	private FileSystem fs;

	@Before
	public void setUp() throws Exception
	{
		mocksControl = EasyMock.createControl();
		pathResolver = mocksControl.createMock(PathResolver.class);
		fs = mocksControl.createMock(FileSystem.class);
		metricfuComplexityYamlParser = mocksControl.createMock(MetricfuComplexityYamlParser.class);
		settings = new Settings();
		
		metricfuComplexitySensor = new MetricfuComplexitySensor(settings, fs, pathResolver, metricfuComplexityYamlParser);
		config = mocksControl.createMock(Configuration.class);
		expect(config.getString("sonar.language", "java")).andStubReturn("ruby");

		project = new Project("test project");	
		Settings settings = new Settings();
//		settings.setProperty(CoreProperties.PROJECT_LANGUAGE_PROPERTY, LanguageRuby.KEY);
//		project.setSettings(settings);
		project.setLanguage(LanguageRuby.INSTANCE);
		
		project.setConfiguration(config);
		
	}
	
	@Test
	public void testConstructor() 
	{	
		assertNotNull(metricfuComplexitySensor);
	}
	
//	@Test
//	public void testShouldExecuteOnRubyProject()
//	{		
//		Configuration config = mocksControl.createMock(Configuration.class);
//		expect(config.getString("sonar.language", "java")).andReturn("ruby");
//		mocksControl.replay();
//		
//		Project project = new Project("test project");
//		project.setConfiguration(config);
//
//		RubySensor sensor = new RubySensor(moduleFileSystem);
//		sensor.shouldExecuteOnProject(project);
//		
//		mocksControl.verify();			
//	}

	@Test
	public void testAnalyse() throws IOException
	{
		List<File> sourceFiles= new ArrayList<File>();
		List<File> sourceDirs = new ArrayList<File>();
		
		sourceDirs.add(new File("lib"));		
		sourceFiles.add(new File("lib/some_path/foo_bar.rb"));

		sensorContext = mocksControl.createMock(SensorContext.class);
		List<RubyFunction> functions = new ArrayList<RubyFunction>();
		functions.add(new RubyFunction("validate", 5, 10));
		
		expect(fs.baseDir()).andReturn(new File("bar"));	
        expect(pathResolver.relativeFile(isA(File.class),isA(String.class))).andReturn(new File("foo"));
		mocksControl.replay();

		metricfuComplexitySensor.analyse(project, sensorContext);
		mocksControl.verify();
	}
}
