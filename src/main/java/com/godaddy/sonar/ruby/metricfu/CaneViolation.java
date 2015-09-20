package com.godaddy.sonar.ruby.metricfu;

public abstract class CaneViolation extends MetricBase {
    public CaneViolation(String file) {
        this.file = file;
    }

    public CaneViolation() {
    }

    public abstract String getKey();

    @Override
    public String toString() {
        return "file: " + file;
    }
}