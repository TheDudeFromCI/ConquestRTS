package wraithaven.conquest.client.GameWorld.Voxel;

import wraithaven.conquest.client.ClientLauncher;

public class CubeTextures implements Cloneable{
	public static CubeTextures decrypt(String s){
		CubeTextures t = new CubeTextures();
		String[] parts = s.split("\"");
		String[] colorParts = parts[0].split("'");
		String[] textureParts = parts[1].split("'");
		String[] rotationParts = parts[2].split("'");
		t.colors[0] = Float.valueOf(colorParts[0]);
		t.colors[1] = Float.valueOf(colorParts[1]);
		t.colors[2] = Float.valueOf(colorParts[2]);
		t.xUp = Texture.getTexture(ClientLauncher.textureFolder, textureParts[0], 4, MipmapQuality.HIGH);
		t.xDown = Texture.getTexture(ClientLauncher.textureFolder, textureParts[1], 4, MipmapQuality.HIGH);
		t.yUp = Texture.getTexture(ClientLauncher.textureFolder, textureParts[2], 4, MipmapQuality.HIGH);
		t.yDown = Texture.getTexture(ClientLauncher.textureFolder, textureParts[3], 4, MipmapQuality.HIGH);
		t.zUp = Texture.getTexture(ClientLauncher.textureFolder, textureParts[4], 4, MipmapQuality.HIGH);
		t.zDown = Texture.getTexture(ClientLauncher.textureFolder, textureParts[5], 4, MipmapQuality.HIGH);
		t.xUpRotation = Integer.valueOf(rotationParts[0]);
		t.xDownRotation = Integer.valueOf(rotationParts[1]);
		t.yUpRotation = Integer.valueOf(rotationParts[2]);
		t.yDownRotation = Integer.valueOf(rotationParts[3]);
		t.zUpRotation = Integer.valueOf(rotationParts[4]);
		t.zDownRotation = Integer.valueOf(rotationParts[5]);
		return t;
	}
	public static String encrypt(CubeTextures t){
		StringBuilder s = new StringBuilder();
		s.append(t.colors[0]);
		s.append('\'');
		s.append(t.colors[1]);
		s.append('\'');
		s.append(t.colors[2]);
		s.append('\"');
		s.append(t.xUp.file);
		s.append('\'');
		s.append(t.xDown.file);
		s.append('\'');
		s.append(t.yUp.file);
		s.append('\'');
		s.append(t.yDown.file);
		s.append('\'');
		s.append(t.zUp.file);
		s.append('\'');
		s.append(t.zDown.file);
		s.append('\"');
		s.append(t.xUpRotation);
		s.append('\'');
		s.append(t.xDownRotation);
		s.append('\'');
		s.append(t.yUpRotation);
		s.append('\'');
		s.append(t.yDownRotation);
		s.append('\'');
		s.append(t.zUpRotation);
		s.append('\'');
		s.append(t.zDownRotation);
		return s.toString();
	}
	public float[] colors = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	public Texture xUp, xDown, yUp, yDown, zUp, zDown;
	public int xUpRotation, xDownRotation, yUpRotation, yDownRotation, zUpRotation, zDownRotation;
	public CubeTextures duplicate(){
		CubeTextures c = new CubeTextures();
		c.xUp = xUp;
		c.xDown = xDown;
		c.yUp = yUp;
		c.yDown = yDown;
		c.zUp = zUp;
		c.zDown = zDown;
		for(int i = 0; i<colors.length; i++)
			c.colors[i] = colors[i];
		c.xUpRotation = xUpRotation;
		c.xDownRotation = xDownRotation;
		c.yUpRotation = yUpRotation;
		c.yDownRotation = yDownRotation;
		c.zUpRotation = zUpRotation;
		c.zDownRotation = zDownRotation;
		return c;
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
	public Texture getTexture(int side){
		if(side==0)return xUp;
		if(side==1)return xDown;
		if(side==2)return yUp;
		if(side==3)return yDown;
		if(side==4)return zUp;
		if(side==5)return zDown;
		return null;
	}
	public void set(CubeTextures textures){
		xUp = textures.xUp;
		xDown = textures.xDown;
		yUp = textures.yUp;
		yDown = textures.yDown;
		zUp = textures.zUp;
		zDown = textures.zDown;
		for(int i = 0; i<colors.length; i++)
			colors[i] = textures.colors[i];
		xUpRotation = textures.xUpRotation;
		xDownRotation = textures.xDownRotation;
		yUpRotation = textures.yUpRotation;
		yDownRotation = textures.yDownRotation;
		zUpRotation = textures.zUpRotation;
		zDownRotation = textures.zDownRotation;
	}
	public void setColor(int side, float r, float g, float b){
		colors[side*3] = r;
		colors[side*3+1] = g;
		colors[side*3+2] = b;
	}
	public void setTexture(int side, Texture texture, int rotation){
		if(side==0){
			xUp = texture;
			xUpRotation = rotation;
		}
		if(side==1){
			xDown = texture;
			xDownRotation = rotation;
		}
		if(side==2){
			yUp = texture;
			yUpRotation = rotation;
		}
		if(side==3){
			yDown = texture;
			yDownRotation = rotation;
		}
		if(side==4){
			zUp = texture;
			zUpRotation = rotation;
		}
		if(side==5){
			zDown = texture;
			zDownRotation = rotation;
		}
	}
	public boolean transparent(){
		return xUp.transparent||xDown.transparent||yUp.transparent||yDown.transparent||zUp.transparent||zDown.transparent;
	}
}