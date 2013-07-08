package com.godaddy.sonar.ruby.ui;

import org.sonar.api.web.Footer;

public final class RubyFooter implements Footer {

  public String getHtml() {
    return "<p>Footer Example - <em>This is static HTML</em></p>";
  }
}
