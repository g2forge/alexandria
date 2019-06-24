package com.g2forge.alexandria.annotations.note;

import java.util.function.Supplier;

class NoteRecord {
	protected interface IType {
		public String getQualifiedName();

		public String getSimpleName();
	}

	protected class ToString {
		protected final Supplier<? extends CharSequence> supplier;

		protected boolean used = false;

		public ToString(String value) {
			this(() -> value);
		}

		public ToString(Supplier<? extends CharSequence> supplier) {
			this.supplier = supplier == null ? () -> "" : supplier;
		}

		public Supplier<? extends CharSequence> getSupplier() {
			return supplier;
		}

		public boolean isUsed() {
			return used;
		}

		@Override
		public String toString() {
			used = true;
			return getSupplier().get().toString();
		}
	}

	public static boolean isNonEmpty(final String string) {
		return (string != null) && (string.length() > 0);
	}

	protected final NoteType type;

	protected final String value;

	protected final String issue;

	protected final IType ref;

	public NoteRecord(NoteType type, String value, String issue, IType ref) {
		this.type = type;
		this.value = value;
		this.issue = issue;
		this.ref = ref;
	}

	public String getIssue() {
		return issue;
	}

	public IType getRef() {
		return ref;
	}

	public NoteType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public String toString(Supplier<? extends CharSequence> path) {
		final StringBuilder builder = new StringBuilder();

		final String value = getValue(), issue = getIssue();
		final IType ref = getRef();
		final boolean hasValue = isNonEmpty(value);

		final String message;
		final boolean hasType, hasIssue, hasRef;
		{
			final boolean hasRawIssue = isNonEmpty(issue), hasRawRef = ref != null;
			if (hasValue) {
				// DO NOT SINGLE STEP THIS CODE!! (You'll mess up the used values)
				final ToString pathToString = new ToString(path);
				final ToString typeToString = new ToString(getType().message);
				final ToString issueToString = new ToString(() -> hasRawIssue ? issue : null);
				final ToString refQualifiedToString = new ToString(() -> hasRawRef ? ref.getQualifiedName().toString() : null);
				final ToString refSimpleToString = new ToString(() -> hasRawRef ? ref.getSimpleName() : null);
				message = String.format(value, pathToString, typeToString, issueToString, refQualifiedToString, refSimpleToString);
				// YOU CAN SINGLE STEP AGAIN BELOW THIS LINE

				hasType = !typeToString.isUsed();
				hasIssue = hasRawIssue && !issueToString.isUsed();
				hasRef = hasRawRef && !refQualifiedToString.isUsed() && !refSimpleToString.isUsed();
			} else {
				message = null;
				hasType = true;
				hasIssue = hasRawIssue;
				hasRef = hasRawRef;
			}
		}

		if (hasType) builder.append(getType().message);

		final boolean hasRefBefore = !hasValue && hasRef;
		if (hasIssue || hasRefBefore) {
			if (builder.length() > 0) builder.append(' ');
			builder.append('(');
			if (hasIssue) builder.append(issue);
			if (hasIssue && hasRefBefore) builder.append(", ");
			if (hasRefBefore) {
				builder.append("see ");
				builder.append(ref.getQualifiedName());
			}
			builder.append(')');
		}

		if (hasValue) {
			if (builder.length() > 0) builder.append(": ");
			builder.append(message);

			if (hasRef) {
				builder.append(" (see ");
				builder.append(ref.getQualifiedName());
				builder.append(')');
			}
		}

		return builder.toString();
	}
}
