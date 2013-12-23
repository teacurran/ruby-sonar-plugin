package com.godaddy.sonar.ruby.metricfu;

public class CaneViolation {
    private String file;
    private int line;
    private String violation;

    public CaneViolation(String file, int line, String violation) {
        this.file = file;
        this.line = line;
        this.violation = violation;
    }

    public CaneViolation() {
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    @Override
    public String toString() {
        return "file: " + file + " line: " + line + " violation: " + violation;
    }
}
