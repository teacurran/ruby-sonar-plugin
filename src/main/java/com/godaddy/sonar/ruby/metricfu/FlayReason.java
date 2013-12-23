package com.godaddy.sonar.ruby.metricfu;

import java.util.ArrayList;

public class FlayReason {
	
	public class Match {
		private String file;
		private Integer line;
		
		public Match(String file, Integer line) {
			this.file = file;
			this.line = line;
		}
		
		public String getFile() {
			return file;
		}
		public void setFile(String file) {
			this.file = file;
		}
		public Integer getLine() {
			return line;
		}
		public void setLine(Integer line) {
			this.line = line;
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
	
	public void addMatch(String file, Integer line) {
		matches.add(new Match(file, line));
	}

    @Override
    public String toString() {
        return "reason: " + reason;
    }
}
