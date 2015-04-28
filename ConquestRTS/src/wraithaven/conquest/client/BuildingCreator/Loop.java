package wraithaven.conquest.client.BuildingCreator;

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
		creatorWorld.setup(world);
		inputController=new InputController(camera, BuildingCreator.loop.getWindow());
	}
	public void update(float delta, long time){
		inputController.update(world, delta);
		camera.update(delta, time);
	}
	public void render(){
		world.render();
	}
	public void key(long window, int key, int action){
	}
	public void mouse(long window, int button, int action){
	}
	public Loop(float aspect){ this.aspect=aspect; }
}