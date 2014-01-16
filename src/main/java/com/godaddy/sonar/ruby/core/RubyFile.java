package com.godaddy.sonar.ruby.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.scan.filesystem.PathResolver.RelativePath;
import org.sonar.api.utils.WildcardPattern;

import java.io.File;
import java.util.List;

public class RubyFile extends Resource<RubyPackage>
{
	private static final long serialVersionUID = 678217195520058883L;
	private String filename;
    private String longName;
    private String packageKey;
    private RubyPackage parent = null;

    public RubyFile(File file, List<File> sourceDirs)
    {
        super();

        if (file == null)
        {
            throw new IllegalArgumentException("File cannot be null");
        }

        this.packageKey = RubyPackage.DEFAULT_PACKAGE_NAME;
        this.filename = StringUtils.substringBeforeLast(file.getName(), ".");
        this.longName = this.filename;
        String key = this.packageKey + File.separator + this.filename;

        if (sourceDirs != null)
        {
            PathResolver resolver = new PathResolver();
            RelativePath relativePath = resolver.relativePath(sourceDirs, file);
            if (relativePath != null && relativePath.path().indexOf(File.separator) >= 0)
            {                	
                this.packageKey = StringUtils.substringBeforeLast(relativePath.path(), File.separator);
                this.packageKey = StringUtils.strip(this.packageKey, File.separator);
                key = this.packageKey + File.separator + this.filename;
                this.longName = key;
            }
        } 
        setKey(key);
    }

    public RubyPackage getParent()
    {
        if (parent == null)
        {
            parent = new RubyPackage(packageKey);
        }
        return parent;
    }

    public String getDescription()
    {
        return null;
    }

    public Language getLanguage()
    {
        return Ruby.INSTANCE;
    }

    public String getName()
    {
        return filename;
    }

    public String getLongName()
    {
        return longName;
    }

    public String getScope()
    {
        return Scopes.FILE;
    }

    public String getQualifier()
    {
        return Qualifiers.CLASS;
    }

    public boolean matchFilePattern(String antPattern)
    {
        String patternWithoutFileSuffix = StringUtils.substringBeforeLast(antPattern, ".");
        WildcardPattern matcher = WildcardPattern.create(patternWithoutFileSuffix, File.separator);
        String key = getKey();
        return matcher.match(key);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("key", getKey()).append("package", packageKey).append("longName", longName).toString();
    }
}
