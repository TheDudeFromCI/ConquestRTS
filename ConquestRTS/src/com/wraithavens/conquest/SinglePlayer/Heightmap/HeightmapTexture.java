package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

class HeightmapTexture{
	private static FloatBuffer generatePixelBuffer(File file){
		HeightmapFormat format = new HeightmapFormat(file);
		int size = WorldHeightmaps.TextureDetail*WorldHeightmaps.TextureDetail;
		FloatBuffer buffer = BufferUtils.createFloatBuffer(size*4);
		format.beginReading();
		float[] color = new float[4];
		for(int i = 0; i<size; i++){
			format.read(color);
			buffer.put(color);
		}
		format.stopReading();
		buffer.flip();
		return buffer;
	}
	private static FloatBuffer generatePixelBuffer(HeightmapRaw raw){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(raw.getColors().length);
		buffer.put(raw.getColors());
		buffer.flip();
		return buffer;
	}
	private static int loadTexture(File file){
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, WorldHeightmaps.TextureDetail,
			WorldHeightmaps.TextureDetail, 0, GL11.GL_RGBA, GL11.GL_FLOAT, generatePixelBuffer(file));
		return textureID;
	}
	private static int loadTexture(HeightmapRaw raw){
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, WorldHeightmaps.TextureDetail,
			WorldHeightmaps.TextureDetail, 0, GL11.GL_RGBA, GL11.GL_FLOAT, generatePixelBuffer(raw));
		return textureID;
	}
	private final int textureId;
	public HeightmapTexture(File file){
		textureId = loadTexture(file);
	}
	public HeightmapTexture(HeightmapRaw raw){
		textureId = loadTexture(raw);
	}
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	public void dispose(){
		if(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)==textureId)
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDeleteTextures(textureId);
		System.out.println("Disposed heightmap.");
	}
}
