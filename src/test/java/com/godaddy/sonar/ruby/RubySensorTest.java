package com.godaddy.sonar.ruby;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;

import com.godaddy.sonar.ruby.core.LanguageRuby;

public class RubySensorTest {
  public static String   INPUT_SOURCE_DIR  = "src/test/resources/test-data";
  public static String   INPUT_SOURCE_FILE = "src/test/resources/test-data/hello_world.rb";

  private IMocksControl  mocksControl;
  private SensorContext  sensorContext;
  private Project        project;
  private List<File>     sourceDirs;
  private List<File>     files;

  private Settings       settings;
  private FileSystem     fs;
  private FilePredicates filePredicates;
  private FilePredicate  filePredicate;

  @Before
  public void setUp() throws Exception {
    mocksControl = EasyMock.createControl();
    fs = mocksControl.createMock(FileSystem.class);
    filePredicates = mocksControl.createMock(FilePredicates.class);
    filePredicate = mocksControl.createMock(FilePredicate.class);

    project = new Project("test project");
    settings = new Settings();
    project.setLanguage(LanguageRuby.INSTANCE);

    sensorContext = mocksControl.createMock(SensorContext.class);

    sourceDirs = new ArrayList<File>();
    sourceDirs.add(new File(INPUT_SOURCE_DIR));
    files = new ArrayList<File>();
    files.add(new File(INPUT_SOURCE_FILE));

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testRubySensor() {
    RubySensor sensor = new RubySensor(settings, fs);
    assertNotNull(sensor);
  }

  @Test
  public void testShouldExecuteOnProject() {
    RubySensor sensor = new RubySensor(settings, fs);

    expect(fs.predicates()).andReturn(filePredicates).times(1);
    expect(fs.hasFiles(isA(FilePredicate.class))).andReturn(true).times(1);
    expect(filePredicates.hasLanguage(eq("ruby"))).andReturn(filePredicate).times(1);
    mocksControl.replay();

    sensor.shouldExecuteOnProject(project);

    mocksControl.verify();
  }

  @Test
  public void testAnalyse() {
    RubySensor sensor = new RubySensor(settings, fs);

    Measure measure = new Measure();
    List<InputFile> inputFiles = new ArrayList<InputFile>();
    File aFile = new File(INPUT_SOURCE_FILE);
    DefaultInputFile difFile = new DefaultInputFile(project.getKey(), INPUT_SOURCE_FILE);
    difFile.setModuleBaseDir(FileSystems.getDefault().getPath("."));

    inputFiles.add(difFile);

    expect(sensorContext.saveMeasure(isA(InputFile.class), isA(Metric.class), isA(Double.class))).andReturn(measure).times(4);
    expect(fs.predicates()).andReturn(filePredicates).times(1);
    expect(filePredicates.hasLanguage(eq("ruby"))).andReturn(filePredicate).times(1);
    expect(fs.inputFiles(isA(FilePredicate.class))).andReturn(inputFiles).times(1);
    expect(fs.encoding()).andReturn(StandardCharsets.UTF_8).times(1);

    mocksControl.replay();

    sensor.analyse(project, sensorContext);
    mocksControl.verify();
  }

  @Test
  public void testToString() {
    RubySensor sensor = new RubySensor(settings, fs);
    String result = sensor.toString();
    assertEquals("RubySensor", result);
  }
}
