package wraithaven.conquest.client.BuildingCreator;

import org.lwjgl.opengl.GL11;
import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.LoopObjective;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraithaven.conquest.client.BlockTextures;

public class Loop implements LoopObjective{
	private Camera camera;
	private VoxelWorld world;
	private BuildCreatorWorld creatorWorld;
	private InputController inputController;
	private UserBlockHandler userBlockHandler;
	private final float aspect;
	public void preLoop(){
		camera=new Camera(70, aspect, 0.1f, 1000, false);
		BlockTextures.genTextures();
		creatorWorld=new BuildCreatorWorld();
		world=new VoxelWorld(creatorWorld, false);
		creatorWorld.setup(camera);
		inputController=new InputController(camera, BuildingCreator.loop.getWindow());
		userBlockHandler=new UserBlockHandler(world, camera);
		generateWorld();
		setupCameraPosition();
		setupOGL();
	}
	public void update(double delta, double time){
		inputController.processWalk(world, delta);
		camera.update(delta, time);
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
		camera.goalY=camera.y=center;
		camera.goalX=camera.x=center;
	}
	public void mouseMove(long window, double x, double y){ inputController.processMouse(x, y); }
	public Loop(float aspect){ this.aspect=aspect; }
	public void render(){ world.render(); }
	public void mouse(long window, int button, int action){ userBlockHandler.mouseClick(button, action); }
	public void key(long window, int key, int action){ inputController.onKey(window, key, action); }
	private static void setupOGL(){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
}