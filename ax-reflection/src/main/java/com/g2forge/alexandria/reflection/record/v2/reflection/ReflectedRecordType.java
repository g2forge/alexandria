package com.g2forge.alexandria.reflection.record.v2.reflection;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.java.function.CachingSupplier;
import com.g2forge.alexandria.reflection.object.IJavaConcreteReflection;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.object.ReflectionHelpers;
import com.g2forge.alexandria.reflection.record.v2.IPropertyType;
import com.g2forge.alexandria.reflection.record.v2.IRecordType;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ReflectedRecordType implements IRecordType {
	protected static void putAll(Map<String, APropertyType> properties, Map<String, APropertyType> next) {
		for (Map.Entry<String, APropertyType> entry : next.entrySet()) {
			final String name = entry.getKey();
			final APropertyType property = entry.getValue();

			final APropertyType prev = properties.get(name);
			if (prev != null) property.setOverride(prev);
			properties.put(name, property);
		}
	}

	protected final IJavaConcreteReflection<?> reflection;

	@SuppressWarnings("unchecked")
	protected Supplier<Map<String, APropertyType>> properties = new CachingSupplier<>(() -> {
		final Map<String, APropertyType> properties = new LinkedHashMap<>();
		putAll(properties, getReflection().getFields(JavaScope.Inherited, null).map(field -> new FieldPropertyType((IJavaFieldReflection<Object, Object>) field)).collect(Collectors.toMap(IPropertyType::getName, Function.identity())));
		putAll(properties, getReflection().getMethods(JavaScope.Inherited, null).filter(method -> !Object.class.equals(method.getDeclaringClass().getType().getJavaType())).filter(GetterPropertyType::isGetter).collect(Collectors.toList()).stream().map(GetterPropertyType::new).collect(Collectors.toMap(IPropertyType::getName, Function.identity())));
		return properties;
	});

	public ReflectedRecordType(Class<?> type) {
		this.reflection = ReflectionHelpers.toReflection(type);
	}

	public Collection<? extends IPropertyType> getProperties() {
		return properties.get().values();
	}

	protected IJavaConcreteReflection<?> getReflection() {
		return reflection;
	}
}
