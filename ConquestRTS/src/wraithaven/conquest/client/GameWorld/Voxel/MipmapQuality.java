package wraithaven.conquest.client.GameWorld.Voxel;

import org.lwjgl.opengl.GL11;

public enum MipmapQuality{
	HIGH(GL11.GL_LINEAR_MIPMAP_LINEAR), LOW(GL11.GL_LINEAR_MIPMAP_NEAREST);
	private final int q;
	private MipmapQuality(int q){
		this.q = q;
	}
	public int getQuality(){
		return q;
	}
}