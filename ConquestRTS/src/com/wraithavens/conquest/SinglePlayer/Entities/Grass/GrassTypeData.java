package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

class GrassTypeData{
	private final int buffer;
	private final int texture;
	private FloatBuffer data;
	private int count;
	private int references;
	GrassTypeData(int texture){
		this.texture = texture;
		buffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
	}
	void addReference(){
		references++;
	}
	FloatBuffer allocateData(int count){
		this.count = count;
		count *= 5;
		if(data==null||data.capacity()<count)
			data = BufferUtils.createFloatBuffer(count);
		data.clear();
		return data;
	}
	void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
	}
	void dispose(){
		GL11.glDeleteTextures(texture);
		GL15.glDeleteBuffers(buffer);
	}
	int getCount(){
		return count;
	}
	void recompile(){
		data.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STREAM_DRAW);
	}
	boolean removeReferences(){
		references--;
		return references<=0;
	}
}
