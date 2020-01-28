package com.g2forge.alexandria.wizard;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.Console;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserStringInput extends AInput<String> {
	protected static final Console console = System.console();

	public static String[] prompt(String title, String label, String[] prompt, boolean[] echo) {
		final Container panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		final GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		constraints.weightx = 1.0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridx = 0;
		if (label != null) {
			panel.add(new JLabel(label), constraints);
			constraints.gridy++;
		}

		constraints.gridwidth = GridBagConstraints.RELATIVE;

		final JTextField[] texts = new JTextField[prompt.length];
		for (int i = 0; i < prompt.length; i++) {
			constraints.fill = GridBagConstraints.NONE;
			constraints.gridx = 0;
			constraints.weightx = 1;
			panel.add(new JLabel(prompt[i] + ": "), constraints);

			constraints.gridx = 1;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weighty = 1;
			texts[i] = echo[i] ? new JTextField(20) : new JPasswordField(20);
			panel.add(texts[i], constraints);
			constraints.gridy++;
		}

		texts[0].addHierarchyListener(new HierarchyListener() {
			protected final HierarchyListener hierarchyListener = this;

			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				final JRootPane rootPane = SwingUtilities.getRootPane(texts[0]);
				if (rootPane == null) return;
				final JButton okButton = rootPane.getDefaultButton();
				if (okButton == null) return;
				okButton.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						if (!e.isTemporary()) {
							texts[0].requestFocusInWindow();
							texts[0].removeHierarchyListener(hierarchyListener);
							okButton.removeFocusListener(this);
						}
					}
				});
			}
		});

		if (JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
			final String[] response = new String[prompt.length];
			for (int i = 0; i < prompt.length; i++) {
				response[i] = texts[i].getText();
			}
			return response;
		} else {
			return null;
		}
	}

	@Getter
	protected final String prompt;

	@Getter
	protected final boolean emptyToNull;

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final String value = computeValue();

	protected String computeValue() {
		final String input;
		if (console != null) input = console.readLine("%s: ", getPrompt());
		else {
			final String[] retVal = UserStringInput.prompt(getPrompt(), null, new String[] { getPrompt() }, new boolean[] { true });
			if (retVal == null) throw new InputUnspecifiedException();
			input = retVal[0];
		}
		if ((input.trim().length() <= 0) && isEmptyToNull()) return null;
		return input;
	}

	@Override
	public String get() {
		return getValue();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
