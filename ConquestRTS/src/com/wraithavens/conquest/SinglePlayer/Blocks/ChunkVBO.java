package com.wraithavens.conquest.SinglePlayer.Blocks;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class ChunkVBO{
	public final int vbo;
	public final int ibo;
	public final int indexCount;
	public ChunkVBO(int vbo, int ibo, int indexCount){
		this.vbo = vbo;
		this.ibo = ibo;
		this.indexCount = indexCount;
	}
	public void dispose(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
	}
	public void render(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 32, 12);
		GL20.glVertexAttribPointer(World.SHADER_LOCATION, 1, GL11.GL_FLOAT, false, 32, 24);
		GL20.glVertexAttribPointer(World.SHADER_LOCATION_2, 1, GL11.GL_FLOAT, false, 32, 28);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_SHORT, 0);
	}
}
