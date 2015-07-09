package com.wraithavens.conquest.SinglePlayer;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public class QuadBatch{
	private static final int MAX_SIZE = 500;
	private int elementCount, indexCount;
	private final int indexBufferId;
	private ArrayList<Quad> quads = new ArrayList();
	private final Texture texture;
	private final int textureCoordBufferId;
	private final int vertexBufferId;
	private final int normalBufferId;
	QuadBatch(Texture texture){
		this.texture = texture;
		vertexBufferId = GL15.glGenBuffers();
		textureCoordBufferId = GL15.glGenBuffers();
		indexBufferId = GL15.glGenBuffers();
		normalBufferId = GL15.glGenBuffers();
	}
	void addQuad(Quad q){
		quads.add(q);
	}
	void cleanUp(){
		if(GL11.glGetInteger(GL15.GL_VERTEX_ARRAY_BUFFER_BINDING)==vertexBufferId)GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		if(GL11.glGetInteger(GL15.GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING)==textureCoordBufferId)GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		if(GL11.glGetInteger(GL15.GL_NORMAL_ARRAY_BUFFER_BINDING)==normalBufferId)GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		if(GL11.glGetInteger(GL15.GL_INDEX_ARRAY_BUFFER_BINDING)==indexBufferId)GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vertexBufferId);
		GL15.glDeleteBuffers(textureCoordBufferId);
		GL15.glDeleteBuffers(normalBufferId);
		GL15.glDeleteBuffers(indexBufferId);
		System.out.println("Disposed data buffers: "+vertexBufferId+", "+textureCoordBufferId+", "+indexBufferId+", "+normalBufferId);
	}
	public Texture getTexture(){
		return texture;
	}
	void compileBuffer(){
		int points = 0;
		int indices = 0;
		for(int i = 0; i<quads.size(); i++){
			points += 4;
			indices += 6;
		}
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(points*3);
		FloatBuffer textureCoordBuffer = BufferUtils.createFloatBuffer(points*2);
		FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(points*3);
		ShortBuffer indexBuffer = BufferUtils.createShortBuffer(indices);
		elementCount = 0;
		indexCount = 0;
		Quad q;
		for(int i = 0; i<quads.size(); i++){
			q = quads.get(i);
			vertexBuffer.put(q.data.get(8)).put(q.data.get(9)).put(q.data.get(10));
			textureCoordBuffer.put(q.data.get(0)).put(q.data.get(1));
			if(q.side==0)normalBuffer.put(1).put(0).put(0);
			else if(q.side==1)normalBuffer.put(-1).put(0).put(0);
			else if(q.side==2)normalBuffer.put(0).put(1).put(0);
			else if(q.side==3)normalBuffer.put(0).put(-1).put(0);
			else if(q.side==4)normalBuffer.put(0).put(0).put(1);
			else normalBuffer.put(0).put(0).put(-1);
			vertexBuffer.put(q.data.get(11)).put(q.data.get(12)).put(q.data.get(13));
			textureCoordBuffer.put(q.data.get(2)).put(q.data.get(3));
			if(q.side==0)normalBuffer.put(1).put(0).put(0);
			else if(q.side==1)normalBuffer.put(-1).put(0).put(0);
			else if(q.side==2)normalBuffer.put(0).put(1).put(0);
			else if(q.side==3)normalBuffer.put(0).put(-1).put(0);
			else if(q.side==4)normalBuffer.put(0).put(0).put(1);
			else normalBuffer.put(0).put(0).put(-1);
			vertexBuffer.put(q.data.get(14)).put(q.data.get(15)).put(q.data.get(16));
			textureCoordBuffer.put(q.data.get(4)).put(q.data.get(5));
			if(q.side==0)normalBuffer.put(1).put(0).put(0);
			else if(q.side==1)normalBuffer.put(-1).put(0).put(0);
			else if(q.side==2)normalBuffer.put(0).put(1).put(0);
			else if(q.side==3)normalBuffer.put(0).put(-1).put(0);
			else if(q.side==4)normalBuffer.put(0).put(0).put(1);
			else normalBuffer.put(0).put(0).put(-1);
			vertexBuffer.put(q.data.get(17)).put(q.data.get(18)).put(q.data.get(19));
			textureCoordBuffer.put(q.data.get(6)).put(q.data.get(7));
			if(q.side==0)normalBuffer.put(1).put(0).put(0);
			else if(q.side==1)normalBuffer.put(-1).put(0).put(0);
			else if(q.side==2)normalBuffer.put(0).put(1).put(0);
			else if(q.side==3)normalBuffer.put(0).put(-1).put(0);
			else if(q.side==4)normalBuffer.put(0).put(0).put(1);
			else normalBuffer.put(0).put(0).put(-1);
			indexBuffer.put((short)(elementCount));
			indexBuffer.put((short)(elementCount+1));
			indexBuffer.put((short)(elementCount+2));
			indexBuffer.put((short)(elementCount));
			indexBuffer.put((short)(elementCount+2));
			indexBuffer.put((short)(elementCount+3));
			elementCount += 4;
			indexCount += 6;
		}
		vertexBuffer.flip();
		textureCoordBuffer.flip();
		normalBuffer.flip();
		indexBuffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureCoordBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureCoordBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
		quads = null;
	}
	public void renderPart(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureCoordBufferId);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBufferId);
		GL11.glNormalPointer(GL11.GL_FLOAT, 12, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_SHORT, 0);
	}
	boolean isFull(){
		return quads.size()>=QuadBatch.MAX_SIZE;
	}
}