package com.g2forge.alexandria.java.algo.walk;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Step<Node> {
	protected final StepType type;

	protected final Node node;
}