package com.wraithavens.conquest.SinglePlayer.Blocks.World;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Entities.AesiaStem;
import com.wraithavens.conquest.SinglePlayer.Entities.Entity;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassPatch;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassTransform;
import com.wraithavens.conquest.SinglePlayer.Entities.Water.WaterPuddle;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class ChunkStack{
	private final int biomeTextureId;
	private final int x;
	private final int z;
	private final float[] cameraBox = new float[6];
	private WaterPuddle[] waterPuddles;
	private Entity[] entities;
	private GrassPatch[] grass;
	private ChunkMesh[] meshes;
	private int cameraDistance;
	public ChunkStack(int x, int z){
		this.x = x;
		this.z = z;
		cameraBox[0] = x+32;
		cameraBox[1] = x;
		cameraBox[2] = Float.MAX_VALUE;
		cameraBox[3] = Float.MIN_VALUE;
		cameraBox[4] = z+32;
		cameraBox[5] = z;
		BinaryFile bin = new BinaryFile(Algorithms.getChunkStackPath(x, z));
		bin.decompress(false);
		waterPuddles = new WaterPuddle[bin.getInt()];
		entities = new Entity[bin.getInt()];
		grass = new GrassPatch[bin.getInt()];
		meshes = new ChunkMesh[bin.getInt()];
		{
			// ---
			// Load biome data
			// ---
			biomeTextureId = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, biomeTextureId);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			ByteBuffer pixels = BufferUtils.createByteBuffer(32*32*3);
			pixels.flip();
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, 32, 32, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
				pixels);
		}
		{
			// ---
			// Load water data.
			// ---
			for(int i = 0; i<waterPuddles.length; i++){
				FloatBuffer vertexData = BufferUtils.createFloatBuffer(bin.getInt());
				while(vertexData.hasRemaining())
					vertexData.put(bin.getFloat());
				vertexData.flip();
				ShortBuffer indexData = BufferUtils.createShortBuffer(bin.getInt());
				while(indexData.hasRemaining())
					indexData.put(bin.getShort());
				indexData.flip();
				waterPuddles[i] = new WaterPuddle(vertexData, indexData, x, bin.getInt(), z);
				SinglePlayerGame.INSTANCE.getWaterWorks().addPuddle(waterPuddles[i]);
			}
		}
		{
			// ---
			// Load entity data.
			// ---
			for(int i = 0; i<entities.length; i++){
				EntityType type = EntityType.values()[bin.getShort()];
				if(type.isType(EntityType.AesiaStems, 24))
					entities[i] = new AesiaStem(type);
				else
					entities[i] = new Entity(type);
				entities[i].moveTo(bin.getFloat(), bin.getFloat(), bin.getFloat());
				entities[i].setYaw(bin.getFloat());
				entities[i].scaleTo(bin.getFloat());
				entities[i].updateAABB();
				SinglePlayerGame.INSTANCE.getEntityDatabase().addEntity(entities[i]);
			}
		}
		{
			// ---
			// Load grass data.
			// ---
			int i, j;
			for(i = 0; i<grass.length; i++){
				EntityType type = EntityType.values()[bin.getShort()];
				ArrayList<GrassTransform> g = new ArrayList();
				int count = bin.getInt();
				for(j = 0; j<count; j++)
					g.add(new GrassTransform(bin.getFloat(), bin.getFloat(), bin.getFloat(), bin.getFloat(),
						bin.getFloat(), bin.getFloat(), bin.getFloat(), bin.getFloat()));
				grass[i] = new GrassPatch(type, g, x, z);
				SinglePlayerGame.INSTANCE.getGrasslands().addPatch(grass[i]);
			}
		}
		{
			// ---
			// Load mesh data.
			// ---
			for(int i = 0; i<meshes.length; i++){
				FloatBuffer vertexData = BufferUtils.createFloatBuffer(bin.getInt());
				while(vertexData.hasRemaining())
					vertexData.put(bin.getFloat());
				vertexData.flip();
				ShortBuffer indexData = BufferUtils.createShortBuffer(bin.getInt());
				while(indexData.hasRemaining())
					indexData.put(bin.getShort());
				indexData.flip();
				meshes[i] = new ChunkMesh(vertexData, indexData, x, bin.getInt(), z);
			}
		}
	}
	public void dispose(){
		GL11.glDeleteTextures(biomeTextureId);
		for(GrassPatch patch : grass)
			SinglePlayerGame.INSTANCE.getGrasslands().removePatch(patch);
		for(Entity entity : entities)
			SinglePlayerGame.INSTANCE.getEntityDatabase().removeEntity(entity);
		for(WaterPuddle water : waterPuddles)
			SinglePlayerGame.INSTANCE.getWaterWorks().removePuddle(water);
		for(ChunkMesh mesh : meshes)
			mesh.dispose();
	}
	public int getX(){
		return x;
	}
	public int getZ(){
		return z;
	}
	public boolean isVisible(){
		double range = WraithavensConquest.Settings.getChunkRenderDistance();
		return cameraDistance<=range*range&&SinglePlayerGame.INSTANCE.getCamera().boxInView(cameraBox);
	}
	public void render(ShaderProgram shader){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, biomeTextureId);
		for(ChunkMesh mesh : meshes)
			mesh.render(shader);
	}
	public boolean shouldUnload(){
		double range = WraithavensConquest.Settings.getChunkCatcheDistance();
		return cameraDistance>range*range;
	}
	public void updateCameraDistance(){
		Camera camera = SinglePlayerGame.INSTANCE.getCamera();
		double x = Algorithms.groupLocation((int)camera.x, 32)/32;
		double z = Algorithms.groupLocation((int)camera.z, 32)/32;
		x -= getX()/32;
		z -= getZ()/32;
		cameraDistance = (int)Math.round(x*x+z*z);
	}
}
