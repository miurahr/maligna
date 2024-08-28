package net.loomchild.maligna.ui.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.loomchild.maligna.ui.console.command.Command;
import net.loomchild.maligna.ui.console.command.CommandFactory;
import net.loomchild.maligna.util.Version;


public class Maligna {
	
	private static final Logger log = Logger.getLogger(Maligna.class.getName());
	
	public static final String MAIN_COMMAND_NAME = "maligna";
	

	public static void main(String[] args) {
		Maligna maligna = new Maligna();
		maligna.run(args);
	}
	
	public static void printSignature() {
		String signature = "mALIGNa";
		if (Version.getInstance().getVersion() != null) {
			signature += " " + Version.getInstance().getVersion();
		}
		if (Version.getInstance().getDate() != null) {
			signature += ", " + Version.getInstance().getDate();
		}
		signature += ".";
		System.out.println(signature);
	}
	
	private void run(String[] args) {
		try {
			if (args.length == 0) {
				printUsage();
			} else {
				String commandName = args[0];
				if (commandName.equals("-h") || commandName.equals("--help")) {
					printHelp();
				} else {
					Command command = 
						CommandFactory.getInstance().getCommand(commandName);
					if (command == null) {
						printUsage();
					} else {
						String[] commandArgs = 
							Arrays.copyOfRange(args, 1, args.length);
						command.run(commandArgs);
					}
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unknown exception.", e);
		}
	}
	
	private void printUsage() {
		System.out.println("Unknown command. Use maligna -h for help.");
	}
	
	private void printHelp() {
		printSignature();
		System.out.println("Syntax: ");
		System.out.println("    maligna <command> [command options...]");
		System.out.println("Available commands are: ");
		List<String> commandNameList = 
			new ArrayList<String>(CommandFactory.getInstance().getCommandNameSet());
		Collections.sort(commandNameList);
		System.out.println("    " + Arrays.toString(commandNameList.toArray()));
		System.out.println("To get help on specific command options use maligna <command> -h.");
	}

}
