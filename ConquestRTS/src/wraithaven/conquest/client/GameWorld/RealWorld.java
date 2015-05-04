package wraithaven.conquest.client.GameWorld;

import java.awt.Dimension;
import java.awt.Toolkit;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldBounds;
import wraithaven.conquest.client.GameWorld.LoopControls.WindowInitalizer;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.GameWorld.LoopControls.MainLoop;
import wraithaven.conquest.client.GameWorld.LoopControls.LoopObjective;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;

public class RealWorld{
	public static VoxelWorld voxelWorld;
	public static final boolean DEBUG = false;
	private static float ZOOM_LEVEL = 0.03f;
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
				cam=new Camera(screenRes.width*ZOOM_LEVEL, screenRes.height*ZOOM_LEVEL, -1000, 1000, true);
				cam.goalY=cam.y=100;
				cam.goalRX=cam.rx=30;
				cam.goalRX=cam.ry=45;
				cam.cameraRotationSpeed=3.75f;
				cam.cameraMoveSpeed=3.75f;
				voxelWorld=new VoxelWorld(chunkLoader=new CatcheChunkLoader(), new VoxelWorldBounds(-10000, 0, -10000, 10000, 16, 10000));
				chunkLoader.setup(voxelWorld, cam);
				VoxelChunkQue.setupTextures(voxelWorld);
				inputHandler=new InputHandler(cam, voxelWorld);
				inputHandler.moveSpeed=30;
				lastPrint=System.currentTimeMillis();
			}
			public void render(){
				voxelWorld.render();
				if(DEBUG){
					time=System.currentTimeMillis();
					frames++;
					if(time-lastPrint>=1000){
						System.out.println("FPS: "+frames);
						lastPrint=time;
						frames=0;
					}
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
			public void update(double delta, double time){
				inputHandler.processWalk(delta);
				cam.update(delta);
				chunkLoader.update(Math.max((int)(CHUNK_UPDATES_PER_SECOND*delta), 1), time);
				voxelWorld.setNeedsRebatch();
			}
			public void mouseWheel(long window, double xPos, double yPos){
				ZOOM_LEVEL=(float)Math.max(ZOOM_LEVEL-yPos*0.001, 0.01);
				MatrixUtils.setupOrtho(screenRes.width*ZOOM_LEVEL, screenRes.height*ZOOM_LEVEL, -1000, 1000);
			}
			public void key(long window, int key, int action){ inputHandler.onKey(window, key, action); }
			public void mouse(long window, int button, int action){}
			public void mouseMove(long window, double xpos, double ypos){}
		};
		loop.create(init);
	}
}