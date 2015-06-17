package wraithaven.conquest.client.GameWorld.TileSystem;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.QuadOptimizer;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.QuadListener;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkXQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkYQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkZQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.Quad;

public class Tile{
	private static void updateTile(Tile t){
		if(t!=null)t.update(false);
	}
	private static int getHeightestNumber(ArrayList<Integer> numbers){
		int i = Integer.MIN_VALUE;
		for(int a : numbers)
			if(a>i)i = a;
		return i;
	}
	public static final int TILE_SIZE_BITS = 3;
	public static final int TILE_SIZE = (int)Math.pow(2, TILE_SIZE_BITS);
	public static final short MIN_HEIGHT = 0;
	public static final short MAX_HEIGHT = 100;
	private static final ChunkXQuadCounter QUAD_COUNTER_X = new ChunkXQuadCounter();
	private static final ChunkYQuadCounter QUAD_COUNTER_Y = new ChunkYQuadCounter();
	private static final ChunkZQuadCounter QUAD_COUNTER_Z = new ChunkZQuadCounter();
	private static final boolean[][] TEMP_QUADS = new boolean[Tile.TILE_SIZE*8][Tile.TILE_SIZE*8]; 
	private static final int[][] TEMP_HEIGHTS = new int[Tile.TILE_SIZE*8][Tile.TILE_SIZE*8];
	private static final int[][] TEMP_STORAGE = new int[Tile.TILE_SIZE*8][Tile.TILE_SIZE*8];
	private static final int[][] TEMP_STORAGE_2 = new int[Tile.TILE_SIZE*8][Tile.TILE_SIZE*8];
	private short height;
	public final int x, z;
	private final TileWorld world;
	private final ArrayList<Quad> ownedQuads = new ArrayList();
	private final QuadListener quadListener = new QuadListener(){
		public void addQuad(Quad q){
			Tile.this.addQuad(q, world.getTextures().getTexture(q.side));
		}
	};
	public Tile(TileWorld world, int x, int z, short height){
		this.x = x;
		this.z = z;
		this.world = world;
		this.height = (short)Math.min(Math.max(MIN_HEIGHT, height), MAX_HEIGHT);
	}
	public void raise(){
		if(height==MAX_HEIGHT)return;
		height++;
		update(true);
	}
	public void lower(){
		if(height==MIN_HEIGHT)return;
		height--;
		update(true);
	}
	public int getHeight(){
		return height;
	}
	public void setHeight(short height){
		this.height = height;
		rebuild();
	}
	private void rebuild(){
		clearQuads(false);
		build();
		world.recompileBuffers();
	}
	private void build(){
		short h1 = getTileHeight(x-1, z-1);
		short h2 = getTileHeight(x, z-1);
		short h3 = getTileHeight(x+1, z-1);
		short h4 = getTileHeight(x-1, z);
		short h5 = (short)(height*8-1);
		short h6 = getTileHeight(x+1, z);
		short h7 = getTileHeight(x-1, z+1);
		short h8 = getTileHeight(x, z+1);
		short h9 = getTileHeight(x+1, z+1);
		int cornerOffset = (Tile.TILE_SIZE-1)*8;
		int x, y, z, q;
		for(x = 8; x<cornerOffset; x++)
			for(z = 8; z<cornerOffset; z++)
				TEMP_HEIGHTS[x][z] = h5;
		for(x = 0; x<8; x++)
			for(z = 0; z<8; z++){
				TEMP_HEIGHTS[x][z] = (int)Math.round(BilinearInterpolation.cosineInterpolation(h1, h2, h4, h5, (x+8)/16.0, (z+8)/16.0));
				TEMP_HEIGHTS[x+cornerOffset][z] = (int)Math.round(BilinearInterpolation.cosineInterpolation(h2, h3, h5, h6, x/16.0, (z+8)/16.0));
				TEMP_HEIGHTS[x][z+cornerOffset] = (int)Math.round(BilinearInterpolation.cosineInterpolation(h4, h5, h7, h8, (x+8)/16.0, z/16.0));
				TEMP_HEIGHTS[x+cornerOffset][z+cornerOffset] = (int)Math.round(BilinearInterpolation.cosineInterpolation(h5, h6, h8, h9, x/16.0, z/16.0));
			}
		int smallBlocks = Tile.TILE_SIZE*8;
		for(x = 8; x<cornerOffset; x++)
			for(z = 0; z<8; z++)
				TEMP_HEIGHTS[x][z] = (int)Math.round(BilinearInterpolation.cosineInterpolation(h2, h5, (z+8)/16.0));
		for(x = 8; x<cornerOffset; x++)
			for(z = 0; z<8; z++)
				TEMP_HEIGHTS[x][z+cornerOffset] = (int)Math.round(BilinearInterpolation.cosineInterpolation(h5, h8, z/16.0));
		for(x = 0; x<8; x++)
			for(z = 8; z<cornerOffset; z++)
				TEMP_HEIGHTS[x][z] = (int)Math.round(BilinearInterpolation.cosineInterpolation(h4, h5, (x+8)/16.0));
		for(x = 0; x<8; x++)
			for(z = 8; z<cornerOffset; z++)
				TEMP_HEIGHTS[x+cornerOffset][z] = (int)Math.round(BilinearInterpolation.cosineInterpolation(h5, h6, x/16.0));
		ArrayList<Integer> heights = new ArrayList();
		for(x = 0; x<smallBlocks; x++)
			for(z = 0; z<smallBlocks; z++)
				if(!heights.contains(TEMP_HEIGHTS[x][z]))heights.add(TEMP_HEIGHTS[x][z]);
		int highestValue = getHeightestNumber(heights);
		int lowestValue = getLowestHeight();
		for(int i : heights){
			for(x = 0; x<smallBlocks; x++)
				for(z = 0; z<smallBlocks; z++)
					TEMP_QUADS[x][z] = TEMP_HEIGHTS[x][z]==i;
			q = QuadOptimizer.optimize(TEMP_STORAGE, TEMP_STORAGE_2, TEMP_QUADS, smallBlocks, smallBlocks);
			QUAD_COUNTER_Y.setup(this.x*Tile.TILE_SIZE, 0, this.z*Tile.TILE_SIZE, i, 2, quadListener, world.getTextures().yUpRotation, world.getTextures().colors[6], world.getTextures().colors[7], world.getTextures().colors[8]);
			QuadOptimizer.countQuads(QUAD_COUNTER_Y, TEMP_STORAGE, smallBlocks, smallBlocks, q);
		}
		int[][] partHeights = new int[(highestValue-lowestValue)+1][smallBlocks];
		boolean[][] quads = new boolean[partHeights.length][smallBlocks];
		int[][] storage = new int[partHeights.length][smallBlocks];
		int[][] storage2 = new int[partHeights.length][smallBlocks];
		for(x = 0; x<smallBlocks; x++){
			for(z = 0; z<smallBlocks; z++)
				for(y = 0; y<partHeights.length; y++){
					if(x==smallBlocks-1)quads[y][z] = TEMP_HEIGHTS[x][z]>=y+lowestValue;
					else quads[y][z] = TEMP_HEIGHTS[x][z]>=y+lowestValue
							&&TEMP_HEIGHTS[x+1][z]<y+lowestValue;
				}
			q = QuadOptimizer.optimize(storage, storage2, quads, quads.length, smallBlocks);
			if(q==0)continue;
			QUAD_COUNTER_X.setup(this.x*Tile.TILE_SIZE, 0, this.z*Tile.TILE_SIZE, x, 0, quadListener, world.getTextures().xUpRotation, world.getTextures().colors[0], world.getTextures().colors[1], world.getTextures().colors[2], lowestValue);
			QuadOptimizer.countQuads(QUAD_COUNTER_X, storage, quads.length, smallBlocks, q);
		}
		for(x = 0; x<smallBlocks; x++){
			for(z = 0; z<smallBlocks; z++)
				for(y = 0; y<partHeights.length; y++){
					if(x==0)quads[y][z] = TEMP_HEIGHTS[x][z]>=y+lowestValue;
					else quads[y][z] = TEMP_HEIGHTS[x][z]>=y+lowestValue
							&&TEMP_HEIGHTS[x-1][z]<y+lowestValue;
				}
			q = QuadOptimizer.optimize(storage, storage2, quads, quads.length, smallBlocks);
			if(q==0)continue;
			QUAD_COUNTER_X.setup(this.x*Tile.TILE_SIZE, 0, this.z*Tile.TILE_SIZE, x, 1, quadListener, world.getTextures().xDownRotation, world.getTextures().colors[3], world.getTextures().colors[4], world.getTextures().colors[5], lowestValue);
			QuadOptimizer.countQuads(QUAD_COUNTER_X, storage, quads.length, smallBlocks, q);
		}
		partHeights = new int[smallBlocks][(highestValue-lowestValue)+1];
		quads = new boolean[smallBlocks][partHeights.length];
		storage = new int[smallBlocks][partHeights.length];
		storage2 = new int[smallBlocks][partHeights.length];
		for(z = 0; z<smallBlocks; z++){
			for(x = 0; x<smallBlocks; x++)
				for(y = 0; y<partHeights.length; y++){
					if(z==smallBlocks-1)quads[x][y] = TEMP_HEIGHTS[x][z]>=y+lowestValue;
					else quads[x][y] = TEMP_HEIGHTS[x][z]>=y+lowestValue
							&&TEMP_HEIGHTS[x][z+1]<y+lowestValue;
				}
			q = QuadOptimizer.optimize(storage, storage2, quads, quads.length, smallBlocks);
			if(q==0)continue;
			QUAD_COUNTER_Z.setup(this.x*Tile.TILE_SIZE, 0, this.z*Tile.TILE_SIZE, z, 4, quadListener, world.getTextures().zUpRotation, world.getTextures().colors[12], world.getTextures().colors[13], world.getTextures().colors[14], lowestValue);
			QuadOptimizer.countQuads(QUAD_COUNTER_Z, storage, quads.length, smallBlocks, q);
		}
		for(z = 0; z<smallBlocks; z++){
			for(x = 0; x<smallBlocks; x++)
				for(y = 0; y<partHeights.length; y++){
					if(z==0)quads[x][y] = TEMP_HEIGHTS[x][z]>=y+lowestValue;
					else quads[x][y] = TEMP_HEIGHTS[x][z]>=y+lowestValue
							&&TEMP_HEIGHTS[x][z-1]<y+lowestValue;
				}
			q = QuadOptimizer.optimize(storage, storage2, quads, quads.length, smallBlocks);
			if(q==0)continue;
			QUAD_COUNTER_Z.setup(this.x*Tile.TILE_SIZE, 0, this.z*Tile.TILE_SIZE, z, 5, quadListener, world.getTextures().zDownRotation, world.getTextures().colors[15], world.getTextures().colors[16], world.getTextures().colors[17], lowestValue);
			QuadOptimizer.countQuads(QUAD_COUNTER_Z, storage, quads.length, smallBlocks, q);
		}
	}
	private int getLowestHeight(){
		int h = height*8;
		h = Math.min(getTileHeight(x-1, z-1), h);
		h = Math.min(getTileHeight(x, z-1), h);
		h = Math.min(getTileHeight(x+1, z-1), h);
		h = Math.min(getTileHeight(x-1, z), h);
		h = Math.min(getTileHeight(x, z), h);
		h = Math.min(getTileHeight(x-1, z+1), h);
		h = Math.min(getTileHeight(x, z+1), h);
		h = Math.min(getTileHeight(x+1, z+1), h);
		return h;
	}
	private short getTileHeight(int x, int z){
		Tile t = world.getTile(x, z);
		if(t==null)return (short)(height*8-1);
		return (short)(t.height*8-1);
	}
	public void update(boolean primary){
		if(primary){
			updateTile(world.getTile(x+1, z));
			updateTile(world.getTile(x-1, z));
			updateTile(world.getTile(x, z+1));
			updateTile(world.getTile(x, z-1));
			updateTile(world.getTile(x+1, z+1));
			updateTile(world.getTile(x+1, z-1));
			updateTile(world.getTile(x-1, z+1));
			updateTile(world.getTile(x-1, z-1));
		}
		rebuild();
	}
	private void addQuad(Quad q, Texture t){
		ownedQuads.add(q);
		world.addQuad(q, t);
	}
	public void dispose(){
		clearQuads(true);
		world.removeTile(this);
	}
	private void clearQuads(boolean recompile){
		for(Quad q : ownedQuads)
			world.removeQuad(q);
		ownedQuads.clear();
		if(recompile)world.recompileBuffers();
	}
}