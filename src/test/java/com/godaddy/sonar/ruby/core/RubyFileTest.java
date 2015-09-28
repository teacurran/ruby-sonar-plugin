package com.godaddy.sonar.ruby.core;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Scopes;

public class RubyFileTest {
	private final static String SOURCE_DIR = "/path/to";  // Equivalent to sonar.sources in project properties.
	protected final static String SOURCE_FILE = SOURCE_DIR + "/source/file.rb";
	
	protected RubyFile rubyFile;
	
	@Before
	public void setUp() {
		File file = new File(SOURCE_FILE);
		List<File> sourceDirs = new ArrayList<File>();
		sourceDirs.add(new File(SOURCE_DIR));
		
		rubyFile = new RubyFile(file, sourceDirs);
	}
	
	@After 
	public void tearDown() {
		
	}
	
	

	@Test(expected=IllegalArgumentException.class)
	public void testRubyFileWithNullFile() {
		new RubyFile(null, new ArrayList<File>());
	}
	
	@Test
	public void testRubyFileWithNullSourceDirs() {
		File file = new File(SOURCE_FILE);
		rubyFile = new RubyFile(file, null);
		assertEquals("[default]/file", rubyFile.getKey());
	}
	
	@Test
	public void testGetParent() {
		RubyPackage parent = rubyFile.getParent();
		assertEquals("source", parent.getKey());
	}

	@Test
	public void testGetDescription() {
		assertNull(rubyFile.getDescription());
	}

	@Test
	public void testGetLanguage() {
		assertEquals(Ruby.INSTANCE, rubyFile.getLanguage());
	}

	@Test
	public void testGetName() {
		assertEquals("file", rubyFile.getName());
	}

	@Test
	public void testGetLongName() {
		assertEquals("source/file", rubyFile.getLongName());
	}

	@Test
	public void testGetScope() {
		assertEquals(Scopes.FILE, rubyFile.getScope());
	}

	@Test
	public void testGetQualifier() {
		assertEquals(Qualifiers.CLASS, rubyFile.getQualifier());
	}

	@Test
	public void testMatchFilePatternString() {
		assertTrue(rubyFile.matchFilePattern("source/file.rb"));
	}

	@Test
	public void testToString() {
		System.out.println(rubyFile.toString());
		assertTrue(rubyFile.toString().contains("key=source/file,package=source,longName=source/file"));
	}

}
