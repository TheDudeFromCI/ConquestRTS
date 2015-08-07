package com.wraithavens.conquest.SinglePlayer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.Driver;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector4f;
import com.wraithavens.conquest.SinglePlayer.Blocks.World;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.StaticEntity;
import com.wraithavens.conquest.SinglePlayer.Heightmap.Dynmap;
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
	private static final boolean LoadWorld = false;
	private static final boolean LoadHeightmap = false;
	private static final boolean LoadSkyboxes = false;
	private static final boolean LoadCloudBackdrop = true;
	private static final boolean LoadCloudForeground = true;
	private static final boolean LoadMountainSkybox = false;
	private static final boolean LoadDynmap = false;
	private static final boolean LoadEntityDatabase = true;
	private WorldHeightmaps heightMaps;
	private boolean w, a, s, d, shift, space, fly, lockedMouse, walkLock, e;
	private boolean wireframeMode;
	private boolean processBlocks = true;
	private boolean processHeightmap = true;
	private boolean processMoveEvents = true;
	private final float cameraSpeed = 40f;
	private final float mouseSpeed = 0.2f;
	private final Camera camera = new Camera();
	private double frameDelta;
	private World world;
	private SkyBox skybox;
	private Dynmap dynmap;
	private WorldNoiseMachine machine;
	private EntityDatabase entityDatabase;
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
		machine = WorldNoiseMachine.generate(seeds);
		// ---
		// Setup the camera.
		// ---
		camera.cameraMoveSpeed = 10.0f;
		camera.goalY = camera.y = (float)machine.getWorldHeight(0, 0)+6;
		camera.goalX = 8192;
		camera.goalZ = 8192;
		// ---
		// Properties for rendering.
		// ---
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		// ---
		// Load the landscape.
		// ---
		if(LoadWorld)
			world = new World(machine, camera);
		if(LoadHeightmap)
			heightMaps = new WorldHeightmaps(machine);
		// // ---
		// // Load the skyboxes.
		// // ---
		if(LoadSkyboxes){
			SkyboxClouds noise = null;
			SkyboxClouds noise2 = null;
			MountainSkybox mountains = null;
			// ---
			// Load the backdrop sky.
			// ---
			if(LoadCloudBackdrop){
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
			// ---
			// Load the forground clouds.
			// ---
			if(LoadCloudForeground){
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
			// ---
			// Load the mountain skybox renderer.
			// ---
			if(LoadMountainSkybox){
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
			if(mountains!=null)
				skybox.redrawMountains();
		}
		if(LoadEntityDatabase)
			entityDatabase = new EntityDatabase();
		if(LoadDynmap)
			dynmap = new Dynmap(machine);
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
				System.out.println("Wireframe mode now set to "+wireframeMode+".");
			}
		}else if(key==GLFW.GLFW_KEY_2){
			if(action==GLFW.GLFW_PRESS){
				fly = !fly;
				System.out.println("Fly mode now set to "+fly+".");
			}
		}else if(key==GLFW.GLFW_KEY_ESCAPE){
			if(action==GLFW.GLFW_PRESS)
				GLFW.glfwSetWindowShouldClose(WraithavensConquest.INSTANCE.getWindow(), GL11.GL_TRUE);
		}else if(key==GLFW.GLFW_KEY_3){
			if(action==GLFW.GLFW_PRESS){
				walkLock = !walkLock;
				System.out.println("Walklock now set to "+walkLock+".");
			}
		}else if(key==GLFW.GLFW_KEY_4){
			if(action==GLFW.GLFW_PRESS){
				world.unloadAllChunks();
				System.out.println("All chunks unloaded.");
			}
		}else if(key==GLFW.GLFW_KEY_5){
			if(action==GLFW.GLFW_PRESS){
				processBlocks = !processBlocks;
				System.out.println("Block processing now set to "+processBlocks+".");
			}
		}else if(key==GLFW.GLFW_KEY_6){
			if(action==GLFW.GLFW_PRESS){
				processHeightmap = !processHeightmap;
				System.out.println("Heightmap processing now set to "+processHeightmap+".");
			}
		}else if(key==GLFW.GLFW_KEY_7){
			if(action==GLFW.GLFW_PRESS){
				processMoveEvents = !processMoveEvents;
				System.out.println("Move event processing now set to "+processMoveEvents+".");
			}
		}else if(key==GLFW.GLFW_KEY_8){
			if(action==GLFW.GLFW_PRESS){
				if(entityDatabase==null){
					System.out.println("Entity database not created. Could not place entity.");
					return;
				}
				StaticEntity e = new StaticEntity(EntityType.Grass);
				entityDatabase.addEntity(e);
				System.out.println("Spawned grass entity.");
			}
		}else if(key==GLFW.GLFW_KEY_9){
			if(action==GLFW.GLFW_PRESS){
				if(entityDatabase==null){
					System.out.println("Entity database not created. Could not clear entities.");
					return;
				}
				entityDatabase.dispose();
				System.out.println("Entity database cleared.");
				System.out.println("Testing entity mesh references:");
				for(EntityType e : EntityType.values()){
					if(e.getMeshRenferences()==-1)
						System.out.println("  "+e.fileName+" = No References");
					else{
						System.out.println(">>>Reference found for "+e.fileName+"!");
						System.out.println("  -Reference count: "+e.getMeshRenferences());
					}
				}
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
		if(wireframeMode||skybox==null)
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		else
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glPushMatrix();
		updateCamera(frameDelta);
		if(wireframeMode){
			if(processHeightmap)
				if(heightMaps!=null)
					heightMaps.render(false);
		}else if(skybox!=null)
			skybox.render(camera.x, camera.y, camera.z);
		if(processBlocks)
			if(world!=null)
				world.render();
		if(dynmap!=null)
			dynmap.render();
		if(entityDatabase!=null)
			entityDatabase.render();
		GL11.glPopMatrix();
	}
	public void update(double delta, double time){
		frameDelta = delta;
		move(delta);
		// ---
		// Check to see if we should or should not update the world. Then act
		// accoringly.
		// ---
		if(processBlocks)
			if(world!=null)
				world.update();
		// ---
		// Skybox isn't visible in wireframe mode, so no need to update it.
		// ---
		if(!wireframeMode)
			if(skybox!=null)
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
			camera.goalY = (float)(machine.getWorldHeight(camera.goalX, camera.goalZ)+6);
	}
	private void updateCamera(double delta){
		float x = camera.x;
		float y = camera.y;
		float z = camera.z;
		camera.update(delta);
		if(!processMoveEvents)
			return;
		if(camera.x!=x||camera.z!=z){
			if(dynmap!=null)
				dynmap.update(camera.x, camera.z);
			if(processHeightmap)
				if(heightMaps!=null)
					heightMaps.update(camera.x, camera.z);
		}
		if(camera.x!=x||camera.y!=y||camera.z!=z){
			if(skybox!=null)
				skybox.redrawMountains();
		}
	}
}
