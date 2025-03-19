package com.g2forge.alexandria.command;

import com.g2forge.alexandria.java.core.properties.IKnownPropertyBoolean;

public enum AXCommandFlag implements IKnownPropertyBoolean {
	FORCE_CMD_PASSTHROUGH {
		@Override
		public Boolean getDefault() {
			return true;
		}
	};
}
