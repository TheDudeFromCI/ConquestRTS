package wraithaven.conquest.client.GameWorld;

import java.io.File;
import wraith.library.LWJGL.MipmapQuality;
import wraith.library.LWJGL.Texture;

public enum BlockTextures{
	grass("Grass.png"),
	sideDirt("Side Dirt.png"),
	dirt("Dirt.png");
	private Texture texture;
	private final String textureFile;
	private BlockTextures(String textureFile){ this.textureFile=textureFile; }
	public Texture getTexture(){ return texture; }
	public static void genTextures(){
		String folder = getFolder();
		for(BlockTextures t : values())t.texture=new Texture(new File(folder, t.textureFile), 4, MipmapQuality.HIGH);
	}
	public static String getFolder(){
		File file = new File(System.getProperty("user.dir")+File.separatorChar+"Assets");
		if(!file.exists())file.mkdirs();
		return file.getAbsolutePath();
	}
}