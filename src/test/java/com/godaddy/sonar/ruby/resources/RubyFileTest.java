package com.godaddy.sonar.ruby.resources;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.godaddy.sonar.ruby.core.Ruby;

public class RubyFileTest {
	protected Ruby ruby;
	
	@Before
	public void setUp()
	{
		ruby = new Ruby();
	}
	
	@After 
	public void tearDown()
	{
		
	}
	
	@Test
	public void test() 
	{	
		assertEquals("ruby", ruby.getKey());
		assertEquals("Ruby", ruby.getName());
		
		String[] expected = new String[] {".rb"};
		assertArrayEquals(expected, ruby.getFileSuffixes());
	}

}
