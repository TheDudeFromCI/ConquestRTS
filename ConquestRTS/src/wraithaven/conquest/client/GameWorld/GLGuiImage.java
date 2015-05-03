package wraithaven.conquest.client.GameWorld;

import java.io.File;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class GLGuiImage{
	Texture texture;
	public float x, y, z, w, h;
	public GLGuiImage(File file){ texture=new Texture(file, 0, null); }
}