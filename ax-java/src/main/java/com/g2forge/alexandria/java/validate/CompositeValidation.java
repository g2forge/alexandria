package com.g2forge.alexandria.java.validate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class CompositeValidation implements IValidation {
	public static IValidation create(IValidation... validations) {
		return create(HCollection.asList(validations));
	}

	public static IValidation create(List<IValidation> validations) {
		final List<IValidation> list = validations.stream().filter(Objects::nonNull).filter(v -> !(v instanceof ValidValidation)).collect(Collectors.toList());
		if (list.size() == 1) return HCollection.getOne(list);
		return new CompositeValidation(list);

	}

	@Singular
	protected final List<IValidation> validations;

	@Getter(lazy = true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private final boolean valid = getValidations().stream().map(IValidation::isValid).reduce(true, Boolean::logicalAnd);

	public CompositeValidation(IValidation... validations) {
		this(HCollection.asList(validations));
	}
}
