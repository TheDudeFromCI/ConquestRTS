package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.BlockProperties;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkXQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkYQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkZQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Quad;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadListener;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadOptimizer;

public class ChunkPainter{
	private static ChunkVBO build(RawChunk raw){
		ArrayList<Quad> quads = new ArrayList();
		QuadListener quadListener = new QuadListener(){
			public void addQuad(Quad q){
				quads.add(q);
			}
		};
		int x, y, z, j, i, q;
		byte block;
		for(j = 0; j<6; j++){
			if(j==0||j==1){
				for(x = 0; x<16; x++){
					PROPERTIES.reset();
					for(y = 0; y<16; y++)
						for(z = 0; z<16; z++){
							block = raw.getBlock(x, y, z);
							if(block==Block.AIR)
								continue;
							if(!PROPERTIES.contains(block))
								PROPERTIES.place(block);
						}
					for(i = 0; i<PROPERTIES.size(); i++){
						for(y = 0; y<16; y++)
							for(z = 0; z<16; z++)
								QUAD_LAYER[y][z] =
								raw.getBlock(x, y, z)==PROPERTIES.get(i)&&isOpen(raw, x, y, z, j);
						q = QuadOptimizer.optimize(QUAD_STORAGE, QUAD_STORAGE_2, QUAD_LAYER, 16, 16, true);
						if(q==0)
							continue;
						X_QUAD_COUNTER.setup(raw.getX(), raw.getY(), raw.getZ(), x, j, quadListener,
							Block.values()[PROPERTIES.get(i)+Block.ID_SHIFT]);
						QuadOptimizer.countQuads(X_QUAD_COUNTER, QUAD_STORAGE, 16, 16, q);
					}
				}
			}else if(j==2||j==3){
				for(y = 0; y<16; y++){
					PROPERTIES.reset();
					for(x = 0; x<16; x++)
						for(z = 0; z<16; z++){
							block = raw.getBlock(x, y, z);
							if(block==Block.AIR)
								continue;
							if(!PROPERTIES.contains(block))
								PROPERTIES.place(block);
						}
					for(i = 0; i<PROPERTIES.size(); i++){
						for(x = 0; x<16; x++)
							for(z = 0; z<16; z++)
								QUAD_LAYER[x][z] =
								raw.getBlock(x, y, z)==PROPERTIES.get(i)&&isOpen(raw, x, y, z, j);
						q = QuadOptimizer.optimize(QUAD_STORAGE, QUAD_STORAGE_2, QUAD_LAYER, 16, 16, true);
						if(q==0)
							continue;
						Y_QUAD_COUNTER.setup(raw.getX(), raw.getY(), raw.getZ(), y, j, quadListener,
							Block.values()[PROPERTIES.get(i)+Block.ID_SHIFT]);
						QuadOptimizer.countQuads(Y_QUAD_COUNTER, QUAD_STORAGE, 16, 16, q);
					}
				}
			}else{
				for(z = 0; z<16; z++){
					PROPERTIES.reset();
					for(x = 0; x<16; x++)
						for(y = 0; y<16; y++){
							block = raw.getBlock(x, y, z);
							if(block==Block.AIR)
								continue;
							if(!PROPERTIES.contains(block))
								PROPERTIES.place(block);
						}
					for(i = 0; i<PROPERTIES.size(); i++){
						for(x = 0; x<16; x++)
							for(y = 0; y<16; y++)
								QUAD_LAYER[x][y] =
								raw.getBlock(x, y, z)==PROPERTIES.get(i)&&isOpen(raw, x, y, z, j);
						q = QuadOptimizer.optimize(QUAD_STORAGE, QUAD_STORAGE_2, QUAD_LAYER, 16, 16, true);
						if(q==0)
							continue;
						Z_QUAD_COUNTER.setup(raw.getX(), raw.getY(), raw.getZ(), z, j, quadListener,
							Block.values()[PROPERTIES.get(i)+Block.ID_SHIFT]);
						QuadOptimizer.countQuads(Z_QUAD_COUNTER, QUAD_STORAGE, 16, 16, q);
					}
				}
			}
		}
		int vbo = GL15.glGenBuffers();
		int ibo = GL15.glGenBuffers();
		int elementCount = compileBuffer(quads, vbo, ibo);
		return new ChunkVBO(vbo, ibo, elementCount);
	}
	private static int compileBuffer(ArrayList<Quad> quads, int vboId, int iboId){
		int points = quads.size()*4;
		int indices = quads.size()*6;
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(points*8);
		ShortBuffer indexData = BufferUtils.createShortBuffer(indices);
		short elementCount = 0;
		int indexCount = 0;
		Quad q;
		float shade;
		float grass;
		for(int i = 0; i<quads.size(); i++){
			q = quads.get(i);
			shade = q.side==2?1.0f:q.side==3?0.6f:q.side==0||q.side==1?0.8f:0.7f;
			grass = q.blockType==Block.GRASS.id()?1.0f:0.0f;
			vertexData.put(q.data.get(0)).put(q.data.get(1)).put(q.data.get(2));
			vertexData.put(q.data.get(12)).put(q.data.get(13)).put(q.data.get(14));
			vertexData.put(shade);
			vertexData.put(grass);
			vertexData.put(q.data.get(3)).put(q.data.get(4)).put(q.data.get(5));
			vertexData.put(q.data.get(12)).put(q.data.get(13)).put(q.data.get(14));
			vertexData.put(shade);
			vertexData.put(grass);
			vertexData.put(q.data.get(6)).put(q.data.get(7)).put(q.data.get(8));
			vertexData.put(q.data.get(12)).put(q.data.get(13)).put(q.data.get(14));
			vertexData.put(shade);
			vertexData.put(grass);
			vertexData.put(q.data.get(9)).put(q.data.get(10)).put(q.data.get(11));
			vertexData.put(q.data.get(12)).put(q.data.get(13)).put(q.data.get(14));
			vertexData.put(shade);
			vertexData.put(grass);
			indexData.put(elementCount).put((short)(elementCount+1)).put((short)(elementCount+2));
			indexData.put(elementCount).put((short)(elementCount+2)).put((short)(elementCount+3));
			elementCount += 4;
			indexCount += 6;
		}
		vertexData.flip();
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
		quads = null;
		return indexCount;
	}
	private static boolean isAir(RawChunk raw, int x, int y, int z){
		if(x<0)
			return true;
		if(y<0)
			return true;
		if(z<0)
			return true;
		if(x>15)
			return true;
		if(y>15)
			return true;
		if(z>15)
			return true;
		return raw.getBlock(x, y, z)==Block.AIR;
	}
	private static boolean isOpen(RawChunk raw, int x, int y, int z, int j){
		int tempX = x;
		int tempY = y;
		int tempZ = z;
		if(j==0)
			tempX++;
		if(j==1)
			tempX--;
		if(j==2)
			tempY++;
		if(j==3)
			tempY--;
		if(j==4)
			tempZ++;
		if(j==5)
			tempZ--;
		return isAir(raw, tempX, tempY, tempZ);
	}
	private static final boolean[][] QUAD_LAYER = new boolean[16][16];
	private static final int[][] QUAD_STORAGE = new int[16][16];
	private static final int[][] QUAD_STORAGE_2 = new int[16][16];
	private static final ChunkXQuadCounter X_QUAD_COUNTER = new ChunkXQuadCounter();
	private static final ChunkYQuadCounter Y_QUAD_COUNTER = new ChunkYQuadCounter();
	private static final ChunkZQuadCounter Z_QUAD_COUNTER = new ChunkZQuadCounter();
	private static final BlockProperties PROPERTIES = new BlockProperties(Math.min(256, Block.values().length));
	private final ChunkVBO vbo;
	public final int x;
	public final int y;
	public final int z;
	public ChunkPainter(RawChunk raw){
		x = raw.getX();
		y = raw.getY();
		z = raw.getZ();
		vbo = raw.isEmpty()?null:build(raw);
	}
	public void dispose(){
		if(vbo!=null)
			vbo.dispose();
	}
	public void render(){
		if(vbo!=null)
			vbo.render();
	}
}
