package com.godaddy.sonar.ruby.simplecov;

import java.util.HashMap;
import java.util.Map;

public class SimpleCovCsvFileResult {
	private String fileName;
	private double percentCoverage;
	private double lines;
	private double relevantLines;
	private double linesCovered;
	private double linesMissed;
	
	public enum FieldName
	{
		FILE_NAME("File"),
		PERCENT_COVERED("% covered"),
		LINES("Lines"),
		RELEVANT_LINES("Relevant Lines"),
		LINES_COVERED("Lines covered"),
		LINES_MISSED("Lines missed");
	
		private final String fieldNameDesc;
		
		private static Map<String, FieldName> fieldNameMap = new HashMap<String, FieldName>();
	
        static {
            for (FieldName fieldName : FieldName.values())
                fieldNameMap.put(fieldName.getFieldNameDesc(), fieldName);
        }
        
		private FieldName(String desc) 
		{
			this.fieldNameDesc = desc;
		}
		
		private String getFieldNameDesc()
		{
			return this.fieldNameDesc;
		}
		
		public static FieldName getFieldName(String desc)
		{
			return fieldNameMap.get(desc);
		}
	}
	
	public void set(String fieldNameDesc, String value) throws NumberFormatException, IllegalArgumentException
	{
		FieldName fieldName = FieldName.getFieldName(fieldNameDesc);
		if (fieldName == FieldName.FILE_NAME)
			this.fileName = value;
		else if (fieldName == FieldName.PERCENT_COVERED)
			this.percentCoverage = Double.parseDouble(value);
		else if (fieldName == FieldName.LINES)
			this.lines = Double.parseDouble(value);
		else if (fieldName == FieldName.RELEVANT_LINES)
			this.relevantLines = Double.parseDouble(value);
		else if (fieldName == FieldName.LINES_COVERED)
			this.linesCovered = Double.parseDouble(value);
		else if (fieldName == FieldName.LINES_MISSED)
			this.linesMissed = Double.parseDouble(value);
		else		
			throw new IllegalArgumentException(String.format("Undefined field name '%s'", fieldNameDesc));
	}

	public String getFileName() 
	{
		return fileName;
	}
	
	public double getPercentCoverage() 
	{
		return percentCoverage;
	}
		
	public double getLines() 
	{
		return lines;
	}
		
	public double getRelevantLines() 
	{
		return relevantLines;
	}
		
	public double getLinesCovered() 
	{
		return linesCovered;
	}
	
	public double getLinesMissed() 
	{
		return linesMissed;
	}	
}
