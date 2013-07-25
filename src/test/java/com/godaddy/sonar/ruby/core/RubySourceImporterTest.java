package com.godaddy.sonar.ruby.core;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.eq;
import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

public class RubySourceImporterTest {	
	public static String INPUT_DIR = "src/test/resources/test-data";
	public static String INPUT_FILE = "src/test/resources/test-data/hello_world.rb";

	private IMocksControl mocksControl;
	private ModuleFileSystem moduleFileSystem;
	private SensorContext sensorContext;
	private Configuration config;
	private Project project;
	private List<File> sourceDirs;
	private List<File> files;
	private List<File> testDirs;
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		mocksControl = EasyMock.createControl();
		moduleFileSystem = mocksControl.createMock(ModuleFileSystem.class);
		
		config = mocksControl.createMock(Configuration.class);
		expect(config.getString("sonar.language", "java")).andStubReturn("ruby");
		
		project = new Project("test project");
		project.setConfiguration(config);
		
		sensorContext = mocksControl.createMock(SensorContext.class);
		sourceDirs = new ArrayList<File>();
		sourceDirs.add(new File(INPUT_DIR));
		files = new ArrayList<File>();
		files.add(new File(INPUT_FILE));
		testDirs = new ArrayList<File>();
		testDirs.add(new File(INPUT_DIR));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRubySourceImporter() {
		RubySourceImporter importer = new RubySourceImporter(project, moduleFileSystem);
		assertNotNull(importer);
		assertEquals(Ruby.INSTANCE, importer.getLanguage());
		assertEquals(RubySourceImporter.class, importer.getClass());
	}

	@Test
	public void testAnalyseProject() {
		RubySourceImporter importer = new RubySourceImporter(project, moduleFileSystem);
		
		expect(config.getBoolean(isA(String.class), eq(true))).andStubReturn(true);
		expect(moduleFileSystem.files(isA(FileQuery.class))).andReturn(files).times(2);
		expect(moduleFileSystem.sourceDirs()).andReturn(sourceDirs);
		expect(sensorContext.index(isA(RubyFile.class))).andReturn(true).times(2);
		expect(moduleFileSystem.sourceCharset()).andReturn(Charset.defaultCharset());
		expect(moduleFileSystem.testDirs()).andReturn(testDirs);
		sensorContext.saveSource(isA(RubyFile.class), isA(String.class));
		sensorContext.saveSource(isA(RubyFile.class), isA(String.class));
		
		mocksControl.replay();

		importer.analyse(project,  sensorContext);

		mocksControl.verify();
	}

	@Test
	public void testCreateResource() {
		RubySourceImporter importer = new RubySourceImporter(project, moduleFileSystem);
		RubyFile file = importer.createResource(files.get(0), sourceDirs, false);
		assertNotNull(file);
		assertEquals("hello_world", file.getName());
	}
	
	@Test
	public void testCreateResourceFileIsNull() {
		RubySourceImporter importer = new RubySourceImporter(project, moduleFileSystem);
		RubyFile file = importer.createResource(null, sourceDirs, false);
		assertNull(file);		
	}

	@Test
	public void testParseDirs() {
		RubySourceImporter importer = new RubySourceImporter(project, moduleFileSystem);
		
		expect(config.getBoolean(isA(String.class), eq(true))).andStubReturn(true);
		expect(sensorContext.index(isA(RubyFile.class))).andReturn(true);
		expect(moduleFileSystem.sourceCharset()).andReturn(Charset.defaultCharset()).once();
		sensorContext.saveSource(isA(RubyFile.class), isA(String.class));
		
		mocksControl.replay();
		
		importer.parseDirs(sensorContext, files, sourceDirs, false, moduleFileSystem.sourceCharset());
		
		mocksControl.verify();
	}
	
	@Test
	public void testToString() {
		RubySourceImporter importer = new RubySourceImporter(project, moduleFileSystem);
		String result = importer.toString();
		assertEquals("RubySourceImporter[getLanguage()=Ruby,getClass()=class com.godaddy.sonar.ruby.core.RubySourceImporter]", result);
	}
}
