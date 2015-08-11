package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkXQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkYQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkZQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ExtremeQuadOptimizer;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Quad;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadListener;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadOptimizer;
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
	LandscapeChunk(WorldNoiseMachine machine, int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
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
				int i;
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
						heights[a][b] = (int)machine.getWorldHeight(a-1+x, b-1+z);
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
				bin.compile(file);
				vertexData.flip();
				indexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
				GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
				indexCount = indices.size();
				GlError.out("Generated landmass.\n  Verts: "+vertices.size()+"\n  Tris: "+indices.size()/3+" ("
					+indices.size()+" Indices)\n  Finished in "+(System.currentTimeMillis()-time)+" ms.");
			}
		}
		GlError.dumpError();
	}
	void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		GlError.dumpError();
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
