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
	private float shiftRY;
	private final ArrayList<QuadBatch> batches = new ArrayList();
	private final ChunklessBlock block;
	private final float initalRandomSpin;
	private static final float SPIN_SPEED = 30;
	private static final float BLOCK_PITCH = 30;
	public static float BLOCK_ZOOM = 40;
	public BlockIcon(BlockShape shape, CubeTextures textures){
		this.shape=shape;
		this.textures=textures;
		block=new ChunklessBlock(this, shape, textures);
		block.build();
		for(int i = 0; i<6; i++)block.optimizeSide(i);
		for(int i = 0; i<batches.size(); i++)batches.get(i).recompileBuffer();
		initalRandomSpin=(float)(Math.random()*10);
	}
	public void update(double time){
		time+=initalRandomSpin;
		shiftRY=(float)((time*SPIN_SPEED)%360);
	}
	public void render(){
		glPushMatrix();
		glTranslatef(getX(itemSlot), getY(itemSlot), 0);
		glRotatef(shiftRY, 0, 1, 0);
		glRotatef(BLOCK_PITCH, 1, 0, 0);
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
	private static float getX(int id){ return (id<10?1:-1)*(1-GuiHandler.HOTBAR_SLOT)*(BLOCK_ZOOM/2f); }
	private static float getY(int id){ return (((0.5f-((GuiHandler.HOTBAR_SLOT*Loop.screenRes.width/Loop.screenRes.height)*10)/2f-GuiHandler.HOTBAR_SLOT/2f)+(GuiHandler.HOTBAR_SLOT*Loop.screenRes.width/Loop.screenRes.height)*(9-id%10))-0.5f+GuiHandler.HOTBAR_SLOT)*BLOCK_ZOOM; }
}