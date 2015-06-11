package wraithaven.conquest.client.GameWorld.Voxel;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.QuadCounter;

public class QuadOptimizer{
	private static boolean[] quadReferences = new boolean[10];
	private static final ArrayList<Integer> used = new ArrayList();
	public static void countQuads(QuadCounter counter, int[][] storage, int width, int height, int quadCount){
		int x, y, w, h, o;
		if(quadCount>QuadOptimizer.quadReferences.length) QuadOptimizer.quadReferences = new boolean[quadCount];
		else for(o = 0; o<QuadOptimizer.quadReferences.length; o++)
			QuadOptimizer.quadReferences[o] = false;
		for(x = 0; x<width; x++){
			for(y = 0; y<height; y++){
				if(storage[x][y]==-1) continue;
				if(QuadOptimizer.quadReferences[storage[x][y]]) continue;
				o = storage[x][y];
				QuadOptimizer.quadReferences[o] = true;
				for(w = x; w<width; w++)
					if(storage[w][y]!=o) break;
				for(h = y; h<height; h++)
					if(storage[x][h]!=o) break;
				w -= x;
				h -= y;
				counter.addQuad(x, y, w, h);
			}
		}
	}
	public static int optimize(int[][] storage, int[][] temp, boolean[][] quads, int width, int height){
		int x, y;
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				temp[x][y] = storage[x][y];
		int xS = QuadOptimizer.optimizeXStrong(storage, quads, width, height);
		int yS = QuadOptimizer.optimizeYStrong(temp, quads, width, height);
		QuadOptimizer.used.clear();
		if(xS<=yS) return xS;
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				storage[x][y] = temp[x][y];
		return yS;
	}
	private static int optimizeXStrong(int[][] storage, boolean[][] quads, int width, int height){
		int x, y, t, q;
		int w = -1;
		int total = 0;
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				storage[x][y] = -1;
		for(y = 0; y<height; y++){
			w = -1;
			for(x = 0; x<width; x++){
				if(!quads[x][y]){
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
				if(!quads[x][y]){
					if(w>0){
						q = 0;
						if(!quads[x][y+1]&&(x==w||!quads[x-w-1][y+1])){
							for(t = x-w; t<x; t++){
								if(!quads[t][y+1]) break;
								q++;
							}
							if(q==w) for(t = x-w; t<x; t++)
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
				if(x==w||!quads[x-w-1][y+1]){
					for(t = x-w; t<x; t++){
						if(!quads[t][y+1]) break;
						q++;
					}
					if(q==w) for(t = x-w; t<x; t++)
						storage[t][y+1] = storage[t][y];
				}
			}
		}
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				if(storage[x][y]>-1&&!QuadOptimizer.used.contains(storage[x][y])) QuadOptimizer.used.add(storage[x][y]);
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				if(storage[x][y]>-1) storage[x][y] = QuadOptimizer.used.indexOf(storage[x][y]);
		return QuadOptimizer.used.size();
	}
	private static int optimizeYStrong(int[][] storage, boolean[][] quads, int width, int height){
		int x, y, t, q;
		int w = -1;
		int total = 0;
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				storage[x][y] = -1;
		for(x = 0; x<width; x++){
			w = -1;
			for(y = 0; y<height; y++){
				if(!quads[x][y]){
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
				if(!quads[x][y]){
					if(w>0){
						q = 0;
						if(!quads[x+1][y]&&(y==w||!quads[x+1][y-w-1])){
							for(t = y-w; t<y; t++){
								if(!quads[x+1][t]) break;
								q++;
							}
							if(q==w) for(t = y-w; t<y; t++)
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
				if(y==w||!quads[x+1][y-w-1]){
					for(t = y-w; t<y; t++){
						if(!quads[x+1][t]) break;
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
				if(storage[x][y]>-1&&!QuadOptimizer.used.contains(storage[x][y])) QuadOptimizer.used.add(storage[x][y]);
		for(x = 0; x<width; x++)
			for(y = 0; y<height; y++)
				if(storage[x][y]>-1) storage[x][y] = QuadOptimizer.used.indexOf(storage[x][y]);
		return QuadOptimizer.used.size();
	}
}