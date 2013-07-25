package com.godaddy.sonar.ruby.metricfu;

public class RubyFunction
{

    private int complexity = -1;
    private int line;
    private String name;

    public RubyFunction(String name, int complexity, int line)
    {
        this.name = name;
        this.complexity = complexity;
        this.line = line;
    }

    public RubyFunction()
    {
    }

    public int getComplexity()
    {
        return complexity;
    }

    public void setComplexity(int complexity)
    {
        this.complexity = complexity;
    }

    public int getLine()
    {
        return line;
    }

    public void setLine(int line)
    {
        this.line = line;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "name: " + name + " complexity: " + complexity + " lines: " + line;
    }
}
