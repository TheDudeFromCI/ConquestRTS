package wraithaven.conquest.client.BuildingCreator;

import java.awt.Dimension;
import java.io.File;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldBounds;
import wraithaven.conquest.client.GameWorld.LoopControls.LoopObjective;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.PalleteRenderer;
import wraithaven.conquest.client.GameWorld.BlockTextures;
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
	public static Dimension screenRes;
	private BuildingCreator buildingCreator;
	private PalleteRenderer palleteRenderer;
	private boolean removePalette, createPalette;
	public static float ISO_ZOOM = 0.12f;
	public static final float CAMERA_NEAR_CLIP = 0.05f;
	public Loop(Dimension screenRes, BuildingCreator buildingCreator){
		Loop.screenRes=screenRes;
		this.buildingCreator=buildingCreator;
	}
	public void preLoop(){
		camera=new Camera(70, screenRes.width/(float)screenRes.height, CAMERA_NEAR_CLIP, 1000, false);
		BlockTextures.genTextures();
		creatorWorld=new BuildCreatorWorld();
		world=new VoxelWorld(creatorWorld, new VoxelWorldBounds(0, 0, 0, BuildingCreator.WORLD_BOUNDS_SIZE-1, BuildingCreator.WORLD_BOUNDS_SIZE-1, BuildingCreator.WORLD_BOUNDS_SIZE-1));
		creatorWorld.setup(world, camera);
		inputController=new InputController(buildingCreator, camera, buildingCreator.getWindow(), this);
		userBlockHandler=new UserBlockHandler(this, world, camera);
		guiHandler=new GuiHandler();
		skybox=new Skybox(new Texture(new File(ClientLauncher.assetFolder, "Day Skybox.png"), 0, null));
		selectedBlock=new SelectedBlock(camera, world);
		generateWorld();
		setupCameraPosition();
		setupOGL();
	}
	public void update(double delta, double time){
		if(palleteRenderer!=null)palleteRenderer.update(delta, time);
		else{
			inputController.processWalk(world, delta);
			GL11.glPushMatrix();
			camera.update(delta);
			userBlockHandler.update(time);
			world.setNeedsRebatch();
		}
	}
	private void setupCameraPosition(){
		float center = (BuildingCreator.WORLD_BOUNDS_SIZE-1)/2f;
		camera.goalX=camera.x=center;
		camera.goalY=camera.y=5;
		camera.goalZ=camera.z=center;
		camera.cameraRotationSpeed=3.75f;
		camera.cameraMoveSpeed=3.75f;
	}
	public void render(){
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
		}
		if(removePalette){
			palleteRenderer=null;
			removePalette=false;
			glfwSetCursorPos(buildingCreator.getWindow(), screenRes.width/2.0, screenRes.height/2.0);
			glfwSetInputMode(buildingCreator.getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		}
		if(createPalette){
			palleteRenderer=new PalleteRenderer();
			createPalette=false;
			glfwSetInputMode(buildingCreator.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}
	}
	private void generateWorld(){
		int chunkLimit = (BuildingCreator.WORLD_BOUNDS_SIZE-1)>>Chunk.CHUNK_BITS;
		int x, y, z;
		for(x=0; x<=chunkLimit; x++)for(y=0; y<=chunkLimit; y++)for(z=0; z<=chunkLimit; z++)world.loadChunk(x, y, z);
	}
	public void mouseMove(long window, double x, double y){
		if(hasPalette())palleteRenderer.onMouseMove(x, y);
		else inputController.processMouse(x, y);
	}
	private DoubleBuffer mouseBufferX = BufferUtils.createDoubleBuffer(1);
	private DoubleBuffer mouseBufferY = BufferUtils.createDoubleBuffer(1);
	public void mouse(long window, int button, int action){
		if(hasPalette()){
			if(action==GLFW_PRESS){
				glfwGetCursorPos(window, mouseBufferX, mouseBufferY);
				palleteRenderer.onMouseDown(mouseBufferX.get(0), mouseBufferY.get(0));
			}else if(action==GLFW_RELEASE)palleteRenderer.onMouseUp();
		}else userBlockHandler.mouseClick(button, action);
	}
	public void key(long window, int key, int action){ inputController.onKey(window, key, action); }
	public void disposePalette(){ removePalette=true; }
	public void setPalette(){ createPalette=true; }
	public boolean hasPalette(){ return palleteRenderer!=null; }
	public GuiHandler getGuiHandler(){ return guiHandler; }
	public void mouseWheel(long window, double xPos, double yPos){ inputController.mouseWheel(yPos); }
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