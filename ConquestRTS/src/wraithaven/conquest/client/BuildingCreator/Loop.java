package wraithaven.conquest.client.BuildingCreator;

import org.lwjgl.opengl.GL11;
import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.LoopObjective;
import wraith.library.LWJGL.Voxel.VoxelWorld;

public class Loop implements LoopObjective{
	private Camera camera;
	private VoxelWorld world;
	private BuildCreatorWorld creatorWorld;
	private InputController inputController;
	private final float aspect;
	public void preLoop(){
		camera=new Camera(70, aspect, 0.3f, 100, false);
		creatorWorld=new BuildCreatorWorld();
		world=new VoxelWorld(creatorWorld, false);
		creatorWorld.setup(world, camera);
		inputController=new InputController(camera, BuildingCreator.loop.getWindow());
		generateWorld();
		setupOGL();
	}
	public void update(float delta, long time){
		inputController.update(world, delta);
		camera.update(delta, time);
	}
	public void render(){
		world.render();
	}
	private void generateWorld(){
		int chunkLimit = BuildingCreator.WORLD_BOUNDS_SIZE>>4;
		int x, y, z;
		for(x=0; x<=chunkLimit; x++)for(y=0; y<=chunkLimit; y++)for(z=0; z<=chunkLimit; z++)world.getChunk(x, y, z);
	}
	public Loop(float aspect){ this.aspect=aspect; }
	public void mouse(long window, int button, int action){ inputController.onMouse(button, action); }
	public void key(long window, int key, int action){ inputController.onKey(window, key, action); }
	private static void setupOGL(){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
}