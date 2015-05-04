package wraithaven.conquest.client.GameWorld.LoopControls;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import java.nio.ByteBuffer;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MainLoop{
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWScrollCallback scrollCallback;
	private long window;
	private WindowInitalizer windowInitalizer;
	private WindowInitalizer recreateInitalizer;
	public void create(WindowInitalizer windowInitalizer){
		runLoop(windowInitalizer);
		while(recreateInitalizer!=null)runLoop(recreateInitalizer);
	}
	public void recreate(WindowInitalizer windowInitalizer){
		recreateInitalizer=windowInitalizer;
		dispose();
	}
	private void runLoop(WindowInitalizer windowInitalizer){
		this.windowInitalizer=windowInitalizer;
		recreateInitalizer=null;
		try{
			init();
			loop();
			glfwDestroyWindow(window);
			keyCallback.release();
			mouseButtonCallback.release();
			cursorPosCallback.release();
			scrollCallback.release();
		}catch(Exception exception){
			exception.printStackTrace();
		}finally{
			glfwTerminate();
			errorCallback.release();
			System.exit(0);
		}
	}
	private void init(){
		glfwSetErrorCallback(errorCallback=errorCallbackPrint(System.err));
		if(glfwInit()!=GL11.GL_TRUE)throw new IllegalStateException("Unable to initialize GLFW");
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, windowInitalizer.resizeable?GL_TRUE:GL_FALSE);
		window=glfwCreateWindow(windowInitalizer.width, windowInitalizer.height, windowInitalizer.windowName, windowInitalizer.fullscreen?glfwGetPrimaryMonitor():NULL, NULL);
		if(window==NULL)throw new RuntimeException("Failed to create the GLFW window");
		glfwSetKeyCallback(window, keyCallback=new GLFWKeyCallback(){
			public void invoke(long window, int key, int scancode, int action, int mods){ windowInitalizer.loopObjective.key(window, key, action); }
		});
		glfwSetMouseButtonCallback(window, mouseButtonCallback=new GLFWMouseButtonCallback(){
			public void invoke(long window, int button, int action, int mods){ windowInitalizer.loopObjective.mouse(window, button, action); }
		});
		glfwSetCursorPosCallback(window, cursorPosCallback=new GLFWCursorPosCallback(){
			public void invoke(long window, double xpos, double ypos){ windowInitalizer.loopObjective.mouseMove(window, xpos, ypos); }
		});
		glfwSetScrollCallback(window, scrollCallback=new GLFWScrollCallback(){
			public void invoke(long window, double xoffset, double yoffset){ windowInitalizer.loopObjective.mouseWheel(window, xoffset, yoffset); }
		});
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		if(!windowInitalizer.fullscreen)glfwSetWindowPos(window, (GLFWvidmode.width(vidmode)-windowInitalizer.width)/2, (GLFWvidmode.height(vidmode)-windowInitalizer.height)/2);
		glfwMakeContextCurrent(window);
		glfwSwapInterval(windowInitalizer.vSync?1:0);
		glfwShowWindow(window);
	}
	private void loop(){
		GLContext.createFromCurrent();
		glEnable(GL_DEPTH_TEST);
		glClearColor(windowInitalizer.clearRed, windowInitalizer.clearGreen, windowInitalizer.clearBlue, 0.0f);
		double lastTime = 0;
		double currentTime;
		double delta;
		windowInitalizer.loopObjective.preLoop();
		glEnable(GL_DEPTH_TEST);
		while(glfwWindowShouldClose(window)==GL_FALSE){
			glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
			currentTime=glfwGetTime();
			delta=currentTime-lastTime;
			lastTime=currentTime;
			glPushMatrix();
			windowInitalizer.loopObjective.update(delta, currentTime);
			windowInitalizer.loopObjective.render();
			glPopMatrix();
			glfwSwapBuffers(window);
			glfwPollEvents();
			sync(60);
		}
	}
	private long variableYieldTime, lastTime;
	private void sync(int fps){
		if(fps<=0)return;
		long sleepTime = 1000000000/fps;
		long yieldTime = Math.min(sleepTime, variableYieldTime+sleepTime%(1000*1000));
		long overSleep = 0;
		try{
			long t;
			while(true){
				t=System.nanoTime()-lastTime;
				if(t<sleepTime-yieldTime)Thread.sleep(1);
				else if(t<sleepTime)Thread.yield();
				else{
					overSleep=t-sleepTime;
					break;
				}
			}
		}catch(InterruptedException e){ e.printStackTrace();
		}finally{
			lastTime=System.nanoTime()-Math.min(overSleep, sleepTime);
			if(overSleep>variableYieldTime)variableYieldTime=Math.min(variableYieldTime+200*1000, sleepTime);
			else if(overSleep<variableYieldTime-200*1000)variableYieldTime=Math.max(variableYieldTime-2*1000, 0);
		}
	}
	public void dispose(){ glfwSetWindowShouldClose(window, GL_TRUE); }
	public long getWindow(){ return window; }
}