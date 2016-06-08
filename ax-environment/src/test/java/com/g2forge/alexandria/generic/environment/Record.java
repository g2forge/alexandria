package com.g2forge.alexandria.generic.environment;

import com.g2forge.alexandria.generic.environment.implementations.EmptyEnvironment;

public class Record<E extends IEnvironment> implements IEnvironmental<E> {
	protected IEnvReference<String, ? super E> field0;
	
	protected IEnvReference<Integer, ? super E> field1;
	
	/**
	 * @param field0
	 * @param field1
	 */
	public Record(final IEnvReference<String, ? super E> field0, final IEnvReference<Integer, ? super E> field1) {
		this.field0 = field0;
		this.field1 = field1;
	}
	
	@Override
	public Record<EmptyEnvironment> bind(final E environment) {
		return new Record<>(getField0().bind(environment), getField1().bind(environment));
	}
	
	/**
	 * @return the field0
	 */
	public IEnvReference<String, ? super E> getField0() {
		return field0;
	}
	
	/**
	 * @return the field1
	 */
	public IEnvReference<Integer, ? super E> getField1() {
		return field1;
	}
	
	/**
	 * @param field0 the field0 to set
	 */
	public void setField0(final IEnvReference<String, E> field0) {
		this.field0 = field0;
	}
	
	/**
	 * @param field1 the field1 to set
	 */
	public void setField1(final IEnvReference<Integer, E> field1) {
		this.field1 = field1;
	}
}
