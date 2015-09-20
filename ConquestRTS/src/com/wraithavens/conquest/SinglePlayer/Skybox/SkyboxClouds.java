package com.wraithavens.conquest.SinglePlayer.Skybox;

import java.io.File;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.BinaryFile;

public class SkyboxClouds{
	private static void compile(int side, ByteBuffer data, boolean backdrop){
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
			?GL11.GL_RGB:GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
	}
	private static void load(boolean backdrop, int weatherType){
		int skyId = (int)(Math.random()*CloudCombinationCount);
		File file =
			new File(WraithavensConquest.assetFolder+File.separatorChar+"Sky", skyId+(backdrop?"b":"a")
				+weatherType+".dat");
		BinaryFile bin = new BinaryFile(file);
		ByteBuffer data = BufferUtils.createByteBuffer(TextureSize*TextureSize*(backdrop?3:4));
		int floats = TextureSize*TextureSize*(backdrop?3:4);
		int i, j;
		for(i = 0; i<6; i++){
			for(j = 0; j<floats; j++)
				data.put(bin.getByte());
			data.flip();
			compile(i, data, backdrop);
		}
	}
	public static final int TextureSize = 128;
	public static final int CloudCombinationCount = 5;
	public static final int LayerCount = 0;
	private final int textureId;
	private final float spinSpeed;
	private float angle = 0.0f;
	public SkyboxClouds(boolean backdrop, float spinSpeed, int weatherType){
		this.spinSpeed = spinSpeed;
		textureId = GL11.glGenTextures();
		createTexture();
		load(backdrop, weatherType);
	}
	private void createTexture(){
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureId);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	}
	void dispose(){
		GL11.glDeleteTextures(textureId);
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
