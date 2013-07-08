package com.godaddy.sonar.ruby.simplecovrcov;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.CoverageMeasuresBuilder;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.core.RubyFile;
import com.godaddy.sonar.ruby.simplecov.SimpleCovCsvFileResult;
import com.godaddy.sonar.ruby.simplecov.SimpleCovCsvParser;
import com.godaddy.sonar.ruby.simplecov.SimpleCovCsvResult;

public class SimpleCovRcovSensor implements Sensor {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleCovRcovSensor.class);

	private Settings settings;
	private SimpleCovRcovJsonParser simpleCovRcovJsonParser;
	private SimpleCovCsvParser simpleCovCsvParser;
	private ModuleFileSystem moduleFileSystem;
	private Ruby ruby;
	
	
	/**
	 * Use of IoC to get Settings
	 */
	public SimpleCovRcovSensor(Settings settings, ModuleFileSystem moduleFileSystem, SimpleCovRcovJsonParser simpleCovRcovJsonParser, SimpleCovCsvParser simpleCovCsvParser) 
	{
		this.settings = settings;
		this.moduleFileSystem = moduleFileSystem;
		this.simpleCovRcovJsonParser = simpleCovRcovJsonParser;		
		this.simpleCovCsvParser = simpleCovCsvParser;
	}
	
	public boolean shouldExecuteOnProject(Project project)
	{
		return Ruby.KEY.equals(project.getLanguageKey());
	}
	
	public void analyse(Project project, SensorContext context)
	{
		File csvFile = new File("coverage/results.csv");
		File jsonFile = new File("coverage/results.json");
		
		List<File> sourceDirs = moduleFileSystem.sourceDirs();
		try 
		{
			calculateMetrics(sourceDirs, csvFile, jsonFile, context);
		}
		catch (IOException e)
		{
			
		}
	}
    
	private void calculateMetrics(List<File> sourceDirs, File csvFile, File jsonFile, final SensorContext context) throws IOException
	{
		SimpleCovCsvResult csvResults = simpleCovCsvParser.parse(csvFile);
		Map<String, CoverageMeasuresBuilder> jsonResults = simpleCovRcovJsonParser.parse(jsonFile);
		context.saveMeasure(CoreMetrics.COVERAGE, csvResults.getTotalPercentCoverage());
		
		File sourceFile = null;
		for (SimpleCovCsvFileResult result : csvResults.getCsvFilesResult())
		{
            try {
            	String fileName = result.getFileName().replaceAll("\"", "");
            	sourceFile = new File(fileName);
            	RubyFile rubyFile = RubyFile.fromIOFile(sourceFile, sourceDirs, false);
            	context.saveMeasure(rubyFile, CoreMetrics.LINE_COVERAGE, (double)result.getPercentCoverage());
            	context.saveMeasure(rubyFile, CoreMetrics.COVERAGE_LINE_HITS_DATA, (double)result.getLinesCovered());

                context.saveMeasure(rubyFile, CoreMetrics.LINES_TO_COVER, (double)result.getRelevantLines());
                context.saveMeasure(rubyFile, CoreMetrics.UNCOVERED_LINES, (double)result.getLinesMissed());
                
                CoverageMeasuresBuilder fileCoverage = jsonResults.get(fileName);
                
            } catch (Exception e) {
            	if (sourceFile != null) {
            		LOG.error("Unable to save metrics for file: " + sourceFile.getName(), e);
            	} else {
            		LOG.error("Unable to save metrics.", e);
            	}
            }
		}
	}	
}
