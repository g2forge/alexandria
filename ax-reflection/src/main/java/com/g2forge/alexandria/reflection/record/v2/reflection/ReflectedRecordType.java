package com.g2forge.alexandria.reflection.record.v2.reflection;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.function.cache.FixedCachingSupplier;
import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.reflection.object.HReflection;
import com.g2forge.alexandria.reflection.object.IJavaConcreteReflection;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.object.IJavaMethodReflection;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;
import com.g2forge.alexandria.reflection.record.v2.IPropertyType;
import com.g2forge.alexandria.reflection.record.v2.IRecordType;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ReflectedRecordType implements IRecordType {
	protected static FieldPropertyType<?> field(IJavaFieldReflection<Object, ?> field) {
		return new FieldPropertyType<>(field);
	}

	protected static MethodPropertyType<?> method(IJavaMethodReflection<Object, ?> method) {
		return new MethodPropertyType<>(method);
	}

	protected static void putAll(Map<String, APropertyType<?>> properties, Collection<? extends APropertyType<?>> next) {
		for (APropertyType<?> property : next) {
			final String name = property.getName();

			final APropertyType<?> prev = properties.get(name);
			if (prev != null) property.setOverride(prev);
			properties.put(name, property);
		}
	}

	protected final IJavaConcreteReflection<?> reflection;

	@SuppressWarnings("unchecked")
	protected Supplier<Map<String, APropertyType<?>>> properties = new FixedCachingSupplier<>(() -> {
		final Map<String, APropertyType<?>> properties = new LinkedHashMap<>();
		final IJavaConcreteReflection<Object> reflection = (IJavaConcreteReflection<Object>) getReflection();
		putAll(properties, reflection.getFields(JavaScope.Inherited, null).map(ReflectedRecordType::field).collect(Collectors.toList()));
		putAll(properties, reflection.getMethods(JavaScope.Inherited, null).filter(method -> !Object.class.equals(method.getDeclaringClass().getType().getJavaType())).filter(m -> m.toAccessorMethod().getAccessorType() != null).map(ReflectedRecordType::method).collect(Collectors.toList()));
		return properties;
	});

	public ReflectedRecordType(IJavaTypeReflection<?> reflection) {
		this.reflection = reflection.erase();
	}

	public ReflectedRecordType(Type type) {
		this(HReflection.toReflection(type));
	}

	public Collection<? extends IPropertyType<?>> getProperties() {
		return properties.get().values();
	}

	protected IJavaConcreteReflection<?> getReflection() {
		return reflection;
	}
}
