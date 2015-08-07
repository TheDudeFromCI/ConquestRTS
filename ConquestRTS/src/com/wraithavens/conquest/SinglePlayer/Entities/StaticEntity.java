package com.wraithavens.conquest.SinglePlayer.Entities;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;

public class StaticEntity extends Entity{
	// public final Matrix4f transform = new Matrix4f();
	public final Vector3f position = new Vector3f();
	public StaticEntity(EntityType type){
		super(type);
	}
	@Override
	public void render(){
		GL11.glPushMatrix();
		// GL11.glMultMatrixf(transform.read());
		GL11.glTranslatef(position.x, position.y, position.z);
		mesh.drawStatic();
		GL11.glPopMatrix();
	}
}
