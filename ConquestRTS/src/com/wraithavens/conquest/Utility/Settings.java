package com.wraithavens.conquest.Utility;

import java.io.File;
import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.SecondaryLoop;

public class Settings{
	/**
	 * This is the number of chunks that the player can see out, not counting
	 * the chunk they are currently in. This should be atleast 2 for the game to
	 * even be playable.
	 */
	private int chunkRenderDistance;
	/**
	 * This is the radius of the chunks around the player which will be kept in
	 * memory after being loaded. Higher values will use more memory, but chunk
	 * loading while moving around the same area will be much quicker. This
	 * value must be atleast as high as chunk render distance.
	 */
	private int chunkCatcheDistance;
	/**
	 * This is the radius of the range that chunks will generate around the
	 * player, not counting the chunk the player is in. This <i>must</i> be
	 * greater than, or equal to the render distance.
	 */
	private int chunkLoadDistance;
	/**
	 * This is the priority for how much the chunk generator sleeps. <br>
	 * 0 - Minimal Sleeping<br>
	 * 1 - Normal Sleeping<br>
	 * 2 - Maximal Sleeping
	 */
	private int generatorSleeping;
	/**
	 * This is the number of frames between chunk updates. (Specifically
	 * loading/unloading.) This must be atleast 1.
	 */
	private int chunkUpdateFrames;
	/**
	 * This is the max Fps. Set to 0 for no cap.
	 */
	private int fpsCap;
	/**
	 * This is the index of the size of the game viewport resolution (And window
	 * size, if not full screen.), as determined by the order determined in the
	 * ResolutionSize class.
	 */
	private int screenResolution;
	/**
	 * This enables/disables vSync.
	 */
	private boolean vSync;
	/**
	 * This enables/disables fullscreen mode.
	 */
	private boolean fullScreen;
	/**
	 * This sets the maximum amount of particles which can be in the game at
	 * once.
	 */
	private int particleCount;
	/**
	 * This sets whether or not the skybox should be rendered.
	 */
	private boolean renderSky;
	public Settings(){
		File file = new File(WraithavensConquest.saveFolder, "Settings.dat");
		if(file.exists()){
			BinaryFile bin = new BinaryFile(file);
			bin.decompress(false);
			chunkRenderDistance = bin.getInt();
			chunkCatcheDistance = bin.getInt();
			chunkLoadDistance = bin.getInt();
			generatorSleeping = bin.getInt();
			chunkUpdateFrames = bin.getInt();
			fpsCap = bin.getInt();
			screenResolution = bin.getInt();
			vSync = bin.getBoolean();
			fullScreen = bin.getBoolean();
			particleCount = bin.getInt();
			renderSky = bin.getBoolean();
			System.out.println("Settings loaded.");
		}else{
			chunkRenderDistance = 4;
			chunkCatcheDistance = 5;
			chunkLoadDistance = 5;
			generatorSleeping = 1;
			chunkUpdateFrames = 5;
			fpsCap = 30;
			screenResolution = 0;
			vSync = true;
			fullScreen = false;
			particleCount = 5000;
			renderSky = true;
			System.out.println("Settings defaulted.");
		}
	}
	public int getChunkCatcheDistance(){
		return chunkCatcheDistance;
	}
	public int getChunkLoadDistance(){
		return chunkLoadDistance;
	}
	public int getChunkRenderDistance(){
		return chunkRenderDistance;
	}
	public int getChunkUpdateFrames(){
		return chunkUpdateFrames;
	}
	public int getFpsCap(){
		return fpsCap;
	}
	public int getGeneratorSleeping(){
		return generatorSleeping;
	}
	public int getParticleCount(){
		return particleCount;
	}
	public int getScreenResolution(){
		return screenResolution;
	}
	public boolean isFullScreen(){
		return fullScreen;
	}
	public boolean isRenderSky(){
		return renderSky;
	}
	public boolean isvSync(){
		return vSync;
	}
	public SettingsChangeRequest requestChange(){
		return new SettingsChangeRequest(this);
	}
	private void save(){
		BinaryFile bin = new BinaryFile(8*4+3);
		bin.addInt(chunkRenderDistance);
		bin.addInt(chunkCatcheDistance);
		bin.addInt(chunkLoadDistance);
		bin.addInt(generatorSleeping);
		bin.addInt(chunkUpdateFrames);
		bin.addInt(fpsCap);
		bin.addInt(screenResolution);
		bin.addBoolean(vSync);
		bin.addBoolean(fullScreen);
		bin.addInt(particleCount);
		bin.addBoolean(renderSky);
		bin.compress(false);
		bin.compile(new File(WraithavensConquest.saveFolder, "Settings.dat"));
		System.out.println("Settings saved.");
	}
	void submitChange(SettingsChangeRequest request){
		boolean changed = false;
		if(request.getChunkRenderDistance()!=chunkRenderDistance){
			chunkRenderDistance = request.getChunkRenderDistance();
			if(SinglePlayerGame.INSTANCE!=null)
				SinglePlayerGame.INSTANCE.getLandscape().setRenderDistance(getChunkRenderDistance());
			changed = true;
		}
		if(request.getChunkCatcheDistance()!=chunkCatcheDistance){
			chunkCatcheDistance = request.getChunkCatcheDistance();
			changed = true;
		}
		if(request.getChunkLoadDistance()!=chunkLoadDistance){
			chunkLoadDistance = request.getChunkLoadDistance();
			if(SinglePlayerGame.INSTANCE!=null){
				SecondaryLoop loop = SinglePlayerGame.INSTANCE.getLandscape().getLoadingLoop();
				loop.setMaxLoadDistance(chunkLoadDistance);
			}
			changed = true;
		}
		if(request.getGeneratorSleeping()!=generatorSleeping){
			// TODO Make generator sleeping adjustable.
			generatorSleeping = request.getGeneratorSleeping();
			changed = true;
		}
		if(request.getChunkUpdateFrames()!=chunkUpdateFrames){
			chunkUpdateFrames = request.getChunkUpdateFrames();
			changed = true;
		}
		if(request.getFpsCap()!=fpsCap){
			System.out.println("Merp?");
			fpsCap = request.getFpsCap();
			MainLoop.FPS_SYNC = fpsCap;
			changed = true;
		}
		if(request.getScreenResolution()!=screenResolution){
			// TODO Make screen resolution adjustable.
			screenResolution = request.getScreenResolution();
			changed = true;
		}
		if(request.isvSync()!=vSync){
			// TODO Make vSync adjustable.
			vSync = request.isvSync();
			changed = true;
		}
		if(request.isFullScreen()!=fullScreen){
			// TODO Make full screen adjustable.
			fullScreen = request.isFullScreen();
			changed = true;
		}
		if(request.getParticleCount()!=particleCount){
			particleCount = request.getParticleCount();
			if(SinglePlayerGame.INSTANCE!=null)
				SinglePlayerGame.INSTANCE.getParticleBatch().setMaxParticles(particleCount);
			changed = true;
		}
		if(request.isRenderSky()!=renderSky){
			renderSky = request.isRenderSky();
			changed = true;
		}
		if(changed)
			save();
	}
}
