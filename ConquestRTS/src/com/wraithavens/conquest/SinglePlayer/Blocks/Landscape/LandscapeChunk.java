package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Matrix4f;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkXQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkYQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkZQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ExtremeQuadOptimizer;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Quad;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadListener;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadOptimizer;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityBatch;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.LodRadius;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassPatch;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.Utility.BinaryFile;

public class LandscapeChunk{
	static final int LandscapeSize = 64;
	private final int x;
	private final int y;
	private final int z;
	private final int vbo;
	private final int ibo;
	private final int indexCount;
	private final EntityBatch[] plantLife;
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
		HashMap<EntityType,ArrayList<Matrix4f>> plantLocations = new HashMap();
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
				ArrayList<Matrix4f> locs;
				Matrix4f mat;
				for(i = 0; i<plantLifeTypes; i++){
					plantLocations.put(EntityType.values()[bin.getInt()], locs = new ArrayList());
					locationCount = bin.getInt();
					for(a = 0; a<locationCount; a++){
						mat = new Matrix4f();
						mat.m00 = bin.getFloat();
						mat.m01 = bin.getFloat();
						mat.m02 = bin.getFloat();
						mat.m03 = bin.getFloat();
						mat.m10 = bin.getFloat();
						mat.m11 = bin.getFloat();
						mat.m12 = bin.getFloat();
						mat.m13 = bin.getFloat();
						mat.m20 = bin.getFloat();
						mat.m21 = bin.getFloat();
						mat.m22 = bin.getFloat();
						mat.m23 = bin.getFloat();
						mat.m30 = bin.getFloat();
						mat.m31 = bin.getFloat();
						mat.m32 = bin.getFloat();
						mat.m33 = bin.getFloat();
						locs.add(mat);
					}
				}
				int grassPatchCount = bin.getInt();
				if(grassLands==null)
					grassPatches = null;
				else
					grassPatches = new GrassPatch[grassPatchCount];
				for(i = 0; i<grassPatchCount; i++){
					locationCount = bin.getInt();
					ArrayList<Vector3f> locations = new ArrayList();
					EntityType grassType = EntityType.values()[bin.getInt()];
					for(a = 0; a<locationCount; a++)
						locations.add(new Vector3f(bin.getFloat(), bin.getFloat(), bin.getFloat()));
					if(grassLands!=null){
						grassPatches[i] = new GrassPatch(grassType, locations);
						grassLands.addPatch(grassPatches[i]);
					}
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
				indexCount = indices.size();
				GlError.out("Generated landmass.\n  Verts: "+vertices.size()+"\n  Tris: "+indices.size()/3+" ("
					+indices.size()+" Indices)\n  Finished in "+(System.currentTimeMillis()-time)+" ms.");
				HashMap<EntityType,ArrayList<Vector3f>> grassLocations = new HashMap();
				int plantCount = 0;
				EntityType entity;
				for(a = 0; a<LandscapeSize; a++)
					for(b = 0; b<LandscapeSize; b++){
						entity = machine.randomPlant(a+x, b+z);
						if(entity!=null){
							if(entity.isGrass){
								Vector3f loc =
									new Vector3f(a+x+0.5f, machine.getGroundLevel(a+x, b+z), b+z+0.5f);
								if(grassLocations.containsKey(entity))
									grassLocations.get(entity).add(loc);
								else{
									ArrayList<Vector3f> locs = new ArrayList();
									locs.add(loc);
									grassLocations.put(entity, locs);
								}
							}else{
								Matrix4f mat = new Matrix4f();
								mat.translate(a+x+0.5f, machine.getGroundLevel(a+x, b+z), b+z+0.5f);
								mat.scale(1/20f, 1/20f, 1/20f);
								if(plantLocations.containsKey(entity))
									plantLocations.get(entity).add(mat);
								else{
									ArrayList<Matrix4f> locs = new ArrayList();
									locs.add(mat);
									plantLocations.put(entity, locs);
								}
							}
						}
					}
				int bytes = 8;
				for(EntityType type : plantLocations.keySet()){
					bytes += 8;
					bytes += plantLocations.get(type).size()*16*4;
				}
				for(EntityType type : grassLocations.keySet()){
					bytes += 8;
					bytes += grassLocations.get(type).size()*3*4;
				}
				bin.allocateMoreSpace(bytes);
				bin.addInt(plantLocations.size());
				for(EntityType type : plantLocations.keySet()){
					bin.addInt(type.ordinal());
					ArrayList<Matrix4f> locs = plantLocations.get(type);
					bin.addInt(locs.size());
					for(int i = 0; i<locs.size(); i++){
						Matrix4f mat = locs.get(i);
						bin.addFloat(mat.m00);
						bin.addFloat(mat.m01);
						bin.addFloat(mat.m02);
						bin.addFloat(mat.m03);
						bin.addFloat(mat.m10);
						bin.addFloat(mat.m11);
						bin.addFloat(mat.m12);
						bin.addFloat(mat.m13);
						bin.addFloat(mat.m20);
						bin.addFloat(mat.m21);
						bin.addFloat(mat.m22);
						bin.addFloat(mat.m23);
						bin.addFloat(mat.m30);
						bin.addFloat(mat.m31);
						bin.addFloat(mat.m32);
						bin.addFloat(mat.m33);
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
					ArrayList<Vector3f> locs = grassLocations.get(type);
					bin.addInt(locs.size());
					for(Vector3f loc : locs){
						bin.addFloat(loc.x);
						bin.addFloat(loc.y);
						bin.addFloat(loc.z);
					}
					if(grassLands!=null){
						grassPatches[i] = new GrassPatch(type, locs);
						grassLands.addPatch(grassPatches[i]);
					}
					grassBladeCount += locs.size();
					i++;
				}
				bin.compile(file);
				GlError.out("Generated plantlife.\n  Types: "+plantLocations.size()+"\n  Total Entitys: "
					+plantCount+"\n  Grass Types: "+grassLocations.size()+"\n   Amount"+grassBladeCount);
			}
		}
		GlError.dumpError();
		if(entityDatabase==null)
			plantLife = null;
		else
			plantLife = new EntityBatch[plantLocations.size()];
		int i = 0;
		Vector3f center = new Vector3f(x+LandscapeSize/2, y+LandscapeSize/2, z+LandscapeSize/2);
		LodRadius lodRadius = new LodRadius(100, 200, 400, 800, 1600, 3200);
		if(plantLife!=null)
			for(EntityType entity : plantLocations.keySet()){
				plantLife[i] = new EntityBatch(entity, plantLocations.get(entity), center, lodRadius);
				entityDatabase.addEntity(plantLife[i]);
				i++;
			}
	}
	void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		GlError.dumpError();
		if(plantLife!=null)
			for(EntityBatch batch : plantLife){
				batch.dispose();
				entityDatabase.removeEntity(batch);
			}
		if(grassPatches!=null)
			for(GrassPatch patch : grassPatches){
				patch.dispose();
				grassLands.removePatch(patch);
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
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 13, 0);
		GL20.glVertexAttribPointer(LandscapeWorld.ShadeAttribLocation, 1, GL11.GL_UNSIGNED_BYTE, true, 13, 12);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
		GlError.dumpError();
	}
}
