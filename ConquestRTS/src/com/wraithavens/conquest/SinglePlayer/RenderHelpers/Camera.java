package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;

public class Camera{
	public float cameraMoveSpeed = 1;
	public final Frustum frustum = new Frustum();
	public float goalX, goalY, goalZ;
	public float x;
	public float y;
	public float z;
	public float rx;
	public float ry;
	public float rz;
	public float sx = 1;
	public float sy = 1;
	public float sz = 1;
	public Vector3f getDirection(Vector3f direction){
		direction.x = (float)(Math.cos(Math.toRadians(ry-90))*Math.cos(Math.toRadians(-rx)));
		direction.y = (float)Math.sin(Math.toRadians(-rx));
		direction.z = (float)(Math.sin(Math.toRadians(ry-90))*Math.cos(Math.toRadians(-rx)));
		return direction;
	}
	public void update(double delta){
		// ---
		// The clamping is important to prevent the camera from "bouncing" or
		// going past it's goal during lag.
		// ---
		double shift = Math.min(delta*cameraMoveSpeed, 1.0);
		x = (float)(x*(1f-shift)+goalX*shift);
		y = (float)(y*(1f-shift)+goalY*shift);
		z = (float)(z*(1f-shift)+goalZ*shift);
		translateInvertMatrix();
		frustum.calculateFrustum();
	}
	private void translateInvertMatrix(){
		GL11.glRotatef(rx, 1, 0, 0);
		GL11.glRotatef(ry, 0, 1, 0);
		GL11.glRotatef(rz, 0, 0, 1);
		GL11.glTranslatef(-x, -y, -z);
		GL11.glScalef(sx, sy, sz);
	}
}
