package com.g2forge.alexandria.adt.graph.v2.member;

import com.g2forge.alexandria.adt.graph.v2.IGraphGeneric;

public interface IMemberDataStrategy<V, E, Member, MemberData extends IGraphGeneric<V, E>> extends IGraphGeneric<V, E> {
	public MemberData get(Member member);

	public void put(Member member, MemberData data);

	public MemberData remove(Member member);
}
