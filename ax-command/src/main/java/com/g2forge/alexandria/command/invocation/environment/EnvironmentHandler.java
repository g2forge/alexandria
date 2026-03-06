package com.g2forge.alexandria.command.invocation.environment;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation should be used to mark methods which add extended functionality on top of {@link IEnvironment}. These are the methods that may need to be
 * modified if a new implementation of {@link IEnvironment} is ever creted.
 */
@Documented
@Retention(SOURCE)
@Target(METHOD)
public @interface EnvironmentHandler {}
