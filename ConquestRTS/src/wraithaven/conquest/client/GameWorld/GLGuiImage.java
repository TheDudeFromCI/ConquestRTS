package wraithaven.conquest.client.GameWorld;

import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class GLGuiImage{
	Texture texture;
	public float x,
			y,
			z,
			w,
			h;
	public GLGuiImage(String folder, String file){
		texture = Texture.getTexture(folder, file);
	}
	public GLGuiImage(Texture texture){
		this.texture = texture;
	}
}