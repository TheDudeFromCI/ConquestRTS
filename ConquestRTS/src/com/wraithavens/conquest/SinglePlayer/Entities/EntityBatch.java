package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.Utility.BinaryFile;

public class EntityBatch extends Entity{
	private final int vbo;
	private final int ibo;
	private final int indexCount;
	private final AABB aabb;
	public EntityBatch(EntityType type, ArrayList<Vector3f> positions){
		super(null);
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		GlError.dumpError();
		aabb = new AABB();
		{
			File file = new File(WraithavensConquest.modelFolder, type.fileName);
			BinaryFile bin = new BinaryFile(file);
			// ---
			// This slot would normally check to see if the mesh was boneless or
			// not. Because I don't currently have support for layered meshes, I
			// can just ignore this values.
			// ---
			bin.getBoolean();
			int vertexCount = 0;
			float minX = Integer.MAX_VALUE;
			float maxX = Integer.MIN_VALUE;
			float minY = Integer.MAX_VALUE;
			float maxY = Integer.MIN_VALUE;
			float minZ = Integer.MAX_VALUE;
			float maxZ = Integer.MIN_VALUE;
			{
				// ---
				// Load all vertices, relative to their new location.
				// ---
				vertexCount = bin.getInt();
				ByteBuffer vertexData = BufferUtils.createByteBuffer(vertexCount*16*positions.size());
				float[] tempData = new float[vertexCount*7];
				int i, j;
				float f;
				for(i = 0; i<tempData.length; i += 7){
					tempData[i+0] = bin.getFloat();
					tempData[i+1] = bin.getFloat();
					tempData[i+2] = bin.getFloat();
					tempData[i+3] = bin.getByte();
					tempData[i+4] = bin.getByte();
					tempData[i+5] = bin.getByte();
					tempData[i+6] = bin.getByte();
				}
				for(i = 0; i<positions.size(); i++)
					for(j = 0; j<tempData.length; j += 7){
						vertexData.putFloat(f = tempData[j+0]/20f+positions.get(i).x);
						if(f<minX)
							minX = f;
						if(f>maxX)
							maxX = f;
						vertexData.putFloat(f = tempData[j+1]/20f+positions.get(i).y);
						if(f<minY)
							minY = f;
						if(f>maxY)
							maxY = f;
						vertexData.putFloat(f = tempData[j+2]/20f+positions.get(i).z);
						if(f<minZ)
							minZ = f;
						if(f>maxZ)
							maxZ = f;
						vertexData.put((byte)tempData[j+3]);
						vertexData.put((byte)tempData[j+4]);
						vertexData.put((byte)tempData[j+5]);
						vertexData.put((byte)tempData[j+6]);
					}
				vertexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
				GlError.dumpError();
			}
			{
				// ---
				// Load all indices, and reference them to their correct
				// vertices.
				// ---
				int indexCount = bin.getInt();
				IntBuffer indexData = BufferUtils.createIntBuffer(indexCount*positions.size());
				int[] tempData = new int[indexCount];
				int i, j;
				for(i = 0; i<tempData.length; i++)
					tempData[i] = bin.getInt();
				int indexOffset;
				for(i = 0; i<positions.size(); i++){
					indexOffset = i*vertexCount;
					for(j = 0; j<tempData.length; j++)
						indexData.put(tempData[j]+indexOffset);
				}
				indexData.flip();
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
				GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
				GlError.dumpError();
				this.indexCount = indexCount*positions.size();
			}
			{
				// ---
				// Create the AABB.
				// ---
				float sizeX = maxX-minX;
				float sizeY = maxY-minY;
				float sizeZ = maxZ-minZ;
				float size = Math.max(Math.max(sizeX, sizeY), sizeZ);
				float centerX = sizeX/2+minX;
				float centerY = sizeY/2+minY;
				float centerZ = sizeZ/2+minZ;
				aabb.calculate(centerX, centerY, centerZ, size);
			}
		}
	}
	@Override
	public void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		GlError.dumpError();
	}
	@Override
	public void render(Camera camera){
		if(!aabb.visible(camera))
			return;
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 16, 0);
		GL20.glVertexAttribPointer(EntityDatabase.ShaderLocation, 1, GL11.GL_UNSIGNED_BYTE, true, 16, 12);
		GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, 16, 13);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
		GlError.dumpError();
	}
}
