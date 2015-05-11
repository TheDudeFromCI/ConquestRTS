package wraithaven.conquest.client.BuildingCreator;

import java.io.File;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;
import wraithaven.conquest.client.GameWorld.Sphere;
import wraithaven.conquest.client.GameWorld.BoundingBox;
import wraithaven.conquest.client.GameWorld.Voxel.Block;
import wraithaven.conquest.client.ClientLauncher;
import static org.lwjgl.glfw.GLFW.*;

public class InputController{
	private boolean w, a, s, d, shift, space;
	private int x, y, z;
	private int bcx1, bcy1, bcz1, bcx2, bcy2, bcz2;
	private float currentCamX, currentCamY, currentCamZ;
	public float mouseSensitivity = 0.1f;
	public float moveSpeed = 10.8f;
	private Block block;
	private final Loop loop;
	private final Sphere cameraSphere = new Sphere();
	private final BoundingBox boundingBox = new BoundingBox();
	private final Camera cam;
	private final long window;
	private final IntBuffer screenWidth = BufferUtils.createIntBuffer(1);
	private final IntBuffer screenHeight = BufferUtils.createIntBuffer(1);
	private final BuildingCreator buildingCreator;
	public static final float CAMERA_RADIUS = 0.41f;
	public InputController(BuildingCreator buildingCreator, Camera cam, long window, Loop loop){
		this.cam=cam;
		this.window=window;
		this.buildingCreator=buildingCreator;
		this.loop=loop;
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		glfwGetWindowSize(window, screenWidth, screenHeight);
		screenWidth.put(0, screenWidth.get(0)/2);
		screenHeight.put(0, screenHeight.get(0)/2);
		cameraSphere.r=CAMERA_RADIUS;
	}
	public void onKey(long window, int key, int action){
		if(key==GLFW.GLFW_KEY_F12&&action==GLFW.GLFW_RELEASE)GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE);
		if(loop.hasPalette()){
			if(key==GLFW.GLFW_KEY_F5&&action==GLFW.GLFW_PRESS)loop.disposePalette();
			return;
		}
		if(key==GLFW.GLFW_KEY_F12&&action==GLFW.GLFW_RELEASE)GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE);
		if(key==GLFW.GLFW_KEY_W){
			if(action==GLFW.GLFW_PRESS)w=true;
			else if(action==GLFW.GLFW_RELEASE)w=false;
		}
		if(key==GLFW.GLFW_KEY_A){
			if(action==GLFW.GLFW_PRESS)a=true;
			else if(action==GLFW.GLFW_RELEASE)a=false;
		}
		if(key==GLFW.GLFW_KEY_S){
			if(action==GLFW.GLFW_PRESS)s=true;
			else if(action==GLFW.GLFW_RELEASE)s=false;
		}
		if(key==GLFW.GLFW_KEY_D){
			if(action==GLFW.GLFW_PRESS)d=true;
			else if(action==GLFW.GLFW_RELEASE)d=false;
		}
		if(key==GLFW.GLFW_KEY_LEFT_SHIFT){
			if(action==GLFW.GLFW_PRESS)shift=true;
			else if(action==GLFW.GLFW_RELEASE)shift=false;
		}
		if(key==GLFW.GLFW_KEY_SPACE){
			if(action==GLFW.GLFW_PRESS)space=true;
			else if(action==GLFW.GLFW_RELEASE)space=false;
		}
		if(key==GLFW.GLFW_KEY_F1){
			if(action==GLFW.GLFW_PRESS)MatrixUtils.takeScreenShot(new File(ClientLauncher.screenShotFolder,  System.currentTimeMillis()+".png"), buildingCreator.getInit().width, buildingCreator. getInit().height);
		}
		if(key==GLFW.GLFW_KEY_F5&&action==GLFW.GLFW_PRESS)loop.setPalette();
		if(key==GLFW.GLFW_KEY_E&&action==GLFW.GLFW_PRESS){
			if(loop.getGuiHandler().getHotbarSelectorId()<10)loop.getGuiHandler().updateHotbarSelector((loop.getGuiHandler().getHotbarSelectorId()+1)%10);
			else loop.getGuiHandler().updateHotbarSelector((loop.getGuiHandler().getHotbarSelectorId()%10+1)%10+10);
		}
		if(key==GLFW.GLFW_KEY_Q&&action==GLFW.GLFW_PRESS){
			if(loop.getGuiHandler().getHotbarSelectorId()<10)loop.getGuiHandler().updateHotbarSelector((loop.getGuiHandler().getHotbarSelectorId()-1+10)%10);
			else loop.getGuiHandler().updateHotbarSelector((loop.getGuiHandler().getHotbarSelectorId()%10-1+10)%10+10);
		}
		if(key==GLFW.GLFW_KEY_TAB&&action==GLFW.GLFW_PRESS){
			if(loop.getGuiHandler().getHotbarSelectorId()<10)loop.getGuiHandler().updateHotbarSelector(10);
			else loop.getGuiHandler().updateHotbarSelector(0);
		}
	}
	public void processMouse(double x, double y){
		cam.goalRY=cam.ry+=(x-screenWidth.get(0))*mouseSensitivity;
		cam.goalRX=cam.rx=(float)Math.max(Math.min(cam.rx+(y-screenHeight.get(0))*mouseSensitivity, 90), -90);
		glfwSetCursorPos(window, screenWidth.get(0), screenHeight.get(0));
	}
	public void processWalk(VoxelWorld world, double delta){
		delta*=moveSpeed;
		currentCamX=cam.goalX;
		currentCamY=cam.goalY;
		currentCamZ=cam.goalZ;
		if(w)currentCamX+=delta*(float)Math.sin(Math.toRadians(cam.goalRY));
		if(a)currentCamX+=delta*(float)Math.sin(Math.toRadians(cam.goalRY-90));
		if(s)currentCamX-=delta*(float)Math.sin(Math.toRadians(cam.goalRY));
		if(d)currentCamX+=delta*(float)Math.sin(Math.toRadians(cam.goalRY+90));
		if(canMoveTo(world, currentCamX, currentCamY, currentCamZ))cam.goalX=currentCamX;
		currentCamX=cam.goalX;
		if(w)currentCamZ-=delta*(float)Math.cos(Math.toRadians(cam.goalRY));
		if(a)currentCamZ-=delta*(float)Math.cos(Math.toRadians(cam.goalRY-90));
		if(s)currentCamZ+=delta*(float)Math.cos(Math.toRadians(cam.goalRY));
		if(d)currentCamZ-=delta*(float)Math.cos(Math.toRadians(cam.goalRY+90));
		if(canMoveTo(world, currentCamX, currentCamY, currentCamZ))cam.goalZ=currentCamZ;
		currentCamZ=cam.goalZ;
		if(shift)currentCamY-=delta;
		if(space)currentCamY+=delta;
		if(canMoveTo(world, currentCamX, currentCamY, currentCamZ))cam.goalY=currentCamY;
	}
	public void mouseWheel(double yPos){
		yPos=Math.round(yPos);
		if(yPos>0){
			for(int i = 0; i<yPos; i++){
				if(loop.getGuiHandler().getHotbarSelectorId()<10)loop.getGuiHandler().updateHotbarSelector((loop.getGuiHandler().getHotbarSelectorId()-1+10)%10);
				else loop.getGuiHandler().updateHotbarSelector((loop.getGuiHandler().getHotbarSelectorId()%10-1+10)%10+10);
			}
		}else{
			for(int i = 0; i>yPos; i--){
				if(loop.getGuiHandler().getHotbarSelectorId()<10)loop.getGuiHandler().updateHotbarSelector((loop.getGuiHandler().getHotbarSelectorId()+1)%10);
				else loop.getGuiHandler().updateHotbarSelector((loop.getGuiHandler().getHotbarSelectorId()%10+1)%10+10);
			}
		}
	}
	private boolean canMoveTo(VoxelWorld world, float sx, float sy, float sz){
		if(sx<0||sy<0||sz<0||sx>=BuildingCreator.WORLD_BOUNDS_SIZE||sz>=BuildingCreator.WORLD_BOUNDS_SIZE||sz>=BuildingCreator.WORLD_BOUNDS_SIZE)return false;
		cameraSphere.x=sx;
		cameraSphere.y=sy;
		cameraSphere.z=sz;
		bcx1=(int)Math.floor(sx)-1;
		bcy1=(int)Math.floor(sy)-1;
		bcz1=(int)Math.floor(sz)-1;
		bcx2=bcx1+2;
		bcy2=bcy1+2;
		bcz2=bcz1+2;
		for(x=bcx1; x<=bcx2; x++){
			for(y=bcy1; y<=bcy2; y++){
				for(z=bcz1; z<=bcz2; z++){
					block=world.getBlock(x, y, z, false);
					if(block==null)continue;
					boundingBox.x1=block.x;
					boundingBox.y1=block.y;
					boundingBox.z1=block.z;
					boundingBox.x2=block.x+1;
					boundingBox.y2=block.y+1;
					boundingBox.z2=block.z+1;
					if(intersectsWith(boundingBox, cameraSphere))return false;
				}
			}
		}
		return true;
	}
	private static boolean intersectsWith(BoundingBox bb, Sphere sphere){
		float dmin = 0;
		if(sphere.x<bb.x1)dmin+=Math.pow(sphere.x-bb.x1, 2);
		else if(sphere.x>bb.x2)dmin+=Math.pow(sphere.x-bb.x2, 2);
		if(sphere.y<bb.y1)dmin+=Math.pow(sphere.y-bb.y1, 2);
		else if(sphere.y>bb.y2)dmin+=Math.pow(sphere.y-bb.y2, 2);
		if(sphere.z<bb.z1)dmin+=Math.pow(sphere.z-bb.z1, 2);
		else if(sphere.z>bb.z2)dmin+=Math.pow(sphere.z-bb.z2, 2);
		return dmin<=Math.pow(sphere.r, 2);
	}
}