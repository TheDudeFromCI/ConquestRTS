package wraithaven.conquest.client.BuildingCreator;

import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import wraithaven.conquest.client.GameWorld.Voxel.Cube;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class Skybox{
	private final QuadBatch batch;
	private static final float[] WHITE_COLORS = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	public Skybox(Texture texture){
		batch=new QuadBatch(texture, false, 0, 0, 0);
		batch.addQuad(Cube.generateQuad(2, -1, 0, -1, 0, WHITE_COLORS, 2, new float[]{0.25f, 0.5f, 0.25f, 0.5f}));
		batch.addQuad(Cube.generateQuad(0, 0, 1, -1, 1, WHITE_COLORS, 1, new float[]{0.75f, 0.25f, 0.25f, 0.25f}));
		batch.addQuad(Cube.generateQuad(0, 0, 1, 0, 1, WHITE_COLORS, 1, new float[]{0.75f, 0.25f, 0.5f, 0.25f}));
		batch.addQuad(Cube.generateQuad(1, -1, 1, -1, 0, WHITE_COLORS, 1, new float[]{0, 0.25f, 0.25f, 0.25f}));
		batch.addQuad(Cube.generateQuad(1, -1, 1, 0, 0, WHITE_COLORS, 1, new float[]{0, 0.25f, 0.5f, 0.25f}));
		batch.addQuad(Cube.generateQuad(4, -1, 1, 0, 3, WHITE_COLORS, 1, new float[]{0.25f, 0.25f, 0.75f, 0.25f}));
		batch.addQuad(Cube.generateQuad(4, 0, 1, 0, 3, WHITE_COLORS, 1, new float[]{0.5f, 0.25f, 0.75f, 0.25f}));
		batch.addQuad(Cube.generateQuad(5, -1, 1, -1, 0, WHITE_COLORS, 1, new float[]{0.25f, 0.25f, 0, 0.25f}));
		batch.addQuad(Cube.generateQuad(5, 0, 1, -1, 0, WHITE_COLORS, 1, new float[]{0.5f, 0.25f, 0, 0.25f}));
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