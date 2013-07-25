package com.godaddy.sonar.ruby.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class RubyRecognizerTest {
	@Test
	public void testRubyRecognizer() {
		RubyRecognizer recognizer = new RubyRecognizer();
		assertNotNull(recognizer);
	}
	
	@Test
	public void testIsLineOfCode() {
		RubyRecognizer recognizer = new RubyRecognizer();
		boolean result = recognizer.isLineOfCode("def repeat(n)");
		assertTrue(result);
	}
}
