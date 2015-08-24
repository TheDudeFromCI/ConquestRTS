package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkXQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkYQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkZQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ExtremeQuadOptimizer;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Quad;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadListener;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadOptimizer;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.StaticEntity;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassPatch;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassTransform;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
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
	LandscapeChunk(
		WorldNoiseMachine machine, EntityDatabase entityDatabase, Grasslands grassLands, int x, int y, int z){
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
			File file =
				new File(WraithavensConquest.currentGameFolder+File.separatorChar+"Landscape", x+","+y+","+z
					+".dat");
			if(file.exists()&&file.length()>0){
				BinaryFile bin = new BinaryFile(file);
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
						grassPatches[i] = new GrassPatch(grassType, locations);
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
			}else{
				GlError.out("New landmass discovered. Generating now.");
				long time = System.currentTimeMillis();
				// ---
				// Prepare the quad building algorithm.
				// ---
				int[][] heights = new int[LandscapeSize+2][LandscapeSize+2];
				ChunkXQuadCounter xCounter = new ChunkXQuadCounter();
				ChunkYQuadCounter yCounter = new ChunkYQuadCounter();
				ChunkZQuadCounter zCounter = new ChunkZQuadCounter();
				ArrayList<Quad> quadList = new ArrayList();
				QuadListener listener = new QuadListener(){
					public void addQuad(Quad q){
						quadList.add(q);
					}
				};
				// ---
				// Calculate the world heights.
				// ---
				int a, b, c, j, q;
				for(a = 0; a<LandscapeSize+2; a++)
					for(b = 0; b<LandscapeSize+2; b++)
						heights[a][b] = machine.getGroundLevel(a-1+x, b-1+z)-1;
				// ---
				// Build the rest of the data, based on that information.
				// ---
				int[][] quads = new int[LandscapeSize][LandscapeSize];
				int[][] storage = new int[LandscapeSize][LandscapeSize];
				int[][] tempStorage = new int[LandscapeSize][LandscapeSize];
				// ---
				// Combine the quads into their final form.
				// ---
				boolean hasBack;
				boolean placeQuad;
				for(j = 0; j<6; j++){
					if(j==3)
						continue;
					if(j==0||j==1){
						for(a = 0; a<LandscapeSize; a++){
							for(b = 0; b<LandscapeSize; b++)
								for(c = 0; c<LandscapeSize; c++){
									hasBack = heights[a+1][c+1]>=b+y;
									placeQuad = heights[a+1+(j==0?1:-1)][c+1]<b+y;
									if(hasBack&&placeQuad)
										quads[b][c] = 1;
									else if(placeQuad)
										quads[b][c] = -1;
									else
										quads[b][c] = 0;
								}
							q =
								ExtremeQuadOptimizer.optimize(storage, tempStorage, quads, LandscapeSize,
									LandscapeSize);
							if(q==0)
								continue;
							xCounter.setup(x, y, z, a, j, listener, Block.GRASS);
							QuadOptimizer.countQuads(xCounter, storage, LandscapeSize, LandscapeSize, q);
						}
					}else if(j==2){
						for(b = 0; b<LandscapeSize; b++){
							for(a = 0; a<LandscapeSize; a++)
								for(c = 0; c<LandscapeSize; c++){
									if(heights[a+1][c+1]==b+y)
										quads[a][c] = 1;
									else if(heights[a+1][c+1]<b+y)
										quads[a][c] = -1;
									else
										quads[a][c] = 0;
								}
							q =
								ExtremeQuadOptimizer.optimize(storage, tempStorage, quads, LandscapeSize,
									LandscapeSize);
							if(q==0)
								continue;
							yCounter.setup(x, y, z, b, j, listener, Block.GRASS);
							QuadOptimizer.countQuads(yCounter, storage, LandscapeSize, LandscapeSize, q);
						}
					}else{
						for(c = 0; c<LandscapeSize; c++){
							for(a = 0; a<LandscapeSize; a++)
								for(b = 0; b<LandscapeSize; b++){
									hasBack = heights[a+1][c+1]>=b+y;
									placeQuad = heights[a+1][c+1+(j==4?1:-1)]<b+y;
									if(hasBack&&placeQuad)
										quads[a][b] = 1;
									else if(placeQuad)
										quads[a][b] = -1;
									else
										quads[a][b] = 0;
								}
							q =
								ExtremeQuadOptimizer.optimize(storage, tempStorage, quads, LandscapeSize,
									LandscapeSize);
							if(q==0)
								continue;
							zCounter.setup(x, y, z, c, j, listener, Block.GRASS);
							QuadOptimizer.countQuads(zCounter, storage, LandscapeSize, LandscapeSize, q);
						}
					}
				}
				// ---
				// Build the vertices and indices.
				// ---
				VertexStorage vertices = new VertexStorage();
				IndexStorage indices = new IndexStorage();
				int v0, v1, v2, v3;
				byte shade;
				for(Quad quad : quadList){
					shade = (byte)(quad.side==2?255:quad.side==3?130:quad.side==0||quad.side==1?200:180);
					v0 = vertices.indexOf(quad.data.get(0), quad.data.get(1), quad.data.get(2), shade);
					v1 = vertices.indexOf(quad.data.get(3), quad.data.get(4), quad.data.get(5), shade);
					v2 = vertices.indexOf(quad.data.get(6), quad.data.get(7), quad.data.get(8), shade);
					v3 = vertices.indexOf(quad.data.get(9), quad.data.get(10), quad.data.get(11), shade);
					indices.place(v0);
					indices.place(v1);
					indices.place(v2);
					indices.place(v0);
					indices.place(v2);
					indices.place(v3);
				}
				// ---
				// Compile and save.
				// ---
				ByteBuffer vertexData = BufferUtils.createByteBuffer(vertices.size()*13);
				IntBuffer indexData = BufferUtils.createIntBuffer(indices.size());
				BinaryFile bin = new BinaryFile(vertices.size()*13+indices.size()*4+8);
				bin.addInt(vertices.size());
				bin.addInt(indices.size());
				Vertex v;
				for(int i = 0; i<vertices.size(); i++){
					v = vertices.get(i);
					vertexData.putFloat(v.getX());
					vertexData.putFloat(v.getY());
					vertexData.putFloat(v.getZ());
					vertexData.put(v.getShade());
					bin.addFloat(v.getX());
					bin.addFloat(v.getY());
					bin.addFloat(v.getZ());
					bin.addByte(v.getShade());
				}
				for(int i = 0; i<indices.size(); i++){
					indexData.put(indices.get(i));
					bin.addInt(indices.get(i));
				}
				vertexData.flip();
				indexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
				GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
				// ---
				// Load plantlife.
				// ---
				indexCount = indices.size();
				GlError.out("Generated landmass.\n  Verts: "+vertices.size()+"\n  Tris: "+indices.size()/3+" ("
					+indices.size()+" Indices)\n  Finished in "+(System.currentTimeMillis()-time)+" ms.");
				HashMap<EntityType,ArrayList<GrassTransform>> grassLocations = new HashMap();
				HashMap<EntityType,ArrayList<Vector3f>> plantLocations = new HashMap();
				int plantCount = 0;
				EntityType entity;
				for(a = 0; a<LandscapeSize; a++)
					for(b = 0; b<LandscapeSize; b++){
						entity = machine.randomPlant(a+x, b+z);
						if(entity!=null){
							if(entity.isGrass){
								GrassTransform loc =
									new GrassTransform(a+x+0.5f, machine.getGroundLevel(a+x, b+z), b+z+0.5f,
										(float)(Math.random()*Math.PI*2), 2.0f+(float)(Math.random()*0.3f-0.15f));
								if(grassLocations.containsKey(entity))
									grassLocations.get(entity).add(loc);
								else{
									ArrayList<GrassTransform> locs = new ArrayList();
									locs.add(loc);
									grassLocations.put(entity, locs);
								}
							}else{
								Vector3f loc =
									new Vector3f(a+x+0.5f, machine.getGroundLevel(a+x, b+z), b+z+0.5f);
								if(plantLocations.containsKey(entity))
									plantLocations.get(entity).add(loc);
								else{
									ArrayList<Vector3f> locs = new ArrayList();
									locs.add(loc);
									plantLocations.put(entity, locs);
								}
								plantCount++;
							}
						}
					}
				int bytes = 8;
				for(EntityType type : plantLocations.keySet()){
					bytes += 8;
					bytes += plantLocations.get(type).size()*5*4;
				}
				for(EntityType type : grassLocations.keySet()){
					bytes += 8;
					bytes += grassLocations.get(type).size()*5*4;
				}
				bin.allocateMoreSpace(bytes);
				bin.addInt(plantLocations.size());
				for(EntityType type : plantLocations.keySet()){
					bin.addInt(type.ordinal());
					ArrayList<Vector3f> locs = plantLocations.get(type);
					bin.addInt(locs.size());
					for(int i = 0; i<locs.size(); i++){
						Vector3f loc = locs.get(i);
						bin.addFloat(loc.x);
						bin.addFloat(loc.y);
						bin.addFloat(loc.z);
						float scale = (float)(Math.random()*0.1f-0.05f+1/5f);
						float yaw = (float)(Math.random()*360);
						bin.addFloat(scale);
						bin.addFloat(yaw);
						if(entityDatabase!=null){
							StaticEntity e = new StaticEntity(type);
							e.moveTo(loc.x, loc.y, loc.z);
							e.scaleTo(scale);
							e.setYaw(yaw);
							plantLife.add(e);
							entityDatabase.addEntity(e);
						}
					}
				}
				bin.addInt(grassLocations.size());
				if(grassLands==null)
					grassPatches = null;
				else
					grassPatches = new GrassPatch[grassLocations.size()];
				int i = 0;
				int grassBladeCount = 0;
				for(EntityType type : grassLocations.keySet()){
					bin.addInt(type.ordinal());
					ArrayList<GrassTransform> locs = grassLocations.get(type);
					bin.addInt(locs.size());
					for(GrassTransform loc : locs){
						bin.addFloat(loc.getX());
						bin.addFloat(loc.getY());
						bin.addFloat(loc.getZ());
						bin.addFloat(loc.getRotation());
						bin.addFloat(loc.getScale());
					}
					if(grassLands!=null){
						grassPatches[i] = new GrassPatch(type, locs);
						grassLands.addPatch(grassPatches[i]);
					}
					grassBladeCount += locs.size();
					i++;
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
					bin.allocateMoreSpace(byteCount);
					ByteBuffer pixels = BufferUtils.createByteBuffer(byteCount);
					{
						// ---
						// Generate biome colors.
						// ---
						int blockX, blockY, blockZ;
						byte red, green, blue;
						Vector3f colors = new Vector3f();
						for(blockZ = 0; blockZ<64; blockZ++)
							for(blockY = 0; blockY<64; blockY++)
								for(blockX = 0; blockX<64; blockX++){
									machine.getBiomeColorAt(blockX+x, blockY+y, blockZ+z, colors);
									red = (byte)Math.round(colors.x*255);
									green = (byte)Math.round(colors.y*255);
									blue = (byte)Math.round(colors.z*255);
									pixels.put(red);
									pixels.put(green);
									pixels.put(blue);
									bin.addByte(red);
									bin.addByte(green);
									bin.addByte(blue);
								}
					}
					pixels.flip();
					GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGB8, 64, 64, 64, 0, GL11.GL_RGB,
						GL11.GL_UNSIGNED_BYTE, pixels);
				}
				bin.compile(file);
				GlError.out("Generated plantlife.\n  Types: "+plantLocations.size()+"\n  Total Entitys: "
					+plantCount+"\n  Grass Types: "+grassLocations.size()+"\n   Amount: "+grassBladeCount);
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
