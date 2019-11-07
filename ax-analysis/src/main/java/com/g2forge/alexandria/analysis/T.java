package com.g2forge.alexandria.analysis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;

import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;

import lombok.Data;

@Data
class T implements Serializable {
	protected static class ThunkOutputStream extends ObjectOutputStream {
		protected ThunkOutputStream(OutputStream out) throws IOException {
			super(out);
			enableReplaceObject(true);
		}

		@Override
		protected Object replaceObject(Object obj) throws IOException {
			if (!(obj instanceof Serializable)) return null;
			return obj;
		}
	}

	private static final long serialVersionUID = 8025925345765570181l;

	protected static T create(Object lambda) {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (final ObjectOutputStream out = new ThunkOutputStream(baos)) {
				out.writeObject(lambda);
			}
			final byte[] rewrite = rewrite(baos.toByteArray());
			final Object retVal = new ObjectInputStream(new ByteArrayInputStream(rewrite)).readObject();
			if (retVal instanceof T) return (T) retVal;
			return null;
		} catch (Exception exception) {
			throw new RuntimeReflectionException("Failed to expose lambda", exception);
		}

	}

	protected static int find(byte[] data, String find) {
		for (int start = 0; start < (data.length - find.length()); start++) {
			int offset = 0;
			for (; offset < find.length(); offset++) {
				if (data[start + offset] != find.codePointAt(offset)) break;
			}
			if (offset == find.length()) return start;
		}
		return -1;
	}

	protected static byte[] rewrite(byte[] data) {
		final String find = SerializedLambda.class.getName();
		final int start = find(data, find);

		if (start != -1) {
			final String replace = T.class.getName();
			if (find.length() != replace.length()) throw new Error("Class name of the serializable thunk must be the same length as " + find + ".");

			for (int i = 0; i < replace.length(); i++)
				data[start + i] = (byte) replace.codePointAt(i);
		}
		return data;
	}

	protected Object[] capturedArgs;

	protected String implClass;

	protected String implMethodName;

	protected String implMethodSignature;
}