package com.g2forge.alexandria.adt.graph.v2.member;

import com.g2forge.alexandria.adt.graph.v2.IGraphGeneric;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class MemberDataStrategy<V, E, Member, MemberData extends IGraphGeneric<V, E>> implements IMemberDataStrategy<V, E, Member, MemberData> {
	protected final IGraphKey<V, E> key;

	protected AMember cast(Member member) {
		return (AMember) member;
	}

	@Override
	public MemberData get(Member member) {
		@SuppressWarnings("unchecked")
		final MemberData retVal = (MemberData) cast(member).getGraphData(getKey(), false);
		return retVal;
	}

	@Override
	public void put(Member member, MemberData data) {
		cast(member).putGraphData(getKey(), data);
	}

	@Override
	public MemberData remove(Member member) {
		@SuppressWarnings("unchecked")
		final MemberData retVal = (MemberData) cast(member).getGraphData(getKey(), true);
		return retVal;
	}
}
