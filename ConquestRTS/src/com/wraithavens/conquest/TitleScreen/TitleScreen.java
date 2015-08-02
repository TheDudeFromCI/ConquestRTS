package com.wraithavens.conquest.TitleScreen;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.Driver;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public class TitleScreen implements Driver{
	public void dispose(){
		Texture.disposeAll();
	}
	public void initalize(double time){
		// World world = new World();
		// WraithavensConquest.INSTANCE.setDriver(new SinglePlayerGame(world));
		WraithavensConquest.INSTANCE.setDriver(new SinglePlayerGame());
	}
	public void onKey(int key, int action){}
	public void onMouse(int button, int action){}
	public void onMouseMove(double x, double y){}
	public void onMouseWheel(double x, double y){}
	public void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	public void update(double delta, double time){}
}
