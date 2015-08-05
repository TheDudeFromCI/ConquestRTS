package com.wraithavens.conquest.SinglePlayer.Skybox;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.Math.Vector4f;
import com.wraithavens.conquest.SinglePlayer.Noise.CloudNoise;

public class SkyboxClouds{
	public static final int TextureSize = 512;
	private static final Vector4f temp = new Vector4f();
	private static final Vector3f temp2 = new Vector3f();
	private final CloudNoise noise;
	private final Vector3f skyColor;
	private final int textureId;
	private final boolean backdrop;
	private float spinSpeed = 0.0f;
	private float angle = 0.0f;
	SkyboxClouds(CloudNoise noise, boolean backdrop){
		this.noise = noise;
		this.backdrop = backdrop;
		skyColor = new Vector3f(0.4f, 0.6f, 0.9f);
		textureId = GL11.glGenTextures();
		createTexture();
		randomize();
	}
	public void setSpinSpeed(float spinSpeed){
		this.spinSpeed = spinSpeed;
	}
	private void blendOver(){
		if(!backdrop)
			return;
		if(temp.w>=1.0f){
			temp.set(temp.x, temp.y, temp.z, 1.0f);
			return;
		}
		temp.set(temp.x*temp.w+skyColor.x*(1.0f-temp.w), temp.y*temp.w+skyColor.y*(1.0f-temp.w), temp.z*temp.w
			+skyColor.z*(1.0f-temp.w), 1.0f);
	}
	private void compile(int side, FloatBuffer data){
		int i;
		if(side==0)
			i = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
		else if(side==1)
			i = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
		else if(side==2)
			i = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
		else if(side==3)
			i = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
		else if(side==4)
			i = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
		else
			i = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
		GL11.glTexImage2D(i, 0, backdrop?GL11.GL_RGB8:GL11.GL_RGBA8, TextureSize, TextureSize, 0, backdrop
			?GL11.GL_RGB:GL11.GL_RGBA, GL11.GL_FLOAT, data);
	}
	private void createTexture(){
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureId);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	}
	private void makeSide(int side, FloatBuffer data){
		int x, y, z;
		if(side==0){
			x = TextureSize-1;
			for(y = TextureSize-1; y>=0; y--)
				for(z = TextureSize-1; z>=0; z--)
					placeColor(x, y, z, data);
		}else if(side==1){
			x = 0;
			for(y = TextureSize-1; y>=0; y--)
				for(z = 0; z<TextureSize; z++)
					placeColor(x, y, z, data);
		}else if(side==2){
			y = TextureSize-1;
			for(z = 0; z<TextureSize; z++)
				for(x = 0; x<TextureSize; x++)
					placeColor(x, y, z, data);
		}else if(side==3){
			y = 0;
			for(z = TextureSize-1; z>=0; z--)
				for(x = 0; x<TextureSize; x++)
					placeColor(x, y, z, data);
		}else if(side==4){
			z = TextureSize-1;
			for(y = TextureSize-1; y>=0; y--)
				for(x = 0; x<TextureSize; x++)
					placeColor(x, y, z, data);
		}else{
			z = 0;
			for(y = TextureSize-1; y>=0; y--)
				for(x = TextureSize-1; x>=0; x--)
					placeColor(x, y, z, data);
		}
		data.flip();
		compile(side, data);
	}
	private void placeColor(float x, float y, float z, FloatBuffer data){
		temp2.set(x-TextureSize/2, y-TextureSize/2, z-TextureSize/2);
		temp2.normalize();
		temp2.scale(TextureSize);
		noise.noise(temp2.x, temp2.y, temp2.z, temp);
		blendOver();
		data.put(temp.x);
		data.put(temp.y);
		data.put(temp.z);
		if(!backdrop)
			data.put(temp.w);
	}
	void randomize(){
		FloatBuffer data = BufferUtils.createFloatBuffer(TextureSize*TextureSize*(backdrop?3:4));
		for(int i = 0; i<6; i++)
			makeSide(i, data);
	}
	void render(){
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureId);
		GL11.glPushMatrix();
		GL11.glRotatef(angle, 0, 1, 0);
		GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
		GL11.glPopMatrix();
	}
	void update(double time){
		angle = (float)(time*spinSpeed);
	}
}
