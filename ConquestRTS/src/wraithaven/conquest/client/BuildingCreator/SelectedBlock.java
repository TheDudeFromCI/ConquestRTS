package wraithaven.conquest.client.BuildingCreator;

import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.IndexManager;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTarget;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTargetCallback;

public class SelectedBlock{
	private static final float OUTLINE_BUFFER = 0.01f;
	private static final float OUTLINE_INTENSITY = 0.5f;
	private CameraTargetCallback callback;
	private final CameraTarget cameraTarget;
	public SelectedBlock(){
		cameraTarget = new CameraTarget(Loop.INSTANCE.getCamera());
	}
	public void render(){
		if(Loop.INSTANCE.getInputController().wireframeMode) return;
		callback = cameraTarget.getTargetBlock(Loop.INSTANCE.getVoxelWorld(), 500, false);
		if(callback.block!=IndexManager.AIR_BLOCK){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glColor4f(0, 0, 0, SelectedBlock.OUTLINE_INTENSITY);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glBegin(GL11.GL_QUADS);
			float sx = callback.x-SelectedBlock.OUTLINE_BUFFER;
			float sy = callback.y-SelectedBlock.OUTLINE_BUFFER;
			float sz = callback.z-SelectedBlock.OUTLINE_BUFFER;
			float ex = callback.x+1+SelectedBlock.OUTLINE_BUFFER;
			float ey = callback.y+1+SelectedBlock.OUTLINE_BUFFER;
			float ez = callback.z+1+SelectedBlock.OUTLINE_BUFFER;
			GL11.glVertex3f(sx, sy, sz);
			GL11.glVertex3f(sx, sy, ez);
			GL11.glVertex3f(ex, sy, ez);
			GL11.glVertex3f(ex, sy, sz);
			GL11.glVertex3f(sx, sy, sz);
			GL11.glVertex3f(sx, ey, sz);
			GL11.glVertex3f(ex, ey, sz);
			GL11.glVertex3f(ex, sy, sz);
			GL11.glVertex3f(sx, sy, sz);
			GL11.glVertex3f(sx, sy, ez);
			GL11.glVertex3f(sx, ey, ez);
			GL11.glVertex3f(sx, ey, sz);
			GL11.glVertex3f(sx, ey, sz);
			GL11.glVertex3f(sx, ey, ez);
			GL11.glVertex3f(ex, ey, ez);
			GL11.glVertex3f(ex, ey, sz);
			GL11.glVertex3f(sx, sy, ez);
			GL11.glVertex3f(sx, ey, ez);
			GL11.glVertex3f(ex, ey, ez);
			GL11.glVertex3f(ex, sy, ez);
			GL11.glVertex3f(ex, sy, sz);
			GL11.glVertex3f(ex, sy, ez);
			GL11.glVertex3f(ex, ey, ez);
			GL11.glVertex3f(ex, ey, sz);
			GL11.glEnd();
			GL11.glColor3f(1, 1, 1);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
	}
}