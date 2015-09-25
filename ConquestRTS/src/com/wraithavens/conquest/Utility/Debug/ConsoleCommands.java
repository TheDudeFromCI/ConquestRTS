package com.wraithavens.conquest.Utility.Debug;

import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.SpiralGridAlgorithm;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class ConsoleCommands implements ConsoleListener{
	private final ColorConsole console;
	public ConsoleCommands(ColorConsole console){
		this.console = console;
	}
	public void onCommandSent(String command){
		console.println(ChatColor.DARK_GRAY+">> "+command);
		String[] args = command.toLowerCase().split(" ");
		if(args[0].equals("find_biome")){
			findBiomeCommand(args);
		}else
			console.println(ChatColor.RED+"Unknown command, '"+ChatColor.GOLD+args[0]+ChatColor.RED+"'.");
	}
	private void findBiomeCommand(String[] args){
		if(args.length<2){
			console.println(ChatColor.RED+"Error! Unknown number of arguments.");
			return;
		}
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
						camera.teleport(x, machine.getGroundLevel(x, z), z);
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
	}
}
