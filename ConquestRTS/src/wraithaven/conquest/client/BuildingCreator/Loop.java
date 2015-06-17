package wraithaven.conquest.client.BuildingCreator;

import java.awt.Dimension;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.WindowUtil.FileChooser;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.LoadingScreen;
import wraithaven.conquest.client.LoadingScreenTask;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.PalleteRenderer;
import wraithaven.conquest.client.GameWorld.LoopControls.LoopObjective;
import wraithaven.conquest.client.GameWorld.LoopControls.MainLoop;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldBounds;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;

public class Loop implements LoopObjective{
	public static final float CAMERA_NEAR_CLIP = 0.05f;
	public static Loop INSTANCE;
	public static float ISO_ZOOM = 0.12f;
	public static Dimension screenRes;
	public static int EXTRA_TERRAIN_DISTANCE = 128;
	private static void setupOGL(){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}
	private BuildingCreator buildingCreator;
	private Camera camera;
	private BuildCreatorWorld creatorWorld;
	private GuiHandler guiHandler;
	private InputController inputController;
	private Inventory inventory;
	private LoadingScreen loadingScreen;
	private DoubleBuffer mouseBufferX = BufferUtils.createDoubleBuffer(1);
	private DoubleBuffer mouseBufferY = BufferUtils.createDoubleBuffer(1);
	private PalleteRenderer palleteRenderer;
	private boolean removePalette, createPalette;
	private SelectedBlock selectedBlock;
	private Skybox skybox;
	private UserBlockHandler userBlockHandler;
	private VoxelWorld world;
	public Loop(Dimension screenRes, BuildingCreator buildingCreator){
		Loop.screenRes = screenRes;
		this.buildingCreator = buildingCreator;
		Loop.INSTANCE = this;
	}
	public void dispose(){
		world.clearAll();
		guiHandler.cleanUp();
		Texture.disposeAll();
		Loop.INSTANCE = null;
	}
	public void disposePalette(){
		removePalette = true;
	}
	public BuildingCreator getBuildingCreator(){
		return buildingCreator;
	}
	public Camera getCamera(){
		return camera;
	}
	public GuiHandler getGuiHandler(){
		return guiHandler;
	}
	public InputController getInputController(){
		return inputController;
	}
	public Inventory getInventory(){
		return inventory;
	}
	public VoxelWorld getVoxelWorld(){
		return world;
	}
	public long getWindow(){
		return buildingCreator.getWindow();
	}
	public boolean hasPalette(){
		return palleteRenderer!=null;
	}
	public void key(long window, int key, int action){
		if(loadingScreen!=null)return;
		if(FileChooser.INSTANCE!=null){
			FileChooser.INSTANCE.onKey(key, action);
			return;
		}
		if(key==GLFW.GLFW_KEY_C&&action==GLFW.GLFW_PRESS&&!hasPalette()){
			inventory.setShown(!inventory.isShown());
			return;
		}
		if(inventory.isShown())return;
		inputController.onKey(key, action);
	}
	public void mouse(long window, int button, int action){
		if(loadingScreen!=null)return;
		if(hasPalette()){
			if(action==GLFW.GLFW_PRESS){
				GLFW.glfwGetCursorPos(window, mouseBufferX, mouseBufferY);
				palleteRenderer.onMouseDown(mouseBufferX.get(0), mouseBufferY.get(0));
			}else if(action==GLFW.GLFW_RELEASE)palleteRenderer.onMouseUp(button);
		}else if(inventory.isShown()){
			if(action==GLFW.GLFW_PRESS){
				GLFW.glfwGetCursorPos(window, mouseBufferX, mouseBufferY);
				inventory.onMouseDown(mouseBufferX.get(0), mouseBufferY.get(0));
			}else if(action==GLFW.GLFW_RELEASE)inventory.onMouseUp();
		}else if(guiHandler.isPaused()){
			if(FileChooser.INSTANCE!=null){
				if(action==GLFW.GLFW_PRESS){
					GLFW.glfwGetCursorPos(window, mouseBufferX, mouseBufferY);
					FileChooser.INSTANCE.onMouseDown(mouseBufferX.get(0), mouseBufferY.get(0));
				}else if(action==GLFW.GLFW_RELEASE)FileChooser.INSTANCE.onMouseUp();
			}else{
				if(action==GLFW.GLFW_PRESS){
					GLFW.glfwGetCursorPos(window, mouseBufferX, mouseBufferY);
					guiHandler.onMouseDown(mouseBufferX.get(0), mouseBufferY.get(0));
				}else if(action==GLFW.GLFW_RELEASE)guiHandler.onMouseUp();
			}
		}else userBlockHandler.mouseClick(button, action);
	}
	public void mouseMove(long window, double x, double y){
		if(loadingScreen!=null)return;
		if(hasPalette())palleteRenderer.onMouseMove(x, y);
		else if(inventory.isShown())inventory.onMouseMove(x, y);
		else if(guiHandler.isPaused()){
			if(FileChooser.INSTANCE!=null)FileChooser.INSTANCE.onMouseMove(x, y);
			guiHandler.onMouseMove(x, y);
		}else inputController.processMouse(x, y);
	}
	public void mouseWheel(long window, double xPos, double yPos){
		if(loadingScreen!=null)return;
		if(hasPalette())return;
		if(inventory.isShown())return;
		if(guiHandler.isPaused()){
			if(FileChooser.INSTANCE!=null)FileChooser.INSTANCE.onMouseWheel(yPos);
			return;
		}
		inputController.mouseWheel(yPos);
	}
	public void preLoop(){
		camera = new Camera(70, Loop.screenRes.width/(float)Loop.screenRes.height, Loop.CAMERA_NEAR_CLIP, 1000, false);
		creatorWorld = new BuildCreatorWorld();
		world = new VoxelWorld(creatorWorld, new VoxelWorldBounds(-EXTRA_TERRAIN_DISTANCE, 0, -EXTRA_TERRAIN_DISTANCE, BuildingCreator.WORLD_BOUNDS_SIZE-1+EXTRA_TERRAIN_DISTANCE, BuildingCreator.WORLD_BOUNDS_SIZE-1, BuildingCreator.WORLD_BOUNDS_SIZE-1+EXTRA_TERRAIN_DISTANCE));
		BuildCreatorWorld.setup();
		inputController = new InputController();
		userBlockHandler = new UserBlockHandler();
		guiHandler = new GuiHandler();
		skybox = new Skybox(Texture.getTexture(ClientLauncher.assetFolder, "Day Skybox.png"));
		selectedBlock = new SelectedBlock();
		inventory = new Inventory();
		setupCameraPosition();
		Loop.setupOGL();
		MainLoop.FPS_SYNC = false;
		loadingScreen = new LoadingScreen(new LoadingScreenTask(){
			int chunkLimit = (BuildingCreator.WORLD_BOUNDS_SIZE-1+EXTRA_TERRAIN_DISTANCE)/Chunk.BLOCKS_PER_CHUNK;
			float percent, loadingPercent, rebuildingPercent;
			int extraDistance = EXTRA_TERRAIN_DISTANCE/Chunk.BLOCKS_PER_CHUNK-1;
			int x = -extraDistance, z = -extraDistance, w;
			int maxIterations = (int)Math.pow(Loop.EXTRA_TERRAIN_DISTANCE/Chunk.BLOCKS_PER_CHUNK+BuildingCreator.WORLD_BOUNDS_CHUNKS, 2), loops;
			private void load(){
				for(int i = 0; i<64; i++){
					world.loadChunk(x, 0, z);
					loops++;
					z++;
					if(z>chunkLimit){
						z = -extraDistance;
						x++;
						if(x>chunkLimit){
							loadingPercent = 1;
							return;
						}
					}
				}
				loadingPercent = loops/(float)maxIterations;
			}
			private void rebuild(){
				for(int i = 0; i<64; i++){
					world.getChunk(w).rebuild();
					w++;
					rebuildingPercent = w/(float)world.getChunkCount();
					if(w==world.getChunkCount())return;
				}
			}
			public int update(){
				if(loadingPercent==1)rebuild();
				else load();
				percent = loadingPercent*0.05f+rebuildingPercent*0.95f;
				return (int)(percent*100);
			}
		}, new Runnable(){
			public void run(){
				loadingScreen = null;
				MatrixUtils.setupPerspective(70, Loop.screenRes.width/(float)Loop.screenRes.height, Loop.CAMERA_NEAR_CLIP, 1000);
				GL11.glClearColor(219/255f, 246/255f, 251/255f, 0);
				MainLoop.FPS_SYNC = true;
			}
		});
	}
	public void render(){
		if(loadingScreen!=null){
			loadingScreen.render();
			return;
		}
		if(palleteRenderer!=null)palleteRenderer.render();
		else{
			skybox.render(camera);
			world.render();
			selectedBlock.render();
			if(BuildingCreator.DEBUG){
				GL11.glBegin(GL11.GL_LINES);
				GL11.glColor3f(1, 0, 0);
				GL11.glVertex3f(camera.x, camera.y-2, camera.z);
				GL11.glColor3f(1, 0, 0);
				GL11.glVertex3f(camera.x+5, camera.y-2, camera.z);
				GL11.glColor3f(0, 0, 1);
				GL11.glVertex3f(camera.x, camera.y-2, camera.z);
				GL11.glColor3f(0, 0, 1);
				GL11.glVertex3f(camera.x, camera.y-2, camera.z+5);
				GL11.glEnd();
			}
			GL11.glPopMatrix();
			guiHandler.render();
			if(FileChooser.INSTANCE!=null)FileChooser.INSTANCE.render();
			inventory.render();
			MatrixUtils.setupPerspective(70, Loop.screenRes.width/(float)Loop.screenRes.height, Loop.CAMERA_NEAR_CLIP, 1000);
		}
		if(removePalette){
			palleteRenderer.dispose();
			palleteRenderer = null;
			removePalette = false;
			GLFW.glfwSetCursorPos(buildingCreator.getWindow(), Loop.screenRes.width/2.0, Loop.screenRes.height/2.0);
			GLFW.glfwSetInputMode(buildingCreator.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
			GL11.glClearColor(219/255f, 246/255f, 251/255f, 0);
		}
		if(createPalette){
			palleteRenderer = new PalleteRenderer();
			createPalette = false;
			GLFW.glfwSetInputMode(buildingCreator.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
			GL11.glClearColor(0, 0, 0, 0);
		}
	}
	public void setPalette(){
		createPalette = true;
	}
	private void setupCameraPosition(){
		float center = (BuildingCreator.WORLD_BOUNDS_SIZE-1)/2f;
		camera.goalX = camera.x = center;
		camera.goalY = camera.y = 5;
		camera.goalZ = camera.z = center;
		camera.cameraMoveSpeed = 3.75f;
	}
	public void update(double delta, double time){
		if(loadingScreen!=null){
			loadingScreen.update();
			return;
		}
		if(palleteRenderer!=null)palleteRenderer.update(time);
		else{
			inputController.processWalk(world, delta);
			GL11.glPushMatrix();
			camera.update(delta);
			userBlockHandler.update(time);
			world.setNeedsRebatch();
			guiHandler.update(delta);
			if(FileChooser.INSTANCE!=null)FileChooser.INSTANCE.update(time);
		}
	}
	public void setLoadingScreen(LoadingScreen ls){
		loadingScreen = ls;
	}
}