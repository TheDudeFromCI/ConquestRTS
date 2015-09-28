package com.wraithavens.conquest.Utility.Debug.Commands;

import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.Debug.ChatColor;
import com.wraithavens.conquest.Utility.Debug.ColorConsole;
import com.wraithavens.conquest.Utility.Debug.Command;

public class SettingsCommand implements Command{
	private final ColorConsole console;
	public SettingsCommand(ColorConsole console){
		this.console = console;
	}
	public String getCommandName(){
		return "settings";
	}
	public String getDescription(){
		return "Views or edits settings.";
	}
	public String getUsage(){
		return "settings {}";
	}
	public void parse(String[] args){
		if(args.length==1){
			console.println(ChatColor.GREEN+"~~~~~~~~~ Settings ~~~~~~~~~");
			print("Chunk Render Distance: ", WraithavensConquest.Settings.getChunkRenderDistance());
			print("Chunk Catche Distance: ", WraithavensConquest.Settings.getChunkCatcheDistance());
			print("Chunk Load Distance:   ", WraithavensConquest.Settings.getChunkLoadDistance());
			print("Generator Sleeping:    ", WraithavensConquest.Settings.getGeneratorSleeping());
			print("Chunk Update Frames:   ", WraithavensConquest.Settings.getChunkUpdateFrames());
			print("FPS Cap:               ", WraithavensConquest.Settings.getFpsCap());
			print("Screen Resolution:     ", WraithavensConquest.Settings.getScreenResolution());
			print("V Sync:                ", WraithavensConquest.Settings.isvSync());
			print("Full Screen:           ", WraithavensConquest.Settings.isFullScreen());
			print("Particle Count:        ", WraithavensConquest.Settings.getParticleCount());
			print("Render Sky:            ", WraithavensConquest.Settings.isRenderSky());
			console.println(ChatColor.GREEN+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			return;
		}
	}
	private void print(String name, Object o){
		console.println(ChatColor.GRAY+name+ChatColor.DARK_AQUA+o);
	}
}
