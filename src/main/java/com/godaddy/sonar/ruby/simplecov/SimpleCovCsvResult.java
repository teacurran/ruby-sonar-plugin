package com.godaddy.sonar.ruby.simplecov;

import java.util.List;

public class SimpleCovCsvResult {
	private double totalPercentCoverage = 0;
	private double totalLines = 0;
	private double totalRelevantLines = 0;
	private double totalLinesCovered  = 0;
	private double totalLinesMissed = 0;
	private List<SimpleCovCsvFileResult> csvFilesResult;
	
	public void setCsvFilesResult(List<SimpleCovCsvFileResult> csvFilesResult) 
	{
		this.csvFilesResult = csvFilesResult;
		for (SimpleCovCsvFileResult fileResult : csvFilesResult) {
			this.totalPercentCoverage += fileResult.getPercentCoverage();
			this.totalLines += fileResult.getLines();
			this.totalRelevantLines += fileResult.getRelevantLines();
			this.totalLinesCovered += fileResult.getLinesCovered();
			this.totalLinesMissed += fileResult.getLinesMissed();
		}
		this.totalPercentCoverage = totalPercentCoverage/csvFilesResult.size();
	}
	
	public double getTotalPercentCoverage()
	{
		return this.totalPercentCoverage;
	}
	
	public double getTotalLines()
	{
		return this.totalLines;
	}
	
	public double getTotalRelevantLines()
	{
		return this.totalRelevantLines;
	}
	
	public double getTotalLinesCovered()
	{
		return this.totalLinesCovered;
	}
	
	public double getTotalLinesMissed()
	{
		return this.totalLinesMissed;
	}
	
	public List<SimpleCovCsvFileResult> getCsvFilesResult() {
		return this.csvFilesResult;
	}
}
