package wraithaven.conquest.client.GameWorld.Voxel; 

import java.nio.FloatBuffer;

public class CustomBlock extends Block{
	private boolean block_dd, block_d0, block_du, block_0d, block_0u, block_ud, block_u0, block_uu;
	protected final BlockShape shape;
	private final float[] texturePoints = new float[4];
	private final CubeTextures textures;
	private final SubBlock[] subBlocks = new SubBlock[512];
	private static final float shadowIntensity = 0.65f;
	protected CustomBlock(Chunk chunk, int x, int y, int z, BlockType type, BlockShape shape, CubeTextures textures){
		super(chunk, x, y, z, type);
		this.shape=shape;
		this.textures=textures;
	}
	public void build(){
		for(int i = 0; i<subBlocks.length; i++)if(shape.getBlock(i))subBlocks[i]=new SubBlock();
		optimize();
	}
	public void destroy(){
		int i, j;
		for(i=0; i<subBlocks.length; i++){
			if(subBlocks[i]==null)continue;
			for(j=0; j<6; j++){
				if(subBlocks[i].quads[j]==null)continue;
				getBatch(textures.getTexture(j)).removeQuad(subBlocks[i].quads[j]);
			}
			subBlocks[i]=null;
		}
		for(j=0; j<6; j++){
			if(quads[j]==null)continue;
			getLargeBatch(textures.getTexture(j)).removeQuad(quads[j]);
			quads[j]=null;
		}
	}
	private void optimize(){
		int x, y, z, index, j;
		for(x=0; x<8; x++){
			for(y=0; y<8; y++){
				for(z=0; z<8; z++){
					index=BlockShape.getIndex(x, y, z);
					if(subBlocks[index]==null)continue;
					for(j=0; j<6; j++){
						if(shape.hasNeighbor(x, y, z, j)==0){
							subBlocks[index].quads[j]=Cube.generateQuad(j, this.x+(x/8f), this.y+(y/8f), this.z+(z/8f), textures.getRotation(j), WHITE_COLORS, false, generateTexturePoints(x, y, z, j, textures.getRotation(j)));
							subBlocks[index].quads[j].centerPoint=updateShadows(subBlocks[index].quads[j].data, x, y, z, j);
							getBatch(textures.getTexture(j)).addQuad(subBlocks[index].quads[j]);
							if(chunk!=null)chunk.setNeedsRebatch();
						}
					}
				}
			}
		}
	}
	public void optimizeSide(int side){
		if(shape.fullSide(side)){
			boolean nearBlock = isSideHidden(side);
			if(nearBlock!=(quads[side]==null)){
				if(nearBlock){
					getLargeBatch(textures.getTexture(side)).removeQuad(quads[side]);
					quads[side]=null;
					if(isFullyHidden())setHidden(true);
					else setHidden(false);
				}else{
					quads[side]=Cube.generateQuad(side, x, y, z, textures.getRotation(side), WHITE_COLORS, true, TEXTURE_POSITIONS);
					getLargeBatch(textures.getTexture(side)).addQuad(quads[side]);
					if(isFullyHidden())setHidden(true);
					else setHidden(false);
				}
				if(chunk!=null)chunk.setNeedsRebatch();
			}
			return;
		}
		if(side==0||side==1){
			int y, z;
			int x = side==0?7:0;
			for(y=0; y<8; y++)for(z=0; z<8; z++)optimizeSubBlockSide(BlockShape.getIndex(x, y, z), side, getSubBlock(x==7?8:-1, y, z), x, y, z);
		}
		if(side==2||side==3){
			int x, z;
			int y = side==2?7:0;
			for(x=0; x<8; x++)for(z=0; z<8; z++)optimizeSubBlockSide(BlockShape.getIndex(x, y, z), side, getSubBlock(x, y==7?8:-1, z), x, y, z);
		}
		if(side==4||side==5){
			int x, y;
			int z = side==4?7:0;
			for(x=0; x<8; x++)for(y=0; y<8; y++)optimizeSubBlockSide(BlockShape.getIndex(x, y, z), side, getSubBlock(x, y, z==7?8:-1), x, y, z);
		}
	}
	private void optimizeSubBlockSide(int index, int side, boolean nextBlock, int x, int y, int z){
		if(subBlocks[index]==null)return;
		if(subBlocks[index].quads[side]==null!=nextBlock){
			if(nextBlock){
				getBatch(textures.getTexture(side)).removeQuad(subBlocks[index].quads[side]);
				subBlocks[index].quads[side]=null;
			}else{
				subBlocks[index].quads[side]=Cube.generateQuad(side, this.x+(x/8f), this.y+(y/8f), this.z+(z/8f), textures.getRotation(side), WHITE_COLORS, false, generateTexturePoints(x, y, z, side, textures.getRotation(side)));
				subBlocks[index].quads[side].centerPoint=updateShadows(subBlocks[index].quads[side].data, x, y, z, side);
				getBatch(textures.getTexture(side)).addQuad(subBlocks[index].quads[side]);
			}
			if(chunk!=null)chunk.setNeedsRebatch();
		}else if(subBlocks[index].quads[side]!=null)subBlocks[index].quads[side].centerPoint=updateShadows(subBlocks[index].quads[side].data, x, y, z, side);
	}
	protected boolean getSubBlock(int x, int y, int z){
		if(x<0||x>7||y<0||y>7||z<0||z>7){
			Block block = chunk.world.getBlock(this.x+(x>>3), this.y+(y>>3), this.z+(z>>3));
			if(block==null)return false;
			if(block instanceof CustomBlock)return ((CustomBlock)block).shape.getBlock(x&7, y&7, z&7);
			return true;
		}
		return shape.getBlock(x, y, z);
	}
	private boolean isSideHidden(int side){
		Block block = getTouchingBlock(side);
		if(block==null)return false;
		if(block instanceof CustomBlock)return ((CustomBlock)block).shape.fullSide(oppositeSide(side));
		return true;
	}
	private boolean updateShadows(FloatBuffer data, int x, int y, int z, int side){
		if(side==0){
			block_dd=getSubBlock(x+1, y-1, z-1);
			block_d0=getSubBlock(x+1, y-1, z);
			block_du=getSubBlock(x+1, y-1, z+1);
			block_0d=getSubBlock(x+1, y, z-1);
			block_0u=getSubBlock(x+1, y, z+1);
			block_ud=getSubBlock(x+1, y+1, z-1);
			block_u0=getSubBlock(x+1, y+1, z);
			block_uu=getSubBlock(x+1, y+1, z+1);
			if(block_uu||block_0u||block_u0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_dd||block_0d||block_d0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==1){
			block_dd=getSubBlock(x-1, y-1, z-1);
			block_d0=getSubBlock(x-1, y-1, z);
			block_du=getSubBlock(x-1, y-1, z+1);
			block_0d=getSubBlock(x-1, y, z-1);
			block_0u=getSubBlock(x-1, y, z+1);
			block_ud=getSubBlock(x-1, y+1, z-1);
			block_u0=getSubBlock(x-1, y+1, z);
			block_uu=getSubBlock(x-1, y+1, z+1);
			if(block_dd||block_0d||block_d0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_uu||block_0u||block_u0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==2){
			block_dd=getSubBlock(x-1, y+1, z-1);
			block_d0=getSubBlock(x-1, y+1, z);
			block_du=getSubBlock(x-1, y+1, z+1);
			block_0d=getSubBlock(x, y+1, z-1);
			block_0u=getSubBlock(x, y+1, z+1);
			block_ud=getSubBlock(x+1, y+1, z-1);
			block_u0=getSubBlock(x+1, y+1, z);
			block_uu=getSubBlock(x+1, y+1, z+1);
			if(block_dd||block_0d||block_d0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_uu||block_0u||block_u0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==3){
			block_dd=getSubBlock(x-1, y-1, z-1);
			block_d0=getSubBlock(x-1, y-1, z);
			block_du=getSubBlock(x-1, y-1, z+1);
			block_0d=getSubBlock(x, y-1, z-1);
			block_0u=getSubBlock(x, y-1, z+1);
			block_ud=getSubBlock(x+1, y-1, z-1);
			block_u0=getSubBlock(x+1, y-1, z);
			block_uu=getSubBlock(x+1, y-1, z+1);
			if(block_uu||block_u0||block_0u){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_0u||block_d0){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_dd||block_0d||block_d0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==4){
			block_dd=getSubBlock(x-1, y-1, z+1);
			block_d0=getSubBlock(x-1, y, z+1);
			block_du=getSubBlock(x-1, y+1, z+1);
			block_0d=getSubBlock(x, y-1, z+1);
			block_0u=getSubBlock(x, y+1, z+1);
			block_ud=getSubBlock(x+1, y-1, z+1);
			block_u0=getSubBlock(x+1, y, z+1);
			block_uu=getSubBlock(x+1, y+1, z+1);
			if(block_uu||block_0u||block_u0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_dd||block_0d||block_d0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==5){
			block_dd=getSubBlock(x-1, y-1, z-1);
			block_d0=getSubBlock(x-1, y, z-1);
			block_du=getSubBlock(x-1, y+1, z-1);
			block_0d=getSubBlock(x, y-1, z-1);
			block_0u=getSubBlock(x, y+1, z-1);
			block_ud=getSubBlock(x+1, y-1, z-1);
			block_u0=getSubBlock(x+1, y, z-1);
			block_uu=getSubBlock(x+1, y+1, z-1);
			if(block_dd||block_0d||block_d0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_uu||block_0u||block_u0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if((block_d0&&block_0d)||(block_u0&&block_0u)){
			data.put(12, shadowIntensity);
			data.put(13, shadowIntensity);
			data.put(14, shadowIntensity);
		}else if(flat())return false;
		else{
			data.put(12, 1);
			data.put(13, 1);
			data.put(14, 1);
		}
		return true;
	}
	private boolean flat(){
		if(block_dd||block_ud||block_du||block_uu){
			if(block_dd){
				if(block_d0||block_0d)return true;
				return false;
			}
			if(block_du){
				if(block_d0||block_0u)return true;
				return false;
			}
			if(block_ud){
				if(block_u0||block_0d)return true;
				return false;
			}
			if(block_uu){
				if(block_u0||block_0u)return true;
				return false;
			}
		}else{
			if(block_d0==block_u0)return true;
			if(block_0d==block_0u)return true;
		}
		return false;
	}
	private float[] generateTexturePoints(int x, int y, int z, int side, int r){
		if(side==0){
			if(r==0){
				texturePoints[0]=(7-z)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-y)/8f;
				texturePoints[3]=1/8f;
			}
			if(r==1){
				texturePoints[0]=(7-y)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=z/8f;
				texturePoints[3]=1/8f;
			}
			if(r==2){
				texturePoints[0]=z/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=y/8f;
				texturePoints[3]=1/8f;
			}
			if(r==3){
				texturePoints[0]=y/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-z)/8f;
				texturePoints[3]=1/8f;
			}
		}
		if(side==1){
			if(r==0){
				texturePoints[0]=y/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=z/8f;
				texturePoints[3]=1/8f;
			}
			if(r==1){
				texturePoints[0]=z/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-y)/8f;
				texturePoints[3]=1/8f;
			}
			if(r==2){
				texturePoints[0]=(7-y)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-z)/8f;
				texturePoints[3]=1/8f;
			}
			if(r==3){
				texturePoints[0]=(7-z)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=y/8f;
				texturePoints[3]=1/8f;
			}
		}
		if(side==2){
			if(r==0){
				texturePoints[0]=x/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=z/8f;
				texturePoints[3]=1/8f;
			}
			if(r==1){
				texturePoints[0]=z/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-x)/8f;
				texturePoints[3]=1/8f;
			}
			if(r==2){
				texturePoints[0]=(7-x)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-z)/8f;
				texturePoints[3]=1/8f;
			}
			if(r==3){
				texturePoints[0]=(7-z)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=x/8f;
				texturePoints[3]=1/8f;
			}
		}
		if(side==3){
			if(r==0){
				texturePoints[0]=(7-z)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-x)/8f;
				texturePoints[3]=1/8f;
			}
			if(r==1){
				texturePoints[0]=(7-x)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=z/8f;
				texturePoints[3]=1/8f;
			}
			if(r==2){
				texturePoints[0]=z/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=x/8f;
				texturePoints[3]=1/8f;
			}
			if(r==3){
				texturePoints[0]=x/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-z)/8f;
				texturePoints[3]=1/8f;
			}
		}
		if(side==4){
			if(r==0){
				texturePoints[0]=(7-y)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-x)/8f;
				texturePoints[3]=1/8f;
			}
			if(r==1){
				texturePoints[0]=(7-x)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=y/8f;
				texturePoints[3]=1/8f;
			}
			if(r==2){
				texturePoints[0]=y/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=x/8f;
				texturePoints[3]=1/8f;
			}
			if(r==3){
				texturePoints[0]=x/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-y)/8f;
				texturePoints[3]=1/8f;
			}
		}
		if(side==5){
			if(r==0){
				texturePoints[0]=x/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=y/8f;
				texturePoints[3]=1/8f;
			}
			if(r==1){
				texturePoints[0]=y/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-x)/8f;
				texturePoints[3]=1/8f;
			}
			if(r==2){
				texturePoints[0]=(7-x)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=(7-y)/8f;
				texturePoints[3]=1/8f;
			}
			if(r==3){
				texturePoints[0]=(7-y)/8f;
				texturePoints[1]=1/8f;
				texturePoints[2]=x/8f;
				texturePoints[3]=1/8f;
			}
		}
		return texturePoints;
	}
	@Override protected boolean isFullyHidden(){
		int i, j;
		for(i=0; i<6; i++)if(quads[i]!=null)return false;
		for(i=0; i<subBlocks.length; i++){
			if(subBlocks[i]==null)continue;
			for(j=0; j<6; j++)if(subBlocks[i].quads[j]!=null)return false;
		}
		return true;
	}
	protected QuadBatch getBatch(Texture texture){ return chunk.getBatch(texture, true, x, y, z); }
	protected QuadBatch getLargeBatch(Texture texture){ return chunk.getBatch(texture, false, x, y, z); }
	private static int oppositeSide(int side){
		if(side==0)return 1;
		if(side==1)return 0;
		if(side==2)return 3;
		if(side==3)return 2;
		if(side==4)return 5;
		if(side==5)return 4;
		return side;
	}
}