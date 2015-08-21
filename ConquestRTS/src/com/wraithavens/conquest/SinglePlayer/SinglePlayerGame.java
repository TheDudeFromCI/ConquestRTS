package com.wraithavens.conquest.SinglePlayer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.Driver;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.MatrixUtils;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Heightmap.Dynmap;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyBox;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxClouds;
import com.wraithavens.conquest.Utility.WireframeCube;

public class SinglePlayerGame implements Driver{
	private static final boolean LoadSkyboxes = true;
	private static final boolean LoadCloudBackdrop = true;
	private static final boolean LoadCloudForeground = true;
	private static final boolean LoadDynmap = true;
	private static final boolean LoadEntityDatabase = true;
	private static final boolean LoadLandscape = true;
	private static final boolean LoadGrasslands = true;
	private boolean w, a, s, d, shift, space, grounded = true, lockedMouse, walkLock, e;
	private boolean wireframeMode;
	private boolean processBlocks = true;
	private boolean processHeightmap = true;
	private boolean processMoveEvents = true;
	private boolean chunkLoading = true;
	private final float cameraSpeed = 10f;
	private final float mouseSpeed = 0.2f;
	private final Camera camera = new Camera();
	private double frameDelta;
	private SkyBox skybox;
	private Dynmap dynmap;
	private WorldNoiseMachine machine;
	private EntityDatabase entityDatabase;
	private LandscapeWorld landscape;
	private Grasslands grassLands;
	public void dispose(){
		GlError.out("Disposing single player driver.");
		GlError.dumpError();
		if(dynmap!=null)
			dynmap.dispose();
		if(skybox!=null)
			skybox.dispose();
		if(landscape!=null)
			landscape.dispose();
		if(entityDatabase!=null)
			entityDatabase.dispose();
		WireframeCube.dipose();
		GlError.dumpError();
	}
	public void initalize(double time){
		GlError.out("Initalizing single player driver.");
		long[] seeds = new long[]{
			0, 1, 2, 3
		};
		machine = WorldNoiseMachine.generate(seeds);
		// ---
		// Setup the camera.
		// ---
		GlError.out("Preparing camera.");
		camera.cameraMoveSpeed = 10.0f;
		camera.goalY = camera.y = machine.getGroundLevel(8192, 8192)+6;
		camera.goalX = 4096;
		camera.goalZ = 4096;
		MatrixUtils.setupPerspective(70, WraithavensConquest.INSTANCE.getScreenWidth()
			/(float)WraithavensConquest.INSTANCE.getScreenHeight(), 0.5f, 16384);
		if(LoadSkyboxes){
			SkyboxClouds noise = LoadCloudBackdrop?new SkyboxClouds(true, 0.5f):null;
			SkyboxClouds[] noise2 = null;
			if(LoadCloudForeground){
				noise2 = new SkyboxClouds[SkyboxClouds.LayerCount];
				for(int i = 0; i<SkyboxClouds.LayerCount; i++)
					noise2[i] = new SkyboxClouds(false, (float)Math.random()*2);
			}
			skybox = new SkyBox(noise, null, noise2);
		}
		if(LoadDynmap)
			dynmap = new Dynmap(machine);
		if(LoadEntityDatabase)
			entityDatabase = new EntityDatabase(camera);
		if(LoadGrasslands)
			grassLands = new Grasslands(camera);
		if(LoadLandscape)
			landscape = new LandscapeWorld(machine, entityDatabase, grassLands, camera);
		if(entityDatabase!=null)
			entityDatabase.setLandscape(landscape);
		if(grassLands!=null)
			grassLands.setLandscape(landscape);
		// WireframeCube.build();
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
				GlError.out("Wireframe mode now set to "+wireframeMode+".");
			}
		}else if(key==GLFW.GLFW_KEY_2){
			if(action==GLFW.GLFW_PRESS){
				grounded = !grounded;
				GlError.out("Ground mode now set to "+grounded+".");
			}
		}else if(key==GLFW.GLFW_KEY_ESCAPE){
			if(action==GLFW.GLFW_PRESS)
				GLFW.glfwSetWindowShouldClose(WraithavensConquest.INSTANCE.getWindow(), GL11.GL_TRUE);
		}else if(key==GLFW.GLFW_KEY_3){
			if(action==GLFW.GLFW_PRESS){
				walkLock = !walkLock;
				GlError.out("Walklock now set to "+walkLock+".");
			}
		}else if(key==GLFW.GLFW_KEY_5){
			if(action==GLFW.GLFW_PRESS){
				processBlocks = !processBlocks;
				GlError.out("Block processing now set to "+processBlocks+".");
			}
		}else if(key==GLFW.GLFW_KEY_6){
			if(action==GLFW.GLFW_PRESS){
				processHeightmap = !processHeightmap;
				GlError.out("Heightmap processing now set to "+processHeightmap+".");
			}
		}else if(key==GLFW.GLFW_KEY_7){
			if(action==GLFW.GLFW_PRESS){
				processMoveEvents = !processMoveEvents;
				GlError.out("Move event processing now set to "+processMoveEvents+".");
			}
		}else if(key==GLFW.GLFW_KEY_9){
			if(action==GLFW.GLFW_PRESS){
				if(entityDatabase==null){
					GlError.out("Entity database not created. Could not clear entities.");
					return;
				}
				entityDatabase.clear();
				GlError.out("Entity database cleared.");
				GlError.out("Testing entity mesh references:");
				for(EntityType e : EntityType.values()){
					if(e.getMeshRenferences()==-1)
						GlError.out("  "+e.fileName+" = No References");
					else{
						GlError.out(">>>Reference found for "+e.fileName+"!");
						GlError.out("  -Reference count: "+e.getMeshRenferences());
					}
				}
			}
		}else if(key==GLFW.GLFW_KEY_0){
			if(action==GLFW.GLFW_PRESS){
				chunkLoading = !chunkLoading;
				GlError.out("Chunk loading now set to "+chunkLoading+".");
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
		if(skybox!=null&&!wireframeMode)
			skybox.render(camera.x, camera.y, camera.z);
		if(dynmap!=null){
			dynmap.render();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		}
		if(processBlocks){
			if(landscape!=null)
				landscape.render();
		}
		if(grassLands!=null)
			grassLands.render();
		if(entityDatabase!=null)
			entityDatabase.render();
		WireframeCube.render();
		GL11.glPopMatrix();
	}
	public void update(double delta, double time){
		frameDelta = delta;
		move(delta);
		// ---
		// Check to see if we should or should not update the world. Then act
		// accoringly.
		// ---
		if(processBlocks&&chunkLoading){
			if(landscape!=null)
				landscape.update();
		}
		// ---
		// Skybox isn't visible in wireframe mode, so no need to update it.
		// ---
		if(!wireframeMode&&skybox!=null)
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
		if(cameraMoved&&grounded)
			camera.goalY = machine.getGroundLevel((int)camera.goalX, (int)camera.goalZ)+6;
	}
	private void updateCamera(double delta){
		float x = camera.x;
		float z = camera.z;
		camera.update(delta);
		if(!processMoveEvents)
			return;
		if(camera.x!=x||camera.z!=z){
			if(dynmap!=null)
				dynmap.update(camera.x, camera.z);
		}
	}
}
