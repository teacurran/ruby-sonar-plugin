package com.godaddy.sonar.ruby.simplecovrcov;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.sonar.api.measures.CoverageMeasuresBuilder;

import com.google.common.collect.Maps;

public class SimpleCovRcovJsonParserImpl implements SimpleCovRcovJsonParser {

	public Map<String, CoverageMeasuresBuilder> parse(File file) throws IOException
	{
		
		Map<String, CoverageMeasuresBuilder> coveredFiles = Maps.newHashMap();
		
		File fileToFindCoverage = file;
		//read content of file as one string, assumes the content of the file is in json format
		String fileString = FileUtils.readFileToString(fileToFindCoverage, "UTF-8");
		
		//Parse the json string into a JSONObject, the coverageJsonObject will contain the map for files and their coverage
		JSONObject resultJsonObject = (JSONObject) JSONValue.parse(fileString);			
		JSONObject coverageJsonObj = (JSONObject) ((JSONObject) resultJsonObject.get("RSpec")).get("coverage");
	
		//for each file in the coverage report
		for(int j = 0; j < coverageJsonObj.keySet().size(); j++)
		{

			CoverageMeasuresBuilder fileCoverage = CoverageMeasuresBuilder.create();
			
			String filePath = coverageJsonObj.keySet().toArray()[j].toString();
			JSONArray coverageArray = (JSONArray)coverageJsonObj.get(coverageJsonObj.keySet().toArray()[j]);
									
			//for each line in the coverage array
			for(int i = 0; i < coverageArray.size(); i++)
			{
				//get the current line
				Long line = (Long) coverageArray.toArray()[i];
				//going to store that line as int, so it can be used with the setHits sonar api call
				Integer intLine = 0;
				
				//the current line number will be the index of the array + 1, since there is not a line 0
				int lineNumber = i+1;
				
				//if line is null, then that line was not covered, so set the value to 0
				if(line==null)
				{
					intLine = 0;
				}
				//else take the long value and parse it as an int 
				else
				{	
					intLine = line.intValue();
				}		
				fileCoverage.setHits(lineNumber, intLine);			
			}
			coveredFiles.put(filePath, fileCoverage);
		}
		return coveredFiles;
	}
	
	
}
