package com.g2forge.alexandria.regex;

import com.g2forge.alexandria.java.function.builder.IBuilder;

public interface IGroupBuilder<Arguments, Result, Pattern extends IPattern<?>, Built> extends IPartialPatternBuilder<Arguments, Result, Pattern, Built, IGroupBuilder<Arguments, Result, Pattern, Built>>, IBuilder<Built> {}
