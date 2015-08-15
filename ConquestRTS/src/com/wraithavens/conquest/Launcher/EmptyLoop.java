package com.wraithavens.conquest.Launcher;

import java.text.NumberFormat;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL32;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;

abstract class EmptyLoop implements LoopObjective{
	private static void setupOGL(){
		GlError.out("Setting up OpenGL parameters.");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL12.GL_TEXTURE_3D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST);
		GL11.glEnable(GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0.0f);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f);
		GlError.dumpError();
	}
	protected final MainLoop mainLoop;
	protected final WindowInitalizer init;
	private double lastFpsDumpTime;
	private int frames;
	EmptyLoop(WindowInitalizer init){
		init.loopObjective = this;
		this.init = init;
		mainLoop = new MainLoop();
		mainLoop.create(init);
	}
	@Override
	public void key(long window, int key, int action){}
	@Override
	public void mouse(long window, int button, int action){}
	@Override
	public void mouseMove(long window, double xpos, double ypos){}
	@Override
	public void mouseWheel(long window, double xPos, double yPos){}
	@Override
	public void preLoop(){
		EmptyLoop.setupOGL();
	}
	@Override
	public void update(double delta, double time){
		frames++;
		double timePassed = time-lastFpsDumpTime;
		if(timePassed>=1){
			String renderTime =
				"Render MS: "+NumberFormat.getInstance().format(timePassed/frames*1000.0)+"  (~"
					+Math.round(frames/timePassed)+" Fps)";
			GlError.out(renderTime);
			GLFW.glfwSetWindowTitle(WraithavensConquest.INSTANCE.getWindow(), "Talantra ["+renderTime+"]");
			lastFpsDumpTime = time;
			frames = 0;
		}
	}
}
