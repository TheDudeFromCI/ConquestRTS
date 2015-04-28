package wraithaven.conquest.client;

import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.LoopObjective;
import wraith.library.LWJGL.MainLoop;
import wraith.library.LWJGL.WindowInitalizer;
import wraith.library.LWJGL.Voxel.VoxelWorld;

public class BuildingCreator{
	public static void main(String[] args){
		final MainLoop loop = new MainLoop();
		WindowInitalizer init = new WindowInitalizer();
		init.height=500;
		init.width=500;
		init.windowName="Building Creator";
		init.loopObjective=new LoopObjective(){
			Camera cam;
			VoxelWorld world;
			public void preLoop(){
				cam=new Camera(70, 1, 0.3f, 1000, false);
				BuildCreatorWorld buildingCreator = new BuildCreatorWorld();
				world=new VoxelWorld(buildingCreator, false);
				buildingCreator.setup(world);
			}
			public void update(float delta, long time){
				cam.update(delta, time);
			}
			public void render(){
			}
			public void key(long window, int key, int action){
			}
			public void mouse(long window, int button, int action){
			}
		};
		loop.create(init);
	}
}