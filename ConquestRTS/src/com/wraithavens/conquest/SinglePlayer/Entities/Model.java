package com.wraithavens.conquest.SinglePlayer.Entities;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.CompactBinaryFile;

public class Model{
	public static Model load(String fileName){
		CompactBinaryFile file = new CompactBinaryFile(WraithavensConquest.modelFolder, fileName);
		if(!file.exists())throw new RuntimeException("Model not found!");
		file.read();
		int vertexCount = (int)file.getNumber(32);
		FloatBuffer data = BufferUtils.createFloatBuffer(vertexCount*7);
		for(int i = 0; i<vertexCount; i++){
			data.put(file.getFloat());
			data.put(file.getFloat());
			data.put(file.getFloat());
			data.put(file.getFloat());
			data.put(file.getFloat());
			data.put(file.getFloat());
			data.put((float)Math.random());
		}
		int indexCount = (int)file.getNumber(32);
		ShortBuffer indexData = BufferUtils.createShortBuffer(indexCount);
		for(int i = 0; i<indexCount; i++)
			indexData.put((short)file.getNumber(16));
		file.stopReading();
		data.flip();
		indexData.flip();
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
		return new Model(vbo, ibo, indexCount);
	}
	private final int vbo;
	private final int ibo;
	private final int size;
	private int references = 0;
	private Model(int vbo, int ibo, int size){
		this.vbo = vbo;
		this.ibo = ibo;
		this.size = size;
	}
	void render(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 28, 0);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 28, 12);
		GL20.glVertexAttribPointer(ModelGroup.LOCATION, 1, GL11.GL_FLOAT, false, 28, 24);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL15.GL_ELEMENT_ARRAY_BUFFER, size, GL11.GL_UNSIGNED_SHORT, 0);
	}
	private void dispose(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
	}
	void addReference(){
		references++;
	}
	void removeReference(){
		references--;
		if(references==0)dispose();
	}
}