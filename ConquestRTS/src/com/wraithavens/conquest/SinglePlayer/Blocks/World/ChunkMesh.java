package com.wraithavens.conquest.SinglePlayer.Blocks.World;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class ChunkMesh{
	private final int vbo;
	private final int ibo;
	private final int indexCount;
	private final int x;
	private final int y;
	private final int z;
	public ChunkMesh(FloatBuffer vertexData, ShortBuffer indexData, int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		indexCount = indexData.capacity();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
	}
	public void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
	}
	public void render(ShaderProgram shader){
		if(hiddenFromCamera())
			return;
		shader.setUniform3f(1, x, y, z);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 28, 0);
		GL20.glVertexAttribPointer(World.ShadeAttribLocation, 1, GL11.GL_FLOAT, false, 28, 12);
		GL20.glVertexAttribPointer(World.UvAttribLocation, 3, GL11.GL_FLOAT, false, 28, 16);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_SHORT, 0);
	}
	private boolean hiddenFromCamera(){
		return !SinglePlayerGame.INSTANCE.getCamera().cubeInView(x, y, z, 32);
	}
}
