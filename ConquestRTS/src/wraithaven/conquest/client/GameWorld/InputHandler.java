package wraithaven.conquest.client.GameWorld;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;

public class InputHandler{
	private final Camera cam;
	private Sphere cameraSphere = new Sphere();
	public float moveSpeed = 8;
	private boolean w,
			a,
			s,
			d;
	public InputHandler(Camera cam){
		this.cam = cam;
		cameraSphere.r = 0.3f;
	}
	public void onKey(long window, int key, int action){
		if(key==GLFW.GLFW_KEY_F12&&action==GLFW.GLFW_RELEASE) GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE);
		if(key==GLFW.GLFW_KEY_W){
			if(action==GLFW.GLFW_PRESS) w = true;
			else if(action==GLFW.GLFW_RELEASE) w = false;
		}
		if(key==GLFW.GLFW_KEY_A){
			if(action==GLFW.GLFW_PRESS) a = true;
			else if(action==GLFW.GLFW_RELEASE) a = false;
		}
		if(key==GLFW.GLFW_KEY_S){
			if(action==GLFW.GLFW_PRESS) s = true;
			else if(action==GLFW.GLFW_RELEASE) s = false;
		}
		if(key==GLFW.GLFW_KEY_D){
			if(action==GLFW.GLFW_PRESS) d = true;
			else if(action==GLFW.GLFW_RELEASE) d = false;
		}
	}
	public void processWalk(double delta){
		delta *= moveSpeed;
		if(w){
			cam.goalX += delta*(float)Math.sin(Math.toRadians(cam.ry));
			cam.goalZ -= delta*(float)Math.cos(Math.toRadians(cam.ry));
		}
		if(a){
			cam.goalX += delta*(float)Math.sin(Math.toRadians(cam.ry-90));
			cam.goalZ -= delta*(float)Math.cos(Math.toRadians(cam.ry-90));
		}
		if(s){
			cam.goalX -= delta*(float)Math.sin(Math.toRadians(cam.ry));
			cam.goalZ += delta*(float)Math.cos(Math.toRadians(cam.ry));
		}
		if(d){
			cam.goalX += delta*(float)Math.sin(Math.toRadians(cam.ry+90));
			cam.goalZ -= delta*(float)Math.cos(Math.toRadians(cam.ry+90));
		}
	}
}