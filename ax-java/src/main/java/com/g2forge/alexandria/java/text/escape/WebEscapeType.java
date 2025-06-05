package com.g2forge.alexandria.java.text.escape;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.text.TextUpdate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebEscapeType implements IEscapeType {
	XML(SequenceEscaper.builder().prefix("&").postfix(";").escape("\"", "quot", true).escape("&", "amp", true).escape("'", "apos", true).escape("<", "lt", true).escape(">", "gt", true).build()),
	URL(new IEscaper() {
		@Override
		public void computeEscape(String string, IConsumer1<? super TextUpdate<?>> consumer) {
			final String encoded;
			try {
				encoded = URLEncoder.encode(string, StandardCharsets.UTF_8.displayName());
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			consumer.accept(new TextUpdate<Object>(0, string.length(), encoded.equals(string) ? IFunction1.identity() : x -> encoded));
		}

		@Override
		public void computeUnescape(String string, IConsumer1<? super TextUpdate<?>> consumer) {
			final String decoded;
			try {
				decoded = URLDecoder.decode(string, StandardCharsets.UTF_8.displayName());
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			consumer.accept(new TextUpdate<Object>(0, string.length(), decoded.equals(string) ? IFunction1.identity() : x -> decoded));
		}

		@Override
		public Pattern getRequiresEscapePattern() {
			throw new UnsupportedOperationException();
		}
	}),
	JSON(new SingleCharacterEscaper("\\", null, "\"\\\b\n\r\f\t/", "\"\\bnrft/", 2));

	protected final IEscaper escaper;
}
