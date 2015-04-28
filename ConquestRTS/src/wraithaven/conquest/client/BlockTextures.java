package wraithaven.conquest.client;

import java.io.File;
import wraith.library.LWJGL.CubeTextures;
import wraith.library.LWJGL.Texture;

public enum BlockTextures{
	grass1(new int[]{1, 1, 0, 0, 1, 1}, new int[]{2, 0, 0, 0, 3, 0}),
	grass2(new int[]{1, 1, 0, 0, 1, 1}, new int[]{2, 0, 1, 0, 3, 0}),
	grass3(new int[]{1, 1, 0, 0, 1, 1}, new int[]{2, 0, 2, 0, 3, 0}),
	grass4(new int[]{1, 1, 0, 0, 1, 1}, new int[]{2, 0, 3, 0, 3, 0}),
	selected(new int[]{2, 2, 2, 2, 2, 2}, new int[]{0, 0, 0, 0, 0, 0});
	private final int[] indices;
	private final int[] rotations;
	private final CubeTextures texture;
	private static final String[] textureList = {
		"Grass.png",
		"Dirt.png",
		"Selected.png"
	};
	private static Texture[] textures = new Texture[textureList.length];
	private BlockTextures(int[] indices, int[] rotations){
		this.indices=indices;
		this.rotations=rotations;
		texture=new CubeTextures();
	}
	public CubeTextures getTextures(){ return texture; }
	public static void genTextures(){
		String folder = getFolder();
		for(int i = 0; i<textureList.length; i++)textures[i]=new Texture(new File(folder, textureList[i]), 4);
		for(BlockTextures t : values()){
			t.texture.xUp=textures[t.indices[0]];
			t.texture.xDown=textures[t.indices[1]];
			t.texture.yUp=textures[t.indices[2]];
			t.texture.yDown=textures[t.indices[3]];
			t.texture.zUp=textures[t.indices[4]];
			t.texture.zDown=textures[t.indices[5]];
			t.texture.xUpRotation=t.rotations[0];
			t.texture.xDownRotation=t.rotations[1];
			t.texture.yUpRotation=t.rotations[2];
			t.texture.yDownRotation=t.rotations[3];
			t.texture.zUpRotation=t.rotations[4];
			t.texture.zDownRotation=t.rotations[5];
		}
	}
	private static String getFolder(){
		File file = new File(System.getProperty("user.dir")+File.separatorChar+"Assets");
		if(!file.exists())file.mkdirs();
		return file.getAbsolutePath();
	}
}