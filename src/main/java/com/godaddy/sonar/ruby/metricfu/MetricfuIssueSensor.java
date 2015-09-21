package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issuable.IssueBuilder;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;

import com.godaddy.sonar.ruby.RubyPlugin;
import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.core.RubyFile;
import com.godaddy.sonar.ruby.metricfu.RoodiProblem.RoodiCheck;

import com.google.common.collect.Lists;

public class MetricfuIssueSensor implements Sensor
{
    private static final Logger LOG = LoggerFactory.getLogger(MetricfuIssueSensor.class);

    private static final Integer NO_LINE_NUMBER = -1;

    private MetricfuYamlParser metricfuYamlParser;
    private FileSystem fs;
    private final ResourcePerspectives perspectives;

    public MetricfuIssueSensor(FileSystem fs, MetricfuYamlParser metricfuYamlParser, ResourcePerspectives perspectives) {
        this.fs = fs;
        this.metricfuYamlParser = metricfuYamlParser;
        this.perspectives = perspectives;
    }

    public boolean shouldExecuteOnProject(Project project) {
        return Ruby.KEY.equals(project.getLanguageKey());
    }

    public void analyse(Project project, SensorContext context) {
        List<InputFile> sourceFiles = Lists.newArrayList(fs.inputFiles(fs.predicates().hasLanguage("ruby")));

        for (InputFile inputFile : sourceFiles) {
            LOG.debug("analyzing issues in the file: " + inputFile.file().getName());
            try {
                analyzeFile(inputFile, sourceFiles, context);
            } catch (IOException e) {
                LOG.error("Can not analyze the file " + inputFile.file().getAbsolutePath() + " for issues");
            }
        }
    }

    private void analyzeFile(InputFile inputFile, List<InputFile> sourceFiles, SensorContext sensorContext) throws IOException
    {
        RubyFile resource = new RubyFile(inputFile.file(), sourceFiles);
        List<ReekSmell> smells = metricfuYamlParser.parseReek(resource.getName());

        for (ReekSmell smell : smells) {
            addIssue(resource, RubyPlugin.KEY_REPOSITORY_REEK, smell.getType(), ReekSmell.toSeverity(smell.getType()), smell.getMessage());
        }

        List<RoodiProblem> problems = metricfuYamlParser.parseRoodi(resource.getName());
        for (RoodiProblem problem : problems) {
            RoodiCheck check = RoodiProblem.messageToKey(problem.getProblem());
            addIssue(resource, problem.getLine(), RubyPlugin.KEY_REPOSITORY_ROODI, check.toString(), RoodiProblem.toSeverity(check), problem.getProblem());
        }

        List<CaneViolation> violations = metricfuYamlParser.parseCane(resource.getName());
        for (CaneViolation violation : violations) {
            if (violation instanceof CaneCommentViolation) {
                CaneCommentViolation c = (CaneCommentViolation)violation;
                addIssue(resource, c.getLine(), RubyPlugin.KEY_REPOSITORY_CANE, c.getKey(), Severity.MINOR,
                    "Class ' " + c.getClassName() + "' requires explanatory comments on preceding line.");
            } else if (violation instanceof CaneComplexityViolation) {
                CaneComplexityViolation c = (CaneComplexityViolation)violation;
                addIssue(resource, NO_LINE_NUMBER, RubyPlugin.KEY_REPOSITORY_CANE, c.getKey(), Severity.MAJOR,
                    "Method '" + c.getMethod() + "' has ABC complexity of " + c.getComplexity() + ".");
            } else if (violation instanceof CaneLineStyleViolation) {
                CaneLineStyleViolation c = (CaneLineStyleViolation)violation;
                addIssue(resource, c.getLine(), RubyPlugin.KEY_REPOSITORY_CANE, c.getKey(), Severity.MINOR, c.getDescription() + ".");
            }
        }
    }

    public void addIssue(RubyFile resource, Integer line, String repo, String key, String severity, String message) {
        try {

            Issuable issuable = perspectives.as(Issuable.class, resource);
            IssueBuilder bld = issuable.newIssueBuilder()
                    .ruleKey(RuleKey.of(repo, key))
                    .message(message)
                    .severity(severity);
            if (line != NO_LINE_NUMBER) {
                bld = bld.line(line);
            }
            Issue issue = bld.build();
            if (!issuable.addIssue(issue)) {
                LOG.error("Failed to register issue.\nIssue Object : " + issue.toString());
            }
        } catch(Exception e) {
            LOG.error("Error in create issue object" + e.getMessage());
        }
    }

    public void addIssue(RubyFile resource, String repo, String key, String severity, String message) {
        addIssue(resource, NO_LINE_NUMBER, repo, key, severity, message);
    }
}