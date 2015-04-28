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
	public static VoxelWorld voxelWorld;
	public static final boolean ISOMETRIC = true;
	public static final boolean DEBUG = true;
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
					cam=new Camera(screenRes.width*0.06f, screenRes.height*0.06f, -1000, 1000, true);
					cam.goalY=cam.y=100;
				}else{
					cam=new Camera(70, screenRes.width/(float)screenRes.height, 0.1f, 1000, false);
					cam.goalY=cam.y=40;
				}
				cam.goalRX=cam.rx=30;
				cam.cameraSpeed=5;
				voxelWorld=new VoxelWorld(chunkLoader=new CatcheChunkLoader(), true);
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
					if(DEBUG)System.out.println("FPS: "+frames);
					lastPrint=time;
					frames=0;
				}
				if(DEBUG){
					GL11.glBegin(GL11.GL_LINES);
					GL11.glColor3f(1, 0, 0);
					GL11.glVertex3f(cam.x, cam.y-2, cam.z);
					GL11.glColor3f(1, 0, 0);
					GL11.glVertex3f(cam.x+5, cam.y-2, cam.z);
					GL11.glColor3f(0, 0, 1);
					GL11.glVertex3f(cam.x, cam.y-2, cam.z);
					GL11.glColor3f(0, 0, 1);
					GL11.glVertex3f(cam.x, cam.y-2, cam.z+5);
					GL11.glEnd();
				}
			}
			public void update(float delta, long time){
				inputHandler.update(voxelWorld, delta);
				cam.update(delta, time);
				chunkLoader.update(Math.max((int)(CHUNK_UPDATES_PER_SECOND*delta), 1));
			}
			public void key(long window, int key, int action){ inputHandler.onKey(window, key, action); }
			public void mouse(long window, int button, int action){ inputHandler.onMouse(window, button, action); }
		};
		loop.create(init);
	}
}