package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.BlockTextures;

public class MeshFormatter{
	private static boolean[] quadReferences = new boolean[10];
	private static final ArrayList<Integer> used = new ArrayList();
	private final BlockTypeList typeList;
	private final byte[][] quads;
	private final int[][] storage;
	private final int[][] temp;
	private final VertexStorage vertexStorage;
	private final IndexStorage indexStorage;
	private final VertexStorage waterVertexStorage;
	private final IndexStorage waterIndexStorage;
	private boolean basicState;
	private int currentTextureId;
	public MeshFormatter(){
		typeList = new BlockTypeList();
		quads = new byte[64][64];
		storage = new int[64][64];
		temp = new int[64][64];
		vertexStorage = new VertexStorage();
		indexStorage = new IndexStorage();
		waterVertexStorage = new VertexStorage();
		waterIndexStorage = new IndexStorage();
	}
	private void addQuad(int x, int y, int w, int h, int j, int o){
		float shade = j==2?1:j==3?130/255f:j==0||j==1?200/255f:180/255f;
		VertexStorage vertices;
		IndexStorage indices;
		boolean water;
		final float waterOffset = -0.1f;
		if(BlockTextures.values()[currentTextureId].isWater()){
			vertices = waterVertexStorage;
			indices = waterIndexStorage;
			shade = 1;
			water = true;
		}else{
			vertices = vertexStorage;
			indices = indexStorage;
			water = false;
		}
		if(j==0){
			float sx = o;
			float sy = x+(water?waterOffset:0);
			float sz = y;
			float bx = sx+1;
			float by = sy+w;
			float bz = sz+h;
			short v1 = (short)vertices.place(bx, by, bz, shade, 0, 0, currentTextureId);
			short v2 = (short)vertices.place(bx, sy, bz, shade, 0, w, currentTextureId);
			short v3 = (short)vertices.place(bx, sy, sz, shade, h, w, currentTextureId);
			short v4 = (short)vertices.place(bx, by, sz, shade, h, 0, currentTextureId);
			indices.place(v1);
			indices.place(v2);
			indices.place(v3);
			indices.place(v1);
			indices.place(v3);
			indices.place(v4);
		}else if(j==1){
			float sx = o;
			float sy = x+(water?waterOffset:0);
			float sz = y;
			float by = sy+w;
			float bz = sz+h;
			short v1 = (short)vertices.place(sx, sy, sz, shade, h, w, currentTextureId);
			short v2 = (short)vertices.place(sx, sy, bz, shade, 0, w, currentTextureId);
			short v3 = (short)vertices.place(sx, by, bz, shade, 0, 0, currentTextureId);
			short v4 = (short)vertices.place(sx, by, sz, shade, h, 0, currentTextureId);
			indices.place(v1);
			indices.place(v2);
			indices.place(v3);
			indices.place(v1);
			indices.place(v3);
			indices.place(v4);
		}else if(j==2){
			float sx = x;
			float sy = o+(water?waterOffset:0);
			float sz = y;
			float bx = sx+w;
			float by = sy+1;
			float bz = sz+h;
			short v1 = (short)vertices.place(sx, by, sz, shade, 0, 0, currentTextureId);
			short v2 = (short)vertices.place(sx, by, bz, shade, 0, h, currentTextureId);
			short v3 = (short)vertices.place(bx, by, bz, shade, w, h, currentTextureId);
			short v4 = (short)vertices.place(bx, by, sz, shade, w, 0, currentTextureId);
			indices.place(v1);
			indices.place(v2);
			indices.place(v3);
			indices.place(v1);
			indices.place(v3);
			indices.place(v4);
		}else if(j==3){
			float sx = x;
			float sy = o+(water?waterOffset:0);
			float sz = y;
			float bx = sx+w;
			float bz = sz+h;
			short v1 = (short)vertices.place(bx, sy, bz, shade, w, h, currentTextureId);
			short v2 = (short)vertices.place(sx, sy, bz, shade, 0, h, currentTextureId);
			short v3 = (short)vertices.place(sx, sy, sz, shade, 0, 0, currentTextureId);
			short v4 = (short)vertices.place(bx, sy, sz, shade, w, 0, currentTextureId);
			indices.place(v1);
			indices.place(v2);
			indices.place(v3);
			indices.place(v1);
			indices.place(v3);
			indices.place(v4);
		}else if(j==4){
			float sx = x;
			float sy = y+(water?waterOffset:0);
			float sz = o;
			float bx = sx+w;
			float by = sy+h;
			float bz = sz+1;
			short v1 = (short)vertices.place(bx, by, bz, shade, 0, 0, currentTextureId);
			short v2 = (short)vertices.place(sx, by, bz, shade, 0, w, currentTextureId);
			short v3 = (short)vertices.place(sx, sy, bz, shade, h, w, currentTextureId);
			short v4 = (short)vertices.place(bx, sy, bz, shade, h, 0, currentTextureId);
			indices.place(v1);
			indices.place(v2);
			indices.place(v3);
			indices.place(v1);
			indices.place(v3);
			indices.place(v4);
		}else if(j==5){
			float sx = x;
			float sy = y+(water?waterOffset:0);
			float sz = o;
			float bx = sx+w;
			float by = sy+h;
			short v1 = (short)vertices.place(sx, sy, sz, shade, 0, 0, currentTextureId);
			short v2 = (short)vertices.place(sx, by, sz, shade, 0, h, currentTextureId);
			short v3 = (short)vertices.place(bx, by, sz, shade, w, h, currentTextureId);
			short v4 = (short)vertices.place(bx, sy, sz, shade, w, 0, currentTextureId);
			indices.place(v1);
			indices.place(v2);
			indices.place(v3);
			indices.place(v1);
			indices.place(v3);
			indices.place(v4);
		}else
			throw new RuntimeException();
	}
	private void countQuads(int quadCount, int j, int offset){
		int x, y, w, h, o;
		if(quadCount>quadReferences.length)
			quadReferences = new boolean[quadCount];
		else
			for(o = 0; o<quadReferences.length; o++)
				quadReferences[o] = false;
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++){
				if(storage[x][y]==-1)
					continue;
				if(quadReferences[storage[x][y]])
					continue;
				o = storage[x][y];
				quadReferences[o] = true;
				for(w = x; w<64; w++)
					if(storage[w][y]!=o)
						break;
				for(h = y; h<64; h++)
					if(storage[x][h]!=o)
						break;
				w -= x;
				h -= y;
				addQuad(x, y, w, h, j, offset);
			}
	}
	private int optimize(){
		if(basicState)
			return optimizeSimpleXStrong();
		int x, y;
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				temp[x][y] = storage[x][y];
		int xS = optimizeXStrong(storage);
		removeFalse(storage, xS);
		used.clear();
		int yS = optimizeYStrong(temp);
		removeFalse(temp, yS);
		used.clear();
		if(xS<=yS)
			return xS;
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				storage[x][y] = temp[x][y];
		return yS;
	}
	private int optimizeSimpleXStrong(){
		int x, y, t, q;
		int w = -1;
		int total = 0;
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				storage[x][y] = -1;
		for(y = 0; y<64; y++){
			w = -1;
			for(x = 0; x<64; x++){
				if(quads[x][y]!=1){
					w = -1;
					continue;
				}
				if(w!=1){
					w = total;
					total++;
				}
				storage[x][y] = w;
			}
		}
		for(y = 0; y<64-1; y++){
			w = 0;
			for(x = 0; x<64; x++){
				if(quads[x][y]!=1){
					if(w>0){
						q = 0;
						if(quads[x][y+1]!=1&&(x==w||quads[x-w-1][y+1]!=1)){
							for(t = x-w; t<x; t++){
								if(quads[t][y+1]!=1)
									break;
								q++;
							}
							if(q==w)
								for(t = x-w; t<x; t++)
									storage[t][y+1] = storage[t][y];
						}
						w = 0;
					}
					continue;
				}
				w++;
			}
			if(w>0){
				q = 0;
				if(x==w||quads[x-w-1][y+1]!=1){
					for(t = x-w; t<x; t++){
						if(quads[t][y+1]!=1)
							break;
						q++;
					}
					if(q==w)
						for(t = x-w; t<x; t++)
							storage[t][y+1] = storage[t][y];
				}
			}
		}
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				if(storage[x][y]>-1&&!used.contains(storage[x][y]))
					used.add(storage[x][y]);
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				if(storage[x][y]>-1)
					storage[x][y] = used.indexOf(storage[x][y]);
		return used.size();
	}
	private int optimizeXStrong(int[][] storage){
		int x, y, t, q;
		int w = -1;
		int total = 0;
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				storage[x][y] = -1;
		for(y = 0; y<64; y++){
			w = -1;
			for(x = 0; x<64; x++){
				if(quads[x][y]==-1){
					w = -1;
					continue;
				}
				if(w==-1){
					w = total;
					total++;
				}
				storage[x][y] = w;
			}
		}
		for(y = 0; y<64-1; y++){
			w = 0;
			for(x = 0; x<64; x++){
				if(quads[x][y]==-1){
					if(w>0){
						q = 0;
						if(quads[x][y+1]==-1&&(x==w||quads[x-w-1][y+1]==-1)){
							for(t = x-w; t<x; t++){
								if(quads[t][y+1]==-1)
									break;
								q++;
							}
							if(q==w)
								for(t = x-w; t<x; t++)
									storage[t][y+1] = storage[t][y];
						}
						w = 0;
					}
					continue;
				}
				w++;
			}
			if(w>0){
				q = 0;
				if(x==w||quads[x-w-1][y+1]==-1){
					for(t = x-w; t<x; t++){
						if(quads[t][y+1]==-1)
							break;
						q++;
					}
					if(q==w)
						for(t = x-w; t<x; t++)
							storage[t][y+1] = storage[t][y];
				}
			}
		}
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				if(storage[x][y]>-1&&!used.contains(storage[x][y]))
					used.add(storage[x][y]);
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				if(storage[x][y]>-1)
					storage[x][y] = used.indexOf(storage[x][y]);
		return used.size();
	}
	private int optimizeYStrong(int[][] storage){
		int x, y, t, q;
		int w = -1;
		int total = 0;
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				storage[x][y] = -1;
		for(x = 0; x<64; x++){
			w = -1;
			for(y = 0; y<64; y++){
				if(quads[x][y]==-1){
					w = -1;
					continue;
				}
				if(w==-1){
					w = total;
					total++;
				}
				storage[x][y] = w;
			}
		}
		for(x = 0; x<64-1; x++){
			w = 0;
			for(y = 0; y<64; y++){
				if(quads[x][y]==-1){
					if(w>0){
						q = 0;
						if(quads[x+1][y]==-1&&(y==w||quads[x+1][y-w-1]==-1)){
							for(t = y-w; t<y; t++){
								if(quads[x+1][t]==-1)
									break;
								q++;
							}
							if(q==w)
								for(t = y-w; t<y; t++)
									storage[x+1][t] = storage[x][t];
						}
						w = 0;
					}
					continue;
				}
				w++;
			}
			if(w>0){
				q = 0;
				if(y==w||quads[x+1][y-w-1]==-1){
					for(t = y-w; t<y; t++){
						if(quads[x+1][t]==-1)
							break;
						q++;
					}
					if(q==w){
						for(t = y-w; t<y; t++)
							storage[x+1][t] = storage[x][t];
					}
				}
			}
		}
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				if(storage[x][y]>-1&&!used.contains(storage[x][y]))
					used.add(storage[x][y]);
		for(x = 0; x<64; x++)
			for(y = 0; y<64; y++)
				if(storage[x][y]>-1)
					storage[x][y] = used.indexOf(storage[x][y]);
		return used.size();
	}
	private void removeFalse(int[][] storage, int quadCount){
		int x, y, w, h, o, a, b;
		if(quadCount>quadReferences.length)
			quadReferences = new boolean[quadCount];
		else
			for(o = 0; o<quadReferences.length; o++)
				quadReferences[o] = false;
		for(x = 0; x<64; x++){
			quadLooker:for(y = 0; y<64; y++){
				if(storage[x][y]==-1)
					continue;
				if(quadReferences[storage[x][y]])
					continue;
				o = storage[x][y];
				quadReferences[o] = true;
				for(w = x; w<64; w++)
					if(storage[w][y]!=o)
						break;
				for(h = y; h<64; h++)
					if(storage[x][h]!=o)
						break;
				for(a = x; a<w; a++)
					for(b = y; b<h; b++)
						if(quads[a][b]==1)
							continue quadLooker;
				for(a = x; a<w; a++)
					for(b = y; b<h; b++)
						storage[a][b] = -1;
			}
		}
		shrinkQuads(storage, quadCount);
	}
	private void shrinkQuads(int[][] storage, int quadCount){
		int x, y, w, h, o, a, j;
		int nx, ny, nw, nh;
		if(quadCount>quadReferences.length)
			quadReferences = new boolean[quadCount];
		else
			for(o = 0; o<quadReferences.length; o++)
				quadReferences[o] = false;
		for(x = 0; x<64; x++){
			for(y = 0; y<64; y++){
				if(storage[x][y]==-1)
					continue;
				if(quadReferences[storage[x][y]])
					continue;
				o = storage[x][y];
				quadReferences[o] = true;
				for(w = x; w<64; w++)
					if(storage[w][y]!=o)
						break;
				for(h = y; h<64; h++)
					if(storage[x][h]!=o)
						break;
				nx = x;
				ny = y;
				nw = w;
				nh = h;
				for(j = 0; j<4; j++)
					looper:while(true){
						if(j==0){
							for(a = nx; a<nw; a++)
								if(quads[a][ny]==1)
									break looper;
							for(a = nx; a<nw; a++)
								storage[a][ny] = -1;
							ny++;
						}else if(j==1){
							for(a = ny; a<nh; a++)
								if(quads[nw-1][a]==1)
									break looper;
							for(a = ny; a<nh; a++)
								storage[nw-1][a] = -1;
							nw--;
						}else if(j==2){
							for(a = nx; a<nw; a++)
								if(quads[a][nh-1]==1)
									break looper;
							for(a = nx; a<nw; a++)
								storage[a][nh-1] = -1;
							nh--;
						}else{
							for(a = ny; a<nh; a++)
								if(quads[nx][a]==1)
									break looper;
							for(a = ny; a<nh; a++)
								storage[nx][a] = -1;
							nx++;
						}
					}
			}
		}
	}
	void addBlockType(byte block){
		typeList.add(block);
	}
	void clearTypeList(){
		typeList.clear();
	}
	void compileLayer(int side, int o){
		int count = optimize();
		if(count==0)
			return;
		countQuads(count, side, o);
	}
	MeshRenderer extract(){
		FloatBuffer verts = BufferUtils.createFloatBuffer(vertexStorage.size());
		ShortBuffer inds = BufferUtils.createShortBuffer(indexStorage.size());
		verts.put(vertexStorage.getAll(), 0, vertexStorage.size());
		inds.put(indexStorage.getAll(), 0, indexStorage.size());
		verts.flip();
		inds.flip();
		vertexStorage.clear();
		indexStorage.clear();
		if(waterIndexStorage.size()==0)
			return new MeshRenderer(verts, inds, null, null);
		int vertexCount = waterVertexStorage.size()/7;
		FloatBuffer verts2 = BufferUtils.createFloatBuffer(vertexCount*5);
		ShortBuffer inds2 = BufferUtils.createShortBuffer(waterIndexStorage.size());
		for(int i = 0; i<waterVertexStorage.size(); i += 7){
			verts2.put(waterVertexStorage.get(i+0));
			verts2.put(waterVertexStorage.get(i+1));
			verts2.put(waterVertexStorage.get(i+2));
			verts2.put(waterVertexStorage.get(i+4));
			verts2.put(waterVertexStorage.get(i+5));
		}
		inds2.put(waterIndexStorage.getAll(), 0, waterIndexStorage.size());
		verts2.flip();
		inds2.flip();
		waterVertexStorage.clear();
		waterIndexStorage.clear();
		return new MeshRenderer(verts, inds, verts2, inds2);
	}
	byte getBlockType(int index){
		return typeList.get(index);
	}
	void setBasicState(boolean basicState){
		this.basicState = basicState;
	}
	void setBlock(int a, int b, byte state){
		quads[a][b] = state;
	}
	void setCurrentTexutre(int id){
		currentTextureId = id;
	}
	int typeListSize(){
		return typeList.size();
	}
}
