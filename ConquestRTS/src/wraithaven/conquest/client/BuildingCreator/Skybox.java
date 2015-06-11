package wraithaven.conquest.client.BuildingCreator;

import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.Quad;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class Skybox{
	private static final float[] WHITE_COLORS = {1, 1, 1};
	private final QuadBatch batch;
	public Skybox(Texture texture){
		batch = new QuadBatch(texture);
		batch.addQuad(new Quad(new float[12], Skybox.WHITE_COLORS, new float[8], 0));
		batch.addQuad(new Quad(new float[12], Skybox.WHITE_COLORS, new float[8], 1));
		batch.addQuad(new Quad(new float[12], Skybox.WHITE_COLORS, new float[8], 2));
		batch.addQuad(new Quad(new float[12], Skybox.WHITE_COLORS, new float[8], 3));
		batch.addQuad(new Quad(new float[12], Skybox.WHITE_COLORS, new float[8], 4));
		batch.addQuad(new Quad(new float[12], Skybox.WHITE_COLORS, new float[8], 5));
		batch.recompileBuffer();
	}
	public void render(Camera camera){
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPushMatrix();
		GL11.glCullFace(GL11.GL_FRONT);
		GL11.glTranslatef(camera.x, camera.y-1, camera.z);
		batch.getTexture().bind();
		batch.renderPart();
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}