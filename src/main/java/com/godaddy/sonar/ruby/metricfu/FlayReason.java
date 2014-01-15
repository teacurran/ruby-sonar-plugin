package com.godaddy.sonar.ruby.metricfu;

import java.util.ArrayList;

public class FlayReason {
	
	public class Match {
		private String file;
		private Integer start;
		private Integer end; // Currently flay does provide an end line, but when it does...
		
		public Match(String file, Integer start, Integer end) {
			this.file = file;
			this.start = start;
			this.setEndLine(end);
		}
		
		public Match(String file, Integer start) {
			this(file, start, start);
		}
		
		public String getFile() {
			return file;
		}
		public void setFile(String file) {
			this.file = file;
		}
		public Integer getStartLine() {
			return start;
		}
		public void setStartLine(Integer start) {
			this.start = start;
		}
		public Integer getEndLine() {
			return end;
		}
		public void setEndLine(Integer end) {
			this.end = end;
		}
		public Integer getNumLines() {
			return end - start + 1;
		}
	}

    private String reason;
    private ArrayList<Match> matches = new ArrayList<Match>();

    public FlayReason(String reason) {
        this.reason = reason;
    }

    public FlayReason() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

	public ArrayList<Match> getMatches() {
		return matches;
	}
	
	public void addMatch(String file, Integer start) {
		matches.add(new Match(file, start));
	}

    @Override
    public String toString() {
        return "reason: " + reason;
    }
}
