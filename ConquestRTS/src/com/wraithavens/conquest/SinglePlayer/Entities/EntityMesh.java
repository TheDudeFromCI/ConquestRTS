package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.Utility.BinaryFile;

public class EntityMesh{
	private final EntityType type;
	int references = 0;
	private final int vbo;
	private final int ibo;
	private final int indexCount;
	private final int dataType;
	private final Vector3f aabbMin;
	private final Vector3f aabbMax;
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
				ByteBuffer vertexData = BufferUtils.createByteBuffer(vertexCount*16);
				for(int i = 0; i<vertexCount; i++){
					vertexData.putFloat(bin.getFloat());
					vertexData.putFloat(bin.getFloat());
					vertexData.putFloat(bin.getFloat());
					vertexData.put(bin.getByte());
					vertexData.put(bin.getByte());
					vertexData.put(bin.getByte());
					vertexData.put(bin.getByte());
				}
				vertexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
				dataType = vertexCount<65536?GL11.GL_UNSIGNED_SHORT:GL11.GL_UNSIGNED_INT;
				GlError.dumpError();
			}
			{
				indexCount = bin.getInt();
				if(dataType==GL11.GL_UNSIGNED_SHORT){
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
			aabbMin = new Vector3f(bin.getFloat(), bin.getFloat(), bin.getFloat());
			aabbMax = new Vector3f(bin.getFloat(), bin.getFloat(), bin.getFloat());
			GlError.out("Loaded entity: "+type.fileName+".");
			GlError.out("  Vertex Count: "+vertexCount);
			GlError.out("  Index Count: "+indexCount+"  ("+indexCount/3+" tris) (Storage: "
				+(dataType==GL11.GL_UNSIGNED_SHORT?"Short":"Integer")+")");
		}
	}
	private void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		GlError.out(type.fileName+" disposed.");
	}
	void addReference(){
		references++;
	}
	void bind(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 16, 0);
		GL20.glVertexAttribPointer(EntityDatabase.SingularShaderAttrib, 1, GL11.GL_UNSIGNED_BYTE, true, 16, 12);
		GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, 16, 13);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GlError.dumpError();
	}
	void drawStatic(){
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, dataType, 0);
		GlError.dumpError();
	}
	Vector3f getAabbMax(){
		return aabbMax;
	}
	Vector3f getAabbMin(){
		return aabbMin;
	}
	int getId(){
		return type.ordinal();
	}
	EntityType getType(){
		return type;
	}
	void removeReference(){
		references--;
		if(references==0){
			dispose();
			type.mesh = null;
		}
		GlError.dumpError();
	}
}
