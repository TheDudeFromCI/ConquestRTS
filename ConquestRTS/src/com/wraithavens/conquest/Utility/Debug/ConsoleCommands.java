package com.wraithavens.conquest.Utility.Debug;

public class ConsoleCommands implements ConsoleListener{
	private final ColorConsole console;
	public ConsoleCommands(ColorConsole console){
		this.console = console;
	}
	public void onCommandSent(String command){}
}
