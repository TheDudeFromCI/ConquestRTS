package com.wraithavens.conquest.Utility.Debug.Commands;

import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.SpiralGridAlgorithm;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.Utility.Debug.ChatColor;
import com.wraithavens.conquest.Utility.Debug.ColorConsole;
import com.wraithavens.conquest.Utility.Debug.Command;

public class TpCommand implements Command{
	private final ColorConsole console;
	public TpCommand(ColorConsole console){
		this.console = console;
	}
	public String getCommandName(){
		return "tp";
	}
	public String getDescription(){
		return "Finds the nearest location with a biome of the requested type.";
	}
	public String getUsage(){
		return "tp [biome] {-maxRange:#}";
	}
	public void parse(String[] args){
		if(args.length!=3&&args.length!=4){
			console.println(ChatColor.RED+"Error! Unknown number of arguments.");
			return;
		}
		if(args.length==2){
			int maxRangeFirstPass = Integer.MAX_VALUE;
			for(String s : args){
				if(s==args[0]||s==args[1])
					continue;
				if(s.startsWith("-maxRange")){
					try{
						maxRangeFirstPass = Integer.valueOf(s.substring(9));
					}catch(Exception exception){
						console.println(ChatColor.RED+"That's not a number! '"+s.substring(9)+"'");
						return;
					}
				}else
					console.println(ChatColor.YELLOW+"Warning! Unknown argument: '"+s+"'.");
			}
			final int maxRange = maxRangeFirstPass;
			final Biome targetBiome = Biome.getByName(args[1]);
			if(targetBiome==null){
				console.println(ChatColor.RED+"Error! Biome not found. '"+args[1]+"'.");
				return;
			}
			console.println(ChatColor.GREEN+"Begining search...");
			MainLoop.endLoopTasks.add(new Runnable(){
				public void run(){
					SinglePlayerGame game = SinglePlayerGame.INSTANCE;
					if(game==null){
						console.println(ChatColor.RED+"Error! Game not initalized!");
						return;
					}
					WorldNoiseMachine machine = game.getWorldNoiseMachine();
					SpiralGridAlgorithm grid = new SpiralGridAlgorithm();
					int searchSize = 100;
					grid.setMaxDistance(maxRange/searchSize);
					Camera camera = game.getCamera();
					int x, z;
					float[] temp = new float[3];
					while(true){
						x = grid.getX()*searchSize+(int)camera.x;
						z = grid.getY()*searchSize+(int)camera.z;
						if(machine.getBiomeAt(x, z, temp)==targetBiome){
							camera.teleport(x, machine.getGroundLevel(x, z)+6, z);
							break;
						}
						if(!grid.hasNext()){
							console.println(ChatColor.YELLOW+"Biome not found.");
							return;
						}
						grid.next();
					}
					console.println(ChatColor.GREEN+"Biome found.");
				}
			});
		}else{
			float x, y, z;
			try{
				x = Float.valueOf(args[1]);
			}catch(NumberFormatException exception){
				console.println(ChatColor.RED+"Thats not a number! '"+args[1]+"'");
				return;
			}
			try{
				y = Float.valueOf(args[2]);
			}catch(NumberFormatException exception){
				console.println(ChatColor.RED+"Thats not a number! '"+args[2]+"'");
				return;
			}
			try{
				z = Float.valueOf(args[3]);
			}catch(NumberFormatException exception){
				console.println(ChatColor.RED+"Thats not a number! '"+args[3]+"'");
				return;
			}
			MainLoop.endLoopTasks.add(new Runnable(){
				public void run(){
					SinglePlayerGame game = SinglePlayerGame.INSTANCE;
					if(game==null){
						console.println(ChatColor.RED+"Error! Game not initalized!");
						return;
					}
					game.getCamera().teleport(x, y, z);
					console.println(ChatColor.GREEN+"Teleported to ("+x+", "+y+", "+z+").");
				}
			});
		}
	}
}
