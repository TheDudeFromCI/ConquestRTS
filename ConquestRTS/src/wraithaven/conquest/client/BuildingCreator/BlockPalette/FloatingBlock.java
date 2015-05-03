package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import org.lwjgl.opengl.GL11;
import wraith.library.LWJGL.CubeTextures;

public class FloatingBlock{
	float shiftRX, shiftRY;
	float x, y, z;
	CubeTextures cubeTextures;
	private float rx, ry, tempY;
	private static final float BLOCK_BOB_SPEED = 0.6f;
	private static final float BLOCK_BOB_DISTANCE = 8;
	private static final float BLOCK_HOVER_SPEED = 0.25f;
	private static final float BLOCK_HOVER_DISTANCE = 0.1f;
	public void render(){
		GL11.glTranslatef(x, y+tempY, z);
		GL11.glRotatef(rx+shiftRX, 1, 0, 0);
		GL11.glRotatef(ry+shiftRY, 0, 1, 0);
		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
	}
	public void update(double time){
		rx=(float)Math.cos(time*BLOCK_BOB_SPEED)*BLOCK_BOB_DISTANCE+30;
		ry=(float)Math.sin(time*BLOCK_BOB_SPEED)*BLOCK_BOB_DISTANCE+45;
		tempY=(float)Math.sin(time*BLOCK_HOVER_SPEED*0.9f)*BLOCK_HOVER_DISTANCE;
	}
}