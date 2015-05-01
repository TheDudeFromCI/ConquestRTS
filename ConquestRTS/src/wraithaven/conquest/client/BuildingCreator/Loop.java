package wraithaven.conquest.client.BuildingCreator;

import java.awt.Dimension;
import org.lwjgl.opengl.GL11;
import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.LoopObjective;
import wraith.library.LWJGL.MatrixUtils;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.LWJGL.Voxel.VoxelWorldBounds;
import wraithaven.conquest.client.GameWorld.BlockTextures;

public class Loop implements LoopObjective{
	private Camera camera;
	@SuppressWarnings("unused")private float aspect;
	private VoxelWorld world;
	private BuildCreatorWorld creatorWorld;
	private InputController inputController;
	private UserBlockHandler userBlockHandler;
	private GuiHandler guiHandler;
	private final Dimension screenRes;
	public static final float ISO_ZOOM = 0.12f;
	public void preLoop(){
		camera=new Camera(70, aspect=(screenRes.width/(float)screenRes.height), 0.15f, 1000, false);
		BlockTextures.genTextures();
		creatorWorld=new BuildCreatorWorld();
		world=new VoxelWorld(creatorWorld, new VoxelWorldBounds(0, 0, 0, BuildingCreator.WORLD_BOUNDS_SIZE-1, BuildingCreator.WORLD_BOUNDS_SIZE-1, BuildingCreator.WORLD_BOUNDS_SIZE-1));
		creatorWorld.setup(world, camera);
		inputController=new InputController(camera, BuildingCreator.loop.getWindow(), screenRes);
		userBlockHandler=new UserBlockHandler(world, camera, inputController);
		guiHandler=new GuiHandler(screenRes);
		generateWorld();
		setupCameraPosition();
		setupOGL();
	}
	public void update(double delta, double time){
		inputController.processWalk(world, delta);
		GL11.glPushMatrix();
		camera.update(delta, time);
		userBlockHandler.update(time);
		world.setNeedsRebatch();
	}
	private void generateWorld(){
		int chunkLimit = (BuildingCreator.WORLD_BOUNDS_SIZE-1)>>4;
		int x, y, z;
		for(x=0; x<=chunkLimit; x++)for(y=0; y<=chunkLimit; y++)for(z=0; z<=chunkLimit; z++)world.loadChunk(x, y, z);
	}
	private void setupCameraPosition(){
		float center = (BuildingCreator.WORLD_BOUNDS_SIZE-1)/2f;
		camera.goalX=camera.x=center;
		camera.goalY=camera.y=5;
		camera.goalZ=camera.z=center;
		camera.cameraSpeed=3;
	}
	public void render(){
		world.render();
		GL11.glPopMatrix();
		if(!inputController.iso){
			guiHandler.render();
			if(inputController.iso)MatrixUtils.setupOrtho(screenRes.width*ISO_ZOOM, screenRes.height*ISO_ZOOM, -1000, 1000);
			else MatrixUtils.setupPerspective(70, screenRes.width/(float)screenRes.height, 0.15f, 1000);
		}
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
	}
	public Loop(Dimension screenRes){ this.screenRes=screenRes; }
	public void mouseMove(long window, double x, double y){ inputController.processMouse(x, y); }
	public void mouse(long window, int button, int action){ userBlockHandler.mouseClick(button, action); }
	public void key(long window, int key, int action){ inputController.onKey(window, key, action); }
	private static void setupOGL(){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
}