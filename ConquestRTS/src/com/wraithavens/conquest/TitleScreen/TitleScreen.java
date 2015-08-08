package com.wraithavens.conquest.TitleScreen;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.Driver;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public class TitleScreen implements Driver{
	public void dispose(){
		GlError.out("Disposing title screen.");
		Texture.disposeAll();
		GlError.dumpError();
	}
	public void initalize(double time){
		GlError.out("Initalizing title screen driver.");
		WraithavensConquest.INSTANCE.setDriver(new SinglePlayerGame());
	}
	public void onKey(int key, int action){}
	public void onMouse(int button, int action){}
	public void onMouseMove(double x, double y){}
	public void onMouseWheel(double x, double y){}
	public void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GlError.dumpError();
	}
	public void update(double delta, double time){}
}
