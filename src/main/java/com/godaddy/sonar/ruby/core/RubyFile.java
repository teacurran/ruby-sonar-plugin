package com.godaddy.sonar.ruby.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.scan.filesystem.PathResolver.RelativePath;
import org.sonar.api.utils.WildcardPattern;

import java.io.File;
import java.util.List;

public class RubyFile extends Resource<RubyPackage> {

	private static final long serialVersionUID = 1L;

	private String filename;
	private String longName;
	private String packageKey;
	private RubyPackage parent = null;

	public RubyFile(String key) {
		super();
		String realKey = StringUtils.trim(key);

		if (realKey.contains(".")) {
			this.filename = StringUtils.substringAfterLast(realKey, ".");
			this.packageKey = StringUtils.substringBeforeLast(realKey, ".");
			this.longName = realKey;

		} else {
			this.filename = realKey;
			this.longName = realKey;
			this.packageKey = RubyPackage.DEFAULT_PACKAGE_NAME;
			realKey = new StringBuilder()
					.append(RubyPackage.DEFAULT_PACKAGE_NAME).append(".")
					.append(realKey).toString();
		}
		setKey(realKey);
	}

	public RubyFile(File file, List<File> sourceDirs) {
		super();
		
		PathResolver resolver = new PathResolver();
		RelativePath relativePath = resolver.relativePath(sourceDirs, file);
		if (relativePath != null) 
		{
			String packName = null;
			String className = relativePath.toString();
			
			String path = relativePath.path();
			if (path.indexOf('/') >= 0) 
			{
				packName = StringUtils.substringBeforeLast(path, "/");
				packName = StringUtils.replace(packName, "/", ".");
				className = StringUtils.substringAfterLast(path, "/");
			}
			className = StringUtils.substringBeforeLast(className, ".");
			this.setPackageKey(packName);
			this.setLongName(className);
			return;
		}
		throw new IllegalArgumentException("Relative path cannot be found.");
	}

	public RubyFile(String packageKey, String className) {
		super();

		this.filename = className.trim();
		String key;
		if (StringUtils.isBlank(packageKey)) {
			this.packageKey = RubyPackage.DEFAULT_PACKAGE_NAME;
			this.longName = this.filename;
			key = new StringBuilder().append(this.packageKey).append(".").append(this.filename).toString();
		} else {
			this.packageKey = packageKey.trim();
			key = new StringBuilder().append(this.packageKey).append(".").append(this.filename).toString();
			this.longName = key;
		}
		setKey(key);
	}
	
	public RubyPackage getParent() {
		if (parent == null) {
			parent = new RubyPackage(packageKey);
		}
		return parent;
	}

	public String getDescription() {
		return null;
	}

	public Language getLanguage() {
		return Ruby.INSTANCE;
	}

	public String getName() {
		return filename;
	}

	public String getLongName() {
		return longName;
	}

	public String getScope() {
		return Scopes.FILE;
	}

	public String getQualifier() {
		return Qualifiers.CLASS;
	}

	public boolean matchFilePattern(String antPattern) {
		String patternWithoutFileSuffix = StringUtils.substringBeforeLast(
				antPattern, ".");
		WildcardPattern matcher = WildcardPattern.create(
				patternWithoutFileSuffix, ".");
		return matcher.match(getKey());
	}

	private void setPackageKey(String packName)
	{
		
	}
	
	private void setLongName(String longName)
	{
		
	}
	
	/**
	 * Creates a {@link RubyFile} from a file in the source directories.
	 * 
	 * @param unitTest
	 *            whether it is a unit test file or a source file
	 * @return the {@link RubyFile} created if exists, null otherwise
	 */
	public static RubyFile fromIOFile(File file, List<File> sourceDirs, boolean unitTest) {
		if (file == null) 
		{
			return null;
		}
		PathResolver resolver = new PathResolver();
		RelativePath relativePath = resolver.relativePath(sourceDirs, file);
		if (relativePath != null) 
		{
			String pacname = null;
			String classname = relativePath.toString();
			
			String path = relativePath.path();
			if (path.indexOf('/') >= 0) 
			{
				pacname = StringUtils.substringBeforeLast(path, "/");
				pacname = StringUtils.replace(pacname, "/", ".");
				classname = StringUtils.substringAfterLast(path, "/");
			}
			classname = StringUtils.substringBeforeLast(classname, ".");
			return new RubyFile(pacname, classname, unitTest);
		}
		return null;
	}

	/**
	 * Shortcut to {@link #fromIOFile(File, List, boolean)} with an absolute
	 * path.
	 */
	public static RubyFile fromAbsolutePath(String path, List<File> sourceDirs,
			boolean unitTest) {
		if (path == null) {
			return null;
		}
		return fromIOFile(new File(path), sourceDirs, unitTest);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("key", getKey())
				.append("package", packageKey).append("longName", longName)
				.append("unitTest", unitTest).toString();
	}
}
