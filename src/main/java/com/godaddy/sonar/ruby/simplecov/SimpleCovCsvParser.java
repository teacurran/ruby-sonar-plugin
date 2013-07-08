package com.godaddy.sonar.ruby.simplecov;

import java.io.File;
import java.io.IOException;

import org.sonar.api.BatchExtension;

public interface SimpleCovCsvParser extends BatchExtension
{
	public SimpleCovCsvResult parse(File csvFile) throws IOException,IllegalArgumentException;
}
