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
	public static final boolean ISOMETRIC = true;
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
			CatcheChunkLoader chunkLoader;
			long lastPrint, time;
			int frames;
			private static final int CHUNK_UPDATES_PER_SECOND = 4096;
			public void preLoop(){
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glCullFace(GL11.GL_BACK);
				BlockTextures.genTextures();
				if(ISOMETRIC){
					cam=new Camera(120, 0.3f, 1000);
					cam.goalY=cam.y=100;
				}else{
					cam=new Camera(70, screenRes.width/(float)screenRes.height, 0.1f, 1000);
					cam.goalY=cam.y=40;
				}
				cam.goalRX=cam.rx=45;
				cam.cameraSpeed=5;
				voxelWorld=new VoxelWorld(chunkLoader=new CatcheChunkLoader());
				chunkLoader.setup(voxelWorld, cam);
				VoxelChunkQue.setupTextures();
				inputHandler=new InputHandler(cam, loop.getWindow());
				if(ISOMETRIC)inputHandler.moveSpeed=30;
				lastPrint=System.currentTimeMillis();
			}
			public void render(){
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
				voxelWorld.render();
				time=System.currentTimeMillis();
				frames++;
				if(time-lastPrint>=1000){
					System.out.println("FPS: "+frames);
					lastPrint=time;
					frames=0;
				}
			}
			public void update(float delta, long time){
				inputHandler.update(voxelWorld, delta);
				cam.update(delta, time);
				chunkLoader.update(Math.max((int)(CHUNK_UPDATES_PER_SECOND*delta), 1));
			}
			public void key(long window, int key, int action){ inputHandler.onKey(window, key, action); }
		};
		loop.create(init);
	}
}