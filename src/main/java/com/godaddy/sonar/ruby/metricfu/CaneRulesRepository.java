package com.godaddy.sonar.ruby.metricfu;

import org.apache.commons.io.IOUtils;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

import com.godaddy.sonar.ruby.RubyPlugin;
import com.godaddy.sonar.ruby.core.Ruby;

import java.io.InputStream;
import java.util.List;

public class CaneRulesRepository extends RuleRepository {
	public CaneRulesRepository() {
		super(RubyPlugin.KEY_REPOSITORY_CANE, Ruby.KEY);
		setName(RubyPlugin.NAME_REPOSITORY_CANE);
	}


	@Override
	public List<Rule> createRules() {
		XMLRuleParser parser = new XMLRuleParser();
		InputStream input = CaneRulesRepository.class.getResourceAsStream("/CaneRulesRepository.xml");
		try {
			return parser.parse(input);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
}
