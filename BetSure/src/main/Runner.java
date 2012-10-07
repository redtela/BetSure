package main;

import log.LogController;
import message.Message;

public class Runner {

	public static void showHelp() {
		System.out.println("TODO - Show help!");
	}
	
	public static void performDebug() {
		LogController c = new LogController();
		c.initialise();
		c.consume(new Message("Test message"));
		c.terminate();
		System.out.println("We're still here.");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			showHelp();
			return;
		}
		switch (args[0]) {
		case "debug": performDebug(); break;
		default: showHelp();
		}

	}

}
