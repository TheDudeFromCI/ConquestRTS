package wraithaven.conquest.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import org.lwjgl.opengl.GL11;
import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.LoopObjective;
import wraith.library.LWJGL.MainLoop;
import wraith.library.LWJGL.WindowInitalizer;
import wraith.library.LWJGL.Voxel.VoxelWorld;

public class Test{
	public static void main(String[] args){
		final MainLoop loop = new MainLoop();
		WindowInitalizer init = new WindowInitalizer();
		final Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();
		init.width=screenRes.width;
		init.height=screenRes.height;
		init.fullscreen=true;
		init.windowName="Test";
		init.loopObjective=new LoopObjective(){
			Camera cam;
			VoxelWorld voxelWorld;
			InputHandler inputHandler;
			public void preLoop(){
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glCullFace(GL11.GL_BACK);
				BlockTextures.genTextures();
				//First person view.
				cam=new Camera(70, screenRes.width/(float)screenRes.height, 0.1f, 1000);
				//Iso view.
				//cam=new Camera(60, 0.3f, 1000);
				cam.goalY=cam.y=40;
				cam.goalRX=cam.rx=45;
				cam.goalRY=cam.ry=45;
				cam.cameraSpeed=5;
				WorldGenerator worldGen =new WorldGenerator(cam);
				voxelWorld=new VoxelWorld(worldGen);
				worldGen.setWorld(voxelWorld);
				inputHandler=new InputHandler(cam, loop.getWindow());
			}
			public void render(){
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
				cam.translateInvertMatrix();
				voxelWorld.render();
			}
			public void update(float delta, long time){
				inputHandler.update(voxelWorld, delta);
				cam.update(delta, time);
				inputHandler.updateChunks(voxelWorld);
			}
			public void key(long window, int key, int action){ inputHandler.onKey(window, key, action); }
		};
		loop.create(init);
	}
}