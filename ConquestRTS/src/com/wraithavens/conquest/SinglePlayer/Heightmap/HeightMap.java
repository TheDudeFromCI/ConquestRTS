package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;

class HeightMap{
	static final int VertexCount = 1024;
	static final int ViewDistance = 16384;
	static final int Vertices = VertexCount*VertexCount;
	static final int VertexSize = 9;
	private static final int indexCount = (VertexCount*2+2)*(VertexCount-1)-2;
	private int posX;
	private int posZ;
	private final WorldNoiseMachine machine;
	private final int vbo;
	private final int ibo;
	public HeightMap(WorldNoiseMachine machine){
		this.machine = machine;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		buildIndexData();
		rebuild();
	}
	public int getX(){
		return posX;
	}
	public int getZ(){
		return posZ;
	}
	private void buildIndexData(){
		IntBuffer indexData = BufferUtils.createIntBuffer(indexCount);
		final int TriangleRows = VertexCount-1;
		final int VerticesPerRow = VertexCount*2;
		int x, y;
		int index;
		for(y = 0; y<TriangleRows; y++){
			if(y>0)
				// ---
				// Double first index.
				// ---
				indexData.put(y*VertexCount);
			for(x = 0; x<VerticesPerRow; x++){
				index = y*VertexCount;
				index += x/2;
				if(x%2==1)
					index += VertexCount;
				indexData.put(index);
			}
			if(y<TriangleRows-1){
				// ---
				// Repeat last index.
				// ---
				index = (y+1)*VertexCount;
				index += (VerticesPerRow-1)/2;
				indexData.put(index);
			}
		}
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
	}
	private void calculateNormal(float x, float z, Vector3f out){
		double z0 = machine.getWorldHeight(x-1, z+1)/machine.getMaxHeight();
		double z1 = machine.getWorldHeight(x, z+1)/machine.getMaxHeight();
		double z2 = machine.getWorldHeight(x+1, z+1)/machine.getMaxHeight();
		double z3 = machine.getWorldHeight(x-1, z)/machine.getMaxHeight();
		double z4 = machine.getWorldHeight(x, z)/machine.getMaxHeight();
		double z5 = machine.getWorldHeight(x-1, z-1)/machine.getMaxHeight();
		double z6 = machine.getWorldHeight(x, z-1)/machine.getMaxHeight();
		double z7 = machine.getWorldHeight(x+1, z-1)/machine.getMaxHeight();
		out.set((float)(z2+2.0f*z4+z7-z0-2.0f*z3-z5), (float)(1.0f/machine.getMaxHeight()), (float)(z5+2.0f*z6
			+z7-z0-2.0f*z1-z2));
		out.normalize();
	}
	private void rebuild(){
		System.out.println("Rebuilding heightmap.");
		long time = System.currentTimeMillis();
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(Vertices*VertexSize);
		int x, y;
		float s = ViewDistance*2/(VertexCount-1.0f);
		float blockX;
		float blockZ;
		Vector3f normal = new Vector3f();
		for(y = 0; y<VertexCount; y++)
			for(x = 0; x<VertexCount; x++){
				// ---
				// Get the vertex information in real-world.
				// ---
				blockX = x*s-ViewDistance/2;
				blockZ = y*s-ViewDistance/2;
				calculateNormal(blockX, blockZ, normal);
				// ---
				// Bind the vertex location
				// ---
				vertexData.put(blockX);
				vertexData.put(Math.round(machine.getWorldHeight(blockX, blockZ)));
				vertexData.put(blockZ);
				// ---
				// Bind the vertex color.
				// ---
				vertexData.put(1.0f);
				vertexData.put(1.0f);
				vertexData.put(1.0f);
				// ---
				// Bind the vertex normal.
				// ---
				vertexData.put(normal.x);
				vertexData.put(normal.y);
				vertexData.put(normal.z);
			}
		vertexData.flip();
		// ---
		// Now compile and build the vertex data. Here I use static draw instead
		// of dynamic draw because of how rarely the heightmap needs to be
		// reloaded.
		// ---
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
		System.out.println("Finished in "+(System.currentTimeMillis()-time)+" ms.");
	}
	void dispose(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(ibo);
	}
	void render(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 36, 0);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 36, 12);
		GL11.glNormalPointer(GL11.GL_FLOAT, 36, 24);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, indexCount, GL11.GL_UNSIGNED_INT, 0);
	}
	void update(int x, int z){
		posX = x;
		posZ = z;
		rebuild();
	}
}
