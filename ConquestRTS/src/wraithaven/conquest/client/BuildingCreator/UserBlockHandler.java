package wraithaven.conquest.client.BuildingCreator;

import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.CameraTarget;
import wraith.library.LWJGL.CameraTargetCallback;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.MiscUtil.BoundingBox;
import wraith.library.MiscUtil.Sphere;
import static org.lwjgl.glfw.GLFW.*;

public class UserBlockHandler{
	private CameraTargetCallback callback;
	private final BoundingBox boundingBox = new BoundingBox();
	private final Sphere cameraSphere = new Sphere();
	private final Camera camera;
	private final VoxelWorld world;
	private final CameraTarget cameraTarget;
	private final InputController controller;
	public UserBlockHandler(VoxelWorld world, Camera camera, InputController controller){
		cameraTarget=new CameraTarget(this.camera=camera);
		this.world=world;
		this.controller=controller;
		cameraSphere.r=0.3f;
	}
	public void mouseClick(int button, int action){
		if(controller.iso)return;
		if(button==GLFW_MOUSE_BUTTON_LEFT){
			if(action==GLFW_PRESS){
				callback=cameraTarget.getTargetBlock(world, 500, false);
				if(callback.block!=null&&callback.block.y>0)callback.block.chunk.setBlock(callback.block.x, callback.block.y, callback.block.z, null);
			}
		}else if(button==GLFW_MOUSE_BUTTON_RIGHT){
			if(action==GLFW_PRESS){
				callback=cameraTarget.getTargetBlock(world, 500, false);
				if(callback.block!=null){
					if(callback.side==0){
						if(callback.block.chunk.world.getBlock(callback.block.x+1, callback.block.y, callback.block.z, false)==null&&!collidesWithCamera(callback.block.x+1, callback.block.y, callback.block.z))callback.block.chunk.world.setBlock(callback.block.x+1, callback.block.y, callback.block.z, callback.block.type);
					}
					if(callback.side==1){
						if(callback.block.chunk.world.getBlock(callback.block.x-1, callback.block.y, callback.block.z, false)==null&&!collidesWithCamera(callback.block.x-1, callback.block.y, callback.block.z))callback.block.chunk.world.setBlock(callback.block.x-1, callback.block.y, callback.block.z, callback.block.type);
					}
					if(callback.side==2){
						if(callback.block.chunk.world.getBlock(callback.block.x, callback.block.y+1, callback.block.z, false)==null&&!collidesWithCamera(callback.block.x, callback.block.y+1, callback.block.z))callback.block.chunk.world.setBlock(callback.block.x, callback.block.y+1, callback.block.z, callback.block.type);
					}
					if(callback.side==3){
						if(callback.block.chunk.world.getBlock(callback.block.x, callback.block.y-1, callback.block.z, false)==null&&!collidesWithCamera(callback.block.x, callback.block.y-1, callback.block.z))callback.block.chunk.world.setBlock(callback.block.x, callback.block.y-1, callback.block.z, callback.block.type);
					}
					if(callback.side==4){
						if(callback.block.chunk.world.getBlock(callback.block.x, callback.block.y, callback.block.z+1, false)==null&&!collidesWithCamera(callback.block.x, callback.block.y, callback.block.z+1))callback.block.chunk.world.setBlock(callback.block.x, callback.block.y, callback.block.z+1, callback.block.type);
					}
					if(callback.side==5){
						if(callback.block.chunk.world.getBlock(callback.block.x, callback.block.y, callback.block.z-1, false)==null&&!collidesWithCamera(callback.block.x, callback.block.y, callback.block.z-1))callback.block.chunk.world.setBlock(callback.block.x, callback.block.y, callback.block.z-1, callback.block.type);
					}
				}
			}
		}
	}
	private boolean collidesWithCamera(int x, int y, int z){
		boundingBox.x1=x;
		boundingBox.y1=y;
		boundingBox.z1=z;
		boundingBox.x2=x+1;
		boundingBox.y2=y+1;
		boundingBox.z2=z+1;
		cameraSphere.x=camera.x;
		cameraSphere.y=camera.y;
		cameraSphere.z=camera.z;
		return intersectsWith(boundingBox, cameraSphere);
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