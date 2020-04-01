package com.g2forge.alexandria.java.platform;

public class ReportArgs {
	public static void main(String[] args) {
		System.out.println(args.length);
		for (String arg : args) {
			System.out.println(arg.replace("\n", "\\n"));
		}
	}
}
