package jterm.command;

import jterm.io.IOHeadlessInit;

import java.util.List;

public class ResetBinds {
	@Command(name = "reset_binds", syntax = "reset_binds [-h]")
	public static void execute(List<String> options) {
		if (options.get(0).equals("-h")) {
			System.out.println("Asks user to press special keys again, in case the keys were poorly configured when \n"
					+ "when the program was first used");
		} else
			IOHeadlessInit.setupVals();
	}
}
