package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;

public class Camera{
	private static float cameraMoveSpeed = 10.0f;
	private final Frustum frustum = new Frustum();
	private final CameraTargetBlock targetBlock;
	private float goalX, goalY, goalZ;
	private volatile float x;
	private volatile float y;
	private volatile float z;
	private float rx;
	private float ry;
	private final Vector3f position = new Vector3f();
	private final Vector3f direction = new Vector3f();
	public Camera(){
		targetBlock = new CameraTargetBlock(this);
	}
	public boolean boxInView(float[] e){
		return frustum.boxInFrustum(e);
	}
	public boolean cubeInView(float x, float y, float z, float size){
		return frustum.cubeInFrustum(x, y, z, size);
	}
	public double distanceSquared(double x, double y, double z){
		x -= this.x;
		y -= this.y;
		z -= this.z;
		return x*x+y*y+z*z;
	}
	public int getBlockX(){
		return (int)Math.floor(x);
	}
	public int getBlockY(){
		return (int)Math.floor(y);
	}
	public int getBlockZ(){
		return (int)Math.floor(z);
	}
	public Vector3f getDirection(){
		direction.x = (float)(Math.cos(Math.toRadians(ry-90))*Math.cos(Math.toRadians(-rx)));
		direction.y = (float)Math.sin(Math.toRadians(-rx));
		direction.z = (float)(Math.sin(Math.toRadians(ry-90))*Math.cos(Math.toRadians(-rx)));
		return direction;
	}
	public int getGoalBlockX(){
		return (int)Math.floor(goalX);
	}
	public int getGoalBlockY(){
		return (int)Math.floor(goalY);
	}
	public int getGoalBlockZ(){
		return (int)Math.floor(goalZ);
	}
	public float getGoalX(){
		return goalX;
	}
	public float getGoalY(){
		return goalY;
	}
	public float getGoalZ(){
		return goalZ;
	}
	public Vector3f getPosition(){
		position.x = x;
		position.y = y;
		position.z = z;
		return position;
	}
	public float getRX(){
		return rx;
	}
	public float getRY(){
		return ry;
	}
	public CameraTargetBlockCallback getTargetBlock(int range){
		return targetBlock.getTargetBlock(range);
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public float getZ(){
		return z;
	}
	public void moveTo(float x, float y, float z){
		goalX = x;
		goalY = y;
		goalZ = z;
	}
	public void teleport(float x, float y, float z){
		goalX = this.x = x;
		goalY = this.y = y;
		goalZ = this.z = z;
	}
	public void turnTo(float rx, float ry){
		this.rx = rx;
		this.ry = ry;
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
