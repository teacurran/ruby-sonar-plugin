package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.yaml.snakeyaml.Yaml;

import com.godaddy.sonar.ruby.RubyPlugin;
import com.godaddy.sonar.ruby.metricfu.FlayReason.Match;

public class MetricfuYamlParser implements BatchExtension {
    private static final Logger LOG = LoggerFactory.getLogger(MetricfuYamlParser.class);

	private static final String REPORT_FILE = "tmp/metric_fu/report.yml";
	private static Pattern escapePattern = Pattern.compile("\\e\\[\\d+m", Pattern.CASE_INSENSITIVE);

	private Map<String, Object> metricfuResult = null;

	ArrayList<Map<String, Object>> saikuroFiles = null;
	Map<String, Object> caneViolations = null;
	ArrayList<Map<String, Object>> roodiProblems = null;
	ArrayList<Map<String, Object>> reekFiles = null;
	ArrayList<Map<String, Object>> flayReasons = null;

	public MetricfuYamlParser(Project project, Settings settings, FileSystem fileSystem) {
		this(project, settings, fileSystem, REPORT_FILE);
	}

	@SuppressWarnings("unchecked")
    public MetricfuYamlParser(Project project, Settings settings, FileSystem fileSystem, String filename) {
        String prop = settings.getString(RubyPlugin.METRICFU_REPORT_PATH_PROPERTY);
        if (prop != null) {
            filename = prop;
        }

        FilePredicates p = fileSystem.predicates();
        LOG.error("Looking up report file: file:**/" + filename);
        File report = new File(filename);
		if (report.exists()) {
		    LOG.error("Report file: " + report.getPath());
            try {
                FileInputStream input = new FileInputStream(report);
                Yaml yaml = new Yaml();

                this.metricfuResult = yaml.loadAs(input, Map.class);
            } catch (FileNotFoundException e) {
                LOG.error("File '" + report.getPath() + "' not found.", e);
            }
		} else {
			LOG.error("File '" + filename + "' not found.");
			Iterable<File> reports = fileSystem.files(p.matchesPathPattern("katello/"+filename));
			LOG.error("Got some files = " + (reports.iterator().hasNext() ? "true" : " false"));
			File ff = fileSystem.resolvePath("katello/"+filename);
            LOG.error("Got a file = " + (ff != null ? "true" : " false"));
		}
	}

	@SuppressWarnings("unchecked")
	public List<SaikuroClassComplexity> parseSaikuro(String filename) {
	    LOG.error("parseSaikuro: " + filename);
        List<SaikuroClassComplexity> complexities = new ArrayList<SaikuroClassComplexity>();
	    if (metricfuResult == null) {
	        LOG.warn("No metricfu results for saikuro.");
	    } else {
    		if (saikuroFiles == null) {
    			Map<String, Object> saikuro = (Map<String, Object>) metricfuResult.get(":saikuro");
    			if (saikuro != null) {
    			    saikuroFiles = (ArrayList<Map<String, Object>>) saikuro.get(":files");
    			}
    		}

    		if (saikuroFiles != null) {
    			for (Map<String, Object> fileInfo : saikuroFiles) {
    				String file = (String) fileInfo.get(":filename");
    				LOG.error("  checking: " + file);
    				if (file.equals(filename)) {
    					ArrayList<Map<String, Object>> classes = (ArrayList<Map<String, Object>>) fileInfo.get(":classes");

    					for (Map<String, Object> cls : classes) {
                            SaikuroClassComplexity c = new SaikuroClassComplexity();
                            c.setFile(file);
                            c.setName((String) cls.get(":class_name"));
                            c.setLines((Integer) cls.get(":lines"));
                            c.setComplexity((Integer) cls.get(":complexity"));

                            ArrayList<Map<String, Object>> methods = (ArrayList<Map<String, Object>>) cls.get(":methods");
    						for (Map<String, Object> method : methods) {
    						    SaikuroMethodComplexity m = new SaikuroMethodComplexity();
    							m.setName((String) method.get(":name"));
    							m.setComplexity((Integer) method.get(":complexity"));
    							m.setLine((Integer) method.get(":lines"));
    							LOG.error("    adding method: " + m.getName() + ", complexit = " + m.getComplexity());
    							c.getMethods().add(m);
    						}
    						LOG.error("    adding class: " + c.getName() + ", complexity = " + c.getComplexity());
    						complexities.add(c);
    					}
    					break;
    				}
    			}
    		}
	    }
		return complexities;
	}

