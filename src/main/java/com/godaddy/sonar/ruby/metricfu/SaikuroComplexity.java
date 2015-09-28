package com.godaddy.sonar.ruby.metricfu;

public class SaikuroComplexity
{

    private String file;
    private int line;
    private String name;
    private int complexity = -1;

    public SaikuroComplexity(String file, int line, String name, int complexity)
    {
        this.file = file;
        this.name = name;
        this.line = line;
        this.complexity = complexity;
    }

    public SaikuroComplexity()
    {
    }

    public String getFile()
    {
        return file;
    }

    public void setFile(String file)
    {
        this.file = file;
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

    public int getComplexity()
    {
        return complexity;
    }

    public void setComplexity(int complexity)
    {
        this.complexity = complexity;
    }

    @Override
    public String toString()
    {
        return "file: " + file + " line: " + line + " name: " + name + " complexity: " + complexity;
    }
}
