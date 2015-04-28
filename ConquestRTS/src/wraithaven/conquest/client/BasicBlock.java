package wraithaven.conquest.client;

import wraith.library.LWJGL.CubeTextures;
import wraith.library.LWJGL.Texture;
import wraith.library.LWJGL.Voxel.BlockType;

public class BasicBlock implements BlockType{
	private final CubeTextures textures;
	public Texture getTexture(int side){
		if(side==0)return textures.xUp;
		if(side==1)return textures.xDown;
		if(side==2)return textures.yUp;
		if(side==3)return textures.yDown;
		if(side==4)return textures.zUp;
		if(side==5)return textures.zDown;
		return null;
	}
	public int getRotation(int side){
		if(side==0)return textures.xUpRotation;
		if(side==1)return textures.xDownRotation;
		if(side==2)return textures.yUpRotation;
		if(side==3)return textures.yDownRotation;
		if(side==4)return textures.zUpRotation;
		if(side==5)return textures.zDownRotation;
		return 0;
	}
	public BasicBlock(CubeTextures textures){ this.textures=textures; }
}