package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Entities.AesiaStem;
import com.wraithavens.conquest.SinglePlayer.Entities.Entity;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassPatch;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassTransform;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Entities.Water.WaterPuddle;
import com.wraithavens.conquest.SinglePlayer.Entities.Water.WaterWorks;
import com.wraithavens.conquest.Utility.BinaryFile;

class LandscapeChunk{
	static final int LandscapeSize = 64;
	private final int x;
	private final int y;
	private final int z;
	private final int vbo;
	private final int ibo;
	private final int textureId;
	private final int indexCount;
	private final Entity[] plantLife;
	private final GrassPatch[] grassPatches;
	private final EntityDatabase entityDatabase;
	private final Grasslands grassLands;
	private WaterPuddle waterPuddle;
	LandscapeChunk(EntityDatabase entityDatabase, Grasslands grassLands, int x, int y, int z, File file){
		this.x = x;
		this.y = y;
		this.z = z;
		this.entityDatabase = entityDatabase;
		this.grassLands = grassLands;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		textureId = GL11.glGenTextures();
		{
			// ---
			// Load/Parse binary file.
			// ---
			BinaryFile bin = new BinaryFile(file);
			bin.decompress(false);
			int vertexCount = bin.getInt();
			indexCount = bin.getInt();
			int entityCount = bin.getInt();
			int grassCount = bin.getInt();
			int grassPatchCount = bin.getInt();
			boolean hasWater = bin.getBoolean();
			int waterVertexSize = hasWater?bin.getInt():0;
			int waterIndexSize = hasWater?bin.getInt():0;
			{
				// ---
				// Load mesh data.
				// ---
				FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexCount);
				ShortBuffer indexData = BufferUtils.createShortBuffer(indexCount);
				while(vertexData.hasRemaining())
					vertexData.put(bin.getFloat());
				while(indexData.hasRemaining())
					indexData.put(bin.getShort());
				vertexData.flip();
				indexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
				GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
			}
			{
				// ---
				// Load entity data.
				// ---
				plantLife = new Entity[entityCount];
				Entity entity;
				EntityType type;
				for(int i = 0; i<entityCount; i++){
					type = EntityType.values()[bin.getInt()];
					if(type.isAesiaStemType())
						entity = new AesiaStem(type);
					else
						entity = new Entity(type);
					entity.moveTo(bin.getFloat(), bin.getFloat(), bin.getFloat());
					entity.setYaw(bin.getFloat());
					entity.scaleTo(bin.getFloat());
					entity.updateAABB();
					entityDatabase.addEntity(entity);
					plantLife[i] = entity;
				}
			}
			{
				// ---
				// Load grass data.
				// ---
				grassPatches = new GrassPatch[grassPatchCount];
				if(grassPatchCount>0){
					ArrayList<GrassTransform> locations = null;
					EntityType lastType = null;
					EntityType currentType;
					int p = 0;
					for(int i = 0; i<grassCount; i++){
						currentType = EntityType.values()[bin.getInt()];
						if(currentType!=lastType){
							locations = new ArrayList();
							grassPatches[p] = new GrassPatch(currentType, locations, x, z);
							grassLands.addPatch(grassPatches[p]);
							p++;
							lastType = currentType;
						}
						locations.add(new GrassTransform(bin.getFloat(), bin.getFloat(), bin.getFloat(), bin
							.getFloat(), bin.getFloat(), bin.getFloat(), bin.getFloat(), bin.getFloat()));
					}
				}
			}
			{
				// ---
				// Load biome color data.
				// ---
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				int byteCount = 64*64*3;
				ByteBuffer pixels = BufferUtils.createByteBuffer(byteCount);
				while(pixels.hasRemaining())
					pixels.put(bin.getByte());
				pixels.flip();
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, 64, 64, 0, GL11.GL_RGB,
					GL11.GL_UNSIGNED_BYTE, pixels);
			}
			if(hasWater){
				// ---
				// Load water puddle.
				// ---
				FloatBuffer vertexData = BufferUtils.createFloatBuffer(waterVertexSize);
				ShortBuffer indexData = BufferUtils.createShortBuffer(waterIndexSize);
				while(vertexData.hasRemaining())
					vertexData.put(bin.getFloat());
				while(indexData.hasRemaining())
					indexData.put(bin.getShort());
				vertexData.flip();
				indexData.flip();
				WaterWorks waterWorks = SinglePlayerGame.INSTANCE.getWaterWorks();
				waterPuddle = new WaterPuddle(vertexData, indexData, x, y, z);
				waterWorks.addPuddle(waterPuddle);
			}
		}
	}
	void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		GL11.glDeleteTextures(textureId);
		for(Entity batch : plantLife){
			batch.dispose();
			entityDatabase.removeEntity(batch);
		}
		for(GrassPatch patch : grassPatches)
			grassLands.removePatch(patch);
		if(waterPuddle!=null){
			WaterWorks waterWorks = SinglePlayerGame.INSTANCE.getWaterWorks();
			waterWorks.removePuddle(waterPuddle);
		}
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
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 28, 0);
		GL20.glVertexAttribPointer(LandscapeWorld.ShadeAttribLocation, 1, GL11.GL_FLOAT, false, 28, 12);
		GL20.glVertexAttribPointer(LandscapeWorld.UvAttribLocation, 3, GL11.GL_FLOAT, false, 28, 16);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_SHORT, 0);
	}
}
