package com.g2forge.alexandria.java.text.escape;

import org.junit.Test;

import lombok.AccessLevel;
import lombok.Getter;

public class TestWebXMLEscapeType extends ATestEscapeType {
	@Getter(AccessLevel.PROTECTED)
	protected final IEscaper escaper = WebEscapeType.XML.getEscaper();

	@Test
	public void simple() {
		test("&lt;xml &quot;attribute&quot;=&quot;&amp;&quot;/&gt;", "<xml \"attribute\"=\"&\"/>");
	}
}
