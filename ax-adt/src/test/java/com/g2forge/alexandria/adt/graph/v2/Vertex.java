package com.g2forge.alexandria.adt.graph.v2;

import com.g2forge.alexandria.adt.graph.v2.member.AMultiGraphMember;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Vertex extends AMultiGraphMember {
	protected final String name;
}