package com.godaddy.sonar.ruby.resources;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Resource;

@SuppressWarnings("rawtypes")
public class RubyFile extends Resource<RubyPackage>
{
	private String filename;
	private String longName;
	private String packageKey;
	
	public RubyFile()
	{
	}
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getLongName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public Language getLanguage() {
		return null;
	}

	@Override
	public String getScope() {
		return null;
	}

	@Override
	public String getQualifier() {
		return null;
	}

	@Override
	public RubyPackage getParent() {
		return null;
	}

	@Override
	public boolean matchFilePattern(String antPattern) {
		return false;
	}
}
