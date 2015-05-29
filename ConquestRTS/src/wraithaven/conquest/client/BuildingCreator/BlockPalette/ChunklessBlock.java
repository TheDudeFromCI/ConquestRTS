package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkXQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkYQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkZQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import wraithaven.conquest.client.GameWorld.Voxel.QuadOptimizer;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.BlockSideProperties;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.Block;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;

public class ChunklessBlock{
	public final Block block;
	private final ChunklessBlockHolder holder;
	private static final ArrayList<BlockSideProperties> PROPS_TEMP_STORAGE = new ArrayList();
	private static final int[][] TEMP_STORAGE = new int[8][8];
	private static final int[][] TEMP_STORAGE_2 = new int[8][8];
	private static final boolean[][] TEMP_QUADS = new boolean[8][8];
	private static final ChunkXQuadCounter QUAD_COUNTER_X = new ChunkXQuadCounter();
	private static final ChunkYQuadCounter QUAD_COUNTER_Y = new ChunkYQuadCounter();
	private static final ChunkZQuadCounter QUAD_COUNTER_Z = new ChunkZQuadCounter();
	public ChunklessBlock(ChunklessBlockHolder holder, BlockShape shape, CubeTextures textures, BlockRotation rotation){
		block=new Block(shape, textures, rotation);
		this.holder=holder;
		rebuild();
	}
	private void countTextures(int i, int side){
		PROPS_TEMP_STORAGE.clear();
		int x, y, z;
		BlockSideProperties props;
		if(side==0||side==1){
			x=i;
			for(y=0; y<1; y++){
				for(z=0; z<1; z++){
					props=new BlockSideProperties();
					props.texture=block.textures.getTexture(side);
					props.side=side;
					props.rotation=block.textures.getRotation(side);
					props.r=block.textures.colors[side*3];
					props.g=block.textures.colors[side*3+1];
					props.b=block.textures.colors[side*3+2];
					if(!PROPS_TEMP_STORAGE.contains(props))PROPS_TEMP_STORAGE.add(props);
				}
			}
		}
		if(side==2||side==3){
			y=i;
			for(x=0; x<1; x++){
				for(z=0; z<1; z++){
					props=new BlockSideProperties();
					props.texture=block.textures.getTexture(side);
					props.side=side;
					props.rotation=block.textures.getRotation(side);
					props.r=block.textures.colors[side*3];
					props.g=block.textures.colors[side*3+1];
					props.b=block.textures.colors[side*3+2];
					if(!PROPS_TEMP_STORAGE.contains(props))PROPS_TEMP_STORAGE.add(props);
				}
			}
		}
		if(side==4||side==5){
			z=i;
			for(x=0; x<1; x++){
				for(y=0; y<1; y++){
					props=new BlockSideProperties();
					props.texture=block.textures.getTexture(side);
					props.side=side;
					props.rotation=block.textures.getRotation(side);
					props.r=block.textures.colors[side*3];
					props.g=block.textures.colors[side*3+1];
					props.b=block.textures.colors[side*3+2];
					if(!PROPS_TEMP_STORAGE.contains(props))PROPS_TEMP_STORAGE.add(props);
				}
			}
		}
	}
	public void rebuild(){
		int i, j, k, l;
		QuadBatch batch;
		for(j=0; j<6; j++){
			for(i=0; i<1; i++){
				countTextures(i, j);
				for(l=0; l<PROPS_TEMP_STORAGE.size(); l++){
					batch=holder.getBatch(PROPS_TEMP_STORAGE.get(l).texture);
					for(k=0; k<8; k++){
						if(j==0||j==1)optimizeSideX(i*8+k, j, batch, PROPS_TEMP_STORAGE.get(l));
						if(j==2||j==3)optimizeSideY(i*8+k, j, batch, PROPS_TEMP_STORAGE.get(l));
						if(j==4||j==5)optimizeSideZ(i*8+k, j, batch, PROPS_TEMP_STORAGE.get(l));
					}
				}
			}
		}
		PROPS_TEMP_STORAGE.clear();
	}
	private boolean getSmallBlock(int x, int y, int z, BlockSideProperties props){
		if(props==null
				||(block.textures.getTexture(props.side)==props.texture
				&&block.textures.getRotation(props.side)==props.rotation
				&&block.textures.colors[props.side*3]==props.r
				&&block.textures.colors[props.side*3+1]==props.g
				&&block.textures.colors[props.side*3+2]==props.b))return block.shape.getBlock(x%8, y%8, z%8, block.rotation);
		return false;
	}
	private boolean hasSmallNeighbor(int x, int y, int z, int side){
		if(side==0)return x==7?false:getSmallBlock(x+1, y, z, null);
		if(side==1)return x==0?false:getSmallBlock(x-1, y, z, null);
		if(side==2)return y==7?false:getSmallBlock(x, y+1, z, null);
		if(side==3)return y==0?false:getSmallBlock(x, y-1, z, null);
		if(side==4)return z==7?false:getSmallBlock(x, y, z+1, null);
		if(side==5)return z==0?false:getSmallBlock(x, y, z-1, null);
		return false;
	}
	private void optimizeSideX(int x, int side, QuadBatch batch, BlockSideProperties props){
		int y, z, q;
		for(y=0; y<8; y++)for(z=0; z<8; z++)TEMP_QUADS[y][z]=getSmallBlock(x, y, z, props)&&!hasSmallNeighbor(x, y, z, side);
		if((q=QuadOptimizer.optimize(TEMP_STORAGE, TEMP_STORAGE_2, TEMP_QUADS, 8, 8))>0){
			QUAD_COUNTER_X.setup(0, 0, 0, x, side, batch, props.rotation, props.r, props.g, props.b);
			QuadOptimizer.countQuads(QUAD_COUNTER_X, TEMP_STORAGE, 8, 8, q);
		}
	}
	private void optimizeSideY(int y, int side, QuadBatch batch, BlockSideProperties props){
		int x, z, q;
		for(x=0; x<8; x++)for(z=0; z<8; z++)TEMP_QUADS[x][z]=getSmallBlock(x, y, z, props)&&!hasSmallNeighbor(x, y, z, side);
		if((q=QuadOptimizer.optimize(TEMP_STORAGE, TEMP_STORAGE_2, TEMP_QUADS, 8, 8))>0){
			QUAD_COUNTER_Y.setup(0, 0, 0, y, side, batch, props.rotation, props.r, props.g, props.b);
			QuadOptimizer.countQuads(QUAD_COUNTER_Y, TEMP_STORAGE, 8, 8, q);
		}
	}
	private void optimizeSideZ(int z, int side, QuadBatch batch, BlockSideProperties props){
		int y, x, q;
		for(x=0; x<8; x++)for(y=0; y<8; y++)TEMP_QUADS[x][y]=getSmallBlock(x, y, z, props)&&!hasSmallNeighbor(x, y, z, side);
		if((q=QuadOptimizer.optimize(TEMP_STORAGE, TEMP_STORAGE_2, TEMP_QUADS, 8, 8))>0){
			QUAD_COUNTER_Z.setup(0, 0, 0, z, side, batch, props.rotation, props.r, props.g, props.b);
			QuadOptimizer.countQuads(QUAD_COUNTER_Z, TEMP_STORAGE, 8, 8, q);
		}
	}
}