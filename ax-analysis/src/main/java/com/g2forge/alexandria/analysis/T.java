package com.g2forge.alexandria.analysis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Type;

import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;

import lombok.Data;

@Data
class T implements Serializable {
	private static final long serialVersionUID = 8025925345765570181l;

	protected static T create(Object lambda) {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (final ObjectOutputStream out = new ObjectOutputStream(baos)) {
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

	protected static String getField(Type target, String name, Type ret, Type[] args) throws ClassNotFoundException {
		final JavaClass clazz = Repository.lookupClass(target.toString());
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(name) && method.getSignature().equals(Type.getMethodSignature(ret, args))) {
				final Code code = method.getCode();
				final Instruction[] instructions = new InstructionList(code.getCode()).getInstructions();
				if (instructions.length != 3) throw new Error("Method " + method + " does not have exactly three instruction!");
				if (!(instructions[0] instanceof ALOAD) || (((ALOAD) instructions[0]).getIndex() != 0)) throw new Error();
				if (!(instructions[instructions.length - 1] instanceof ReturnInstruction)) throw new Error();

				final ConstantPoolGen constantPoolGen = new ConstantPoolGen(method.getConstantPool());
				final GETFIELD get = ((GETFIELD) instructions[1]);
				return get.getFieldName(constantPoolGen);
			}
		}
		throw new Error();
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