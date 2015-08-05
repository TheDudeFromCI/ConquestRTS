package com.wraithavens.conquest.SinglePlayer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.Driver;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector4f;
import com.wraithavens.conquest.SinglePlayer.Blocks.World;
import com.wraithavens.conquest.SinglePlayer.Heightmap.WorldHeightmaps;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.Skybox.MountainRenderer;
import com.wraithavens.conquest.SinglePlayer.Skybox.MountainSkybox;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyBox;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxBuilder;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxClouds;
import com.wraithavens.conquest.Utility.PowerInterpolation;

public class SinglePlayerGame implements Driver{
	private WorldHeightmaps heightMaps;
	private boolean w, a, s, d, shift, space, fly, lockedMouse, walkLock, e;
	private boolean wireframeMode;
	private final float cameraSpeed = 40f;
	private final float mouseSpeed = 0.2f;
	private final Camera camera = new Camera();
	private double frameDelta;
	private World world;
	private SkyBox skybox;
	public void dispose(){
		if(heightMaps!=null)
			heightMaps.dispose();
		if(world!=null)
			world.dispose();
	}
	public void initalize(double time){
		long[] seeds = new long[]{
			0, 1, 2, 3
		};
		WorldNoiseMachine machine = WorldNoiseMachine.generate(seeds);
		camera.cameraMoveSpeed = 10.0f;
		camera.goalY = camera.y = (float)machine.getWorldHeight(0, 0)+6;
		camera.goalX = 8192;
		camera.goalZ = 8192;
		world = new World(machine, camera);
		heightMaps = new WorldHeightmaps(machine);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		SkyboxClouds noise = null;
		SkyboxClouds noise2 = null;
		MountainSkybox mountains = null;
		{
			SkyboxBuilder builder = new SkyboxBuilder();
			builder.setBackdrop(true);
			builder.setSeed(0);
			builder.setSmoothness(50);
			builder.setDetail(3);
			builder.setFunction(SkyboxBuilder.Cerp);
			PowerInterpolation Perp2 = new PowerInterpolation(2);
			builder.setColorFunction(Perp2);
			builder.setMaxColorWeight(2);
			builder.setMaxColor(new Vector4f(1.0f, 1.0f, 1.0f, 0.8f));
			noise = builder.build();
			noise.setSpinSpeed(0.1f);
		}
		{
			SkyboxBuilder builder = new SkyboxBuilder();
			builder.setBackdrop(false);
			builder.setSeed(1);
			builder.setSmoothness(70);
			builder.setDetail(4);
			builder.setFunction(SkyboxBuilder.Cerp);
			builder.setColorFunction(SkyboxBuilder.Lerp);
			builder.setMaxColorWeight(2);
			builder.setMaxColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
			noise2 = builder.build();
			noise2.setSpinSpeed(1.0f);
		}
		{
			mountains = new MountainSkybox(new MountainRenderer(){
				public float getCameraX(){
					return camera.x;
				}
				public float getCameraY(){
					return camera.y;
				}
				public float getCameraZ(){
					return camera.z;
				}
				public WorldHeightmaps getHeightmap(){
					return heightMaps;
				}
				public void renderMesh(){
					heightMaps.render(false);
				}
				public void renderSkybox(){
					heightMaps.render(true);
				}
			});
		}
		skybox = new SkyBox(noise, null, noise2, mountains);
		skybox.redrawMountains();
	}
	public void onKey(int key, int action){
		if(key==GLFW.GLFW_KEY_W){
			if(action==GLFW.GLFW_PRESS)
				w = true;
			else if(action==GLFW.GLFW_RELEASE)
				w = false;
		}else if(key==GLFW.GLFW_KEY_A){
			if(action==GLFW.GLFW_PRESS)
				a = true;
			else if(action==GLFW.GLFW_RELEASE)
				a = false;
		}else if(key==GLFW.GLFW_KEY_S){
			if(action==GLFW.GLFW_PRESS)
				s = true;
			else if(action==GLFW.GLFW_RELEASE)
				s = false;
		}else if(key==GLFW.GLFW_KEY_D){
			if(action==GLFW.GLFW_PRESS)
				d = true;
			else if(action==GLFW.GLFW_RELEASE)
				d = false;
		}else if(key==GLFW.GLFW_KEY_E){
			if(action==GLFW.GLFW_PRESS)
				e = true;
			else if(action==GLFW.GLFW_RELEASE)
				e = false;
		}else if(key==GLFW.GLFW_KEY_SPACE){
			if(action==GLFW.GLFW_PRESS)
				space = true;
			else if(action==GLFW.GLFW_RELEASE)
				space = false;
		}else if(key==GLFW.GLFW_KEY_LEFT_SHIFT){
			if(action==GLFW.GLFW_PRESS)
				shift = true;
			else if(action==GLFW.GLFW_RELEASE)
				shift = false;
		}else if(key==GLFW.GLFW_KEY_1){
			if(action==GLFW.GLFW_PRESS){
				if(wireframeMode){
					GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
					GL11.glEnable(GL11.GL_CULL_FACE);
				}else{
					GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
					GL11.glDisable(GL11.GL_CULL_FACE);
				}
				wireframeMode = !wireframeMode;
			}
		}else if(key==GLFW.GLFW_KEY_2){
			if(action==GLFW.GLFW_PRESS){
				fly = !fly;
			}
		}else if(key==GLFW.GLFW_KEY_ESCAPE){
			if(action==GLFW.GLFW_PRESS)
				GLFW.glfwSetWindowShouldClose(WraithavensConquest.INSTANCE.getWindow(), GL11.GL_TRUE);
		}else if(key==GLFW.GLFW_KEY_3){
			if(action==GLFW.GLFW_PRESS)
				walkLock = !walkLock;
		}else if(key==GLFW.GLFW_KEY_4){
			if(action==GLFW.GLFW_PRESS)
				world.unloadAllChunks();
		}
	}
	public void onMouse(int button, int action){
		if(action==GLFW.GLFW_PRESS){
			if(lockedMouse){
				GLFW.glfwSetInputMode(WraithavensConquest.INSTANCE.getWindow(), GLFW.GLFW_CURSOR,
					GLFW.GLFW_CURSOR_NORMAL);
			}else{
				GLFW.glfwSetCursorPos(WraithavensConquest.INSTANCE.getWindow(),
					WraithavensConquest.INSTANCE.getScreenWidth()/2f,
					WraithavensConquest.INSTANCE.getScreenHeight()/2f);
				GLFW.glfwSetInputMode(WraithavensConquest.INSTANCE.getWindow(), GLFW.GLFW_CURSOR,
					GLFW.GLFW_CURSOR_HIDDEN);
			}
			lockedMouse = !lockedMouse;
		}
	}
	public void onMouseMove(double x, double y){
		if(!lockedMouse)
			return;
		if(x==WraithavensConquest.INSTANCE.getScreenWidth()/2f
			&&y==WraithavensConquest.INSTANCE.getScreenHeight()/2f)
			return;
		camera.ry = (float)(camera.ry+(x-WraithavensConquest.INSTANCE.getScreenWidth()/2f)*mouseSpeed);
		camera.rx =
			(float)Math.max(
				Math.min(camera.rx+(y-WraithavensConquest.INSTANCE.getScreenHeight()/2f)*mouseSpeed, 90), -90);
		GLFW.glfwSetCursorPos(WraithavensConquest.INSTANCE.getWindow(),
			WraithavensConquest.INSTANCE.getScreenWidth()/2, WraithavensConquest.INSTANCE.getScreenHeight()/2f);
	}
	public void onMouseWheel(double x, double y){}
	public void render(){
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glPushMatrix();
		updateCamera(frameDelta);
		skybox.render(camera.x, camera.y, camera.z);
		// world.render();
		GL11.glPopMatrix();
	}
	public void update(double delta, double time){
		frameDelta = delta;
		move(delta);
		// world.update();
		skybox.update(time);
	}
	private void move(double delta){
		delta *= cameraSpeed;
		boolean cameraMoved = false;
		if(e)
			delta *= 50;
		if(w||walkLock){
			camera.goalX += delta*(float)Math.sin(Math.toRadians(camera.ry));
			camera.goalZ -= delta*(float)Math.cos(Math.toRadians(camera.ry));
			cameraMoved = true;
		}
		if(a){
			camera.goalX += delta*(float)Math.sin(Math.toRadians(camera.ry-90));
			camera.goalZ -= delta*(float)Math.cos(Math.toRadians(camera.ry-90));
			cameraMoved = true;
		}
		if(s){
			camera.goalX -= delta*(float)Math.sin(Math.toRadians(camera.ry));
			camera.goalZ += delta*(float)Math.cos(Math.toRadians(camera.ry));
			cameraMoved = true;
		}
		if(d){
			camera.goalX += delta*(float)Math.sin(Math.toRadians(camera.ry+90));
			camera.goalZ -= delta*(float)Math.cos(Math.toRadians(camera.ry+90));
			cameraMoved = true;
		}
		if(space)
			camera.goalY += delta;
		if(shift)
			camera.goalY -= delta;
		if(cameraMoved&&fly)
			camera.goalY = world.getHeightAt((int)camera.goalX, (int)camera.goalZ)+6;
	}
	private void updateCamera(double delta){
		float x = camera.x;
		float y = camera.y;
		float z = camera.z;
		camera.update(delta);
		if(camera.x!=x||camera.z!=z)
			heightMaps.update(camera.x, camera.z);
		if(camera.x!=x||camera.y!=y||camera.z!=z)
			skybox.redrawMountains();
	}
}
