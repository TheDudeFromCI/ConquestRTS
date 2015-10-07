package com.wraithavens.conquest.SinglePlayer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.Driver;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.MatrixUtils;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Entities.Water.WaterWorks;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.CameraTargetBlockCallback;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyBox;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxClouds;
import com.wraithavens.conquest.SinglePlayer.Skybox.Sunbox;
import com.wraithavens.conquest.Utility.LoadingScreen;
import com.wraithavens.conquest.Utility.Debug.ColorConsole;

public class SinglePlayerGame implements Driver{
	public static SinglePlayerGame INSTANCE;
	private boolean w, a, s, d, shift, space, grounded = true, lockedMouse, walkLock, e;
	private boolean wireframeMode;
	private final float cameraSpeed = 10f;
	private final float mouseSpeed = 0.2f;
	private final Camera camera = new Camera();
	private double frameDelta;
	private SkyBox skybox;
	private WorldNoiseMachine machine;
	private EntityDatabase entityDatabase;
	private LandscapeWorld landscape;
	private Grasslands grassLands;
	private ParticleBatch particleBatch;
	private WaterWorks waterWorks;
	private boolean initalized = false;
	private LoadingScreen loadingScreen;
	private double lastLocationTime = 0;
	private int renderedSky = 0;
	public void dispose(){
		skybox.dispose();
		landscape.dispose();
		entityDatabase.dispose();
		grassLands.dispose();
		particleBatch.dispose();
		loadingScreen.dispose();
		waterWorks.dispose();
		INSTANCE = null;
	}
	public Camera getCamera(){
		return camera;
	}
	public LandscapeWorld getLandscape(){
		return landscape;
	}
	public ParticleBatch getParticleBatch(){
		return particleBatch;
	}
	public WaterWorks getWaterWorks(){
		return waterWorks;
	}
	public WorldNoiseMachine getWorldNoiseMachine(){
		return machine;
	}
	public void initalize(double time){
		if(initalized)
			return;
		INSTANCE = this;
		initalized = true;
		long[] seeds = new long[]{
			0, 1, 2, 3, 4, 5, 6, 7
		};
		machine = WorldNoiseMachine.generate(seeds);
		// ---
		// Setup the camera.
		// ---
		camera.cameraMoveSpeed = 10.0f;
		camera.goalX = camera.x = 0;
		camera.goalZ = camera.z = 0;
		camera.goalY = machine.getGroundLevel(camera.x, camera.z)+6;
		SkyboxClouds noise = new SkyboxClouds(true, 0.5f, 0);
		SkyboxClouds[] noise2 = null;
		noise2 = new SkyboxClouds[SkyboxClouds.LayerCount];
		for(int i = 0; i<SkyboxClouds.LayerCount; i++)
			noise2[i] = new SkyboxClouds(false, (float)Math.random()*2, 0);
		skybox = new SkyBox(noise, new Sunbox(), noise2);
		entityDatabase = new EntityDatabase(camera);
		particleBatch = new ParticleBatch(camera);
		waterWorks = new WaterWorks();
		landscape = new LandscapeWorld(machine, entityDatabase, camera, particleBatch);
		grassLands = new Grasslands(landscape, camera);
		entityDatabase.setLandscape(landscape);
		landscape.setup(grassLands);
		loadingScreen = new LoadingScreen();
		landscape.start();
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
		}else if(key==GLFW.GLFW_KEY_ESCAPE){
			if(action==GLFW.GLFW_PRESS)
				GLFW.glfwSetWindowShouldClose(WraithavensConquest.INSTANCE.getWindow(), GL11.GL_TRUE);
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
				System.out.println("Wireframe mode now set to "+wireframeMode+".");
			}
		}else if(key==GLFW.GLFW_KEY_2){
			if(action==GLFW.GLFW_PRESS){
				grounded = !grounded;
				System.out.println("Ground mode now set to "+grounded+".");
			}
		}else if(key==GLFW.GLFW_KEY_3){
			if(action==GLFW.GLFW_PRESS){
				walkLock = !walkLock;
				System.out.println("Walklock now set to "+walkLock+".");
			}
		}else if(key==GLFW.GLFW_KEY_4){
			if(action==GLFW.GLFW_PRESS){
				ColorConsole con = ColorConsole.INSTANCE;
				CameraTargetBlockCallback callback = camera.getTargetBlock(50);
				con.println("Block Hit: "+callback.block);
				con.println("     Side: "+callback.side);
				con.println("      Pos: ["+callback.x+", "+callback.y+", "+callback.z+"]");
			}
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
		if(loadingScreen.hasTask()){
			loadingScreen.render();
			return;
		}
		GL11.glPushMatrix();
		camera.update(frameDelta);
		if(wireframeMode||!WraithavensConquest.Settings.isRenderSky()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
			renderedSky = 1;
		}else{
			skybox.render(camera.x, camera.y, camera.z);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			renderedSky = 2;
		}
		if(renderedSky!=1)
			MatrixUtils.setupPerspective(70, WraithavensConquest.INSTANCE.getScreenWidth()
				/(float)WraithavensConquest.INSTANCE.getScreenHeight(), 0.5f, 5000);
		landscape.render();
		entityDatabase.render();
		grassLands.render();
		waterWorks.render();
		particleBatch.render();
		GL11.glPopMatrix();
	}
	public void update(double delta, double time){
		if(loadingScreen.hasTask()){
			loadingScreen.update(time);
			return;
		}
		if(time-lastLocationTime>=1){
			System.out.println("Camera Location: ["+Math.floor(camera.x)+", "+Math.floor(camera.y)+", "
				+Math.floor(camera.z)+"]");
			lastLocationTime = time;
		}
		frameDelta = delta;
		move(delta);
		landscape.update(time);
		skybox.update(time);
		waterWorks.update(time);
		grassLands.update(time);
		entityDatabase.update(time);
		particleBatch.update(delta, time);
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
		if(grounded){
			if(cameraMoved)
				camera.goalY = machine.getGroundLevel((int)camera.goalX, (int)camera.goalZ)+6;
		}else{
			if(space)
				camera.goalY += delta;
			if(shift)
				camera.goalY -= delta;
		}
	}
}
