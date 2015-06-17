package wraithaven.conquest.client.BuildingCreator;

import org.lwjgl.glfw.GLFW;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.IndexManager;
import wraithaven.conquest.client.GameWorld.BoundingBox;
import wraithaven.conquest.client.GameWorld.Sphere;
import wraithaven.conquest.client.GameWorld.Voxel.Block;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTarget;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTargetCallback;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;

public class UserBlockHandler{
	private static final double CLICK_PING_RATE = 0.2;
	private static boolean intersectsWith(BoundingBox bb, Sphere sphere){
		float dmin = 0;
		if(sphere.x<bb.x1)dmin += Math.pow(sphere.x-bb.x1, 2);
		else if(sphere.x>bb.x2)dmin += Math.pow(sphere.x-bb.x2, 2);
		if(sphere.y<bb.y1)dmin += Math.pow(sphere.y-bb.y1, 2);
		else if(sphere.y>bb.y2)dmin += Math.pow(sphere.y-bb.y2, 2);
		if(sphere.z<bb.z1)dmin += Math.pow(sphere.z-bb.z1, 2);
		else if(sphere.z>bb.z2)dmin += Math.pow(sphere.z-bb.z2, 2);
		return dmin<=Math.pow(sphere.r, 2);
	}
	private BlockRotation blockRotation;
	private final BoundingBox boundingBox = new BoundingBox();
	private CameraTargetCallback callback;
	private final Sphere cameraSphere = new Sphere();
	private final CameraTarget cameraTarget;
	private CubeTextures cubeTextures;
	private boolean holdingLeftButton, holdingRightButton;
	private double lastButtonPing;
	private BlockShape shape;
	public UserBlockHandler(){
		cameraTarget = new CameraTarget(Loop.INSTANCE.getCamera());
		cameraSphere.r = InputController.CAMERA_RADIUS;
	}
	private boolean collidesWithCamera(int x, int y, int z){
		boundingBox.x1 = x;
		boundingBox.y1 = y;
		boundingBox.z1 = z;
		boundingBox.x2 = x+1;
		boundingBox.y2 = y+1;
		boundingBox.z2 = z+1;
		cameraSphere.x = Loop.INSTANCE.getCamera().goalX;
		cameraSphere.y = Loop.INSTANCE.getCamera().goalY;
		cameraSphere.z = Loop.INSTANCE.getCamera().goalZ;
		return UserBlockHandler.intersectsWith(boundingBox, cameraSphere);
	}
	private void deleteBlock(){
		callback = cameraTarget.getTargetBlock(Loop.INSTANCE.getVoxelWorld(), 500, false);
		if(callback.block!=IndexManager.AIR_BLOCK
				&&callback.y>0)Loop.INSTANCE.getVoxelWorld().setBlock(callback.x, callback.y, callback.z, IndexManager.AIR_BLOCK, true);
	}
	public void mouseClick(int button, int action){
		if(button==GLFW.GLFW_MOUSE_BUTTON_LEFT){
			if(action==GLFW.GLFW_PRESS){
				holdingRightButton = true;
				lastButtonPing = GLFW.glfwGetTime();
				deleteBlock();
			}else if(action==GLFW.GLFW_RELEASE) holdingRightButton = false;
		}else if(button==GLFW.GLFW_MOUSE_BUTTON_RIGHT){
			if(action==GLFW.GLFW_PRESS){
				holdingLeftButton = true;
				lastButtonPing = GLFW.glfwGetTime();
				placeBlock();
			}else if(action==GLFW.GLFW_RELEASE) holdingLeftButton = false;
		}else if(button==GLFW.GLFW_MOUSE_BUTTON_MIDDLE){
			if(action==GLFW.GLFW_PRESS){
				callback = cameraTarget.getTargetBlock(Loop.INSTANCE.getVoxelWorld(), 500, false);
				if(callback.block!=IndexManager.AIR_BLOCK){
					Block block = Loop.INSTANCE.getVoxelWorld().getBlock(callback.block);
					BlockIcon icon;
					for(int i = 0; i<10; i++){
						icon = Loop.INSTANCE.getGuiHandler().getIconManager().getIcon(i);
						if(icon==null) continue;
						if(icon.block.block.shape==block.shape&&icon.block.block.originalCubeTextures==block.originalCubeTextures){
							Loop.INSTANCE.getGuiHandler().updateHotbarSelector(i);
							Loop.INSTANCE.getGuiHandler().goalWheelRotation = block.rotation.index;
							return;
						}
					}
					for(int i = 0; i<10; i++){
						icon = Loop.INSTANCE.getGuiHandler().getIconManager().getIcon(i);
						if(icon==null){
							Loop.INSTANCE.getGuiHandler().addIcon(i, new BlockIcon(block.shape, block.originalCubeTextures, block.rotation));
							Loop.INSTANCE.getGuiHandler().updateHotbarSelector(i);
							Loop.INSTANCE.getGuiHandler().goalWheelRotation = block.rotation.index;
							return;
						}
					}
					Loop.INSTANCE.getGuiHandler().addIcon(Loop.INSTANCE.getGuiHandler().getHotbarSelectorId(), Loop.INSTANCE.getInventory().getBlockIcon(block.shape, block.originalCubeTextures));
					Loop.INSTANCE.getGuiHandler().goalWheelRotation = block.rotation.index;
				}
			}
		}
	}
	private void placeBlock(){
		shape = Loop.INSTANCE.getGuiHandler().getSelectedShape();
		cubeTextures = Loop.INSTANCE.getGuiHandler().getSelectedCubeTextures();
		blockRotation = Loop.INSTANCE.getGuiHandler().getSelectedRotation();
		if(shape==null) return;
		callback = cameraTarget.getTargetBlock(Loop.INSTANCE.getVoxelWorld(), 500, false);
		if(callback.block!=IndexManager.AIR_BLOCK){
			short index = Loop.INSTANCE.getVoxelWorld().indexOfBlock(shape, cubeTextures, blockRotation);
			if(callback.side==0){
				if(Loop.INSTANCE.getVoxelWorld().getBlock(callback.x+1, callback.y, callback.z, false)==IndexManager.AIR_BLOCK
						&&!collidesWithCamera(callback.x+1, callback.y, callback.z))Loop.INSTANCE.getVoxelWorld().setBlock(callback.x+1, callback.y, callback.z, index, true);
			}
			if(callback.side==1){
				if(Loop.INSTANCE.getVoxelWorld().getBlock(callback.x-1, callback.y, callback.z, false)==IndexManager.AIR_BLOCK
						&&!collidesWithCamera(callback.x-1, callback.y, callback.z))Loop.INSTANCE.getVoxelWorld().setBlock(callback.x-1, callback.y, callback.z, index, true);
			}
			if(callback.side==2){
				if(Loop.INSTANCE.getVoxelWorld().getBlock(callback.x, callback.y+1, callback.z, false)==IndexManager.AIR_BLOCK
						&&!collidesWithCamera(callback.x, callback.y+1, callback.z))Loop.INSTANCE.getVoxelWorld().setBlock(callback.x, callback.y+1, callback.z, index, true);
			}
			if(callback.side==3){
				if(Loop.INSTANCE.getVoxelWorld().getBlock(callback.x, callback.y-1, callback.z, false)==IndexManager.AIR_BLOCK
						&&!collidesWithCamera(callback.x, callback.y-1, callback.z))Loop.INSTANCE.getVoxelWorld().setBlock(callback.x, callback.y-1, callback.z, index, true);
			}
			if(callback.side==4){
				if(Loop.INSTANCE.getVoxelWorld().getBlock(callback.x, callback.y, callback.z+1, false)==IndexManager.AIR_BLOCK
						&&!collidesWithCamera(callback.x, callback.y, callback.z+1))Loop.INSTANCE.getVoxelWorld().setBlock(callback.x, callback.y, callback.z+1, index, true);
			}
			if(callback.side==5){
				if(Loop.INSTANCE.getVoxelWorld().getBlock(callback.x, callback.y, callback.z-1, false)==IndexManager.AIR_BLOCK
						&&!collidesWithCamera(callback.x, callback.y, callback.z-1))Loop.INSTANCE.getVoxelWorld().setBlock(callback.x, callback.y, callback.z-1, index, true);
			}
		}
	}
	public void update(double time){
		if(time>lastButtonPing+UserBlockHandler.CLICK_PING_RATE){
			if(holdingLeftButton){
				lastButtonPing = time;
				placeBlock();
			}
			if(holdingRightButton){
				lastButtonPing = time;
				deleteBlock();
			}
		}
	}
}