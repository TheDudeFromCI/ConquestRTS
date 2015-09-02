package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.StaticEntity;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassPatch;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassTransform;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.Utility.BinaryFile;

public class LandscapeChunk{
	public static final int LandscapeSize = 64;
	private final int x;
	private final int y;
	private final int z;
	private final int vbo;
	private final int ibo;
	private final int textureId;
	private final int indexCount;
	private final ArrayList<StaticEntity> plantLife;
	private final GrassPatch[] grassPatches;
	private final EntityDatabase entityDatabase;
	private final Grasslands grassLands;
	LandscapeChunk(EntityDatabase entityDatabase, Grasslands grassLands, int x, int y, int z, File file){
		System.out.println("Chunk loaded.");
		this.x = x;
		this.y = y;
		this.z = z;
		this.entityDatabase = entityDatabase;
		this.grassLands = grassLands;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		textureId = GL11.glGenTextures();
		plantLife = new ArrayList();
		{
			// ---
			// Load this chunk, or generate if nessicary.
			// ---
			BinaryFile bin = new BinaryFile(file);
			bin.decompress(false);
			int vertexCount = bin.getInt();
			indexCount = bin.getInt();
			ByteBuffer vertexData = BufferUtils.createByteBuffer(vertexCount*13);
			IntBuffer indexData = BufferUtils.createIntBuffer(indexCount);
			int i, a;
			for(i = 0; i<vertexCount; i++){
				vertexData.putFloat(bin.getFloat());
				vertexData.putFloat(bin.getFloat());
				vertexData.putFloat(bin.getFloat());
				vertexData.put(bin.getByte());
			}
			for(i = 0; i<indexCount; i++)
				indexData.put(bin.getInt());
			vertexData.flip();
			indexData.flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
			int plantLifeTypes = bin.getInt();
			int locationCount;
			for(i = 0; i<plantLifeTypes; i++){
				EntityType type = EntityType.values()[bin.getInt()];
				locationCount = bin.getInt();
				for(a = 0; a<locationCount; a++){
					float vx = bin.getFloat();
					float vy = bin.getFloat();
					float vz = bin.getFloat();
					float scale = bin.getFloat();
					float yaw = bin.getFloat();
					if(entityDatabase!=null){
						StaticEntity e = new StaticEntity(type);
						e.moveTo(vx, vy, vz);
						e.scaleTo(scale);
						e.setYaw(yaw);
						plantLife.add(e);
						entityDatabase.addEntity(e);
					}
				}
			}
			int grassPatchCount = bin.getInt();
			if(grassLands==null)
				grassPatches = null;
			else
				grassPatches = new GrassPatch[grassPatchCount];
			for(i = 0; i<grassPatchCount; i++){
				ArrayList<GrassTransform> locations = new ArrayList();
				EntityType grassType = EntityType.values()[bin.getInt()];
				locationCount = bin.getInt();
				for(a = 0; a<locationCount; a++)
					locations.add(new GrassTransform(bin.getFloat(), bin.getFloat(), bin.getFloat(), bin
						.getFloat(), bin.getFloat()));
				if(grassLands!=null){
					grassPatches[i] = new GrassPatch(grassType, locations, x, z);
					grassLands.addPatch(grassPatches[i]);
				}
			}
			{
				// ---
				// Now load the 3D texture.
				// ---
				GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureId);
				GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
				GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
				GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
				GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				int byteCount = 64*64*64*3;
				ByteBuffer pixels = BufferUtils.createByteBuffer(byteCount);
				for(i = 0; i<byteCount; i++)
					pixels.put(bin.getByte());
				pixels.flip();
				GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGB8, 64, 64, 64, 0, GL11.GL_RGB,
					GL11.GL_UNSIGNED_BYTE, pixels);
			}
		}
		GlError.dumpError();
	}
	void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		GL11.glDeleteTextures(textureId);
		GlError.dumpError();
		if(entityDatabase!=null)
			for(StaticEntity batch : plantLife){
				batch.dispose();
				entityDatabase.removeEntity(batch);
			}
		if(grassPatches!=null)
			for(GrassPatch patch : grassPatches)
				grassLands.removePatch(patch);
	}
	int getX(){
		return x;
	}
	int getY(){
		return y;
	}
	int getZ(){
		return z;
	}
	void render(){
		GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 13, 0);
		GL20.glVertexAttribPointer(LandscapeWorld.ShadeAttribLocation, 1, GL11.GL_UNSIGNED_BYTE, true, 13, 12);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
		GlError.dumpError();
	}
}
