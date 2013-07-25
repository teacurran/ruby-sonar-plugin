[![Build Status](http://jenkins.notjomax.com/buildStatus/icon?job=ruby-sonar-plugin)](http://jenkins.notjomax.com/job/ruby-sonar-plugin/)
Sonar Ruby Plugin    
=================
##Description / Features
The plugin enables analysis of Ruby projects within SonarQube

Currently the plugin captures basic metrics (Lines of Code, Number of classes and packages, Comment percentage), 
complexity on each file, and a visual line-by-line code coverage report.

It relies on well-known external tools: [SimpleCov](https://github.com/colszowka/simplecov), [SimpleCov-RCov](https://github.com/fguillen/simplecov-rcov) and [Metric_Fu](https://github.com/metricfu/metric_fu/)

##Install
Download the plugin into the SONARQUBE_HOME/extensions/plugins directory

##Usage
Make sure the property sonar.language is set to ruby: `sonar.language=ruby` in the sonar-project.properties file

#####Code Coverage
In order for the plugin to report on code coverage, the ruby project needs to be using [simplecov-rcov](https://github.com/fguillen/simplecov-rcov) 
to generate a coverage report when you run your tests/specs, please see the gem's homepage [here](https://github.com/fguillen/simplecov-rcov) for installation
and usage instructions.  
**Important:** Do not change the output directory for the simplecov-rcov report, leave it as default, or code coverage will not be reported.

#####Code Complexity
In order for the plugin to report on code complexity, [metric_fu](https://github.com/metricfu/metric_fu/) needs to be ran against the ruby project,
which will generate a metric report. Please see the gem's homepage [here](https://github.com/metricfu/metric_fu/) for installation and usage instructions.  
**Important:** metric_fu reports on more than just code complexity, however we still recommend to use the metric_fu command: `metric_fu -r`
this will run all metrics. At the very least, Saikuro and Hotspots metrics need to be ran for complexity to be reported.
Also, do not change the output directory for the metric_fu report, leave it as default or code complexity will not be reported

##Future Plans
* Code Duplication
* Code Violations
* Configuration option to specify simplecov-rcov and metric_fu report directories

##Giving Credit
The github project [pica/ruby-sonar-plugin](https://github.com/pica/ruby-sonar-plugin), is where the ruby-sonar-plugin started, rather than reinvent the wheel, we thought it better to enhance it.
We used that plugin as a starting point for basic stats, then, updated the references to their latest versions and added additional metrics like line-by-line code coverage and code complexity.

We referenced the [javascript sonar plugin](https://github.com/SonarCommunity/sonar-javascript) and the [php sonar plugin](https://github.com/SonarCommunity/sonar-php) for complexity and coverage implementation.
Our complexity sensor and code coverage sensor borrow heavily from the javascript plugin's equivalent sensors.

##Tool Versions
* We are using Sonar 3.5.1 along with the Sonar 3.5.1 api (You must be using at least sonar 3.5 for the plugin to work)
* We are using Sonar-Runner 2.2.2 to anaylze our projects
* metric_fu 4.2.1 (latest at time of edit)
* simplecov 0.7.1 (latest at time of edit)
* simplecov-rcov 0.2.3 (latest at time of edit)
