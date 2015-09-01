package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class EntityMesh{
	private final EntityType type;
	int references = 0;
	private final int vbo;
	private final int ibo;
	private final int textureColorsId;
	private final int indexCount;
	private final int dataType;
	private final Vector3f aabbMin;
	private final Vector3f aabbMax;
	private final Vector3f textureOffset3D = new Vector3f();
	private final Vector3f textureSize3D = new Vector3f();
	EntityMesh(EntityType type){
		this.type = type;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		{
			File file = new File(WraithavensConquest.modelFolder, type.fileName);
			BinaryFile bin = new BinaryFile(file);
			bin.decompress(true);
			// ---
			// Get the type of mesh this TAL file represents.
			// 0 = Normal Static Mesh
			// 1 = Mesh with bones. (TODO)
			// 2 = Giant Object. (Has 3D Color Texture.)
			// ----
			byte meshType = bin.getByte();
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
			int xSize = 0;
			int ySize = 0;
			int zSize = 0;
			int byteCount = 0;
			{
				// ---
				// Load the 3d texture, if it exists.
				// ---
				if(meshType==2){
					textureColorsId = GL11.glGenTextures();
					GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureColorsId);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
					// ---
					// Get the total size of the texture.
					// ---
					xSize = bin.getInt();
					ySize = bin.getInt();
					zSize = bin.getInt();
					// ---
					// Now get the object's position in the texture.
					// ---
					textureSize3D.set(xSize, ySize, zSize);
					textureOffset3D.set(bin.getFloat(), bin.getFloat(), bin.getFloat());
					// ---
					// And load the color data.
					// ---
					byteCount = xSize*ySize*zSize*3;
					ByteBuffer pixels = BufferUtils.createByteBuffer(byteCount);
					for(int i = 0; i<byteCount; i++)
						pixels.put(bin.getByte());
					pixels.flip();
					GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGB8, xSize, ySize, zSize, 0, GL11.GL_RGB,
						GL11.GL_UNSIGNED_BYTE, pixels);
				}else
					textureColorsId = 0;
			}
			aabbMin = new Vector3f(bin.getFloat(), bin.getFloat(), bin.getFloat());
			aabbMax = new Vector3f(bin.getFloat(), bin.getFloat(), bin.getFloat());
			System.out.println("Loaded entity: "+type.fileName+".");
			System.out.println("  Vertex Count: "+vertexCount);
			System.out.println("  Index Count: "+indexCount+"  ("+indexCount/3+" tris) (Storage: "
				+(dataType==GL11.GL_UNSIGNED_SHORT?"Short":"Integer")+")");
			if(meshType==2)
				System.out.println("  3D Texture Size: "+xSize+" x "+ySize+" x "+zSize+"  (~"
					+Algorithms.formatBytes(byteCount)+")");
		}
	}
	private void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		if(textureColorsId!=0)
			GL11.glDeleteTextures(textureColorsId);
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
		if(textureColorsId!=0)
			GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureColorsId);
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
	Vector3f getTextureOffset3D(){
		return textureOffset3D;
	}
	Vector3f getTextureSize3D(){
		return textureSize3D;
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
