package com.godaddy.sonar.ruby;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.Extension;
import org.sonar.api.Properties;
import org.sonar.api.SonarPlugin;

import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.core.RubySourceCodeColorizer;
import com.godaddy.sonar.ruby.core.RubySourceImporter;
import com.godaddy.sonar.ruby.core.profiles.SonarWayProfile;
import com.godaddy.sonar.ruby.metricfu.CaneRulesRepository;
import com.godaddy.sonar.ruby.metricfu.MetricfuComplexitySensor;
import com.godaddy.sonar.ruby.metricfu.MetricfuDuplicationSensor;
import com.godaddy.sonar.ruby.metricfu.MetricfuIssueSensor;
import com.godaddy.sonar.ruby.metricfu.MetricfuYamlParser;
import com.godaddy.sonar.ruby.metricfu.ReekRulesRepository;
import com.godaddy.sonar.ruby.metricfu.RoodiRulesRepository;
import com.godaddy.sonar.ruby.simplecovrcov.SimpleCovRcovJsonParserImpl;
import com.godaddy.sonar.ruby.simplecovrcov.SimpleCovRcovSensor;

/**
 * This class is the entry point for all extensions
 */
@Properties({})
public final class RubyPlugin extends SonarPlugin
{
	public static final String KEY_REPOSITORY_CANE = "cane";
	public static final String NAME_REPOSITORY_CANE = "Cane";

	public static final String KEY_REPOSITORY_REEK = "reek";
	public static final String NAME_REPOSITORY_REEK = "Reek";

	public static final String KEY_REPOSITORY_ROODI = "roodi";
	public static final String NAME_REPOSITORY_ROODI = "Roodi";

	public List<Class<? extends Extension>> getExtensions() {
		List<Class<? extends Extension>> extensions = new ArrayList<Class<? extends Extension>>();
		extensions.add(Ruby.class);
		extensions.add(SimpleCovRcovSensor.class);
		extensions.add(SimpleCovRcovJsonParserImpl.class);
		extensions.add(MetricfuYamlParser.class);
		extensions.add(RubySourceImporter.class);
		extensions.add(RubySourceCodeColorizer.class);
		extensions.add(RubySensor.class);
		extensions.add(MetricfuComplexitySensor.class);
		extensions.add(MetricfuDuplicationSensor.class);
		extensions.add(MetricfuIssueSensor.class);
		extensions.add(CaneRulesRepository.class);
		extensions.add(ReekRulesRepository.class);
		extensions.add(RoodiRulesRepository.class);

		// Profiles
		extensions.add(SonarWayProfile.class);

		return extensions;
	}
}
