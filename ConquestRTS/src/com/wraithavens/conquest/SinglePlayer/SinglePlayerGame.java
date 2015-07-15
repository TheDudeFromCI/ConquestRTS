package com.wraithavens.conquest.SinglePlayer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.Driver;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.MatrixUtils;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.World;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.WorldRenderer;
import com.wraithavens.conquest.SinglePlayer.Entities.ModelGroup;
import com.wraithavens.conquest.SinglePlayer.Entities.ModelInstance;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public class SinglePlayerGame implements Driver{
	private final World world;
	private boolean loaded;
	private boolean initalized;
	private final WorldRenderer renderer;
	private boolean w, a, s, d, shift, space, lockedMouse;
	private final float cameraSpeed = 20f;
	private final float mouseSpeed = 0.2f;
	private final Camera camera;
	private ModelGroup modelGroups;
	public SinglePlayerGame(World world){
		this.world = world;
		camera = new Camera();
		renderer = new WorldRenderer(world, camera);
	}
	public void initalize(double time){
		if(!loaded){
			world.initalize(this);
			loaded = true;
		}else{
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glClearColor(0.4f, 0.6f, 0.9f, 1);
			MatrixUtils.setupPerspective(70, WraithavensConquest.INSTANCE.getScreenWidth()/(float)WraithavensConquest.INSTANCE.getScreenHeight(), 1, 10000);
			camera.cameraMoveSpeed = 10;
			initalized = true;
			renderer.initalize();
			camera.goalX = 0.5f;
			camera.goalY = world.getHeightAt(0, 0)+1;
			camera.goalZ = 0.5f;
			modelGroups = new ModelGroup();
		}
	}
	public void render(){
		if(!initalized)return;
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		renderer.render();
		modelGroups.render();
		GL11.glPopMatrix();
	}
	public void update(double delta, double time){
		if(!initalized)return;
		GL11.glPushMatrix();
		move(delta);
		moveCamera(delta);
		renderer.update();
	}
	private void moveCamera(double delta){
		long x = (long)(camera.x*100);
		long y = (long)(camera.y*100);
		long z = (long)(camera.z*100);
		camera.update(delta);
		if(x!=(long)(camera.x*100)
				||y!=(long)(camera.y*100)
				||z!=(long)(camera.z*100)){
			world.setNeedsRebuffer();
		}
	}
	private void lookAtPlayer(ModelInstance mi){
		float deltaX = mi.position.x-camera.x;
		float deltaZ = mi.position.z-camera.z;
		float angle = -(float)Math.toDegrees(Math.atan2(deltaZ, deltaX))-90;
		mi.rotation.y = angle;
	}
	public void onKey(int key, int action){
		if(key==GLFW.GLFW_KEY_W){
			if(action==GLFW.GLFW_PRESS)w = true;
			else if(action==GLFW.GLFW_RELEASE)w = false;
		}else if(key==GLFW.GLFW_KEY_A){
			if(action==GLFW.GLFW_PRESS)a = true;
			else if(action==GLFW.GLFW_RELEASE)a = false;
		}else if(key==GLFW.GLFW_KEY_S){
			if(action==GLFW.GLFW_PRESS)s = true;
			else if(action==GLFW.GLFW_RELEASE)s = false;
		}else if(key==GLFW.GLFW_KEY_D){
			if(action==GLFW.GLFW_PRESS)d = true;
			else if(action==GLFW.GLFW_RELEASE)d = false;
		}else if(key==GLFW.GLFW_KEY_LEFT_SHIFT){
			if(action==GLFW.GLFW_PRESS)shift = true;
			else if(action==GLFW.GLFW_RELEASE)shift = false;
		}else if(key==GLFW.GLFW_KEY_SPACE){
			if(action==GLFW.GLFW_PRESS)space = true;
			else if(action==GLFW.GLFW_RELEASE)space = false;
		}else if(key==GLFW.GLFW_KEY_P){
			if(action==GLFW.GLFW_PRESS){
				modelGroups.instances.get(0).position.set(camera.x, world.getHeightAt((int)camera.x, (int)camera.z), camera.z);
				modelGroups.instances.get(0).rotation.set(-90, -camera.ry+180, 0);
				System.out.println("Moved NPC to: "+modelGroups.instances.get(0).position);
			}
		}else if(key==GLFW.GLFW_KEY_ESCAPE){
			if(action==GLFW.GLFW_PRESS)GLFW.glfwSetWindowShouldClose(WraithavensConquest.INSTANCE.getWindow(), GL11.GL_TRUE);
		}else if(key==GLFW.GLFW_KEY_L){
			if(action==GLFW.GLFW_PRESS){
				modelGroups.getShader().setUniformVec3(0, camera.getDirection(new Vector3f()));
			}
		}else if(key==GLFW.GLFW_KEY_M){
			if(action==GLFW.GLFW_PRESS){
				for(int i = 0; i<modelGroups.instances.size(); i++)
					lookAtPlayer(modelGroups.instances.get(i));
			}
		}
	}
	public void onMouse(int button, int action){
		if(action==GLFW.GLFW_PRESS){
			if(lockedMouse){
				GLFW.glfwSetInputMode(WraithavensConquest.INSTANCE.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
			}else{
				GLFW.glfwSetCursorPos(WraithavensConquest.INSTANCE.getWindow(), WraithavensConquest.INSTANCE.getScreenWidth()/2f, WraithavensConquest.INSTANCE.getScreenHeight()/2f);
				GLFW.glfwSetInputMode(WraithavensConquest.INSTANCE.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
			}
			lockedMouse = !lockedMouse;
		}
	}
	public void onMouseMove(double x, double y){
		if(!lockedMouse)return;
		if(x==WraithavensConquest.INSTANCE.getScreenWidth()/2f
				&&y==WraithavensConquest.INSTANCE.getScreenHeight()/2f)return;
		camera.ry = (float)(camera.ry+(x-WraithavensConquest.INSTANCE.getScreenWidth()/2f)*mouseSpeed);
		camera.rx = (float)Math.max(Math.min(camera.rx+(y-WraithavensConquest.INSTANCE.getScreenHeight()/2f)*mouseSpeed, 90), -90);
		GLFW.glfwSetCursorPos(WraithavensConquest.INSTANCE.getWindow(), WraithavensConquest.INSTANCE.getScreenWidth()/2, WraithavensConquest.INSTANCE.getScreenHeight()/2f);
		world.setNeedsRebuffer();
	}
	private void move(double delta){
		delta *= cameraSpeed;
		if(w){
			camera.goalX += delta*(float)Math.sin(Math.toRadians(camera.ry));
			camera.goalZ -= delta*(float)Math.cos(Math.toRadians(camera.ry));
		}
		if(a){
			camera.goalX += delta*(float)Math.sin(Math.toRadians(camera.ry-90));
			camera.goalZ -= delta*(float)Math.cos(Math.toRadians(camera.ry-90));
		}
		if(s){
			camera.goalX -= delta*(float)Math.sin(Math.toRadians(camera.ry));
			camera.goalZ += delta*(float)Math.cos(Math.toRadians(camera.ry));
		}
		if(d){
			camera.goalX += delta*(float)Math.sin(Math.toRadians(camera.ry+90));
			camera.goalZ -= delta*(float)Math.cos(Math.toRadians(camera.ry+90));
		}
		if(shift)camera.goalY -= delta;
		if(space)camera.goalY += delta;
	}
	public void onMouseWheel(double x, double y){}
	public void dispose(){
		if(initalized){
			System.out.println("Disposed single player driver.");
			modelGroups.dispose();
			world.dispose();
			Texture.disposeAll();
		}
	}
}