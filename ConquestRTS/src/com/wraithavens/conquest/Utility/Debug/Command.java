package com.wraithavens.conquest.Utility.Debug;

public interface Command{
	public String getCommandName();
	public void parse(String[] args);
	public void printHelp();
}
