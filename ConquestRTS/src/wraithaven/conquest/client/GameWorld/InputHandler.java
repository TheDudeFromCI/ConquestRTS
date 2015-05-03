package wraithaven.conquest.client.GameWorld;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTarget;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTargetCallback;

public class InputHandler{
	private boolean w, a, s, d;
	private Sphere cameraSphere = new Sphere();
	public float moveSpeed = 8;
	private CameraTargetCallback callback;
	private VoxelWorld world;
	private final Camera cam;
	private final CameraTarget cameraTarget;
	public InputHandler(Camera cam, VoxelWorld world){
		this.cam=cam;
		cameraSphere.r=0.3f;
		cameraTarget=new CameraTarget(cam);
		this.world=world;
	}
	public void onKey(long window, int key, int action){
		if(key==GLFW.GLFW_KEY_F12&&action==GLFW.GLFW_RELEASE)GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE);
		if(key==GLFW.GLFW_KEY_W){
			if(action==GLFW.GLFW_PRESS)w=true;
			else if(action==GLFW.GLFW_RELEASE)w=false;
		}
		if(key==GLFW.GLFW_KEY_A){
			if(action==GLFW.GLFW_PRESS)a=true;
			else if(action==GLFW.GLFW_RELEASE)a=false;
		}
		if(key==GLFW.GLFW_KEY_S){
			if(action==GLFW.GLFW_PRESS)s=true;
			else if(action==GLFW.GLFW_RELEASE)s=false;
		}
		if(key==GLFW.GLFW_KEY_D){
			if(action==GLFW.GLFW_PRESS)d=true;
			else if(action==GLFW.GLFW_RELEASE)d=false;
		}
		if(key==GLFW.GLFW_KEY_Q&&action==GLFW.GLFW_PRESS){
			callback=cameraTarget.getGoalTargetBlock(world, 500, false);
			if(callback.block!=null){
				double x1 = cam.goalX-(callback.block.x+0.5);
				double y1 = cam.goalZ-(callback.block.z+0.5);
				cam.goalX=(float)(x1*Math.cos(Math.toRadians(-22.5f))-y1*Math.sin(Math.toRadians(-22.5f))+callback.block.x+0.5);
				cam.goalZ=(float)(x1*Math.sin(Math.toRadians(-22.5f))+y1*Math.cos(Math.toRadians(-22.5f))+callback.block.z+0.5);
			}
			cam.goalRY-=22.5f;
		}
		if(key==GLFW.GLFW_KEY_E&&action==GLFW.GLFW_PRESS){
			callback=cameraTarget.getGoalTargetBlock(world, 500, false);
			if(callback.block!=null){
				double x1 = cam.goalX-(callback.block.x+0.5);
				double y1 = cam.goalZ-(callback.block.z+0.5);
				cam.goalX=(float)(x1*Math.cos(Math.toRadians(22.5f))-y1*Math.sin(Math.toRadians(22.5f))+callback.block.x+0.5);
				cam.goalZ=(float)(x1*Math.sin(Math.toRadians(22.5f))+y1*Math.cos(Math.toRadians(22.5f))+callback.block.z+0.5);
			}
			cam.goalRY+=22.5f;
		}
	}
	public void processWalk(double delta){
		delta*=moveSpeed;
		if(w){
			cam.goalX+=delta*(float)Math.sin(Math.toRadians(cam.ry));
			cam.goalZ-=delta*(float)Math.cos(Math.toRadians(cam.ry));
		}
		if(a){
			cam.goalX+=delta*(float)Math.sin(Math.toRadians(cam.ry-90));
			cam.goalZ-=delta*(float)Math.cos(Math.toRadians(cam.ry-90));
		}
		if(s){
			cam.goalX-=delta*(float)Math.sin(Math.toRadians(cam.ry));
			cam.goalZ+=delta*(float)Math.cos(Math.toRadians(cam.ry));
		}
		if(d){
			cam.goalX+=delta*(float)Math.sin(Math.toRadians(cam.ry+90));
			cam.goalZ-=delta*(float)Math.cos(Math.toRadians(cam.ry+90));
		}
	}
}