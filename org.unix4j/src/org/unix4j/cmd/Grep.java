package org.unix4j.cmd;

import java.io.File;

import org.unix4j.AbstractCommand;
import org.unix4j.Input;
import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgVals;
import org.unix4j.arg.DefaultArg;
import org.unix4j.arg.DefaultArgVals;
import org.unix4j.arg.DefaultOpt;
import org.unix4j.arg.Opt;

public class Grep extends AbstractCommand<Grep.ArgName> {
	
	public static final String NAME = "grep";
	
	public static interface Option {
		Opt<ArgName> i = new DefaultOpt<ArgName>(ArgName.ignoreCase);
		Opt<ArgName> ignoreCase = new DefaultOpt<ArgName>(ArgName.ignoreCase);
		Opt<ArgName> v = new DefaultOpt<ArgName>(ArgName.invert);
		Opt<ArgName> invert = new DefaultOpt<ArgName>(ArgName.invert);
	}
	public static class Argument {
		public static final Arg<ArgName,String> expression = new DefaultArg<ArgName,String>(ArgName.expression, String.class, 1, 1);
		public static final Arg<ArgName,File> files = new DefaultArg<ArgName,File>(ArgName.files, File.class, 1, Integer.MAX_VALUE);
		public static ArgVals<ArgName, String> expression(String expression) {
			return new DefaultArgVals<Grep.ArgName, String>(Argument.expression, expression);
		}
		public static ArgVals<ArgName, File> files(File... files) {
			return new DefaultArgVals<Grep.ArgName, File>(Argument.files, files);
		}
	}

	public static enum ArgName {
		ignoreCase, invert, expression, files;
	}
	
	public Grep() {
		super(NAME, true);
	}

	@Override
	public void executeBatch() {
		final String matchString = getArgs(Argument.expression).getFirst();
		final Input input = getInput();
		while (input.hasMoreLines()) {
			final String line = input.readLine();
			boolean matches;
			if (isOptSet(Option.i) || isOptSet(Option.ignoreCase)) {
				matches = line.toLowerCase().contains(matchString.toLowerCase());
			} else {
				matches = line.contains(matchString);
			}
			if (isOptSet(Option.v) || isOptSet(Option.invert)) {
				matches = !matches;
			}
			if (matches) {
				getOutput().writeLine(line);
			}
		}
	}

}