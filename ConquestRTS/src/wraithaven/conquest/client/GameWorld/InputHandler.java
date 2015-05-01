package wraithaven.conquest.client.GameWorld;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.Voxel.VoxelBlock;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.MiscUtil.BoundingBox;
import wraith.library.MiscUtil.Sphere;

public class InputHandler{
	private boolean w, a, s, d;
	private int x, y, z;
	private int bcx1, bcy1, bcz1, bcx2, bcy2, bcz2;
	private Sphere cameraSphere = new Sphere();
	private BoundingBox boundingBox = new BoundingBox();
	private float currentCamX, currentCamY, currentCamZ;
	public float moveSpeed = 8;
	private final Camera cam;
	public InputHandler(Camera cam){
		this.cam=cam;
		cameraSphere.r=0.3f;
	}
	public void onKey(long window, int key, int action){
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
		if(key==GLFW.GLFW_KEY_Q)if(action==GLFW.GLFW_PRESS)cam.goalRY-=22.5f;
		if(key==GLFW.GLFW_KEY_E)if(action==GLFW.GLFW_PRESS)cam.goalRY+=22.5f;
	}
	public void processWalk(VoxelWorld world, double delta){
		delta*=moveSpeed;
		currentCamX=cam.goalX;
		currentCamY=cam.goalY;
		currentCamZ=cam.goalZ;
		if(w)currentCamX+=delta*(float)Math.sin(Math.toRadians(cam.ry));
		if(a)currentCamX+=delta*(float)Math.sin(Math.toRadians(cam.ry-90));
		if(s)currentCamX-=delta*(float)Math.sin(Math.toRadians(cam.ry));
		if(d)currentCamX+=delta*(float)Math.sin(Math.toRadians(cam.ry+90));
		if(canMoveTo(world, currentCamX, currentCamY, currentCamZ))cam.goalX=cam.x=currentCamX;
		currentCamX=cam.goalX;
		if(w)currentCamZ-=delta*(float)Math.cos(Math.toRadians(cam.ry));
		if(a)currentCamZ-=delta*(float)Math.cos(Math.toRadians(cam.ry-90));
		if(s)currentCamZ+=delta*(float)Math.cos(Math.toRadians(cam.ry));
		if(d)currentCamZ-=delta*(float)Math.cos(Math.toRadians(cam.ry+90));
		if(canMoveTo(world, currentCamX, currentCamY, currentCamZ))cam.goalZ=cam.z=currentCamZ;
		currentCamZ=cam.goalZ;
	}
	private boolean canMoveTo(VoxelWorld world, float sx, float sy, float sz){
		if(Test.DEBUG)return true;
		VoxelBlock block;
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
					block=world.getBlock(x, y, z);
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
		if(sphere.z<bb.z1)dmin += Math.pow(sphere.z-bb.z1, 2);
		else if(sphere.z>bb.z2)dmin+=Math.pow(sphere.z-bb.z2, 2);
		return dmin<=Math.pow(sphere.r, 2);
	}
}