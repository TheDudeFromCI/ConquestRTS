package wraithaven.conquest.client.BuildingCreator;

import java.io.File;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.IndexManager;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.BoundingBox;
import wraithaven.conquest.client.GameWorld.Sphere;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;

public class InputController{
	public static final float CAMERA_RADIUS = 0.41f;
	private static boolean intersectsWith(BoundingBox bb, Sphere sphere){
		float dmin = 0;
		if(sphere.x<bb.x1) dmin += Math.pow(sphere.x-bb.x1, 2);
		else if(sphere.x>bb.x2) dmin += Math.pow(sphere.x-bb.x2, 2);
		if(sphere.y<bb.y1) dmin += Math.pow(sphere.y-bb.y1, 2);
		else if(sphere.y>bb.y2) dmin += Math.pow(sphere.y-bb.y2, 2);
		if(sphere.z<bb.z1) dmin += Math.pow(sphere.z-bb.z1, 2);
		else if(sphere.z>bb.z2) dmin += Math.pow(sphere.z-bb.z2, 2);
		return dmin<=Math.pow(sphere.r, 2);
	}
	private int bcx1, bcy1, bcz1, bcx2, bcy2, bcz2;
	private final BoundingBox boundingBox = new BoundingBox();
	private final Sphere cameraSphere = new Sphere();
	private float currentCamX, currentCamY, currentCamZ;
	public float mouseSensitivity = 0.1f;
	public float moveSpeed = 10.8f;
	private final IntBuffer screenHeight = BufferUtils.createIntBuffer(1);
	private final IntBuffer screenWidth = BufferUtils.createIntBuffer(1);
	private boolean w, a, s, d, shift, space, rotator;
	public boolean wireframeMode, cullFace;
	private int x, y, z;
	public InputController(){
		GLFW.glfwSetInputMode(Loop.INSTANCE.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
		GLFW.glfwGetWindowSize(Loop.INSTANCE.getWindow(), screenWidth, screenHeight);
		screenWidth.put(0, screenWidth.get(0)/2);
		screenHeight.put(0, screenHeight.get(0)/2);
		cameraSphere.r = InputController.CAMERA_RADIUS;
	}
	private boolean canMoveTo(VoxelWorld world, float sx, float sy, float sz){
		if(sx<0||sy<0||sz<0||sx>=BuildingCreator.WORLD_BOUNDS_SIZE||sy>=BuildingCreator.WORLD_BOUNDS_SIZE||sz>=BuildingCreator.WORLD_BOUNDS_SIZE)return false;
		cameraSphere.x = sx;
		cameraSphere.y = sy;
		cameraSphere.z = sz;
		bcx1 = (int)Math.floor(sx)-1;
		bcy1 = (int)Math.floor(sy)-1;
		bcz1 = (int)Math.floor(sz)-1;
		bcx2 = bcx1+2;
		bcy2 = bcy1+2;
		bcz2 = bcz1+2;
		for(x = bcx1; x<=bcx2; x++){
			for(y = bcy1; y<=bcy2; y++){
				for(z = bcz1; z<=bcz2; z++){
					if(world.getBlock(x, y, z, false)==IndexManager.AIR_BLOCK)continue;
					boundingBox.x1 = x;
					boundingBox.y1 = y;
					boundingBox.z1 = z;
					boundingBox.x2 = x+1;
					boundingBox.y2 = y+1;
					boundingBox.z2 = z+1;
					if(InputController.intersectsWith(boundingBox, cameraSphere))return false;
				}
			}
		}
		return true;
	}
	public void mouseWheel(double yPos){
		yPos = Math.round(yPos);
		if(yPos>0){
			for(int i = 0; i<yPos; i++){
				if(rotator)Loop.INSTANCE.getGuiHandler().goalWheelRotation--;
				else{
					if(Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()<10) Loop.INSTANCE.getGuiHandler().updateHotbarSelector((Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()-1+10)%10);
					else Loop.INSTANCE.getGuiHandler().updateHotbarSelector((Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()%10-1+10)%10+10);
				}
			}
		}else{
			for(int i = 0; i>yPos; i--){
				if(rotator)Loop.INSTANCE.getGuiHandler().goalWheelRotation++;
				else{
					if(Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()<10) Loop.INSTANCE.getGuiHandler().updateHotbarSelector((Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()+1)%10);
					else Loop.INSTANCE.getGuiHandler().updateHotbarSelector((Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()%10+1)%10+10);
				}
			}
		}
	}
	public void onKey(int key, int action){
		if(key==GLFW.GLFW_KEY_F1){
			if(action==GLFW.GLFW_PRESS)MatrixUtils.takeScreenShot(new File(ClientLauncher.screenShotFolder, System.currentTimeMillis()+".png"), Loop.INSTANCE.getBuildingCreator().getInit().width, Loop.INSTANCE.getBuildingCreator().getInit().height);
			return;
		}
		if(key==GLFW.GLFW_KEY_ESCAPE&&action==GLFW.GLFW_PRESS){
			if(!Loop.INSTANCE.hasPalette())Loop.INSTANCE.getGuiHandler().togglePauseMenu();
			return;
		}
		if(Loop.INSTANCE.getGuiHandler().isPaused())return;
		if(Loop.INSTANCE.hasPalette()){
			if(key==GLFW.GLFW_KEY_F5&&action==GLFW.GLFW_PRESS)Loop.INSTANCE.disposePalette();
			return;
		}
		if(key==GLFW.GLFW_KEY_P&&action==GLFW.GLFW_PRESS){
			if(wireframeMode){
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				if(cullFace){
					GL11.glEnable(GL11.GL_CULL_FACE);
					cullFace = true;
				}
			}else{
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				GL11.glDisable(GL11.GL_CULL_FACE);
				cullFace = false;
			}
			wireframeMode = !wireframeMode;
			return;
		}
		if(key==GLFW.GLFW_KEY_O&&action==GLFW.GLFW_PRESS){
			if(!wireframeMode) return;
			if(cullFace) GL11.glEnable(GL11.GL_CULL_FACE);
			else GL11.glDisable(GL11.GL_CULL_FACE);
			cullFace = !cullFace;
			return;
		}
		if(key==GLFW.GLFW_KEY_W){
			if(action==GLFW.GLFW_PRESS) w = true;
			else if(action==GLFW.GLFW_RELEASE) w = false;
		}
		if(key==GLFW.GLFW_KEY_A){
			if(action==GLFW.GLFW_PRESS) a = true;
			else if(action==GLFW.GLFW_RELEASE) a = false;
		}
		if(key==GLFW.GLFW_KEY_S){
			if(action==GLFW.GLFW_PRESS) s = true;
			else if(action==GLFW.GLFW_RELEASE) s = false;
		}
		if(key==GLFW.GLFW_KEY_D){
			if(action==GLFW.GLFW_PRESS) d = true;
			else if(action==GLFW.GLFW_RELEASE) d = false;
		}
		if(key==GLFW.GLFW_KEY_LEFT_SHIFT){
			if(action==GLFW.GLFW_PRESS) shift = true;
			else if(action==GLFW.GLFW_RELEASE) shift = false;
		}
		if(key==GLFW.GLFW_KEY_SPACE){
			if(action==GLFW.GLFW_PRESS) space = true;
			else if(action==GLFW.GLFW_RELEASE) space = false;
		}
		if(key==GLFW.GLFW_KEY_F5&&action==GLFW.GLFW_PRESS) Loop.INSTANCE.setPalette();
		if(key==GLFW.GLFW_KEY_R&&action==GLFW.GLFW_PRESS){
			if(Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()<10) Loop.INSTANCE.getGuiHandler().updateHotbarSelector((Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()+1)%10);
			else Loop.INSTANCE.getGuiHandler().updateHotbarSelector((Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()%10+1)%10+10);
		}
		if(key==GLFW.GLFW_KEY_F&&action==GLFW.GLFW_PRESS){
			if(Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()<10) Loop.INSTANCE.getGuiHandler().updateHotbarSelector((Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()-1+10)%10);
			else Loop.INSTANCE.getGuiHandler().updateHotbarSelector((Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()%10-1+10)%10+10);
		}
		if(key==GLFW.GLFW_KEY_TAB&&action==GLFW.GLFW_PRESS){
			if(Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()<10) Loop.INSTANCE.getGuiHandler().updateHotbarSelector(10);
			else Loop.INSTANCE.getGuiHandler().updateHotbarSelector(0);
		}
		if(key==GLFW.GLFW_KEY_Q){
			if(action==GLFW.GLFW_PRESS){
				Loop.INSTANCE.getGuiHandler().goalWheelRotation--;
				rotator = true;
			}else if(action==GLFW.GLFW_RELEASE) rotator = false;
		}
		if(key==GLFW.GLFW_KEY_E){
			if(action==GLFW.GLFW_PRESS){
				Loop.INSTANCE.getGuiHandler().goalWheelRotation++;
				rotator = true;
			}else if(action==GLFW.GLFW_RELEASE) rotator = false;
		}
	}
	public void processMouse(double x, double y){
		Loop.INSTANCE.getCamera().ry = Loop.INSTANCE.getCamera().ry += (x-screenWidth.get(0))*mouseSensitivity;
		Loop.INSTANCE.getCamera().rx = (float)Math.max(Math.min(Loop.INSTANCE.getCamera().rx+(y-screenHeight.get(0))*mouseSensitivity, 90), -90);
		GLFW.glfwSetCursorPos(Loop.INSTANCE.getWindow(), screenWidth.get(0), screenHeight.get(0));
	}
	public void processWalk(VoxelWorld world, double delta){
		delta *= moveSpeed;
		currentCamX = Loop.INSTANCE.getCamera().goalX;
		currentCamY = Loop.INSTANCE.getCamera().goalY;
		currentCamZ = Loop.INSTANCE.getCamera().goalZ;
		if(w) currentCamX += delta*(float)Math.sin(Math.toRadians(Loop.INSTANCE.getCamera().ry));
		if(a) currentCamX += delta*(float)Math.sin(Math.toRadians(Loop.INSTANCE.getCamera().ry-90));
		if(s) currentCamX -= delta*(float)Math.sin(Math.toRadians(Loop.INSTANCE.getCamera().ry));
		if(d) currentCamX += delta*(float)Math.sin(Math.toRadians(Loop.INSTANCE.getCamera().ry+90));
		if(canMoveTo(world, currentCamX, currentCamY, currentCamZ))Loop.INSTANCE.getCamera().goalX = currentCamX;
		currentCamX = Loop.INSTANCE.getCamera().goalX;
		if(w) currentCamZ -= delta*(float)Math.cos(Math.toRadians(Loop.INSTANCE.getCamera().ry));
		if(a) currentCamZ -= delta*(float)Math.cos(Math.toRadians(Loop.INSTANCE.getCamera().ry-90));
		if(s) currentCamZ += delta*(float)Math.cos(Math.toRadians(Loop.INSTANCE.getCamera().ry));
		if(d) currentCamZ -= delta*(float)Math.cos(Math.toRadians(Loop.INSTANCE.getCamera().ry+90));
		if(canMoveTo(world, currentCamX, currentCamY, currentCamZ))Loop.INSTANCE.getCamera().goalZ = currentCamZ;
		currentCamZ = Loop.INSTANCE.getCamera().goalZ;
		if(shift) currentCamY -= delta;
		if(space) currentCamY += delta;
		if(canMoveTo(world, currentCamX, currentCamY, currentCamZ))Loop.INSTANCE.getCamera().goalY = currentCamY;
	}
}