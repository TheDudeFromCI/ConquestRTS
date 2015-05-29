package wraithaven.conquest.client.BuildingCreator;

import java.awt.Dimension;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.LoopControls.MainLoop;
import wraithaven.conquest.client.LoadingScreenTask;
import wraithaven.conquest.client.LoadingScreen;
import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldBounds;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.LoopControls.LoopObjective;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.PalleteRenderer;
import static org.lwjgl.glfw.GLFW.*;

public class Loop implements LoopObjective{
	private Camera camera;
	private VoxelWorld world;
	private BuildCreatorWorld creatorWorld;
	private InputController inputController;
	private UserBlockHandler userBlockHandler;
	private GuiHandler guiHandler;
	private Skybox skybox;
	private SelectedBlock selectedBlock;
	private Inventory inventory;
	private BuildingCreator buildingCreator;
	private PalleteRenderer palleteRenderer;
	private boolean removePalette, createPalette;
	public static Dimension screenRes;
	public static float ISO_ZOOM = 0.12f;
	public static final float CAMERA_NEAR_CLIP = 0.05f;
	public static Loop INSTANCE;
	public Loop(Dimension screenRes, BuildingCreator buildingCreator){
		Loop.screenRes=screenRes;
		this.buildingCreator=buildingCreator;
		INSTANCE=this;
	}
	private LoadingScreen loadingScreen;
	public void preLoop(){
		camera=new Camera(70, screenRes.width/(float)screenRes.height, CAMERA_NEAR_CLIP, 1000, false);
		creatorWorld=new BuildCreatorWorld();
		world=new VoxelWorld(creatorWorld, new VoxelWorldBounds(0, 0, 0, BuildingCreator.WORLD_BOUNDS_SIZE-1, BuildingCreator.WORLD_BOUNDS_SIZE-1, BuildingCreator.WORLD_BOUNDS_SIZE-1));
		BuildCreatorWorld.setup();
		inputController=new InputController();
		userBlockHandler=new UserBlockHandler();
		guiHandler=new GuiHandler();
		skybox=new Skybox(Texture.getTexture(ClientLauncher.assetFolder, "Day Skybox.png"));
		selectedBlock=new SelectedBlock();
		inventory=new Inventory();
		setupCameraPosition();
		setupOGL();
		MainLoop.FPS_SYNC=false;
		loadingScreen=new LoadingScreen(new LoadingScreenTask(){
			int x, z, w;
			int chunkLimit = (BuildingCreator.WORLD_BOUNDS_SIZE-1)>>Chunk.CHUNK_BITS;
			float percent, loadingPercent, rebuildingPercent;
			public int update(){
				if(loadingPercent<1)load();
				else rebuild();
				percent=loadingPercent*0.05f+rebuildingPercent*0.95f;
				return (int)(percent*100);
			}
			private void load(){
				for(int i = 0; i<64; i++){
					world.loadChunk(x, 0, z);
					z++;
					if(z>chunkLimit){
						z=0;
						x++;
						if(x>chunkLimit){
							loadingPercent=1;
							return;
						}
					}
				}
				loadingPercent=(x*chunkLimit+z)/(float)(chunkLimit*chunkLimit);
			}
			private void rebuild(){
				for(int i = 0; i<64; i++){
					world.getChunk(w).rebuild();
					w++;
					rebuildingPercent=w/(float)world.getChunkCount();
				}
			}
		}, new Runnable(){
			public void run(){
				loadingScreen=null;
				MatrixUtils.setupPerspective(70, Loop.screenRes.width/(float)Loop.screenRes.height, Loop.CAMERA_NEAR_CLIP, 1000);
				GL11.glClearColor(219/255f, 246/255f, 251/255f, 0);
				MainLoop.FPS_SYNC=true;
			}
		});
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
		}
	}
	private void setupCameraPosition(){
		float center = (BuildingCreator.WORLD_BOUNDS_SIZE-1)/2f;
		camera.goalX=camera.x=center;
		camera.goalY=camera.y=5;
		camera.goalZ=camera.z=center;
		camera.cameraMoveSpeed=3.75f;
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
			inventory.render();
			MatrixUtils.setupPerspective(70, Loop.screenRes.width/(float)Loop.screenRes.height, Loop.CAMERA_NEAR_CLIP, 1000);
		}
		if(removePalette){
			palleteRenderer.dispose();
			palleteRenderer=null;
			removePalette=false;
			glfwSetCursorPos(buildingCreator.getWindow(), screenRes.width/2.0, screenRes.height/2.0);
			glfwSetInputMode(buildingCreator.getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
			GL11.glClearColor(219/255f, 246/255f, 251/255f, 0);
		}
		if(createPalette){
			palleteRenderer=new PalleteRenderer();
			createPalette=false;
			glfwSetInputMode(buildingCreator.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
			GL11.glClearColor(0, 0, 0, 0);
		}
	}
	public void mouseMove(long window, double x, double y){
		if(hasPalette())palleteRenderer.onMouseMove(x, y);
		else if(inventory.isShown())inventory.onMouseMove(x, y);
		else inputController.processMouse(x, y);
	}
	public void dispose(){
		world.clearAll();
		guiHandler.cleanUp();
		Texture.disposeAll();
		INSTANCE=null;
	}
	private DoubleBuffer mouseBufferX = BufferUtils.createDoubleBuffer(1);
	private DoubleBuffer mouseBufferY = BufferUtils.createDoubleBuffer(1);
	public void mouse(long window, int button, int action){
		if(hasPalette()){
			if(action==GLFW_PRESS){
				glfwGetCursorPos(window, mouseBufferX, mouseBufferY);
				palleteRenderer.onMouseDown(mouseBufferX.get(0), mouseBufferY.get(0));
			}else if(action==GLFW_RELEASE)palleteRenderer.onMouseUp(button);
		}else if(inventory.isShown()){
			if(action==GLFW_PRESS){
				glfwGetCursorPos(window, mouseBufferX, mouseBufferY);
				inventory.onMouseDown(mouseBufferX.get(0), mouseBufferY.get(0));
			}else if(action==GLFW_RELEASE)inventory.onMouseUp();
		}else userBlockHandler.mouseClick(button, action);
	}
	public void key(long window, int key, int action){
		if(key==GLFW_KEY_C&&action==GLFW_PRESS&&!hasPalette()){
			inventory.setShown(!inventory.isShown());
			return;
		}
		if(inventory.isShown())return;
		inputController.onKey(window, key, action);
	}
	public void disposePalette(){ removePalette=true; }
	public void setPalette(){ createPalette=true; }
	public boolean hasPalette(){ return palleteRenderer!=null; }
	public GuiHandler getGuiHandler(){ return guiHandler; }
	public void mouseWheel(long window, double xPos, double yPos){ inputController.mouseWheel(yPos); }
	public Camera getCamera(){ return camera; }
	public VoxelWorld getVoxelWorld(){ return world; }
	public BuildingCreator getBuildingCreator(){ return buildingCreator; }
	public Inventory getInventory(){ return inventory; }
	public long getWindow(){ return buildingCreator.getWindow(); }
	public InputController getInputController(){ return inputController; }
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
}