	@SuppressWarnings("unchecked")
	public List<CaneViolation> parseCane(String filename) {
        List<CaneViolation> violations = new ArrayList<CaneViolation>();
        if (metricfuResult == null) {
            LOG.warn("No metricfu results for cane.");
        } else {
    		if (caneViolations == null) {
    			Map<String, Object> caneResult = (Map<String, Object>) metricfuResult.get(":cane");
    			if (caneResult != null) {
    			    caneViolations = (Map<String, Object>) caneResult.get(":violations");
    			}
    		}

    		if (caneViolations != null) {
    			ArrayList<Map<String, Object>> caneViolationsComplexityResult = (ArrayList<Map<String, Object>>) caneViolations.get(":abc_complexity");
    			for (Map<String, Object> caneViolationsLineResultRow : caneViolationsComplexityResult) {
    				String file = (String)caneViolationsLineResultRow.get(":file");
                    if (filename.equals(file)) {
    					CaneComplexityViolation violation = new CaneComplexityViolation();
    					violation.setFile(file);
    					violation.setMethod((String)caneViolationsLineResultRow.get(":method"));
    					violation.setComplexity(Integer.parseInt((String)caneViolationsLineResultRow.get(":complexity")));
    					violations.add(violation);
    				}
    			}

    			ArrayList<Map<String, Object>> caneViolationsLineResult = (ArrayList<Map<String, Object>>) caneViolations.get(":line_style");
    			for (Map<String, Object> caneViolationsLineResultRow : caneViolationsLineResult) {
    				String parts[] = ((String)caneViolationsLineResultRow.get(":line")).split(":");
    				if (parts[0].length() > 0 && parts[0].equals(filename)) {
    					CaneLineStyleViolation violation = new CaneLineStyleViolation();
    					violation.setFile(parts[0]);
    					violation.setLine(Integer.parseInt(parts[1]));
    					violation.setDescription((String)caneViolationsLineResultRow.get(":description"));
    					violations.add(violation);
    				}
    			}

    			ArrayList<Map<String, Object>> caneViolationsCommentResult = (ArrayList<Map<String, Object>>) caneViolations.get(":comment");
    			for (Map<String, Object> caneViolationsLineResultRow : caneViolationsCommentResult) {
    				String parts[] = ((String)caneViolationsLineResultRow.get(":line")).split(":");
    				if (parts[0].length() > 0 && parts[0].equals(filename)) {
    					CaneCommentViolation violation = new CaneCommentViolation();
    					violation.setFile(parts[0]);
    					violation.setLine(Integer.parseInt(parts[1]));
    					violation.setClassName((String)caneViolationsLineResultRow.get(":class_name"));
    					violations.add(violation);
    				}
    			}
    		}
        }
		return violations;
	}

	@SuppressWarnings("unchecked")
	public List<RoodiProblem> parseRoodi(String filename) {
        List<RoodiProblem> problems = new ArrayList<RoodiProblem>();
        if (metricfuResult == null) {
            LOG.warn("No metricfu results for roodi.");
        } else {
    		if (roodiProblems == null) {
    			Map<String, Object> roodi = (Map<String, Object>) metricfuResult.get(":roodi");
    			if (roodi != null) {
    			    roodiProblems = (ArrayList<Map<String, Object>>) roodi.get(":problems");
    			}
    		}

    		if (roodiProblems != null) {

    			for (Map<String, Object> prob : roodiProblems) {
    				String file = escapePattern.matcher(safeString((String) prob.get(":file"))).replaceAll("");

                    if (filename.equals(file)) {
    					RoodiProblem problem = new RoodiProblem();
    					problem.setFile(file);
    					problem.setLine(safeInteger((String)prob.get(":line")));
    					problem.setProblem(escapePattern.matcher(safeString((String) prob.get(":problem"))).replaceAll(""));

    					if (problem.getFile().length() > 0 && problem.getLine() > 0) {
    						problems.add(problem);
    					}
    				}
    			}
    		}
        }
		return problems;
	}

	@SuppressWarnings("unchecked")
	public List<ReekSmell> parseReek(String filename) {
        List<ReekSmell> smells = new ArrayList<ReekSmell>();
        if (metricfuResult == null) {
            LOG.warn("No metricfu results for reek.");
        } else {
    		if (reekFiles == null) {
    			Map<String, Object> reek = (Map<String, Object>) metricfuResult.get(":reek");
    			if (reek != null) {
    			    reekFiles = (ArrayList<Map<String, Object>>) reek.get(":matches");
    			}
    		}

    		if (reekFiles != null) {

    			for (Map<String, Object> resultFile : reekFiles) {
    				String file = safeString((String) resultFile.get(":file_path"));

    				if (filename.equals(file)) {
    					ArrayList<Map<String, Object>> resultSmells = (ArrayList<Map<String, Object>>) resultFile.get(":code_smells");

    					for (Map<String, Object> resultSmell : resultSmells) {
    						ReekSmell smell = new ReekSmell();
    						smell.setFile(file);
    						smell.setMethod(safeString((String)resultSmell.get(":method")));
    						smell.setMessage(safeString((String)resultSmell.get(":message")));
    						smell.setType(safeString((String)resultSmell.get(":type")));
    						smells.add(smell);
    					}
    					break;
    				}
    			}
    		}
        }
		return smells;
	}

	@SuppressWarnings("unchecked")
	public List<FlayReason> parseFlay() {
        List<FlayReason> reasons = new ArrayList<FlayReason>();
        if (metricfuResult == null) {
            LOG.warn("No metricfu results for flay.");
        } else {
    		if (flayReasons == null) {
    			Map<String, Object> flay = (Map<String, Object>) metricfuResult.get(":flay");
    			if (flay != null) {
    			    flayReasons = (ArrayList<Map<String, Object>>) flay.get(":matches");
    			}
    		}

    		if (flayReasons != null) {

    			for (Map<String, Object> resultReason : flayReasons) {
    				FlayReason reason = new FlayReason();
    				reason.setReason(safeString((String) resultReason.get(":reason")));

    				ArrayList<Map<String, Object>> resultMatches = (ArrayList<Map<String, Object>>) resultReason.get(":matches");
    				for (Map<String, Object> resultDuplication : resultMatches) {
    					Match match = reason.new Match((String)resultDuplication.get(":name"));

    					// If flay was run with --diff, we should have the number of lines in the duplication. If not, make it 1.
    					Integer line = safeInteger((String)resultDuplication.get(":line"));
    					if (line > 0) {
    						match.setStartLine(line);
    						match.setLines(1);
    					} else {
    						Integer start = safeInteger((String)resultDuplication.get(":start"));
    						if (start > 0) {
    							match.setStartLine(start);
    						}
    						Integer lines = safeInteger((String)resultDuplication.get(":lines"));
    						if (lines > 0) {
    							match.setLines(lines);
    						}
    					}
    					reason.getMatches().add(match);
    				}
    				reasons.add(reason);
    			}
    		}
        }
		return reasons;
	}

	private String safeString (String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	private Integer safeInteger (String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}
}
