package com.godaddy.sonar.ruby.metricfu;

public class MetricBase {
    protected int line;
    protected String file;

    public MetricBase() {
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "file: " + file + " line: " + line;
    }
}
