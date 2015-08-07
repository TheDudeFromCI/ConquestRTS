package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.BinaryFile;

public class EntityMesh{
	private final EntityType type;
	int references = 0;
	private final int vbo;
	private final int ibo;
	private final int indexCount;
	private final int dataType;
	private final float aabbSize;
	EntityMesh(EntityType type){
		this.type = type;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		{
			File file = new File(WraithavensConquest.modelFolder, type.fileName);
			BinaryFile bin = new BinaryFile(file);
			// ---
			// This slot would normally check to see if the mesh was boneless or
			// not. Because I don't currently have support for layered meshes, I
			// can just ignore this values.
			// ---
			bin.getBoolean();
			int vertexCount = bin.getInt();
			{
				FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexCount*28);
				int vertexFloats = vertexCount*7;
				for(int i = 0; i<vertexFloats; i++)
					vertexData.put(bin.getFloat());
				vertexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
				dataType =
					vertexCount<256?GL11.GL_UNSIGNED_BYTE:vertexCount<65536?GL11.GL_UNSIGNED_SHORT
						:GL11.GL_UNSIGNED_INT;
			}
			{
				indexCount = bin.getInt();
				if(dataType==GL11.GL_UNSIGNED_BYTE){
					ByteBuffer indexData = BufferUtils.createByteBuffer(indexCount);
					for(int i = 0; i<indexCount; i++)
						indexData.put((byte)bin.getInt());
					indexData.flip();
					GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
					GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
				}else if(dataType==GL11.GL_UNSIGNED_SHORT){
					ShortBuffer indexData = BufferUtils.createShortBuffer(indexCount);
					for(int i = 0; i<indexCount; i++)
						indexData.put((short)bin.getInt());
					indexData.flip();
					GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
					GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
				}else{
					IntBuffer indexData = BufferUtils.createIntBuffer(indexCount);
					for(int i = 0; i<indexCount; i++)
						indexData.put(bin.getInt());
					indexData.flip();
					GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
					GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
				}
			}
			aabbSize = bin.getFloat();
			System.out.println("Loaded entity: "+type.fileName+".");
			System.out.println("  Vertex Count: "+vertexCount);
			System.out
				.println("  Index Count: "+indexCount+"  ("+indexCount/3+" tris) (Storage: "
					+(dataType==GL11.GL_UNSIGNED_BYTE?"Byte":dataType==GL11.GL_UNSIGNED_SHORT?"Short":"Integer")
					+")");
		}
	}
	private void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		System.out.println(type.fileName+" disposed.");
	}
	void addReference(){
		references++;
		System.out.println("Added reference to entity: '"+type.fileName+"'. References: "+references);
	}
	void bind(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 28, 0);
		GL20.glVertexAttribPointer(EntityDatabase.ShaderLocation, 1, GL11.GL_FLOAT, false, 28, 12);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 28, 16);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	void drawStatic(){
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, dataType, 0);
	}
	int getId(){
		return type.ordinal();
	}
	float getSize(){
		return aabbSize;
	}
	void removeReference(){
		references--;
		System.out.println("Removed reference to entity: '"+type.fileName+"'. References: "+references);
		if(references==0){
			dispose();
			type.mesh = null;
		}
	}
}
