package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class EntityMesh{
	private final EntityType type;
	int references = 0;
	private final int vbo;
	private final int ibo;
	private final int vbo2;
	private final int ibo2;
	private final int textureColorsId;
	private final int indexCount;
	private final int dataType;
	private final int dynmapIndices;
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
			// 1 Bit = Has Bones? (TODO)
			// 2 Bit = Is Color Blended?
			// 4 Bit = Has Decimated Model?
			// ----
			byte meshType = bin.getByte();
			// boolean hasBones = (meshType&1)==1;
			boolean isColorBlended = (meshType&2)==2;
			boolean hasDecimatedModel = (meshType&4)==4;
			// boolean hasBillboardImage = (meshType&8)==8;
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
				if(isColorBlended){
					textureColorsId = GL11.glGenTextures();
					GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureColorsId);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
					GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_NEAREST_MIPMAP_LINEAR);
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
			int decimationSize = 0;
			if(hasDecimatedModel){
				int vc = bin.getInt();
				int ic = bin.getInt();
				decimationSize += vc*13+ic*2;
				dynmapIndices = ic;
				vbo2 = GL15.glGenBuffers();
				ibo2 = GL15.glGenBuffers();
				ByteBuffer vertexData = BufferUtils.createByteBuffer(vc*13);
				ShortBuffer indexData = BufferUtils.createShortBuffer(ic);
				int i;
				for(i = 0; i<vc; i++){
					vertexData.putFloat(bin.getFloat());
					vertexData.putFloat(bin.getFloat());
					vertexData.putFloat(bin.getFloat());
					vertexData.put(bin.getByte());
				}
				for(i = 0; i<ic; i++)
					indexData.put(bin.getShort());
				vertexData.flip();
				indexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo2);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo2);
				GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
			}else{
				vbo2 = 0;
				ibo2 = 0;
				dynmapIndices = 0;
			}
			long totalSize =
				vertexCount*16L+indexCount*(dataType==GL11.GL_UNSIGNED_SHORT?2L:4L)+byteCount+decimationSize;
			System.out.println("Loaded entity: "+type.fileName+".  (~"+Algorithms.formatBytes(totalSize)+")");
		}
	}
	public void bind(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 16, 0);
		GL20.glVertexAttribPointer(EntityDatabase.SingularShaderAttrib, 1, GL11.GL_UNSIGNED_BYTE, true, 16, 12);
		GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, 16, 13);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		if(textureColorsId!=0)
			GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureColorsId);
	}
	public void drawStatic(){
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, dataType, 0);
	}
	public void dynmapBatchBind(int shadeAtttribLocation){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo2);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 13, 0);
		GL20.glVertexAttribPointer(shadeAtttribLocation, 1, GL11.GL_UNSIGNED_BYTE, true, 13, 12);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo2);
		GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureColorsId);
	}
	public Vector3f getAabbMax(){
		return aabbMax;
	}
	public Vector3f getAabbMin(){
		return aabbMin;
	}
	public int getDataType(){
		return dataType;
	}
	public int getDynmapIndexCount(){
		return dynmapIndices;
	}
	public int getIndexCount(){
		return indexCount;
	}
	public Vector3f getTextureOffset3D(){
		return textureOffset3D;
	}
	public Vector3f getTextureSize3D(){
		return textureSize3D;
	}
	public EntityType getType(){
		return type;
	}
	public void removeReference(){
		references--;
		if(references==0){
			dispose();
			type.mesh = null;
		}
	}
	private void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		if(vbo2!=0){
			GL15.glDeleteBuffers(vbo2);
			GL15.glDeleteBuffers(ibo2);
		}
		if(textureColorsId!=0)
			GL11.glDeleteTextures(textureColorsId);
		System.out.println(type.fileName+" disposed.");
	}
	void addReference(){
		references++;
	}
	int getId(){
		return type.ordinal();
	}
}
