package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import org.lwjgl.opengl.GL11;

public class Camera{
	public float cameraMoveSpeed = 1;
	private final Frustum frustum = new Frustum();
	public float goalX, goalY, goalZ;
	public volatile float x;
	public volatile float y;
	public volatile float z;
	public float rx;
	public float ry;
	public Frustum getFrustum(){
		return frustum;
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
		GL11.glTranslatef(-x, -y, -z);
	}
}
