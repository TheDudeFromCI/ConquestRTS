package wraithaven.conquest.client.GameWorld.Voxel;

public class CubeTextures implements Cloneable{
	public Texture xUp, xDown, yUp, yDown, zUp, zDown;
	public int xUpRotation, xDownRotation, yUpRotation, yDownRotation, zUpRotation, zDownRotation;
	public float[] colors = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	public Texture getTexture(int side){
		if(side==0)return xUp;
		if(side==1)return xDown;
		if(side==2)return yUp;
		if(side==3)return yDown;
		if(side==4)return zUp;
		if(side==5)return zDown;
		return null;
	}
	public int getRotation(int side){
		if(side==0)return xUpRotation;
		if(side==1)return xDownRotation;
		if(side==2)return yUpRotation;
		if(side==3)return yDownRotation;
		if(side==4)return zUpRotation;
		if(side==5)return zDownRotation;
		return 0;
	}
	public CubeTextures duplicate(){
		try{ return (CubeTextures)clone();
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	public void set(CubeTextures textures){
		xUp=textures.xUp;
		xDown=textures.xDown;
		yUp=textures.yUp;
		yDown=textures.yDown;
		zUp=textures.zUp;
		zDown=textures.zDown;
		for(int i = 0; i<colors.length; i++)colors[i]=textures.colors[i];
		xUpRotation=textures.xUpRotation;
		xDownRotation=textures.xDownRotation;
		yUpRotation=textures.yUpRotation;
		yDownRotation=textures.yDownRotation;
		zUpRotation=textures.zUpRotation;
		zDownRotation=textures.zDownRotation;
	}
}