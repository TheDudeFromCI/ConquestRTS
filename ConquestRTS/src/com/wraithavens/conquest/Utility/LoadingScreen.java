package com.wraithavens.conquest.Utility;

import java.io.File;
import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.Driver;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.MatrixUtils;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public class LoadingScreen implements Driver{
	private static final float SPINNER_SPEED = 2f;
	private static final int IMAGE_FLIP_SECONDS = 8;
	private final LoadingScreenTask task;
	private final Driver returnStatus;
	private UiElement spinner;
	private UiElement background, backgroundOverlay;
	private double lastImageTime;
	private int lastPictureIndex, fileCount;
	private ShaderProgram shader;
	public LoadingScreen(LoadingScreenTask task, Driver returnStatus){
		this.task = task;
		this.returnStatus = returnStatus;
		shader = new ShaderProgram("LoadingScreen");
		shader.loadUniforms("texture", "alpha");
		shader.setUniform1I(0, 0);
	}
	public void dispose(){
		background.texture.dispose();
		backgroundOverlay.texture.dispose();
		spinner.texture.dispose();
	}
	public void initalize(double time){
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.bind();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		spinner =
			new UiElement(Texture.getTexture(new File(WraithavensConquest.assetFolder, "Loading Spinner.png")));
		int screenWidth = WraithavensConquest.INSTANCE.getScreenWidth();
		int screenHeight = WraithavensConquest.INSTANCE.getScreenHeight();
		MatrixUtils.setupImageOrtho(screenWidth, screenHeight, -1, 1);
		spinner.w = 64;
		spinner.h = 64;
		spinner.x = screenWidth-spinner.w/2f;
		spinner.y = spinner.h/2f;
		String[] list = new File(WraithavensConquest.loadingScreenImagesFolder).list();
		fileCount = 0;
		for(String s : list)
			if(s.endsWith(".png"))
				fileCount++;
		lastPictureIndex = (int)(Math.random()*fileCount);
		background =
			new UiElement(Texture.getTexture(new File(WraithavensConquest.loadingScreenImagesFolder,
				lastPictureIndex+".png")));
		background.w = screenWidth;
		background.h = screenHeight;
		background.x = screenWidth/2f;
		background.y = screenHeight/2f;
		backgroundOverlay = new UiElement(background.texture);
		backgroundOverlay.w = screenWidth;
		backgroundOverlay.h = screenHeight;
		backgroundOverlay.x = screenWidth/2f;
		backgroundOverlay.y = screenHeight/2f;
		lastImageTime = time;
	}
	public void onKey(int key, int action){}
	public void onMouse(int button, int action){}
	public void onMouseMove(double x, double y){}
	public void onMouseWheel(double x, double y){}
	public void render(){
		background.render(shader);
		backgroundOverlay.render(shader);
		spinner.render(shader);
	}
	public void update(double delta, double time){
		spinner.r = (float)(time*SPINNER_SPEED);
		if(task.runStep()){
			WraithavensConquest.INSTANCE.setDriver(returnStatus);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
		}
		if(time-lastImageTime>IMAGE_FLIP_SECONDS){
			if(background.texture!=backgroundOverlay.texture)
				background.texture.dispose();
			int randomPicture = (int)(Math.random()*fileCount);
			while(randomPicture==lastPictureIndex)
				randomPicture = (int)(Math.random()*fileCount);
			lastPictureIndex = randomPicture;
			Texture newTexture =
				Texture.getTexture(new File(WraithavensConquest.loadingScreenImagesFolder, lastPictureIndex
					+".png"));
			background.texture = backgroundOverlay.texture;
			backgroundOverlay.texture = newTexture;
			lastImageTime = time;
		}
		backgroundOverlay.a = (float)Math.min(time-lastImageTime, 1);
	}
}
