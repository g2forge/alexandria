package com.g2forge.alexandria.analysis;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.ReturnInstruction;

import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;

public class AnalysisHelpers {
	public static String getPath(final SerializableFunction<?, ?> function) {
		final T thunk = T.create(function);
		final JavaClass clazz;
		try {
			clazz = Repository.lookupClass(thunk.getImplClass());
		} catch (ClassNotFoundException e) {
			throw new RuntimeReflectionException(e);
		}

		final StringBuilder retVal = new StringBuilder();
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(thunk.getImplMethodName()) && method.getSignature().equals(thunk.getImplMethodSignature())) {
				final Code code = method.getCode();
				final Instruction[] instructions = new InstructionList(code.getCode()).getInstructions();
				if (!(instructions[0] instanceof ALOAD) || (((ALOAD) instructions[0]).getIndex() != 0)) throw new Error();
				if (!(instructions[instructions.length - 1] instanceof ReturnInstruction)) throw new Error();

				final ConstantPoolGen constantPoolGen = new ConstantPoolGen(method.getConstantPool());
				for (int i = 1; i < instructions.length - 1; i++) {
					final String field;

					if (instructions[i] instanceof INVOKEVIRTUAL) {
						final InvokeInstruction invoke = (InvokeInstruction) instructions[i];
						try {
							field = T.getField(invoke.getReferenceType(constantPoolGen), invoke.getMethodName(constantPoolGen), invoke.getReturnType(constantPoolGen), invoke.getArgumentTypes(constantPoolGen));
						} catch (ClassNotFoundException e) {
							throw new RuntimeReflectionException(e);
						}
					} else if (instructions[i] instanceof GETFIELD) {
						final GETFIELD get = ((GETFIELD) instructions[i]);
						field = get.getFieldName(constantPoolGen);
					} else if (instructions[i] instanceof INVOKESTATIC) {
						final InvokeInstruction invoke = (InvokeInstruction) instructions[i];
						if (!Integer.class.getName().equals(invoke.getReferenceType(constantPoolGen).toString()) || !"valueOf".equals(invoke.getMethodName(constantPoolGen))) throw new Error();
						field = null;
					} else throw new Error();

					if (field != null) {
						if (retVal.length() > 0) retVal.append('.');
						retVal.append(field);
					}
				}
			}
		}
		return retVal.toString();
	}
}
