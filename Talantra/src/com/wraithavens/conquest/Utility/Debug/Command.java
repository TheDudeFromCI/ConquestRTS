package com.wraithavens.conquest.Utility.Debug;

public interface Command{
	public String getCommandName();
	public String getDescription();
	public String getUsage();
	public void parse(String[] args);
}
