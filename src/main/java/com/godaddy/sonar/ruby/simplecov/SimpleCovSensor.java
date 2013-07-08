package com.godaddy.sonar.ruby.simplecov;

import java.io.File;
import java.io.IOException;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.godaddy.sonar.ruby.core.RubyFile;

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
		List<File> sourceDirs = moduleFileSystem.sourceDirs();
		try 
		{
			calculateMetrics(sourceDirs, reportCsvFile, context);
		}
		catch (IOException e)
		{
			
		}
	}
    
	private void calculateMetrics(List<File> sourceDirs, File csvFile, final SensorContext context) throws IOException
	{
		SimpleCovCsvResult results = simpleCovCsvParser.parse(csvFile);
		
		context.saveMeasure(CoreMetrics.COVERAGE, results.getTotalPercentCoverage());
		
		File sourceFile = null;
		for (SimpleCovCsvFileResult result : results.getCsvFilesResult())
		{
            try {
            	String fileName = result.getFileName().replaceAll("\"", "");
            	sourceFile = new File(fileName);
            	RubyFile rubyFile = RubyFile.fromIOFile(sourceFile, sourceDirs, false);
            	context.saveMeasure(rubyFile, CoreMetrics.LINE_COVERAGE, (double)result.getPercentCoverage());
            	context.saveMeasure(rubyFile, CoreMetrics.COVERAGE_LINE_HITS_DATA, (double)result.getLinesCovered());

                context.saveMeasure(rubyFile, CoreMetrics.LINES_TO_COVER, (double)result.getRelevantLines());
                context.saveMeasure(rubyFile, CoreMetrics.UNCOVERED_LINES, (double)result.getLinesMissed());
            } catch (Exception e) {
            	if (sourceFile != null) {
            		LOG.error("Unable to save metrics for file: " + sourceFile.getName(), e);
            	} else {
            		LOG.error("Unable to save metrics.", e);
            	}
            }
		}
	}
	
    @Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
}
