package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jfree.util.Log;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.FileQuery;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.sonar.api.measures.RangeDistributionBuilder;
import org.sonar.api.resources.Project;

import com.godaddy.sonar.ruby.core.RubyFile;
import com.google.common.collect.Lists;

public class MetricfuDuplicationSensor implements Sensor
{
	private MetricfuYamlParser metricfuYamlParser;
  private FileSystem fs;

	public MetricfuDuplicationSensor(FileSystem fs, MetricfuYamlParser metricfuYamlParser)
	{
    this.fs = fs;
    this.metricfuYamlParser = metricfuYamlParser;
	}

	public boolean shouldExecuteOnProject(Project project)
	{
		return fs.hasFiles(fs.predicates().hasLanguage("ruby"));
	}

	public void analyse(Project project, SensorContext context)
	{
		List<InputFile> sourceFiles = Lists.newArrayList(fs.inputFiles(fs.predicates().hasLanguage("ruby")));

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.newDocument();
			Element root = doc.createElement("duplications");
			doc.appendChild(root);

			HashMap<String, Double> duplicated_blocks = new HashMap<String, Double>();
			HashMap<String, Double> duplicated_lines = new HashMap<String, Double>();
			HashMap<String, Document> duplicated_xml = new HashMap<String, Document>();

			List<FlayReason> duplications = metricfuYamlParser.parseFlay();
			for (FlayReason duplication : duplications) {
				Element group = doc.createElement("g");
				for (FlayReason.Match match : duplication.getMatches()) {
					File file = new File(fs.baseDir(), match.getFile());
					RubyFile resource = new RubyFile(file, sourceFiles);
					String key = project.getKey() + ":" + resource.getKey();
					if (duplicated_blocks.containsKey(key)) {
						duplicated_blocks.put(key, duplicated_blocks.get(key)+1);
					} else {
						duplicated_blocks.put(key, 1.0);
					}

					if (duplicated_lines.containsKey(key)) {
						duplicated_lines.put(key, duplicated_lines.get(key)+match.getLines());
					} else {
						duplicated_lines.put(key, match.getLines() * 1.0);
					}

					Element block = doc.createElement("b");
					block.setAttribute("r", key);
					block.setAttribute("s", match.getStartLine().toString());
					block.setAttribute("l", match.getLines().toString());
					group.appendChild(block);
				}

				// Now that we have the group XML, add it to each file.
				HashSet<String> already_added = new HashSet<String>();
				for (FlayReason.Match match : duplication.getMatches()) {
					File file = new File(fs.baseDir(), match.getFile());
					RubyFile resource = new RubyFile(file, sourceFiles);
					String key = project.getKey() + ":" + resource.getKey();
					if (!duplicated_xml.containsKey(key)) {
						Document d = builder.newDocument();
						Element r = d.createElement("duplications");
						d.appendChild(r);
						duplicated_xml.put(key, d);
					}

					// If we have duplications in the same file, only add them once.
					if (!already_added.contains(key)) {
						Document d = duplicated_xml.get(key);
						d.getFirstChild().appendChild(d.importNode(group, true));
						already_added.add(key);
					}
				}
			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			for (InputFile inputFile : sourceFiles)
			{
				RubyFile resource = new RubyFile(inputFile.file(), sourceFiles);
				String key = project.getKey() + ":" + resource.getKey();
				if (duplicated_blocks.containsKey(key)) {
					context.saveMeasure(resource, CoreMetrics.DUPLICATED_FILES, 1.0);
					context.saveMeasure(resource, CoreMetrics.DUPLICATED_BLOCKS, duplicated_blocks.get(key));
					context.saveMeasure(resource, CoreMetrics.DUPLICATED_LINES, duplicated_lines.get(key));
				} else {
					context.saveMeasure(resource, CoreMetrics.DUPLICATED_FILES, 0.0);
				}

				if (duplicated_xml.containsKey(key)) {
					StringWriter writer = new StringWriter();
					transformer.transform(new DOMSource(duplicated_xml.get(key)), new StreamResult(writer));
					context.saveMeasure(resource, new Measure(CoreMetrics.DUPLICATIONS_DATA, writer.getBuffer().toString()));
				}
			}

		} catch (Exception e) {
			Log.error("Exception raised while processing duplications.", e);
		}
	}
}