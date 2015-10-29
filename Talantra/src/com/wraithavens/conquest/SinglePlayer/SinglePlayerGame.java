package com.wraithavens.conquest.SinglePlayer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.Driver;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.MatrixUtils;
import com.wraithavens.conquest.SinglePlayer.Blocks.World.World;
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
	private static float clamp(float a, float min, float max){
		return a<min?min:a>max?max:a;
	}
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
	private World world;
	private Grasslands grassLands;
	private ParticleBatch particleBatch;
	private WaterWorks waterWorks;
	private boolean initalized = false;
	private LoadingScreen loadingScreen;
	private int renderedSky = 0;
	public void dispose(){
		world.dispose();
		skybox.dispose();
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
	public EntityDatabase getEntityDatabase(){
		return entityDatabase;
	}
	public Grasslands getGrasslands(){
		return grassLands;
	}
	public ParticleBatch getParticleBatch(){
		return particleBatch;
	}
	public WaterWorks getWaterWorks(){
		return waterWorks;
	}
	public World getWorld(){
		return world;
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
		camera.teleport(0, machine.getGroundLevel(0, 0)+6, 0);
		SkyboxClouds noise = new SkyboxClouds(true, 0.5f, 0);
		SkyboxClouds[] noise2 = null;
		noise2 = new SkyboxClouds[SkyboxClouds.LayerCount];
		for(int i = 0; i<SkyboxClouds.LayerCount; i++)
			noise2[i] = new SkyboxClouds(false, (float)Math.random()*2, 0);
		skybox = new SkyBox(noise, new Sunbox(), noise2);
		entityDatabase = new EntityDatabase();
		particleBatch = new ParticleBatch();
		waterWorks = new WaterWorks();
		world = new World();
		grassLands = new Grasslands();
		loadingScreen = new LoadingScreen();
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
	}
	public void onMouse(int button, int action){
		if(button==GLFW.GLFW_MOUSE_BUTTON_LEFT||button==GLFW.GLFW_MOUSE_BUTTON_RIGHT){
			@SuppressWarnings("unused")
			boolean left = button==GLFW.GLFW_MOUSE_BUTTON_LEFT;
			if(action==GLFW.GLFW_PRESS){
				ColorConsole con = ColorConsole.INSTANCE;
				CameraTargetBlockCallback callback = camera.getTargetBlock(50);
				con.println("Block Hit: "+callback.block);
				con.println("     Side: "+callback.side);
				con.println("      Pos: ["+callback.x+", "+callback.y+", "+callback.z+"]");
				// TODO Reimplement block editing.
				// if(callback.block!=null){
				// int x = callback.x;
				// int y = callback.y;
				// int z = callback.z;
				// if(left)
				// landscape.setBlock(x, y, z, null);
				// else{
				// switch(callback.side){
				// case 0:
				// x++;
				// break;
				// case 1:
				// x--;
				// break;
				// case 2:
				// y++;
				// break;
				// case 3:
				// y--;
				// break;
				// case 4:
				// z++;
				// break;
				// case 5:
				// z--;
				// break;
				// default:
				// throw new RuntimeException();
				// }
				// landscape.setBlock(x, y, z, Block.Dirt);
				// }
				// }
			}
		}
	}
	public void onMouseMove(double x, double y){
		if(!lockedMouse)
			return;
		float width = WraithavensConquest.INSTANCE.getScreenWidth()/2f;
		float height = WraithavensConquest.INSTANCE.getScreenHeight()/2f;
		if(x==width&&y==height)
			return;
		camera.turnTo(clamp((float)(camera.getRX()+(y-height)*mouseSpeed), -89, 89),
			(float)(camera.getRY()+(x-width)*mouseSpeed));
		GLFW.glfwSetCursorPos(WraithavensConquest.INSTANCE.getWindow(), width, height);
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
			skybox.render(camera.getX(), camera.getY(), camera.getZ());
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			renderedSky = 2;
		}
		if(renderedSky!=1)
			MatrixUtils.setupPerspective(70, WraithavensConquest.INSTANCE.getScreenWidth()
				/(float)WraithavensConquest.INSTANCE.getScreenHeight(), 0.5f, 5000);
		world.render();
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
		frameDelta = delta;
		move(delta);
		world.update();
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
			camera.moveTo((float)(camera.getGoalX()+delta*(float)Math.sin(Math.toRadians(camera.getRY()))),
				camera.getGoalY(),
				(float)(camera.getGoalX()-delta*(float)Math.cos(Math.toRadians(camera.getRY()))));
			cameraMoved = true;
		}
		if(a){
			camera.moveTo((float)(camera.getGoalX()+delta*(float)Math.sin(Math.toRadians(camera.getRY()-90))),
				camera.getGoalY(),
				(float)(camera.getGoalX()-delta*(float)Math.cos(Math.toRadians(camera.getRY()-90))));
			cameraMoved = true;
		}
		if(s){
			camera.moveTo((float)(camera.getGoalX()-delta*(float)Math.sin(Math.toRadians(camera.getRY()))),
				camera.getGoalY(),
				(float)(camera.getGoalX()+delta*(float)Math.cos(Math.toRadians(camera.getRY()))));
			cameraMoved = true;
		}
		if(d){
			camera.moveTo((float)(camera.getGoalX()+delta*(float)Math.sin(Math.toRadians(camera.getRY()+90))),
				camera.getGoalY(),
				(float)(camera.getGoalX()-delta*(float)Math.cos(Math.toRadians(camera.getRY()+90))));
			cameraMoved = true;
		}
		if(grounded){
			if(cameraMoved)
				camera.moveTo(camera.getGoalX(),
					machine.getGroundLevel(camera.getGoalBlockX(), camera.getGoalBlockZ())+6, camera.getGoalZ());
		}else{
			if(space)
				camera.moveTo(camera.getGoalX(), (float)(camera.getGoalY()+delta), camera.getGoalZ());
			if(shift)
				camera.moveTo(camera.getGoalX(), (float)(camera.getGoalY()-delta), camera.getGoalZ());
		}
	}
}
