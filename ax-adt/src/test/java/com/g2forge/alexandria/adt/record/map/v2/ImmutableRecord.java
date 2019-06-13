package com.g2forge.alexandria.adt.record.map.v2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class ImmutableRecord {
	protected final String field;
}