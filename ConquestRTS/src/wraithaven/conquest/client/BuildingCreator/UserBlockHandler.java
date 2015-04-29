package wraithaven.conquest.client.BuildingCreator;

import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.CameraTarget;
import wraith.library.LWJGL.CameraTargetCallback;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import static org.lwjgl.glfw.GLFW.*;

public class UserBlockHandler{
	private CameraTargetCallback callback;
	private final VoxelWorld world;
	private final CameraTarget cameraTarget;
	private final InputController controller;
	public UserBlockHandler(VoxelWorld world, Camera camera, InputController controller){
		cameraTarget=new CameraTarget(camera);
		this.world=world;
		this.controller=controller;
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
					if(callback.side==0)callback.block.chunk.world.setBlock(callback.block.x+1, callback.block.y, callback.block.z, callback.block.type);
					if(callback.side==1)callback.block.chunk.world.setBlock(callback.block.x-1, callback.block.y, callback.block.z, callback.block.type);
					if(callback.side==2)callback.block.chunk.world.setBlock(callback.block.x, callback.block.y+1, callback.block.z, callback.block.type);
					if(callback.side==3)callback.block.chunk.world.setBlock(callback.block.x, callback.block.y-1, callback.block.z, callback.block.type);
					if(callback.side==4)callback.block.chunk.world.setBlock(callback.block.x, callback.block.y, callback.block.z+1, callback.block.type);
					if(callback.side==5)callback.block.chunk.world.setBlock(callback.block.x, callback.block.y, callback.block.z-1, callback.block.type);
				}
			}
		}
	}
}