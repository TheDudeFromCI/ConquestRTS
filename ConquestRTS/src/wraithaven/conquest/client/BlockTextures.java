package wraithaven.conquest.client;

import java.io.File;
import wraith.library.LWJGL.CubeTextures;
import wraith.library.LWJGL.Texture;

public enum BlockTextures{
	grass(new int[]{1, 1, 0, 1, 1, 1});
	private final int[] indices;
	private final CubeTextures texture;
	private static final String[] textureList = {
		"Grass.png",
		"Dirt.png"
	};
	private static Texture[] textures = new Texture[textureList.length];
	private BlockTextures(int[] indices){
		this.indices=indices;
		texture=new CubeTextures();
	}
	public CubeTextures getTextures(){ return texture; }
	public static void genTextures(){
		String folder = getFolder();
		for(int i = 0; i<textureList.length; i++)textures[i]=new Texture(new File(folder, textureList[i]));
		for(BlockTextures t : values()){
			t.texture.xUp=textures[t.indices[0]];
			t.texture.xDown=textures[t.indices[1]];
			t.texture.yUp=textures[t.indices[2]];
			t.texture.yDown=textures[t.indices[3]];
			t.texture.zUp=textures[t.indices[4]];
			t.texture.zDown=textures[t.indices[5]];
		}
	}
	private static String getFolder(){
		File file = new File(System.getProperty("user.dir")+File.separatorChar+"Assets");
		if(!file.exists())file.mkdirs();
		return file.getAbsolutePath();
	}
}