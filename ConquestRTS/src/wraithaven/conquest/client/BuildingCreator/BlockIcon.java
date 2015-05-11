package wraithaven.conquest.client.BuildingCreator;

import java.util.ArrayList;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.ChunklessBlock;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.ChunklessBlockHolder;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import static org.lwjgl.opengl.GL11.*;

public class BlockIcon implements ChunklessBlockHolder{
	public int itemSlot;
	final BlockShape shape;
	final CubeTextures textures;
	private final ArrayList<QuadBatch> batches = new ArrayList();
	final ChunklessBlock block;
	private static final float BLOCK_PITCH = 30;
	private static final float BLOCK_YAW = 45;
	public static float BLOCK_ZOOM = 30;
	public BlockIcon(BlockShape shape, CubeTextures textures){
		this.shape=shape;
		this.textures=textures;
		block=new ChunklessBlock(this, shape, textures);
		block.build();
		for(int i = 0; i<6; i++)block.optimizeSide(i);
		for(int i = 0; i<batches.size(); i++)batches.get(i).recompileBuffer();
	}
	public void render(){
		glPushMatrix();
		glTranslatef(getX(itemSlot), getY(itemSlot), 0);
		glRotatef(BLOCK_PITCH, 1, 0, 0);
		glRotatef(BLOCK_YAW, 0, 1, 0);
		glTranslatef(-0.5f, -0.5f, -0.5f);
		for(int i = 0; i<batches.size(); i++){
			batches.get(i).getTexture().bind();
			batches.get(i).renderPart();
		}
		glPopMatrix();
	}
	public QuadBatch getBatch(Texture texture){
		QuadBatch batch;
		for(int i = 0; i<batches.size(); i++){
			batch=batches.get(i);
			if(batch.getTexture()==texture)return batch;
		}
		batch=new QuadBatch(texture, true, 0, 0, 0);
		batches.add(batch);
		return batch;
	}
	private static float getX(int id){ return (id<10?1:-1)*(1-GuiHandler.HOTBAR_SLOT)*(BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height/2f); }
	private static float getY(int id){ return (((0.5f-((GuiHandler.HOTBAR_SLOT*Loop.screenRes.width/Loop.screenRes.height)*10)/2f-GuiHandler.HOTBAR_SLOT/2f)+(GuiHandler.HOTBAR_SLOT*Loop.screenRes.width/Loop.screenRes.height)*(9-id%10))-0.5f+GuiHandler.HOTBAR_SLOT)*BLOCK_ZOOM; }
}