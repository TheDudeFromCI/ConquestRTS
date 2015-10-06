package com.wraithavens.conquest.SinglePlayer.Entities.Water;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class WaterPuddle{
	private static void print(FloatBuffer buf, ShortBuffer in){
		StringBuilder sb = new StringBuilder();
		sb.append("Water Puddle:\n\nVertices\n\n");
		for(int i = 0; i<buf.capacity(); i++)
			sb.append(buf.get(i)+"\n");
		sb.append("\nIndices\n\n");
		for(int i = 0; i<in.capacity(); i++)
			sb.append(in.get(i)+"\n");
		sb.append("\n\n");
		System.out.println(sb.toString());
	}
	private final int vbo;
	private final int ibo;
	private final int count;
	private final int x;
	private final int y;
	private final int z;
	public WaterPuddle(FloatBuffer vertexData, ShortBuffer indexData, int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		count = indexData.capacity();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
		// print(vertexData, indexData);
		System.out.println("Count: "+count);
	}
	void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
	}
	int getX(){
		return x;
	}
	int getY(){
		return y;
	}
	int getZ(){
		return z;
	}
	void render(int uvAttribLocation){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
		GL20.glVertexAttribPointer(uvAttribLocation, 2, GL11.GL_FLOAT, false, 20, 12);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, count, GL11.GL_UNSIGNED_SHORT, 0);
	}
}
