package com.godaddy.sonar.ruby.simplecov;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.godaddy.sonar.ruby.resources.RubyFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

public class SimpleCovSensor implements Sensor {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleCovSensor.class);

	private Settings settings;
	private SimpleCovCsvParser simpleCovCsvParser;
	private ModuleFileSystem moduleFileSystem;

	/**
	 * Use of IoC to get Settings
	 */
	public SimpleCovSensor(Settings settings, ModuleFileSystem moduleFileSystem, SimpleCovCsvParser simpleCovCsvParser) 
	{
		this.settings = settings;
		this.moduleFileSystem = moduleFileSystem;
		this.simpleCovCsvParser = simpleCovCsvParser;		
	}

	public boolean shouldExecuteOnProject(Project project) {
		return true;
	}

	public void analyse(Project project, SensorContext context)
	{
		File reportCsvFile = new File("coverage/results.csv");
		List<Project> modules = project.getModules();
		for (Project module: modules)
		{
			moduleFileSystem.sourceDirs();
		}
	}
    
	private void calculateMetrics()
	{
		
	}
	
    @Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
}
