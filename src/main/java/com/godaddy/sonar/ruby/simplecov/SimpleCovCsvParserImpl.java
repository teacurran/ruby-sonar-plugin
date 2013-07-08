package com.godaddy.sonar.ruby.simplecov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SimpleCovCsvParserImpl implements SimpleCovCsvParser {
	
	public SimpleCovCsvResult parse(File csvFile) throws IOException,IllegalArgumentException
	{
		String[] fieldNames = null;
		String line;
		BufferedReader reader = null;
		List<SimpleCovCsvFileResult> filesResult = new ArrayList<SimpleCovCsvFileResult>();
		try 
		{
			reader = new BufferedReader(new FileReader(csvFile));
			while ((line = reader.readLine()) != null) 
			{
				line = line.trim();
				if (line.equals("")) continue;
				if (fieldNames == null)
				{
					fieldNames = line.split(",");
					continue;
				}
				String[] values = line.split(",");
				if (fieldNames.length != values.length)
					throw new IllegalArgumentException(String.format("Number of headers [%d] != Number of values [%d]", fieldNames.length, values.length));
				
				SimpleCovCsvFileResult fileResult = new SimpleCovCsvFileResult();
				for (int i = 0 ; i < fieldNames.length; i++)
				{
					fileResult.set(fieldNames[i], values[i]);
				}
				filesResult.add(fileResult);
			}
		}
		finally
		{
			if (reader != null)
				reader.close();
		}
		
		SimpleCovCsvResult result = new SimpleCovCsvResult();
		result.setCsvFilesResult(filesResult);
		
		return result;
	}
}