package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.MeshRenderer;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.BiomeColorDataPacket;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.ChunkDataPacket;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.ChunkPainter;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.EntityDataPacket;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.GrassDataPacket;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.MeshDataPacket;
import com.wraithavens.conquest.SinglePlayer.Entities.Entity;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassPatch;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Entities.Water.WaterPuddle;
import com.wraithavens.conquest.SinglePlayer.Entities.Water.WaterWorks;

class LandscapeChunk{
	static final int LandscapeSize = 64;
	private final int x;
	private final int y;
	private final int z;
	private final int vbo;
	private final int ibo;
	private final int textureId;
	private final ArrayList<Entity> plantLife = new ArrayList();
	private final ArrayList<GrassPatch> grassPatches = new ArrayList();
	private int indexCount;
	private WaterPuddle waterPuddle;
	LandscapeChunk(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		textureId = GL11.glGenTextures();
		ChunkPainter painter = new ChunkPainter();
		painter.load(x, y, z);
		for(ChunkDataPacket packet : painter.getPackets()){
			switch(packet.getPacketType()){
				case ChunkDataPacket.MeshDataPacket:
					MeshDataPacket meshData = (MeshDataPacket)packet;
					meshData.decompile();
					GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
					GL15.glBufferData(GL15.GL_ARRAY_BUFFER, meshData.getVertexData(), GL15.GL_STATIC_DRAW);
					GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
					GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, meshData.getIndexData(), GL15.GL_STATIC_DRAW);
					indexCount = meshData.getIndexData().capacity();
					waterPuddle = meshData.getWaterPuddle(x, y, z);
					if(waterPuddle!=null)
						SinglePlayerGame.INSTANCE.getWaterWorks().addPuddle(waterPuddle);
					break;
				case ChunkDataPacket.EntityDataPacket:
					EntityDataPacket entityData = (EntityDataPacket)packet;
					entityData.prepareEntityIterator();
					EntityDatabase d = SinglePlayerGame.INSTANCE.getEntityDatabase();
					Entity e;
					while(entityData.hasNext()){
						e = entityData.next();
						d.addEntity(e);
						plantLife.add(e);
					}
					break;
				case ChunkDataPacket.GrassDataPacket:
					GrassDataPacket grassData = (GrassDataPacket)packet;
					GrassPatch patch = grassData.decode(x, z);
					grassPatches.add(patch);
					Grasslands grassLands = SinglePlayerGame.INSTANCE.getGrasslands();
					grassLands.addPatch(patch);
					break;
				case ChunkDataPacket.BiomeColorDataPacket:
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
					GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, 64, 64, 0, GL11.GL_RGB,
						GL11.GL_UNSIGNED_BYTE, ((BiomeColorDataPacket)packet).getPixelData());
					break;
				default:
					throw new RuntimeException();
			}
		}
	}
	void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		GL11.glDeleteTextures(textureId);
		if(plantLife.size()>0){
			EntityDatabase e = SinglePlayerGame.INSTANCE.getEntityDatabase();
			for(Entity batch : plantLife){
				batch.dispose();
				e.removeEntity(batch);
			}
		}
		if(grassPatches.size()>0){
			Grasslands e = SinglePlayerGame.INSTANCE.getGrasslands();
			for(GrassPatch patch : grassPatches)
				e.removePatch(patch);
		}
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
	void reload(MeshRenderer newMesh){
		{
			// ---
			// Dispose old mesh.
			// ---
			if(waterPuddle!=null){
				WaterWorks waterWorks = SinglePlayerGame.INSTANCE.getWaterWorks();
				waterWorks.removePuddle(waterPuddle);
			}
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, newMesh.getVertexData(), GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, newMesh.getIndexData(), GL15.GL_STATIC_DRAW);
		indexCount = newMesh.getIndexData().capacity();
		if(newMesh.getWaterVertexData()!=null){
			waterPuddle = new WaterPuddle(newMesh.getVertexData(), newMesh.getIndexData(), x, y, z);
			SinglePlayerGame.INSTANCE.getWaterWorks().addPuddle(waterPuddle);
		}
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
