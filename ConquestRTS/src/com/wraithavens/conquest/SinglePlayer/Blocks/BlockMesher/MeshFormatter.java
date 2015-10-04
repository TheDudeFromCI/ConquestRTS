package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;

public class MeshFormatter{
	private static boolean[] quadReferences = new boolean[10];
	private static final ArrayList<Integer> used = new ArrayList();
	private final BlockTypeList typeList;
	private final byte[][] quads;
	private final int[][] storage;
	private final int[][] temp;
	private final VertexStorage vertexStorage;
	private final IndexStorage indexStorage;
	private boolean basicState;
	private int currentTextureId;
	public MeshFormatter(){
		typeList = new BlockTypeList();
		quads = new byte[64][64];
		storage = new int[64][64];
		temp = new int[64][64];
		vertexStorage = new VertexStorage();
		indexStorage = new IndexStorage();
	}
	public void addBlockType(byte block){
		typeList.add(block);
	}
	public void clearTypeList(){
		typeList.clear();
	}
	public void compileLayer(int side, int o){
		int count = optimize();
		if(count==0)
			return;
		countQuads(count, side, o);
	}
	public MeshRenderer extract(){
		FloatBuffer verts = BufferUtils.createFloatBuffer(vertexStorage.size());
		ShortBuffer inds = BufferUtils.createShortBuffer(indexStorage.size());
		verts.put(vertexStorage.getAll(), 0, vertexStorage.size());
		inds.put(indexStorage.getAll(), 0, indexStorage.size());
		verts.flip();
		inds.flip();
		vertexStorage.clear();
		indexStorage.clear();
		return new MeshRenderer(verts, inds);
	}
	public byte getBlockType(int index){
		return typeList.get(index);
	}
	public void setBasicState(boolean basicState){
		this.basicState = basicState;
	}
	public void setBlock(int a, int b, byte state){
		quads[a][b] = state;
	}
	public void setCurrentTexutre(int id){
		currentTextureId = id;
	}
	public int typeListSize(){
		return typeList.size();
	}
	private void addQuad(int x, int y, int w, int h, int j, int o){
		float shade = j==2?1:j==3?130/255f:j==0||j==1?200/255f:180/255f;
		if(j==0){
			float sx = o;
			float sy = x;
			float sz = y;
			float bx = sx+1;
			float by = sy+w;
			float bz = sz+h;
			short v1 = (short)vertexStorage.place(bx, by, bz, shade, 0, 0, currentTextureId);
			short v2 = (short)vertexStorage.place(bx, sy, bz, shade, 0, w, currentTextureId);
			short v3 = (short)vertexStorage.place(bx, sy, sz, shade, h, w, currentTextureId);
			short v4 = (short)vertexStorage.place(bx, by, sz, shade, h, 0, currentTextureId);
			indexStorage.place(v1);
			indexStorage.place(v2);
			indexStorage.place(v3);
			indexStorage.place(v1);
			indexStorage.place(v3);
			indexStorage.place(v4);
		}else if(j==1){
			float sx = o;
			float sy = x;
			float sz = y;
			float by = sy+w;
			float bz = sz+h;
			short v1 = (short)vertexStorage.place(sx, sy, sz, shade, w, h, currentTextureId);
			short v2 = (short)vertexStorage.place(sx, sy, bz, shade, 0, h, currentTextureId);
			short v3 = (short)vertexStorage.place(sx, by, bz, shade, 0, 0, currentTextureId);
			short v4 = (short)vertexStorage.place(sx, by, sz, shade, w, 0, currentTextureId);
			indexStorage.place(v1);
			indexStorage.place(v2);
			indexStorage.place(v3);
			indexStorage.place(v1);
			indexStorage.place(v3);
			indexStorage.place(v4);
		}else if(j==2){
			float sx = x;
			float sy = o;
			float sz = y;
			float bx = sx+w;
			float by = sy+1;
			float bz = sz+h;
			short v1 = (short)vertexStorage.place(sx, by, sz, shade, 0, 0, currentTextureId);
			short v2 = (short)vertexStorage.place(sx, by, bz, shade, 0, h, currentTextureId);
			short v3 = (short)vertexStorage.place(bx, by, bz, shade, w, h, currentTextureId);
			short v4 = (short)vertexStorage.place(bx, by, sz, shade, w, 0, currentTextureId);
			indexStorage.place(v1);
			indexStorage.place(v2);
			indexStorage.place(v3);
			indexStorage.place(v1);
			indexStorage.place(v3);
			indexStorage.place(v4);
		}else if(j==3){
			float sx = x;
			float sy = o;
			float sz = y;
			float bx = sx+w;
			float bz = sz+h;
			short v1 = (short)vertexStorage.place(bx, sy, bz, shade, w, h, currentTextureId);
			short v2 = (short)vertexStorage.place(sx, sy, bz, shade, 0, h, currentTextureId);
			short v3 = (short)vertexStorage.place(sx, sy, sz, shade, 0, 0, currentTextureId);
			short v4 = (short)vertexStorage.place(bx, sy, sz, shade, w, 0, currentTextureId);
			indexStorage.place(v1);
			indexStorage.place(v2);
			indexStorage.place(v3);
			indexStorage.place(v1);
			indexStorage.place(v3);
			indexStorage.place(v4);
		}else if(j==4){
			float sx = x;
			float sy = y;
			float sz = o;
			float bx = sx+w;
			float by = sy+h;
			float bz = sz+1;
			short v1 = (short)vertexStorage.place(bx, by, bz, shade, 0, 0, currentTextureId);
			short v2 = (short)vertexStorage.place(sx, by, bz, shade, 0, w, currentTextureId);
			short v3 = (short)vertexStorage.place(sx, sy, bz, shade, h, w, currentTextureId);
			short v4 = (short)vertexStorage.place(bx, sy, bz, shade, h, 0, currentTextureId);
			indexStorage.place(v1);
			indexStorage.place(v2);
			indexStorage.place(v3);
			indexStorage.place(v1);
			indexStorage.place(v3);
			indexStorage.place(v4);
		}else{
			float sx = x;
			float sy = y;
			float sz = o;
			float bx = sx+w;
			float by = sy+h;
			short v1 = (short)vertexStorage.place(sx, sy, sz, shade, w, h, currentTextureId);
			short v2 = (short)vertexStorage.place(sx, by, sz, shade, 0, h, currentTextureId);
			short v3 = (short)vertexStorage.place(bx, by, sz, shade, 0, 0, currentTextureId);
			short v4 = (short)vertexStorage.place(bx, sy, sz, shade, w, 0, currentTextureId);
			indexStorage.place(v1);
			indexStorage.place(v2);
			indexStorage.place(v3);
			indexStorage.place(v1);
			indexStorage.place(v3);
			indexStorage.place(v4);
		}
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
}
