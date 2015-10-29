package com.wraithavens.conquest.Utility.Debug.Commands;

import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.SettingsChangeRequest;
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
		return "settings {[setting name] [setting value]}";
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
		if(args.length!=3){
			console.println(ChatColor.RED+"Error! Unknown number of arguments.");
			return;
		}
		Object value = null;
		try{
			value = Integer.valueOf(args[2]);
		}catch(Exception exception){
			try{
				value = Boolean.valueOf(args[2]);
			}catch(Exception exception1){
				console.println(ChatColor.RED+"Error! Unknown value type.");
				return;
			}
		}
		args[1] = args[1].toLowerCase();
		if(args[1].equals("chunkrenderdistance")){
			if(value instanceof Integer){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setChunkRenderDistance((int)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("chunkcatchedistance")){
			if(value instanceof Integer){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setChunkCatcheDistance((int)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("chunkloaddistance")){
			if(value instanceof Integer){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setChunkLoadDistance((int)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("generatorsleeping")){
			if(value instanceof Integer){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setGeneratorSleeping((int)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("chunkupdateframes")){
			if(value instanceof Integer){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setChunkUpdateFrames((int)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("fpscap")){
			if(value instanceof Integer){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setFpsCap((int)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("screenresolution")){
			if(value instanceof Integer){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setScreenResolution((int)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("vsync")){
			if(value instanceof Boolean){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setvSync((boolean)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("fullscreen")){
			if(value instanceof Boolean){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setFullScreen((boolean)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("particlecount")){
			if(value instanceof Integer){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setParticleCount((int)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else if(args[1].equals("rendersky")){
			if(value instanceof Boolean){
				SettingsChangeRequest set = WraithavensConquest.Settings.requestChange();
				set.setRenderSky((boolean)value);
				set.submit();
				console.println(ChatColor.GREEN+"Value set.");
				return;
			}
			console.println(ChatColor.RED+"Error! Value type does not match.");
			return;
		}else{
			console.println(ChatColor.RED+"Error! Unknown setting name.");
			return;
		}
	}
	private void print(String name, Object o){
		console.println(ChatColor.GRAY+name+ChatColor.DARK_AQUA+o);
	}
}
