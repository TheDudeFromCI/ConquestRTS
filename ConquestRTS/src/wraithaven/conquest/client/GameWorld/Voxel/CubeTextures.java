package wraithaven.conquest.client.GameWorld.Voxel;

public class CubeTextures{
	public Texture xUp, xDown, yUp, yDown, zUp, zDown;
	public int xUpRotation, xDownRotation, yUpRotation, yDownRotation, zUpRotation, zDownRotation;
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
}