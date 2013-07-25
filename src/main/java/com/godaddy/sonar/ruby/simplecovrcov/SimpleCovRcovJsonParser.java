package com.godaddy.sonar.ruby.simplecovrcov;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.sonar.api.BatchExtension;
import org.sonar.api.measures.CoverageMeasuresBuilder;

public interface SimpleCovRcovJsonParser extends BatchExtension
{
    Map<String, CoverageMeasuresBuilder> parse(File file) throws IOException;
}
