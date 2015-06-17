package wraithaven.conquest.client.GameWorld;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.BuildingCreator.Loop;

public class InputHandler{
	private Sphere cameraSphere = new Sphere();
	public float moveSpeed = 8;
	private boolean w, a, s, d;
	public InputHandler(){
		cameraSphere.r = 0.3f;
	}
	public void onKey(long window, int key, int action){
		if(key==GLFW.GLFW_KEY_F12&&action==GLFW.GLFW_RELEASE)GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE);
		if(key==GLFW.GLFW_KEY_W){
			if(action==GLFW.GLFW_PRESS)w = true;
			else if(action==GLFW.GLFW_RELEASE)w = false;
			return;
		}
		if(key==GLFW.GLFW_KEY_A){
			if(action==GLFW.GLFW_PRESS)a = true;
			else if(action==GLFW.GLFW_RELEASE)a = false;
			return;
		}
		if(key==GLFW.GLFW_KEY_S){
			if(action==GLFW.GLFW_PRESS)s = true;
			else if(action==GLFW.GLFW_RELEASE)s = false;
			return;
		}
		if(key==GLFW.GLFW_KEY_D){
			if(action==GLFW.GLFW_PRESS)d = true;
			else if(action==GLFW.GLFW_RELEASE)d = false;
			return;
		}
	}
	public void processWalk(double delta){
		delta *= moveSpeed;
		if(w){
			Loop.INSTANCE.getCamera().goalX += delta*(float)Math.sin(Math.toRadians(Loop.INSTANCE.getCamera().ry));
			Loop.INSTANCE.getCamera().goalZ -= delta*(float)Math.cos(Math.toRadians(Loop.INSTANCE.getCamera().ry));
		}
		if(a){
			Loop.INSTANCE.getCamera().goalX += delta*(float)Math.sin(Math.toRadians(Loop.INSTANCE.getCamera().ry-90));
			Loop.INSTANCE.getCamera().goalZ -= delta*(float)Math.cos(Math.toRadians(Loop.INSTANCE.getCamera().ry-90));
		}
		if(s){
			Loop.INSTANCE.getCamera().goalX -= delta*(float)Math.sin(Math.toRadians(Loop.INSTANCE.getCamera().ry));
			Loop.INSTANCE.getCamera().goalZ += delta*(float)Math.cos(Math.toRadians(Loop.INSTANCE.getCamera().ry));
		}
		if(d){
			Loop.INSTANCE.getCamera().goalX += delta*(float)Math.sin(Math.toRadians(Loop.INSTANCE.getCamera().ry+90));
			Loop.INSTANCE.getCamera().goalZ -= delta*(float)Math.cos(Math.toRadians(Loop.INSTANCE.getCamera().ry+90));
		}
	}
}