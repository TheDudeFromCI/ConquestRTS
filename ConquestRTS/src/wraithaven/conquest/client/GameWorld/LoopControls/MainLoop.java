package wraithaven.conquest.client.GameWorld.LoopControls;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.system.MemoryUtil.NULL;
import java.nio.ByteBuffer;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class MainLoop{
	public static boolean FPS_SYNC = true;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private WindowInitalizer recreateInitalizer;
	private GLFWScrollCallback scrollCallback;
	private long variableYieldTime,
			lastTime;
	private long window;
	private WindowInitalizer windowInitalizer;
	public void create(WindowInitalizer windowInitalizer){
		runLoop(windowInitalizer);
		while(recreateInitalizer!=null)
			runLoop(recreateInitalizer);
	}
	public void dispose(){
		glfwSetWindowShouldClose(window, GL_TRUE);
	}
	public long getWindow(){
		return window;
	}
	private void init(){
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		if(glfwInit()!=GL11.GL_TRUE) throw new IllegalStateException("Unable to initialize GLFW");
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, windowInitalizer.resizeable?GL_TRUE:GL_FALSE);
		window = glfwCreateWindow(windowInitalizer.width, windowInitalizer.height, windowInitalizer.windowName, windowInitalizer.fullscreen?glfwGetPrimaryMonitor():NULL, NULL);
		if(window==NULL) throw new RuntimeException("Failed to create the GLFW window");
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback(){
			@Override public void invoke(long window, int key, int scancode, int action, int mods){
				windowInitalizer.loopObjective.key(window, key, action);
			}
		});
		glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback(){
			@Override public void invoke(long window, int button, int action, int mods){
				windowInitalizer.loopObjective.mouse(window, button, action);
			}
		});
		glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback(){
			@Override public void invoke(long window, double xpos, double ypos){
				windowInitalizer.loopObjective.mouseMove(window, xpos, ypos);
			}
		});
		glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback(){
			@Override public void invoke(long window, double xoffset, double yoffset){
				windowInitalizer.loopObjective.mouseWheel(window, xoffset, yoffset);
			}
		});
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		if(!windowInitalizer.fullscreen) glfwSetWindowPos(window, (GLFWvidmode.width(vidmode)-windowInitalizer.width)/2, (GLFWvidmode.height(vidmode)-windowInitalizer.height)/2);
		glfwMakeContextCurrent(window);
		glfwSwapInterval(windowInitalizer.vSync?1:0);
		glfwShowWindow(window);
	}
	private void loop(){
		GLContext.createFromCurrent();
		glClearColor(windowInitalizer.clearRed, windowInitalizer.clearGreen, windowInitalizer.clearBlue, 0.0f);
		double lastTime = 0;
		double currentTime;
		double delta;
		windowInitalizer.loopObjective.preLoop();
		glEnable(GL_DEPTH_TEST);
		while(glfwWindowShouldClose(window)==GL_FALSE){
			glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
			currentTime = glfwGetTime();
			delta = currentTime-lastTime;
			lastTime = currentTime;
			glPushMatrix();
			windowInitalizer.loopObjective.update(delta, currentTime);
			windowInitalizer.loopObjective.render();
			glPopMatrix();
			glfwSwapBuffers(window);
			glfwPollEvents();
			if(FPS_SYNC) sync(60);
		}
	}
	public void recreate(WindowInitalizer windowInitalizer){
		recreateInitalizer = windowInitalizer;
		dispose();
	}
	private void runLoop(WindowInitalizer windowInitalizer){
		this.windowInitalizer = windowInitalizer;
		recreateInitalizer = null;
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
			Texture.disposeAll();
			glfwTerminate();
			errorCallback.release();
			System.exit(0);
		}
	}
	private void sync(int fps){
		if(fps<=0) return;
		long sleepTime = 1000000000/fps;
		long yieldTime = Math.min(sleepTime, variableYieldTime+sleepTime%(1000*1000));
		long overSleep = 0;
		try{
			long t;
			while(true){
				t = System.nanoTime()-lastTime;
				if(t<sleepTime-yieldTime) Thread.sleep(1);
				else if(t<sleepTime) Thread.yield();
				else{
					overSleep = t-sleepTime;
					break;
				}
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			lastTime = System.nanoTime()-Math.min(overSleep, sleepTime);
			if(overSleep>variableYieldTime) variableYieldTime = Math.min(variableYieldTime+200*1000, sleepTime);
			else if(overSleep<variableYieldTime-200*1000) variableYieldTime = Math.max(variableYieldTime-2*1000, 0);
		}
	}
}