package com.wraithavens.conquest.SinglePlayer.Entities;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;

public class StaticEntity extends Entity{
	public final Vector3f position = new Vector3f();
	public float scale = 1f/20;
	public StaticEntity(EntityType type){
		super(type);
	}
	@Override
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(position.x, position.y, position.z);
		GL11.glScalef(scale, scale, scale);
		mesh.drawStatic();
		GL11.glPopMatrix();
	}
}
