package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.fsm.generic.value.IValue1;
import com.g2forge.alexandria.java.type.IGeneric;

/**
 * Implementing this interface allows an enumeration to be used for the states or events of an FSM. It has the following parent types for the given reasons:
 * 
 * <table>
 * <tr>
 * <th>Interface</th>
 * <th>Reason</th>
 * </tr>
 * <tr>
 * <td>ITyped1</td>
 * <td>Allows the enumeration type to be the root state (event) type of the FSM.</td>
 * </tr>
 * <tr>
 * <td>IBoundType1</td>
 * <td>Allows members of the enumeration to be used in specifying transitions states (events).</td>
 * </tr>
 * <tr>
 * <td>IValue</td>
 * <td>Allows members of the enumeration to be used as concrete states (events) including the refinement (argument).</td>
 * </tr>
 * </table>
 * 
 * @author gdgib
 *
 * @param <T>
 */
public interface IFSMEnum<T extends IGeneric<Void>> extends IGeneric<Void>, IType1<T, Void>, IValue1<T, Void> {
	@Override
	public default IType1<T, Void> getType() {
		return this;
	}

	@Override
	public default Void getValue() {
		return null;
	}
}
