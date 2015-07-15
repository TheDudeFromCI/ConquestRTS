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
		int vbo = GL15.glGenBuffers();
		int ibo = GL15.glGenBuffers();
		file.read();
		int vertexCount = (int)file.getNumber(32);
		FloatBuffer data = BufferUtils.createFloatBuffer(vertexCount*10);
		for(int i = 0; i<vertexCount; i++){
			data.put(file.getFloat());  //Vertex X
			data.put(file.getFloat());  //Vertex Y
			data.put(file.getFloat());  //Vertex Z
			data.put(file.getFloat());  //Red Value
			data.put(file.getFloat());  //Green Value
			data.put(file.getFloat());  //Blue Value
			data.put(file.getFloat());  //Normal X
			data.put(file.getFloat());  //Normal Y
			data.put(file.getFloat());  //Normal Z
			data.put((float)Math.random());  //Bone Weight Test
		}
		int indexCount = (int)file.getNumber(32);
		ShortBuffer indexData = BufferUtils.createShortBuffer(indexCount);
		for(int i = 0; i<indexCount; i++)
			indexData.put((short)file.getNumber(16));
		file.stopReading();
		data.flip();
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
		return new Model(vbo, ibo, indexCount);
	}
	private final int ibo;
	private final int vbo;
	private final int indexCount;
	private int references;
	private Model(int vbo, int ibo, int indexCount){
		this.vbo = vbo;
		this.ibo = ibo;
		this.indexCount = indexCount;
	}
	void render(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 40, 0);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 40, 12);
		GL11.glNormalPointer(GL11.GL_FLOAT, 40, 24);
		GL20.glVertexAttribPointer(ModelGroup.LOCATION, 1, GL11.GL_FLOAT, false, 40, 36);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_SHORT, 0);
	}
	private void dispose(){
		System.out.println("Model "+vbo+", "+ibo+" disposed.");
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
	}
	void addReference(){
		references++;
		System.out.println("Created reference to model: "+vbo+"-"+ibo);
		System.out.println("  Current references: "+references);
	}
	void removeReference(){
		references--;
		System.out.println("Removed reference to model: "+vbo+"-"+ibo);
		System.out.println("  Current references: "+references);
		if(references==0)dispose();
	}
}