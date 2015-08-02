package com.wraithavens.conquest.SinglePlayer.Blocks;

import org.lwjgl.opengl.GL15;

public class VBO{
	public final int vbo;
	public final int ibo;
	public final int indexCount;
	public VBO(int vbo, int ibo, int indexCount){
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
}
