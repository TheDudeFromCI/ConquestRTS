package com.wraithavens.conquest.Utility;


public class SettingsChangeRequest{
	private final Settings settings;
	private int chunkRenderDistance;
	private int chunkLoadDistance;
	private int generatorSleeping;
	private int chunkUpdateFrames;
	private int fpsCap;
	private int screenResolution;
	private boolean vSync;
	private boolean fullScreen;
	private int particleCount;
	private boolean renderSky;
	SettingsChangeRequest(Settings settings){
		this.settings = settings;
		chunkRenderDistance = settings.getChunkRenderDistance();
		chunkLoadDistance = settings.getChunkLoadDistance();
		generatorSleeping = settings.getGeneratorSleeping();
		chunkUpdateFrames = settings.getChunkUpdateFrames();
		fpsCap = settings.getFpsCap();
		screenResolution = settings.getScreenResolution();
		vSync = settings.isvSync();
		fullScreen = settings.isFullScreen();
		particleCount = settings.getParticleCount();
		renderSky = settings.isRenderSky();
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
	public void submit(){
		settings.submitChange(this);
	}
}
