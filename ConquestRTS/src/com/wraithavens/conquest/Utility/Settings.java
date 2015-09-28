package com.wraithavens.conquest.Utility;

import java.io.File;
import com.wraithavens.conquest.Launcher.WraithavensConquest;

public class Settings{
	/**
	 * This is the number of chunks that the player can see out, not counting
	 * the chunk they are currently in. This should be atleast 2 for the game to
	 * even be playable.
	 */
	private int chunkRenderDistance;
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
			chunkLoadDistance = bin.getInt();
			generatorSleeping = bin.getInt();
			chunkUpdateFrames = bin.getInt();
			fpsCap = bin.getInt();
			screenResolution = bin.getInt();
			vSync = bin.getBoolean();
			fullScreen = bin.getBoolean();
			particleCount = bin.getInt();
			renderSky = bin.getBoolean();
		}else{
			chunkRenderDistance = 4;
			chunkLoadDistance = 4;
			generatorSleeping = 1;
			chunkUpdateFrames = 5;
			fpsCap = 30;
			screenResolution = 0;
			vSync = true;
			fullScreen = false;
			particleCount = 5000;
			renderSky = true;
		}
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
	public void save(){
		BinaryFile bin = new BinaryFile(7*4+3);
		bin.addInt(chunkRenderDistance);
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
	}
	public void setChunkLoadDistance(int chunkLoadDistance){
		this.chunkLoadDistance = chunkLoadDistance;
	}
	public void setChunkRenderDistance(int chunkRenderDistance){
		this.chunkRenderDistance = chunkRenderDistance;
	}
	public void setChunkUpdateFrames(int chunkUpdateFrames){
		this.chunkUpdateFrames = chunkUpdateFrames;
	}
	public void setFpsCap(int fpsCap){
		this.fpsCap = fpsCap;
	}
	public void setFullScreen(boolean fullscren){
		fullScreen = fullscren;
	}
	public void setGeneratorSleeping(int generatorSleeping){
		this.generatorSleeping = generatorSleeping;
	}
	public void setParticleCount(int particleCount){
		this.particleCount = particleCount;
	}
	public void setRenderSky(boolean renderSky){
		this.renderSky = renderSky;
	}
	public void setScreenResolution(int screenResolution){
		this.screenResolution = screenResolution;
	}
	public void setvSync(boolean vSync){
		this.vSync = vSync;
	}
	void submitChange(SettingsChangeRequest request){
		// TODO
	}
}
