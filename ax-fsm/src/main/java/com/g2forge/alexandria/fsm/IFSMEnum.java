package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.fsm.value.IFSMValue;
import com.g2forge.alexandria.java.type.IGeneric;

/**
 * Implementing this interface allows an enumeration to be used for the states or events of an FSM. It has the following parent types for the given reasons:
 * 
 * <table>
 * <caption>Explanation of parent interfaces</caption>
 * <tr>
 * <th>Interface</th>
 * <th>Reason</th>
 * </tr>
 * <tr>
 * <td>{@link IGeneric}</td>
 * <td>Allows the enumeration type to be the root state (event) type of the FSM.</td>
 * </tr>
 * <tr>
 * <td>{@link IFSMType}</td>
 * <td>Allows members of the enumeration to be used in specifying transition states (events).</td>
 * </tr>
 * <tr>
 * <td>{@link IFSMValue}</td>
 * <td>Allows members of the enumeration to be used as concrete states (events) including the refinement (argument).</td>
 * </tr>
 * </table>
 * 
 * @param <T> The payload type for this enum.
 */
public interface IFSMEnum<T extends IGeneric<Void>> extends IGeneric<Void>, IFSMType<T, Void>, IFSMValue<T, Void> {
	@Override
	public default IFSMType<T, Void> getType() {
		return this;
	}

	@Override
	public default Void getValue() {
		return null;
	}
}
