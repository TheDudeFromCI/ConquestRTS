package com.wraithavens.conquest.Utility.Debug.Commands;

import java.util.ArrayList;
import com.wraithavens.conquest.Utility.Debug.ChatColor;
import com.wraithavens.conquest.Utility.Debug.ColorConsole;
import com.wraithavens.conquest.Utility.Debug.Command;

public class HelpCommand implements Command{
	private final ColorConsole console;
	private final ArrayList<Command> commandList;
	public HelpCommand(ColorConsole console, ArrayList<Command> commandList){
		this.console = console;
		this.commandList = commandList;
	}
	public String getCommandName(){
		return "help";
	}
	public String getDescription(){
		return "Lists all commands, their usage, and their description.";
	}
	public String getUsage(){
		return "help";
	}
	public void parse(String[] args){
		if(args.length!=1){
			console.println(ChatColor.RED+"Error! Unknown number of arguments.");
			return;
		}
		console.println(ChatColor.GREEN+"~~~ Listing Commands ~~~");
		for(Command c : commandList){
			console.println(ChatColor.LIGHT_GRAY+c.getCommandName());
			console.println(ChatColor.GRAY+"  Usage: "+c.getUsage());
			console.println(ChatColor.GRAY+"  "+c.getDescription());
		}
		console.println(ChatColor.GREEN+"~~~~~~~~~~~~~~~~~~~~~");
	}
}
