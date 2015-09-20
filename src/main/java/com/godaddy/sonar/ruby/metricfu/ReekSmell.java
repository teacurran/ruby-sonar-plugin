package com.godaddy.sonar.ruby.metricfu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sonar.api.rule.Severity;

public class ReekSmell extends MetricBase {

    private String method;
    private String message;
    private String type;

	public static enum Smell {
		Attribute,
		ClassVariable,
		ControlCouple,
		BooleanParameter,
		ControlParameter,
		DataClump,
		Duplication,
		DuplicateMethodCall,
		FeatureEnvy,
		UtilityFunction,
		IrresponsibleModule,
		LongParameterList,
		LongYieldList,
		NestedIterators,
		SimulatedPolymorphism,
		NilCheck,
		RepeatedConditional,
		LargeClass,
		TooManyInstanceVariables,
		TooManyMethods,
		TooManyStatements,
		UncommunicativeName,
		UncommunicativeMethodName,
		UncommunicativeModuleName,
		UncommunicativeParameterName,
		UncommunicativeVariableName,
		UnusedParameters,
	}

	private static final Map<Smell, String> keyToSeverityMap;

	static {
		HashMap<Smell, String> mapKeyToSeverity = new HashMap<Smell, String>();
		mapKeyToSeverity.put(Smell.Attribute, Severity.MINOR);
		mapKeyToSeverity.put(Smell.ClassVariable, Severity.MINOR);
		mapKeyToSeverity.put(Smell.ControlCouple, Severity.MAJOR);
		mapKeyToSeverity.put(Smell.BooleanParameter, Severity.MAJOR);
		mapKeyToSeverity.put(Smell.ControlParameter, Severity.MAJOR);
		mapKeyToSeverity.put(Smell.DataClump, Severity.MINOR);
		mapKeyToSeverity.put(Smell.Duplication, Severity.MINOR);
		mapKeyToSeverity.put(Smell.DuplicateMethodCall, Severity.MINOR);
		mapKeyToSeverity.put(Smell.FeatureEnvy, Severity.MAJOR);
		mapKeyToSeverity.put(Smell.UtilityFunction, Severity.MAJOR);
		mapKeyToSeverity.put(Smell.IrresponsibleModule, Severity.INFO);
		mapKeyToSeverity.put(Smell.LongParameterList, Severity.MINOR);
		mapKeyToSeverity.put(Smell.LongYieldList, Severity.MINOR);
		mapKeyToSeverity.put(Smell.NestedIterators, Severity.MINOR);
		mapKeyToSeverity.put(Smell.SimulatedPolymorphism, Severity.MINOR);
		mapKeyToSeverity.put(Smell.NilCheck, Severity.MINOR);
		mapKeyToSeverity.put(Smell.RepeatedConditional, Severity.MINOR);
		mapKeyToSeverity.put(Smell.LargeClass, Severity.MINOR);
		mapKeyToSeverity.put(Smell.TooManyInstanceVariables, Severity.MINOR);
		mapKeyToSeverity.put(Smell.TooManyMethods, Severity.MINOR);
		mapKeyToSeverity.put(Smell.TooManyStatements, Severity.MINOR);
		mapKeyToSeverity.put(Smell.UncommunicativeName, Severity.MINOR);
		mapKeyToSeverity.put(Smell.UncommunicativeMethodName, Severity.MINOR);
		mapKeyToSeverity.put(Smell.UncommunicativeModuleName, Severity.MINOR);
		mapKeyToSeverity.put(Smell.UncommunicativeParameterName, Severity.MINOR);
		mapKeyToSeverity.put(Smell.UncommunicativeVariableName, Severity.MINOR);
		mapKeyToSeverity.put(Smell.UnusedParameters, Severity.MINOR);
		keyToSeverityMap = Collections.unmodifiableMap(mapKeyToSeverity);
	}

    public ReekSmell(String file, String method, String message, String type) {
        this.file = file;
        this.method = method;
        this.message = message;
        this.type = type;
    }

    public ReekSmell() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "file: " + file + " methods: " + method + " message: " + message + " type: " + type;
    }
	
	public static String toSeverity(Smell smell) {
		if (keyToSeverityMap.containsKey(smell)) {
			return keyToSeverityMap.get(smell);
		}
		return Severity.BLOCKER; // Make sure we catch this case.
	}
	
	public static String toSeverity(String smell) {
		try {
			return toSeverity(Smell.valueOf(smell));
		} catch (Exception e) {
			return Severity.BLOCKER;
		}
	}
}