package com.wraithavens.conquest.SinglePlayer.Entities;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Matrix4f;

public class StaticEntity extends Entity{
	public final Matrix4f transform = new Matrix4f();
	public StaticEntity(EntityType type){
		super(type);
	}
	@Override
	public void render(){
		GL11.glPushMatrix();
		GL11.glMultMatrixf(transform.read());
		mesh.drawStatic();
		GL11.glPopMatrix();
	}
}
