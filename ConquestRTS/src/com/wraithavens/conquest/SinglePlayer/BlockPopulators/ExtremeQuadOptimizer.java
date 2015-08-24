package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.util.ArrayList;

public class ExtremeQuadOptimizer{
	public synchronized static void countQuads(QuadCounter counter, int[][] storage, int width, int height,
		int quadCount){
		int x, y, w, h, o;
		if(quadCount>quadReferences.length)
			quadReferences = new boolean[quadCount];
		else
			for(o = 0; o<quadReferences.length; o++)
				quadReferences[o] = false;
		for(x = 0; x<width; x++){
			for(y = 0; y<height; y++){
				if(storage[x][y]==-1)
					continue;
				if(quadReferences[storage[x][y]])
					continue;
				o = storage[x][y];
				quadReferences[o] = true;
				for(w = x; w<width; w++)
					if(storage[w][y]!=o)
						break;
				for(h = y; h<height; h++)
					if(storage[x][h]!=o)
						break;
				w -= x;
				h -= y;
				counter.addQuad(x, y, w, h);
			}
		}
	}
	public synchronized static int optimize(int[][] storage, int[][] temp, int[][] quads, int width, int height){
		int x, y;
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				temp[x][y] = storage[x][y];
		int xS = optimizeXStrong(storage, quads, width, height);
		removeFalse(quads, storage, width, height, xS);
		used.clear();
		int yS = optimizeYStrong(temp, quads, width, height);
		removeFalse(quads, temp, width, height, yS);
		used.clear();
		if(xS<=yS)
			return xS;
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				storage[x][y] = temp[x][y];
		return yS;
	}
	private static int optimizeXStrong(int[][] storage, int[][] quads, int width, int height){
		int x, y, t, q;
		int w = -1;
		int total = 0;
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				storage[x][y] = -1;
		for(y = 0; y<height; y++){
			w = -1;
			for(x = 0; x<width; x++){
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
		for(y = 0; y<height-1; y++){
			w = 0;
			for(x = 0; x<width; x++){
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
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				if(storage[x][y]>-1&&!used.contains(storage[x][y]))
					used.add(storage[x][y]);
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				if(storage[x][y]>-1)
					storage[x][y] = used.indexOf(storage[x][y]);
		return used.size();
	}
	private static int optimizeYStrong(int[][] storage, int[][] quads, int width, int height){
		int x, y, t, q;
		int w = -1;
		int total = 0;
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				storage[x][y] = -1;
		for(x = 0; x<width; x++){
			w = -1;
			for(y = 0; y<height; y++){
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
		for(x = 0; x<width-1; x++){
			w = 0;
			for(y = 0; y<height; y++){
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
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				if(storage[x][y]>-1&&!used.contains(storage[x][y]))
					used.add(storage[x][y]);
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				if(storage[x][y]>-1)
					storage[x][y] = used.indexOf(storage[x][y]);
		return used.size();
	}
	private static void removeFalse(int[][] quads, int[][] storage, int width, int height, int quadCount){
		int x, y, w, h, o, a, b;
		if(quadCount>quadReferences.length)
			quadReferences = new boolean[quadCount];
		else
			for(o = 0; o<quadReferences.length; o++)
				quadReferences[o] = false;
		for(x = 0; x<width; x++){
			quadLooker:for(y = 0; y<height; y++){
				if(storage[x][y]==-1)
					continue;
				if(quadReferences[storage[x][y]])
					continue;
				o = storage[x][y];
				quadReferences[o] = true;
				for(w = x; w<width; w++)
					if(storage[w][y]!=o)
						break;
				for(h = y; h<height; h++)
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
		shrinkQuads(storage, quads, width, height, quadCount);
	}
	private static void shrinkQuads(int[][] storage, int[][] quads, int width, int height, int quadCount){
		int x, y, w, h, o, a, j;
		int nx, ny, nw, nh;
		if(quadCount>quadReferences.length)
			quadReferences = new boolean[quadCount];
		else
			for(o = 0; o<quadReferences.length; o++)
				quadReferences[o] = false;
		for(x = 0; x<width; x++){
			for(y = 0; y<height; y++){
				if(storage[x][y]==-1)
					continue;
				if(quadReferences[storage[x][y]])
					continue;
				o = storage[x][y];
				quadReferences[o] = true;
				for(w = x; w<width; w++)
					if(storage[w][y]!=o)
						break;
				for(h = y; h<height; h++)
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
	private static boolean[] quadReferences = new boolean[10];
	private static final ArrayList<Integer> used = new ArrayList();
}
