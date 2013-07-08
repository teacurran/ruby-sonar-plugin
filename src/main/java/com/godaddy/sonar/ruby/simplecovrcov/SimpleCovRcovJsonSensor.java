package com.godaddy.sonar.ruby.simplecovrcov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.simplecov.SimpleCovCsvParser;
import com.godaddy.sonar.ruby.simplecov.SimpleCovSensor;

public class SimpleCovRcovJsonSensor {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleCovSensor.class);

	private Settings settings;
	private SimpleCovRcovJsonParser simpleCovRcovJsonParser;
	private ModuleFileSystem moduleFileSystem;
	private Ruby ruby;
	
	
	
	/**
	 * Use of IoC to get Settings
	 */
	public SimpleCovRcovJsonSensor(Settings settings, ModuleFileSystem moduleFileSystem, SimpleCovRcovJsonParser simpleCovRcovJsonParser) 
	{
		this.settings = settings;
		this.moduleFileSystem = moduleFileSystem;
		this.simpleCovRcovJsonParser = simpleCovRcovJsonParser;		
	}
	
	public boolean shouldExecuteOnProject(Project project)
	{
		return Ruby.KEY.equals(project.getLanguageKey());
	}
}
