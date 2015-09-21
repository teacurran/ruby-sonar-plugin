package com.godaddy.sonar.ruby.metricfu;

import org.apache.commons.io.IOUtils;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

import com.godaddy.sonar.ruby.RubyPlugin;
import com.godaddy.sonar.ruby.core.Ruby;

import java.io.InputStream;
import java.util.List;

public class ReekRulesRepository extends RuleRepository {

	public ReekRulesRepository() {
		super(RubyPlugin.KEY_REPOSITORY_REEK, Ruby.KEY);
		setName(RubyPlugin.NAME_REPOSITORY_REEK);
	}

	@Override
	public List<Rule> createRules() {
		XMLRuleParser parser = new XMLRuleParser();
		InputStream input = ReekRulesRepository.class.getResourceAsStream("/ReekRulesRepository.xml");
		try {
			return parser.parse(input);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
}
