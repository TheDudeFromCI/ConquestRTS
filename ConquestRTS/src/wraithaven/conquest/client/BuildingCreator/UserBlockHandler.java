package wraithaven.conquest.client.BuildingCreator;

import static org.lwjgl.glfw.GLFW.*;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.CustomBlock;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTarget;
import wraithaven.conquest.client.GameWorld.Sphere;
import wraithaven.conquest.client.GameWorld.BoundingBox;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTargetCallback;

public class UserBlockHandler{
	private CameraTargetCallback callback;
	private boolean holdingLeftButton, holdingRightButton;
	private double lastButtonPing;
	private BlockShape shape;
	private CubeTextures cubeTextures;
	private BlockRotation blockRotation;
	private final BoundingBox boundingBox = new BoundingBox();
	private final Sphere cameraSphere = new Sphere();
	private final CameraTarget cameraTarget;
	private static final double CLICK_PING_RATE = 0.2;
	public UserBlockHandler(){
		cameraTarget=new CameraTarget(Loop.INSTANCE.getCamera());
		cameraSphere.r=InputController.CAMERA_RADIUS;
	}
	public void mouseClick(int button, int action){
		if(button==GLFW_MOUSE_BUTTON_LEFT){
			if(action==GLFW_PRESS){
				holdingRightButton=true;
				lastButtonPing=glfwGetTime();
				deleteBlock();
			}else if(action==GLFW_RELEASE)holdingRightButton=false;
		}else if(button==GLFW_MOUSE_BUTTON_RIGHT){
			if(action==GLFW_PRESS){
				holdingLeftButton=true;
				lastButtonPing=glfwGetTime();
				placeBlock();
			}else if(action==GLFW_RELEASE)holdingLeftButton=false;
		}else if(button==GLFW_MOUSE_BUTTON_MIDDLE){
			if(action==GLFW_PRESS){
				callback=cameraTarget.getTargetBlock(Loop.INSTANCE.getVoxelWorld(), 500, false);
				if(callback.block!=null){
					if(callback.block instanceof CustomBlock){
						CustomBlock block = (CustomBlock)callback.block;
						BlockIcon icon;
						for(int i = 0; i<10; i++){
							icon=Loop.INSTANCE.getGuiHandler().getIconManager().getIcon(i);
							if(icon==null)continue;
							if(icon.shape==block.shape&&icon.textures==block.textures){
								Loop.INSTANCE.getGuiHandler().updateHotbarSelector(i);
								Loop.INSTANCE.getGuiHandler().goalWheelRotation=block.rotation.index;
								return;
							}
						}
						for(int i = 0; i<10; i++){
							icon=Loop.INSTANCE.getGuiHandler().getIconManager().getIcon(i);
							if(icon==null){
								Loop.INSTANCE.getGuiHandler().addIcon(i, new BlockIcon(block.shape, block.textures, block.rotation));
								Loop.INSTANCE.getGuiHandler().updateHotbarSelector(i);
								Loop.INSTANCE.getGuiHandler().goalWheelRotation=block.rotation.index;
								return;
							}
						}
						Loop.INSTANCE.getGuiHandler().addIcon(Loop.INSTANCE.getGuiHandler().getHotbarSelectorId(), Loop.INSTANCE.getInventory().getBlockIcon(block.shape, block.textures));
						Loop.INSTANCE.getGuiHandler().goalWheelRotation=block.rotation.index;
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
		cameraSphere.x=Loop.INSTANCE.getCamera().goalX;
		cameraSphere.y=Loop.INSTANCE.getCamera().goalY;
		cameraSphere.z=Loop.INSTANCE.getCamera().goalZ;
		return intersectsWith(boundingBox, cameraSphere);
	}
	private void placeBlock(){
		shape=Loop.INSTANCE.getGuiHandler().getSelectedShape();
		cubeTextures=Loop.INSTANCE.getGuiHandler().getSelectedCubeTextures();
		blockRotation=Loop.INSTANCE.getGuiHandler().getSelectedRotation();
		if(shape==null)return;
		callback=cameraTarget.getTargetBlock(Loop.INSTANCE.getVoxelWorld(), 500, false);
		if(callback.block!=null){
			if(callback.side==0){
				if(callback.block.chunk.world.getBlock(callback.block.x+1, callback.block.y, callback.block.z, false)==null&&!collidesWithCamera(callback.block.x+1, callback.block.y, callback.block.z))callback.block.chunk.world.setBlock(callback.block.x+1, callback.block.y, callback.block.z, shape, cubeTextures, blockRotation);
			}
			if(callback.side==1){
				if(callback.block.chunk.world.getBlock(callback.block.x-1, callback.block.y, callback.block.z, false)==null&&!collidesWithCamera(callback.block.x-1, callback.block.y, callback.block.z))callback.block.chunk.world.setBlock(callback.block.x-1, callback.block.y, callback.block.z, shape, cubeTextures, blockRotation);
			}
			if(callback.side==2){
				if(callback.block.chunk.world.getBlock(callback.block.x, callback.block.y+1, callback.block.z, false)==null&&!collidesWithCamera(callback.block.x, callback.block.y+1, callback.block.z))callback.block.chunk.world.setBlock(callback.block.x, callback.block.y+1, callback.block.z, shape, cubeTextures, blockRotation);
			}
			if(callback.side==3){
				if(callback.block.chunk.world.getBlock(callback.block.x, callback.block.y-1, callback.block.z, false)==null&&!collidesWithCamera(callback.block.x, callback.block.y-1, callback.block.z))callback.block.chunk.world.setBlock(callback.block.x, callback.block.y-1, callback.block.z, shape, cubeTextures, blockRotation);
			}
			if(callback.side==4){
				if(callback.block.chunk.world.getBlock(callback.block.x, callback.block.y, callback.block.z+1, false)==null&&!collidesWithCamera(callback.block.x, callback.block.y, callback.block.z+1))callback.block.chunk.world.setBlock(callback.block.x, callback.block.y, callback.block.z+1, shape, cubeTextures, blockRotation);
			}
			if(callback.side==5){
				if(callback.block.chunk.world.getBlock(callback.block.x, callback.block.y, callback.block.z-1, false)==null&&!collidesWithCamera(callback.block.x, callback.block.y, callback.block.z-1))callback.block.chunk.world.setBlock(callback.block.x, callback.block.y, callback.block.z-1, shape, cubeTextures, blockRotation);
			}
		}
	}
	private void deleteBlock(){
		callback=cameraTarget.getTargetBlock(Loop.INSTANCE.getVoxelWorld(), 500, false);
		if(callback.block!=null&&callback.block.y>0)callback.block.chunk.setBlock(callback.block.x, callback.block.y, callback.block.z, null, null, null);
	}
	public void update(double time){
		if(time>lastButtonPing+CLICK_PING_RATE){
			if(holdingLeftButton){
				lastButtonPing=time;
				placeBlock();
			}
			if(holdingRightButton){
				lastButtonPing=time;
				deleteBlock();
			}
		}
		{
			if(Loop.INSTANCE.getGuiHandler().getHotbarSelectorId()==9){
				shape=Loop.INSTANCE.getGuiHandler().getSelectedShape();
				cubeTextures=Loop.INSTANCE.getGuiHandler().getSelectedCubeTextures();
				int x = (int)(Math.random()*128);
				int y = (int)(Math.random()*128);
				int z = (int)(Math.random()*128);
				if(Loop.INSTANCE.getVoxelWorld().getBlock(x, y, z, false)==null)Loop.INSTANCE.getVoxelWorld().setBlock(x, y, z, shape, cubeTextures, BlockRotation.getRotation((int)(Math.random()*24)));
			}
		}
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