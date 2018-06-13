package com.g2forge.alexandria.command.exit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Exit implements IExit {
	protected final int code;
}
