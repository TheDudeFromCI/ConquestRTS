package wraithaven.conquest.client.BuildingCreator;

import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTargetCallback;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTarget;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;

public class SelectedBlock{
	private CameraTargetCallback callback;
	private final CameraTarget cameraTarget;
	private final VoxelWorld world;
	private static final float OUTLINE_INTENSITY = 0.2f;
	public SelectedBlock(Camera camera, VoxelWorld world){
		cameraTarget=new CameraTarget(camera);
		this.world=world;
	}
	public void render(){
		callback=cameraTarget.getTargetBlock(world, 500, false);
		if(callback.block!=null){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(0, 0, 0, OUTLINE_INTENSITY);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(callback.block.x, callback.block.y, callback.block.z);
			GL11.glVertex3f(callback.block.x, callback.block.y, callback.block.z+1);
			GL11.glVertex3f(callback.block.x+1, callback.block.y, callback.block.z+1);
			GL11.glVertex3f(callback.block.x+1, callback.block.y, callback.block.z);
			GL11.glVertex3f(callback.block.x, callback.block.y, callback.block.z);
			GL11.glVertex3f(callback.block.x, callback.block.y+1, callback.block.z);
			GL11.glVertex3f(callback.block.x+1, callback.block.y+1, callback.block.z);
			GL11.glVertex3f(callback.block.x+1, callback.block.y, callback.block.z);
			GL11.glVertex3f(callback.block.x, callback.block.y, callback.block.z);
			GL11.glVertex3f(callback.block.x, callback.block.y, callback.block.z+1);
			GL11.glVertex3f(callback.block.x, callback.block.y+1, callback.block.z+1);
			GL11.glVertex3f(callback.block.x, callback.block.y+1, callback.block.z);
			GL11.glVertex3f(callback.block.x, callback.block.y+1, callback.block.z);
			GL11.glVertex3f(callback.block.x, callback.block.y+1, callback.block.z+1);
			GL11.glVertex3f(callback.block.x+1, callback.block.y+1, callback.block.z+1);
			GL11.glVertex3f(callback.block.x+1, callback.block.y+1, callback.block.z);
			GL11.glVertex3f(callback.block.x, callback.block.y, callback.block.z+1);
			GL11.glVertex3f(callback.block.x, callback.block.y+1, callback.block.z+1);
			GL11.glVertex3f(callback.block.x+1, callback.block.y+1, callback.block.z+1);
			GL11.glVertex3f(callback.block.x+1, callback.block.y, callback.block.z+1);
			GL11.glVertex3f(callback.block.x+1, callback.block.y, callback.block.z);
			GL11.glVertex3f(callback.block.x+1, callback.block.y, callback.block.z+1);
			GL11.glVertex3f(callback.block.x+1, callback.block.y+1, callback.block.z+1);
			GL11.glVertex3f(callback.block.x+1, callback.block.y+1, callback.block.z);
			GL11.glEnd();
			GL11.glColor3f(1, 1, 1);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
	}
}
