package com.g2forge.alexandria.metadata.v5;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.metadata.v5.load.IMetadataLoader;
import com.g2forge.alexandria.test.HAssert;

import lombok.Builder;
import lombok.Data;

public class TestMetadata {
	@Retained("Hello")
	@NotRetained("Hello")
	public static class Annotated {}

	public @interface NotRetained {
		public String value();
	}

	@Data
	@Builder(toBuilder = true)
	@MetadataLoader(RecordLoader.class)
	public static class Record {
		protected final String string;
	}

	public static class RecordLoader implements IMetadataLoader {
		@Override
		@TODO(value = "Use static type switch", link = "G2-432")
		public <T> T load(Class<T> type, IMetadata element) {
			if (!Record.class.equals(type)) throw new IllegalArgumentException();
			@SuppressWarnings("unchecked")
			final T retVal = (T) new Record("Hello");
			return retVal;
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Retained {
		public String value();
	}

	@Test(expected = IllegalArgumentException.class)
	public void notRetained() {
		IMetadata.of(Annotated.class).getMetadata(NotRetained.class);
	}

	@Test
	public void record() {
		final Record record = IMetadata.of(Annotated.class).getMetadata(Record.class);
		HAssert.assertEquals("Hello", record.getString());
	}

	@Test
	public void retained() {
		final Retained value = IMetadata.of(Annotated.class).getMetadata(Retained.class);
		HAssert.assertEquals("Hello", value.value());
	}
}
